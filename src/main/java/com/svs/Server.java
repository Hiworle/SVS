package com.svs;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

//本程序仅用于测试使用
public class Server extends Thread {
    ServerSocket server = null;
    Socket you = null;
    int number = 0;
    int numbermax;
    String serverips[];
    Candidate candidate;

    Server(Candidate candidate, int numbermax) {
        this.candidate = candidate;
        this.numbermax = numbermax;
        serverips = new String[numbermax];
        ServerThread.flag = false;
    }

    public void run() {
        while (true) {
            try {
                server = new ServerSocket(4332);
            } catch (IOException e1) {
            }
            try {
                you = server.accept();
            } catch (IOException e) {
                System.out.println("正在等待其他投票者(估计等不到了)");
            }
            if (you != null) {
                System.out.println("投票者加入人数："+Math.addExact(number,1)+"/"+numbermax);
                serverips[number] = you.getInetAddress().getHostAddress();
                new ServerThread(you, number, candidate).start(); // 为每个客户启动一个专门的线程
                number++;
                if (number == numbermax) {
                    ServerThread.number = number;
                    int i = 0;
                    for (i = 0; i < number; i++) {
                        ServerThread.ips[i] = serverips[i];
                    }
                    ServerThread.flag = true;
                    try {
                        server.close();
                        System.out.println("所有人到齐了！");
                        return;
                    } catch (Exception e) {
                        System.out.println("怎么关闭不了？");
                    }
                }
            }
        }
    }
}

class ServerThread extends Thread {
    static int number;
    static String ips[] = new String[100];
    static boolean flag;
    Socket socket;
    int id;
    Candidate candidate;
    DataOutputStream out = null;
    ObjectOutputStream out2 = null;

    ServerThread(Socket t, int id, Candidate candidate) {
        socket = t;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            out2 = new ObjectOutputStream(socket.getOutputStream());
            this.id = id;
            this.candidate = candidate;
        } catch (IOException e) {
        }
    }

    public void run() {
        while (true) {
            if (flag == true) {
                try {
                    out2.writeObject(candidate);
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
