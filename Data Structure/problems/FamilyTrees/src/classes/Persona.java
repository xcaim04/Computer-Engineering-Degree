package classes;

import java.util.List;
import java.util.ArrayList;

public class Persona {
    String nombre;
    String fechaNac;
    String genero;
    List<Persona> hijos;
    
    public Persona(String nombre, String fechaNac, String genero) {
        this.nombre = nombre;
        this.fechaNac = fechaNac;
        this.genero = genero;
        this.hijos = new ArrayList<>();
    }
}