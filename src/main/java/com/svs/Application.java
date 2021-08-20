package com.svs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.svs.controller.UserController;
import com.svs.controller.HelloController;
import com.svs.controller.LoginController;

@SpringBootApplication
@EnableAsync
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(UserController.class, args);
	}
}
