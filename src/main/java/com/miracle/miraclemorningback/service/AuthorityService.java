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

	// 권한 추가
	@Transactional
	public AuthorityResponseDto addAuthority(AuthorityRequestDto requestDto) {
		AuthorityEntity authorityEntity = new AuthorityEntity(requestDto);
		authorityRepository.save(authorityEntity);
		return new AuthorityResponseDto(authorityEntity);
	}

	// 권한 삭제
	@Transactional
	public AuthorityDeleteSuccessResponseDto deleteAuthority(Long authorityId) throws Exception {
		authorityRepository.findById(authorityId).orElseThrow(
				() -> new IllegalArgumentException("존재하지 않은 권한입니다."));
		authorityRepository.deleteById(authorityId);
		return new AuthorityDeleteSuccessResponseDto(true);
	}

	// 특정 사용자 권한 조회(?)
	@Transactional
	public List<AuthorityResponseDto> getAuthorities(Long authorityId) {
		List<AuthorityResponseDto> authorityList = authorityRepository.findById(authorityId).stream()
				.map(AuthorityResponseDto::new).toList();
		if (authorityList == null) {
			new IllegalArgumentException("존재하지 않은 사용자입니다.");
			return null;
		} else {
			return authorityList;
		}
	}

	// 사용자 권한 확인
	@Transactional
	public boolean checkAuthority(Long memberId, String authorityName) {
		List<AuthorityResponseDto> authorityList = authorityRepository.findById(memberId).stream()
				.map(AuthorityResponseDto::new).toList();
		for (int i = 0; i < authorityList.size(); i++) {
			if (authorityList.get(i).getAuthorityName().equals(authorityName))
				return true;
		}

		return false;
	}
}
