package com.alvinxu.TheDailyGrind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //(exclude = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
public class TheDailyGrindApplication {

	public static void main(String[] args) {
		SpringApplication.run(TheDailyGrindApplication.class, args);
	}

}
