package com.miracle.miraclemorningback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MiracleMorningBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiracleMorningBackApplication.class, args);
	}

}