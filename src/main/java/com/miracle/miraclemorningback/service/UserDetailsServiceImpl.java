package com.miracle.miraclemorningback.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.miracle.miraclemorningback.entity.MemberEntity;
import com.miracle.miraclemorningback.entity.UserDetailsImpl;
import com.miracle.miraclemorningback.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberName) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepository.findByMemberName(memberName)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return UserDetailsImpl.builder()
                .memberEntity(memberEntity)
                .memberName(memberEntity.getMemberName())
                .build();
    }
}
