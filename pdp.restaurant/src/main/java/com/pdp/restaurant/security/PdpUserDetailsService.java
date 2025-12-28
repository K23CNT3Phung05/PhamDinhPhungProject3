package com.pdp.restaurant.security;

import com.pdp.restaurant.entity.PdpUser;
import com.pdp.restaurant.repository.PdpUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PdpUserDetailsService implements UserDetailsService {

    private final PdpUserRepository userRepository;

    /**
     * input = thứ người dùng nhập ở ô login
     * → có thể là EMAIL hoặc FULL NAME
     */
    @Override
    public UserDetails loadUserByUsername(String input)
            throws UsernameNotFoundException {

        PdpUser user = userRepository.findByEmail(input)
                .or(() -> userRepository.findByFullName(input))
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Không tìm thấy tài khoản với email hoặc tên: " + input
                        )
                );

        return new PdpUserDetails(user);
    }
}
