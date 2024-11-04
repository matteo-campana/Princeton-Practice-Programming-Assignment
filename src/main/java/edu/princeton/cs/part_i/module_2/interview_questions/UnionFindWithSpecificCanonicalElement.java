package edu.princeton.cs.module_2.interview_questions;

public class UnionFindWithSpecificCanonicalElement {
    private final int[] parent;
    private final int[] size;
    private final int[] largest;
    private final int[] smallest;

    public UnionFindWithSpecificCanonicalElement(int n) {
        parent = new int[n];
        size = new int[n];
        largest = new int[n];
        smallest = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
            largest[i] = i;
            smallest[i] = i;
        }
    }

    public int Parent(int p) {
        if (p == parent[p]) return p;
        return parent[p] = Parent(parent[p]);
    }

    public int Find(int a) {
        return largest[Parent(a)];
    }

    public void Union(int a, int b) {
        int root_a = Parent(a);
        int root_b = Parent(b);

        if (size[root_a] < size[root_b]) {
            parent[root_a] = root_b;
            size[root_b] += size[root_a];
            largest[root_b] = Math.max(largest[root_b], largest[root_a]);
            smallest[root_b] = Math.min(smallest[root_b], smallest[root_a]);
        } else {
            parent[root_b] = root_a;
            size[root_a] += size[root_b];
            largest[root_a] = Math.max(largest[root_b], largest[root_a]);
            smallest[root_a] = Math.min(smallest[root_b], smallest[root_a]);
        }
    }

    public boolean Connected(int a, int b) {
        return Parent(a) == Parent(b);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UnionFindWithSpecificCanonicalElement{");
        sb.append("parent=").append(java.util.Arrays.toString(parent));
        sb.append(", size=").append(java.util.Arrays.toString(size));
        sb.append(", largest=").append(java.util.Arrays.toString(largest));
        sb.append(", smallest=").append(java.util.Arrays.toString(smallest));
        sb.append('}');
        return sb.toString();
    }


    public static void main(String[] args) {
        int n = 10;
        UnionFindWithSpecificCanonicalElement uf = new UnionFindWithSpecificCanonicalElement(n);

        // Union some elements
        uf.Union(1, 2);
        uf.Union(3, 4);
        uf.Union(5, 6);
        uf.Union(7, 8);
        uf.Union(1, 4);
        uf.Union(5, 8);

        System.out.println(uf);

        // Test Find and Connected methods
        System.out.println("Find(1): " + uf.Find(1)); // Should print the largest element in the component containing 1
        System.out.println("Find(3): " + uf.Find(3)); // Should print the largest element in the component containing 3
        System.out.println("Connected(1, 4): " + uf.Connected(1, 4)); // Should print true
        System.out.println("Connected(1, 5): " + uf.Connected(1, 5)); // Should print false
        System.out.println("Connected(5, 8): " + uf.Connected(5, 8)); // Should print true
    }

}
