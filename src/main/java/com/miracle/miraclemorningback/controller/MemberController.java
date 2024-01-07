package com.miracle.miraclemorningback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    @PostMapping("/api/member")
    public MemberResponseDto registerMember(@RequestBody MemberRequestDto requestDto) {
        return memberService.registerMember(requestDto);
    }

    // 특정 회원 검색
    @GetMapping("/api/member/{member_id}")
    public MemberResponseDto getMember(@PathVariable Long member_id) {
        return memberService.getMember(member_id);
    }

    // 회원 정보 수정
    @PutMapping("/api/member/{member_id}")
    public MemberResponseDto updateMember(@PathVariable Long member_id, @RequestBody MemberRequestDto requestDto)
            throws Exception {
        return memberService.updateMember(member_id, requestDto);
    }

    // 회원 삭제
    @DeleteMapping("/api/member/{member_id}")
    public MemberDeleteSuccessResponseDto deleteMember(@PathVariable Long member_id,
            @RequestBody MemberRequestDto requestDto)
            throws Exception {
        return memberService.deleteMember(member_id, requestDto);
    }
}