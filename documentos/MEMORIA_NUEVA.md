---
title: Escape de la Mazmorra
subtitle: Memoria del Proyecto
authors:
  - Alvaro Martinez del Campo
  - Guillermo Salgado Malcuori
  - Hector Montero Plaza
subjects:
  - Metodologia de la Programacion
  - Estructuras de Datos
degree: Matematicas y Computacion
institution: Universidad de Alcala, UAH
date: 27 de mayo de 2026
---

# Escape de la Mazmorra

**Memoria del Proyecto**

**Autores:** Alvaro Martinez del Campo, Guillermo Salgado Malcuori y Hector Montero Plaza

**Asignaturas:** Metodologia de la Programacion y Estructuras de Datos

**Grado:** Matematicas y Computacion

**Universidad:** Universidad de Alcala, UAH

**Fecha:** 27 de mayo de 2026

\newpage

# 2. Resumen Ejecutivo

`Escape de la Mazmorra` es un juego de aventura por turnos desarrollado en Java. El jugador controla a un mago que debe avanzar por tres cuevas conectadas, recoger objetos, abrir puertas, combatir enemigos y derrotar al boss final antes de escapar por la salida. La entrega combina modelo de dominio, estructuras de datos propias, persistencia JSON, interfaz JavaFX, pruebas JUnit y documentacion de gestion.

El diseno separa de forma explicita modelo, vista, control y persistencia. El nucleo de reglas vive en `modelo.juego.Partida`; la estructura global del mundo se representa mediante `Mazmorra`, que contiene un `Grafo<Cueva>` propio; y cada cueva usa una matriz propia basada en `ListaSE<ListaSE<Celda>>`. Esta decision evita colecciones prohibidas de `java.util` en las estructuras evaluadas y permite justificar tanto la representacion matricial de cada sala como el uso de BFS para movimiento y caminos minimos.

La aplicacion carga la configuracion inicial desde `datos/cuevas.json`, donde se describen cuevas, matrices, enemigos, objetos y conexiones. El guardado de partida y el ranking se serializan con Gson mediante DTOs especificos. La interfaz JavaFX ofrece menu inicial, introduccion narrativa, transiciones, pantalla de juego, ayuda, pausa, inventario, log, sonidos, musica, niebla de guerra, animaciones y pantalla final con puntuacion.

La validacion se apoya en una suite de 223 tests JUnit distribuidos entre estructuras, mapa, juego, personajes, objetos, JSON, ranking y recursos visuales o sonoros. Ademas, el proyecto mantiene una carpeta de `project-management` con decisiones, tareas, checklist, post mortem y diario de IA, lo que permite defender una metodologia human-in-the-loop con trazabilidad.

# 3. Introduccion

## 3.1. Objetivo Del Proyecto

El objetivo del proyecto es construir una aplicacion jugable que integre los contenidos de programacion orientada a objetos y estructuras de datos. La entrega no se limita a una demostracion visual, sino que incorpora reglas de juego, persistencia, algoritmos de recorrido, pruebas automaticas y documentacion academica.

Desde el punto de vista de Estructuras de Datos, el proyecto demuestra el uso de listas enlazadas, cola y grafo implementados por el propio grupo. Desde el punto de vista de Metodologia de la Programacion, muestra separacion de responsabilidades, encapsulamiento, pruebas, documentacion de decisiones y gestion iterativa de alcance.

## 3.2. Descripcion Del Juego

El jugador interpreta a un mago que atraviesa una mazmorra formada por tres cuevas de dificultad progresiva: `Las Criptas de Marfil`, `El Paramo Putrefacto` y `El Abismo de Malakor`. Cada cueva tiene una matriz de celdas con muros, suelo, inicio, puertas, tesoros, rocas, arbustos y salida. Sobre esa matriz aparecen enemigos, objetos y elementos interactivos.

La partida avanza por turnos. En cada turno el jugador puede mover y realizar una accion, como atacar, recoger, usar pocion, abrir tesoro, lanzar hechizos o interactuar con una puerta. Los enemigos responden al pasar turno: si estan cerca atacan, si no pueden acercarse mediante camino minimo, y algunos arqueros disparan a distancia si hay linea de tiro. La victoria exige derrotar a Malakor, obtener la llave final y llegar a una celda de salida.

## 3.3. Alcance De La Entrega

La entrega incluye una aplicacion JavaFX funcional, configuracion inicial desde JSON, guardado y carga de partida, ranking local, pantallas narrativas, efectos visuales y sonoros, UML, JSON de ejemplo, diario de IA y pruebas JUnit. Tambien incluye esta memoria generada de cero a partir del codigo fuente, tests, JSON, diagramas y documentacion auxiliar del proyecto.

Quedan fuera del alcance algunas mejoras futuras registradas en los documentos de gestion: mas tipos de enemigo, IA avanzada, lineas de vision mas sofisticadas, sistema completo de cofres con inventario variable, arboles u otras estructuras no implementadas y una herramienta de build independiente como Maven o Gradle.

## 3.4. Tecnologias Utilizadas

| Area | Tecnologia | Uso |
|---|---|---|
| Lenguaje | Java | Modelo, estructuras, reglas y control |
| Interfaz | JavaFX | Pantallas, grid, animaciones, audio y eventos |
| Persistencia | Gson | Lectura y escritura de JSON |
| Pruebas | JUnit 5 | Tests unitarios y de regresion |
| Diagramas | PlantUML y PNG | UML de clases, componentes, actividad, estados y secuencia |
| Documentacion | Markdown | Project-management, diario IA y memoria |
| Generacion | Python y ReportLab | Renderizado del PDF final |

# 4. Arquitectura General

## 4.1. Separacion Por Paquetes

El codigo fuente se organiza en paquetes con responsabilidades diferenciadas. `src/Estructuras` contiene las implementaciones propias de listas, cola, grafo, nodos e iteradores. `src/modelo/mapa` define la cueva como matriz de celdas y los algoritmos internos de recorrido. `src/modelo/juego` contiene la fachada de partida, mazmorra, puertas, vistas de estado, resultados de hechizos y estadisticas. `src/modelo/personajes` modela jugador, enemigos y boss. `src/modelo/objetos` agrupa objetos consumibles, equipables y llaves. `src/json` se encarga de DTOs, carga, guardado y ranking. `src/control` coordina flujo y eventos. `src/vista` construye las pantallas JavaFX.

Esta separacion responde a una decision explicita documentada en `ARCHITECTURE.md`: JavaFX no debe modificar estructuras internas del modelo, y JSON no debe contener reglas de juego. La vista consulta y envia acciones de alto nivel; el modelo decide si esas acciones son validas.

## 4.2. Modelo, Vista, Control Y JSON

El proyecto sigue una arquitectura cercana a MVC. El modelo mantiene estado y reglas. La vista muestra la matriz, paneles, inventario, ayuda, pausa, animaciones y feedback visual. El control traduce eventos de teclado o raton en llamadas sobre `Partida`. La capa JSON actua como adaptador entre ficheros externos y objetos del dominio.

`EscapeMazmorraApp` inicializa la aplicacion, crea pantallas y gestiona el menu. `ControladorFlujo` centraliza la navegacion entre introduccion, transiciones, partida y final. `JuegoController` recibe teclas y clics, activa movimientos, ataques direccionales y hechizos. `PantallaJuego` se ocupa de pintar y actualizar el estado visible, pero no decide reglas como danio, llaves o victoria.

![Figura 1. Diagrama de componentes MVC](documentos/ENTREGA/diagramas uml/componentes/diagrama_componentes_mvc.png)

## 4.3. Flujo Principal De La Aplicacion

La ejecucion comienza en `Main.java`, que delega en `EscapeMazmorraApp.main`. El menu permite iniciar partida nueva, cargar partida, consultar controles, ver ranking o salir. En partida nueva se pide el nombre del jugador y se llama a `Partida.crearPartidaNueva(nombre)`, que carga `datos/cuevas.json` con `CargadorConfiguracion` y construye la partida mediante `FabricaPartida`.

Despues se muestra una introduccion narrativa, una pantalla de transicion de cueva y la pantalla de juego. Cuando el jugador cambia de cueva, `PantallaJuego` avisa mediante callback y el flujo muestra de nuevo una transicion. Cuando el estado deja de ser `EN_CURSO`, se muestra `PantallaFinal`, se calculan estadisticas y puede registrarse el resultado en el ranking.

## 4.4. Relacion Entre JavaFX Y Modelo

JavaFX trabaja con vistas simples del modelo: `CuevaEnMapa`, `CeldaEnMapa`, `PersonajeEnMapa` y listas copiadas. La interfaz no necesita acceder directamente al grafo ni a la matriz interna. Para operar llama a metodos de alto nivel como `moverJugador`, `atacar`, `pasarTurno`, `recogerObjeto`, `usarObjeto`, `equiparObjeto`, `abrirTesoro`, `cambiarCueva`, `registrarDisparoBolaFuego` o `guardar`.

Este contrato reduce el riesgo de que la interfaz salte reglas de partida. Por ejemplo, la vista puede mostrar una celda alcanzable, pero la decision final de mover sigue en `Partida`. Del mismo modo, una animacion de bola de fuego se pinta en JavaFX, pero el impacto real se resuelve en el modelo mediante `impactarBolaFuego`.

# 5. Diseno Orientado A Objetos

## 5.1. Clases Principales

El diseno orientado a objetos se organiza alrededor de entidades del dominio. `Partida` coordina reglas y estado global. `Mazmorra` guarda el grafo de cuevas. `Cueva` representa una sala con matriz propia de celdas. `Jugador`, `Enemigo` y `Boss` representan personajes. `Objeto`, `Arma`, `Llave`, `Pocion`, `Escudo`, `Espada` y `Arco` representan elementos del inventario o del suelo.

![Figura 2. Diagrama UML de clases principal](documentos/ENTREGA/diagramas uml/clases/diagrama_clases.png)

## 5.2. Encapsulamiento

El encapsulamiento aparece en varios niveles. `Personaje` no expone setters generales para vida maxima, ataque o defensa; la vida cambia mediante `recibirDano` y `curar`, y la posicion mediante `cambiarPosicion`. `Jugador` gestiona su inventario con `ListaDE<Objeto>` y ofrece operaciones controladas para agregar, quitar, equipar o usar objetos. `Cueva` devuelve copias de la estructura de listas para proteger la forma de la matriz.

`Partida` expone metodos de accion y vistas de consulta, no sus estructuras internas completas. Aunque existen algunos metodos de apoyo para integracion y tests, el contrato principal evita que la vista manipule directamente enemigos, puertas o contenido por cueva. Esta decision fue reforzada tras revisiones independientes.

## 5.3. Herencia Y Polimorfismo

`Personaje` es una clase abstracta con atributos comunes de vida, ataque, defensa, movimiento y posicion. `Jugador` y `Enemigo` heredan de ella, y `Boss` hereda de `Enemigo`. Esto permite tratar al boss como enemigo en combate, pero reconocerlo con `instanceof Boss` cuando su derrota debe entregar la llave final y registrar estadisticas especiales.

En objetos, `Objeto` es la base comun. `Arma` hereda de `Objeto` y especializa el comportamiento equipable con bonificacion de ataque. `Espada` y `Arco` heredan de `Arma`. `Escudo` tambien es equipable, pero no arma. `Pocion` es consumible y aplica curacion. `Llave` no es equipable ni consumible: conserva un tipo de cerradura y un codigo que debe coincidir con puertas o cofres.

## 5.4. Composicion

La composicion es central. `Mazmorra` contiene un `Grafo<Cueva>`, pero no hereda de grafo. `Cueva` contiene una matriz propia de `Celda`, pero no hereda de matriz ni de grafo. `Partida` contiene `Mazmorra`, `Jugador`, listas de contenidos por cueva, puertas, log, disparos pendientes y `EstadisticasPartida`. `Jugador` contiene inventario, arma equipada y escudo equipado.

Esta preferencia por composicion evita confundir conceptos de dominio con estructuras auxiliares. Una mazmorra no es un grafo en terminos del juego; usa un grafo para resolver conexiones. Una cueva no es una lista; usa listas para almacenar celdas.

## 5.5. Responsabilidades De Dominio

| Clase | Responsabilidad principal |
|---|---|
| `Partida` | Coordinar turnos, movimiento, combate, objetos, puertas, victoria, derrota, log, guardado y estadisticas |
| `Mazmorra` | Mantener cuevas, conexiones dirigidas, cueva actual y caminos entre cuevas |
| `Cueva` | Mantener matriz de celdas, transitabilidad, celdas alcanzables y caminos minimos internos |
| `Jugador` | Gestionar inventario, equipo, vida, ataque total y defensa total |
| `Enemigo` | Representar enemigos con tipo, vida, posicion y congelacion |
| `Boss` | Especializar enemigo final de tipo `BOSS` |
| `Objeto` | Base comun con id, nombre, descripcion e igualdad por id |
| `Arma` | Objeto equipable con bonificacion de ataque |
| `Llave` | Objeto con tipo de cerradura y codigo de apertura |
| `Pocion` | Objeto consumible que cura al jugador |
| `Escudo` | Objeto equipable con bonificacion de defensa |

# 6. Estructuras De Datos Propias

## 6.1. Restriccion Academica

El proyecto evita usar colecciones prohibidas de `java.util.*` como sustituto de las estructuras evaluadas. Las estructuras principales viven en `src/Estructuras` y son usadas de forma real en el juego. En zonas auxiliares se permiten APIs de entrada y salida, Gson, JavaFX y arrays DTO cuando la persistencia JSON lo requiere, pero la matriz de juego, el inventario, la cola de BFS y el grafo de cuevas se apoyan en estructuras propias.

![Figura 3. Diagrama UML de estructuras propias](documentos/ENTREGA/diagramas uml/clases/estructuras.png)

## 6.2. ListaSE

`ListaSE<T>` es una lista simplemente enlazada con referencia al primer nodo y contador de tamanio. Permite insertar al inicio en coste constante e insertar al final con recorrido lineal. Usa `equals` para comparar, no exige `Comparable`, lo que permite almacenar `Celda`, `Cueva`, `Posicion` u objetos del dominio sin fingir un orden natural.

Su uso mas importante es la matriz de `Cueva`, representada como `ListaSE<ListaSE<Celda>>`. Tambien aparece en el grafo, en logs, caminos, celdas alcanzables, contenidos por cueva, listas de puertas y resultados de algoritmos. La limitacion principal es que el acceso por indice y la busqueda son lineales.

## 6.3. ListaDE

`ListaDE<T>` es una lista doblemente enlazada con referencias a primer y ultimo nodo. Permite insertar al final en coste constante y borrar nodos enlazando anterior y siguiente. Se usa en el inventario del jugador y en listas de objetos o enemigos actuales donde interesa recorrer, insertar y eliminar.

El inventario se beneficia de `ListaDE<Objeto>` porque permite mantener objetos de tipos distintos bajo la interfaz comun `Objeto`, y retirar una pocion consumida o un item concreto por igualdad de id. Su acceso por posicion sigue siendo lineal, ya que el codigo recorre desde el principio.

## 6.4. Cola

`Cola<T>` implementa una politica FIFO con nodos enlazados y referencias a frente y final. Se usa en BFS, tanto dentro de una cueva como en el grafo de cuevas. `offer` y `poll` son operaciones de coste constante, lo que encaja con la exploracion por niveles.

La cola no tiene iterador ni copia porque su uso principal es operacional: almacenar la frontera pendiente de un algoritmo. Si esta vacia, `poll` devuelve `null`, decision simple y suficiente para los bucles actuales.

## 6.5. Grafo

`Grafo<T>` mantiene nodos y arcos dirigidos usando `ListaSE<NodoGrafo<T>>` y `ListaSE<ArcoGrafo<T>>`. Permite anadir nodos, crear arcos etiquetados, comprobar conexiones, obtener adyacentes, recorrer con BFS, comprobar caminos y calcular camino minimo con pesos unitarios.

En el juego se instancia como `Grafo<Cueva>` dentro de `Mazmorra`. Las conexiones son dirigidas: de cueva facil a cueva media, y de cueva media a cueva dificil. La estructura no usa `HashMap` ni `HashSet`; por eso las busquedas de nodos, arcos y visitados son lineales. Para tres cuevas, este coste es adecuado.

## 6.6. Iteradores Y Nodos Auxiliares

`ElementoSE` y `ElementoDE` son los nodos internos de listas. `IteradorSE`, `IteradorDE` y `MiIterador` permiten recorrer sin exponer directamente enlaces internos. `NodoGrafo` almacena id, dato y arcos de salida. `ArcoGrafo` representa origen, destino y etiqueta.

Estas clases auxiliares son pequeñas, pero importantes para defender la implementacion propia. Permiten que estructuras y modelo no dependan de colecciones externas.

## 6.7. Costes Aproximados Y Limitaciones

| Estructura | Operaciones favorables | Limitaciones |
|---|---|---|
| `ListaSE` | Insercion al inicio O(1), copia y recorrido simples | Acceso por indice O(n), busqueda O(n), insercion final O(n) |
| `ListaDE` | Insercion final O(1), borrado con enlaces dobles | Busqueda y acceso por indice O(n) |
| `Cola` | `offer` y `poll` O(1) | Sin busqueda, copia ni iterador |
| `Grafo` | Modelo claro de conexiones y BFS | Busquedas lineales, coste mayor que con mapas hash |

La decision es coherente con el tamano del proyecto. Los mapas tienen 15x15, 19x19 y 23x23 celdas, y la mazmorra tiene tres cuevas. En este contexto, la claridad academica y el cumplimiento de restricciones pesan mas que la optimizacion extrema.

# 7. Mapa, Mazmorra Y Algoritmos

## 7.1. Cueva Como Matriz Propia

`Cueva` almacena cada sala como una matriz propia de listas enlazadas. Cada celda tiene fila, columna y `TipoCelda`. Los tipos relevantes son `SUELO`, `MURO`, `INICIO`, `PUERTA`, `TESORO`, `SALIDA`, `TRAMPA`, `ROCA` y `ARBUSTO`. La transitabilidad depende del tipo: muros y obstaculos no se pisan, los tesoros se tratan con reglas especificas para no romper BFS ni movimiento.

El acceso a una celda recorre la lista de filas y despues la fila concreta. Aunque no es tan rapido como un array, cumple la restriccion de no representar la matriz evaluable con `Celda[][]`.

## 7.2. Mazmorra Como Grafo De Cuevas

`Mazmorra` contiene un grafo dirigido de cuevas. Las cuevas se identifican por id estable, y las conexiones representan puertas o transiciones posibles. El JSON real define tres cuevas y dos conexiones: `cueva_facil` a `cueva_media`, y `cueva_media` a `cueva_dificil`.

La clase `Puerta` no sustituye al arco del grafo. El arco indica que existe conexion estructural; la puerta anade una regla jugable, como exigir una llave con un codigo concreto. Asi se separan topologia y requisitos.

## 7.3. BFS Para Celdas Alcanzables

`Cueva.getCeldasAlcanzables` usa BFS desde una posicion inicial y con un maximo de pasos. La cola propia mantiene la frontera y una lista de posiciones visitadas evita repetir celdas. Los vecinos se calculan de forma implicita en cuatro direcciones: arriba, abajo, izquierda y derecha. No se permiten diagonales para movimiento.

BFS es adecuado porque cada paso entre celdas transitables cuesta uno. En esas condiciones, explorar por niveles garantiza que una celda encontrada a distancia `d` se alcanza con el minimo numero de movimientos.

## 7.4. Camino Minimo Dentro De Una Cueva

`Cueva.getCaminoMinimo` tambien usa BFS, pero guarda relaciones de padre para reconstruir el camino desde destino hasta origen. Devuelve una `ListaSE<Celda>` con la celda inicial y la celda destino incluidas. Si no existe camino, devuelve una lista vacia y la distancia minima es `-1`.

`Partida` reutiliza este calculo para movimiento de enemigos y para ayudas como distancia a puerta o vision comprada del camino. Tambien filtra casos de ocupacion o tesoros cerrados cuando el movimiento real requiere reglas adicionales.

## 7.5. Camino Entre Cuevas

`Grafo.caminoMinimo` calcula caminos entre cuevas con BFS sobre adyacentes. `Mazmorra` lo expone para saber si existe camino, obtener distancia minima o presentar la secuencia de cuevas hacia la salida. Como solo hay tres cuevas, el coste es muy bajo.

## 7.6. Comparacion Con Dijkstra

Dijkstra seria necesario si las aristas tuvieran pesos distintos, por ejemplo coste variable por terreno, peligro o energia. En este proyecto los movimientos de celda y las conexiones entre cuevas tienen coste unitario. Por eso BFS es mas simple, suficiente y defendible. Ademas, BFS se apoya directamente en la `Cola` propia, lo que refuerza el objetivo academico.

![Figura 4. Diagrama de actividad del turno](documentos/ENTREGA/diagramas uml/actividad/diagrama_actividad_turno.png)

# 8. Reglas Del Juego

## 8.1. Turnos Y Movimiento

La partida empieza en estado `EN_CURSO` con 60 turnos por cueva. En cada turno el jugador puede realizar un movimiento cardinal a una celda adyacente transitable y no ocupada por enemigo. El movimiento no permite atravesar muros, rocas, arbustos ni tesoros cerrados. La bandera `movimientoRealizado` evita mover dos veces antes de pasar turno.

Mover no termina automaticamente el turno. Esta decision permite combinar movimiento y accion en el mismo turno, y deja al usuario decidir cuando pulsa pasar turno.

## 8.2. Acciones

La accion del turno se controla con `accionRealizada`. Atacar, recoger objeto, usar pocion o lanzar hechizo consume accion. Equipar arma o escudo no consume accion, segun la regla documentada en las decisiones de proyecto. Abrir tesoro o interactuar con elementos cercanos se valida en `Partida`.

## 8.3. Combate

El jugador puede atacar enemigos adyacentes, incluyendo diagonales, y si tiene arco equipado puede atacar a distancia dentro de alcance. El ataque direccional permite elegir objetivo mediante clic o combinaciones de teclado con `Shift`. La formula base aplica danio igual a ataque menos defensa, con minimo 1 si el ataque es valido.

Los hechizos anaden mecanicas especiales. La bola de fuego consume accion, viaja en linea recta con rango definido y causa danio fijo al impactar. La bola de hielo congela al enemigo durante tres turnos, impidiendo su actuacion mientras dure el efecto.

## 8.4. Enemigos Y Boss Final

Los enemigos normales tienen tipo, vida, ataque, defensa, movimiento y posicion. Si estan adyacentes al jugador, atacan al pasar turno. Si no, buscan acercarse mediante camino minimo. Los arqueros pueden disparar a distancia si hay linea de tiro y no hay obstaculos bloqueantes.

`Boss` representa a Malakor como enemigo especial. Al derrotarlo, `Partida` registra la muerte del boss, marca a Malakor como derrotado en estadisticas y entrega la llave final. La victoria no se activa solo por matar al boss: tambien se necesita llegar a la salida con la llave final.

## 8.5. Objetos, Inventario, Puertas Y Llaves

Los objetos en suelo se modelan como `ObjetoEnMapa`, que combina objeto, cueva y posicion. Los objetos en inventario no tienen posicion. El jugador puede recoger objetos cercanos o en la misma celda segun las reglas de partida. Las pociones curan y se consumen. Las armas aumentan ataque. El escudo aumenta defensa. El arco ocupa las dos manos, por lo que desequipa escudo.

Las puertas conectan cuevas y pueden requerir una llave de tipo `PUERTA` con codigo coincidente. Una vez abierta, una puerta puede atravesarse sin volver a exigir llave. Las llaves normales permiten avanzar entre cuevas; la llave final se obtiene al derrotar al boss y habilita la salida final.

## 8.6. Victoria Y Derrota

La victoria se produce cuando el jugador tiene la llave final y pisa una celda `SALIDA`. La derrota se produce si la vida llega a cero o si se agotan los turnos. La pantalla final muestra resultado, estadisticas, puntuacion y titulo del jugador.

![Figura 5. Diagrama de estados de partida](documentos/ENTREGA/diagramas uml/estados/diagrama_estados.png)

# 9. Persistencia JSON

## 9.1. Configuracion Inicial

El fichero `datos/cuevas.json` contiene la configuracion inicial real. Define el nombre del juego, tres cuevas, matrices de tipos de celda, enemigos, objetos y conexiones. Las dimensiones actuales son 15x15 para `cueva_facil`, 19x19 para `cueva_media` y 23x23 para `cueva_dificil`. La cueva facil contiene tres enemigos y tres objetos; la media cinco enemigos y tres objetos; la dificil siete enemigos y tres objetos.

## 9.2. DTOs Y CargadorConfiguracion

La capa `src/json` usa DTOs para no mezclar el formato JSON con clases del modelo. `ConfiguracionMazmorra`, `ConfiguracionCuevaDTO`, `ConfiguracionEnemigoDTO`, `ConfiguracionObjetoDTO` y `ConexionDTO` representan la entrada. `CargadorConfiguracion` lee con Gson, crea las cuevas, asigna tipos de celda, acumula enemigos y objetos y crea conexiones en la mazmorra.

Si el JSON no contiene cuevas, es nulo o tiene conexiones a cuevas inexistentes, el cargador lanza errores. Para tipos de celda desconocidos usa `SUELO` como valor seguro, registrando una advertencia.

## 9.3. FabricaPartida

`FabricaPartida` recibe `ResultadoCarga` y construye una `Partida` completa. Localiza la cueva inicial, crea el jugador base, traduce conexiones a puertas con codigos de llave, crea enemigos segun tipo y coloca objetos segun configuracion. Si un enemigo, objeto o llave tiene datos invalidos, la fabrica lanza `IllegalArgumentException`.

Esta clase actua como puente entre JSON y modelo, dejando a `CargadorConfiguracion` centrado en datos y a `Partida` centrada en reglas.

## 9.4. Guardado, Carga Y Ranking

`SerializadorPartida` convierte partida, mazmorra, jugador, enemigos, objetos y puertas en DTOs de guardado y escribe JSON con Gson pretty printing. La carga reconstruye `Mazmorra`, `Jugador`, enemigos, objetos, puertas, estado y turnos. `Partida.guardar` delega en esta capa y `Partida.cargarPartida` reconstruye desde fichero.

`SerializadorRanking` mantiene resultados finales en un array JSON simple. Ordena por puntuacion usando estructuras propias y ofrece un Top 10. Los ejemplos de entrega se encuentran en `documentos/ENTREGA/json_ejemplo/`.

![Figura 6. Secuencia de guardado y carga JSON](documentos/ENTREGA/diagramas uml/secuencia/diagrama_secuencia_guardado_carga_json.png)

## 9.5. Robustez Ante JSON Invalido

Los tests de JSON cubren carga correcta, dimensiones, matrices, conexiones, enemigos, objetos, posiciones transitables, tesoros accesibles, fichero inexistente y conexion invalida. Tambien hay tests de round-trip para guardado y carga de partida, incluyendo inventario, equipo, enemigos vivos, objetos en suelo, puertas, estado, turnos y ranking.

# 10. Gestion De Excepciones Y Robustez

## 10.1. Validaciones

Las clases del dominio validan datos obligatorios y rangos. `Cueva` rechaza ids vacios y dimensiones no positivas. `Personaje` rechaza vida no positiva, posiciones negativas y texto obligatorio vacio. `Objeto` valida id, nombre y descripcion. `Llave` valida tipo y codigo de cerradura. `Partida` valida mazmorra, jugador, turnos, estado de juego y acciones permitidas.

## 10.2. Try-Catch Y Errores De Carga

La entrada y salida de ficheros usa `IOException`. La carga JSON puede lanzar errores de sintaxis o de dominio. El menu de JavaFX captura errores al cargar partida y muestra feedback al usuario. La aplicacion tambien tiene logging de errores para fallos de pantalla o recursos.

## 10.3. Errores De Guardado, Assets Y Audio

El guardado puede fallar por ruta, permisos o datos invalidos. En interfaz se informa mediante feedback visual y log. Los recursos graficos y de audio se cargan desde `datos/`; si falta un asset, la vista intenta continuar con representaciones alternativas o registra el problema. Los reproductores de musica y SFX encapsulan JavaFX media para que el resto de la vista no dependa de detalles de audio.

## 10.4. Excepciones De Dominio

El proyecto usa `IllegalArgumentException` para estados de dominio invalidos detectados pronto. En acciones normales de partida se prefiere devolver `false` cuando la accion no es valida: movimiento imposible, ataque fuera de alcance, objeto inexistente, accion ya usada o puerta no disponible. Esta mezcla permite separar errores de programacion o datos corruptos de decisiones normales del jugador.

# 11. Interfaz JavaFX

## 11.1. Pantalla Inicial

`EscapeMazmorraApp` crea una ventana de 1280x720 con estetica de mazmorra. El menu permite iniciar partida, cargar, consultar controles, ver ranking y salir. La pantalla incluye fondo, botones estilizados, assets del Dungeon Asset Pack y dialogo propio para introducir el nombre del jugador.

![Figura 7. Boceto inicial del menu](documentos/capturas/boceto_inicial_menu.png)

## 11.2. Pantallas Narrativas

`PantallaIntroduccion` presenta la historia inicial antes de entrar en la primera cueva. `PantallaTransicion` se reutiliza antes de cada cueva y toma textos y colores desde `DatosTemaCueva`. `PantallaFinal` muestra victoria o derrota, estadisticas, puntuacion, titulo y regreso al menu.

## 11.3. Pantalla De Juego

`PantallaJuego` dibuja la matriz, sprites de jugador y enemigos, objetos, puertas, salida, barras de vida, panel de estadisticas, inventario, acciones y log. Tambien gestiona feedback visual, ayuda, pausa, niebla de guerra, animaciones de movimiento, ataque, muerte y proyectiles.

La pantalla no decide reglas. Cuando el usuario pulsa teclas o celdas, `JuegoController` traduce el evento a llamadas de `Partida`. Despues la vista refresca el grid a partir del estado devuelto.

## 11.4. Inventario, Acciones Y Controles

El inventario visual muestra objetos y equipo. Las acciones disponibles incluyen movimiento, ataque, recoger, usar pocion, equipar, abrir tesoro, pasar turno, guardar, ayuda y pausa. El teclado admite WASD o flechas para movimiento, combinaciones con `Shift` para ataque direccional, `F` para bola de fuego y controles de ayuda o pausa.

## 11.5. Niebla De Guerra, Feedback Visual, Sonidos Y Musica

La niebla de guerra reduce la visibilidad fuera del radio del jugador. Los ataques y danos generan flashes y animaciones. La musica de fondo se gestiona con `ReproductorMusica`, y los efectos de interaccion con `ReproductorSfx`. Los tests visuales comprueban que los iconos esperados existen y son PNG validos, y los tests de audio validan cabeceras WAV.

# 12. Pruebas

## 12.1. Vision General

La suite contiene 223 tests JUnit distribuidos en 18 clases. Cubren estructuras propias, grafo, cola, matriz de cueva, caminos, personajes, inventario, objetos, partida, mazmorra, fabrica, estadisticas, JSON, serializacion, ranking, iconos y SFX.

| Area | Clases de test | Numero aproximado |
|---|---|---|
| Estructuras | `ColaTest`, `GrafoTest`, `ListaDETest`, `ListaSETest` | 24 |
| Mapa y mazmorra | `CuevaTest`, `MazmorraTest` | 27 |
| Juego | `PartidaTest`, `FabricaPartidaTest`, `EstadisticasPartidaTest` | 87 |
| Personajes y objetos | `PersonajeTest`, `JugadorInventarioTest`, `EnemigoTest`, `ObjetoTest` | 45 |
| JSON y ranking | `CargadorConfiguracionTest`, `SerializadorPartidaTest`, `SerializadorRankingTest` | 38 |
| Vista y recursos | `IconosVisualesTest`, `ReproductorSfxTest` | 2 |

## 12.2. Tests De Estructuras

Los tests de `ListaSE` y `ListaDE` comprueban insercion, borrado, copia, busqueda por igualdad, acceso por indice e iteracion. `ColaTest` valida FIFO, `peek`, `poll`, vaciado y tamanio. `GrafoTest` verifica nodos sin orden natural, arcos dirigidos, duplicados, BFS, camino minimo, distancia y objetos sin `Comparable`.

## 12.3. Tests De Modelo Y Mapa

`CuevaTest` cubre creacion de matriz propia, limites, transitabilidad, igualdad por id, BFS de alcanzables, camino minimo y destinos inaccesibles. `MazmorraTest` cubre cueva actual, conexiones dirigidas, camino entre cuevas y distancia minima.

`PartidaTest` es la clase mas extensa. Valida movimiento, turnos, ocupacion, recogida, equipamiento, puertas, llaves, victoria, derrota, enemigos, arco, ataque direccional, bola de fuego, bola de hielo, arqueros, tesoros, vision de camino, guardado y carga. `FabricaPartidaTest` asegura que el JSON se convierte en partida jugable y que los errores de configuracion se detectan.

## 12.4. Tests De JSON, Ranking Y Recursos

`CargadorConfiguracionTest` comprueba que las tres cuevas reales cargan con dimensiones correctas, conexiones correctas, enemigos y objetos asociados, y que los objetos estan en posiciones validas. `SerializadorPartidaTest` prueba conversiones DTO y persistencia. `SerializadorRankingTest` valida guardado, lectura, orden y Top 10. `IconosVisualesTest` y `ReproductorSfxTest` verifican recursos minimos de entrega.

## 12.5. Cobertura Y Limites

La suite cubre el nucleo logico y muchos casos de regresion. No sustituye por completo pruebas manuales de JavaFX, animaciones, audio real o experiencia visual. Tampoco mide rendimiento a gran escala, ya que el tamano de los mapas es limitado y la prioridad academica es claridad de estructuras propias.

# 13. Uso De IA

## 13.1. Metodologia Human-In-The-Loop

El proyecto uso IA como apoyo, no como autoridad automatica. Los agentes proponian, implementaban o revisaban, pero el equipo humano marcaba alcance, aceptaba cambios, corregia desviaciones y registraba decisiones. La carpeta `project-management` funciono como memoria compartida para que las sesiones no dependieran solo del chat.

## 13.2. Roles De Agentes

La organizacion separo areas: Parte A para estructuras y mapa, Parte B para logica, personajes y objetos, Parte C para JavaFX, JSON y documentacion, y un revisor independiente para restricciones, tests y riesgos. Esta division redujo conflictos y ayudo a que cada agente leyera solo el contexto necesario.

## 13.3. Tabla Cronologica De Sesiones Relevantes

| Fecha | Responsable | Objetivo | Resultado |
|---|---|---|---|
| 2026-05-20 | Coordinacion | Crear metodologia y documentos base | PRD, arquitectura, tareas, decisiones, checklist y diario IA |
| 2026-05-20 | Guillermo | Disenar logica inicial | Reglas de jugador, enemigos, objetos y turnos |
| 2026-05-21 | Alvaro | Optimizar workflow de agentes | Modo economico y politica de modelos |
| 2026-05-21 | Hector | Crear JSON inicial y Gson | DTOs, cargador y tests |
| 2026-05-22 | Parte B | Implementar `Partida` | Fachada de reglas, puertas, vistas y tests |
| 2026-05-22 | Integracion | Crear `FabricaPartida` | Puente JSON a partida jugable |
| 2026-05-22 | Parte C | Guardado y ayuda | Persistencia completa y overlay de controles |
| 2026-05-23 | Revision | Detectar colecciones prohibidas | Sustitucion de `HashMap` por estructura propia |
| 2026-05-24 | Parte A y B | Ataque direccional | Clic, `Shift` con teclas y tests |
| 2026-05-24 | Parte C | Ranking, estadisticas y hechizos | Puntuacion, ranking, bola de fuego y bola de hielo |

## 13.4. Prompts, Resultados Y Reajustes Humanos

Los prompts registrados pedian tareas concretas: organizar el proyecto, implementar JSON, disenar reglas, resolver conflictos, revisar restricciones, corregir UI, mejorar guardado o anadir mecanicas. Los resultados se aceptaban si cumplian pruebas y restricciones. En varios casos se rechazaron o corrigieron propuestas: `Partida` no debia conectar cuevas, la interfaz no debia exponer mutabilidad excesiva, `HashMap` no era aceptable para cache visual, y algunas versiones de menu fueron revertidas por estilo.

## 13.5. Critica Del Uso De IA

La IA acelero documentacion, implementacion y revision. Tambien detecto riesgos, ayudo a escribir tests y facilito continuidad. Sus riesgos fueron asumir decisiones no confirmadas, tocar zonas compartidas, generar soluciones comodas pero prohibidas por el enunciado, o proponer pulidos visuales que no encajaban con la estetica final. Las medidas de control fueron lectura de documentos, tareas acotadas, revision independiente, pruebas, checklist y registro de aceptaciones y rechazos.

# 14. Organizacion Del Proyecto Y Gestion

## 14.1. Project-Management

La carpeta `documentos/ENTREGA/project-management/` contiene la trazabilidad del proyecto. `ARCHITECTURE.md` fija principios de arquitectura y estructuras. `DECISIONS.md` registra decisiones de diseno. `TASKS.md` mantiene tareas, responsables y estados. `POST_MORTEM.md` recoge problemas detectados y acciones. `REVIEW_CHECKLIST.md` define controles antes de aceptar cambios. `IA_DIARY.md` registra uso de IA.

## 14.2. Ramas, PRs Y Revision

La documentacion menciona trabajo por ramas y Pull Requests, con reparto por partes. Tambien registra revisiones independientes de cambios de riesgo. El flujo recomendado exige leer documentacion actualizada al empezar, acotar archivos permitidos, ejecutar tests, registrar resultados y no mezclar responsabilidades sin permiso.

## 14.3. Checklist Y Decisiones Relevantes

El checklist incluye restricciones de estructuras, arquitectura, area de trabajo, funcionalidad, documentacion y revision independiente. Entre las decisiones mas relevantes estan: `Mazmorra` contiene `Grafo<Cueva>`, `Cueva` contiene `ListaSE<ListaSE<Celda>>`, BFS con `Cola`, inventario con `ListaDE`, puertas como regla sobre conexiones, victoria con boss derrotado y salida, estadisticas en clase dedicada y ranking con Gson.

# 15. Critica Del Proyecto

## 15.1. Mejoras Aplicadas

Durante el proyecto se aplicaron mejoras relevantes: mapas mas grandes, obstaculos, niebla de guerra, musica, animaciones, ataques direccionales, hechizos, guardado completo, ranking, pantalla final, ayuda integrada, pausa, iconos especificos y tests de regresion. Tambien se corrigio el uso de una coleccion prohibida en la cache visual.

## 15.2. Fallos Detectados Y Corregidos

Las revisiones detectaron exposicion mutable excesiva, metodos de preparacion dentro de la interfaz publica, solapes entre jugador y enemigos, avance a cueva ocupada, semantica incompleta de puerta abierta, conflictos de id de llave final, auto-avance mal activado, cambio de cueva que podia regalar acciones, tesoros que interferian con BFS y placeholders visuales confusos. Las correcciones quedaron documentadas y en muchos casos protegidas con tests.

## 15.3. Puntos Abiertos Y Limitaciones

La clase `Partida` concentra mucha responsabilidad y podria dividirse en servicios de combate, turnos, puertas o contenidos por cueva. La persistencia funciona, pero la reconstruccion desde DTOs podria evolucionar a un builder mas formal. La interfaz visual depende de pruebas manuales para algunos detalles. No hay Maven o Gradle configurado como build universal. El rendimiento de BFS y grafo es adecuado para el tamano actual, pero no esta optimizado para mapas grandes.

## 15.4. Posibles Mejoras Futuras

Como mejoras futuras se proponen enemigos con IA mas variada, lineas de vision mas precisas, cofres con contenido configurable, trampas con efectos, drops especificos, balance avanzado de turnos, modos de dificultad, refactor de `Partida`, mas tests visuales automatizados y un sistema de build reproducible para todo el equipo.

# 16. Conclusiones

`Escape de la Mazmorra` cumple el objetivo de integrar estructuras de datos propias, orientacion a objetos, algoritmos, persistencia, interfaz grafica, pruebas y documentacion. La solucion evita presentar las estructuras como ejercicios aislados: `ListaSE`, `ListaDE`, `Cola` y `Grafo` forman parte real del funcionamiento del juego.

La arquitectura separa razonablemente dominio, vista, control y JSON. `Partida` actua como fachada de reglas, `Mazmorra` mantiene la topologia de cuevas, `Cueva` resuelve caminos internos y JavaFX se limita a visualizar y enviar acciones. BFS esta justificado por costes unitarios y por el uso natural de la cola propia.

La metodologia de gestion y uso de IA aporta trazabilidad. Las decisiones, tareas, revisiones y criticas permiten explicar no solo el resultado, sino tambien el proceso. La entrega final es defendible porque combina funcionalidad, pruebas y reflexion sobre limitaciones.

# 17. Anexos

## 17.1. Referencia A Diagramas UML

Los diagramas UML se encuentran en `documentos/ENTREGA/diagramas uml/`. Incluyen diagramas de componentes, clases por paquete, actividad de turno, estados, casos de uso y secuencias de ataque y guardado/carga JSON. Las versiones PNG se insertan en esta memoria y las versiones PlantUML quedan disponibles para consulta.

## 17.2. Referencia A JSON De Ejemplo

Los JSON de ejemplo estan en `documentos/ENTREGA/json_ejemplo/`. `cuevas_ejemplo.json` refleja la configuracion inicial real. `partida_guardada_ejemplo.json` muestra un estado persistido. `ranking_ejemplo.json` muestra el formato del ranking. `json_prueba.json` sirve como ejemplo documental reducido.

## 17.3. Referencia A Project-Management

La carpeta `documentos/ENTREGA/project-management/` contiene `ARCHITECTURE.md`, `DECISIONS.md`, `TASKS.md`, `POST_MORTEM.md`, `REVIEW_CHECKLIST.md`, `IA_DIARY.md` y otros documentos auxiliares. Esta memoria se basa en esos documentos, pero no reutiliza la memoria anterior como fuente.

## 17.4. Referencia A Diario IA Completo

El diario completo de uso de IA esta en `documentos/ENTREGA/project-management/IA_DIARY.md`. La version resumida esta en `documentos/ENTREGA/IA_DIARY_RESUMIDO.md`. Ambos documentan metodologia human-in-the-loop, prompts, resultados, cambios aceptados, cambios rechazados y critica.
