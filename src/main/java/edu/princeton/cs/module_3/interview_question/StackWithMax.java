package edu.princeton.cs.module_3.interview_question;

import java.util.Stack;


public class StackWithMax {
    private Stack<Double> mainStack;  // Main stack to store elements
    private Stack<Double> maxStack;   // Stack to store maximums

    // Constructor to initialize both stacks
    public StackWithMax() {
        mainStack = new Stack<>();
        maxStack = new Stack<>();
    }

    // Push element onto the stack
    public void push(double value) {
        mainStack.push(value); // Push value onto the main stack

        // Push the maximum value so far onto the max stack
        if (maxStack.isEmpty()) {
            maxStack.push(value);  // If max stack is empty, push the current value
        } else {
            double currentMax = maxStack.peek();
            maxStack.push(Math.max(currentMax, value));  // Push the greater of the two
        }
    }

    // Pop element from the stack
    public double pop() {
        if (mainStack.isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }

        maxStack.pop(); // Pop from the max stack
        return mainStack.pop(); // Pop from the main stack
    }

    // Get the current maximum value
    public double getMax() {
        if (maxStack.isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }

        return maxStack.peek();  // The top of maxStack contains the maximum element
    }

    // Check if the stack is empty
    public boolean isEmpty() {
        return mainStack.isEmpty();
    }

    public static void main(String[] args) {
        StackWithMax stack = new StackWithMax();

        stack.push(3.0);
        stack.push(5.0);
        System.out.println(stack.getMax()); // Output: 5.0
        stack.push(2.0);
        stack.push(1.0);
        System.out.println(stack.getMax()); // Output: 5.0
        stack.pop();
        stack.pop();
        System.out.println(stack.getMax()); // Output: 5.0
        stack.pop();
        System.out.println(stack.getMax()); // Output: 3.0
    }
}
