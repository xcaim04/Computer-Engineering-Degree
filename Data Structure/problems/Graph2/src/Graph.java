import java.util.*;

public class Graph {
    
    private int[][] adjacencyMatrix;
    private int numVertices;
    
    public Graph(int[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
        this.numVertices = adjacencyMatrix.length;
    }
    
    public List<Integer> bfs(int start) {
        List<Integer> visitOrder = new ArrayList<>();
        boolean[] visited = new boolean[numVertices];
        Queue<Integer> queue = new LinkedList<>();
        
        visited[start] = true;
        queue.add(start);
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            visitOrder.add(current);
            
            for (int neighbor = 0; neighbor < numVertices; neighbor++) {
                if (adjacencyMatrix[current][neighbor] == 1 && !visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }
        
        return visitOrder;
    }
    
    public List<Integer> dfsStack(int start) {
        List<Integer> visitOrder = new ArrayList<>();
        boolean[] visited = new boolean[numVertices];
        Stack<Integer> stack = new Stack<>();
        
        stack.push(start);
        
        while (!stack.isEmpty()) {
            int current = stack.pop();
            
            if (!visited[current]) {
                visited[current] = true;
                visitOrder.add(current);
                
                for (int neighbor = numVertices - 1; neighbor >= 0; neighbor--) {
                    if (adjacencyMatrix[current][neighbor] == 1 && !visited[neighbor]) {
                        stack.push(neighbor);
                    }
                }
            }
        }
        
        return visitOrder;
    }
    
    public List<Integer> dfsRecursive(int start) {
        List<Integer> visitOrder = new ArrayList<>();
        boolean[] visited = new boolean[numVertices];
        
        dfsHelper(start, visited, visitOrder);
        return visitOrder;
    }
    
    private void dfsHelper(int current, boolean[] visited, List<Integer> visitOrder) {
        visited[current] = true;
        visitOrder.add(current);
        
        for (int neighbor = 0; neighbor < numVertices; neighbor++) {
            if (adjacencyMatrix[current][neighbor] == 1 && !visited[neighbor]) {
                dfsHelper(neighbor, visited, visitOrder);
            }
        }
    }
    
    public void printMatrix() {
        System.out.println("Adjacency Matrix:");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                System.out.print(adjacencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}