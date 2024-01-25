package com.miracle.miraclemorningback.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.miracle.miraclemorningback.dto.MemberRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class MemberEntity extends Timestamped implements UserDetails{
    
	private MemberEntity memberEntity;
	
	public MemberEntity(MemberRequestDto requestDto) {
        this.memberName = requestDto.getMemberName();
        this.password = requestDto.getPassword();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId; // 유저 아이디

    @Column(nullable = false, unique = true, columnDefinition = "varchar(10)")
    private String memberName; // 사용자명

    @Column(nullable = false, columnDefinition = "char(5)")
    private String password; // 비밀번호

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Role role = memberEntity.getRole();
        String authority = role.getAuthority();
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        
        authorities.add(grantedAuthority);

        return authorities;
	}

	@Override
	public String getUsername() {
		return this.memberName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
    
}
