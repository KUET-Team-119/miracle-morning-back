package com.miracle.miraclemorningback.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.miracle.miraclemorningback.dto.MemberDTO;
import com.miracle.miraclemorningback.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor // MemberService에 대한 멤버를 사용 가능
public class MemberController {

    // 생성자 주입
    private final MemberService memberService;

    // 회원가입 페이지 출력
    @GetMapping("/member/sign-up")
    public String signUpPage() {
        return "sign-up";
    }

    // 회원가입 정보 받기
    // name값을 requestparam에 담음
    @PostMapping("/member/sign-up")
    public String addUser(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberController.addUser");
        System.out.println("memberDTO = " + memberDTO);
        memberService.addUser(memberDTO);

        return "index";
    }
}