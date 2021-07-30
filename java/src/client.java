import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

//测试用客户端
public class client {
   public static void main(String[] args) throws Exception {
      Socket mysocket;
      DataInputStream in = null;
      boolean MyVoterMsg[] = new boolean[] { true, false, true };// 正式应用中，需要根据客户选项来提供这个数组
      Voter me = null;
      String receiveMsg[];
      BigInteger sendMsg[];
      String receiveMsg2[];
      BigInteger sendMsg2[];
      int number = 0;
      int id = 0; // id标识了这名投票者在IPS[]中的位置
      String ips[];
      ips = new String[100];
      int i = 0;
      BigInteger sum = new BigInteger("0");
      // 获取所有投票者的ip地址
      try {
         System.out.println("正在呼叫服务器");
         mysocket = new Socket(InetAddress.getLocalHost().getHostAddress(), 4332);
         System.out.println(InetAddress.getLocalHost().getHostAddress());
         in = new DataInputStream(mysocket.getInputStream());
         number = in.readInt();
         id = in.readInt();
         for (i = 0; i < number; i++) {
            ips[i] = in.readUTF();
         }
         System.out.println("其中一位的地址是：" + ips[0]);
         System.out.println("我是第" + id + "位投票者client1");
      } catch (Exception e) {
         System.out.println("未知错误222" + e);
      }

      Candidate candidate = new Candidate(3);
      me = new Voter(number, candidate, MyVoterMsg, ips);
      receiveMsg = new String[number];
      receiveMsg[id] = me.randomDec[id].toString();
      sendMsg = me.randomDec;
      Receive receive = new Receive(receiveMsg, sendMsg, id, number);
      Thread thread = new Thread(receive);// 在这个线程中,自己作为服务端与id大于自己的投票者进行通信
      Send send = new Send(receiveMsg, sendMsg, id, ips);
      Thread thread2 = new Thread(send);// 在这个线程中，自己作为客户端与id小于自己的投票者进行通信
      thread.start();
      thread2.start();
      thread.join();
      thread2.join();
      System.out.println("第一次数据传输完成");
      System.out.println("这次收到信息的第一个" + receiveMsg[0]);
      System.out.println("收到信息的第二个" + receiveMsg[1]);

      for (i = 0; i < number; i++) {
         sum = sum.add(new BigInteger(receiveMsg[i]));
      }
      receiveMsg2 = new String[number];
      receiveMsg2[id] = sum.toString();
      sendMsg2 = new BigInteger[number];
      for (i = 0; i < number; i++) {
         sendMsg2[i] = sum;
      }
      Receive receive2 = new Receive(receiveMsg2, sendMsg2, id, number);
      Thread thread3 = new Thread(receive2);// 在这个线程中,自己作为服务端与id大于自己的投票者进行通信
      Send send2 = new Send(receiveMsg2, sendMsg2, id, ips);
      Thread thread4 = new Thread(send2);// 在这个线程中，自己作为客户端与id小于自己的投票者进行通信
      thread3.start();
      thread4.start();
      thread3.join();
      thread4.join();
      System.out.println("第三阶段完成（吃苹果）");
      me.receiveMsg = receiveMsg2;
      System.out.println("收到信息的第一个" + me.receiveMsg[0]);
      System.out.println("收到信息的第二个" + me.receiveMsg[1]);
      System.out.println("有几位投票人？" + me.number);
      me.getResult();
      System.out.println("候选人1的票数：" + me.result[0]);
   }
}

class Receive implements Runnable {// 接听者
   String receiveMsg[];
   BigInteger sendMsg[];
   int number;
   int id;
   ServerSocket server = null;
   Socket you = null;
   int num = 0;

   Receive(String receiveMsg[], BigInteger sendMsg[], int id, int number) {
      this.receiveMsg = receiveMsg;
      this.sendMsg = sendMsg;
      this.id = id;
      this.number = number;
   }

   public void run() {
      if (number == 1) {
         return;
      }
      try {
         server = new ServerSocket(4399);
      } catch (IOException e1) {
      }
      id++;
      communication threads[] = new communication[number - id];
      while (id < number) {
         try {
            you = server.accept();
         } catch (IOException e) {
            System.out.println("等待其他人呼叫");
         }
         try {
            Thread.sleep(500);
         } catch (InterruptedException e) {
         }
         if (you != null) {
            threads[num] = new communication(you, receiveMsg, sendMsg[id], id, true);
            threads[num++].start();
            id++;
         }
      }
      for (num--; num >= 0; num--) {
         try {
            threads[num].join();
         } catch (Exception e) {
         }
      }
      try {
         server.close();
      } catch (Exception e) {
      } // 必须要有
   }
}

class Send implements Runnable {// 拨打者
   Socket socket = null;
   String receiveMsg[];
   BigInteger sendMsg[];
   ServerSocket server = null;
   Socket you = null;
   int id;
   int num = 0;
   String ips[];

   Send(String receiveMsg[], BigInteger sendMsg[], int id, String ips[]) {
      this.receiveMsg = receiveMsg;
      this.sendMsg = sendMsg;
      this.id = id;
      this.ips = ips;
      num = 0;
   }

   public void run() {
      communication threads[] = new communication[id];
      while (num < id) {
         try {
            you = new Socket(ips[num], 4399);
            if (you != null) {
               threads[num] = new communication(you, receiveMsg, sendMsg[num], num, false);
               threads[num].start();
               num++;
            }
            you = null;
         } catch (Exception e) {
         }
      }
      for (; num >= 0; num--) {
         try {
            threads[num].join();
         } catch (Exception e) {
         }
      }
   }
}

class communication extends Thread {
   String receiveMsg[];
   Socket socket = null;
   DataInputStream in = null;
   DataOutputStream out = null;
   BigInteger sendMsg;
   int num = 0;
   boolean choice;

   communication(Socket socket, String receiveMsg[], BigInteger sendMsg, int num, boolean choice) {
      this.socket = socket;
      this.receiveMsg = receiveMsg;
      this.sendMsg = sendMsg;
      this.num = num;
      this.choice = choice;
      try {
         in = new DataInputStream(socket.getInputStream());
         out = new DataOutputStream(socket.getOutputStream());
      } catch (IOException e) {
      }
   }

   public void run() {
      if (choice == false) {
         try {
            out.writeUTF(sendMsg.toString());
            receiveMsg[num] = in.readUTF();
            return;
         } catch (IOException e) {
            System.out.println("正在建立连接（坏消息）+1");
         }
      } else if (choice == true) {
         try {
            if (choice == true) {
               receiveMsg[num] = in.readUTF();
               out.writeUTF(sendMsg.toString());
               return;
            }
         } catch (IOException e) {
            System.out.println("正在建立连接（坏消息）");
         }
      }
   }
}