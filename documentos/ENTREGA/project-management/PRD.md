# PRD - Escape de la Mazmorra

## 1. Objetivo del proyecto

Crear un juego sencillo por turnos en Java con interfaz JavaFX, usando estructuras de datos propias. El jugador debe escapar de una mazmorra compuesta por tres cuevas conectadas entre si.

El objetivo principal no es hacer un videojuego grande, sino demostrar:

- Diseno orientado a objetos.
- Uso de estructuras propias.
- Grafo de cuevas.
- Matriz propia para cada cueva.
- Movimiento por turnos.
- Inventario y objetos.
- Enemigos y combate.
- Persistencia JSON.
- Interfaz JavaFX clara.
- Pruebas y documentacion.

## 2. Alcance minimo

La version minima debe permitir jugar una partida completa:

- Una mazmorra con 3 cuevas.
- Cada cueva es una matriz propia.
- Las cuevas estan conectadas mediante un grafo propio.
- El jugador se mueve por la cueva actual.
- El jugador puede recoger y usar objetos.
- Hay enemigos normales y un boss final.
- Hay puertas entre cuevas.
- Al menos una puerta requiere llave.
- Hay una salida final.
- El jugador gana si llega a la salida final cumpliendo sus requisitos.
- El jugador pierde si se queda sin vida o sin turnos.
- La partida se puede cargar desde JSON.
- La partida se puede guardar y cargar desde JSON.
- La interfaz muestra matriz, jugador, enemigos, objetos, inventario, estado, acciones y log.

## 3. Mundo del juego

Nombre provisional: Escape de la Mazmorra.

La mazmorra contiene 3 cuevas:

| Cueva | Tamano | Dificultad | Contenido minimo |
|---|---:|---|---|
| Cueva Facil | 5x5 | Facil | Jugador inicial, 1 enemigo, 1 objeto, puerta |
| Cueva Media | 6x6 | Media | 2 enemigos, llave, arma u objeto, puerta con requisito |
| Cueva Dificil | 7x7 | Dificil | Boss, salida final, trampas u objetos |

## 4. Jugador inicial

Valores propuestos, pendientes de confirmar por el grupo:

| Atributo | Valor inicial |
|---|---:|
| Vida | 100 |
| Ataque | 15 |
| Defensa | 5 |
| Movimiento | 3 |
| Turnos | 40 |

## 5. Acciones del jugador

En un turno, el jugador puede hacer como maximo:

- 1 movimiento.
- 1 accion.

Acciones minimas:

- Moverse.
- Atacar.
- Recoger objeto.
- Usar objeto.
- Equipar objeto.
- Abrir puerta.
- No hacer nada.

Despues del jugador actuan los enemigos de la cueva actual.

## 6. Objetos minimos

| Objeto | Tipo | Efecto | Prioridad |
|---|---|---|---|
| Pocion | Fungible | Cura vida | Obligatorio |
| Llave | Uso/requisito | Abre una puerta concreta | Obligatorio |
| Espada | Equipable | Aumenta ataque | Obligatorio |
| Escudo | Equipable | Aumenta defensa | Opcional si da tiempo |
| Arco | Equipable | Ataque a distancia | Extra |
| Objeto para esconderse | Especial | Evita ataque o reduce deteccion | Extra |

## 7. Enemigos

Modelo minimo:

- `Enemigo`: enemigo normal.
- `Boss`: hereda de `Enemigo`, con mas vida, ataque y defensa.

La logica exacta de movimiento se cerrara en `DECISIONS.md`. La idea inicial es que el enemigo use camino minimo hacia el jugador con BFS sobre las celdas transitables de la cueva.

## 8. Victoria y derrota

Victoria minima:

- Llegar a la salida final de la cueva dificil.
- Cumplir el requisito de la salida final, por ejemplo derrotar al boss.

Derrota:

- Vida del jugador llega a 0.
- Turnos restantes llegan a 0.

## 9. Extras congelados

No implementar hasta tener una version jugable:

- Mas cuevas.
- Mas tipos de enemigos.
- Dificultades configurables.
- Arco con reglas complejas.
- Sistema avanzado de escondite.
- Animaciones.
- Objetos con duracion temporal compleja.
- Boss con IA especial compleja.

