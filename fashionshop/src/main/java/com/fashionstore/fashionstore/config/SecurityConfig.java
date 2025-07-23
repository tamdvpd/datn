package com.fashionstore.fashionstore.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 👉 Cho phép CORS
                .cors(Customizer.withDefaults())

                // 👉 Tắt CSRF vì bạn đang làm API REST
                .csrf().disable()

                // 👉 Phân quyền endpoint
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/users/auth/**", // Cho phép login, register, google login...
                                "/users/register/**",
                                "/users/check-email",
                                "/users/test-email")
                        .permitAll()
                        .anyRequest().authenticated() // Những API khác cần JWT
                )

                // 👉 Xử lý lỗi không có quyền
                .exceptionHandling()
                .authenticationEntryPoint((req, res, ex) -> {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                })
                .and()

                // 👉 Không dùng session
                .sessionManagement()
                .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)

                // ✅ Thêm header COOP/COEP để tránh lỗi postMessage bị chặn
                .and()
                .headers()
                .addHeaderWriter((request, response) -> {
                    response.setHeader("Cross-Origin-Opener-Policy", "same-origin-allow-popups");
                    response.setHeader("Cross-Origin-Embedder-Policy", "require-corp");

                });

        return http.build();
    }

    // 👉 Mã hoá mật khẩu bằng BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 👉 Cấu hình CORS cho frontend ở cổng khác
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:3001")); // Frontend
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // Nếu bạn dùng cookie

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}