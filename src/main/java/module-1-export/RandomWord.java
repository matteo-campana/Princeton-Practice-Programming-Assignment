import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {

        String ans = null;
        double count = 0;

        while (!StdIn.isEmpty()) {
            count++;
            String word = StdIn.readString();
            if (StdRandom.bernoulli(1 / count)) {
                ans = word;
            }
        }

        if (ans != null) {
            StdOut.println(ans);
        }
    }
}
