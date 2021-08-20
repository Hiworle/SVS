package com.svs.controller;

import java.util.Arrays;

import javax.naming.spi.DirStateFactory.Result;

import com.svs.MyClient;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
public class UserController {

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String start(
    // @RequestParam(value = "candidate-number") int cddNumber,
    // @RequestParam(value = "candidate-name") String cddName,
    // @RequestParam(value = "candidate-msg") String cddMsg, @RequestParam(value =
    // "vote-number") int voteNumber
    ) {

        String in = new String("0\n" + "2\n" + "3\n" + "Tom\n" + "I am Tom!\n" + "Bob\n" + "I am Bob.\n" + "小明\n"
                + "我是小明。\n" + "1\n0\n1\n");
        int[] result = null;
        try {
            result = MyClient.run(in);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Arrays.toString(result);
    }

    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public String start(@RequestParam(value = "id-code", required = true) String idCode) {
        // ========================================
        // ================= code here ============
        // ========================================
        // System.out.println("Hello!");
        return "index";
    }
}