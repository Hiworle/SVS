package com.svs.controller;

import java.util.Map;
import java.util.HashMap;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.util.JSONPObject;

@EnableAutoConfiguration
@Controller
public class LoginController {

	@ResponseBody
	@RequestMapping(value = "/login")
	public Map<String,Object> login(@RequestParam(value = "name", required = false)String name) {
//		System.out.println(map.get("name").toString());
		System.out.println(name);
		Map<String,Object> map = new HashMap<>(1);
		map.put("name", name);
		return map;
	}
}
