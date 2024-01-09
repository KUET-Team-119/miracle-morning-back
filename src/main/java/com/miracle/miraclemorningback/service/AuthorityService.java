package com.miracle.miraclemorningback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miracle.miraclemorningback.dto.AuthorityDeleteSuccessResponseDto;
import com.miracle.miraclemorningback.dto.AuthorityRequestDto;
import com.miracle.miraclemorningback.dto.AuthorityResponseDto;
import com.miracle.miraclemorningback.entity.AuthorityEntity;
import com.miracle.miraclemorningback.repository.AuthorityRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorityService {
	@Autowired
	AuthorityRepository authorityRepository;
	
	@Transactional
	public AuthorityResponseDto addAuthority(AuthorityRequestDto requestDto) {
		AuthorityEntity authorityEntity = new AuthorityEntity(requestDto);
		authorityRepository.save(authorityEntity);
		return new AuthorityResponseDto(authorityEntity);
	}
	
	@Transactional
	public AuthorityDeleteSuccessResponseDto deleteAuthority(Long authority_id) throws Exception {
		authorityRepository.findById(authority_id).orElseThrow(
				()-> new IllegalArgumentException("존재하지 않는 권한입니다."));
		authorityRepository.deleteById(authority_id);
		return new AuthorityDeleteSuccessResponseDto(true);
	}
	
	@Transactional
	public List<AuthorityResponseDto> getAuthority(Long member_id) {
		List<AuthorityResponseDto> authority_list = authorityRepository.findById(member_id).stream().map(AuthorityResponseDto::new).toList();
		if(authority_list == null) {
			new IllegalArgumentException("존재하지 않는 사용자입니다.");
			return null;
		}
		else {
			return authority_list;
		}
	}
	
	@Transactional
	public boolean haveAuthority(Long member_id, String authority_name) {
		List<AuthorityResponseDto> authority_list = authorityRepository.findById(member_id).stream().map(AuthorityResponseDto::new).toList();
		for(int i=0;i<authority_list.size();i++) {
			if(authority_list.get(i).getAuthority_name().equals(authority_name))
				return true;
		}
		
		return false;
	}
}
