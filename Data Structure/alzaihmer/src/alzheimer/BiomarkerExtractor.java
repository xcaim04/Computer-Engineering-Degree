// BiomarkerExtractor.java
package alzheimer;

import java.util.*;

import data_structures.Node;
import data_structures.Stack;
import data_structures.TreeTraversal;

public class BiomarkerExtractor {
    
    public static Map<String, Double> extraerBiomarcadores(Node raiz) {
        Map<String, Double> biomarkers = new HashMap<>();
        
        System.out.println("[BIOMARKER] Iniciando extraccion de biomarcadores");
        
        TreeTraversal.calcularOrdenStrahler(raiz);
        
        int strahlerMax = calcularStrahlerMax(raiz);
        double strahlerMean = calcularStrahlerMean(raiz);
        double icat = calcularICAT(raiz);
        int terminalCount = contarTerminales(raiz);
        double bifurcationRatio = calcularRelacionBifurcacion(raiz);
        
        Map<Integer, Double> deeMap = TreeTraversal.calcularDEEConDFS(raiz);
        double[] deeStats = calcularEstadisticasDEE(raiz, deeMap);
        
        List<Map<String, Object>> niveles = TreeTraversal.analizarPorNivelesBFS(raiz);
        double centroid = calcularCentroideRamificacion(niveles);
        double ird = calcularIRD(niveles);
        
        biomarkers.put("strahler_max", (double) strahlerMax);
        biomarkers.put("strahler_mean", strahlerMean);
        biomarkers.put("icat", icat);
        biomarkers.put("terminal_count", (double) terminalCount);
        biomarkers.put("bifurcation_ratio", bifurcationRatio);
        biomarkers.put("dee_mean", deeStats[0]);
        biomarkers.put("dee_max", deeStats[1]);
        biomarkers.put("centroid", centroid);
        biomarkers.put("ird", ird);
        
        System.out.println("[BIOMARKER] Extraccion completada");
        System.out.println("[BIOMARKER] Resultados: " + biomarkers);
        return biomarkers;
    }
    
    private static int calcularStrahlerMax(Node raiz) {
        int maxStrahler = 0;
        Stack<Node> pila = new Stack<>();
        pila.push(raiz);
        
        while (!pila.isEmpty()) {
            Node nodo = pila.pop();
            if (nodo.strahler > maxStrahler) {
                maxStrahler = nodo.strahler;
            }
            if (nodo.hijoIzquierdo != null) pila.push(nodo.hijoIzquierdo);
            if (nodo.hijoDerecho != null) pila.push(nodo.hijoDerecho);
        }
        
        System.out.println("[BIOMARKER] Strahler maximo: " + maxStrahler);
        return maxStrahler;
    }
    
    private static double calcularStrahlerMean(Node raiz) {
        double sumaPonderada = 0.0;
        double sumaLongitudes = 0.0;
        
        Stack<Node> pila = new Stack<>();
        pila.push(raiz);
        
        while (!pila.isEmpty()) {
            Node nodo = pila.pop();
            
            double longitud = 0.0;
            if (nodo.padre != null) {
                longitud = nodo.longitudArista;
            }
            
            sumaPonderada += nodo.strahler * longitud;
            sumaLongitudes += longitud;
            
            if (nodo.hijoIzquierdo != null) pila.push(nodo.hijoIzquierdo);
            if (nodo.hijoDerecho != null) pila.push(nodo.hijoDerecho);
        }
        
        double mean = sumaLongitudes > 0 ? sumaPonderada / sumaLongitudes : 0.0;
        System.out.println("[BIOMARKER] Strahler medio ponderado: " + mean);
        return mean;
    }
    
    private static double calcularICAT(Node raiz) {
        double icat = 0.0;
        
        Stack<Node> pila = new Stack<>();
        pila.push(raiz);
        
        while (!pila.isEmpty()) {
            Node nodo = pila.pop();
            
            double longitud = 0.0;
            if (nodo.padre != null) {
                longitud = nodo.longitudArista;
            }
            
            icat += nodo.strahler * longitud;
            
            if (nodo.hijoIzquierdo != null) pila.push(nodo.hijoIzquierdo);
            if (nodo.hijoDerecho != null) pila.push(nodo.hijoDerecho);
        }
        
        System.out.println("[BIOMARKER] ICAT: " + icat);
        return icat;
    }
    
    private static int contarTerminales(Node raiz) {
        int count = 0;
        Stack<Node> pila = new Stack<>();
        pila.push(raiz);
        
        while (!pila.isEmpty()) {
            Node nodo = pila.pop();
            
            if (nodo.tipo == Node.NodeType.TERMINAL) {
                count++;
            }
            
            if (nodo.hijoIzquierdo != null) pila.push(nodo.hijoIzquierdo);
            if (nodo.hijoDerecho != null) pila.push(nodo.hijoDerecho);
        }
        
        System.out.println("[BIOMARKER] Terminales: " + count);
        return count;
    }
    
    private static double calcularRelacionBifurcacion(Node raiz) {
        int bifurcaciones = 0;
        int terminales = 0;
        
        Stack<Node> pila = new Stack<>();
        pila.push(raiz);
        
        while (!pila.isEmpty()) {
            Node nodo = pila.pop();
            
            if (nodo.tipo == Node.NodeType.BIFURCACION) {
                bifurcaciones++;
            }
            if (nodo.tipo == Node.NodeType.TERMINAL) {
                terminales++;
            }
            
            if (nodo.hijoIzquierdo != null) pila.push(nodo.hijoIzquierdo);
            if (nodo.hijoDerecho != null) pila.push(nodo.hijoDerecho);
        }
        
        double ratio = terminales > 0 ? (double) bifurcaciones / terminales : 0.0;
        System.out.println("[BIOMARKER] Relacion Bifurcacion/Terminal: " + ratio);
        return ratio;
    }
    
    private static double[] calcularEstadisticasDEE(Node raiz, Map<Integer, Double> deeMap) {
        double sumaDEE = 0.0;
        double maxDEE = 0.0;
        int countTerminales = 0;
        
        Stack<Node> pila = new Stack<>();
        pila.push(raiz);
        
        while (!pila.isEmpty()) {
            Node nodo = pila.pop();
            
            if (nodo.tipo == Node.NodeType.TERMINAL) {
                double dee = deeMap.getOrDefault(nodo.id, 0.0);
                sumaDEE += dee;
                if (dee > maxDEE) {
                    maxDEE = dee;
                }
                countTerminales++;
            }
            
            if (nodo.hijoIzquierdo != null) pila.push(nodo.hijoIzquierdo);
            if (nodo.hijoDerecho != null) pila.push(nodo.hijoDerecho);
        }
        
        double meanDEE = countTerminales > 0 ? sumaDEE / countTerminales : 0.0;
        System.out.println("[BIOMARKER] DEE media: " + meanDEE + ", DEE max: " + maxDEE);
        
        return new double[]{meanDEE, maxDEE};
    }
    
    private static double calcularCentroideRamificacion(List<Map<String, Object>> niveles) {
        double sumaNumerador = 0.0;
        double sumaDenominador = 0.0;
        
        for (Map<String, Object> nivel : niveles) {
            int nivelNum = (int) nivel.get("nivel");
            double densidad = (double) nivel.get("densidad");
            
            sumaNumerador += nivelNum * densidad;
            sumaDenominador += densidad;
        }
        
        double centroid = sumaDenominador > 0 ? sumaNumerador / sumaDenominador : 0.0;
        System.out.println("[BIOMARKER] Centroide de ramificacion: " + centroid);
        return centroid;
    }
    
    private static double calcularIRD(List<Map<String, Object>> niveles) {
        if (niveles.size() < 3) {
            System.out.println("[BIOMARKER] IRD: menos de 3 niveles, retornando 0");
            return 0.0;
        }
        
        double densidadNivel1 = (double) niveles.get(0).get("densidad");
        double densidadNivel2 = (double) niveles.get(1).get("densidad");
        
        double sumaDensidadesDistales = 0.0;
        int nivelesDistales = 0;
        
        for (int i = 2; i < niveles.size(); i++) {
            sumaDensidadesDistales += (double) niveles.get(i).get("densidad");
            nivelesDistales++;
        }
        
        double mediaDistal = nivelesDistales > 0 ? sumaDensidadesDistales / nivelesDistales : 1.0;
        double ird = mediaDistal > 0 ? (densidadNivel1 + densidadNivel2) / mediaDistal : 0.0;
        
        System.out.println("[BIOMARKER] IRD: " + ird);
        return ird;
    }
}