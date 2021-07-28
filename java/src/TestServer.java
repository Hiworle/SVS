import java.io.*;
import java.net.*;
import java.util.*;

import org.graalvm.compiler.nodes.InliningLog.UpdateScope;

//本程序仅用于测试使用
public class TestServer {
   public static void main(String args[]) {
      ServerSocket server = null;
      ServerThread thread;
      Socket you = null;
      int number = 0;
      int numbermax = 1;
      String serverips[];
      serverips = new String[1];
      ServerThread.flag = false;
      while (true) {
         try {
            server = new ServerSocket(4332);
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
            serverips[number] = you.getInetAddress().getHostAddress();
            new ServerThread(you, number).start(); // 为每个客户启动一个专门的线程
            number++;
            if (number == numbermax) {
               ServerThread.flag = true;
               ServerThread.number = number;
               int i = 0;
               for (i = 0; i < number; i++) {
                  ServerThread.ips = serverips;
               }
            }
         }
      }
   }
}

class ServerThread extends Thread {
   static int number;
   static String ips[];
   static boolean flag;
   int id = 0;
   Socket socket=null;
      DataOutputStream out = null;
      DataInputStream in = null;
   ServerThread(Socket t, int id) {
      socket = t;
      this.id = id;
      try {
         out = new DataOutputStream(socket.getOutputStream());
         in = new DataInputStream(socket.getInputStream());
      } catch (IOException e) {
      }
   }

   public void run() {
      while (true) {
         try {
            if (flag == true) {
               out.writeInt(number);
               out.writeInt(id);
               for (int i = 0; i < number; i++) {
                  out.writeUTF(ips[i]);
               }
            }
         } catch (IOException e) {
            System.out.println("客户离开");
            return;
         }
      }
   }
}
