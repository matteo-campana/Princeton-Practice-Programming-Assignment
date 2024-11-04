package edu.princeton.cs.part_i.module_4;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class IntersectionOfTwoSets {

    private final Point[] a;
    private final Point[] b;

    public IntersectionOfTwoSets(int n) {
        a = new Point[n];
        b = new Point[n];
        for (int i = 0; i < n; i++) {

            Point point_a = GenerateRandomPoint();
            Point point_b = GenerateRandomPoint();
            if (i % 2 == 0) {
                a[i] = point_a;
                b[i] = point_a;
            } else {
                a[i] = point_a;
                b[i] = point_b;
            }
        }
    }

    private static void sort(Point[] arr) {
        int n = arr.length;

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                Point tmp = arr[i];
                int j;
                for (j = i; j >= gap && arr[j - gap].compareTo(tmp) > 0; j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = tmp;
            }
        }

    }

    private static boolean isSorted(Point[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1].compareTo(arr[i]) > 0) {
                return false;
            }
        }
        return true;
    }

    private Point GenerateRandomPoint() {

        Random rnd = new Random();
        int min = 1;
        int max = 101;
        int x = min + rnd.nextInt(max);
        int y = min + rnd.nextInt(max);
        return new Point(x, y);
    }


    private static class Point implements Comparable<Point> {

        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }


        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return getX() == point.getX() && getY() == point.getY();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getX(), getY());
        }

        @Override
        public int compareTo(Point p) {
            if (this.x != p.x) return Integer.compare(this.x, p.x);
            return Integer.compare(this.y, p.y);
        }
    }

    @Override
    public String toString() {
        return "IntersectionOfTwoSets{\n" +
                "a=" + Arrays.toString(a) +
                ", \nb=" + Arrays.toString(b) +
                "\n}";
    }

    public static void main(String[] args) {

        int n = 50;
        IntersectionOfTwoSets iots = new IntersectionOfTwoSets(n);

        System.out.println(iots);

        sort(iots.a);
        sort(iots.b);

        System.out.println("\nIs a[] sorted: " + isSorted(iots.a));
        System.out.println("Is b[] sorted: " + isSorted(iots.b));

        System.out.println("\n\n" + iots);
        int i = 0, j = 0;
        while (i < iots.a.length && j < iots.b.length) {

            if (iots.a[i].equals(iots.b[j])) {
                System.out.println("\na[i]: " + i + " equals b[j]: " + j);
                System.out.println("a[i]: " + iots.a[i]);
                System.out.println("b[j]: " + iots.b[j]);
                i++;
                j++;
            } else if (iots.a[i].compareTo(iots.b[j]) < 0) {
                i++;
            } else {
                j++;
            }
        }
    }
}
