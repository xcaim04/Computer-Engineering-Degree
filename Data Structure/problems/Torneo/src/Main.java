

import java.util.*;
import classes.Torneo;

class Main {
    public static void main(String[] args) {
        
        
        List<String> equipos = Arrays.asList(
            "Real Madrid", "Barcelona", "Bayern", "Liverpool",
            "PSG", "Manchester City", "Juventus", "Ajax"
        );
        
        Torneo torneo = new Torneo(equipos);
        torneo.mostrarBracket();
        torneo.simularTorneo();
    }
}