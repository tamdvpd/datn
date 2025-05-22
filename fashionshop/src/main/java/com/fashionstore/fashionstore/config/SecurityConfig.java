package com.fashionstore.fashionstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .anyRequest().permitAll()  // Cho phép tất cả các request mà không cần xác thực
            .and()
            .csrf().disable();  // Tắt CSRF protection (nếu cần thiết trong môi trường công khai)

        return http.build();
    }
}
