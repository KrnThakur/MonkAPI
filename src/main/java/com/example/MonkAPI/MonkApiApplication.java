package com.example.MonkAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MonkApiApplication {

	public static void main(String[] args) {
		System.out.println("Application Started");
		System.out.println("Attempt to trigger the build");
		SpringApplication.run(MonkApiApplication.class, args);
	}

}
