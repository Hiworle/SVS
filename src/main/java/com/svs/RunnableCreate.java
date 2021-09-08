package com.svs;

import com.svs.controller.UserController;

public class RunnableCreate implements Runnable {

    int cddNumber;
    int voterNumber;
    Candidate candidate;
    public int[] result;
    private Thread t;

    public RunnableCreate(int cddNumber, int voterNumber, Candidate candidate) {
        this.cddNumber = cddNumber;
        this.candidate = candidate;
        this.voterNumber = voterNumber;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        String in = Formatter.toCreate(this.voterNumber + "", this.candidate);
        try {
            UserController.result = MyClient.run(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
