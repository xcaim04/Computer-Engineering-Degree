public class Main {
    public static void main(String[] args) {
        int[][] matrix = {
            {0, 1, 1, 0, 0, 0, 0, 0},
            {1, 0, 0, 1, 1, 0, 0, 0},
            {1, 0, 0, 0, 0, 1, 1, 0},
            {0, 1, 0, 0, 0, 0, 0, 1},
            {0, 1, 0, 0, 0, 0, 0, 1},
            {0, 0, 1, 0, 0, 0, 0, 1},
            {0, 0, 1, 0, 0, 0, 0, 1},
            {0, 0, 0, 1, 1, 1, 1, 0}
        };
        
        Graph graph = new Graph(matrix);
        graph.printMatrix();
        
        System.out.println("\nBFS from 0: " + graph.bfs(0));
        System.out.println("DFS with stack from 0: " + graph.dfsStack(0));
        System.out.println("DFS recursive from 0: " + graph.dfsRecursive(0));
    }
}