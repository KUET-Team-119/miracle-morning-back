package com.miracle.miraclemorningback.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 메인 페이지 출력
    @GetMapping("/")
    public String indexPage() {
        return "index";
    }
}