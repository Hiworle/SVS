<<<<<<< HEAD
import java.io.*;
import java.net.*;
=======
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
>>>>>>> cb848acb26530c9b8c82938894d7e3aa259a7efd

//本程序仅用于测试使用
public class TestServer {
   public static void main(String args[]) {
      ServerSocket server = null;
<<<<<<< HEAD
=======
      ServerThread thread;
>>>>>>> cb848acb26530c9b8c82938894d7e3aa259a7efd
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
<<<<<<< HEAD
            System.out.println("num="+number);
=======
>>>>>>> cb848acb26530c9b8c82938894d7e3aa259a7efd
            System.out.println("客户的地址:" + you.getInetAddress());
         } catch (IOException e) {
            System.out.println("正在等待客户");
         }
         if (you != null) {
            serverips[number] = you.getInetAddress().getHostAddress();
<<<<<<< HEAD
            new ServerThread(you, number).start(); // 为每个客户启动一个专门的线程
            number++;
            if (number == numbermax) {
               ServerThread.flag = true;
               ServerThread.number = number;
               ServerThread.ips = serverips;
=======
            number++;
            new ServerThread(you).start(); // 为每个客户启动一个专门的线程
            if (number == numbermax) {
               ServerThread.flag = true;
               ServerThread.number = number;
               int i = 0;
               for (i = 0; i < number; i++) {
                  ServerThread.ips[i] = serverips[i];
>>>>>>> cb848acb26530c9b8c82938894d7e3aa259a7efd
               }
            }
         }
      }
   }
<<<<<<< HEAD

class ServerThread extends Thread {
   static int number=0;
   static String ips[];
   static boolean flag;
   int id = 0;
   Socket socket = null;
   DataOutputStream out = null;
   DataInputStream in = null;

   ServerThread(Socket t, int id) {
      socket = t;
      this.id = id;
      try {
         System.out.println("现在是第"+id+"名客户"+flag);
=======
}

class ServerThread extends Thread {
   static int number;
   static String ips[] = new String[100];
   static boolean flag;
   Socket socket;
   DataOutputStream out = null;
   DataInputStream in = null;

   ServerThread(Socket t) {
      socket = t;
      try {
>>>>>>> cb848acb26530c9b8c82938894d7e3aa259a7efd
         out = new DataOutputStream(socket.getOutputStream());
         in = new DataInputStream(socket.getInputStream());
      } catch (IOException e) {
      }
   }
<<<<<<< HEAD
=======

>>>>>>> cb848acb26530c9b8c82938894d7e3aa259a7efd
   public void run() {
      while (true) {
         try {
            if (flag == true) {
               out.writeInt(number);
<<<<<<< HEAD
               out.writeInt(id);
=======
>>>>>>> cb848acb26530c9b8c82938894d7e3aa259a7efd
               for (int i = 0; i < number; i++) {
                  out.writeUTF(ips[i]);
               }
               return;
            }
         } catch (IOException e) {
            System.out.println("客户离开");
            return;
         }
      }
   }
}
