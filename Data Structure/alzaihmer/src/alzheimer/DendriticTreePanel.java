package alzheimer;

import javax.swing.*;

import data_structures.Node;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class DendriticTreePanel extends JPanel {
    private Node root;
    private Map<Node, Point> nodePositions;
    private int nodeRadius = 10;
    private int levelHeight = 80;
    private int horizontalSpacing = 50;

    public DendriticTreePanel() {
        setBackground(Color.WHITE);
        nodePositions = new HashMap<>();
        System.out.println("[PANEL] Panel de visualizacion inicializado");
    }

    public void setRoot(Node root) {
        this.root = root;
        nodePositions.clear();
        calcularPosiciones();
        repaint();
        System.out.println("[PANEL] Arbol actualizado con nueva raiz");
    }

    private void calcularPosiciones() {
        if (root == null)
            return;

        Map<Node, Integer> subtreeWidths = new HashMap<>();
        calcularAnchoSubarbol(root, subtreeWidths);

        Map<Node, Integer> posicionesX = new HashMap<>();
        calcularPosicionesX(root, 0, subtreeWidths, posicionesX);

        asignarPosicionesY(root, 0, posicionesX);

        System.out.println("[PANEL] Posiciones calculadas para " + nodePositions.size() + " nodos");
    }

    private int calcularAnchoSubarbol(Node nodo, Map<Node, Integer> widths) {
        if (nodo == null)
            return 0;

        if (nodo.esHoja()) {
            widths.put(nodo, 1);
            return 1;
        }

        int anchoIzq = calcularAnchoSubarbol(nodo.hijoIzquierdo, widths);
        int anchoDer = calcularAnchoSubarbol(nodo.hijoDerecho, widths);

        int anchoTotal = anchoIzq + anchoDer;
        widths.put(nodo, anchoTotal);
        return anchoTotal;
    }

    private void calcularPosicionesX(Node nodo, int xInicio, Map<Node, Integer> widths, Map<Node, Integer> posX) {
        if (nodo == null)
            return;

        int anchoNodo = widths.getOrDefault(nodo, 1);
        int xCentro = xInicio + (anchoNodo * horizontalSpacing) / 2;
        posX.put(nodo, xCentro);

        if (nodo.hijoIzquierdo != null) {
            int anchoIzq = widths.getOrDefault(nodo.hijoIzquierdo, 0);
            calcularPosicionesX(nodo.hijoIzquierdo, xInicio, widths, posX);
        }

        if (nodo.hijoDerecho != null) {
            int anchoIzq = widths.getOrDefault(nodo.hijoIzquierdo, 0);
            int xInicioDer = xInicio + (anchoIzq * horizontalSpacing);
            calcularPosicionesX(nodo.hijoDerecho, xInicioDer, widths, posX);
        }
    }

    private void asignarPosicionesY(Node nodo, int nivel, Map<Node, Integer> posX) {
        if (nodo == null)
            return;

        int y = 50 + nivel * levelHeight;
        int x = posX.getOrDefault(nodo, 200);
        nodePositions.put(nodo, new Point(x, y));

        asignarPosicionesY(nodo.hijoIzquierdo, nivel + 1, posX);
        asignarPosicionesY(nodo.hijoDerecho, nivel + 1, posX);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (root == null || nodePositions.isEmpty()) {
            g2d.drawString("No hay neurona cargada", 50, 50);
            return;
        }

        dibujarAristas(g2d);
        dibujarNodos(g2d);

        System.out.println("[PANEL] Arbol renderizado con " + nodePositions.size() + " nodos");
    }

    private void dibujarAristas(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(100, 100, 100));

        for (Map.Entry<Node, Point> entry : nodePositions.entrySet()) {
            Node nodo = entry.getKey();
            Point pos = entry.getValue();

            if (nodo.padre != null && nodePositions.containsKey(nodo.padre)) {
                Point posPadre = nodePositions.get(nodo.padre);
                g2d.drawLine(posPadre.x, posPadre.y, pos.x, pos.y);
            }
        }
    }

    private void dibujarNodos(Graphics2D g2d) {
        for (Map.Entry<Node, Point> entry : nodePositions.entrySet()) {
            Node nodo = entry.getKey();
            Point pos = entry.getValue();

            Color colorNodo = getColorPorTipo(nodo);
            g2d.setColor(colorNodo);
            g2d.fillOval(pos.x - nodeRadius, pos.y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawOval(pos.x - nodeRadius, pos.y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            String label = String.valueOf(nodo.id);
            FontMetrics fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            g2d.drawString(label, pos.x - labelWidth / 2, pos.y - nodeRadius - 2);

            if (nodo.strahler > 0) {
                String strahlerStr = "S:" + nodo.strahler;
                int strahlerWidth = fm.stringWidth(strahlerStr);
                g2d.drawString(strahlerStr, pos.x - strahlerWidth / 2, pos.y + nodeRadius + 12);
            }
        }
    }

    private Color getColorPorTipo(Node nodo) {
        switch (nodo.tipo) {
            case RAIZ:
                return new Color(255, 100, 100);
            case BIFURCACION:
                return new Color(100, 200, 100);
            case CONTINUACION:
                return new Color(100, 150, 255);
            case TERMINAL:
                return new Color(255, 200, 100);
            default:
                return Color.GRAY;
        }
    }
}