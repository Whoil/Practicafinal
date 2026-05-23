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

- **C-09** Pulido audiovisual (sonidos/musica/animaciones) - PENDIENTE
- **R-01** Revision de restricciones (colecciones prohibidas, arrays) - PENDIENTE
- **R-02** Revision de codigo y tests para cada PR - PENDIENTE

### Resumen de tareas pendientes (Parte B)

- **B-02** Modelo de objetos e inventario - REVISION
- **B-03** Reglas de turno y combate - REVISION
- **B-04** Mejoras de logica (IA enemiga, drops, cofres, trampas) - PENDIENTE
- **B-05** Ataque direccional - PENDIENTE

### Resumen de tareas pendientes (Parte A)

- **A-01** Revisar estructuras propias - PENDIENTE

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
- Estado: REVISION
- Archivos permitidos: `src/modelo/objetos/`, `src/modelo/personajes/`, `src/Estructuras/ListaDE.java`, `src/Estructuras/ElementoDE.java`, `src/Estructuras/IteradorDE.java`, `test/`, `project-management/`
- Terminado cuando existan `Objeto`, `Pocion`, `Arma`, `Espada`, `Arco`, `Escudo`, `Llave` e inventario con `ListaDE<Objeto>`.
- Nota de alcance: Guillermo autoriza traer `ListaDE`, `ElementoDE` e `IteradorDE` desde las estructuras del grupo y adaptar `ListaDE` para no exigir `Comparable`, porque el inventario necesita guardar objetos sin orden natural.

### C-02 Cargar y guardar partida

- Responsable: Hector / Parte C
- Estado: HECHA
- Archivos permitidos: `src/json/`, `test/`, `project-management/`
- Terminado cuando se pueda cargar configuracion inicial y guardar/cargar estado.
- Verificacion: 19 tests JUnit pasados el 2026-05-22 (guardado/carga round-trip, matriz, enemigos, objetos, inventario, equipo, estado, errores, conversion DTO<->modelo).
### B-03 Reglas de turno y combate

- Responsable: Guille / Parte B
- Estado: REVISION
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
- Pendiente antes de marcar como HECHA:
  - Ejecutar tests JUnit completos en IntelliJ.
  - Confirmar con Parte C si `FabricaPartida` cubre lo necesario para la carga inicial desde JSON y para el arranque de JavaFX.
  - Revisar si `ALCANCE_ARCO = 3` es el alcance definitivo para la primera version.
  - Revisar si avanzar de cueva debe consumir accion pero no finalizar automaticamente el turno, como queda implementado.

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

### B-05 Ataque direccional cuando haya varios enemigos cerca

- Responsable: Guille / Parte B
- Estado: PENDIENTE
- Archivos permitidos: `src/modelo/juego/`, `src/modelo/personajes/`, `test/modelo/juego/`, `project-management/`
- Terminado cuando el jugador pueda elegir la direccion u objetivo del ataque si hay varios enemigos adyacentes, y solo reciba dano el enemigo elegido.
- Propuesta tecnica: exponer un metodo tipo `atacarDireccion(df, dc)` o reutilizar `atacar(fila, columna)` desde JavaFX.
- Tests minimos: varios enemigos adyacentes, ataque hacia una direccion concreta, direccion sin enemigo devuelve `false`.

### C-09 Pulido audiovisual y presentacion

- Responsable: Hector / Parte C
- Estado: EN_CURSO
- Archivos permitidos: `src/vista/`, `datos/`, recursos graficos/audio, `project-management/`
- Terminado cuando la UI de partida este mas pulida visualmente y, si se aprueba, incluya efectos de sonido o musica para pasos, ataques, objetos y cambio de habitacion.

Sub-tareas:
- **C-09.1** Obstaculos (ROCA, ARBUSTO), mapas laberinticos (15x15, 19x19, 23x23), muros macizos (10px) y sistema de niebla (fog-of-war con radio 3 y opacidad progresiva) - HECHA
- **C-09.2** Musica de fondo con javafx.media y ReproductorMusica singleton - HECHA
- **C-09.3** Animaciones (movimiento suave, ataque, muerte) - PENDIENTE
- **C-09.4** Efectos visuales (particulas, brillos) - PENDIENTE
- **C-09.5** Sonidos de juego (paso, ataque, objeto, puerta, victoria/derrota) - PENDIENTE
- **C-09.6** Mejorar diseno de cuevas: mapas laberinticos con paredes de 2 celdas, 3 dificultades, mejor trazado y obstaculos - HECHA

- **C-09.7** Implementar efectos de sonido cortos (SFX) para interacciones: recoger objeto, ataque y dano - PENDIENTE
- **C-09.8** Anadir animaciones de transicion fluida (fade in/out) entre pantallas - PENDIENTE
- **C-09.9** Desarrollar sistema de alertas visuales temporales: textos flotantes de dano y cura - PENDIENTE
- **C-09.10** Crear modo antorcha / filtro de contraste accesible mediante CSS dinamico - PENDIENTE

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

### PM-01 Crear documentos de coordinacion

- Responsable: Coordinador
- Estado: HECHA
- Archivos permitidos: `project-management/`
- Terminado: existen PRD, arquitectura, tareas, agentes, decisiones, scratchpad, checklist, diario IA y guion de reunion.
