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

## 4. Paquetes propuestos

Pendiente de adaptar al proyecto real, pero se recomienda:

```text
src/estructuras
src/modelo/juego
src/modelo/mapa
src/modelo/personajes
src/modelo/objetos
src/modelo/acciones
src/json
src/vista
src/controlador
src/excepciones
test
```

Si se reutiliza el proyecto actual, se puede conservar `Estructuras`, `ParteA` y `ParteB.Grafo`, pero el codigo nuevo deberia estar ordenado en paquetes nuevos.

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

| Estructura | Uso previsto |
|---|---|
| `ListaSE` | Filas/celdas, listas de visitados, logs simples |
| `ListaDE` | Inventario, listas donde interese operar por extremos |
| `Cola` | Turnos y BFS |
| `ListaCircular` | Rotacion de turnos si se decide usar |
| `Grafo` | Conexion entre cuevas |
| `ArbolBinarioDeBusqueda` | Arbol de acciones o justificacion opcional |

## 8. UML

El diagrama inicial esta en:

```text
diagrama_inicial_juego.puml
```

Debe actualizarse cuando se cierre una decision importante de arquitectura.
