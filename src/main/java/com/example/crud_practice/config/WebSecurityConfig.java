package com.example.crud_practice.config;

import com.example.crud_practice.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailService userService;

    // 개발용: H2 콘솔과 정적 자원은 보안 필터 제외
    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
//                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }

    // Spring Security 6 이상: 람다 스타일 보안 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (API 서버일 경우 필요)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/signup", "/user").permitAll() // 인증 없이 접근 허용
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/login") // 사용자 정의 로그인 페이지
                        .defaultSuccessUrl("/") // 로그인 성공 시 이동
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // 로그아웃 성공 시 이동
                        .invalidateHttpSession(true) // 세션 무효화
                );

        return http.build();
    }

    // 인증 관리자 구성 (사용자 정보 + 비밀번호 인코더 설정)
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder())
                .and()
                .build();
    }

    // 비밀번호 암호화 방식 (BCrypt 사용)
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
