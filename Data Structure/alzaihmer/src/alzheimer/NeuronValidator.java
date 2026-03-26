package alzheimer;

import java.util.HashSet;
import java.util.Set;

import data_structures.Node;

public class NeuronValidator {
    
    public static boolean validarEstructuraArbol(Node raiz) {
        if (raiz == null) {
            System.out.println("[VALIDATOR] Raiz nula");
            return false;
        }
        
        System.out.println("[VALIDATOR] Iniciando validacion del arbol");
        
        Set<Node> visitados = new HashSet<>();
        boolean sinCiclos = validarSinCiclos(raiz, visitados);
        
        if (!sinCiclos) {
            System.out.println("[VALIDATOR] ERROR: Ciclos detectados");
            return false;
        }
        
        boolean todosAlcanzables = validarTodosAlcanzables(raiz, visitados);
        
        if (!todosAlcanzables) {
            System.out.println("[VALIDATOR] ERROR: Nodos no alcanzables desde la raiz");
            return false;
        }
        
        boolean propiedadBinaria = validarPropiedadBinaria(raiz);
        
        if (!propiedadBinaria) {
            System.out.println("[VALIDATOR] ERROR: Violacion de propiedad binaria");
            return false;
        }
        
        System.out.println("[VALIDATOR] Validacion exitosa");
        return true;
    }
    
    private static boolean validarSinCiclos(Node nodo, Set<Node> visitados) {
        if (nodo == null) {
            return true;
        }
        
        if (visitados.contains(nodo)) {
            return false;
        }
        
        visitados.add(nodo);
        
        boolean izquierdoOk = validarSinCiclos(nodo.hijoIzquierdo, visitados);
        boolean derechoOk = validarSinCiclos(nodo.hijoDerecho, visitados);
        
        return izquierdoOk && derechoOk;
    }
    
    private static boolean validarTodosAlcanzables(Node raiz, Set<Node> alcanzables) {
        Set<Node> todosNodos = new HashSet<>();
        recolectarNodos(raiz, todosNodos);
        
        return todosNodos.equals(alcanzables);
    }
    
    private static void recolectarNodos(Node nodo, Set<Node> nodos) {
        if (nodo == null) {
            return;
        }
        
        nodos.add(nodo);
        recolectarNodos(nodo.hijoIzquierdo, nodos);
        recolectarNodos(nodo.hijoDerecho, nodos);
    }
    
    private static boolean validarPropiedadBinaria(Node nodo) {
        if (nodo == null) {
            return true;
        }
        
        int numHijos = nodo.contarHijos();
        if (numHijos > 2) {
            System.out.println("[VALIDATOR] Nodo " + nodo.id + " tiene " + numHijos + " hijos");
            return false;
        }
        
        boolean izquierdoOk = validarPropiedadBinaria(nodo.hijoIzquierdo);
        boolean derechoOk = validarPropiedadBinaria(nodo.hijoDerecho);
        
        return izquierdoOk && derechoOk;
    }
}