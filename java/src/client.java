import java.net.InetAddress;
import java.io.*;
import java.net.*;
public class client {
    public static void main(String[] args) throws Exception {
        Socket mysocket;
        DataOutputStream out=null;
        DataInputStream in=null;
        InetAddress localAddress=InetAddress.getLocalHost();
        int number=0;
        Candidate candidate=null;        
        String ips[];
        ips=new String[100];
        int i=0;
        try{
            mysocket=new Socket("192.168.28.130",4331);
            out=new DataOutputStream(mysocket.getOutputStream());
            in=new DataInputStream(mysocket.getInputStream());
            out.writeUTF(localAddress.getHostAddress());
            for(i=0;i<number-1;i++)
            {
                ips[i]=in.readUTF();
            }
        }
        catch(Exception e){
            System.out.println("未知错误"+e);
        }
    }
}