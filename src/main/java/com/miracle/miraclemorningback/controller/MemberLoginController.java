package com.miracle.miraclemorningback.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.miracle.miraclemorningback.dto.MemberResponseDto;
import com.miracle.miraclemorningback.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberLoginController {

    @Autowired
    private MemberService memberService;
    
    // 로그인
    @PostMapping("/sign-in")
    public ModelAndView signIn(HttpServletRequest request, HttpSession session, ModelAndView mav) {
    	String memberName = request.getParameter("memberName");
    	String password = request.getParameter("password");
    	List<MemberResponseDto> members = memberService.getMembers();
    	MemberResponseDto compare;

    	for (int i=0; i<members.size(); i++) {
    		compare = members.get(i);
    		if (compare.getMemberName().equals(memberName)) {
    			if (compare.getPassword().equals(password) && !compare.getIsAdmin()) {
    				mav.setViewName("redirect:/home");
    				session.setAttribute("memberResponseDto", compare);
    				// 세션 만료기간 : 분 단위. 30 * 60 = 30분
    				session.setMaxInactiveInterval(30 * 60);
    			}else {
    				mav.setViewName("redirect:/welcome");
    			}
    			break;
    		}
    	}
    	return mav;
    }
    
    // 로그아웃
    @GetMapping("/sign-out")
    public ModelAndView memberSignOut(HttpSession session, ModelAndView mav) {
    	session.invalidate();
    	mav.setViewName("redirect:/welcome");
    	return mav;
    }
}