package com.pdp.restaurant.controller.user;

import com.pdp.restaurant.entity.PdpReservation;
import com.pdp.restaurant.repository.PdpReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PdpReservationController {

    @Autowired
    private PdpReservationRepository reservationRepository;

    private final int MAX_CAPACITY = 50;

    // ================= KHÁCH HÀNG =================

    @GetMapping("/reservations/new")
    public String showBookingForm(Model model) {
        if (!model.containsAttribute("res")) {
            model.addAttribute("res", new PdpReservation());
        }
        return "reservation-form";
    }

    @PostMapping("/reservations/save")
    public String saveBooking(@ModelAttribute("res") PdpReservation res, RedirectAttributes redirectAttributes) {
        if (LocalDate.parse(res.getReservationDate()).isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn ngày từ hôm nay trở đi.");
            return "redirect:/reservations/new";
        }

        int currentBooked = reservationRepository.findAll().stream()
                .filter(r -> r.getReservationDate().equals(res.getReservationDate())
                        && !"ĐÃ HỦY".equals(r.getStatus()))
                .mapToInt(PdpReservation::getNumberOfPeople)
                .sum();

        if (currentBooked + res.getNumberOfPeople() > MAX_CAPACITY) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Rất tiếc! Ngày " + res.getReservationDate() + " hiện chỉ còn " + (MAX_CAPACITY - currentBooked) + " chỗ trống.");
            redirectAttributes.addFlashAttribute("res", res);
            return "redirect:/reservations/new";
        }

        res.setStatus("ĐANG CHỜ");
        PdpReservation savedRes = reservationRepository.save(res);

        redirectAttributes.addFlashAttribute("success", true);
        redirectAttributes.addFlashAttribute("latestRes", savedRes);

        // SỬA TẠI ĐÂY: Thêm tham số phone vào URL để trang success tự load dữ liệu ngay lập tức
        return "redirect:/reservations/success?phone=" + res.getPhone();
    }

    @GetMapping("/reservations/success")
    public String success(@RequestParam(name = "phone", required = false) String phone, Model model) {
        // SỬA TẠI ĐÂY: Đảm bảo logic tra cứu hoạt động khi nhận tham số từ URL hoặc Form
        if (phone != null && !phone.isEmpty()) {
            List<PdpReservation> searchResults = reservationRepository.findAll().stream()
                    .filter(r -> r.getPhone().equals(phone))
                    .sorted((a, b) -> b.getId().compareTo(a.getId()))
                    .collect(Collectors.toList());

            model.addAttribute("searchResults", searchResults);
            model.addAttribute("searchedPhone", phone);
        }
        return "reservation-success";
    }

    // ================= TRA CỨU PHẢN HỒI (CHO KHÁCH TẠI TRANG CHỦ) =================

    @GetMapping("/check-reservation")
    public String checkReservation(@RequestParam(name = "phone", required = false) String phone, Model model) {
        // SỬA TẠI ĐÂY: Điều hướng khách về trang success thay vì index để hiện kết quả chuyên nghiệp hơn
        if (phone != null && !phone.isEmpty()) {
            return "redirect:/reservations/success?phone=" + phone;
        }
        return "redirect:/";
    }

    // ================= QUẢN TRỊ (ADMIN) =================

    @GetMapping("/admin/reservations")
    public String adminList(Model model) {
        String today = LocalDate.now().toString();
        int bookedToday = reservationRepository.findAll().stream()
                .filter(r -> r.getReservationDate().equals(today) && !"ĐÃ HỦY".equals(r.getStatus()))
                .mapToInt(PdpReservation::getNumberOfPeople).sum();

        model.addAttribute("reservations", reservationRepository.findAll());
        model.addAttribute("occupancy", bookedToday + "/" + MAX_CAPACITY);
        return "admin/reservations/list";
    }

    @GetMapping("/admin/reservations/confirm/{id}")
    public String confirmBooking(@PathVariable Long id) {
        PdpReservation res = reservationRepository.findById(id).orElse(null);
        if (res != null) {
            res.setStatus("ĐÃ XÁC NHẬN");
            reservationRepository.save(res);
        }
        return "redirect:/admin/reservations";
    }

    @GetMapping("/admin/reservations/cancel/{id}")
    public String cancelBooking(@PathVariable Long id) {
        PdpReservation res = reservationRepository.findById(id).orElse(null);
        if (res != null) {
            res.setStatus("ĐÃ HỦY");
            reservationRepository.save(res);
        }
        return "redirect:/admin/reservations";
    }

    @GetMapping("/admin/reservations/delete/{id}")
    public String deleteBooking(@PathVariable Long id) {
        reservationRepository.deleteById(id);
        return "redirect:/admin/reservations";
    }
}