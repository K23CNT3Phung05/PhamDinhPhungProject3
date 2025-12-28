package com.pdp.restaurant.config;

import com.pdp.restaurant.entity.PdpUser;
import com.pdp.restaurant.repository.PdpUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PdpDataInitializer implements CommandLineRunner {

    private final PdpUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Chỉ tạo khi database chưa có user
        if (userRepo.count() == 0) {

            // Tạo Admin mặc định
            PdpUser admin = PdpUser.builder()
                    .fullName("Admin")                  // Tên đầy đủ
                    .email("P@gmail.com")                  // Email đăng nhập
                    .password(passwordEncoder.encode("p")) // Mật khẩu đã mã hóa
                    .role(PdpUser.Role.ADMIN)                     // Role ADMIN
                    .build();

            // Tạo Customer mặc định
            PdpUser customer = PdpUser.builder()
                    .fullName("Default Customer")
                    .email("customer@example.com")
                    .password(passwordEncoder.encode("customer1"))
                    .role(PdpUser.Role.USER)                      // Role USER
                    .build();

            // Lưu vào DB
            userRepo.save(admin);
            userRepo.save(customer);

            System.out.println("=== Admin & Customer mặc định đã được tạo ===");
        }
    }
}
