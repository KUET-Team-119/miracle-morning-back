package com.miracle.miraclemorningback.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.miracle.miraclemorningback.config.JWTTokenValidator;
import com.miracle.miraclemorningback.constants.SecurityConstants;
import com.miracle.miraclemorningback.service.AccountService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTTokenAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private JWTTokenValidator jwtTokenVaildator;
	
	@Autowired
	private AccountService accountService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = getToken(request), memberName;
		UserDetails userDetails;
		UsernamePasswordAuthenticationToken authentication;
		if (token != null && jwtTokenVaildator.validateToken(token)) {
			try {
				memberName = jwtTokenVaildator.getMemberNameFromToken(token);
				userDetails = accountService.loadUserByUsername(memberName);
				authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
        filterChain.doFilter(request, response);
	}
	
	private String getToken(HttpServletRequest request) {
		String header = request.getHeader(SecurityConstants.JWT_HEADER);
		if (StringUtils.hasText(header) && header.startsWith("Bearer"))
			return header.substring(7, header.length());

		return null;
	}
}
