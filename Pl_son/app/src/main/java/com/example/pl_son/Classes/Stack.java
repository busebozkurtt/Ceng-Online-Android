package com.example.pl_son.Classes;

public class Stack {
    private int top;
    private Object [] elements ;
    Stack(int capacity){
        elements = new Object [capacity];
        top = -1;
    }
    public void Push(Object data) {
        if(isFull())
            System.out.println("stack overflow");
        else {
            top++;
            elements[top] = data;
        }
    }
    public Object pop() {
        if(isEmpty()){
            System.out.println("stack is empty");
            return null;
        }
        else {
            Object Data = elements[top];
            top--;
            return Data;
        }
    }
    boolean isEmpty() {
        return (top == -1);
    }
    boolean isFull() {
        return (elements.length == top +1);
    }
    public int Size() {
        return top+1;
    }

}
