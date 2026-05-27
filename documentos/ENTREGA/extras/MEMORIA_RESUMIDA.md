# Escape de la Mazmorra - Memoria resumida

Esta memoria resumida presenta una versión sintética y defendible del proyecto **Escape de la Mazmorra**. La memoria ampliada completa se conserva en `documentos/MEMORIA.md` y `documentos/MEMORIA.pdf`, donde se desarrollan con más detalle las decisiones técnicas, la evolución del trabajo, los problemas encontrados y la crítica final.

## 1. Objetivo del proyecto

El objetivo del proyecto ha sido desarrollar un juego por turnos en Java que combinara lógica de videojuego, programación orientada a objetos, estructuras de datos propias, persistencia JSON, interfaz JavaFX, pruebas unitarias y documentación técnica. La premisa del juego es sencilla: el jugador controla a un mago que debe escapar de una mazmorra formada por tres cuevas conectadas, sobreviviendo a enemigos, gestionando objetos y alcanzando la salida final tras derrotar al jefe.

Aunque la versión final incluye elementos visuales, música, animaciones, ranking y pantallas narrativas, el foco principal sigue siendo académico. El proyecto demuestra que el equipo sabe diseñar estructuras propias, justificar costes, separar responsabilidades por capas, persistir datos en JSON y validar comportamiento mediante pruebas.

El juego final incluye tres cuevas de dificultad progresiva:

- **Las Criptas de Marfil**, como cueva inicial.
- **El Páramo Putrefacto**, como cueva intermedia.
- **El Abismo de Malakor**, como cueva final.

El jugador se mueve por celdas, combate enemigos, recoge objetos, usa inventario, abre cofres, atraviesa puertas con llaves, lanza hechizos y puede guardar o cargar la partida. La victoria se consigue al derrotar al jefe final, obtener la llave final y llegar a la celda de salida. La derrota se produce si el jugador muere o se queda sin turnos.

## 2. Arquitectura y patrón MVC

El proyecto se organiza siguiendo una separación cercana al patrón MVC. No se usa un framework MVC formal, pero sí existe una división clara entre modelo, vista, control y persistencia.

La capa de **modelo** contiene las reglas del juego y los datos de dominio. Incluye paquetes como `modelo.juego`, `modelo.mapa`, `modelo.personajes`, `modelo.objetos` y `Estructuras`. En esta capa viven clases como `Partida`, `Mazmorra`, `Cueva`, `Jugador`, `Enemigo`, `Objeto`, `Pocion`, `Llave`, `ListaSE`, `ListaDE`, `Cola` y `Grafo`.

La capa de **vista** contiene las pantallas JavaFX. Destacan `PantallaJuego`, `PantallaIntroduccion`, `PantallaTransicion`, `PantallaFinal`, `ReproductorMusica`, `ReproductorSfx` y `DatosTemaCueva`. Esta capa pinta el estado del juego, reproduce sonidos, muestra animaciones y recibe interacciones del usuario, pero no debe modificar directamente las estructuras internas de la lógica.

La capa de **control** coordina el flujo de la aplicación. `EscapeMazmorraApp` actúa como entrada JavaFX, menú y controlador principal de escenas. `ControladorFlujo` coordina introducción, transiciones, partida y pantalla final. `JuegoController` traduce teclado y ratón a acciones de alto nivel sobre `Partida`.

La capa **JSON** contiene DTOs, cargadores y serializadores. `CargadorConfiguracion` lee la configuración inicial desde `datos/cuevas.json`. `SerializadorPartida` guarda y carga partidas. `SerializadorRanking` persiste el ranking local.

Esta separación permite probar la lógica sin abrir una ventana JavaFX. Por ejemplo, `PartidaTest` comprueba movimiento, combate, victoria, derrota y reglas de puertas sin depender de la interfaz. Del mismo modo, los tests de JSON validan configuración y serialización sin pintar la pantalla.

## 3. Estructuras de datos propias

Una restricción importante del proyecto era usar estructuras propias en lugar de colecciones prohibidas de Java. Por eso se implementaron y usaron TADs propios en varias partes del juego.

`ListaSE<T>` es una lista simplemente enlazada. Se usa en la matriz de la cueva, en el log, en resultados de BFS, en almacenamiento interno del grafo y en recorridos auxiliares. Su ventaja es que es sencilla y coherente con las restricciones. Su desventaja principal es que el acceso por índice es lineal.

`ListaDE<T>` es una lista doblemente enlazada. Se usa sobre todo en inventario, listas auxiliares de enemigos u objetos, caché visual y ranking. Permite insertar al principio y al final con coste constante, y resulta adecuada para recorrer, eliminar y mantener colecciones pequeñas o medianas.

`Cola<T>` implementa una cola FIFO con operaciones `offer`, `poll`, `peek`, `isEmpty` y `clear`. Su uso principal es BFS, tanto dentro de una cueva como en el grafo de cuevas. Esta estructura es fundamental porque BFS necesita procesar nodos o celdas en orden de descubrimiento.

`Grafo<T>` representa un grafo dirigido mediante listas propias de nodos y arcos. En el proyecto se usa como `Grafo<Cueva>` dentro de `Mazmorra`. Esto permite representar conexiones entre cuevas sin hacer que la mazmorra herede de grafo. La puerta jugable queda separada del arco estructural: el arco indica que existe conexión y la puerta indica si el jugador puede atravesarla según la llave.

Cada `Cueva` usa una matriz propia:

```text
ListaSE<ListaSE<Celda>>
```

Esta decisión sacrifica acceso directo O(1), pero cumple el objetivo académico de evitar arrays bidimensionales o colecciones de Java como almacenamiento principal. Para los tamaños finales de las cuevas, el rendimiento práctico es suficiente.

## 4. Lógica del juego

La clase central de la lógica es `Partida`. Actúa como fachada: coordina jugador, mazmorra, enemigos, objetos, puertas, turnos, log, estadísticas y estado final. La interfaz no modifica directamente enemigos, celdas u objetos internos, sino que llama a métodos de alto nivel.

Las acciones principales son:

- mover al jugador;
- atacar;
- lanzar hechizos;
- recoger objetos;
- abrir cofres;
- usar pociones;
- equipar armas o escudos;
- atravesar puertas;
- guardar partida;
- pasar turno.

La partida diferencia entre movimiento y acción. En un turno el jugador puede moverse y realizar una acción. Después, al terminar turno, los enemigos actúan. Si están cerca, atacan; si no, intentan acercarse usando caminos calculados.

El combate se basa en ataque, defensa y vida. El daño mínimo es 1 para evitar ataques completamente nulos. Hay enemigos normales y un jefe final. Al derrotar al jefe se puede obtener la llave final necesaria para completar la partida.

El sistema de objetos incluye pociones, armas, escudos y llaves. El inventario del jugador usa `ListaDE<Objeto>`. Las armas y escudos se pueden equipar, las pociones se consumen y las llaves permiten abrir puertas o progresar por la mazmorra.

La lógica también incluye mejoras finales como estadísticas, puntuación, ranking, bola de fuego, bola de hielo, niebla de guerra y cofres abribles desde una celda adyacente.

## 5. Interfaz JavaFX

La interfaz comenzó como una idea de terminal y evolucionó hasta una aplicación JavaFX con varias pantallas. Antes de implementarla se preparó un boceto ASCII en `documentos/BOCETO_JAVAFX.md`, que definía zonas funcionales: matriz de cueva, estado del jugador, inventario, acciones y log.

La versión final incluye:

- menú principal;
- pantalla de introducción;
- transiciones entre cuevas;
- pantalla de juego;
- ayuda integrada;
- pausa;
- guardado/carga;
- pantalla final;
- ranking;
- música, sonidos, iconos y animaciones.

La pantalla de juego muestra el mapa como un grid visual, con sprites, colores de terreno, niebla y overlays. A la derecha aparecen estado, inventario, equipo y acciones. El log inferior ayuda a explicar al jugador qué ha ocurrido tras cada acción.

Los controles combinan teclado y ratón. El jugador puede moverse con WASD o flechas, atacar con espacio, usar ataque direccional, recoger o abrir cofres con `R`, pasar turno con `T`, pausar con `P` o `ESC`, consultar ayuda con `H` y lanzar hechizos con combinaciones de tecla y dirección.

Uno de los aprendizajes del frontend fue que la interfaz no es solo decoración. Los iconos, colores, mensajes y feedback afectan directamente a la comprensión del juego. Por eso se sustituyeron placeholders confusos, se añadieron iconos específicos y se reforzaron mensajes de estado.

## 6. Persistencia JSON

El proyecto trabaja con tres tipos de JSON:

- configuración inicial;
- guardado de partida;
- ranking.

`datos/cuevas.json` describe la mazmorra base: cuevas, dimensiones, mapas, enemigos, objetos y conexiones. Esto permite modificar el contenido del juego sin reescribir código.

El guardado de partida conserva el estado de una ejecución concreta: jugador, inventario, equipo, mazmorra, cueva actual, enemigos vivos, objetos en suelo, puertas, turnos restantes, estado de partida y estadísticas. Al cargar, se reconstruye una `Partida` funcional a partir de DTOs.

El ranking se guarda en `ranking.json`. Cada entrada contiene nombre del jugador, resultado, estadísticas, puntuación y título obtenido. El ranking local muestra las mejores partidas ordenadas por puntuación.

Se usó Gson porque permite mapear DTOs sencillos a JSON con poco código adicional. Aun así, la validación no se delega solo en Gson: los tests comprueban que el JSON real tenga posiciones válidas, objetos accesibles y datos coherentes.

## 7. Pruebas

El proyecto incluye una batería de pruebas JUnit para las partes no visuales. La suite final contiene **223 tests** ejecutados correctamente.

Las pruebas cubren:

- estructuras propias;
- matriz y BFS;
- personajes;
- objetos;
- lógica de partida;
- mazmorra y puertas;
- victoria y derrota;
- JSON de configuración;
- guardado/carga;
- ranking;
- estadísticas;
- algunos recursos visuales verificables sin abrir ventana completa.

La estrategia de pruebas fue incremental. Primero se probaron estructuras y mapa; después personajes, objetos y partida; más tarde JSON, guardado, ranking, tesoros, iconos y hechizos. Cada vez que una revisión detectaba un riesgo o se añadía una regla importante, se intentaba añadir un test asociado.

La principal limitación está en JavaFX. Algunas comprobaciones visuales, como layout, capas, animaciones o percepción de la niebla, requieren validación manual. Por eso la suite automática se centra en reglas deterministas y datos.

## 8. Uso de IA y metodología

El proyecto usó agentes IA como apoyo controlado, no como sustitución del equipo humano. La metodología completa se explica con más detalle en la memoria ampliada y en el diario de IA completo.

El trabajo se organizó por áreas:

- Parte A: estructuras, mapa y grafo.
- Parte B: lógica de juego.
- Parte C: JavaFX, JSON y documentación.
- Revisor independiente: revisión cruzada de cambios importantes.

Los agentes ayudaron a planificar, implementar, revisar, documentar y detectar riesgos. Para evitar pérdida de control se definieron reglas: leer documentación al inicio, no tocar áreas ajenas sin permiso, registrar decisiones, actualizar tareas, ejecutar tests y aceptar los cambios solo tras validación humana.

El diario de IA registra prompts, objetivos, resultados, cambios aceptados, cambios rechazados y crítica. Esto hace que el uso de IA sea trazable y defendible.

## 9. Crítica y conclusiones

El balance del proyecto es positivo. Se consiguió una aplicación jugable y documentada que combina estructuras propias, grafo, matriz, JSON, JavaFX, tests y reflexión sobre el proceso.

Los principales aciertos fueron la separación por capas, la documentación viva, la revisión independiente, la estrategia incremental y la existencia de una suite amplia de tests. También fue positivo que los problemas encontrados no se ocultaran: se registraron y se corrigieron cuando fue posible.

Los principales puntos mejorables son la concentración de responsabilidad en `Partida`, la dependencia del entorno local para JavaFX, la dificultad de automatizar pruebas visuales y algunos cambios de alcance que obligaron a ajustar documentación.

Como ampliaciones futuras se podrían añadir más cuevas, más tipos de enemigos, objetos especiales, generación procedural de mapas, pruebas UI automatizadas y una arquitectura interna más dividida para la lógica de partida.

En conclusión, **Escape de la Mazmorra** no solo es un juego ejecutable, sino también un proyecto defendible. Las decisiones técnicas están justificadas, las restricciones académicas se respetan, la evolución queda documentada y los resultados se validan mediante pruebas.

