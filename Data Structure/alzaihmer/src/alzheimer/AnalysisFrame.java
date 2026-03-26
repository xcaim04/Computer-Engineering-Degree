package alzheimer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import data_structures.*;

public class AnalysisFrame extends JFrame {
    private DendriticTreePanel treePanel;
    private JTextArea logArea;
    private JTextArea resultsArea;
    private JButton loadButton;
    private JButton analyzeButton;
    private JButton classifyButton;
    private Node root;
    private Map<String, Double> biomarkers;
    
    private Color backgroundColor = new Color(30, 30, 30);
    private Color panelColor = new Color(20, 20, 20);
    private Color textColor = new Color(220, 220, 220);
    private Color accentColor = new Color(0, 120, 215);
    private Color borderColor = new Color(60, 60, 60);
    
    private JLabel statusLabel;

    public AnalysisFrame() {
        setTitle("Analisis Morfologico de Arborizaciones Dendriticas - Alzheimer");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);

        initializeComponents();
        setupLayout();
        setupListeners();

        System.out.println("[UI] Interfaz grafica inicializada");
    }

    private void initializeComponents() {
        treePanel = new DendriticTreePanel();
        treePanel.setBackground(new Color(25, 25, 25));
        treePanel.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        
        logArea = createStyledTextArea();
        resultsArea = createStyledTextArea();
        
        loadButton = createStyledButton("Cargar Neurona", new Color(0, 100, 180));
        analyzeButton = createStyledButton("Analizar Biomarcadores", new Color(0, 100, 180));
        classifyButton = createStyledButton("Clasificar Neurona", new Color(180, 100, 0));
        
        analyzeButton.setEnabled(false);
        classifyButton.setEnabled(false);
        
        statusLabel = new JLabel("Sistema listo. Cargue un archivo de neurona para comenzar.");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        statusLabel.setForeground(new Color(150, 150, 150));

        System.out.println("[UI] Componentes inicializados");
    }
    
    private JTextArea createStyledTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(panelColor);
        textArea.setForeground(textColor);
        textArea.setCaretColor(accentColor);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return textArea;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 35));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void setupLayout() {
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(backgroundColor);
        controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        controlPanel.add(loadButton);
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(analyzeButton);
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(classifyButton);
        add(controlPanel, BorderLayout.NORTH);
        
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setBackground(backgroundColor);
        mainSplitPane.setBorder(null);
        
        JPanel centerPanel = createStyledPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            "Visualizacion del Arbol Dendritico",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            textColor
        ));
        centerPanel.add(treePanel, BorderLayout.CENTER);
        
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        rightSplitPane.setBackground(backgroundColor);
        rightSplitPane.setBorder(null);
        rightSplitPane.setResizeWeight(0.5);
        rightSplitPane.setDividerSize(5);
        
        JPanel logPanel = createStyledPanel();
        logPanel.setLayout(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            "Log del Sistema",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            textColor
        ));
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(null);
        logScrollPane.getViewport().setBackground(panelColor);
        logPanel.add(logScrollPane, BorderLayout.CENTER);
        
        JPanel resultsPanel = createStyledPanel();
        resultsPanel.setLayout(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            "Resultados del Analisis",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            textColor
        ));
        JScrollPane resultsScrollPane = new JScrollPane(resultsArea);
        resultsScrollPane.setBorder(null);
        resultsScrollPane.getViewport().setBackground(panelColor);
        resultsPanel.add(resultsScrollPane, BorderLayout.CENTER);
        
        rightSplitPane.setTopComponent(logPanel);
        rightSplitPane.setBottomComponent(resultsPanel);
        
        mainSplitPane.setLeftComponent(centerPanel);
        mainSplitPane.setRightComponent(rightSplitPane);
        mainSplitPane.setDividerLocation(800);
        mainSplitPane.setDividerSize(3);
        
        add(mainSplitPane, BorderLayout.CENTER);
        
        JPanel statusBar = new JPanel();
        statusBar.setBackground(new Color(15, 15, 15));
        statusBar.setBorder(new MatteBorder(1, 0, 0, 0, borderColor));
        statusBar.setPreferredSize(new Dimension(getWidth(), 25));
        statusBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusBar.add(statusLabel);
        
        add(statusBar, BorderLayout.SOUTH);

        System.out.println("[UI] Layout configurado");
    }
    
    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(panelColor);
        return panel;
    }

    private void setupListeners() {
        loadButton.addActionListener(this::onLoadNeuron);
        analyzeButton.addActionListener(this::onAnalyze);
        classifyButton.addActionListener(this::onClassify);
        System.out.println("[UI] Listeners configurados");
    }

    private void onLoadNeuron(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser("data");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de neurona", "txt"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            appendLog("[CARGA] Intentando cargar archivo: " + file.getAbsolutePath());

            try {
                root = NeuronLoader.cargarNeurona(file.getAbsolutePath());
                if (NeuronValidator.validarEstructuraArbol(root)) {
                    treePanel.setRoot(root);
                    treePanel.repaint();
                    analyzeButton.setEnabled(true);
                    classifyButton.setEnabled(false);
                    resultsArea.setText("");
                    appendLog("[CARGA] Neurona cargada exitosamente");
                    appendLog("[CARGA] Estructura validada correctamente");
                    updateStatus("Neurona cargada: " + file.getName());
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Estructura de arbol invalida",
                            "Error de validacion",
                            JOptionPane.ERROR_MESSAGE);
                    appendLog("[ERROR] Estructura de arbol invalida");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al cargar el archivo: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                appendLog("[ERROR] " + ex.getMessage());
                System.err.println("[ERROR] Excepcion al cargar: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void onAnalyze(ActionEvent e) {
        if (root == null) {
            JOptionPane.showMessageDialog(this, "Primero cargue una neurona", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        appendLog("[ANALISIS] Iniciando calculo de biomarcadores");
        updateStatus("Calculando biomarcadores...");

        biomarkers = BiomarkerExtractor.extraerBiomarcadores(root);

        StringBuilder results = new StringBuilder();
        results.append("╔══════════════════════════════════════════════════════════════╗\n");
        results.append("║              BIOMARCADORES MORFOLOGICOS                      ║\n");
        results.append("╚══════════════════════════════════════════════════════════════╝\n\n");
        
        results.append(String.format("┌─────────────────────────────────────────────────────────┐\n"));
        results.append(String.format("│ Orden de Strahler maximo (Smax):              %8.2f │\n", biomarkers.get("strahler_max")));
        results.append(String.format("│ Orden de Strahler medio (Smedio):             %8.2f │\n", biomarkers.get("strahler_mean")));
        results.append(String.format("│ Indice de Complejidad Arborea Total (ICAT):   %8.2f │\n", biomarkers.get("icat")));
        results.append(String.format("│ Numero de terminales:                         %8.0f │\n", biomarkers.get("terminal_count")));
        results.append(String.format("│ Relacion bifurcacion/terminal:                %8.3f │\n", biomarkers.get("bifurcation_ratio")));
        results.append(String.format("│ DEE media (μm):                               %8.2f │\n", biomarkers.get("dee_mean")));
        results.append(String.format("│ DEE maxima (μm):                              %8.2f │\n", biomarkers.get("dee_max")));
        results.append(String.format("│ Centroide de ramificacion:                    %8.2f │\n", biomarkers.get("centroid")));
        results.append(String.format("│ Indice de Retraccion Distal (IRD):            %8.3f │\n", biomarkers.get("ird")));
        results.append(String.format("└─────────────────────────────────────────────────────────┘\n"));

        resultsArea.setText(results.toString());
        classifyButton.setEnabled(true);

        appendLog("[ANALISIS] Biomarcadores calculados correctamente");
        updateStatus("Analisis completado");
    }

    private void onClassify(ActionEvent e) {
        if (biomarkers == null) {
            JOptionPane.showMessageDialog(this, "Primero realice el analisis", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        appendLog("[CLASIFICACION] Iniciando clasificacion neuronal");
        updateStatus("Clasificando neurona...");

        Map<String, Double> thresholds = new HashMap<>();
        thresholds.put("strahler_max", 2.5);
        thresholds.put("dee_mean", 0.07);
        thresholds.put("ird", 3.2);
        thresholds.put("icat", 100.0);
        thresholds.put("bifurcation_ratio", 0.75);

        ClassificationResult result = NeuronClassifier.clasificarNeurona(biomarkers, thresholds);

        StringBuilder message = new StringBuilder();
        message.append("\n\n╔══════════════════════════════════════════════════════════════╗\n");
        message.append("║                    RESULTADO DE CLASIFICACION                 ║\n");
        message.append("╚══════════════════════════════════════════════════════════════╝\n\n");

        if (result.category.equals("NORMAL")) {
            message.append("┌─────────────────────────────────────────────────────────┐\n");
            message.append("│                 NEURONA CLASIFICADA COMO                 │\n");
            message.append("│                      NORMAL                              │\n");
            message.append("└─────────────────────────────────────────────────────────┘\n\n");
        } else {
            message.append("┌─────────────────────────────────────────────────────────┐\n");
            message.append("│              NEURONA CLASIFICADA COMO                    │\n");
            message.append("│                   DEGENERADA                            │\n");
            message.append("└─────────────────────────────────────────────────────────┘\n\n");
        }

        message.append("JUSTIFICACION:\n");
        message.append(result.justification);

        resultsArea.append(message.toString());

        JOptionPane.showMessageDialog(this,
                "Neurona clasificada como: " + result.category +
                        "\n\n" + result.justification,
                "Resultado de Clasificacion",
                result.category.equals("NORMAL") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);

        appendLog("[CLASIFICACION] Neurona clasificada como: " + result.category);
        updateStatus("Clasificacion completada: " + result.category);
    }
    
    private void appendLog(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }
}