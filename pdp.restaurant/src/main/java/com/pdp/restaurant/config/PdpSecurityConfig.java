package com.pdp.restaurant.config;

import com.pdp.restaurant.security.PdpCustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class PdpSecurityConfig {

    private final PdpCustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // PUBLIC
                        .requestMatchers(
                                "/",                 // home
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // USER
                        .requestMatchers("/profile", "/orders/**")
                        .hasAnyRole("USER", "ADMIN")

                        // others
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")            // GET
                        .loginProcessingUrl("/login")   // POST
                        .usernameParameter("email")     // 🔥 QUAN TRỌNG
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", false)  // USER -> /
                        .successHandler((req, res, auth) -> {
                            boolean isAdmin = auth.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                            res.sendRedirect(isAdmin ? "/admin" : "/");
                        })
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
