package com.miracle.miraclemorningback.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequestDto {
    private String memberName; // 사용자명
    private String password; // 비밀번호
    
	public MemberRequestDto(String memberName, String password) {
		super();
		this.memberName = memberName;
		this.password = password;
	}
}