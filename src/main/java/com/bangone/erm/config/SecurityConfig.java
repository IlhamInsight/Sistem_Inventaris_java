package com.bangone.erm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth

                // Public
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()

                // Hanya ADMIN
                .requestMatchers("/auth/**").hasAuthority("ADMIN")

                // Semua role login
                .requestMatchers("/", "/dashboard/**")
                    .hasAnyAuthority("ADMIN", "STAFF_GUDANG", "KARYAWAN")

                // ADMIN & STAFF GUDANG
                .requestMatchers(
                    "/inventory/products/**",
                    "/inventory/categories/**",
                    "/inventory/brands/**",
                    "/inventory/units/**",
                    "/inventory/locations/**",
                    "/inventory/warehouses/**",
                    "/inventory/suppliers/**",
                    "/assets/**",
                    "/loans/approvals/**",
                    "/loans/fines/**"
                ).hasAnyAuthority("ADMIN", "STAFF_GUDANG")

                // Semua role login
                .requestMatchers(
                    "/loans/requests/**",
                    "/loans/returns/**",
                    "/maintenance/logs/**"
                ).hasAnyAuthority("ADMIN", "STAFF_GUDANG", "KARYAWAN")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}