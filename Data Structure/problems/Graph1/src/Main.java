public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
        
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 5);
        graph.addEdge(2, 6);
        graph.addEdge(3, 7);
        graph.addEdge(4, 7);
        graph.addEdge(5, 7);
        graph.addEdge(6, 7);
        
        graph.printGraph();
        
        System.out.println("\nBFS from 0: " + graph.bfs(0));
        System.out.println("DFS with stack from 0: " + graph.dfsStack(0));
        System.out.println("DFS recursive from 0: " + graph.dfsRecursive(0));
    }
}