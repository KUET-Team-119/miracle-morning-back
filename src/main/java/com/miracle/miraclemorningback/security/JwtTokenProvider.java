package com.miracle.miraclemorningback.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.miracle.miraclemorningback.entity.Role;
import com.miracle.miraclemorningback.service.UserDetailsServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider { // JWT 토큰 생성 및 검증

    private final UserDetailsServiceImpl userDetailsService;
    private final Long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 20; // 20초 동안 토큰 유효
    private final Integer VALID_STATE = 0;
    private final Integer MANIPULATED_STATE = 1;
    private final Integer EXPIRED_STATE = 2;
    private final Integer UNSUPPORTED_STATE = 3;
    private final Integer WRONG_STATE = 4;
    private final Integer OTHERS_STATE = 5;

    @Value("${jwt.secret}")
    private String secretKeyPlain;

    @Value("${jwt.issuer}")
    private String issuer;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyPlain.getBytes());
    }

    // Jwt accessToken 생성
    public String generateAccessToken(Long memberId, String memberName, Role roles) {
        Date now = new Date();
        String accessToken = Jwts.builder().claims().issuer(issuer).subject(memberName).issuedAt(now)
                .expiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .add("id", memberId)
                .add("roles", roles).and()
                .signWith(secretKey).compact();
        return accessToken;
    }

    // Jwt refreshToken 생성
    public String generateRefreshToken() {
        String refreshToken = UUID.randomUUID().toString();

        return refreshToken;
    }

    // Jwt 토큰으로 인증 정보 조회
    // 필터에서 인증 성공 시 SecurityContextHolder에 저장할 인증 객체 Authentication 생성
    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getMemberName(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰에서 회원 구별 정보(닉네임) 추출
    public String getMemberName(String accessToken) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(accessToken).getPayload().getSubject();
    }

    // Request Header에서 token 값 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰 부분을 추출
        }
        return null;
    }

    // Jwt 토큰의 유효성 검사
    public Integer validateToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(accessToken);
            if (!claims.getPayload().getExpiration().before(new Date())) {
                return VALID_STATE;
            }
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            return MANIPULATED_STATE;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return EXPIRED_STATE;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            return UNSUPPORTED_STATE;
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            return WRONG_STATE;
        }
        return OTHERS_STATE;
    }
}
