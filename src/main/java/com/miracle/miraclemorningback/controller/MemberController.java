package com.miracle.miraclemorningback.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import com.miracle.miraclemorningback.dto.MemberDTO;
import com.miracle.miraclemorningback.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor // MemberService에 대한 멤버를 사용 가능
public class MemberController {

    // 생성자 주입
    private final MemberService memberService;

    // 회원가입 정보 받기
    // html의 name값을 requestparam에 담음
    @PostMapping("/member/sign-up")
    public String addMember(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberController.addMember");
        System.out.println("memberDTO = " + memberDTO);
        memberService.addMember(memberDTO);

        return "index";
    }

    @PostMapping("/member/sign-in") // session: 로그인 유지
    public String authenticateMember(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO signInResult = memberService.signIn(memberDTO);
        if (signInResult != null) { // 로그인 성공
            Boolean isAdmin = signInResult.getIsAdmin();
            session.setAttribute("nickname", signInResult.getNickname());

            if (isAdmin == true) {
                return "admin-page";
            } else {
                return "main";
            }

        } else { // 로그인 실패
            return "index";
        }
    }

    // 상세 조회, 삭제 기능은 보완 필요
    @GetMapping("/member")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        // 어떠한 html로 가져갈 데이터가 있다면 model 사용
        model.addAttribute("memberList", memberDTOList);
        return "list";
    }

    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        // login처럼 return 값에 따라 분류 가능
        model.addAttribute("member", memberDTO);
        return "detail";
    }

    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteByid(id);

        return "redirect:/member/"; // list로 쓰면 껍데기만 보여짐
    }
}