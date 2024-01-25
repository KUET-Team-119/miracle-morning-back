package com.miracle.miraclemorningback.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.repository.MemberRepository;

@Service
public class AccountService implements UserDetailsService{
	
	@Autowired
	MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String memberName, password;
        List<GrantedAuthority> authorities;
		List<MemberEntity> memberList = memberRepository.getMemberInformation(username);
		MemberEntity member;
		
		if (memberList.size() > 0) {
			member = memberList.get(0);
			memberName = member.getMemberName();
			password = member.getPassword();
			System.out.println(memberName);
			authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(member.getRole().getAuthority()));
		} else {
			throw new UsernameNotFoundException("Not Found User");
		}
		return new User(memberName, password, authorities);
	}

}
