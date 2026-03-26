package data_structures;

import java.util.*;

public class TreeTraversal {
    
    public static int calcularOrdenStrahler(Node nodo) {
        if (nodo == null) {
            return 0;
        }
        
        if (nodo.esHoja()) {
            nodo.strahler = 1;
            System.out.println("[POSTORDER] Nodo " + nodo.id + " (hoja) -> Strahler = 1");
            return 1;
        }
        
        int ordenIzq = calcularOrdenStrahler(nodo.hijoIzquierdo);
        int ordenDer = calcularOrdenStrahler(nodo.hijoDerecho);
        
        if (nodo.hijoIzquierdo != null && nodo.hijoDerecho != null) {
            if (ordenIzq == ordenDer) {
                nodo.strahler = ordenIzq + 1;
            } else {
                nodo.strahler = Math.max(ordenIzq, ordenDer);
            }
        } else if (nodo.hijoIzquierdo != null) {
            nodo.strahler = ordenIzq;
        } else if (nodo.hijoDerecho != null) {
            nodo.strahler = ordenDer;
        }
        
        System.out.println("[POSTORDER] Nodo " + nodo.id + " -> Strahler = " + nodo.strahler);
        return nodo.strahler;
    }
    
    public static Map<Integer, Double> calcularDEEConDFS(Node raiz) {
        Map<Integer, Double> deeMap = new HashMap<>();
        
        if (raiz == null) {
            return deeMap;
        }
        
        dfsPreorderAcumulativo(raiz, 0.0, deeMap);
        
        System.out.println("[DFS] DEE calculada para " + deeMap.size() + " nodos");
        return deeMap;
    }
    
    private static void dfsPreorderAcumulativo(Node nodo, double deeAcumulada, Map<Integer, Double> deeMap) {
        if (nodo == null) {
            return;
        }
        
        if (nodo.padre != null) {
            double lambda = calcularConstanteLongitud(nodo.diametroMedioArista);
            double contribucion = nodo.longitudArista / lambda;
            nodo.dee = deeAcumulada + contribucion;
        } else {
            nodo.dee = 0.0;
        }
        
        deeMap.put(nodo.id, nodo.dee);
        
        System.out.println("[DFS] Nodo " + nodo.id + " -> DEE = " + nodo.dee);
        
        dfsPreorderAcumulativo(nodo.hijoIzquierdo, nodo.dee, deeMap);
        dfsPreorderAcumulativo(nodo.hijoDerecho, nodo.dee, deeMap);
    }
    
    private static double calcularConstanteLongitud(double diametro) {
        double Rm = 5000.0;
        double Ri = 150.0;
        double diametroCm = diametro * 1e-4;
        double lambdaCm = Math.sqrt((diametroCm * Rm) / (4.0 * Ri));
        double lambdaUm = lambdaCm * 1e4;
        
        System.out.println("[DFS] Lambda calculado: diametro=" + diametro + "μm -> λ=" + lambdaUm + "μm");
        return lambdaUm;
    }
    
    public static List<Map<String, Object>> analizarPorNivelesBFS(Node raiz) {
        List<Map<String, Object>> niveles = new ArrayList<>();
        
        if (raiz == null) {
            return niveles;
        }
        
        Queue<Node> cola = new Queue<>();
        cola.enqueue(raiz);
        
        int nivelActual = 0;
        
        while (!cola.isEmpty()) {
            int nivelSize = cola.size();
            int totalNodosNivel = 0;
            int bifurcacionesNivel = 0;
            
            for (int i = 0; i < nivelSize; i++) {
                Node nodo = cola.dequeue();
                totalNodosNivel++;
                
                if (nodo.tipo == Node.NodeType.BIFURCACION) {
                    bifurcacionesNivel++;
                }
                
                if (nodo.hijoIzquierdo != null) {
                    cola.enqueue(nodo.hijoIzquierdo);
                }
                if (nodo.hijoDerecho != null) {
                    cola.enqueue(nodo.hijoDerecho);
                }
            }
            
            double densidad = totalNodosNivel > 0 ? (double) bifurcacionesNivel / totalNodosNivel : 0.0;
            
            Map<String, Object> nivelInfo = new HashMap<>();
            nivelInfo.put("nivel", nivelActual);
            nivelInfo.put("nodos", totalNodosNivel);
            nivelInfo.put("bifurcaciones", bifurcacionesNivel);
            nivelInfo.put("densidad", densidad);
            niveles.add(nivelInfo);
            
            System.out.println("[BFS] Nivel " + nivelActual + ": nodos=" + totalNodosNivel + 
                             ", bifurcaciones=" + bifurcacionesNivel + ", densidad=" + densidad);
            
            nivelActual++;
        }
        
        return niveles;
    }
}