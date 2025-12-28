package com.pdp.restaurant.service;

import com.pdp.restaurant.entity.*;
import com.pdp.restaurant.repository.PdpDishRepository;
import com.pdp.restaurant.repository.PdpOrderRepository;
import com.pdp.restaurant.repository.PdpUserRepository;
import com.pdp.restaurant.repository.PdpWalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PdpOrderService {

    private final PdpOrderRepository orderRepository;
    private final PdpDishRepository dishRepository;
    private final PdpUserRepository userRepository;
    private final PdpWalletTransactionRepository walletTransactionRepository;

    // ================== FIND ALL ==================
    public List<PdpOrder> findAll() {
        return orderRepository.findAll();
    }

    // ================== FIND BY ID ==================
    public PdpOrder findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng ID: " + id));
    }

    // ================== FIND BY USER ==================
    public List<PdpOrder> findByUser(PdpUser user) {
        return orderRepository.findByUser(user);
    }

    // ================== UPDATE STATUS ==================
    @Transactional
    public void updateStatus(Long id, PdpOrderStatus status) {
        PdpOrder order = findById(id);
        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    // ================== DELETE ORDER ==================
    @Transactional
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    // ================== SAVE ORDER (THÊM VÀO ĐỂ FIX LỖI CONTROLLER) ==================
    @Transactional
    public void save(PdpOrder order) {
        orderRepository.save(order);
    }

    // ================== CHECKOUT ==================
    @Transactional
    public Long checkout(
            String name,
            String phone,
            String address,
            String note,
            BigDecimal ship,
            String username,
            Map<Long, PdpCartItem> cart
    ) {
        if (cart == null || cart.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống");
        }

        PdpOrder order = new PdpOrder();
        order.setCustomerName(name);
        order.setCustomerPhone(phone);
        order.setCustomerAddress(address);
        order.setShippingAddress(address);
        order.setNote(note);
        order.setShippingFee(ship != null ? ship : BigDecimal.ZERO);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderStatus(PdpOrderStatus.PENDING);
        order.setPaymentMethod(PdpPaymentMethod.CASH);

        if (username != null && !username.isEmpty()) {
            userRepository.findByEmail(username)
                    .ifPresent(order::setUser);
        }

        List<PdpOrderItem> orderItems = new ArrayList<>();
        BigDecimal totalItemsAmount = BigDecimal.ZERO;

        for (PdpCartItem cartItem : cart.values()) {
            PdpDish dish = dishRepository.findById(cartItem.getDishId())
                    .orElseThrow(() -> new RuntimeException("Món ăn không tồn tại"));

            PdpOrderItem item = new PdpOrderItem();
            item.setDish(dish);
            item.setDishName(dish.getName());
            item.setPrice(cartItem.getPrice());
            item.setQuantity(cartItem.getQuantity());
            item.setOrder(order);

            orderItems.add(item);

            BigDecimal subTotal =
                    cartItem.getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            totalItemsAmount = totalItemsAmount.add(subTotal);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalItemsAmount.add(order.getShippingFee()));

        orderRepository.save(order);

        // ==== Tạo giao dịch ví sau khi checkout ====
        if (order.getUser() != null) {
            createPayOrderTransaction(
                    order.getUser(),
                    order.getTotalAmount(),
                    order.getPaymentMethod(),
                    order.getId()
            );
        }

        return order.getId();
    }

    // ================== WALLET TRANSACTION ==================
    @Transactional
    public void createPayOrderTransaction(
            PdpUser user,
            BigDecimal amount,
            PdpPaymentMethod paymentMethod,
            Long orderId
    ) {
        PdpWalletTransaction tx = new PdpWalletTransaction();
        tx.setUser(user);
        tx.setTransactionType(PdpTransactionType.PAY_ORDER);
        tx.setPaymentMethod(paymentMethod);
        tx.setAmount(amount);
        tx.setDescription("Thanh toán đơn hàng #" + orderId);
        tx.setCreatedAt(LocalDateTime.now());

        walletTransactionRepository.save(tx);
    }
}