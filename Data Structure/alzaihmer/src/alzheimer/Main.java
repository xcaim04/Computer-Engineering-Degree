package alzheimer;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AnalysisFrame frame = new AnalysisFrame();
            frame.setVisible(true);
            System.out.println("[SYSTEM] Aplicacion iniciada correctamente");
        });
    }
}