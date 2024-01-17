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
import org.springframework.web.servlet.ModelAndView;

import com.miracle.miraclemorningback.dto.MemberDeleteSuccessResponseDto;
import com.miracle.miraclemorningback.dto.MemberRequestDto;
import com.miracle.miraclemorningback.dto.MemberResponseDto;
import com.miracle.miraclemorningback.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor // MemberService에 대한 멤버를 사용 가능
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 전체 회원 조회
    @GetMapping("/members")
    public List<MemberResponseDto> getMembers() {
        return memberService.getMembers();
    }

    // 특정 회원 검색
    @GetMapping("/members/{memberName}")
    public MemberResponseDto getMember(@PathVariable String memberName) {
        return memberService.getMember(memberName);
    }

    // 회원 정보 수정
    @PutMapping("/members/update/{memberName}")
    public MemberResponseDto updateMember(@PathVariable String memberName, @RequestBody MemberRequestDto requestDto)
            throws Exception {
        return memberService.updateMember(memberName, requestDto);
    }

    // 회원 삭제
    @DeleteMapping("/members/delete/{memberId}")
    public MemberDeleteSuccessResponseDto deleteMember(@PathVariable Long memberId,
            @RequestBody MemberRequestDto requestDto)
            throws Exception {
        return memberService.deleteMember(memberId, requestDto);
    }
    
    // 회원 등록
    @PostMapping("/sign-up")
	public ModelAndView signUp(HttpServletRequest request, ModelAndView mav) {
    	String memberName = request.getParameter("memberName");
    	String password = request.getParameter("password");
    	
    	memberService.registerMember(new MemberRequestDto(memberName, password));
		mav.setViewName("redirect:/welcome");
		
		return mav;
	}
}