import java.util.Arrays;

public class Test {
    public void run() throws Exception {
        Candidate candidate = new Candidate(3);
        candidate.name[0] = "Alice";
        candidate.name[1] = "Bob";
        candidate.name[2] = "Cat";

        boolean voteMsg[][] = new boolean[][] { { true, false, false }, { false, false, true }, { true, true, false },
                { false, true, false }, { true, false, false }, { false, true, false }, { true, false, true } };

        Voter voter[] = new Voter[7];

        for (int i = 0; i < 7; i++) {
            voter[i] = new Voter(7, candidate, voteMsg[i], null);
        }

        for (int i = 0; i < 7; i++) {
            System.out.println("id = " + i + "; binary = " + voter[i].binary + "\ndecimal = " + voter[i].decimal
                    + "; randomDec[] = " + Arrays.toString(voter[i].randomDec) + "\nresult = " + voter[i].result);
        }
    }
}
