package com.miracle.miraclemorningback.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.miracle.miraclemorningback.constants.SecurityConstants;
import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.repository.MemberRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

// JWT 토큰 유효성 인증 필터
@RequiredArgsConstructor
@Component
public class JWTTokenValidator {
	
	@Autowired
	MemberRepository memberRepository;
	
	public Boolean validateToken(String token) {
		Boolean isValid = false;
		try {
			String memberName = getMemberNameFromToken(token);
			Optional<MemberEntity> memberEntity = memberRepository.findByMemberName(memberName);
			if(!memberEntity.isEmpty()) {
				if(!isTokenExpired(token))
					isValid = true;
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return isValid;
	}

	private boolean isTokenExpired(String token) {
		Date expiration = getExpirationFromToken(token);
		return expiration.before(new Date());
	}

	private Date getExpirationFromToken(String token) {
		Date expiration = null;
		try {
			Claims claims = getClaimsFromToken(token);
			if (claims != null) {
				if (claims.getExpiration() != null)
					expiration = claims.getExpiration();
			} else {
				throw new Exception("claim is null");
			}
		}catch(Exception e) {
			e.getMessage();
		}
		return expiration;
	}

	public String getMemberNameFromToken(String token) throws Exception {
		String memberName = null;
		try {
			Claims claims = getClaimsFromToken(token);
			if (claims != null) {
				if (claims.getId() != null)
					memberName = claims.getId();
				if (claims.getSubject() != null)
					memberName = claims.getSubject();
			} else {
				throw new Exception("claim is null");
			}
		}catch(Exception e) {
			e.getMessage();
		}
		return memberName;
	}

	private Claims getClaimsFromToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(
				SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
		Claims claims = null;
		try {
			claims = Jwts.parser()
						 .verifyWith(key)
						 .build()
						 .parseSignedClaims(token)
						 .getPayload();
		}catch(Exception e) {
			e.getMessage();
		}
		return claims;
	}
	
}