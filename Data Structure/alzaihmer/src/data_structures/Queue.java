package data_structures;

public class Queue<T> {
    private static class Node<T> {
        T data;
        Node<T> next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node<T> front;
    private Node<T> rear;
    private int size;
    
    public Queue() {
        front = null;
        rear = null;
        size = 0;
    }
    
    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        
        size++;
        System.out.println("[QUEUE] Encolado: " + item + " (size=" + size + ")");
    }
    
    public T dequeue() {
        if (isEmpty()) {
            System.out.println("[QUEUE] Error: cola vacia");
            return null;
        }
        
        T data = front.data;
        front = front.next;
        
        if (front == null) {
            rear = null;
        }
        
        size--;
        System.out.println("[QUEUE] Desencolado: " + data + " (size=" + size + ")");
        return data;
    }
    
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return front.data;
    }
    
    public boolean isEmpty() {
        return front == null;
    }
    
    public int size() {
        return size;
    }
}