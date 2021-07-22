import java.net.InetAddress;
import java.io.*;
import java.net.*;
//测试用客户端
public class client {
    public static void main(String[] args) throws Exception {
        Socket mysocket;
        DataOutputStream out=null;
        DataInputStream in=null;
        InetAddress localAddress=InetAddress.getLocalHost();
        int number=0;        
        String ips[];
        ips=new String[100];
        int i=0;
        try{
            mysocket=new Socket("192.168.28.130",4332);
            out=new DataOutputStream(mysocket.getOutputStream());
            in=new DataInputStream(mysocket.getInputStream());
            out.writeUTF(localAddress.getHostAddress());
            number=in.readInt();
            for(i=0;i<number;i++)
            {
                ips[i]=in.readUTF();
            }
            System.out.println("其中一位的地址是："+ips[0]);
        }
        catch(Exception e){
            System.out.println("未知错误"+e);
        }
    }
}