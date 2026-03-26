package alzheimer;

import java.util.Map;

public class NeuronClassifier {
    
    public static ClassificationResult clasificarNeurona(Map<String, Double> biomarkers, Map<String, Double> thresholds) {
        System.out.println("[CLASSIFIER] Iniciando clasificacion con umbrales: " + thresholds);
        
        StringBuilder justification = new StringBuilder();
        int criteriosActivados = 0;
        
        double strahlerMax = biomarkers.getOrDefault("strahler_max", 0.0);
        double strahlerThreshold = thresholds.getOrDefault("strahler_max", 2.5);
        
        if (strahlerMax <= strahlerThreshold) {
            criteriosActivados++;
            justification.append(String.format("- Orden de Strahler maximo reducido: %.1f <= %.1f (pérdida de complejidad jerárquica)\n", 
                strahlerMax, strahlerThreshold));
            System.out.println("[CLASSIFIER] Criterio 1 activado: Strahler bajo");
        }
        
        double deeMean = biomarkers.getOrDefault("dee_mean", 0.0);
        double deeThreshold = thresholds.getOrDefault("dee_mean", 0.07);
        
        if (deeMean > deeThreshold) {
            criteriosActivados++;
            justification.append(String.format("- Distancia electrofisiológica efectiva elevada: %.3f > %.3f (mayor atenuación de señales)\n", 
                deeMean, deeThreshold));
            System.out.println("[CLASSIFIER] Criterio 2 activado: DEE alta");
        }
        
        double ird = biomarkers.getOrDefault("ird", 0.0);
        double irdThreshold = thresholds.getOrDefault("ird", 3.2);
        
        if (ird > irdThreshold) {
            criteriosActivados++;
            justification.append(String.format("- Índice de retracción distal elevado: %.2f > %.2f (degeneración de ramas distales)\n", 
                ird, irdThreshold));
            System.out.println("[CLASSIFIER] Criterio 3 activado: IRD alto");
        }
        
        double icat = biomarkers.getOrDefault("icat", 0.0);
        double icatThreshold = thresholds.getOrDefault("icat", 100.0);
        
        if (icat < icatThreshold) {
            criteriosActivados++;
            justification.append(String.format("- Complejidad arbórea total reducida: %.1f < %.1f (pérdida de estructura arbórea)\n", 
                icat, icatThreshold));
            System.out.println("[CLASSIFIER] Criterio 4 activado: ICAT bajo");
        }
        
        double bifurcationRatio = biomarkers.getOrDefault("bifurcation_ratio", 0.0);
        double ratioThreshold = thresholds.getOrDefault("bifurcation_ratio", 0.75);
        
        if (bifurcationRatio < ratioThreshold) {
            criteriosActivados++;
            justification.append(String.format("- Relación bifurcación/terminal reducida: %.2f < %.2f (menos ramificaciones por terminal)\n", 
                bifurcationRatio, ratioThreshold));
            System.out.println("[CLASSIFIER] Criterio 5 activado: Ratio bifurcacion bajo");
        }
        
        String categoria;
        if (criteriosActivados >= 2) {
            categoria = "DEGENERADA";
            justification.insert(0, String.format("Neurona clasificada como DEGENERADA (%d criterios anormales):\n", criteriosActivados));
            System.out.println("[CLASSIFIER] Neurona clasificada como DEGENERADA con " + criteriosActivados + " criterios");
        } else {
            categoria = "NORMAL";
            justification.insert(0, "Neurona clasificada como NORMAL:\n");
            if (criteriosActivados == 0) {
                justification.append("- Todos los biomarcadores dentro de rangos normales\n");
            } else {
                justification.append(String.format("- Solo %d criterio(s) anormal(es), insuficiente para degeneracion\n", criteriosActivados));
            }
            System.out.println("[CLASSIFIER] Neurona clasificada como NORMAL con " + criteriosActivados + " criterios anormales");
        }
        
        return new ClassificationResult(categoria, justification.toString());
    }
}