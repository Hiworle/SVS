public class Candidate {
    Candidate(int number) {
        this.number = number;
        name = new String[number];
        msg = new String[number];
    }

    Candidate() {
    }

    int number; // 候选人的数目
    String name[]; // 候选人的名字
    String msg[]; // 候选人的其他信息
}
