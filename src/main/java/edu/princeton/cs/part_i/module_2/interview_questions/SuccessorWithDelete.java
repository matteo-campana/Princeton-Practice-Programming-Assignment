package edu.princeton.cs.part_i.module_2.interview_questions;

public class SuccessorWithDelete {
    private final int[] id;
    private final int[] sz;
    private final int[] successors;

    public SuccessorWithDelete(int n) {

        id = new int[n];
        sz = new int[n];
        successors = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
            sz[i] = 1;
            successors[i] = i;
        }
    }

    private int Root(int a) {
        if (id[a] == a) return a;
        return Root(id[a]);
    }

    private void Union(int a, int b) {
        int root_a = Root(a);
        int root_b = Root(b);

        if (sz[root_a] < sz[root_b]) {
            id[root_b] = root_a;
            sz[root_a] += sz[root_b];
            successors[root_a] = Math.max(successors[root_a], successors[root_b]);
        } else {
            id[root_a] = root_b;
            sz[root_b] += sz[root_a];
            successors[root_b] = Math.max(successors[root_a], successors[root_b]);
        }
    }

    private void RemoveElement(int a) {
        Union(a, successors[a]);
    }

    private int FindSuccessor(int a) {
        return successors[Root(a)];
    }

    public static void main(String[] args) {
        int n = 10;
        SuccessorWithDelete swd = new SuccessorWithDelete(n);

        // Remove some elements
        swd.RemoveElement(2);
        swd.RemoveElement(3);
        swd.RemoveElement(5);

        // Test FindSuccessor method
        System.out.println("Successor of 2: " + swd.FindSuccessor(2)); // Should print the successor of 2
        System.out.println("Successor of 3: " + swd.FindSuccessor(3)); // Should print the successor of 3
        System.out.println("Successor of 5: " + swd.FindSuccessor(5)); // Should print the successor of 5
        System.out.println("Successor of 6: " + swd.FindSuccessor(6)); // Should print the successor of 6
    }


}
