# Tasks

## Normas

Cada tarea debe tener:

- ID.
- Responsable.
- Estado.
- Archivos permitidos.
- Criterio de terminado.

Estados:

```text
PENDIENTE
EN_CURSO
BLOQUEADA
REVISION
HECHA
```

## Responsables confirmados

- Parte A: Alvaro + Codex-A Estructuras. Responsables de estructuras propias, matriz de cuevas, grafo de cuevas, BFS, costes y pruebas de estructuras/mapa.
- Parte B: Guille + Agente B Logica. Responsables de logica del juego, personajes, turnos, combate, inventario, objetos, victoria y derrota.
- Parte C: Hector + Agente C JavaFX/JSON/Docs. Responsables de JavaFX, JSON, documentacion, UML, diario IA, bocetos y video/guion.

## En curso

### Resumen de tareas pendientes (Parte C)

- **C-09** Pulido audiovisual (sonidos/musica/animaciones/menu pausa) - PENDIENTE
- **C-10** Ataque direccional, ataque especial cargable y revision de turnos - PENDIENTE
- **C-11** Estadisticas, puntuacion y ranking con Gson - HECHA
- **R-01** Revision de restricciones (colecciones prohibidas, arrays) - PENDIENTE
- **R-02** Revision de codigo y tests para cada PR - PENDIENTE

### Resumen de tareas pendientes (Parte B)

- **B-02** Modelo de objetos e inventario - HECHA
- **B-03** Reglas de turno y combate - HECHA
- **B-04** Mejoras de logica (IA enemiga, drops, cofres, trampas) - PENDIENTE
- **B-05** Ataque direccional - REVISION

### Resumen de tareas pendientes (Parte A)



## Peticiones urgentes entre partes

Usar esta seccion cuando una parte necesite algo de otra. Los agentes deben revisarla al inicio de sesion.

### Para Parte A - Alvaro / Codex-A

- Ninguna peticion urgente registrada.

### Para Parte B - Guille / Agente B Logica

- Cerrar alcance minimo de logica del juego: turnos, condiciones de victoria/derrota, valores base de jugador, enemigos, objetos y puertas.

### Para Parte C - Hector / Agente C JavaFX/JSON/Docs

- Ninguna peticion urgente registrada.

### Para Agente Revisor Independiente

- Revisar toda PR que contenga codigo antes de merge a `main`.

## Pendientes urgentes

### A-01 Revisar estructuras propias existentes

- Responsable: Persona A
- Estado: PENDIENTE
- Archivos permitidos: `src/Estructuras/`, `src/ParteA/`, `src/ParteB/Grafo/`, `project-management/`
- Terminado cuando haya tabla de estructuras disponibles, usos y riesgos.

### B-02 Modelo de objetos e inventario

- Responsable: Guille / Parte B
- Estado: HECHA
- Archivos permitidos: `src/modelo/objetos/`, `src/modelo/personajes/`, `src/Estructuras/ListaDE.java`, `src/Estructuras/ElementoDE.java`, `src/Estructuras/IteradorDE.java`, `test/`, `project-management/`
- Terminado cuando existan `Objeto`, `Pocion`, `Arma`, `Espada`, `Arco`, `Escudo`, `Llave` e inventario con `ListaDE<Objeto>`.
- Nota de alcance: Guillermo autoriza traer `ListaDE`, `ElementoDE` e `IteradorDE` desde las estructuras del grupo y adaptar `ListaDE` para no exigir `Comparable`, porque el inventario necesita guardar objetos sin orden natural.
- Verificacion de cierre: PR #6 implemento modelo y tests; el juego actual carga objetos desde JSON, permite recogerlos y usar/equipar consumibles, armas y escudos desde inventario. El 2026-05-24 se ajusta `equiparObjeto` para no consumir accion, segun regla acordada.

### C-02 Cargar y guardar partida

- Responsable: Hector / Parte C
- Estado: HECHA
- Archivos permitidos: `src/json/`, `test/`, `project-management/`
- Terminado cuando se pueda cargar configuracion inicial y guardar/cargar estado.
- Verificacion: 19 tests JUnit pasados el 2026-05-22 (guardado/carga round-trip, matriz, enemigos, objetos, inventario, equipo, estado, errores, conversion DTO<->modelo).

### B-03 Reglas de turno y combate

- Responsable: Guille / Parte B
- Estado: HECHA
- Archivos permitidos: `src/modelo/juego/`, `src/modelo/personajes/`, `src/modelo/objetos/`, `test/`, `project-management/`
- Terminado cuando el jugador pueda actuar, enemigos respondan, se aplique dano y haya victoria/derrota basica.
- Avance actual: creada la primera version del contrato publico `InterfazPartida`, el enum `EstadoPartida`, `Partida`, `Puerta`, `ObjetoEnMapa`, `PersonajeEnMapa`, `CuevaEnMapa`, `CeldaEnMapa` y tests JUnit basicos de logica de partida.
- Avance de integracion JSON-Partida: anadida `FabricaPartida` para crear una `Partida` desde `ResultadoCarga`, conservando `idCueva` en enemigos/objetos, usando conexiones JSON como puertas y permitiendo objetos con `id`, `tipoLlave` y `codigoCerradura`.
- Decisiones de diseno para implementar `Partida`:
  - `Partida` gestionara inicialmente los enemigos y objetos por cueva, sin meter esa responsabilidad dentro de `Cueva`.
  - La estructura elegida debe permitir que en una iteracion futura cada cueva tenga sus propios enemigos y objetos de forma directa.
  - Los objetos tendran posicion en el mapa para poder recogerlos desde celdas cercanas.
  - Se creara una clase `Puerta` para representar conexiones con requisito de llave.
  - `Partida` no conectara cuevas; las conexiones pertenecen a `Mazmorra`.
  - Las puertas se cargaran como configuracion inicial de `Partida` y solo podran referirse a conexiones ya existentes en `Mazmorra`.
  - Una puerta ya abierta podra atravesarse sin volver a exigir la llave.
  - Jugador y enemigos no podran compartir celda; `Partida` validara solapes mientras `Cueva` no gestione ocupantes.
  - El jugador podra recoger objetos en celdas adyacentes.
  - El jugador podra atacar enemigos adyacentes, incluyendo diagonales.
  - Si el jugador tiene arco equipado, podra atacar a distancia.
  - Formula de dano: ataque menos defensa, con dano minimo 1 cuando el ataque sea valido.
  - En el turno enemigo, cada enemigo atacara si esta adyacente al jugador; si no, se acercara usando camino minimo.
  - El turno del jugador terminara solo cuando pulse `pasarTurno()`, no automaticamente tras moverse o actuar.
  - Para avanzar entre cuevas, el jugador necesitara la llave de la puerta correspondiente.
  - Para ganar, el jugador debera conseguir la llave final, que solo se obtiene derrotando al boss final.
  - El log de eventos vivira de momento en `Partida` como estructura propia simple.
- Revision independiente:
  - Pasada el 2026-05-22 sobre las clases nuevas.
  - Corregidos los problemas detectados sobre exposicion mutable principal, ocupacion de celdas, avance a cueva destino ocupada, llave final con id conflictivo, semantica de puerta abierta, `ObjetoEnMapa.equals()` y metodos de preparacion fuera de `InterfazPartida`.
  - Pasada revision independiente adicional sobre el puente JSON-Partida; corregidos conexion JSON invalida ignorada, recolocacion del jugador al cambiar de cueva y estadisticas del boss en `datos/cuevas.json`.
- Cierre 2026-05-24:
  - PR #15 deja documentados 189/189 tests JUnit pasados.
  - Guillermo confirma que `FabricaPartida` cubre lo necesario para el arranque desde JSON y JavaFX.
  - Guillermo confirma `ALCANCE_ARCO = 3` como alcance definitivo para la primera version.
  - Se corrige avance de cueva: no genera consumo nuevo de accion, no termina turno, no hace actuar enemigos y reinicia turnos a 60 al entrar en la siguiente cueva; si el jugador ya habia usado accion o movimiento, ese estado se conserva hasta pasar turno.
  - Verificacion dirigida: `PartidaTest` y `FabricaPartidaTest` pasan con 64/64 tests correctos en compilacion logica sin JavaFX.

### B-04 Mejoras de logica para iteraciones posteriores

- Responsable: Guille / Parte B
- Estado: PENDIENTE
- Archivos permitidos: `src/modelo/juego/`, `src/modelo/personajes/`, `src/modelo/objetos/`, `src/modelo/mapa/`, `test/`, `project-management/`
- Terminado cuando se revisen y se implementen las mejoras aprobadas despues de la primera version funcional.
- Tareas propuestas:
  - Mover la gestion de enemigos y objetos para que cada `Cueva` tenga directamente sus propios contenidos si el diseno final lo pide.
  - Crear enemigos con ataque a distancia.
  - Evitar que el jugador atraviese enemigos durante un movimiento largo, no solo que termine en una celda ocupada.
  - Hacer que la IA enemiga busque una ruta alternativa si el primer paso del camino minimo esta ocupado.
  - Quitar la referencia mutable a `Cueva` de `ObjetoEnMapa` o sustituirla por id/vista inmutable cuando JavaFX y JSON esten conectados.
  - Revisar si la `FabricaPartida` actual debe evolucionar a builder formal cuando JavaFX y guardado/carga de estado esten cerrados.
  - Anadir tests de derrota por turnos, derrota por muerte, uso de pociones, equipamiento y enemigo acercandose desde distancia.
  - Afinar el alcance real del arco y posibles lineas de vision.
  - Anadir cofres con llave y objetos dentro.
  - Anadir trampas con dano o efectos simples.
  - Anadir drops especificos de enemigos, incluyendo la llave final del boss.
  - Mejorar IA enemiga con reglas distintas por tipo de enemigo.
  - Revisar si el log necesita pasar de `ListaSE<String>` a una clase `LogJuego`.

### C-03 Boceto JavaFX

- Responsable: Hector / Parte C
- Estado: REVISION
- Archivos permitidos: `docs/`, `project-management/`
- Terminado cuando haya boceto de pantalla con matriz, estado, acciones, inventario y log.
- Verificacion: documento `docs/BOCETO_JAVAFX.md` (234 lineas) con layout ASCII, 5 zonas detalladas, tabla de colores, mapeo JavaFX, flujo de datos, estructura de clases para C-04, contrato minimo de Partida, estrategia mock y checklist.

### C-04 Menu principal JavaFX

- Responsable: Hector / Parte C
- Estado: HECHA
- Archivos permitidos: `src/vista/`, `src/controlador/`, `project-management/`
- Terminado cuando exista ventana 1280x720 con fondo cueva, antorchas animadas, tesoro decorativo, pantalla Inicio -> Opciones navegable con FadeTransition.
- Verificacion: `EscapeMazmorraApp.java` compila con JavaFX 23 (2026-05-22).

### C-05 Partida jugable (integracion juego + UI)

- Responsable: Hector / Parte C
- Estado: HECHA
- Archivos permitidos: `src/modelo/juego/Partida.java`, `src/vista/PantallaJuego.java`, `src/vista/EscapeMazmorraApp.java`, `test/`, `project-management/`
- Terminado cuando exista Partida que cargue JSON, maneje movimiento, combate, objetos, turnos, condiciones de victoria/derrota, guardado/carga, y PantallaJuego que muestre grid coloreado con jugador/enemigos/objetos, panel de stats, inventario, acciones, log, teclado WASD/flechas y atajos SPACE/R/T.
- Verificacion: 148 tests JUnit pasados el 2026-05-22 (24 tests de Partida + 124 existentes). Demo jugable confirmada por Hector.

### C-10 Ataque direccional, ataque especial cargable y revision de turnos

- Responsable: Hector / Parte C
- Estado: PENDIENTE
- Archivos permitidos: `src/modelo/juego/`, `src/modelo/personajes/`, `src/vista/`, `test/`, `project-management/`
- Terminado cuando:
  1. El jugador pueda elegir la direccion del ataque si hay varios enemigos adyacentes (ataque direccional). Avance 2026-05-24: implementado para clic sobre enemigo y Shift+WASD/flechas; pendiente revision independiente.
  2. Exista un ataque especial que se vaya cargando a lo largo de la partida (cada cierto numero de turnos) y pueda ejecutarse cuando este cargado.
  3. Se haya revisado y ajustado la cantidad de turnos totales de la partida y los turnos por cueva.

### C-09 Pulido audiovisual y presentacion

- Responsable: Hector / Parte C
- Estado: EN_CURSO
- Archivos permitidos: `src/vista/`, `datos/`, recursos graficos/audio, `project-management/`
- Terminado cuando la UI de partida este mas pulida visualmente y, si se aprueba, incluya efectos de sonido o musica para pasos, ataques, objetos y cambio de habitacion.

Sub-tareas:
- **C-09.1** Obstaculos (ROCA, ARBUSTO), mapas laberinticos (15x15, 19x19, 23x23), muros macizos (10px) y sistema de niebla (fog-of-war con radio 3 y opacidad progresiva) - HECHA
- **C-09.2** Musica de fondo con javafx.media y ReproductorMusica singleton - HECHA
- **C-09.3** Animaciones (movimiento suave, ataque, muerte) - HECHA
- **C-09.4** Efectos visuales (particulas, brillos) - PENDIENTE
- **C-09.5** Sonidos de juego (paso, ataque, objeto, puerta, victoria/derrota) - PENDIENTE
- **C-09.6** Mejorar diseno de cuevas: mapas laberinticos con paredes de 2 celdas, 3 dificultades, mejor trazado y obstaculos - HECHA

- **C-09.7** Implementar efectos de sonido cortos (SFX) para interacciones: recoger objeto, ataque y dano - REVISION
- **C-09.8** Anadir animaciones de transicion fluida (fade in/out) entre pantallas - PENDIENTE
- **C-09.9** Desarrollar sistema de alertas visuales temporales: textos flotantes de dano y cura - PENDIENTE
- **C-09.10** Crear modo antorcha / filtro de contraste accesible mediante CSS dinamico - PENDIENTE
- **C-09.11** Aumentar turnos de 40 a 60 y auto-avance en PUERTA con llave - HECHA
- **C-09.12** Menu de pausa durante la partida (P o ESC) con opciones: Continuar, Guardar, Volver al menu - REVISION
- **C-09.13** Encontrar/crear icono para celda PUERTA (actualmente solo color amarillo) - REVISION
- **C-09.14** Encontrar/crear icono para ESCUDO (reemplazar `staff2.png` por un asset tipo escudo) - REVISION
- **C-09.15** Añadir iconos decorativos para TESORO y SALIDA (actualmente solo color) - REVISION
- **C-09.16** Buscar assets adicionales en itch.io: pociones, puertas, escudos, cofres y bosses - PENDIENTE
  - Avance 2026-05-24 / Guillermo: se anade icono local de pocion, se reemplaza el icono local de escudo y se elimina el color morado de TESORO. Los bosses quedan fuera de esta sesion por decision de alcance.
  - Avance 2026-05-24 / Guillermo: se refuerza `datos/cuevas.json` y los tests para que los objetos configurados no aparezcan en muros/obstaculos y los TESORO tengan acceso desde una celda vecina.
  - Avance 2026-05-24 / Guillermo: se define TESORO como cofre cerrado no pisable, abrible con `R` desde celda adyacente; al abrirse pasa a `SUELO`.

### R-01 Revision de restricciones

- Responsable: Agente Revisor Independiente
- Estado: PENDIENTE
- Archivos permitidos: todo el repositorio en modo lectura; cambios solo con permiso
- Terminado cuando se revise que no hay colecciones prohibidas ni arrays para matriz.

### R-02 Revision de codigo y tests

- Responsable: Agente Revisor Independiente
- Estado: PENDIENTE
- Archivos permitidos: todo el repositorio en modo lectura; cambios solo con permiso
- Terminado cuando cada PR con codigo tenga revision independiente, comprobacion de tests JUnit y checklist completado.

## Hechas

### A-02 Disenar matriz propia de cueva

- Responsable: Alvaro + Codex-A Estructuras
- Estado: HECHA
- Archivos permitidos: `src/modelo/mapa/`, `test/`, `project-management/`
- Terminado: existe implementacion de `Cueva` con matriz propia `ListaSE<ListaSE<Celda>>`, clases `Celda`, `Posicion`, `TipoCelda`, interfaces publicas y tests de matriz.

### A-04 BFS de celdas alcanzables

- Responsable: Alvaro + Codex-A Estructuras
- Estado: HECHA
- Archivos permitidos: `src/modelo/mapa/`, `test/`, `project-management/`
- Terminado: se obtienen celdas alcanzables sin diagonales usando `Cola` propia; tambien existen camino minimo y distancia minima.
- Verificacion: tests pasados en IntelliJ el 2026-05-21, con cobertura alta en `modelo.mapa`, `Cueva`, `Cola` y `ListaSE`.

### A-03 Disenar grafo de cuevas

- Responsable: Alvaro + Codex-A Estructuras
- Estado: HECHA
- Archivos permitidos: `src/modelo/juego/`, `src/modelo/mapa/`, `test/`, `project-management/`
- Terminado: existe `Grafo<T>` dirigido sin `Comparable`, `Mazmorra` contiene `Grafo<Cueva>`, hay cueva actual, conexiones dirigidas, avance entre cuevas, camino minimo y distancia minima.
- Verificacion: tests pasados en IntelliJ el 2026-05-21, con cobertura alta en `Estructuras`, `modelo.juego`, `modelo.mapa`, `Grafo`, `Mazmorra`, `Cueva`, `Cola` y `ListaSE`.

### B-01 Modelo de personajes

- Responsable: Guille / Parte B
- Estado: HECHA
- Archivos permitidos: `src/modelo/personajes/`, `test/`, `project-management/`
- Terminado: existen `Personaje`, `Jugador`, `Enemigo` y `Boss` segun arquitectura acordada.
- Verificacion: PR #4 mergeada en `main` el 2026-05-21; tests JUnit de `modelo.personajes` pasados y cobertura verificada en IntelliJ.

### B-02 Modelo de objetos e inventario

- Responsable: Guille / Parte B
- Estado: HECHA
- Archivos permitidos: `src/modelo/objetos/`, `src/modelo/personajes/`, `src/Estructuras/ListaDE.java`, `src/Estructuras/ElementoDE.java`, `src/Estructuras/IteradorDE.java`, `test/`, `project-management/`
- Terminado: existen `Objeto`, `Pocion`, `Arma`, `Espada`, `Arco`, `Escudo`, `Llave` e inventario con `ListaDE<Objeto>`. Equipo con ranuras de arma y escudo, arco a dos manos desequipa escudo, pocion consumible, llaves con tipo y codigo.
- Verificacion: 16 tests JUnit de objetos/inventario + 189/189 tests globales pasados el 2026-05-24.
- Nota: `ListaDE` adaptada para no exigir `Comparable`, necesaria para inventario sin orden natural.

### A-01 Revisar estructuras propias existentes

- Responsable: Alvaro / Parte A
- Estado: HECHA
- Archivos permitidos: `src/Estructuras/`, `project-management/`
- Terminado: tabla detallada de las 6 estructuras (ListaSE, ListaDE, Cola, Grafo, NodoGrafo, ArcoGrafo) con almacenamiento interno, complejidad, usos concretos en el proyecto y riesgos. Documentado en `ARCHITECTURE.md` seccion 7.

### B-05 Ataque direccional

- Responsable: Guille / Parte B, Alvaro / Parte A (implementacion UI)
- Estado: HECHA
- Archivos permitidos: `src/modelo/juego/`, `src/vista/`, `test/`, `project-management/`
- Terminado: el jugador puede elegir direccion con Shift+WASD/flechas o hacer clic sobre un enemigo para atacarlo. Resaltado visual dorado en enemigos adyacentes atacables. Reutiliza `atacar(fila, columna)` existente.
- Tests: `hayEnemigoEnDireccion`, `getEnemigosAdyacentes`, `atacarDireccionConVariosEnemigosSoloDanaElElegido`, direccion sin enemigo.
- Verificacion: 189/189 tests pasados el 2026-05-24. Commit `b9dc701` en `feature/a-estructuras`.

### C-01 Disenar JSON inicial

- Responsable: Hector / Parte C
- Estado: HECHA
- Archivos permitidos: `src/json/`, `datos/`, `project-management/`
- Terminado: existe un JSON de configuracion para las 3 cuevas, DTOs, cargador Gson y tests de carga.
- Verificacion: PR #3 mergeada en `main` el 2026-05-21; compilacion manual de `src` y tests correcta.

### INT-02 Actualizar tests a los mapas actuales

- Responsable: Alvaro / Guille / Hector segun area afectada
- Estado: HECHA
- Archivos permitidos: `test/`, `datos/cuevas.json`, `project-management/`
- Terminado cuando la suite JUnit refleje los mapas actuales 7x7, 10x10 y 13x13, los ids actuales de objetos/enemigos y las estadisticas actuales del boss.
- Verificacion: 180/180 tests pasados el 2026-05-22.

### B-06 Decidir condicion final de victoria

- Responsable: Guille / Parte B, con validacion del grupo
- Estado: HECHA
- Archivos permitidos: `src/modelo/juego/`, `test/modelo/juego/`, `datos/cuevas.json`, `project-management/DECISIONS.md`
- Terminado: victoria requiere derrotar al boss para obtener llave final Y pisar celda SALIDA. Implementado en `Partida.comprobarVictoriaODerrota()` y documentado en DECISIONS.md D-18.

### C-06 Menu principal completo para demo presentable

- Responsable: Hector / Parte C
- Estado: HECHA
- Archivos permitidos: `src/vista/`, `src/json/`, `src/modelo/juego/`, `project-management/`
- Terminado: boton "Controles" reemplaza a "Ajustes", "Cargar partida" funcional, "Salir" cierra ventana, titulo ESCAPE en linea recta.

### C-07 Guardado y carga honesta desde UI

- Responsable: Hector / Parte C, con apoyo de Parte B si hace falta reconstruir `Partida`
- Estado: HECHA
- Archivos permitidos: `src/json/`, `src/modelo/juego/`, `src/vista/`, `test/`, `project-management/`
- Terminado: guardar serializa enemigos vivos, objetos en suelo y puertas; cargar restaura ContenidoCueva completo desde DTO. 182 tests pasan.
- Nota: puertas se recrean desde conexiones al cargar (opcion A, pierde estado abierto/cerrado).

### C-08 Tutorial o ayuda integrada

- Responsable: Hector / Parte C
- Estado: HECHA
- Archivos permitidos: `src/vista/`, `project-management/`
- Terminado: boton "AYUDA [H]" en panel de acciones, overlay superpuesto con todos los controles, atajo tecla H.

### C-11 Estadisticas, puntuacion y ranking con Gson

- Responsable: Alvaro / Parte A, con excepcion autorizada para tocar logica de Parte B y UI/JSON de Parte C.
- Estado: HECHA
- Archivos permitidos: `src/modelo/juego/`, `src/json/`, `src/vista/`, `test/`, `project-management/`
- Terminado: `Partida` registra estadisticas mediante `EstadisticasPartida`; la pantalla final calcula puntuacion y titulo; `ranking.json` se guarda con Gson; el menu incluye boton `Ranking` con Top 10.
- Ajuste posterior: el menu de opciones sube la botonera para no tapar el cofre y el nombre del mago se pide con un modal propio integrado en la estetica del juego.
- Verificacion: 208/208 tests JUnit pasados el 2026-05-24 con `scripts/test.ps1`.

### B-07 Bola de Fuego en tiempo real

- Responsable: Alvaro / Parte A, con excepcion autorizada para tocar combate de Parte B y UI de Parte C.
- Estado: HECHA
- Archivos permitidos: `src/modelo/juego/`, `src/vista/`, `test/`, `project-management/`
- Terminado: `F + Flecha` lanza una Bola de Fuego con Timeline propio, rango 5, dano fijo 10, impacto contra muros/enemigos, SFX de disparo/impacto y log naranja.
- Verificacion: 213/213 tests JUnit pasados el 2026-05-24 con `scripts/test.ps1`.

### C-12 Redisenio premium pantalla de inicio

- Responsable: Alvaro / Parte A, con excepcion autorizada para pulido visual de UI.
- Estado: HECHA
- Archivos permitidos: `src/vista/EscapeMazmorraApp.java`, `project-management/`
- Terminado: la pantalla de inicio usa logo premium, marco central, arco de mazmorra, luz ambiental, chispas y boton pergamino; el menu de opciones reutiliza el nuevo helper de boton.
- Ajuste posterior: se reemplazo la version de marco/arco por una composicion mas simple de estilo pixel art con assets del Dungeon Asset Pack.
- Ajuste posterior: a peticion de Alvaro, la primera pantalla de inicio se dejo de nuevo como estaba antes de los redisenios de esta sesion.
- Verificacion: 213/213 tests JUnit pasados el 2026-05-24 con `scripts/test.ps1`.

### PM-01 Crear documentos de coordinacion

- Responsable: Coordinador
- Estado: HECHA
- Archivos permitidos: `project-management/`
- Terminado: existen PRD, arquitectura, tareas, agentes, decisiones, scratchpad, checklist, diario IA y guion de reunion.
