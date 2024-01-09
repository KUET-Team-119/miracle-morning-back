package com.miracle.miraclemorningback.entity;

import org.hibernate.annotations.DynamicInsert;

import com.miracle.miraclemorningback.dto.AuthorityRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
@Table(name = "authority")
public class AuthorityEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long authority_id; // 권한 아이디

	@Column
	// @ManyToOne
	// @JoinColumn(name = "member_id")
	private Long member_id; // 사용자 아이디

	@Column
	private String authority_name; // 권한명

	public AuthorityEntity(AuthorityRequestDto requestDto) {
		this.authority_id = requestDto.getAuthority_id();
		this.member_id = requestDto.getMember_id();
	}

	public AuthorityEntity(String authority_name) {
		this.authority_name = authority_name;
	}
}
