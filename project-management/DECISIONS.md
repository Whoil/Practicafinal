# Decisiones del Proyecto

## D-01 Tema del juego

El juego sera una mazmorra con 3 cuevas:

- Facil.
- Media.
- Dificil con boss.

## D-02 Mazmorra y grafo

`Mazmorra` no hereda de `Grafo`.

Decision:

```text
Mazmorra contiene Grafo<Cueva>
```

Motivo:

- Una mazmorra no es una estructura de datos, sino un concepto del juego.
- El grafo es un detalle de implementacion para conectar cuevas.
- Facilita explicar responsabilidades.

## D-03 Cueva y matriz

`Cueva` no hereda de `Grafo`.

Decision:

```text
Cueva contiene ListaSE<ListaSE<Celda>>
```

Motivo:

- El enunciado exige habitaciones/cuevas como matrices.
- JavaFX con GridPane encaja mejor con filas y columnas.
- Evita mantener un grafo permanente de celdas.

## D-04 Movimiento interno

El movimiento dentro de una cueva se calcula con BFS sobre vecinos implicitos:

```text
arriba, abajo, izquierda, derecha
```

No hay movimiento diagonal.

La cola usada para BFS debe ser la cola propia.

## D-05 Herencia de personajes

Decision:

```text
Personaje abstracto
  Jugador
  Enemigo
    Boss
```

Motivo:

- Comparte vida, ataque, defensa, movimiento y posicion.
- Permite polimorfismo sencillo.
- Boss puede reutilizar comportamiento de enemigo.

## D-06 Herencia de objetos

Decision:

```text
Objeto abstracto
  Pocion
  Arma
    Espada
    Arco
  Escudo
  Llave
```

Nota:

- Arco y escondite quedan como extras si no hay version jugable.

## D-07 Inventario

El inventario del jugador usa `ListaDE<Objeto>`.

Motivo:

- Es una estructura propia ya disponible.
- Permite insertar, borrar y recorrer objetos.
- Es facil de justificar en la memoria.

## D-08 Coordinacion con JavaFX

JavaFX no modifica directamente estructuras internas.

Debe usar metodos de alto nivel de `Partida`, por ejemplo:

```text
moverJugador(fila, columna)
atacar(fila, columna)
recoger(fila, columna)
usarObjeto(idObjeto)
abrirPuerta(fila, columna)
```

## D-09 Extras

Los extras quedan congelados hasta tener partida completa:

- Mas enemigos.
- Mas armas.
- Dificultades configurables.
- IA avanzada.
- Animaciones.
- Escondite avanzado.

