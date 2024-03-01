package com.miracle.miraclemorningback.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.miracle.miraclemorningback.jwt.JwtAccessDeniedHandler;
import com.miracle.miraclemorningback.jwt.JwtAuthenticationEntryPoint;
import com.miracle.miraclemorningback.jwt.JwtAuthenticationFilter;
import com.miracle.miraclemorningback.jwt.JwtTokenProvider;
import com.miracle.miraclemorningback.repository.MemberRepository;
import com.miracle.miraclemorningback.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

        private final JwtTokenProvider jwtTokenProvider;

        private final RefreshTokenRepository refreshTokenRepository;

        private final MemberRepository memberRepository;

        private final CorsConfig corsConfig;

        @Bean
        protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(Customizer -> Customizer.disable())
                                .cors(Customizer -> Customizer
                                                .configurationSource(corsConfig.corsConfigurationSource()))
                                .httpBasic(Customizer -> Customizer.disable()) // UI를 사용하는 것을 기본값으로 가진 시큐리티 설정을 비활성화
                                .sessionManagement( // 세션 사용 안함, STATELESS로 설정
                                                sessionManagement -> sessionManagement
                                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll() // 회원가입,
                                                // 로그인은 모두
                                                // 허용
                                                .requestMatchers("/").permitAll()
                                                .requestMatchers("/api/admin/**").hasRole("ADMIN") // 관리자만 관리자 리소스 사용
                                                .anyRequest().hasAnyRole("USER", "ADMIN")) // 그 외에 리소스는 관리자, 회원만 사용
                                .exceptionHandling(Customizer -> Customizer
                                                // 인증 과정에서 발생하는 예외 처리
                                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                                                // 권한 확인 과정에서 발생하는 예외 처리
                                                .accessDeniedHandler(new JwtAccessDeniedHandler()))
                                .addFilterBefore(
                                                new JwtAuthenticationFilter(jwtTokenProvider, refreshTokenRepository,
                                                                memberRepository),
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
