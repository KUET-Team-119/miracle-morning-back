package com.miracle.miraclemorningback.controller;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.miracle.miraclemorningback.dto.MemberRequestDto;
import com.miracle.miraclemorningback.dto.TokenDto;
import com.miracle.miraclemorningback.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberLoginController {

    @Autowired
    private MemberService memberService;

    // 로그인
    @PostMapping("/sign-in")
    public ModelAndView signIn(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelAndView mav) throws Exception{
    	String memberName = request.getParameter("memberName");
    	String password = request.getParameter("password");
    	TokenDto tokenDto = memberService.memberLogin(new MemberRequestDto(memberName, password));
    	if (tokenDto != null) {
    		Cookie cookie = new Cookie("token", "Bearer" + tokenDto);
    		cookie.setPath("/");
    		cookie.setMaxAge(60 * 60 * 24 * 7);
    		response.addCookie(cookie);
    		session.setAttribute("memberName", memberName);
    		mav.setViewName("redirect:/home");
    	}
    	return mav;
    }
    
    // 로그아웃
    @GetMapping("/logout")
    public ModelAndView signOut(HttpSession session, ModelAndView mav) {
    	session.invalidate();
    	mav.setViewName("redirect:/welcome");
    	return mav;
    }

}