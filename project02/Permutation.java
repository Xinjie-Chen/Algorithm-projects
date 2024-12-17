/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Please provide a single integer argument");
        }
        int i = 0;
        int k = Integer.parseInt(args[0]);
        if (k == 0) {
            return;
        }
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        String item;
        for (i = 1; i <= k; ++i) {
            item = StdIn.readString();
            rq.enqueue(item);
        }
        while (!StdIn.isEmpty()) {
            item = StdIn.readString();
            rq.dequeue();
            rq.enqueue(item);
        }
        for (String e : rq) {
            System.out.println(e);
        }
    }
}
