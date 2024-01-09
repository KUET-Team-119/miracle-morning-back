package com.miracle.miraclemorningback.dto;

import com.miracle.miraclemorningback.entity.AuthorityEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthorityResponseDto {
	private Long authority_id;
    private String authority_name;

    public AuthorityResponseDto(AuthorityEntity authorityEntity) {
    	this.authority_id = authorityEntity.getAuthority_id();
        this.authority_name = authorityEntity.getAuthority_name();
    }
}
