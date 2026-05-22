# Boceto JavaFX — Escape de la Mazmorra

## 1. Objetivo

Este documento define el diseno visual y la distribucion de componentes de la
interfaz JavaFX del juego, antes de implementarla. Sirve como guia para C-04
(JavaFX minima) y como referencia para que el grupo valide la disposicion de
pantalla, las acciones disponibles y la informacion mostrada.

## 2. Restricciones del proyecto que afectan al boceto

| Restriccion | Impacto en JavaFX |
|---|---|
| Solo metodos de alto nivel de Partida | JavaFX no puede modificar celdas, enemigos ni objetos directamente. Todo pasa por `Partida.moverJugador()`, `Partida.atacar()`, etc. |
| Partida no existe (B-03 pendiente) | El boceto define el contrato que Partida debera exponer. C-04 implementara con datos mock si B-03 no esta listo. |
| Matriz propia `ListaSE<ListaSE<Celda>>` | JavaFX recorre la estructura propia para pintar el GridPane. No se convierte a array. |
| Sin colecciones prohibidas | JavaFX puede usar `ObservableList` de JavaFX (no es `java.util.*` sustituto de estructuras). El resto de datos del modelo se mantiene en estructuras propias. |
| Sin animaciones (extra congelado) | Sin transiciones, sin efectos visuales, sin movimiento animado de personajes. |
| Arco y escondite congelados | No se incluyen en el panel de acciones. |
| Inventario con `ListaDE<Objeto>` | El panel de inventario itera con `MiIterador<Objeto>` sobre la lista del Jugador. |

## 3. Distribucion general de la ventana

```
+-------------------------------------------------------------------+
|  ESCAPE DE LA MAZMORRA                        [Cueva: Facil]      |
+-------------------------------------------------------------------+
|                            |  ESTADO DEL JUGADOR                  |
|                            |  Vida:      ████████░░  80/100       |
|                            |  Ataque:    15 (+12) = 27            |
|                            |  Defensa:   5                         |
|                            |  Movimiento: 3                        |
|                            |  Turnos:     35/40                    |
|    MATRIZ DE LA CUEVA      |  Cueva:     Cueva Facil              |
|    (GridPane centrado)     |                                       |
|                            |  INVENTARIO                           |
|    [M][M][M][M][M]         |  [1] Espada        (equipada)  [U]   |
|    [M][J][ ][ ][M]         |  [2] Pocion de cura           [U]   |
|    [M][ ][M][E][M]         |  [3] Llave dorada             [U]   |
|    [M][ ][ ][P][M]         |                                       |
|    [M][M][M][M][M]         |  ACCIONES                             |
|                            |  [↑] [↗] [→] [↘] [↓] [↙] [←] [↖]   |
|                            |  [Atacar] [Recoger] [Usar] [Equipar] |
|                            |  [Abrir puerta] [Esperar turno]      |
+-------------------------------------------------------------------+
|  LOG DE EVENTOS                                                    |
|  > Has entrado en la Cueva Facil.                                  |
|  > Te has movido a (1, 2).                                         |
|  > Has recogido una pocion.                                        |
|  > Has atacado al Goblin (10 de dano).                             |
+-------------------------------------------------------------------+
```

### Leyenda de la matriz

| Simbolo | Significado | Color propuesto |
|---|---|---|
| `[M]` | Muro | Gris oscuro |
| `[ ]` | Suelo | Beige claro |
| `[J]` | Jugador | Azul |
| `[E]` | Enemigo | Rojo |
| `[B]` | Boss | Rojo oscuro |
| `[P]` | Objeto en suelo | Verde |
| `[T]` | Tesoro | Dorado |
| `[S]` | Salida | Blanco brillante |
| `[U]` | Puerta | Marron |
| `[X]` | Trampa | Naranja |
| `[I]` | Inicio | Verde claro |

## 4. Zonas detalladas

### 4.1 Zona de matriz (GridPane)

Ocupa la mitad izquierda de la ventana. Es un GridPane de `filas x columnas`
donde cada celda es un Rectangle o un boton sin texto.

- Cada celda se colorea segun su `TipoCelda`.
- El Jugador se pinta como un circulo o icono sobre la celda actual.
- Los Enemigos se pintan como iconos sobre sus celdas.
- Los Objetos en el suelo se pintan como iconos sobre sus celdas.
- Las celdas alcanzables (movimiento) se resaltan con un borde o tono
  distinto cuando el jugador selecciona "Mover".
- Al hacer clic en una celda alcanzable, se ejecuta `Partida.moverJugador(f,c)`.

**Datos necesarios de Partida:**
- `getCuevaActual().getFilas()`, `getColumnas()` — dimensiones
- `getCuevaActual().getCelda(f,c).getTipo()` — tipo de cada celda
- `getJugador().getFila()`, `getColumna()` — posicion del jugador
- `getEnemigosCuevaActual()` — lista de enemigos con posiciones
- `getObjetosCuevaActual()` — lista de objetos en el suelo
- `getCeldasAlcanzables()` — celdas donde el jugador puede moverse

### 4.2 Zona de estado del jugador

Panel a la derecha, arriba. Muestra:

| Campo | Fuente de datos |
|---|---|
| Nombre | `Jugador.getNombre()` |
| Vida actual / maxima | `Jugador.getVidaActual()` / `getVidaMaxima()` |
| Barra de vida | Rectangle escalado segun porcentaje de vida |
| Ataque total | `Jugador.getAtaqueTotal()` (base + arma) |
| Defensa total | `Jugador.getDefensaTotal()` (base + escudo) |
| Movimiento | `Jugador.getMovimiento()` |
| Turnos restantes | `Partida.getTurnosRestantes()` |
| Cueva actual | `Mazmorra.getCuevaActual().getId()` |
| Arma equipada | `Jugador.getArmaEquipada().getNombre()` o "Ninguna" |
| Escudo equipado | `Jugador.getEscudoEquipado().getNombre()` o "Ninguno" |

### 4.3 Zona de inventario

Lista de objetos que lleva el jugador, con boton "Usar" por objeto.

- Se itera `Jugador.getInventario()` con iterador.
- Cada entrada muestra: numero, nombre del objeto.
- Si el objeto es equipable (`Objeto.esEquipable()`), el boton dice "Equipar".
- Si el objeto es consumible (`Objeto.esConsumible()`), el boton dice "Usar".
- Si el objeto es una Llave, el boton dice "Usar" (abrira puerta via Partida).
- El objeto equipado actualmente se marca visualmente (negrita, icono).

**Metodos necesarios de Partida:**
- `usarObjeto(idObjeto)` — para consumibles
- `equiparObjeto(idObjeto)` — para equipables

### 4.4 Zona de acciones

Botones de accion organizados en dos grupos:

**Grupo 1: Movimiento** — 8 botones direccionales (arriba, abajo, izquierda,
derecha y diagonales). Alternativa: hacer clic en la matriz directamente.

**Grupo 2: Acciones** — botones para:

| Boton | Metodo de Partida | Visible cuando... |
|---|---|---|
| Atacar | `Partida.atacar(fila, columna)` | Hay enemigo adyacente |
| Recoger | `Partida.recoger(fila, columna)` | Hay objeto en la misma celda |
| Usar | `Partida.usarObjeto(id)` | Hay objeto seleccionado en inventario |
| Equipar | `Partida.equiparObjeto(id)` | Objeto equipable seleccionado |
| Abrir puerta | `Partida.abrirPuerta(fila, columna)` | Jugador junto a una PUERTA |
| Esperar turno | `Partida.esperarTurno()` | Siempre visible |

Los botones se habilitan/deshabilitan segun el contexto. Por ejemplo,
"Atacar" solo esta activo si hay un enemigo en una celda adyacente.

### 4.5 Zona de log

TextArea o ListView en la parte inferior, de altura reducida.

- Muestra los ultimos eventos en orden cronologico.
- Cada evento es una linea de texto.
- Los eventos se obtienen de `Partida.getLog()` o se anaden localmente
  cuando el usuario ejecuta una accion y Partida devuelve resultado.

**Datos necesarios de Partida:**
- `getLog()` — devuelve `ListaSE<String>` o similar

## 5. Flujo de datos

```
Partida (B-03)
  |
  ├── getCuevaActual() ──> Cueva
  │                          ├── getFilas(), getColumnas()
  │                          ├── getCelda(f,c) ──> TipoCelda
  │                          └── getCeldasAlcanzables(jf,jc, pasos)
  │
  ├── getJugador() ──> Jugador
  │                      ├── getVidaActual(), getVidaMaxima()
  │                      ├── getAtaqueTotal(), getDefensaTotal()
  │                      ├── getFila(), getColumna()
  │                      ├── getInventario() ──> ListaDE<Objeto>
  │                      ├── getArmaEquipada(), getEscudoEquipado()
  │                      └── getMovimiento()
  │
  ├── getEnemigosCuevaActual() ──> ListaSE<Enemigo>
  ├── getObjetosCuevaActual() ──> ListaSE<Objeto>
  ├── getTurnosRestantes()
  ├── getLog() ──> ListaSE<String>
  │
  ├── moverJugador(f,c) ──> boolean
  ├── atacar(f,c) ──> ResultadoAtaque
  ├── recoger(f,c) ──> boolean
  ├── usarObjeto(id) ──> boolean
  ├── equiparObjeto(id) ──> boolean
  ├── abrirPuerta(f,c) ──> boolean
  └── esperarTurno() ──> void
```

## 6. Estructura de clases propuesta para C-04

```
src/vista/
  └── VentanaPrincipal.java     — Scene, layout, inicializacion
  └── PanelMatriz.java           — GridPane de la cueva
  └── PanelEstado.java           — Estado del jugador
  └── PanelInventario.java       — Lista de objetos
  └── PanelAcciones.java         — Botones de accion
  └── PanelLog.java              — TextArea de eventos

src/controlador/
  └── ControladorJuego.java      — Conecta vista con Partida
```

### 6.1 Responsabilidades

**VentanaPrincipal.java**
- Crea el Stage y la Scene.
- Organiza los paneles en un BorderPane.
- Inicializa Partida (carga JSON, crea Jugador, etc.).
- Pasa la referencia de Partida al Controlador.
- Metodo `refrescar()` que recorre todos los paneles y actualiza su
  contenido. Se llama tras cada accion del jugador.

**PanelMatriz.java**
- Recibe una Cueva + posicion del jugador + enemigos + objetos.
- Construye un GridPane de Rectangle/Button.
- Colorea cada celda segun TipoCelda.
- Pinta iconos de jugador, enemigos y objetos.
- Resalta celdas alcanzables.
- Notifica clics al controlador.

**PanelEstado.java**
- Recibe datos del Jugador + Mazmorra.
- Muestra labels con valores y barra de vida.

**PanelInventario.java**
- Recibe ListaDE<Objeto> + referencias de equipo.
- Muestra cada objeto con boton de accion.
- Notifica "usar" o "equipar" al controlador.

**PanelAcciones.java**
- Botones de accion.
- Habilita/deshabilita segun contexto.
- Notifica acciones al controlador.

**PanelLog.java**
- TextArea no editable.
- Metodo `anadirEvento(String)` para agregar lineas.

**ControladorJuego.java**
- Implementa `EventHandler<ActionEvent>` y `MouseEventHandler`.
- Recibe la referencia a Partida.
- Cada handler llama al metodo correspondiente de Partida.
- Despues de cada accion, llama a `VentanaPrincipal.refrescar()`.

### 6.2 Ciclo de una accion tipica

1. Usuario hace clic en boton "Mover derecha" o en celda de la matriz.
2. ControladorJuego recibe el evento.
3. Controlador llama a `Partida.moverJugador(fila, columna)`.
4. Partida ejecuta la logica (valida movimiento, mueve, turno enemigo...).
5. Partida devuelve resultado (exito/fallo) y actualiza su estado interno.
6. Controlador anade evento al log ("Te has movido a (f, c)").
7. Controlador llama a `VentanaPrincipal.refrescar()`.
8. Todos los paneles se repintan con el nuevo estado.

## 7. Contrato minimo que debe exponer Partida (B-03) para que JavaFX funcione

```
// Consulta de estado
String      getEstadoPartida();           // "EN_CURSO", "VICTORIA", "DERROTA"
Cueva       getCuevaActual();
Jugador     getJugador();
ListaSE<Enemigo> getEnemigosCuevaActual();
ListaSE<Objeto>  getObjetosCuevaActual();
ListaSE<Celda>   getCeldasAlcanzables();
int         getTurnosRestantes();
ListaSE<String>  getLog();

// Acciones
boolean     moverJugador(int fila, int columna);
boolean     atacar(int fila, int columna);
boolean     recoger(int fila, int columna);
boolean     usarObjeto(String idObjeto);
boolean     equiparObjeto(String idObjeto);
boolean     abrirPuerta(int fila, int columna);
void        esperarTurno();
```

## 8. Mock para C-04 si B-03 no esta listo

Si Partida no existe cuando se implemente C-04, se creara un mock
`PartidaMock` que implemente el mismo contrato con datos fijos:

- Carga `datos/cuevas.json` con `CargadorConfiguracion`.
- Crea un `Jugador` en posicion inicial.
- Los metodos de accion devuelven true/false segun reglas simples
  (sin combate real, sin logica de enemigos).
- Suficiente para probar que la interfaz pinta y responde.

## 9. Checklist de verificacion del boceto

- [ ] Muestra la matriz de la cueva actual con colores por TipoCelda.
- [ ] Muestra al jugador en su posicion actual.
- [ ] Muestra enemigos en sus posiciones.
- [ ] Muestra objetos en el suelo.
- [ ] Muestra estado del jugador (vida, ataque, defensa, movimiento, turnos).
- [ ] Muestra inventario con boton de accion por objeto.
- [ ] Muestra acciones disponibles (movimiento, ataque, recoger, usar, equipar, abrir puerta, esperar).
- [ ] Muestra log de eventos.
- [ ] No usa colecciones prohibidas.
- [ ] No modifica directamente estructuras internas del modelo.
- [ ] Usa solo metodos de alto nivel de Partida.
- [ ] Carga configuracion desde JSON.
- [ ] La ventana se actualiza tras cada accion.
