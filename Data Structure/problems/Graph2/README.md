El grafo se modela mediante una matriz de adyacencia implementada como un arreglo bidimensional `int[][] adjacencyMatrix`, donde:

`adjacencyMatrix[i][j] = 1` indica la existencia de una arista entre el vértice i y el vértice j.

`adjacencyMatrix[i][j] = 0` indica ausencia de conexión.

Para un grafo no dirigido, la matriz es simétrica: `adjacencyMatrix[i][j] == adjacencyMatrix[j][i]` para todo `i, j`

Esta implematacion tiene una complejidad de O(V²) en espacio

Algoritmo BFS sobre Matriz de Adyacencia

```
Entrada: Vértice inicial s
Salida: Lista de vértices en orden de visita

1. Marcar s como visitado y encolarlo
2. Mientras la cola no esté vacía:
   a. Desencolar vértice actual v
   b. Agregar v a la lista de orden de visita
   c. Para cada columna j desde 0 hasta V-1:
      Si adjacencyMatrix[v][j] == 1 y j no ha sido visitado:
         - Marcar j como visitado
         - Encolar j
```

Diferencia clave con la versión de listas:
En lugar de iterar sobre una lista de vecinos, se itera sobre todas las columnas de la fila v, verificando en cada una si existe conexión (matrix[v][j] == 1). Esto resulta en un costo O(V) por cada vértice procesado.



Complejidad total del BFS con matriz: O(V²), ya que para cada uno de los V vértices se recorren V columnas.

Algoritmo DFS con Pila sobre Matriz de Adyacencia

```
Entrada: Vértice inicial s
Salida: Lista de vértices en orden de visita

1. Apilar s
2. Mientras la pila no esté vacía:
   a. Desapilar vértice actual v
   b. Si v no ha sido visitado:
      - Marcar v como visitado
      - Agregar v a la lista de orden de visita
      - Para cada columna j desde V-1 hasta 0:
         Si adjacencyMatrix[v][j] == 1 y j no ha sido visitado:
            Apilar j
```

### Algoritmo DFS Recursivo sobre Matriz de Adyacencia

```Entrada: Vértice actual v
Efecto: Marca v como visitado y lo agrega al orden de visita

1. Marcar v como visitado
2. Agregar v a la lista de orden de visita
3. Para cada columna j desde 0 hasta V-1:
   Si adjacencyMatrix[v][j] == 1 y j no ha sido visitado:
      Llamar recursivamente a DFS(j)
```

A diferencia del ejercicio anterior donde se usó HashSet<Integer>, aquí se utiliza un arreglo boolean[] visited porque:

Los vértices son consecutivos desde 0 hasta V-1.

El acceso por índice es O(1) y más rápido que HashSet.


