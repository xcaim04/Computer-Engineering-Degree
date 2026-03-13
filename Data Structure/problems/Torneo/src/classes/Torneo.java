package classes;

import classes.NodoPartido;
import java.util.*;

public class Torneo {
    private NodoPartido raiz;
    private Random rand = new Random();
    private List<String> equipos;
    
    public Torneo(List<String> equipos) {
        if (equipos.size() != 8) {
            throw new IllegalArgumentException("Se necesitan exactamente 8 equipos");
        }
        this.equipos = new ArrayList<>(equipos);
        this.raiz = construirArbol();
    }
    
    private NodoPartido construirArbol() {
        List<NodoPartido> cuartos = new ArrayList<>();
        for (int i = 0; i < 8; i += 2) {
            NodoPartido qf = new NodoPartido(
                equipos.get(i), 
                equipos.get(i + 1), 
                "Cuartos de Final"
            );
            cuartos.add(qf);
        }
        
        NodoPartido semi1 = new NodoPartido("", "", "Semifinal 1");
        semi1.setIzq(cuartos.get(0));
        semi1.setDer(cuartos.get(1));
        
        NodoPartido semi2 = new NodoPartido("", "", "Semifinal 2");
        semi2.setIzq(cuartos.get(2));
        semi2.setDer(cuartos.get(3));
        
        NodoPartido finalPartido = new NodoPartido("", "", "Final");
        finalPartido.setIzq(semi1);
        finalPartido.setDer(semi2);
        
        return finalPartido;
    }
    
    public void simularTorneo() {
        System.out.println("=== SIMULACIÓN DEL TORNEO ===\n");
        simularPartido(raiz);
        System.out.println("\nCAMPEÓN: " + raiz.getGanador());
    }
    
    private String simularPartido(NodoPartido nodo) {
        
        if (nodo == null) 
            return null;
        
        if (nodo.getIzq() == null && nodo.getDer() == null) {
            nodo.setGanador(rand.nextBoolean() ? nodo.getEquipo1() : nodo.getEquipo2());
            mostrarResultado(nodo);
            return nodo.getGanador();
        }
        
        String ganador1 = simularPartido(nodo.getIzq());
        String ganador2 = simularPartido(nodo.getDer());
        
        nodo.setEquipo1(ganador1);
        nodo.setEquipo2(ganador2);
        
        nodo.setGanador(rand.nextBoolean() ? ganador1 : ganador2);
        mostrarResultado(nodo);
        
        return nodo.getGanador();
    }
    
    private void mostrarResultado(NodoPartido nodo) {
        System.out.printf("%s: %s vs %s → Ganador: %s%n",
            nodo.getRonda(), nodo.getEquipo1(), nodo.getEquipo2(), nodo.getGanador());
    }
    
    public void mostrarBracket() {
        System.out.println("\n=== BRACKET DEL TORNEO ===");
        mostrarBracketRec(raiz, 0);
    }
    
    private void mostrarBracketRec(NodoPartido nodo, int nivel) {
        if (nodo == null) return;
        
        String indent = "  ".repeat(nivel);

        if (nodo.getIzq() != null) mostrarBracketRec(nodo.getIzq(), nivel + 1);
        if (nodo.getDer() != null) mostrarBracketRec(nodo.getDer(), nivel + 1);
        
        if (nodo.getIzq() == null && nodo.getDer() == null) {
            System.out.println(indent + " " + nodo.getRonda() + ": " + 
                             nodo.getEquipo1() + " vs " + nodo.getEquipo2() + 
                             " → Ganador: " + nodo.getGanador());
        } else {
            System.out.println(indent + " " + nodo.getRonda() + ": " + 
                             nodo.getEquipo1() + " vs " + nodo.getEquipo2() + 
                             " → Ganador: " + nodo.getGanador());
        }
    }
}