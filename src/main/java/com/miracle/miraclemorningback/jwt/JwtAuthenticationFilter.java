package com.miracle.miraclemorningback.jwt;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.miracle.miraclemorningback.Util.CookieUtil;
import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.entity.RefreshTokenEntity;
import com.miracle.miraclemorningback.repository.MemberRepository;
import com.miracle.miraclemorningback.repository.RefreshTokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.web.util.WebUtils;
import java.io.IOException;
import jakarta.servlet.http.Cookie;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Integer VALID_STATE = 0;
    private final Integer EXPIRED_STATE = 2;

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    private static final String[] excludeURI = { "/", "/api/auth/**" };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return PatternMatchUtils.simpleMatch(excludeURI, requestURI);
    }

    // Request로 들어오는 JWT Token의 유효성을 검증하는 filter를 filterChan에 등록
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {

        // resolveToken: Request의 Header에서 token 값 추출
        String token = jwtTokenProvider.resolveToken(request);

        try { // validateToken: Jwt 토큰의 유효성 검사
            if (token != null && jwtTokenProvider.validateToken(token) == VALID_STATE) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                // 유효한 토큰이면 SecurityContextHolder에 추가
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if (token != null && jwtTokenProvider.validateToken(token) == EXPIRED_STATE) {
                System.out.println("클라이언트 accessToken: " + token);
                // 만료된 accessToken으로 저장된 refreshTokenEntity 찾기
                RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByAccessToken(token)
                        .orElseThrow(() -> new IllegalArgumentException("토큰을 찾을 수 없습니다."));

                String refreshToken = refreshTokenEntity.getRefreshToken();

                // HttpServletRequest에서 refreshToken 쿠키 가져오기
                Cookie cookie = WebUtils.getCookie(request, "refreshToken");
                // 쿠키에서 refreshToken 값 가져오기
                String clientRefreshToken = cookie != null ? cookie.getValue() : null;

                System.out.println("클라이언트 refreshToken: " + clientRefreshToken);
                System.out.println("서버 refreshToken: " + refreshToken);

                if (clientRefreshToken == null || !refreshToken.equals(clientRefreshToken)) {
                    throw new IllegalArgumentException("유효하지 않은 refreshToken");
                }

                // refreshTokenEntity의 memberId를 사용하여 새로운 accessToken 생성
                Long memberId = refreshTokenEntity.getMemberId();
                MemberEntity memberEntity = memberRepository.findById(memberId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
                String newAccessToken = jwtTokenProvider.generateAccessToken(
                        memberId, memberEntity.getMemberName(), memberEntity.getRole());

                // 새로 생성된 accessToken을 응답 헤더에 추가
                response.setHeader("Authorization", "Bearer " + newAccessToken);

                // 새로운 refreshToken 생성
                String newRefreshToken = jwtTokenProvider.generateRefreshToken();

                refreshTokenEntity = RefreshTokenEntity.builder()
                        .memberId(memberEntity.getMemberId())
                        .refreshToken(newRefreshToken)
                        .accessToken(newAccessToken)
                        .build();
                refreshTokenRepository.save(refreshTokenEntity);

                // 기존 쿠키 제거 및 새로운 쿠키 생성
                // CookieUtil.deleteRefreshTokenCookie(response, cookie);
                CookieUtil.generateRefreshTokenCookie(response, newRefreshToken);
            }
        } catch (RedisConnectionFailureException e) {
            SecurityContextHolder.clearContext();
            throw new IllegalArgumentException("REDIS_ERROR");
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        filterChain.doFilter(request, response);
    }
}
