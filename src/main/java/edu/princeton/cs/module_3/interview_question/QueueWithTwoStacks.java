package edu.princeton.cs.module_3.interview_question;

import java.util.Stack;

public class QueueWithTwoStacks<T> {

    private final Stack<T> stack1;
    private final Stack<T> stack2;

    public QueueWithTwoStacks() {
        stack1 = new Stack<T>();
        stack2 = new Stack<T>();
    }

    public void Enqueue(T elem) {
        stack1.push(elem);
    }

    public T Dequeue() {
        if (stack1.isEmpty() && stack2.isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }

        while (!stack1.isEmpty()) {
            T elem = stack1.pop();
            stack2.push(elem);
        }

        return stack2.pop();
    }

    public boolean IsEmpty() {
        return true;
    }

    public static void main(String[] args) {
        QueueWithTwoStacks<Integer> queue = new QueueWithTwoStacks<>();

        queue.Enqueue(1);
        queue.Enqueue(2);
        queue.Enqueue(3);

        System.out.println(queue.Dequeue()); // Output: 1
        System.out.println(queue.Dequeue()); // Output: 2
        System.out.println(queue.IsEmpty()); // Output: false
        System.out.println(queue.Dequeue()); // Output: 3
        System.out.println(queue.IsEmpty()); // Output: true
    }
}
