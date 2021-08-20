package com.svs;

import com.svs.controller.UserController;

public class RunnableCreate implements Runnable {

    int cddNumber;
    int voterNumber;
    Candidate candidate;
    String[] voteMsg;
    public int[] result;
    private Thread t;

    public RunnableCreate(int cddNumber, int voterNumber, Candidate candidate, String[] voteMsg) {
        this.cddNumber = cddNumber;
        this.candidate = candidate;
        this.voterNumber = voterNumber;
        this.voteMsg = voteMsg;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        String in = Formatter.toCreate(this.voterNumber + "", this.candidate, this.voteMsg);
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
