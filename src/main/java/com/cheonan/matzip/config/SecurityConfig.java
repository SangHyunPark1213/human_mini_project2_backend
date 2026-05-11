package com.cheonan.matzip.config;

import com.cheonan.matzip.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ① CSRF 비활성화 (REST API + React 환경)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")  // API는 CSRF 제외
                )

                // ② CORS 설정 (React ↔ Backend 통신 허용)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ③ 경로별 접근 제어
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/members/join", "/api/members/login").permitAll()
                        .requestMatchers("/admin/login").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // ADMIN만 접근
                        .anyRequest().permitAll()
                )

                // ④ Admin용 formLogin (Thymeleaf SSR)
                .formLogin(form -> form
                        .loginPage("/admin/login")           // 커스텀 로그인 페이지
                        .loginProcessingUrl("/admin/login")  // POST 처리 URL
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .failureUrl("/admin/login?error=true")
                        .permitAll()
                )

                // ⑤ 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )

                // ⑥ 세션 관리
                .sessionManagement(session -> session
                        .maximumSessions(1)          // 동시 로그인 1개 제한
                        .maxSessionsPreventsLogin(false) // 새 로그인 시 기존 세션 만료
                );

        return http.build();
    }

    // ⑦ CORS 세부 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // React 주소
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // 세션 쿠키 허용 (필수)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // ⑧ AuthenticationManager (CustomUserDetailsService 연결)
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http)
            throws Exception {
        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
        return builder.build();
    }
}