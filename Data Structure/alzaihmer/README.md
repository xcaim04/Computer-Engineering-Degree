# Análisis Morfológico de Neuronas en Alzheimer

## README Técnico

---

## Descripción del Proyecto

Sistema en Java para analizar neuronas como árboles binarios y detectar patrones de degeneración asociados a la enfermedad de Alzheimer mediante el cálculo de biomarcadores cuantitativos.

---

## Estructura del Proyecto

```
src/
├── alzheimer/
│   ├── AnalysisFrame.java           # Interfaz gráfica principal
│   ├── BiomarkerExtractor.java      # Extracción de biomarcadores
│   ├── ClassificationResult.java    # Resultado de clasificación
│   ├── DendriticTreePanel.java      # Panel de visualización del árbol
│   ├── Main.java                    # Punto de entrada
│   ├── NeuronClassifier.java        # Clasificación NORMAL/DEGENERADA
│   ├── NeuronLoader.java            # Carga desde archivo .txt
│   └── NeuronValidator.java         # Validación de estructura arbórea
│
├── data_structures/
│   ├── Node.java                    # Nodo del árbol neuronal
│   ├── Queue.java                   # Implementación de cola para BFS
│   ├── Stack.java                   # Implementación de pila para DFS
│   └── TreeTraversal.java           # Algoritmos de recorrido (postorder, DFS, BFS)
│
└── data/
    ├── neurona_normal.txt
    └── neurona_alzheimer.txt
```

---

## Clases Principales

### 1. Node.java

```java
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
```

### 2. NeuronParser.java

**Métodos:**
- `loadFromFile(String path)` → carga nodos y aristas, construye árbol
- `validateTree(Node root)` → verifica que sea un árbol binario válido

### 3. Postorder.java

**Método:** `calculateStrahler(Node node)`
- Recursivo
- Terminales → orden 1
- Un hijo → hereda orden
- Dos hijos iguales → orden + 1
- Dos hijos distintos → máximo

### 4. DfsPreorder.java

**Método:** `calculateDEE(Node node, double accumulated)`
- Acumula `longitud / lambda` desde la raíz
- Lambda = √((d * Rm) / (4 * Ri))
- Rm = 5000 Ω·cm², Ri = 150 Ω·cm

### 5. BfsLevel.java

**Método:** `analyzeLevels(Node root)`
- Usa Queue
- Retorna lista con nivel, total nodos, bifurcaciones, densidad

### 6. BiomarkerCalculator.java

**Biomarcadores calculados:**

| Biomarcador | Descripción | Recorrido |
|-------------|-------------|-----------|
| strahlerMax | Orden máximo | Postorder |
| strahlerMean | Orden promedio ponderado | Postorder |
| icat | Σ (strahler × longitud) | Postorder |
| terminalCount | Número de hojas | Cualquiera |
| branchingRatio | Bifurcaciones / terminales | Cualquiera |
| deeMean | DEE promedio de terminales | DFS |
| deeMax | DEE máxima de terminales | DFS |
| branchingCentroid | Centro de masa de bifurcaciones | BFS |
| ird | Índice de retracción distal | BFS |

### 7. AlzheimerClassifier.java

**Criterios de clasificación (umbrales configurables):**
- Strahler max < 4 → degeneración
- DEE mean > 50 μm → degeneración  
- IRD > 2.5 → degeneración

Si al menos 2 criterios se cumplen → "DEGENERADA"

---

## Flujo de Ejecución

```
1. Cargar archivo → NeuronParser.loadFromFile()
   ↓
2. Validar estructura → validateTree()
   ↓
3. Calcular Strahler → Postorder.calculateStrahler()
   ↓
4. Calcular DEE → DfsPreorder.calculateDEE()
   ↓
5. Analizar niveles → BfsLevel.analyzeLevels()
   ↓
6. Agregar biomarcadores → BiomarkerCalculator.computeAll()
   ↓
7. Clasificar → AlzheimerClassifier.classify()
   ↓
8. Mostrar resultados
```

---

## Formato de Archivo de Entrada

```
NODOS:
id tipo x y z diametro

ARISTAS:
id_hijo id_padre longitud diametro_medio tortuosidad
```

**Tipos de nodo:**
- `RAIZ` (1 solo)
- `BIFURCACION` (2 hijos)
- `CONTINUACION` (1 hijo)
- `TERMINAL` (0 hijos)

---

## Configuración

**Constantes fisiológicas (en BiomarkerCalculator.java):**
```java
public static final double RM = 5000.0;   // Ω·cm²
public static final double RI = 150.0;    // Ω·cm
```

**Umbrales de clasificación (en AlzheimerClassifier.java):**
```java
public static final double STRAHLER_THRESHOLD = 4.0;
public static final double DEE_THRESHOLD = 50.0;
public static final double IRD_THRESHOLD = 2.5;
```

---

## Dependencias

- Java 11 o superior

---

## Notas de Implementación

### Por qué postorder para Strahler
El orden de un nodo depende del orden de sus hijos. Postorder procesa hijos antes que el padre.

### Por qué DFS preorder para DEE
La DEE acumula valores desde la raíz hacia las hojas. Preorder propaga el acumulado manteniendo el contexto del camino.

### Por qué BFS para niveles
BFS garantiza procesar todos los nodos del nivel k antes que los del nivel k+1, necesario para el perfil de distribución.

---

## Limitaciones

- El árbol debe ser binario estricto (máximo 2 hijos por nodo)
- El archivo debe contener exactamente un nodo RAIZ
- No se soportan ciclos
- Las coordenadas se asumen en micrómetros

---

## Autores

Carlos Javier Blanco Moreira