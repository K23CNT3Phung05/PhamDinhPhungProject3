package com.pdp.restaurant.service;

import com.pdp.restaurant.entity.PdpUser;
import com.pdp.restaurant.repository.PdpUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PdpUserService {

    private final PdpUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ======================
    // BASIC CRUD
    // ======================

    public List<PdpUser> getAllUsers() {
        return userRepository.findAll();
    }

    public PdpUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Optional<PdpUser> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<PdpUser> getUserByFullName(String fullName) {
        return userRepository.findByFullName(fullName);
    }

    // ======================
    // CHECK EXIST
    // ======================

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByFullName(String fullName) {
        return userRepository.existsByFullName(fullName);
    }

    // ======================
    // REGISTER
    // ======================

    public PdpUser registerUser(PdpUser user) {

        if (existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        if (existsByFullName(user.getFullName())) {
            throw new RuntimeException("Tên người dùng đã tồn tại!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Mặc định role USER nếu chưa set
        if (user.getRole() == null) {
            user.setRole(PdpUser.Role.USER);
        }

        return userRepository.save(user);
    }

    // ======================
    // UPDATE
    // ======================

    public PdpUser updateUser(PdpUser user) {
        PdpUser existingUser = getUserById(user.getId());

        existingUser.setFullName(user.getFullName());
        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ======================
    // LOGIN (EMAIL OR FULLNAME)
    // ======================

    public PdpUser login(String input, String rawPassword) {
        // Tạo biến final để lambda không lỗi
        final String trimmedInput = input.trim();

        PdpUser user = userRepository.findByEmail(trimmedInput)
                .or(() -> userRepository.findByFullName(trimmedInput))
                .orElseThrow(() ->
                        new RuntimeException("Email hoặc tên đăng nhập không tồn tại!")
                );

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu không đúng!");
        }

        return user;
    }

    // ======================
    // ALIAS (CHO CONTROLLER / SECURITY)
    // ======================

    public PdpUser authenticate(String input, String password) {
        return login(input, password);
    }

    public PdpUser register(PdpUser user) {
        return registerUser(user);
    }
}
