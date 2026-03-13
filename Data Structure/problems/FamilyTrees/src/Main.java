import classes.ArbolGenealogico;
import classes.Persona;

public class Main {
    public static void main(String[] args) {
        Persona abuelo = new Persona("Carlos", "1950-05-15", "M");
        ArbolGenealogico arbol = new ArbolGenealogico(abuelo);
        
        Persona padre = new Persona("Juan", "1975-08-20", "M");
        Persona tio = new Persona("Pedro", "1978-03-10", "M");
        arbol.agregarHijo("Carlos", padre);
        arbol.agregarHijo("Carlos", tio);
        
        Persona hijo1 = new Persona("Ana", "2000-12-01", "F");
        Persona hijo2 = new Persona("Luis", "2003-07-18", "M");
        arbol.agregarHijo("Juan", hijo1);
        arbol.agregarHijo("Juan", hijo2);
        
        Persona nieta = new Persona("Sofia", "2022-03-25", "F");
        arbol.agregarHijo("Ana", nieta);
        
        arbol.recorridoPorNiveles();
        arbol.buscarAntepasados("Sofia");
        arbol.listarDescendientes("Carlos");
        arbol.profundidadMaxima();
        arbol.eliminarRama("Pedro");
        arbol.recorridoPorNiveles();
    }
}
