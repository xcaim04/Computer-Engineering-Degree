package data_structures;

public class Stack<T> {
    private static class Node<T> {
        T data;
        Node<T> next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node<T> top;
    private int size;
    
    public Stack() {
        top = null;
        size = 0;
    }
    
    public void push(T item) {
        Node<T> newNode = new Node<>(item);
        newNode.next = top;
        top = newNode;
        size++;
        System.out.println("[STACK] Push: " + item + " (size=" + size + ")");
    }
    
    public T pop() {
        if (isEmpty()) {
            System.out.println("[STACK] Error: pila vacia");
            return null;
        }
        
        T data = top.data;
        top = top.next;
        size--;
        System.out.println("[STACK] Pop: " + data + " (size=" + size + ")");
        return data;
    }
    
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return top.data;
    }
    
    public boolean isEmpty() {
        return top == null;
    }
    
    public int size() {
        return size;
    }
}