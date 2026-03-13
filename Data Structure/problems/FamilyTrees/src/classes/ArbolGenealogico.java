package classes;

import java.util.*;

public class ArbolGenealogico {
    private Persona raiz;
    
    public ArbolGenealogico(Persona raiz) {
        this.raiz = raiz;
    }
    
    public void agregarHijo(String nombrePadre, Persona hijo) {
        Persona padre = buscarPersona(raiz, nombrePadre);
        if (padre != null) {
            padre.hijos.add(hijo);
            System.out.println(hijo.nombre + " agregado como hijo de " + nombrePadre);
        } else {
            System.out.println("No se encontró a " + nombrePadre);
        }
    }
    
    private Persona buscarPersona(Persona actual, String nombre) {
        if (actual == null) return null;
        if (actual.nombre.equals(nombre)) return actual;
        
        for (Persona hijo : actual.hijos) {
            Persona encontrado = buscarPersona(hijo, nombre);
            if (encontrado != null) return encontrado;
        }
        return null;
    }
    
    public void buscarAntepasados(String nombre) {
        System.out.println("\n=== BUSCANDO ANTEPASADOS DE " + nombre + " ===");
        List<String> antepasados = new ArrayList<>();
        if (!buscarAntepasadosRec(raiz, nombre, antepasados)) {
            System.out.println("Persona no encontrada");
        } else if (antepasados.isEmpty()) {
            System.out.println(nombre + " es la raíz del árbol (no tiene antepasados)");
        } else {
            System.out.println("Línea de antepasados:");
            for (int i = 0; i < antepasados.size(); i++) {
                String relacion = i == 0 ? "Padre/Madre" : 
                                 i == 1 ? "Abuelo/a" : 
                                 i == 2 ? "Bisabuelo/a" : 
                                 "Tatarabuelo/a";
                System.out.println(relacion + ": " + antepasados.get(i));
            }
        }
    }
    
    private boolean buscarAntepasadosRec(Persona actual, String nombreBuscado, List<String> antepasados) {
        if (actual == null) return false;
        
        for (Persona hijo : actual.hijos) {
            if (hijo.nombre.equals(nombreBuscado)) {
                antepasados.add(actual.nombre);
                return true;
            }
            if (buscarAntepasadosRec(hijo, nombreBuscado, antepasados)) {
                antepasados.add(actual.nombre);
                return true;
            }
        }
        return false;
    }
    
    public void listarDescendientes(String nombre) {
        System.out.println("\n=== DESCENDIENTES DE " + nombre + " ===");
        Persona persona = buscarPersona(raiz, nombre);
        if (persona == null) {
            System.out.println("Persona no encontrada");
            return;
        }
        
        Map<Integer, List<String>> porGeneracion = new HashMap<>();
        recolectarDescendientes(persona, 1, porGeneracion);
        
        if (porGeneracion.isEmpty()) {
            System.out.println(nombre + " no tiene descendientes");
        } else {
            for (int gen : porGeneracion.keySet()) {
                String tipo = gen == 1 ? "Hijos" : 
                             gen == 2 ? "Nietos" : 
                             gen == 3 ? "Bisnietos" : 
                             "Tataranietos";
                System.out.println(tipo + " (Generación " + gen + "): " + 
                                 String.join(", ", porGeneracion.get(gen)));
            }
        }
    }
    
    private void recolectarDescendientes(Persona persona, int nivel, Map<Integer, List<String>> mapa) {
        for (Persona hijo : persona.hijos) {
            mapa.computeIfAbsent(nivel, k -> new ArrayList<>()).add(hijo.nombre);
            recolectarDescendientes(hijo, nivel + 1, mapa);
        }
    }
    
    public void eliminarRama(String nombre) {
        System.out.println("\n=== ELIMINANDO RAMA DE " + nombre + " ===");
        if (raiz.nombre.equals(nombre)) {
            System.out.println("No se puede eliminar la raíz del árbol");
            return;
        }
        
        if (eliminarRamaRec(raiz, nombre)) {
            System.out.println("Rama eliminada exitosamente");
        } else {
            System.out.println("Persona no encontrada");
        }
    }
    
    private boolean eliminarRamaRec(Persona actual, String nombre) {
        Iterator<Persona> iter = actual.hijos.iterator();
        while (iter.hasNext()) {
            Persona hijo = iter.next();
            if (hijo.nombre.equals(nombre)) {
                iter.remove();
                return true;
            }
            if (eliminarRamaRec(hijo, nombre)) {
                return true;
            }
        }
        return false;
    }
    
    public void profundidadMaxima() {
        System.out.println("\n=== PROFUNDIDAD DEL ÁRBOL ===");
        int prof = calcularProfundidad(raiz);
        System.out.println("La generación más profunda es: " + prof);
    }
    
    private int calcularProfundidad(Persona persona) {
        if (persona == null || persona.hijos.isEmpty()) return 1;
        
        int maxProf = 0;
        for (Persona hijo : persona.hijos) {
            maxProf = Math.max(maxProf, calcularProfundidad(hijo));
        }
        return maxProf + 1;
    }
    
    public void recorridoPorNiveles() {
        System.out.println("\n=== RECORRIDO POR GENERACIONES ===");
        if (raiz == null) return;
        
        Queue<Persona> cola = new LinkedList<>();
        cola.add(raiz);
        int generacion = 1;
        
        while (!cola.isEmpty()) {
            int size = cola.size();
            List<String> personasGen = new ArrayList<>();
            
            for (int i = 0; i < size; i++) {
                Persona actual = cola.poll();
                personasGen.add(actual.nombre + " (" + actual.fechaNac + ")");
                cola.addAll(actual.hijos);
            }
            
            System.out.println("Generación " + generacion + ": " + 
                             String.join(", ", personasGen));
            generacion++;
        }
    }
}
