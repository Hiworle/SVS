import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.plaf.TreeUI;
import javax.xml.crypto.Data;

//测试用客户端
public class client {
   public static void main(String[] args) throws Exception {
      Socket mysocket;
      DataOutputStream out = null;
      DataInputStream in = null;
      InetAddress localAddress = InetAddress.getLocalHost();
      int number = 1;
      int id = 0; // id标识了这名投票者在IPS[]中的位置
      String ips[];
      ips = new String[100];
      int i = 0;
      // 获取所有投票者的ip地址
      try {
         System.out.println("正在呼叫服务器");
         System.out.println(localAddress.getHostAddress());
         mysocket = new Socket("192.168.240.130", 4332);
         System.out.println("localAddress.getHostAddress()");
         out = new DataOutputStream(mysocket.getOutputStream());
         in = new DataInputStream(mysocket.getInputStream());
         System.out.println("kk");
         out.writeUTF(localAddress.getHostAddress());
         number = in.readInt();
         id = in.readInt();

         for (i = 0; i < number; i++) {
            ips[i] = in.readUTF();
            System.out.println("i");
         }
         System.out.println("其中一位的地址是：" + ips[0]);
      } catch (Exception e) {
         System.out.println("未知错误222" + e);
      }
      boolean MyVoterMsg[] = new boolean[] { true, false, true };
      Voter me = new Voter(number, null, MyVoterMsg, ips);

      String receiveMsg[] = new String[number];
      BigInteger sendMsg[] = me.randomDec;
      Receive receive = new Receive(receiveMsg, sendMsg);
      Thread thread = new Thread(receive);// 在这个线程中,自己作为服务端与id大于自己的投票者进行通信
      Send send=new Send(receiveMsg, sendMsg, id,ips);
      Thread thread2 = new Thread(send);//在这个线程中，自己作为客户端与id小于自己的投票者进行通信
      thread.start();
      thread2.start();
   }
}

class Receive implements Runnable {
   String receiveMsg[];
   BigInteger sendmsg[];
   ServerSocket server = null;
   Socket you = null;
   int num = 0;// 启动子线程时，该数字用来标识子线程应该传输出去的数组编号

   Receive(String receivemsg[], BigInteger sendmsg[]) {
      this.receiveMsg = receivemsg;
      this.sendmsg = sendmsg;
   }

   public void run() {
      try {
         server = new ServerSocket(4399);
      } catch (IOException e1) {
      }
      try {
         you = server.accept();

      } catch (Exception e) {
         System.out.println("等待其他人呼叫");
      }
      if (you != null) {
         new communication(you, receiveMsg, sendmsg, num).start();
         num++;
      }
   }  
}

class Send implements Runnable{
   Socket socket=null;
   String receiveMsg[];
   BigInteger sendMsg[];
   ServerSocket server = null;
   Socket you = null;
   int id;
   int num=0;
   String ips[];
   Send(String receivemsg[], BigInteger sendMsg[],int id,String ips[])
   {
      this.id=id;
      this.receiveMsg=receivemsg;
      this.sendMsg=sendMsg;
      this.ips=ips;
   }
   public void run(){
      for(num=0;num<id;num++)
      {
         try{
         you=new Socket(ips[num],4399);
         new communication(you, receiveMsg, sendMsg, num).start();
      }catch(Exception e){
         System.out.println("未能建立套接字连接(坏消息x2)");
      }
      }
   }
}

class communication extends Thread {
   String receiveMsg[];
   BigInteger sendmsg[];
   Socket socket = null;
   DataInputStream in = null;
   DataOutputStream out = null;
   int num=0;

   communication(Socket socket, String receiveMsg[], BigInteger sendMsg[], int num) {
      this.socket = socket;
      this.receiveMsg = receiveMsg;
      this.sendmsg = sendMsg;
      this.num = num;
      try {
         in = new DataInputStream(socket.getInputStream());
         out = new DataOutputStream(socket.getOutputStream());
      } catch (IOException e) {}
   }

   public void run() {
      while (true) {
         try {
            out.writeUTF(sendmsg[num].toString());
            receiveMsg[num] = in.readUTF();
            return;
         } catch (IOException e) {
            System.out.println("正在建立连接（坏消息）");
         }
      }
   }
}   