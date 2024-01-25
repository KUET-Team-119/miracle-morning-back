package com.miracle.miraclemorningback.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.miracle.miraclemorningback.filter.AuthoritiesLoggingAfterFilter;
import com.miracle.miraclemorningback.filter.AuthoritiesLoggingAtFilter;
import com.miracle.miraclemorningback.filter.JWTTokenAuthenticationFilter;
import com.miracle.miraclemorningback.filter.RequestValidationBeforeFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("http://localhost:8080/welcome"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setExposedHeaders(Collections.singletonList("*"));
                config.setMaxAge(3600L);
                return config;
            }
            })).csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers("/welcome","/sign-in","/sign-out","sign-up")
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
            .addFilterAt(new AuthoritiesLoggingAtFilter(),BasicAuthenticationFilter.class)
            .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
            .addFilterBefore(new JWTTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests((requests)->requests
            		.requestMatchers("/admin")
            				.hasRole("ADMIN")
            		.requestMatchers("/welcome","/sign-in","/logout","/admin/logout","/register")
    						.permitAll()
            		.anyRequest()
            				.hasRole("USER"))
            .exceptionHandling((Customizer) -> Customizer
                    .authenticationEntryPoint(new JWTEntryPoint()))
            .formLogin(Customizer.withDefaults())
			.httpBasic((Customizer) -> Customizer.disable());
        return http.build();
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
