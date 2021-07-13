import java.math.BigInteger;
import java.util.Random;

public class Voter {
    int id; // 投票者的编号
    int number; // 所有投票者的数目
    private int digit; // 补充后的二进制具有几位数
    private String ips[]; // 所有投票者的地址
    Candidate candidate; // 候选人信息
    String binary; // 二进制选票信息
    BigInteger decimal; // 十进制选票信息
    BigInteger randomDec[]; // 十进制选票信息随机分成number份
    String receiveMsg[]; // 从其他投票者收到的信息
    int result[]; // 每个候选人的得票数

    /**
     * 构造函数
     * 
     * @param number    投票者的数目
     * @param candidate 候选人信息
     */
    public Voter(int number, Candidate candidate, boolean voteMsg[], String ipMsg[]) {
        this.number = number;
        this.candidate = candidate;
        digit = (int) (Math.log(number) / Math.log(2)) + 1;// 计算位数
        this.setBinary(voteMsg);
        this.setDecimal();
        this.setIps(ipMsg);
        this.setRadomDec();
    }

    /**
     * 设置二进制选票信息
     * 
     * @param voteMsg 投票信息，表示该投票人对每一个候选人是否投票
     */
    public void setBinary(boolean voteMsg[]) {
        int digit;
        for (int i = 0; i < candidate.number; i++) {
            // 每一组先补digit-1个0
            digit = this.digit;
            while (--digit != 0) {
                binary.concat("0");
            }

            // 投了在右边补1，没投补0
            if (voteMsg[i] == true) {
                binary.concat("1");
            } else {
                binary.concat("0");
            }
        }
    }

    /**
     * 把decimal随机分成number份
     * 
     */
    private void setRadomDec() {
        Random random = new Random();
        BigInteger remain = new BigInteger(decimal.toString(10)); // 表示随机分后剩余的数
        Double r;
        randomDec = new BigInteger[number];
        for (int i = 0; i < number - 1; i++) {
            r = random.nextDouble(); // 随机生成[0,1)的数给r
            BigInteger a = new BigInteger(r.toString());
            randomDec[i] = remain.multiply(a);
            remain = remain.subtract(randomDec[i]);
        }
        randomDec[number - 1] = remain; // 最后一个把剩余的数全部取走
    }

    /**
     * 用二进制计算十进制选票信息
     */
    public void setDecimal() {
        decimal = new BigInteger(binary, 2);// 这个函数把2进制的字符串转化为大整数类（十进制）
    }

    /**
     * 计算投票结果result
     */
    public void getResult() {
        // TODO
    }

    /**
     * 储存所有投票者的地址
     * 
     * @param ips
     */
    public void setIps(String ips[]) {
        this.ips = ips;
    }

    /**
     * 发送信息
     * 
     * @param msg
     * @param ip
     */
    public void send(String msg, String ip) {
        // TODO
    }

    /**
     * 接收信息
     * 
     * @param msg
     * @param ip
     */
    public void receive(String msg, String ip) {
        // TODO
    }
}
