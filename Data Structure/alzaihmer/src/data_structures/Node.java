package data_structures;

public class Node {
    public enum NodeType {
        RAIZ, BIFURCACION, CONTINUACION, TERMINAL
    }
    
    public int id;
    public NodeType tipo;
    public double x;
    public double y;
    public double z;
    public double diametro;
    
    public Node hijoIzquierdo;
    public Node hijoDerecho;
    public Node padre;
    
    public double longitudArista;
    public double diametroMedioArista;
    public double tortuosidad;
    
    public int strahler;
    public double dee;
    
    public Node(int id, NodeType tipo, double x, double y, double z, double diametro) {
        this.id = id;
        this.tipo = tipo;
        this.x = x;
        this.y = y;
        this.z = z;
        this.diametro = diametro;
        this.hijoIzquierdo = null;
        this.hijoDerecho = null;
        this.padre = null;
        this.strahler = 0;
        this.dee = 0.0;
    }
    
    public boolean esHoja() {
        return hijoIzquierdo == null && hijoDerecho == null;
    }
    
    public int contarHijos() {
        int count = 0;
        if (hijoIzquierdo != null) count++;
        if (hijoDerecho != null) count++;
        return count;
    }
    
    public String toString() {
        return String.format("Node{id=%d, tipo=%s, pos=(%.1f,%.1f,%.1f), diam=%.1f}", 
            id, tipo, x, y, z, diametro);
    }
}