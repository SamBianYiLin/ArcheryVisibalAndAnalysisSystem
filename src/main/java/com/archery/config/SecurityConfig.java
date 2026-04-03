package com.archery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/login", "/admin/login", "/perform-login", "/auth/register", "/error").permitAll()
                        .requestMatchers("/athletes/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform-login")
                        .successHandler((request, response, authentication) -> handleLoginSuccess(request, response, authentication))
                        .failureHandler((request, response, exception) -> {
                            String portal = request.getParameter("portal");
                            if ("admin".equals(portal)) {
                                response.sendRedirect("/admin/login?error");
                                return;
                            }
                            response.sendRedirect("/login?error");
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());

        return http.build();
    }

    private void handleLoginSuccess(HttpServletRequest request,
                                    jakarta.servlet.http.HttpServletResponse response,
                                    Authentication authentication) throws IOException {
        String portal = request.getParameter("portal");
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        if ("admin".equals(portal) && isAdmin) {
            response.sendRedirect("/");
            return;
        }

        if ("athlete".equals(portal) && !isAdmin) {
            response.sendRedirect("/");
            return;
        }

        new SecurityContextLogoutHandler().logout(request, response, authentication);
        if (isAdmin) {
            response.sendRedirect("/login?error=portal");
            return;
        }
        response.sendRedirect("/admin/login?error=portal");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

