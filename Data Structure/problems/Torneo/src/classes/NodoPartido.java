package classes;

public class NodoPartido {
    private String equipo2;
    private String equipo1;
    private String ronda;
    private String ganador;
    private NodoPartido izq;
    private NodoPartido der;
    
    public String getEquipo1() {
        return equipo1;
    }

    public void setEquipo1(String equipo1) {
        this.equipo1 = equipo1;
    }

    public String getEquipo2() {
        return equipo2;
    }

    public void setEquipo2(String equipo2) {
        this.equipo2 = equipo2;
    }

    public String getRonda() {
        return ronda;
    }

    public void setRonda(String ronda) {
        this.ronda = ronda;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    public NodoPartido getIzq() {
        return izq;
    }

    public void setIzq(NodoPartido izq) {
        this.izq = izq;
    }

    public NodoPartido getDer() {
        return der;
    }

    public void setDer(NodoPartido der) {
        this.der = der;
    }

    public NodoPartido(String equipo1, String equipo2, String ronda) {
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.ronda = ronda;
        this.ganador = null;
        this.izq = null;
        this.der = null;
    }
}