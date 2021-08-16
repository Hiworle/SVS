package com.svs.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@EnableAutoConfiguration
@Controller
public class HelloController {

	@ResponseBody
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String index() {
		System.out.println("Hello!");
        return "index";
    }
}