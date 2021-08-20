package com.svs.controller;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public String creat(
    // @RequestParam(value = "candidate-number") int cddNumber,
    // @RequestParam(value = "candidate-name") String[] cddName,
    // @RequestParam(value = "candidate-msg") String[] cddMsg,
    // @RequestParam(value = "voter-number") int voterNumber, @RequestParam(value =
    // "vote-msg") String voteMsg[]
    ) {

        int cddNumber = 3;
        int voterNumber = 2;
        Candidate candidate = new Candidate(cddNumber);
        candidate.setName(new String[] { "Alice", "Bob", "Cat" });
        candidate.setMsg(new String[] { "Alice msg", "Bob msg", "Cat msg" });
        String[] voteMsg = { "1", "1", "0" };

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
    public String join(
    // @RequestParam(value = "id-code", required = true) String idCode,
    // @RequestParam(value = "vote-msg") String voteMsg[]
    ) {
        int[] result = null;
        String idCode = "192.168.0.100";
        String[] voteMsg = { "0", "0", "1" };

        String in = Formatter.toJoin(idCode, voteMsg);

        try {
            result = MyClient.run(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Arrays.toString(result);
    }
}