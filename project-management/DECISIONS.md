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

## D-10 Responsabilidad de Partida

`Partida` es la fachada principal de logica del juego.

Decision:

```text
Partida coordina Mazmorra, Jugador, enemigos, objetos, puertas, turnos, combate, victoria, derrota y log.
```

`Partida` no debe construir desde cero la mazmorra. La mazmorra debe llegar ya creada desde JSON, tests o una fabrica de partida.

El contrato publico no debe exponer referencias mutables completas de `Jugador`, `Mazmorra`, `Cueva`, `Celda` o `Puerta`. Para pintar el estado se usaran vistas simples como `PersonajeEnMapa`, `CuevaEnMapa` y `CeldaEnMapa`.

Motivo:

- Evita mezclar carga de datos con reglas de juego.
- Permite que JavaFX consulte y ejecute acciones mediante metodos de alto nivel.
- Facilita probar la logica sin depender de la interfaz ni del JSON.
- Reduce el riesgo de que la UI salte reglas de `Partida` modificando objetos internos directamente.

## D-11 Contenidos por cueva

Decision inicial:

```text
Partida gestiona enemigos y objetos por cueva.
```

No se meten todavia enemigos ni objetos dentro de `Cueva`, para no mezclar mapa estructural con logica de partida.

La implementacion debe quedar preparada para que en una iteracion futura cada `Cueva` pueda tener directamente sus propios contenidos si el grupo lo decide.

## D-12 Objetos con posicion

Decision:

```text
Los objetos del suelo se representan con objeto + cueva + fila + columna.
```

Motivo:

- Los objetos base no deberian cargar con posicion si tambien pueden estar en inventario.
- JavaFX necesita saber donde pintar cada objeto.
- Partida necesita comprobar si el jugador puede recoger objetos cercanos.

## D-13 Puertas y llaves

Decision:

```text
Puerta representa una conexion entre cuevas con codigo de llave.
```

Para avanzar entre cuevas, el jugador debe tener una `Llave` de tipo `PUERTA` cuyo codigo coincida con la cerradura de la puerta.

`Partida` no conecta cuevas ni expone un metodo publico para crear conexiones. Las conexiones estructurales se crean en `Mazmorra` mediante `conectarCuevas`. Las puertas entran como configuracion inicial de la partida y solo anaden la regla jugable de llave sobre una conexion que ya existe.

Si una puerta ya esta abierta, se puede atravesar sin volver a exigir la llave. La llave se exige para abrirla por primera vez.

Motivo:

- La conexion del grafo indica que existe camino.
- La puerta indica la regla jugable para poder usar esa conexion.
- Evita confundir configuracion de mapa con acciones de partida.

## D-14 Recoger objetos

Decision:

```text
El jugador puede recoger objetos situados en celdas adyacentes.
```

Se considera adyacente cualquier celda alrededor del jugador, incluyendo diagonales. La celda actual tambien se acepta para no bloquear objetos colocados bajo el jugador.

## D-14b Ocupacion de celdas

Decision:

```text
Jugador y enemigos no pueden compartir celda.
```

Reglas de la primera version:

- `Partida` no permite colocar un enemigo sobre otro enemigo vivo.
- `Partida` no permite colocar un enemigo sobre el jugador en la cueva actual.
- El jugador no puede moverse a una celda ocupada por un enemigo vivo.
- El jugador no puede avanzar a una cueva destino si su celda de entrada actual estaria ocupada por un enemigo vivo en esa cueva.
- Un enemigo no debe moverse a una celda ocupada por otro enemigo vivo ni por el jugador.

Esta regla vive de momento en `Partida`, porque los enemigos todavia no estan integrados dentro de `Cueva`.

## D-15 Ataque del jugador

Decision:

```text
El jugador puede atacar enemigos adyacentes, incluyendo diagonales.
Si tiene arco equipado, puede atacar a distancia.
```

La formula base de dano es:

```text
dano = ataque - defensa
```

Si el ataque es valido, el dano minimo sera 1.

## D-16 Turno enemigo

Decision:

```text
Los enemigos atacan si estan adyacentes al jugador; si no, se acercan usando camino minimo.
```

La primera version no tendra enemigos a distancia. Esa mejora queda apuntada para una iteracion posterior.

## D-17 Fin de turno

Decision:

```text
El turno termina cuando el jugador pulsa pasarTurno().
```

Mover o actuar no finaliza automaticamente el turno. Aun asi, la partida puede controlar si el jugador ya hizo movimiento o accion dentro del turno actual.

## D-18 Victoria

Decision:

```text
El jugador gana al conseguir la llave final.
```

La llave final solo se obtiene derrotando al boss final. La implementacion inicial puede representar esta llave como una `Llave` especial entregada al matar al boss.

## D-19 Log de partida

Decision inicial:

```text
El log vive en Partida como ListaSE<String>.
```

Si mas adelante el log necesita filtros, tipos de evento o formato especifico para JavaFX, se podra crear una clase `LogJuego`.
