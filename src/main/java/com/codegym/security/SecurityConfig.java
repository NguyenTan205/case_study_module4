package com.codegym.security;

import com.codegym.handler.CustomSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomSuccessHandler customSuccessHandler() {
        return new CustomSuccessHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // ✅ Public
                        .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll()

                        // ✅ Trang admin
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")

                        // ✅ Trang teacher
                        .requestMatchers("/teacher/**").hasAnyAuthority("ROLE_TEACHER", "ROLE_ADMIN")

                        // ✅ Trang student: cho cả 3 role được xem
                        .requestMatchers("/students/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT")

                        // ✅ Các trang còn lại yêu cầu login
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(customSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/accessDenied"));

        return http.build();
    }
}
