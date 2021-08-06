import java.io.Serializable;

public class Candidate implements Serializable {
    Candidate(int number) {
        this.number = number;
        name = new String[number];
        msg = new String[500]; // 应该规定每个msg的最大长度为500
    }

    Candidate() {
    }

    int number; // 候选人的数目
    String name[]; // 候选人的名字
    String msg[]; // 候选人的其他信息

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name[]) {
        this.name = name;
    }

    public void setMsg(String msg[]) {
        this.msg = msg;
    }
}
