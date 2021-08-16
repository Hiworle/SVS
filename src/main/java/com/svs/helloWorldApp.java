package com.svs;

import org.springframework.boot.SpringApplication;

import com.svs.controller.HelloController;
import com.svs.controller.LoginController;

public class helloWorldApp {
	public static void main(String[] args) {
		SpringApplication.run(LoginController.class, args);
	}
}
