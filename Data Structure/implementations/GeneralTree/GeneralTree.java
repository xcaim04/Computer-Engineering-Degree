package implementations.GeneralTree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class GeneralTree<E> {
    private Node<E> raiz;

    public GeneralTree() {
        this.raiz = null;
    }

    public GeneralTree(Node<E> raiz) {
        this.raiz = raiz;
    }

    public Node<E> getRaiz() {
        return raiz;
    }

    public void setRaiz(Node<E> raiz) {
        this.raiz = raiz;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public Node<E> buscarNodoRecursivo(Node<E> nodoActual, E valor) {
        if (nodoActual == null) {
            return null;
        }
        
        if (nodoActual.getInfo().equals(valor)) {
            return nodoActual;
        }
        
        for (Node<E> hijo : nodoActual.getHijos()) {
            Node<E> resultado = buscarNodoRecursivo(hijo, valor);
            if (resultado != null) {
                return resultado;
            }
        }
        
        return null;
    }

    public Node<E> buscarNodoBFS(E valor) {
        if (raiz == null) {
            return null;
        }
        
        Queue<Node<E>> cola = new LinkedList<>();
        cola.add(raiz);
        
        while (!cola.isEmpty()) {
            Node<E> nodoTemporal = cola.poll();
            
            if (nodoTemporal.getInfo().equals(valor)) {
                return nodoTemporal;
            }
            
            cola.addAll(nodoTemporal.getHijos());
        }
        
        return null;
    }

    public void insertar(E valorPadre, E valorNuevo) {
        if (raiz == null) {
            System.out.println("Error: El árbol está vacío. No se puede insertar sin raíz.");
            return;
        }
        
        Node<E> nodoPadre = buscarNodoRecursivo(raiz, valorPadre);
        
        if (nodoPadre != null) {
            Node<E> nuevoNodo = new Node<>(valorNuevo);
            nodoPadre.agregarHijo(nuevoNodo);
            System.out.println("Nodo " + valorNuevo + " insertado como hijo de " + valorPadre);
        } else {
            System.out.println("Error: No se encontró el nodo padre con valor " + valorPadre);
        }
    }

    public void eliminarNodo(E valorPadre, E valorHijo) {
        if (raiz == null) {
            System.out.println("Error: El árbol está vacío.");
            return;
        }
        
        Node<E> nodoPadre = buscarNodoRecursivo(raiz, valorPadre);
        
        if (nodoPadre == null) {
            System.out.println("Error: No se encontró el nodo padre con valor " + valorPadre);
            return;
        }
        
        Node<E> nodoHijo = null;
        for (Node<E> hijo : nodoPadre.getHijos()) {
            if (hijo.getInfo().equals(valorHijo)) {
                nodoHijo = hijo;
                break;
            }
        }
        
        if (nodoHijo == null) {
            System.out.println("Error: No se encontró el nodo hijo con valor " + valorHijo + " en el padre " + valorPadre);
        } else {
            nodoPadre.getHijos().remove(nodoHijo);
            System.out.println("Nodo " + valorHijo + " eliminado correctamente");
        }
    }

    public void actualizarNodo(E valorActual, E valorNuevo) {
        if (raiz == null) {
            System.out.println("Error: El árbol está vacío.");
            return;
        }
        
        Node<E> nodoActualizar = buscarNodoRecursivo(raiz, valorActual);
        
        if (nodoActualizar == null) {
            System.out.println("Error: No se encontró el nodo con valor " + valorActual);
        } else {
            nodoActualizar.setInfo(valorNuevo);
            System.out.println("Nodo actualizado: " + valorActual + " -> " + valorNuevo);
        }
    }

    public void imprimirBFS() {
        if (raiz == null) {
            System.out.println("El árbol está vacío");
            return;
        }
        
        System.out.println("\n--- Recorrido BFS (por niveles) ---");
        Queue<Node<E>> cola = new LinkedList<>();
        cola.add(raiz);
        int nivel = 0;
        
        while (!cola.isEmpty()) {
            int nodosEnNivel = cola.size();
            System.out.print("Nivel " + nivel + ": ");
            
            for (int i = 0; i < nodosEnNivel; i++) {
                Node<E> nodoActual = cola.poll();
                System.out.print(nodoActual.getInfo() + " ");
                cola.addAll(nodoActual.getHijos());
            }
            
            System.out.println();
            nivel++;
        }
    }

    public void imprimirDFSRecursivo(Node<E> nodo, int nivel) {
        if (nodo == null) {
            return;
        }
        
        for (int i = 0; i < nivel; i++) {
            System.out.print("  ");
        }
        System.out.println("|-- " + nodo.getInfo());
        
        for (Node<E> hijo : nodo.getHijos()) {
            imprimirDFSRecursivo(hijo, nivel + 1);
        }
    }

    public void imprimirDFSRecursivo() {
        if (raiz == null) {
            System.out.println("El árbol está vacío");
            return;
        }
        
        System.out.println("\n--- Recorrido DFS Recursivo (estructura de árbol) ---");
        imprimirDFSRecursivo(raiz, 0);
    }

    public void imprimirDFSPila() {
        if (raiz == null) {
            System.out.println("El árbol está vacío");
            return;
        }
        
        System.out.println("\n--- Recorrido DFS con Pila (preorden) ---");
        Stack<Node<E>> pila = new Stack<>();
        pila.push(raiz);
        
        while (!pila.isEmpty()) {
            Node<E> nodoActual = pila.pop();
            System.out.print(nodoActual.getInfo() + " ");
            
            LinkedList<Node<E>> hijos = nodoActual.getHijos();
            for (int i = hijos.size() - 1; i >= 0; i--) {
                pila.push(hijos.get(i));
            }
        }
        System.out.println();
    }

    public int altura() {
        return alturaRecursivo(raiz);
    }

    private int alturaRecursivo(Node<E> nodo) {
        if (nodo == null || nodo.getHijos().isEmpty()) {
            return 0;
        }
        
        int maxAltura = 0;
        for (Node<E> hijo : nodo.getHijos()) {
            maxAltura = Math.max(maxAltura, alturaRecursivo(hijo));
        }
        
        return maxAltura + 1;
    }

    public int tamanio() {
        return tamanioRecursivo(raiz);
    }

    private int tamanioRecursivo(Node<E> nodo) {
        if (nodo == null) {
            return 0;
        }
        
        int contador = 1;
        for (Node<E> hijo : nodo.getHijos()) {
            contador += tamanioRecursivo(hijo);
        }
        
        return contador;
    }
}