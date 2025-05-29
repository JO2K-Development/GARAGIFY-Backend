package com.jo2k.garagify;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
@SpringBootApplication
public class GaragifyApplication {


	public static void main(String[] args) {
		SpringApplication.run(GaragifyApplication.class, args);
	}

	@Bean
	public CommandLineRunner printActiveProfiles(Environment env) {
		return args -> System.out.println(">>> Active profiles: " + String.join(", ", env.getActiveProfiles()));
	}
}



