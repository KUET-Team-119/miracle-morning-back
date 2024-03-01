package com.miracle.miraclemorningback.Util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void generateRefreshTokenCookie(HttpServletResponse response, String value) {
        Cookie cookie = new Cookie("refreshToken", value);
        cookie.setMaxAge(60 * 60 * 24 * 7); // 7일 동안 유효
        cookie.setPath("/"); // 쿠키 경로 설정
        cookie.setHttpOnly(true); // JS에서 사용하지 못하도록 설정
        response.addCookie(cookie); // 응답 헤더에 쿠키 추가
    }
}