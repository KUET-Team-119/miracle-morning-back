package com.miracle.miraclemorningback.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@RestController
public class AdminController {
	
	@GetMapping("/admin/sign-out")
	public ModelAndView adminSignOut(HttpSession session, ModelAndView mav) {
    	session.invalidate();
    	mav.setViewName("redirect:/admin");
    	return mav;
    }
}
