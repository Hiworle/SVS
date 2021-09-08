package com.svs;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//本程序仅用于测试使用
public class TestServer {
   public static void main(String args[]) {
      ServerSocket server = null;
      Socket you = null;
      int number = 0;
      int numbermax = 3;
      String serverips[];
      serverips = new String[numbermax];
      ServerThread.flag = false;
      while (true) {
         try {
            server = new ServerSocket(4333);
         } catch (IOException e1) {
            System.out.println("正在监听"); // ServerSocket对象不能重复创建
         }
         try {
            System.out.println(" 等待客户呼叫");
            you = server.accept();
            System.out.println("客户的地址:" + you.getInetAddress());
         } catch (IOException e) {
            System.out.println("正在等待客户");
         }
         if (you != null) {
            System.out.println("投票者加入人数："+Math.addExact(number, 1)+"/"+numbermax);
            serverips[number] = you.getInetAddress().getHostAddress();
            new TheServerThread(you, number).start(); // 为每个客户启动一个专门的线程
            number++;
            System.out.println("number=" + number);
            if (number == numbermax) {
               TheServerThread.number = number;
               int i = 0;
               for (i = 0; i < number; i++) {
                  TheServerThread.ips[i] = serverips[i];
               }
               TheServerThread.flag = true;
               try {
                  server.close();
                  System.out.println("任务结束");
                  return;
               } catch (Exception e) {
                  System.out.println("怎么关闭不了？");
               }
            }
         }
      }
   }
}

class TheServerThread extends Thread {
   static int number;
   static String ips[] = new String[100];
   static boolean flag;
   Socket socket;
   int id;
   DataOutputStream out = null;
   DataInputStream in = null;

   TheServerThread(Socket t, int id) {
      socket = t;
      try {
         out = new DataOutputStream(socket.getOutputStream());
         in = new DataInputStream(socket.getInputStream());
         this.id = id;
      } catch (IOException e) {
      }
   }

   public void run() {
      while (true) {
         if (flag == true) {
            try {
               out.writeInt(number);
               out.writeInt(id);
               for (int i = 0; i < number; i++) {
                  out.writeUTF(ips[i]);
               }
               return;
            } catch (IOException e) {
               System.out.println("客户离开");
               return;
            }
         } else {
            try {
               Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
         }
      }
   }
}
