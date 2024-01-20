package com.miracle.miraclemorningback.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성 및 검증 클래스

    // Jwt Provider 주입
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Request로 들어오는 JWT Token의 유효성을 검증하는 filter를 filterChan에 등록
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)
            throws IOException, ServletException {
        // resolveToken: Request의 Header에서 token 값 추출
        String token = jwtTokenProvider.resolveToken(request);

        // validateToken: Jwt 토큰의 유효 기간 만료 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // getAuthentication : Jwt 토큰으로 인증 정보 조회
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // 유효한 토큰이면 SecurityContextHolder에 추가
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
