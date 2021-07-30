import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.Thread.State;
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
      int number=0;
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
      } catch (Exception e) {
         System.out.println("未知错误222" + e);
      }
      System.out.println(number);
      me = new Voter(number, null, MyVoterMsg, ips);
      System.out.println("hello");
      receiveMsg = new String[number];
      receiveMsg[id] = me.randomDec[id].toString();
      sendMsg = me.randomDec;
      Receive receive = new Receive(receiveMsg, sendMsg);
      Thread thread = new Thread(receive);// 在这个线程中,自己作为服务端与id大于自己的投票者进行通信
      Send send = new Send(receiveMsg, sendMsg, id, ips);
      Thread thread2 = new Thread(send);// 在这个线程中，自己作为客户端与id小于自己的投票者进行通信
      thread.start();
      thread2.start();
      if (thread.getState() == State.TERMINATED && thread2.getState() == State.TERMINATED) {
         for (i = 0; i < number - 1; i++) {
            sum.add(new BigInteger(receiveMsg[i]));
         }
         receiveMsg2 = new String[number];
         receiveMsg2[id] = sum.toString();
         Receive receive2 = new Receive(receiveMsg2, sum);
         Thread thread3 = new Thread(receive2);// 在这个线程中,自己作为服务端与id大于自己的投票者进行通信
         Send send2 = new Send(receiveMsg2, sum, id, ips);
         Thread thread4 = new Thread(send2);// 在这个线程中，自己作为客户端与id小于自己的投票者进行通信
         thread3.start();
         thread4.start();
         if (thread3.getState() == State.TERMINATED && thread4.getState() == State.TERMINATED) {
            me.receiveMsg = receiveMsg2;
            me.getResult();
         }
      }
   }
}

class Receive implements Runnable {
   String receiveMsg[];
   BigInteger sendMsg[];
   ServerSocket server = null;
   Socket you = null;
   int num = 0;// 启动子线程时，该数字用来标识子线程应该传输出去的数组编号
   BigInteger s;
   int window;

   Receive(String receiveMsg[], BigInteger sendMsg[]) {
      this.receiveMsg = receiveMsg;
      this.sendMsg = sendMsg;
      window = 0;
   }

   Receive(String receiveMsg[], BigInteger s) {
      this.receiveMsg = receiveMsg;
      this.s = s;
      window = 1;
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
         if (window == 0) {
            new communication(you, receiveMsg, sendMsg, num, true).start();
            num++;
         } else if (window == 1) {
            new communication(you, receiveMsg, s, num, true).start();
            num++;
         }
      }
   }
}

class Send implements Runnable {
   Socket socket = null;
   String receiveMsg[];
   BigInteger sendMsg[];
   ServerSocket server = null;
   BigInteger s;
   Socket you = null;
   int id;
   int num = 0;
   int window;
   String ips[];

   Send(String receiveMsg[], BigInteger sendMsg[], int id, String ips[]) {
      this.receiveMsg = receiveMsg;
      this.sendMsg = sendMsg;
      this.id = id;
      this.ips = ips;
      window = 0;
   }

   Send(String receiveMsg[], BigInteger s, int id, String ips[]) {
      this.receiveMsg = receiveMsg;
      this.s = s;
      this.id = id;
      this.ips = ips;
      window = 1;
   }

   public void run() {
      if (window == 0) {
         for (num = 0; num < id; num++) {
            try {
               you = new Socket(ips[num], 4399);
               new communication(you, receiveMsg, sendMsg, num, false).start();
            } catch (Exception e) {
               System.out.println("未能建立套接字连接(坏消息x2)");
            }
         }
      } else if (window == 1) {
         for (num = 0; num < id; num++) {
            try {
               you = new Socket(ips[num], 4399);
               new communication(you, receiveMsg, s, num, false).start();
            } catch (Exception e) {
               System.out.println("未能建立套接字连接(坏消息x2)");
            }
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
   BigInteger s;
   int num = 0;
   int window;
   boolean choice;

   communication(Socket socket, String receiveMsg[], BigInteger sendMsg[], int num, boolean choice) {
      this.socket = socket;
      this.receiveMsg = receiveMsg;
      this.sendmsg = sendMsg;
      this.num = num;
      this.choice = choice;
      window = 0;
      try {
         in = new DataInputStream(socket.getInputStream());
         out = new DataOutputStream(socket.getOutputStream());
      } catch (IOException e) {
      }
   }

   communication(Socket socket, String receiveMsg[], BigInteger s, int num, boolean choice) {
      this.socket = socket;
      this.receiveMsg = receiveMsg;
      this.s = s;
      this.choice = choice;
      window = 1;
      try {
         in = new DataInputStream(socket.getInputStream());
         out = new DataOutputStream(socket.getOutputStream());
      } catch (IOException e) {
      }
   }

   public void run() {
      if (window == 0) {
         while (true) {
            try {
               if (choice == false) {
                  out.writeUTF(sendmsg[num].toString());
                  receiveMsg[num] = in.readUTF();
               }
               return;
            } catch (IOException e) {
               System.out.println("正在建立连接（坏消息）");
            }
         }
      }
      if (window == 1) {
         while (true) {
            try {
               if (choice == true)//choice代表是receive还是send创建的这个类，两种choice意味着是先发送数还是先收数据
                  receiveMsg[num] = in.readUTF();
               out.writeUTF(s.toString());
               return;
            } catch (IOException e) {
               System.out.println("正在建立连接（坏消息）");
            }
         }
      }
   }
}