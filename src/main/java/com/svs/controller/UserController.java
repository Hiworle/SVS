package com.svs.controller;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

import Util.AddressUtils;

@EnableAutoConfiguration
@RestController
public class UserController {

    public static int result[] = null;// 储存结果
    public static Candidate candidate = null; // 在这里储存candidate信息，供投票者调取
    public static String title = null; // 投票标题

    /**
     * 映射到/create，用于创建
     * 
     * @param cddNumber  候选人数目
     * @param cddName    候选人名字
     * @param cddMsg     候选人信息
     * @param voteNumber 投票人数
     * @param voteMsg    投票信息（“1”为投票，“0”为不投票）
     * @return 投票结果
     * @throws UnknownHostException
     * @throws SocketException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public Object creat(@RequestParam(value = "candidateNumber") int cddNumber,
            @RequestParam(value = "candidateName") String[] cddName,
            @RequestParam(value = "candidateMsg") String[] cddMsg, @RequestParam(value = "voterNumber") int voterNumber,
            @RequestParam(value = "title") String title, HttpServletRequest request)
            throws UnknownHostException, SocketException {

        // int cddNumber = 3;
        // int voterNumber = 2;
        // Candidate candidate = new Candidate(cddNumber);
        // candidate.setName(new String[] { "Alice", "Bob", "Cat" });
        // candidate.setMsg(new String[] { "Alice msg", "Bob msg", "Cat msg" });
        Candidate candidate = new Candidate(cddNumber);
        candidate.setName(cddName);
        candidate.setMsg(cddMsg);

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

        Formatter.log("收到来自" + ip + "的 /create 请求...");

        UserController.candidate = candidate; // 保存候选人信息
        UserController.title = title;

        RunnableCreate runnableCreate = new RunnableCreate(cddNumber, voterNumber, candidate);
        runnableCreate.start();
        // String in = Formatter.toCreate(voterNumber + "", candidate, voteMsg);
        // try {
        // result = MyClient.run(in);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        String idCode = AddressUtils.getInnetIp(); // 获取本地ip
        // System.out.println(idCode);

        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("idCode", idCode);

        Formatter.log("已返回验证码: " + JSON.toJSONString(obj) + " 给 " + ip);
        return obj; // 返回验证码
    }

    /**
     * 映射到/join，用于加入
     * 
     * @param idCode  验证码
     * @param voteMsg 投票信息（“1”为投票，“0”为不投票）
     * @return 投票结果
     */
    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public Object join(@RequestParam(value = "idCode", required = true) String idCode,
            @RequestParam(value = "voteMsg") String voteMsg[], HttpServletRequest request) {
        int[] result = null;
        // String idCode = "192.168.0.100";
        // String[] voteMsg = { "0", "0", "1" };

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

        Formatter.log("收到来自" + ip + "的 /join 请求...");

        String in = Formatter.toJoin(idCode, voteMsg);

        try {
            result = MyClient.run(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> obj = new HashMap<String, Object>();
        obj.put("result", JSON.toJSONString(result));

        Formatter.log("已返回投票结果：" + obj.toString() + " 给 " + ip);

        return obj;
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

        System.out.println("[" + sdf.format(date) + "] 收到来自 " + ip + " 的 /candidatemsg 请求...");
        if (UserController.candidate == null) {
            System.out.println("[" + sdf.format(date) + "] 候选人信息为空，已返回 null 给 " + ip);
            return null;
        } else {
            System.out.println("[" + sdf.format(date) + "] 已返回候选人信息 "
                    + JSON.toJSONString(UserController.candidate.toObj()) + " 给 " + ip);
            return UserController.candidate.toObj();
        }
    }

    /**
     * 映射到/titlemsg
     * 
     * @return 标题信息
     */
    @RequestMapping(value = "/titlemsg", method = RequestMethod.GET)
    public Object titlemsg(HttpServletRequest request) {

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

        System.out.println("[" + sdf.format(date) + "] 收到来自 " + ip + " 的 /titlemsg 请求...");
        if (title == null) {
            System.out.println("[" + sdf.format(date) + "] 标题信息为空，已返回 null 给 " + ip);
            return null;
        } else {
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put("title", title);

            System.out.println("[" + sdf.format(date) + "] 已返回标题信息 " + obj.toString() + " 给 " + ip);

            return obj;
        }
    }

}