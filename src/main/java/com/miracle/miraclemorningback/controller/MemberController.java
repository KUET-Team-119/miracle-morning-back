package com.miracle.miraclemorningback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.miracle.miraclemorningback.dto.MemberRequestDto;
import com.miracle.miraclemorningback.dto.MemberResponseDto;
import com.miracle.miraclemorningback.entity.UserDetailsImpl;
import com.miracle.miraclemorningback.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor // MemberService에 대한 멤버를 사용 가능
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 전체 회원 조회
    @GetMapping("/api/admin/members")
    public List<MemberResponseDto> getMembers() {
        return memberService.getMembers();
    }

    // 회원 등록
    @PostMapping("/api/auth/member/new")
    public ResponseEntity<Object> registerMember(@RequestBody MemberRequestDto requestDto) {
        return memberService.registerMember(requestDto);
    }

    // 특정 회원 검색
    @GetMapping("/api/admin/member/{memberName}")
    public ResponseEntity<Object> getMember(@PathVariable String memberName) {
        return memberService.getMember(memberName);
    }

    // 회원 권한 수정
    @PatchMapping("/api/admin/member")
    public ResponseEntity<Object> updateMember(@RequestBody MemberRequestDto requestDto) {
        return memberService.updateMemberRole(requestDto);
    }

    // 회원 삭제
    @DeleteMapping("/api/member/{memberId}")
    public ResponseEntity<Object> deleteMember(@PathVariable Long memberId,
            @RequestHeader(value = "Password", required = true) String password,
            Authentication authentication) {
        String requester = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        return memberService.deleteMember(memberId, password, requester);
    }

    // 로그인
    @PostMapping("/api/auth/member")
    public ResponseEntity<Object> loginMember(@RequestBody MemberRequestDto requestDto) {
        return memberService.loginMember(requestDto);
    }
}