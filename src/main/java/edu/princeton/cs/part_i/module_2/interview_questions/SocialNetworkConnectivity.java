package edu.princeton.cs.part_i.module_2.interview_questions;

//<hr></hr> Social network connectivity
//Given a social network containing ( n ) members and a log file containing ( m ) timestamps at which times pairs of members formed friendships,
//design an algorithm to determine the earliest time at which all members are
//connected (i.e., every member is a friend of a friend of a friend ... of a friend).
//Assume that the log file is sorted by timestamp and that friendship is an equivalence relation.
//The running time of your algorithm should be ( m \log n )
//or better and use extra space proportional to ( n ).<hr></hr>


import java.util.ArrayList;

public class SocialNetworkConnectivity {

    private final int n;
    private final int m;
    private final ArrayList<ArrayList<Integer>> members_connectivity;

    private final int[] id;
    private final int[] sz;

    private boolean isMValid(int n, int m) {
        return m >= n - 1;
    }

    public SocialNetworkConnectivity(int n, int m) {
        this.n = n;
        this.m = m;

        if (!isMValid(n, m)) {
            throw new IllegalArgumentException("Invalid m value");
        }

        this.members_connectivity = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            ArrayList<Integer> member = new ArrayList<>();
            member.add(i);
            int second_member;
            do {
                second_member = (int) (Math.random() * n);
            } while (second_member == i);
            member.add(second_member);
            members_connectivity.add(member);
        }

        id = new int[n];
        sz = new int[n];

        for (int i = 0; i < n; i++) {
            id[i] = i;
            sz[i] = 1;
        }
    }

    @Override
    public String toString() {
        return "SocialNetworkConnectivity{" + "n=" + n + ", m=" + m + ", members_connectivity=" + members_connectivity + '}';
    }

    private int GetRoot(int key) {
        if (id[key] == key) {
            return this.id[key];
        }

        return GetRoot(this.id[key]);
    }

    private void Union(int a, int b) {
        int root_a = GetRoot(a);
        int root_b = GetRoot(b);

        if (sz[root_a] < sz[root_b]) {
            id[root_a] = root_b;
            sz[root_b] += sz[root_a];
        } else {
            id[root_b] = root_a;
            sz[root_a] += sz[root_b];
        }
    }


    public static void main(String[] args) {
        SocialNetworkConnectivity socialNetworkConnectivity = new SocialNetworkConnectivity(10, 10);
        System.out.println(socialNetworkConnectivity);

        for (int i = 0; i < socialNetworkConnectivity.members_connectivity.size(); i++) {
            socialNetworkConnectivity.Union(socialNetworkConnectivity.members_connectivity.get(i).get(0), socialNetworkConnectivity.members_connectivity.get(i).get(1));
        }

        // print sz array
        for (int i = 0; i < socialNetworkConnectivity.sz.length; i++) {
            System.out.println("["+i+"] depth:"+socialNetworkConnectivity.sz[i]);
        }
    }

}
