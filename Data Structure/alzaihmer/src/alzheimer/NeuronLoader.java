// NeuronLoader.java
package alzheimer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data_structures.*;

public class NeuronLoader {
    
    public static Node cargarNeurona(String archivo) throws IOException {
        System.out.println("[LOADER] Leyendo archivo: " + archivo);
        
        Map<Integer, Node> nodosMap = new HashMap<>();
        Map<Integer, Integer> padreMap = new HashMap<>();
        Map<Integer, Double> longitudMap = new HashMap<>();
        Map<Integer, Double> diametroMedioMap = new HashMap<>();
        Map<Integer, Double> tortuosidadMap = new HashMap<>();
        
        BufferedReader reader = new BufferedReader(new FileReader(archivo));
        String line;
        boolean leyendoNodos = false;
        boolean leyendoAristas = false;
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            
            if (line.equals("NODOS:")) {
                leyendoNodos = true;
                leyendoAristas = false;
                continue;
            } else if (line.equals("ARISTAS:")) {
                leyendoNodos = false;
                leyendoAristas = true;
                continue;
            }
            
            if (leyendoNodos) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 6) {
                    int id = Integer.parseInt(parts[0]);
                    Node.NodeType tipo = Node.NodeType.valueOf(parts[1]);
                    double x = Double.parseDouble(parts[2]);
                    double y = Double.parseDouble(parts[3]);
                    double z = Double.parseDouble(parts[4]);
                    double diametro = Double.parseDouble(parts[5]);
                    
                    Node node = new Node(id, tipo, x, y, z, diametro);
                    nodosMap.put(id, node);
                    System.out.println("[LOADER] Nodo cargado: " + node);
                }
            } else if (leyendoAristas) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 5) {
                    int nodoId = Integer.parseInt(parts[0]);
                    int padreId = Integer.parseInt(parts[1]);
                    double longitud = Double.parseDouble(parts[2]);
                    double diametroMedio = Double.parseDouble(parts[3]);
                    double tortuosidad = Double.parseDouble(parts[4]);
                    
                    padreMap.put(nodoId, padreId);
                    longitudMap.put(nodoId, longitud);
                    diametroMedioMap.put(nodoId, diametroMedio);
                    tortuosidadMap.put(nodoId, tortuosidad);
                    
                    System.out.println("[LOADER] Arista cargada: " + nodoId + " -> " + padreId);
                }
            }
        }
        reader.close();
        
        Map<Integer, List<Integer>> hijosPorPadre = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : padreMap.entrySet()) {
            int hijoId = entry.getKey();
            int padreId = entry.getValue();
            
            hijosPorPadre.computeIfAbsent(padreId, k -> new ArrayList<>()).add(hijoId);
        }
        
        for (Map.Entry<Integer, List<Integer>> entry : hijosPorPadre.entrySet()) {
            if (entry.getValue().size() > 2) {
                throw new IOException("Nodo " + entry.getKey() + " tiene " + 
                    entry.getValue().size() + " hijos, maximo permitido es 2");
            }
        }
        
        Node raiz = null;
        
        for (Map.Entry<Integer, Node> entry : nodosMap.entrySet()) {
            int id = entry.getKey();
            Node node = entry.getValue();
            
            if (padreMap.containsKey(id)) {
                int padreId = padreMap.get(id);
                Node padre = nodosMap.get(padreId);
                node.padre = padre;
                node.longitudArista = longitudMap.get(id);
                node.diametroMedioArista = diametroMedioMap.get(id);
                node.tortuosidad = tortuosidadMap.get(id);
                
                if (padre.hijoIzquierdo == null) {
                    padre.hijoIzquierdo = node;
                    System.out.println("[LOADER] Asignado como hijo izquierdo de " + padreId + ": " + id);
                } else if (padre.hijoDerecho == null) {
                    padre.hijoDerecho = node;
                    System.out.println("[LOADER] Asignado como hijo derecho de " + padreId + ": " + id);
                } else {
                    System.out.println("[LOADER] ADVERTENCIA: Nodo " + padreId + " ya tiene dos hijos, ignorando asignacion de " + id);
                }
            } else {
                if (raiz == null) {
                    raiz = node;
                    System.out.println("[LOADER] Raiz identificada: " + node);
                } else {
                    throw new IOException("Multiples raices encontradas");
                }
            }
        }
        
        if (raiz == null) {
            throw new IOException("No se encontro raiz en el archivo");
        }
        
        System.out.println("[LOADER] Carga completada. Raiz: " + raiz);
        return raiz;
    }
}