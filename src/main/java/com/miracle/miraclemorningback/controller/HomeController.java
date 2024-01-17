package com.miracle.miraclemorningback.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class HomeController {

    @GetMapping("/welcome")
    public String indexPage() {
        return "index";
    }
    
    @GetMapping("/home")
    public String homePage() {
    	return "home";
    }
    
    @GetMapping("/admin")
    public String adminPage() {
    	return "admin";
    }
}