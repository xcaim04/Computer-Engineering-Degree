import java.util.*;

public class Graph {
    

     /* This is to optimize the structure, i know i should have used a 
       normal Linked List, but i think this is more faster...

       Carlos Blanco
     */

    private Map<Integer, List<Integer>> adjacencyList;
    
    public Graph() {
        adjacencyList = new HashMap<>();
    }
    
    public void addVertex(int vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new ArrayList<>());
        }
    }
    
    public void addEdge(int vertexA, int vertexB) {
        addVertex(vertexA);
        addVertex(vertexB);
        adjacencyList.get(vertexA).add(vertexB);
        adjacencyList.get(vertexB).add(vertexA);
    }
    
    public void addDirectedEdge(int from, int to) {
        addVertex(from);
        addVertex(to);
        adjacencyList.get(from).add(to);
    }
    
    public List<Integer> bfs(int start) {
        List<Integer> visitOrder = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        
        if (!adjacencyList.containsKey(start)) {
            return visitOrder;
        }
        
        visited.add(start);
        queue.add(start);
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            visitOrder.add(current);
            
            List<Integer> neighbors = new ArrayList<>(adjacencyList.get(current));
            Collections.sort(neighbors);
            
            for (int neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        
        return visitOrder;
    }
    
    public List<Integer> dfsStack(int start) {
        List<Integer> visitOrder = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();
        
        if (!adjacencyList.containsKey(start)) {
            return visitOrder;
        }
        
        stack.push(start);
        
        while (!stack.isEmpty()) {
            int current = stack.pop();
            
            if (!visited.contains(current)) {
                visited.add(current);
                visitOrder.add(current);
                
                List<Integer> neighbors = new ArrayList<>(adjacencyList.get(current));
                Collections.sort(neighbors);
                
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    int neighbor = neighbors.get(i);
                    if (!visited.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }
        
        return visitOrder;
    }
    
    public List<Integer> dfsRecursive(int start) {
        List<Integer> visitOrder = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        
        if (!adjacencyList.containsKey(start)) {
            return visitOrder;
        }
        
        dfsHelper(start, visited, visitOrder);
        return visitOrder;
    }
    
    private void dfsHelper(int current, Set<Integer> visited, List<Integer> visitOrder) {
        visited.add(current);
        visitOrder.add(current);
        
        List<Integer> neighbors = new ArrayList<>(adjacencyList.get(current));
        Collections.sort(neighbors);
        
        for (int neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfsHelper(neighbor, visited, visitOrder);
            }
        }
    }
    
    public void printGraph() {
        System.out.println("Adjacency List:");
        for (int vertex : adjacencyList.keySet()) {
            List<Integer> neighbors = new ArrayList<>(adjacencyList.get(vertex));
            Collections.sort(neighbors);
            System.out.println(vertex + " -> " + neighbors);
        }
    }
}