package com.miracle.miraclemorningback.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.miracle.miraclemorningback.constants.SecurityConstants;
import com.miracle.miraclemorningback.entity.Role;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

// JWT 토큰 생성 필터
@RequiredArgsConstructor
@Component
public class JWTTokenGenerator {
	
	private SecretKey key = Keys.hmacShaKeyFor(SecurityConstants
												.JWT_KEY
												.getBytes(StandardCharsets.UTF_8));

	public String createToken(String memberName, Role role) {
		String jwt = Jwts.builder()
						 .issuedAt(new Date())
						 .claim(SecurityConstants.CLAIM_KEY_MEMBERNAME, memberName)
						 .claim(SecurityConstants.CLAIM_KEY_ROLE, role)
		                 .expiration(new Date((new Date()).getTime() + SecurityConstants.EXPIRATION))
						 .signWith(key)
						 .compact();
		return jwt;
	}

}
