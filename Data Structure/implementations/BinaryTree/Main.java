package implementations.BinaryTree;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CREANDO ÁRBOL BINARIO DE BÚSQUEDA ===");
        BinaryTree arbol = new BinaryTree();

        System.out.println("\n--- Insertando valores ---");
        int[] valores = {50, 30, 20, 40, 70, 60, 80, 45, 65, 75, 85};
        
        for (int valor : valores) {
            System.out.println("Insertando: " + valor);
            arbol.insertar(valor);
        }

        System.out.println("\n--- Recorridos del árbol ---");
        
        System.out.print("In-Order (izquierda-raíz-derecha): ");
        arbol.inOrder();
        
        System.out.print("Pre-Order (raíz-izquierda-derecha): ");
        arbol.preOrder();
        
        System.out.print("Post-Order (izquierda-derecha-raíz): ");
        arbol.postOrder();

        System.out.println("\n--- Búsqueda de elementos ---");
        int[] busquedas = {40, 90, 20, 100, 45, 66, 75};
        
        for (int valor : busquedas) {
            boolean encontrado = arbol.buscar(valor);
            System.out.println("¿El valor " + valor + " está en el árbol? " + encontrado);
        }

        System.out.println("\n--- Probando valores duplicados ---");
        System.out.println("Insertando 50 (ya existe)...");
        arbol.insertar(50);
        System.out.println("Insertando 30 (ya existe)...");
        arbol.insertar(30);
        System.out.print("In-Order después de duplicados: ");
        arbol.inOrder();

        System.out.println("\n=== PROBANDO CON ÁRBOL VACÍO ===");
        BinaryTree arbolVacio = new BinaryTree();
        
        System.out.println("¿El árbol está vacío? " + arbolVacio.estaVacio());
        
        System.out.print("Recorrido In-Order (vacío): ");
        arbolVacio.inOrder();
        
        System.out.println("¿El valor 10 está en el árbol vacío? " + arbolVacio.buscar(10));

        System.out.println("\n=== PROBANDO CASOS ESPECIALES ===");
        BinaryTree arbolMinimo = new BinaryTree();
        System.out.println("Insertando un solo valor (100)...");
        arbolMinimo.insertar(100);
        
        System.out.print("In-Order: ");
        arbolMinimo.inOrder();
        
        System.out.print("Pre-Order: ");
        arbolMinimo.preOrder();
        
        System.out.print("Post-Order: ");
        arbolMinimo.postOrder();
        
        System.out.println("¿El valor 100 está en el árbol? " + arbolMinimo.buscar(100));
        System.out.println("¿El valor 999 está en el árbol? " + arbolMinimo.buscar(999));

        System.out.println("\n=== ÁRBOL ORIGINAL (TODOS LOS RECORRIDOS) ===");
        System.out.println("\nTodos los valores insertados: 50, 30, 20, 40, 70, 60, 80, 45, 65, 75, 85");
        
        System.out.println("\nRecorridos completos:");
        System.out.print("In-Order: ");
        arbol.inOrder();
        
        System.out.print("Pre-Order: ");
        arbol.preOrder();
        
        System.out.print("Post-Order: ");
        arbol.postOrder();
    }
}