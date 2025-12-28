package com.pdp.restaurant.security;

import com.pdp.restaurant.entity.PdpUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class PdpUserDetails implements UserDetails {

    private final PdpUser user;

    public PdpUserDetails(PdpUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Username nội bộ của Spring Security
     * → dùng email cho ổn định
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // Dùng trong controller / thymeleaf nếu cần
    public PdpUser getUser() {
        return this.user;
    }
}
