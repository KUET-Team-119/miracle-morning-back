package com.miracle.miraclemorningback.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

        @Autowired
        private JwtTokenProvider jwtTokenProvider;

        @Autowired
        private CorsConfigurationSource corsConfigurationSource;

        @Bean
        protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(Customizer -> Customizer.disable())
                                .cors(Customizer -> Customizer.configurationSource(corsConfigurationSource))
                                .httpBasic(Customizer -> Customizer.disable()) // UI를 사용하는 것을 기본값으로 가진 시큐리티 설정을 비활성화
                                .sessionManagement( // 세션 사용 안함, STATELESS로 설정
                                                sessionManagement -> sessionManagement
                                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers("/admin").hasRole("ADMIN") // 관리자만 관리자 리소스에 접근 가능
                                                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll() // 회원가입,
                                                                                                              // 로그인은 모두
                                                                                                              // 허용
                                                .requestMatchers("/").permitAll()
                                                .anyRequest().hasRole("USER")) // 그 외에 리소스는 모두 회원만 사용 가능
                                .exceptionHandling(Customizer -> Customizer
                                                // 인증 과정에서 발생하는 예외 처리
                                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                                                // 권한 확인 과정에서 발생하는 예외 처리
                                                .accessDeniedHandler(new JwtAccessDeniedHandler()))
                                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        // 암호화에 필요한 PasswordEncoder 생략
}
