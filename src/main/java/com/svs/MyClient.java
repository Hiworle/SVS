package com.svs;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

//测试用客户端
public class MyClient {

   public static int[] run(String str) throws Exception {
      String choice;
      String Serverip = null;
      Socket mysocket;
      DataInputStream in = null;
      ObjectInputStream in2 = null;
      String receiveMsg[];
      BigInteger sendMsg[];
      String receiveMsg2[];
      BigInteger sendMsg2[];
      Voter me = new Voter();
      int i = 0;
      BigInteger sum = new BigInteger("0");
      // 获取所有投票者的ip地址
      // System.out.println("输入0则发起投票，输入1则加入投票");
      Scanner reader = new Scanner(str);
      choice = reader.nextLine();
      if (choice.equals("0")) {
         Formatter.log("正在创建投票...");
         // System.out.println("有几个小伙伴来投票？");
         me.number = reader.nextInt();
         // System.out.println("设置候选人信息");
         // System.out.print("候选人人数：");
         Candidate candidate = new Candidate(reader.nextInt());// 设置候选人信息；
         reader.nextLine();
         for (i = 0; i < candidate.number; i++) {
            // System.out.print("候选人" + i + "的名字：");
            candidate.name[i] = reader.nextLine();
            // System.out.print("候选人" + i + "的个人信息：");
            candidate.msg[i] = reader.nextLine();
         }
         // System.out.println("候选人信息设置完成");
         Formatter.log("候选人信息设置完成: " + candidate.toString());

         // System.out.println("============================================");

         // System.out.println("正式使用时，请让你的小伙伴输入：" +
         // InetAddress.getLocalHost().getHostAddress());
         Formatter.log("已生成验证码: " + InetAddress.getLocalHost().getHostAddress());

         new Server(candidate, me.number).start();
         reader.close();
         return null;
      }

      else if (choice.equals("1")) {
         // System.out.println("输入验证信息");
         Serverip = reader.nextLine();
         Formatter.log("正在加入验证码为 " + Serverip + " 的投票...");

         try {
            // System.out.println("等待所有人加入投票中....");
            // System.out.println("============================================");
            Formatter.log("等待所有人加入投票中...");

            mysocket = new Socket(Serverip, 4332);
            in = new DataInputStream(mysocket.getInputStream());
            in2 = new ObjectInputStream(mysocket.getInputStream());
            me.candidate = (Candidate) in2.readObject();
            me.number = in.readInt();
            me.id = in.readInt();
            me.ips = new String[me.number];
            for (i = 0; i < me.number; i++) {
               me.ips[i] = in.readUTF();
            }
         } catch (Exception e) {
            // System.out.println("未知错误222" + e);
            Formatter.log("发生未知错误222");
         }
         // for (i = 0; i < me.candidate.number; i++) {
         // System.out.print("候选人" + i + "名字" + me.candidate.name[i]);
         // System.out.println("相关信息" + me.candidate.msg[i]);
         // }
         // System.out.println("============================================");
         Formatter.log("候选人信息 " + me.candidate.toString());
         Formatter.log("已被分配为第 " + me.id + " 位投票者");
         // System.out.println("我是第" + me.id + "位投票者");
         // System.out.println("设置你神圣的选票吧！（按1或true表示投该位候选人，其他输入则视为不投）");
         String vote;
         boolean voteMsg[] = new boolean[me.candidate.number];
         for (i = 0; i < me.candidate.number; i++) {
            // System.out.print("候选人" + i + "的选择是：");
            vote = reader.nextLine();
            if (vote.equals("1") || vote.equals("true"))
               voteMsg[i] = true;
            else
               voteMsg[i] = false;
         }

         Formatter.log("本机投票信息是: " + Arrays.toString(voteMsg));
         Formatter.log("等待其他人完成投票...");
         // System.out.println("等待其他人完成投票...");
         reader.close();
         me.work(voteMsg);
         receiveMsg = new String[me.number];
         receiveMsg[me.id] = me.randomDec[me.id].toString();
         sendMsg = me.randomDec;
         Receive receive = new Receive(receiveMsg, sendMsg, me.id, me.number);
         Thread thread = new Thread(receive);// 在这个线程中,自己作为服务端与id大于自己的投票者进行通信
         Send send = new Send(receiveMsg, sendMsg, me.id, me.ips);
         Thread thread2 = new Thread(send);// 在这个线程中，自己作为客户端与id小于自己的投票者进行通信
         thread.start();
         thread2.start();
         thread.join();
         thread2.join();
         // System.out.println("============================================");
         // System.out.println("第一次数据传输完成");
         // for (i = 0; i < me.number; i++) {
         // System.out.println("收到信息的第" + i + "个" + receiveMsg[i]);
         // }

         Formatter.log("第一次数据传输完成");
         Formatter.log("第一次收到的信息: " + Arrays.toString(receiveMsg));

         for (i = 0; i < me.number; i++) {
            sum = sum.add(new BigInteger(receiveMsg[i]));
         }
         // System.out.println("我的计票结果是：" + sum);
         Formatter.log("本机的计票结果是 " + sum);

         receiveMsg2 = new String[me.number];
         receiveMsg2[me.id] = sum.toString();
         sendMsg2 = new BigInteger[me.number];
         for (i = 0; i < me.number; i++) {
            sendMsg2[i] = sum;
         }
         Receive receive2 = new Receive(receiveMsg2, sendMsg2, me.id, me.number);
         Thread thread3 = new Thread(receive2);// 在这个线程中,自己作为服务端与id大于自己的投票者进行通信
         Send send2 = new Send(receiveMsg2, sendMsg2, me.id, me.ips);
         Thread thread4 = new Thread(send2);// 在这个线程中，自己作为客户端与id小于自己的投票者进行通信
         thread3.start();
         thread4.start();
         thread3.join();
         thread4.join();
         // System.out.println("第三阶段完成（吃苹果）");
         // System.out.println("============================================");
         Formatter.log("第三阶段完成（吃苹果）");
         me.receiveMsg = receiveMsg2;
         Formatter.log("第二次收到的信息: " + Arrays.toString(me.receiveMsg));
         // for (i = 0; i < me.number; i++) {
         // System.out.println("收到信息的第" + i + "个" + me.receiveMsg[i]);
         // }
         // System.out.println("============================================");
         // System.out.println("有几位投票人？" + me.number);
         me.getResult();
         // for (i = 0; i < me.candidate.number; i++) {
         // System.out.println("候选人" + i + "的票数:" + me.result[i]);
         // }
         Formatter.log("已完成计票, 投票人数：" + me.number + " , 计票结果： " + Arrays.toString(me.result));
         return me.result;
      } else {
         // System.out.println("退出了？好");
         Formatter.log("退出了？好");
         reader.close();
         return null;
      }
   }
}
