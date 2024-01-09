package com.miracle.miraclemorningback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miracle.miraclemorningback.entity.AuthorityEntity;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthorityController {
	// 회원 관리 열람
	@Autowired
	AuthorityEntity authorityEntity;
	
	@GetMapping("/api/manager")
	public String managerPage() {
		return "Welcome to manager page";
	}
}
