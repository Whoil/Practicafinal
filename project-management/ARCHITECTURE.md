# Arquitectura del Proyecto

## 1. Principios

- La logica del juego debe estar separada de JavaFX.
- JavaFX solo debe consultar estado y enviar acciones a alto nivel.
- Las estructuras evaluadas deben ser propias.
- No usar colecciones prohibidas de Java.
- Mantener el diseno simple y explicable.
- Las clases deben tener responsabilidades claras.
- El codigo debe estar comentado en profundidad para facilitar revision, memoria y defensa.

## 2. Restricciones de estructuras

Prohibido para estructuras evaluadas:

- `ArrayList`
- `LinkedList`
- `HashMap`
- `HashSet`
- `Stack`
- `Queue`
- `PriorityQueue`
- `TreeMap`
- `java.util.*` para sustituir estructuras propias

Tambien se evita usar arrays Java para representar la matriz de las cuevas.

Permitido con cuidado:

- Gson para JSON.
- Clases de E/S como `FileReader`, `FileWriter`, `IOException`.
- JavaFX para interfaz.
- JUnit para pruebas.

## 3. Decision principal de mapa

La mazmorra completa no hereda de grafo.

Modelo correcto:

```text
Mazmorra contiene Grafo<Cueva>
```

Cada cueva no hereda de grafo.

Modelo correcto:

```text
Cueva contiene ListaSE<ListaSE<Celda>>
```

El movimiento dentro de una cueva se calcula con BFS sobre vecinos implicitos:

```text
arriba, abajo, izquierda, derecha
```

Cada celda actua como nodo conceptual durante el BFS, pero no se almacena necesariamente un `Grafo<Celda>` permanente.

## 4. Paquetes actuales

```text
src/Estructuras
src/modelo/juego
src/modelo/mapa
src/modelo/personajes
src/modelo/objetos
src/json
src/vista
src/control
test
src/excepciones (sin usar)
src/modelo/acciones (sin usar)
```

## 5. Clases principales

### Juego

| Clase | Responsabilidad |
|---|---|
| `Partida` | Coordina turnos, acciones, victoria, derrota y estado global |
| `Mazmorra` | Contiene el grafo de cuevas y la cueva actual |
| `LogJuego` | Registra eventos de la partida |
| `EstadoPartida` | Enum: en curso, victoria, derrota |

### Mapa

| Clase | Responsabilidad |
|---|---|
| `Cueva` | Matriz propia de celdas y operaciones internas |
| `Celda` | Posicion y contenido de una casilla |
| `TipoCelda` | Enum de tipos de celda |
| `Puerta` | Conexion entre cuevas con posibles requisitos |
| `Trampa` | Elemento que dana al jugador |
| `Posicion` | Fila y columna, si se decide crear esta clase |

### Personajes

| Clase | Responsabilidad |
|---|---|
| `Personaje` | Clase abstracta con vida, ataque, defensa, movimiento y posicion |
| `Jugador` | Personaje controlado por usuario, inventario y equipo |
| `Enemigo` | Personaje enemigo normal |
| `Boss` | Enemigo especial final |

### Objetos

| Clase | Responsabilidad |
|---|---|
| `Objeto` | Clase abstracta base |
| `Pocion` | Cura y se consume |
| `Arma` | Objeto equipable que aumenta ataque |
| `Espada` | Arma simple |
| `Arco` | Extra futuro, ataque a distancia |
| `Escudo` | Aumenta defensa |
| `Llave` | Abre una puerta concreta |

## 6. Contrato entre logica e interfaz

La interfaz debe trabajar con metodos de alto nivel. No debe modificar directamente estructuras internas.

Metodos sugeridos en `Partida`:

```text
getCuevaActual()
getEstadoJugador()
getInventario()
getLog()
getAccionesDisponibles()
getCeldasAlcanzables()
moverJugador(fila, columna)
atacar(fila, columna)
recoger(fila, columna)
usarObjeto(idObjeto)
equiparObjeto(idObjeto)
abrirPuerta(fila, columna)
guardarPartida(ruta)
cargarPartida(ruta)
```

## 7. Estructuras propias y usos

Todas las estructuras viven en `src/Estructuras/`. Ninguna usa colecciones prohibidas de `java.util`. Ninguna exige `Comparable` — usan `equals()` para igualdad.

### 7.1 ListaSE — Lista simplemente enlazada

| Atributo | Valor |
|---|---|
| **Archivo** | `src/Estructuras/ListaSE.java` |
| **Tipo** | `ListaSE<T> implements Lista<T>` |
| **Almacenamiento** | `ElementoSE<T> primero` + `int size` (sin puntero a ultimo) |
| **Metodos publicos** | `add(T)` (cabeza, O(1)), `addLast(T)` (O(n)), `get(T)`, `get(int)`, `del(T)`, `delFirst()`, `delLast()`, `existeDato(T)`, `invertir()`, `copy()`, `getIterador()` |
| **Usos** | Matriz de Cueva (`ListaSE<ListaSE<Celda>>`), log de Partida, almacenamiento interno del Grafo (nodos, arcos, resultados BFS), contenidos por cueva, retorno de caminos en Mazmorra, carga JSON |
| **Riesgos** | `addLast()` es O(n) por falta de tail pointer; `get(int)` siempre recorre desde cabeza; `copy()` hace copia superficial de referencias |

### 7.2 ListaDE — Lista doblemente enlazada

| Atributo | Valor |
|---|---|
| **Archivo** | `src/Estructuras/ListaDE.java` |
| **Tipo** | `ListaDE<T> implements Lista<T>` |
| **Almacenamiento** | `ElementoDE<T> primero, ultimo` + `int size` |
| **Metodos publicos** | `add(T)` (cabeza, O(1)), `addLast(T)` (O(1)), `get(T)`, `get(int)`, `del(T)`, `delFirst()`, `delLast()`, `existeDato(T)`, `getPrimero()`, `getUltimo()`, `copy()`, `getIterador()` |
| **Usos** | Inventario del jugador (`ListaDE<Objeto>`), listas de enemigos/objetos actuales en Partida, caché de imágenes en PantallaJuego, serializacion JSON |
| **Riesgos** | `get(int)` no optimiza (siempre desde cabeza, nunca desde tail); `copy()` devuelve `ListaDE<T>` en vez de `Lista<T>` (posible violacion LSP); `getPrimero()`/`getUltimo()` no estan en la interfaz `Lista` |

### 7.3 Cola — Cola FIFO

| Atributo | Valor |
|---|---|
| **Archivo** | `src/Estructuras/Cola.java` |
| **Tipo** | `Cola<T> implements InterfazCola<T>` |
| **Almacenamiento** | `ElementoSE<T> primero, ultimo` + `int size` |
| **Metodos publicos** | `offer(T)` (O(1)), `poll()` (O(1)), `peek()` (O(1)), `isEmpty()`, `getSize()`, `clear()` |
| **Usos** | BFS en Cueva (`Cola<PasoBFS>`), BFS en Grafo (`Cola<Cueva>`), animaciones en PantallaJuego |
| **Riesgos** | Sin `copy()` ni iterador; `poll()` devuelve `null` si vacia (sin excepcion) |

### 7.4 Grafo — Grafo dirigido

| Atributo | Valor |
|---|---|
| **Archivo** | `src/Estructuras/Grafo.java` |
| **Tipo** | `Grafo<T> implements InterfazGrafo<T>` |
| **Almacenamiento** | `ListaSE<NodoGrafo<T>> nodos` + `ListaSE<ArcoGrafo<T>> arcos` |
| **Metodos publicos** | `addNodo(T)`, `addArco(T, T, String)`, `existeNodo(T)`, `existeConexion(T, T)`, `getAdyacentes(T)`, `recorridoBFS(T)`, `existeCamino(T, T)`, `caminoMinimo(T, T)`, `getDistanciaMinima(T, T)`, `getNodos()`, `getArcos()` |
| **Usos** | Mazmorra (`Grafo<Cueva>`) — conexiones dirigidas entre cuevas |
| **Riesgos** | BFS es O(V²+E) porque la comprobacion de visitados usa `existeDato()` (O(V)) dentro del bucle interno; todas las busquedas de nodos/arcos son O(n) lineales sobre ListaSE; `caminoMinimo` asume pesos uniformes (BFS sin pesos) |

### 7.5 Clases auxiliares

| Clase | Archivo | Proposito |
|---|---|---|
| `ElementoSE<T>` | `src/Estructuras/ElementoSE.java` | Nodo de lista simplemente enlazada (dato + siguiente) |
| `ElementoDE<T>` | `src/Estructuras/ElementoDE.java` | Nodo de lista doblemente enlazada (dato + anterior + siguiente) |
| `IteradorSE<T>` | `src/Estructuras/IteradorSE.java` | Iterador para ListaSE (solo hasNext/next) |
| `IteradorDE<T>` | `src/Estructuras/IteradorDE.java` | Iterador para ListaDE (solo hasNext/next) |
| `NodoGrafo<T>` | `src/Estructuras/NodoGrafo.java` | Nodo de grafo con id, dato y arcos de salida |
| `ArcoGrafo<T>` | `src/Estructuras/ArcoGrafo.java` | Arco dirigido e inmutable con origen, destino y etiqueta |
| `Lista<T>` | `src/Estructuras/Lista.java` | Interfaz base para ListaSE y ListaDE |
| `InterfazCola<T>` | `src/Estructuras/InterfazCola.java` | Interfaz para Cola |
| `InterfazGrafo<T>` | `src/Estructuras/InterfazGrafo.java` | Interfaz para Grafo |
| `MiIterador<T>` | `src/Estructuras/MiIterador.java` | Interfaz de iterador (hasNext/next) |

### 7.6 Estructuras no implementadas

- **Pila (Stack):** No existe. No se ha necesitado hasta ahora.
- **ListaCircular:** No existe. Se menciono como posible rotacion de turnos, pero no se implemento.
- **ArbolBinarioDeBusqueda:** No existe. Se menciono como posible justificacion academica, pero no se llego a usar.

## 8. UML

El diagrama inicial esta en:

```text
diagrama_inicial_juego.puml
```

Debe actualizarse cuando se cierre una decision importante de arquitectura.
