package com.miracle.miraclemorningback.entity;

import com.miracle.miraclemorningback.dto.AuthorityRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "authority")
public class AuthorityEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long authorityId; // 권한 아이디

	@Column
	// @ManyToOne
	// @JoinColumn(name = "member_id")
	private Long memberId; // 사용자 아이디

	@Column
	private String authorityName; // 권한명

	public AuthorityEntity(AuthorityRequestDto requestDto) {
		this.authorityId = requestDto.getAuthorityId();
		this.memberId = requestDto.getMemberId();
	}

	public AuthorityEntity(String authorityName) {
		this.authorityName = authorityName;
	}
}