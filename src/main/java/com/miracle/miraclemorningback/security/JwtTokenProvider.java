package com.miracle.miraclemorningback.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.miracle.miraclemorningback.entity.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import javax.crypto.SecretKey;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider { // JWT 토큰 생성 및 검증

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKeyPlain;

    @Value("${jwt.issuer}")
    private String issuer;

    private SecretKey secretKey;
    private Long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 30; // 30분 동안 토큰 유효

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyPlain.getBytes());
    }

    // Jwt 토큰 생성
    public String generateToken(String memberName, Role roles) {
        Date now = new Date();
        String accessToken = Jwts.builder().claims().issuer(issuer).subject(memberName).issuedAt(now)
                .expiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME)).add("roles", roles).and()
                .signWith(secretKey).compact();
        return accessToken;
    }

    // Jwt 토큰으로 인증 정보 조회
    // 필터에서 인증 성공 시 SecurityContextHolder에 저장할 Authentication 생성
    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getMemberName(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰에서 회원 구별 정보(닉네임) 추출
    public String getMemberName(String accessToken) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(accessToken).getPayload().getSubject();
    }

    // Request Header에서 token 값 추출 : "X-AUTH-TOKEN: jwt토큰"
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰 부분을 추출
        }
        return null;
    }

    // Jwt 토큰의 유효 기간 만료 검사
    public Boolean validateToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(accessToken);
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
