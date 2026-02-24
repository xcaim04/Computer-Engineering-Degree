package implementations.GeneralTree;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== CREANDO ÁRBOL GENERAL ===");
        GeneralTree<String> arbol = new GeneralTree<>();
        
        System.out.println("\n--- Creando raíz ---");
        Node<String> raiz = new Node<>("Director");
        arbol.setRaiz(raiz);
        System.out.println("Raíz creada: " + arbol.getRaiz().getInfo());

        System.out.println("\n--- Insertando empleados (nivel 1) ---");
        arbol.insertar("Director", "Gerente Ventas");
        arbol.insertar("Director", "Gerente Marketing");
        arbol.insertar("Director", "Gerente IT");
        arbol.insertar("Director", "Gerente RRHH");

        System.out.println("\n--- Insertando subordinados (nivel 2) ---");
        arbol.insertar("Gerente Ventas", "Vendedor 1");
        arbol.insertar("Gerente Ventas", "Vendedor 2");
        arbol.insertar("Gerente Ventas", "Vendedor 3");
        
        arbol.insertar("Gerente Marketing", "Community Manager");
        arbol.insertar("Gerente Marketing", "Diseñador");
        
        arbol.insertar("Gerente IT", "Desarrollador Backend");
        arbol.insertar("Gerente IT", "Desarrollador Frontend");
        arbol.insertar("Gerente IT", "QA Tester");
        arbol.insertar("Gerente IT", "DevOps");
        
        arbol.insertar("Gerente RRHH", "Reclutador");
        arbol.insertar("Gerente RRHH", "Capacitador");

        System.out.println("\n--- Insertando niveles más profundos (nivel 3) ---");
        arbol.insertar("Desarrollador Backend", "Senior Backend");
        arbol.insertar("Desarrollador Backend", "Junior Backend");
        
        arbol.insertar("Desarrollador Frontend", "Senior Frontend");
        arbol.insertar("Desarrollador Frontend", "Junior Frontend");

        System.out.println("\n=== RECORRIDOS DEL ÁRBOL ===");
        
        arbol.imprimirBFS();
        arbol.imprimirDFSRecursivo();
        arbol.imprimirDFSPila();

        System.out.println("\n=== INFORMACIÓN DEL ÁRBOL ===");
        System.out.println("Altura del árbol: " + arbol.altura());
        System.out.println("Tamaño total (número de nodos): " + arbol.tamanio());

        System.out.println("\n=== BÚSQUEDA DE NODOS ===");
        
        System.out.println("\nBúsqueda BFS:");
        Node<String> nodoBuscado = arbol.buscarNodoBFS("QA Tester");
        if (nodoBuscado != null) {
            System.out.println("  Encontrado: " + nodoBuscado.getInfo());
            System.out.println("  Cantidad de hijos: " + nodoBuscado.cantidadHijos());
        }

        System.out.println("\nBúsqueda Recursiva:");
        nodoBuscado = arbol.buscarNodoRecursivo(arbol.getRaiz(), "Senior Frontend");
        if (nodoBuscado != null) {
            System.out.println("  Encontrado: " + nodoBuscado.getInfo());
            System.out.println("  ¿Tiene hijos? " + (nodoBuscado.tieneHijos() ? "Sí" : "No"));
        }

        System.out.println("\nBuscando nodo inexistente:");
        nodoBuscado = arbol.buscarNodoBFS("Gerente Financiero");
        System.out.println("  Resultado: " + (nodoBuscado == null ? "No encontrado" : "Encontrado"));

        System.out.println("\n=== ACTUALIZACIÓN DE NODOS ===");
        System.out.println("Antes de actualizar:");
        arbol.buscarNodoBFS("Community Manager");
        arbol.actualizarNodo("Community Manager", "Social Media Manager");
        System.out.println("Después de actualizar:");
        arbol.buscarNodoBFS("Social Media Manager");

        System.out.println("\n=== ELIMINACIÓN DE NODOS ===");
        System.out.println("Antes de eliminar - cantidad de hijos de Gerente Marketing:");
        Node<String> gerenteMarketing = arbol.buscarNodoBFS("Gerente Marketing");
        System.out.println("  Hijos: " + gerenteMarketing.getHijos());
        
        arbol.eliminarNodo("Gerente Marketing", "Diseñador");
        
        System.out.println("Después de eliminar - cantidad de hijos de Gerente Marketing:");
        gerenteMarketing = arbol.buscarNodoBFS("Gerente Marketing");
        System.out.println("  Hijos: " + gerenteMarketing.getHijos());

        System.out.println("\n=== PROBANDO CASOS DE ERROR ===");
        System.out.println("Intentando insertar sin padre existente:");
        arbol.insertar("Gerente Inexistente", "Empleado Nuevo");
        
        System.out.println("\nIntentando eliminar hijo inexistente:");
        arbol.eliminarNodo("Gerente IT", "Puesto Inexistente");
        
        System.out.println("\nIntentando actualizar nodo inexistente:");
        arbol.actualizarNodo("Nodo Inexistente", "Nuevo Valor");

        System.out.println("\n=== CREANDO ÁRBOL VACÍO ===");
        GeneralTree<Integer> arbolVacio = new GeneralTree<>();
        System.out.println("¿El árbol está vacío? " + arbolVacio.estaVacio());
        System.out.println("Altura del árbol vacío: " + arbolVacio.altura());
        System.out.println("Tamaño del árbol vacío: " + arbolVacio.tamanio());
        
        System.out.println("\nIntentando operaciones en árbol vacío:");
        arbolVacio.imprimirBFS();
        arbolVacio.imprimirDFSRecursivo();
        arbolVacio.insertar(10, 20);

        System.out.println("\n=== ÁRBOL CON NUEVA RAÍZ ===");
        GeneralTree<Integer> arbolNumeros = new GeneralTree<>(new Node<>(100));
        System.out.println("Nueva raíz: " + arbolNumeros.getRaiz().getInfo());
        
        arbolNumeros.insertar(100, 200);
        arbolNumeros.insertar(100, 300);
        arbolNumeros.insertar(200, 250);
        arbolNumeros.insertar(200, 275);
        arbolNumeros.insertar(300, 350);
        
        System.out.println("\nEstructura del árbol numérico:");
        arbolNumeros.imprimirDFSRecursivo();
        System.out.println("Altura: " + arbolNumeros.altura());
        System.out.println("Tamaño: " + arbolNumeros.tamanio());
    }
}