# Guion para el vídeo de presentación

Duración recomendada: 6-8 minutos.

Objetivo: mostrar que el proyecto es jugable, que cumple los requisitos técnicos y que el equipo entiende las decisiones tomadas.

## 1. Introducción breve

**Persona 1**

Hola, somos Guillermo, Álvaro y Héctor. En este vídeo vamos a presentar nuestro proyecto final, **Escape de la Mazmorra**: un videojuego por turnos desarrollado en Java, con interfaz JavaFX, estructuras de datos propias, carga y guardado en JSON, pruebas JUnit y documentación técnica.

La idea del juego es que el jugador controle a un mago que debe avanzar por varias cuevas, recoger objetos, combatir enemigos, abrir puertas con llaves y escapar de la mazmorra tras superar la zona final.

## 2. Arranque del juego y menú principal

**Persona 1**

Al ejecutar el proyecto aparece el menú principal. Desde aquí se puede iniciar una partida nueva, cargar una partida guardada, consultar los controles, ver el ranking o salir del juego.

En esta parte conviene enseñar:

- Menú principal.
- Botón de nueva partida.
- Pantalla de nombre del jugador, si aparece.
- Pantalla de controles o ayuda.

Frase útil:

El menú no es solo decorativo: centraliza el flujo de la aplicación y permite acceder a las funciones principales sin entrar directamente a la partida.

## 3. Demo jugable

**Persona 2**

Ahora mostramos una partida. El jugador aparece en una cueva inicial y puede moverse por casillas usando teclado. La interfaz está dividida en mapa, estadísticas, inventario, acciones y log de eventos.

En esta parte conviene enseñar:

- Movimiento del jugador.
- Log inferior explicando acciones.
- Estadísticas del jugador.
- Inventario y equipo.
- Recoger un objeto.
- Usar o equipar un objeto si se puede.

Frase útil:

La partida funciona por turnos. El jugador puede moverse, realizar acciones como atacar o recoger objetos, y después los enemigos actúan según las reglas de la lógica.

## 4. Combate, enemigos y objetos

**Persona 2**

El combate se gestiona desde la capa de modelo. Cada personaje tiene vida, ataque, defensa, movimiento y posición. El jugador puede atacar enemigos cercanos y también se han incorporado acciones especiales como ataques direccionales o habilidades, según la versión final del juego.

Los objetos también forman parte de la lógica: hay pociones, armas, escudos, llaves y cofres. El inventario utiliza una estructura propia, no colecciones directas de Java, y el equipo diferencia arma y escudo.

En esta parte conviene enseñar:

- Atacar a un enemigo.
- Derrotar un enemigo si es rápido.
- Ver una llave, espada, arco, escudo o poción.
- Abrir cofre o recoger objeto.

Frase útil:

Aunque en pantalla se vea como un juego visual, las reglas importantes están en el modelo y se pueden probar sin depender de JavaFX.

## 5. Cuevas, mapa y estructuras propias

**Persona 3**

El juego está formado por varias cuevas conectadas. Internamente se usan estructuras propias vistas en la asignatura, como listas enlazadas, cola y grafo.

La matriz de cada cueva se representa con listas propias, y el grafo permite modelar las conexiones entre cuevas. La cola se utiliza para recorridos como BFS, por ejemplo para caminos o cálculos de distancia.

En esta parte conviene enseñar:

- Cambio de cueva mediante puerta.
- Mapa con muros, suelo, puertas, objetos y enemigos.
- Si no se enseña en el juego, mostrar rápidamente el UML de componentes o el diagrama de mapa.

Frase útil:

Una decisión importante fue separar la puerta jugable de la conexión del grafo. El grafo dice que dos cuevas están conectadas, pero la puerta y la llave deciden si el jugador puede avanzar.

## 6. JSON, guardado y carga

**Persona 3**

Otra parte importante es la persistencia. El proyecto usa JSON para tres cosas: la configuración inicial, el guardado de partida y el ranking.

La configuración de la mazmorra está en archivos JSON, así que se pueden cambiar mapas, enemigos u objetos sin tocar directamente el código Java. Para guardar y cargar partida usamos DTOs y Gson, evitando serializar directamente referencias internas complejas.

En esta parte conviene enseñar:

- Archivo `datos/cuevas.json` o carpeta `docs/entrega/json_ejemplo/`.
- Botón de guardar partida.
- Botón de cargar partida, si se quiere demostrar.
- Ranking, si hay tiempo.

Frase útil:

La vista no escribe JSON directamente. La interfaz delega en la partida, y la partida delega en los serializadores. Así mantenemos separadas interfaz, lógica y persistencia.

## 7. Pruebas y validación

**Persona 1**

Para validar el proyecto se han creado pruebas JUnit. Cubren estructuras propias, personajes, objetos, mapa, partida, combate, puertas, JSON, guardado y ranking.

La parte visual de JavaFX se ha validado sobre todo manualmente, porque depende de ventana, recursos gráficos y experiencia de usuario. En cambio, las reglas deterministas del juego se han llevado a tests automatizados.

En esta parte conviene enseñar:

- Ejecución de tests en IntelliJ o terminal.
- Resultado de tests correctos.
- Alguna clase de test representativa, como `PartidaTest`, `CargadorConfiguracionTest` o tests de estructuras.

Frase útil:

La estrategia ha sido probar automáticamente la lógica y revisar manualmente la experiencia visual, porque cada tipo de comprobación tiene necesidades distintas.

## 8. Documentación y uso de IA

**Persona 2**

El proyecto también incluye documentación de arquitectura, decisiones, tareas, diario de IA y memoria de entrega. La IA se ha usado como apoyo para implementar, revisar, documentar y detectar riesgos, pero las decisiones de alcance se han validado por el equipo.

En esta parte conviene enseñar:

- `docs/entrega/MEMORIA_RESUMIDA.md`.
- `docs/entrega/IA_DIARY_RESUMIDO.md`.
- UML de componentes MVC.
- UML de guardado y carga JSON.

Frase útil:

El uso de IA queda trazado en el diario. Esto permite explicar que no se ha usado como caja negra, sino como herramienta de apoyo revisada por el equipo.

## 9. Cierre

**Persona 3**

En resumen, Escape de la Mazmorra es una aplicación jugable que integra programación orientada a objetos, estructuras propias, grafo, JSON, JavaFX, pruebas y documentación. El resultado final cumple el objetivo académico y además permite hacer una demostración completa del juego.

Como mejoras futuras se podrían añadir más cuevas, más enemigos, nuevos objetos, generación procedural o pruebas automáticas de interfaz.

Gracias por ver la presentación.

## Orden recomendado para grabar

1. Abrir el juego desde el lanzador o IntelliJ.
2. Mostrar menú principal.
3. Iniciar partida.
4. Moverse, recoger objeto y enseñar inventario.
5. Atacar enemigo o usar acción especial.
6. Mostrar puerta/cambio de cueva si es posible.
7. Guardar o cargar partida.
8. Mostrar ranking o pantalla final si hay una partida preparada.
9. Cambiar a IntelliJ/GitHub y enseñar tests.
10. Mostrar memoria resumida, JSON de ejemplo y UML.

## Reparto sugerido

- Parte A: estructuras, mapa, grafo, BFS y ranking si corresponde.
- Parte B: personajes, objetos, inventario, combate, turnos y reglas de partida.
- Parte C: JavaFX, menú, pantallas, JSON, guardado/carga y recursos visuales.
- Coordinación entre los tres: metodología, documentación, pruebas, uso de IA y cierre.

## Consejos para la grabación

- Preparar una partida guardada cerca de una puerta, un enemigo y algún objeto.
- Evitar depender de que salga una situación concreta durante la grabación.
- No leer todo literal: usar este guion como apoyo.
- Si algo falla visualmente, explicar la funcionalidad desde tests o JSON en lugar de perder tiempo.
- Mantener el vídeo centrado en requisitos: estructuras propias, JSON, JavaFX, tests y juego funcional.
