package com.miracle.miraclemorningback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf((csrf) -> csrf.disable())
            .authorizeHttpRequests((requests)->requests
            		.requestMatchers("/admin")
            				.hasAuthority("true")
            		.requestMatchers("/welcome","/home","/sign-in","/sign-up","/sign-out","/admin/sign-out")
            				.permitAll()
            );
		http.formLogin(Customizer.withDefaults());
		http.httpBasic(Customizer.withDefaults());
        
        return http.build();
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
