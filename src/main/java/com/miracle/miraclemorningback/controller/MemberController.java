package com.miracle.miraclemorningback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.miracle.miraclemorningback.dto.MemberDeleteSuccessResponseDto;
import com.miracle.miraclemorningback.dto.MemberRequestDto;
import com.miracle.miraclemorningback.dto.MemberResponseDto;
import com.miracle.miraclemorningback.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor // MemberService에 대한 멤버를 사용 가능
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 전체 회원 조회
    @GetMapping("/api/members")
    public List<MemberResponseDto> getMembers() {
        return memberService.getMembers();
    }

    // 회원 등록
    @PostMapping("/api/auth/member/new")
    public ResponseEntity<Object> registerMember(@RequestBody MemberRequestDto requestDto) {
        return memberService.registerMember(requestDto);
    }

    // 특정 회원 검색
    @GetMapping("/api/member/{memberName}")
    public MemberResponseDto getMember(@PathVariable String memberName) {
        return memberService.getMember(memberName);
    }

    // 회원 삭제
    @DeleteMapping("/api/member/{memberId}")
    public MemberDeleteSuccessResponseDto deleteMember(@PathVariable Long memberId)
            throws Exception {
        return memberService.deleteMember(memberId);
    }

    // 로그인
    @PostMapping("/api/auth/member")
    public ResponseEntity<Object> loginMember(@RequestBody MemberRequestDto requestDto) throws Exception {
        return memberService.loginMember(requestDto);
    }

    /*
     * 1차 배포에는 사용자 닉네임 변경 기능 제외
     * // 회원 정보 수정
     * 
     * @PutMapping("/api/member/{memberName}")
     * public MemberResponseDto updateMember(@PathVariable String
     * memberName, @RequestBody MemberRequestDto requestDto)
     * throws Exception {
     * return memberService.updateMember(memberName, requestDto);
     * }
     */
}