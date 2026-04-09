El presente ejercicio tiene como propósito fundamental la implementación de dos algoritmos clásicos de recorrido sobre grafos: BFS (Breadth-First Search) y DFS (Depth-First Search) utilizando la estructura de listas de adyacencia como representación del grafo.

Representación del Grafo
El grafo se modela mediante una lista de adyacencia implementada como un `Map<Integer, List<Integer>>`

En esta estructura:

Cada clave del mapa representa un vértice del grafo.

Cada valor es una lista (List<Integer>) que contiene los vértices vecinos del vértice clave.

Preferí utilizar `Map` como estructura base, debido a su forma de organizar los datos en pares clave-valor. Donde se representan las `claves` como los nodos y la `lista de nodos vecinos` a él como la lista en la clave.

De esta forma, aprovecho la forma de estructurar los datos para crear una lista de adyacencia, sin necesidad de usar una Lista de Listas.
Mejorando la complejidad temporal de la busqueda de la lista de sus nodos vecinos, a tan solo `O(1)`. (Tabla de Hash) La búsqueda más rápida algoritmicamente hablando.

`Clave`   `Valor`

`Nodo1`   `[Nodo2, Nodo3]`

`Nodo2` `[Nodo1, Nodo4]`

```
.
.
.
```
`Nodo_n` `[nodo_m, ...]`

Sobre esta misma estructura, podemos lanzar los dos tipos de recorridos clásicos, un recorrido en profundidad, usando una pila o recursividad.

### DFS

```
Entrada: Vértice inicial s
Salida: Lista de vértices en orden de visita

1. Apilar s
2. Mientras la pila no esté vacía:
   a. Desapilar vértice actual v
   b. Si v no ha sido visitado:
      - Marcar v como visitado
      - Agregar v a la lista de orden de visita
      - Para cada vecino u de v (en orden ascendente inverso):
         Si u no ha sido visitado:
            Apilar u
```

Para mantener el orden natural de exploración ascendente al usar una pila (LIFO), el último elemento apilado será el primero en ser explorado. Al recorrer los vecinos en orden ascendente pero apilándolos en orden inverso, se garantiza que el vecino de menor etiqueta sea el primero en ser desapilado y explorado.

Stack<Integer>: Implementa el principio LIFO, permitiendo explorar un camino hasta el final antes de retroceder.

Complejidad: O(V + E) en tiempo, O(V) en espacio adicional.

```
Entrada: Vértice actual v
Efecto: Marca v como visitado y lo agrega al orden de visita

1. Marcar v como visitado
2. Agregar v a la lista de orden de visita
3. Para cada vecino u de v (en orden ascendente):
   Si u no ha sido visitado:
      Llamar recursivamente a DFS(u)

```

Ventajas de la versión recursiva: Código más conciso y legible, aprovecha la pila de llamadas del sistema como estructura implícita.

Limitación: Puede producir desbordamiento de pila (StackOverflowError) en grafos muy profundos (> 10,000 niveles de recursión en Java por defecto).

#### BFS


```
Entrada: Vértice inicial s
Salida: Lista de vértices en orden de visita

1. Marcar s como visitado y encolarlo
2. Mientras la cola no esté vacía:
   a. Desencolar vértice actual v
   b. Agregar v a la lista de orden de visita
   c. Para cada vecino u de v (en orden ascendente):
      Si u no ha sido visitado:
         - Marcar u como visitado
         - Encolar u
```

`Queue<Integer>:`

Implementa el principio FIFO, garantizando que los nodos se exploran por niveles.

`Set<Integer>`: 

Proporciona verificación de pertenencia en tiempo O(1) para evitar reprocesamiento. Es el clásico conjunto, que evita valores repetidos y esta basado en la implementación de un árbol, al igual que el Map, es solo para optimización.

Complejidad: O(V + E) en tiempo, O(V) en espacio adicional.



