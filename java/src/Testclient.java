import java.io.DataInputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;

//测试用客户端
public class Testclient {
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
         mysocket = new Socket(InetAddress.getLocalHost().getHostAddress(), 4333);
         in = new DataInputStream(mysocket.getInputStream());
         number = in.readInt();
         id = in.readInt();
         System.out.println("未知错误222");
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
      for(i=0;i<number;i++)
      {
         System.out.println("收到信息的第"+i+"个"+receiveMsg[i]);
      }
      for (i = 0; i < number; i++) {
         sum = sum.add(new BigInteger(receiveMsg[i]));
      }
      System.out.println("我的计票结果是："+sum);
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
      for(i=0;i<number;i++)
      {
         System.out.println("收到信息的第"+i+"个"+me.receiveMsg[i]);
      }
      System.out.println("有几位投票人？" + me.number);
      me.getResult();
      for(i=0;i<candidate.number;i++)
      {
         System.out.println("候选人"+i+"的票数:"+me.result[i]);
      }
   }
}
