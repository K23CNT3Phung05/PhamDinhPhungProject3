package com.pdp.restaurant.security;

import com.pdp.restaurant.entity.PdpUser;
import com.pdp.restaurant.repository.PdpUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PdpCustomUserDetailsService implements UserDetailsService {

    private final PdpUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        PdpUser user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Email không tồn tại: " + email
                        )
                );

        return new User(
                user.getEmail(),                 // username = email
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name()
                ))
        );
    }
}
