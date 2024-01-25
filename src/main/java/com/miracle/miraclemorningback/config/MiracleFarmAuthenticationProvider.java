package com.miracle.miraclemorningback.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.repository.MemberRepository;

@Component
public class MiracleFarmAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String memberName = authentication.getName();
		String password = authentication.getCredentials().toString();
		String authority;
		List<MemberEntity> member = memberRepository.getMemberInformation(memberName);
		
		if(member.size() > 0) {
			if(password.equals(member.get(0).getPassword())) {
				List<GrantedAuthority> authorities = new ArrayList<>();
				authority = member.get(0).getRole().getAuthority();
				if(authority.equals("ROLE_ADMIN")) {
					authorities.add(new SimpleGrantedAuthority(authority));
					return new UsernamePasswordAuthenticationToken(memberName, password, authorities);
				}else {
					throw new BadCredentialsException("접근 권한이 없는 사용자입니다");
				}
			}else {
				throw new BadCredentialsException("비밀번호를 잘못 입력하셨습니다");
			}
		}else {
			throw new BadCredentialsException("사용자를 찾을 수 없습니다");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
