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

No hay tareas en curso todavia.

## Pendientes urgentes

### A-01 Revisar estructuras propias existentes

- Responsable: Persona A
- Estado: PENDIENTE
- Archivos permitidos: `src/Estructuras/`, `src/ParteA/`, `src/ParteB/Grafo/`, `project-management/`
- Terminado cuando haya tabla de estructuras disponibles, usos y riesgos.

### A-02 Disenar matriz propia de cueva

- Responsable: Persona A
- Estado: PENDIENTE
- Archivos permitidos: `src/modelo/mapa/`, `test/`, `project-management/`
- Terminado cuando exista diseno o implementacion de cueva con `ListaSE<ListaSE<Celda>>`.

### A-03 Disenar grafo de cuevas

- Responsable: Persona A
- Estado: PENDIENTE
- Archivos permitidos: `src/modelo/juego/`, `src/modelo/mapa/`, `test/`, `project-management/`
- Terminado cuando `Mazmorra` pueda representar 3 cuevas conectadas con `Grafo<Cueva>`.

### A-04 BFS de celdas alcanzables

- Responsable: Persona A
- Estado: PENDIENTE
- Archivos permitidos: `src/modelo/mapa/`, `test/`, `project-management/`
- Terminado cuando se puedan obtener celdas alcanzables sin diagonales usando `Cola` propia.

### B-01 Modelo de personajes

- Responsable: Guille / Parte B
- Estado: PENDIENTE
- Archivos permitidos: `src/modelo/personajes/`, `test/`, `project-management/`
- Terminado cuando existan `Personaje`, `Jugador`, `Enemigo` y `Boss` segun arquitectura acordada.

### B-02 Modelo de objetos e inventario

- Responsable: Guille / Parte B
- Estado: PENDIENTE
- Archivos permitidos: `src/modelo/objetos/`, `src/modelo/personajes/`, `test/`, `project-management/`
- Terminado cuando existan `Objeto`, `Pocion`, `Arma`, `Espada`, `Llave` e inventario con `ListaDE<Objeto>`.

### B-03 Reglas de turno y combate

- Responsable: Guille / Parte B
- Estado: PENDIENTE
- Archivos permitidos: `src/modelo/juego/`, `src/modelo/personajes/`, `src/modelo/objetos/`, `test/`, `project-management/`
- Terminado cuando el jugador pueda actuar, enemigos respondan, se aplique dano y haya victoria/derrota basica.

### C-01 Disenar JSON inicial

- Responsable: Hector / Parte C
- Estado: PENDIENTE
- Archivos permitidos: `src/json/`, `datos/`, `project-management/`
- Terminado cuando exista un JSON de configuracion para las 3 cuevas.

### C-02 Cargar y guardar partida

- Responsable: Hector / Parte C
- Estado: PENDIENTE
- Archivos permitidos: `src/json/`, `test/`, `project-management/`
- Terminado cuando se pueda cargar configuracion inicial y guardar/cargar estado.

### C-03 Boceto JavaFX

- Responsable: Hector / Parte C
- Estado: PENDIENTE
- Archivos permitidos: `docs/`, `project-management/`
- Terminado cuando haya boceto de pantalla con matriz, estado, acciones, inventario y log.

### C-04 JavaFX minima

- Responsable: Hector / Parte C
- Estado: PENDIENTE
- Archivos permitidos: `src/vista/`, `src/controlador/`, `project-management/`
- Terminado cuando la interfaz muestre la cueva actual y permita ejecutar acciones basicas via `Partida`.

### R-01 Revision de restricciones

- Responsable: Agente Revisor
- Estado: PENDIENTE
- Archivos permitidos: todo el repositorio en modo lectura; cambios solo con permiso
- Terminado cuando se revise que no hay colecciones prohibidas ni arrays para matriz.

## Hechas

### PM-01 Crear documentos de coordinacion

- Responsable: Coordinador
- Estado: HECHA
- Archivos permitidos: `project-management/`
- Terminado: existen PRD, arquitectura, tareas, agentes, decisiones, scratchpad, checklist, diario IA y guion de reunion.
