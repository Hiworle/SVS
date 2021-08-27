package com.svs.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.svs.Candidate;
import com.svs.Formatter;
import com.svs.MyClient;
import com.svs.RunnableCreate;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
public class UserController {

    public static int result[] = null;// 储存结果
    public static Candidate candidate = null; // 在这里储存candidate信息，供投票者调取

    /**
     * 映射到/create，用于创建
     * 
     * @param cddNumber  候选人数目
     * @param cddName    候选人名字
     * @param cddMsg     候选人信息
     * @param voteNumber 投票人数
     * @param voteMsg    投票信息（“1”为投票，“0”为不投票）
     * @return 投票结果
     * @throws InterruptedException
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String creat(@RequestParam(value = "candidateNumber") int cddNumber,
            @RequestParam(value = "candidateName") String[] cddName,
            @RequestParam(value = "candidateMsg") String[] cddMsg, @RequestParam(value = "voterNumber") int voterNumber,
            @RequestParam(value = "voteMsg") String voteMsg[]) {

        // int cddNumber = 3;
        // int voterNumber = 2;
        // Candidate candidate = new Candidate(cddNumber);
        // candidate.setName(new String[] { "Alice", "Bob", "Cat" });
        // candidate.setMsg(new String[] { "Alice msg", "Bob msg", "Cat msg" });
        // String[] voteMsg = { "1", "1", "0" };
        Candidate candidate = new Candidate(cddNumber);
        candidate.setName(cddName);
        candidate.setMsg(cddMsg);

        UserController.candidate = candidate; // 保存候选人信息

        RunnableCreate runnableCreate = new RunnableCreate(cddNumber, voterNumber, candidate, voteMsg);
        runnableCreate.start();

        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // String in = Formatter.toCreate(voterNumber + "", candidate, voteMsg);
        // try {
        // result = MyClient.run(in);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        return Arrays.toString(result);
    }

    /**
     * 映射到/join，用于加入
     * 
     * @param idCode  验证码
     * @param voteMsg 投票信息（“1”为投票，“0”为不投票）
     * @return 投票结果
     */
    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public String join(@RequestParam(value = "idCode", required = true) String idCode,
            @RequestParam(value = "voteMsg") String voteMsg[]) {
        int[] result = null;
        // String idCode = "192.168.0.100";
        // String[] voteMsg = { "0", "0", "1" };

        String in = Formatter.toJoin(idCode, voteMsg);

        try {
            result = MyClient.run(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Arrays.toString(result);
    }

    /**
     * 映射到/candidatemsg，用于投票者获取候选人信息
     * 
     * @return 候选人信息
     */
    @RequestMapping(value = "/candidatemsg", method = RequestMethod.GET)
    public Object candidatemsg(HttpServletRequest request) {

        // 获取客户端ip地址
        String ip = null;// 客户端ip地址
        ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss.SSS");

        System.out.println("[" + sdf.format(date) + "] 收到来自 " + ip + " 的请求...");
        if (UserController.candidate == null) {
            System.out.println("[" + sdf.format(date) + "] 候选人信息为空，已返回 null 给 " + ip);
            return null;
        } else {
            System.out.println("[" + sdf.format(date) + "] 已返回 " + JSON.toJSONString(UserController.candidate.toObj())
                    + " 给 " + ip);
            return UserController.candidate.toObj();
        }
    }
}