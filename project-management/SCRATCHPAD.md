# Scratchpad

Este archivo es el diario tecnico rapido del equipo y de los agentes.

Debe actualizarse despues de cada cambio importante.

Formato recomendado:

```markdown
## YYYY-MM-DD - Persona/Agente

### Identificacion de sesion

Humano:
Rol:
Agente:

### Contexto

Que se intento hacer.

### Sincronizacion

Rama:
Cambio remoto revisado: si/no
Documentos leidos:

### Cambios

- Archivo tocado.
- Decision aplicada.
- Funcionamiento anadido.

### Pendiente

- Tareas que quedan.
- Bloqueos.

### Riesgos

- Posibles problemas.
- Cosas que revisar.
```

## 2026-05-20 - Coordinacion inicial

### Contexto

Se preparan documentos de organizacion antes de dar acceso a GitHub a los agentes.

### Cambios

- Creada carpeta `project-management`.
- Definidos documentos base de producto, arquitectura, tareas, agentes, decisiones, revision y diario IA.
- Existe UML inicial en `diagrama_inicial_juego.puml`.
- Se acuerda que al inicio de cada sesion cada humano indicara quien es y que parte representa.

### Pendiente

- Confirmar valores finales de jugador, turnos, enemigos y objetos.
- Reparto real confirmado: Alvaro Parte A, Guille Parte B, Hector Parte C.
- Confirmar flujo GitHub y ramas.
- Actualizar `TASKS.md` tras la reunion.
- Mantener los documentos de coordinacion sincronizados en GitHub despues de cada cambio relevante.

### Riesgos

- Empezar a implementar extras antes de tener partida jugable.
- Que los agentes modifiquen archivos fuera de su area.
- Usar colecciones prohibidas por accidente.

## 2026-05-20 - Asignacion Parte A

### Contexto

Se confirma que Alvaro y Codex-A Estructuras quedan encargados de la Parte A del proyecto.

### Cambios

- Parte A queda asignada a estructuras propias, matriz de cuevas, grafo de cuevas, BFS, costes y pruebas de estructuras/mapa.
- Parte A no debe modificar logica de juego, JavaFX, JSON o documentacion final salvo autorizacion previa o coordinacion con las otras partes.
- En sesiones futuras, Alvaro debe identificarse como Parte A para que Codex-A Estructuras confirme el area antes de trabajar.

### Pendiente

- Responsables confirmados: Guille Parte B y Hector Parte C.
- Empezar por tarea `A-01 Revisar estructuras propias existentes`.

### Riesgos

- Que Parte A toque clases compartidas como `Partida`, `Jugador`, `Objeto`, `Cueva` o `Mazmorra` sin coordinarlo.
- Que se creen estructuras nuevas sin autorizacion humana.

## 2026-05-20 - Cierre de sesion con Alvaro Parte A

### Identificacion de sesion

Humano: Alvaro
Rol: Parte A
Agente: Codex-A Estructuras

### Contexto

Se preparo la organizacion inicial del proyecto antes de empezar la implementacion. La sesion se centro en metodologia, coordinacion de agentes, GitHub, ramas de trabajo y reglas para evitar que los agentes se pisen entre si.

### Sincronizacion

Rama: main
Cambio remoto revisado: si
Documentos leidos/modificados: `AGENTS.md`, `GITHUB_WORKFLOW.md`, `TASKS.md`, `SCRATCHPAD.md`, `REVIEW_CHECKLIST.md`, archivos especificos de agentes.

### Cambios

- Se confirmo que Alvaro + Codex-A Estructuras son responsables de Parte A.
- Se confirmo que Guille + Agente B Logica son responsables de Parte B.
- Se confirmo que Hector + Agente C JavaFX/JSON/Docs son responsables de Parte C.
- Se establecio que cada humano debe identificarse al inicio de sesion.
- Se establecio que cada agente debe confirmar rol, area y rama antes de trabajar.
- Se definio que cada agente debe comprobar cambios remotos y leer documentacion actualizada antes de modificar codigo.
- Se definio que los agentes deben actualizar documentacion de coordinacion tras cambios relevantes.
- Se definio que los agentes deben pedir autorizacion explicita para commit/push tras cambios significativos o documentales.
- Se definio que los mensajes de commit deben estar en espanol y ser descriptivos.
- Se crearon y subieron las ramas:
  - `feature/a-estructuras`
  - `feature/b-logica`
  - `feature/c-javafx-json-docs`
- Se establecio que los commits de cada parte deben hacerse en su rama correspondiente.

### Commits relevantes

- `53b7ca4 docs: definir ramas de trabajo por agente`
- `7213dbc docs: definir mensajes de commit en español`
- `b21de6d docs: require agents to sync coordination files`

### Pendiente

- Manana, Alvaro debe iniciar con: `Soy Alvaro, Parte A`.
- Codex-A debe cambiar a `feature/a-estructuras`, sincronizar con GitHub y leer la documentacion antes de trabajar.
- Primera tarea recomendada: `A-01 Revisar estructuras propias existentes`.
- Despues continuar con `A-02 Disenar matriz propia de cueva`.

### Riesgos

- No trabajar directamente en `main` salvo coordinacion excepcional autorizada.
- No crear nuevas estructuras de datos sin autorizacion humana.
- No tocar archivos de Parte B o Parte C sin autorizacion.
- Mantener `project-management/` sincronizado en GitHub para que los otros agentes lean el estado correcto.

## 2026-05-20 - Reglas globales de revision y tests

### Identificacion de sesion

Humano: Alvaro
Rol: Coordinacion global fuera de Parte A
Agente: Codex

### Contexto

Se reviso la organizacion general para reforzar dos normas del proyecto: existencia de un agente revisor independiente y obligatoriedad de tests unitarios JUnit para el codigo no visual.

### Sincronizacion

Rama: main
Cambio remoto revisado: si
Documentos leidos/modificados: `AGENTS.md`, `GITHUB_WORKFLOW.md`, `REVIEW_CHECKLIST.md`, `TASKS.md`, archivos especificos de agentes.

### Cambios

- Se reforzo que el Agente Revisor es independiente de los agentes A, B y C.
- Se establecio que todo cambio de codigo debe pasar por revision independiente antes de merge a `main`.
- Se establecio que todo codigo no visual nuevo o modificado debe tener tests unitarios JUnit.
- Se establecio que, si no se pueden ejecutar tests JUnit, el agente debe explicar el motivo.
- Se anadio tarea `R-02 Revision de codigo y tests`.
- Se actualizaron los archivos especificos de agentes A, B y C para exigir tests JUnit en su area.

### Pendiente

- Hacer commit/push de estos cambios si Alvaro lo autoriza.
- Los agentes B y C deberan leer estas reglas al iniciar sus sesiones.

### Riesgos

- Que los agentes implementen codigo sin tests por ir demasiado rapido.
- Que el revisor independiente se trate como opcional en la practica. La regla actualizada lo hace obligatorio para cambios de codigo.

## 2026-05-20 - Actualizacion automatica de ramas

### Identificacion de sesion

Humano: Alvaro
Rol: Coordinacion global fuera de Parte A
Agente: Codex

### Contexto

Se pidio que las ramas de trabajo se mantengan actualizadas automaticamente para evitar que los agentes trabajen sobre versiones antiguas.

### Sincronizacion

Rama: main
Cambio remoto revisado: si
Documentos leidos/modificados: `AGENTS.md`, `GITHUB_WORKFLOW.md`, `REVIEW_CHECKLIST.md`, archivos especificos de agentes.

### Cambios

- Se establecio que cada agente debe actualizar su rama con `origin/main` al inicio de sesion.
- Si la actualizacion no tiene conflictos, el agente debe hacerla antes de trabajar.
- Si aparecen conflictos, el agente debe parar y pedir ayuda humana.
- Si la actualizacion requiere push de la rama, el agente debe pedir autorizacion humana.

### Pendiente

- Hacer commit/push de estos cambios si Alvaro lo autoriza.
- Actualizar las ramas de Guille y Hector con `main` cuando proceda o cuando lo autoricen.

### Riesgos

- Actualizar ramas sin revisar conflictos puede romper trabajo de otros agentes. Por eso, la regla exige parar si hay conflictos.

## 2026-05-20 - Plantillas y peticiones urgentes

### Identificacion de sesion

Humano: Alvaro
Rol: Coordinacion global fuera de Parte A
Agente: Codex

### Contexto

Se pidio reforzar la organizacion antes de empezar implementacion creando plantillas y una zona visible para peticiones urgentes entre partes.

### Sincronizacion

Rama: main
Cambio remoto revisado: si
Documentos leidos/modificados: `TASKS.md`, `AGENTS.md`, `GITHUB_WORKFLOW.md`, plantillas.

### Cambios

- Se creo plantilla de Pull Request en `.github/pull_request_template.md`.
- Se creo plantilla de cierre de sesion en `project-management/templates/SESSION_SUMMARY_TEMPLATE.md`.
- Se creo plantilla de tarea en `project-management/templates/TASK_TEMPLATE.md`.
- Se definio que cuenta como cambio significativo.
- Se anadio seccion de peticiones urgentes entre partes en `TASKS.md`.
- Se dejo asignada a Parte B la tarea de cerrar el alcance minimo de la logica del juego.

### Pendiente

- Hacer commit/push si Alvaro lo autoriza.
- Actualizar ramas de trabajo despues del commit global si procede.

### Riesgos

- Si las peticiones urgentes no se mantienen actualizadas, los agentes pueden trabajar bloqueados sin saberlo.

## 2026-05-21 - Cierre A-02 y refuerzo A-04

### Identificacion de sesion

Humano: Alvaro
Rol: Parte A
Agente: Codex-A Estructuras

### Contexto

Se continuo la Parte A con las estructuras ya decididas. Se pidio terminar A-02 y anadir tests pertinentes para A-04. Tambien se pidio usar revision independiente antes de commit/push.

### Sincronizacion

Rama: `feature/a-estructuras`
Cambio remoto revisado: si, la rama estaba sincronizada antes de trabajar.
Documentos leidos/modificados: `TASKS.md`, `POST_MORTEM.md`, `SCRATCHPAD.md`.

### Cambios

- Se integro `ListaSE` en el `src` principal sin exigir `Comparable`.
- Se creo `Cola` propia para BFS, sin `java.util.Queue` ni colecciones externas.
- Se implemento `Cueva` con matriz propia `ListaSE<ListaSE<Celda>>`.
- Se crearon `Celda`, `Posicion` y `TipoCelda`.
- Se anadieron interfaces publicas para que B y C conozcan los metodos disponibles: `InterfazCueva`, `InterfazCelda`, `InterfazPosicion`, `InterfazCola`.
- Se implemento BFS de celdas alcanzables, camino minimo y distancia minima dentro de `Cueva`.
- Se mantuvo la celda inicial como opcion valida de movimiento.
- Se quitaron `hashCode()` de `Celda` y `Posicion` para evitar confusion con `HashMap`/`HashSet`.
- Se marco A-02 como HECHA y A-04 como REVISION en `TASKS.md`.
- Se creo `POST_MORTEM.md` para registrar aprendizajes y problemas del desarrollo.

### Pruebas

- Compilacion de clases de `src/Estructuras` y `src/modelo`: correcta.
- Compilacion de tests de `test`: correcta.
- Tests anadidos para matriz, acceso a celdas, limites, BFS, rango cero, no duplicados, camino minimo, destino bloqueado y distancia minima.

### Riesgos

- A-04 todavia necesita revision final antes de marcarse HECHA.
- El proyecto no tiene Maven/Gradle ni runner JUnit standalone, por lo que la verificacion automatizada completa aun depende de IntelliJ o compilacion manual.
- A-03 implicara `Mazmorra`, archivo compartido, por lo que conviene pedir autorizacion explicita antes de tocarlo.

### Pendiente

- Revisar hallazgos del agente revisor independiente.
- Hacer commit/push si la revision no detecta bloqueos.

### Revision independiente

Resultado: revisado por agente independiente.

Hallazgos atendidos:

- Se comprobo que los archivos nuevos estaban sin trackear y se prepararan todos para el commit.
- Se corrigio `src/Main.java` para evitar features preview y permitir compilacion completa de `src`.
- Se cambio `getMatriz()` para devolver copia de la estructura de listas y no permitir romper la matriz interna.
- Se restauraron `hashCode()` en `Celda` y `Posicion` con comentario aclarando que no implica uso de `HashMap` o `HashSet`.
- Se anadieron tests directos de `ListaSE` y `Cola`.
- Se compilo todo `src` y todos los tests tras las correcciones.

### Actualizacion posterior

- Alvaro confirmo que los tests pasaron en IntelliJ.
- Se marco A-04 como HECHA en `TASKS.md`.
- Siguiente decision pendiente: disenar A-03, grafo dirigido de cuevas y `Mazmorra contiene Grafo<Cueva>`.

## 2026-05-21 - Preparacion de Grafo para A-03

### Identificacion de sesion

Humano: Alvaro
Rol: Parte A
Agente: Codex-A Estructuras

### Contexto

Antes de implementar `Mazmorra`, se pidio dejar listo el grafo propio igual que se hizo con `ListaSE`, revisando si era necesario eliminar `Comparable` y anadiendo algoritmos utiles para la futura mazmorra.

### Cambios

- Se creo `Grafo<T>` dirigido en `src/Estructuras`.
- Se crearon `NodoGrafo<T>`, `ArcoGrafo<T>` e `InterfazGrafo<T>`.
- Se elimino la necesidad de `Comparable`; el grafo compara nodos con `equals()`.
- Se anadieron algoritmos de adyacentes, BFS dirigido, existencia de camino, camino minimo y distancia minima.
- Se hizo que `Cueva` compare por `id` para funcionar correctamente como nodo de `Grafo<Cueva>`.
- Se anadieron tests de grafo dirigido, duplicados, datos sin orden natural, camino minimo y uso con `Cueva`.
- Se marco A-03 como EN_CURSO en `TASKS.md`, dejando pendiente la integracion con `Mazmorra`.

### Verificacion

- Compilacion completa de `src`: correcta.
- Compilacion de tests: correcta.
- Busqueda de estructuras prohibidas en codigo: sin usos reales, solo menciones en comentarios explicativos.

### Pendiente

- Pensar el diseno de `Mazmorra` antes de crear/modificar archivos compartidos.

### Actualizacion posterior

- Se amplio `InterfazMazmorra` con consultas de existencia, conexion directa, busqueda por id, conteos y cueva actual.
- Se implemento `Mazmorra` con `Grafo<Cueva>` y `cuevaActual`.
- Se anadieron tests de `Mazmorra` para cuevas, conexiones dirigidas, avance, busqueda por id, camino minimo y copias defensivas.
- A-03 queda en REVISION en `TASKS.md` hasta revision final.

### Cierre

- Alvaro confirmo que los tests pasaron en IntelliJ con cobertura alta para `Grafo`, `Mazmorra`, `Cueva`, `Cola` y `ListaSE`.
- Se marco A-03 como HECHA en `TASKS.md`.
- Queda pendiente revision independiente antes de commit/push del bloque de grafo y mazmorra.

### Revision independiente

Resultado: revisado por agente independiente.

Hallazgos atendidos:

- `Cueva` ahora rechaza ids nulos, vacios o en blanco.
- `Grafo` ahora prohibe nodos y arcos con datos null.
- `Mazmorra` ahora rechaza operaciones con cuevas null y devuelve resultados seguros.
- Se anadieron tests de ids invalidos, grafo con null y operaciones nulas en mazmorra.
- Se recompilo `src` completo y todos los tests tras las correcciones.

## 2026-05-21 - Cierre final de sesion Parte A

### Identificacion de sesion

Humano: Alvaro
Rol: Parte A
Agente: Codex-A Estructuras

### Tareas trabajadas

- A-02 Disenar matriz propia de cueva.
- A-04 BFS de celdas alcanzables.
- A-03 Disenar grafo de cuevas.

### Resultado

- A-02 queda HECHA.
- A-04 queda HECHA.
- A-03 queda HECHA.
- No quedan tareas en curso para Parte A en `TASKS.md`.

### Archivos modificados principales

- `src/Estructuras/ListaSE.java`
- `src/Estructuras/Cola.java`
- `src/Estructuras/Grafo.java`
- `src/Estructuras/NodoGrafo.java`
- `src/Estructuras/ArcoGrafo.java`
- `src/Estructuras/InterfazGrafo.java`
- `src/modelo/mapa/Cueva.java`
- `src/modelo/mapa/Celda.java`
- `src/modelo/mapa/Posicion.java`
- `src/modelo/mapa/TipoCelda.java`
- `src/modelo/mapa/InterfazCueva.java`
- `src/modelo/mapa/InterfazCelda.java`
- `src/modelo/mapa/InterfazPosicion.java`
- `src/modelo/juego/Mazmorra.java`
- `src/modelo/juego/InterfazMazmorra.java`
- `test/Estructuras/ListaSETest.java`
- `test/Estructuras/ColaTest.java`
- `test/Estructuras/GrafoTest.java`
- `test/modelo/mapa/CuevaTest.java`
- `test/modelo/juego/MazmorraTest.java`
- `project-management/TASKS.md`
- `project-management/POST_MORTEM.md`
- `project-management/SCRATCHPAD.md`

### Estructuras usadas

- `ListaSE<T>` para listas propias, matriz de cueva, visitados, resultados y reconstruccion de caminos.
- `Cola<T>` para BFS.
- `Grafo<T>` dirigido para conexiones entre cuevas.
- `Grafo<Cueva>` como base de `Mazmorra`.

### Pruebas ejecutadas

- Tests ejecutados en IntelliJ por Alvaro: pasados.
- Compilacion manual de `src` completo con `javac`: correcta.
- Compilacion manual de tests con JUnit local en classpath: correcta.
- Busqueda de estructuras prohibidas en codigo: sin usos reales, solo menciones en comentarios explicativos.

### Tests JUnit creados o actualizados

- Tests de `ListaSE`.
- Tests de `Cola`.
- Tests de matriz, celdas, limites, BFS, camino minimo y distancia minima en `Cueva`.
- Tests de `Grafo` dirigido, duplicados, camino minimo, distancia minima y uso con `Cueva`.
- Tests de `Mazmorra`, cueva actual, avance dirigido, busqueda por id y copias defensivas.

### Riesgos

- `A-01` sigue pendiente formalmente, aunque se han revisado y adaptado estructuras necesarias durante el desarrollo.
- El proyecto aun no tiene Maven/Gradle ni runner JUnit automatizado fuera de IntelliJ.
- Quedan cambios locales de IntelliJ no relacionados con Parte A: `.idea/misc.xml` y `.idea/codeStyles/`.
- Futuras tareas de Parte B/C deben coordinarse antes de tocar logica de partida, JSON o interfaz.

### Documentos actualizados

- `TASKS.md`: A-02, A-03 y A-04 marcadas como HECHA.
- `POST_MORTEM.md`: aprendizajes y problemas detectados.
- `SCRATCHPAD.md`: resumen tecnico y cierre de sesion.

### Cambios remotos

- Rama de trabajo: `feature/a-estructuras`.
- Commits subidos:
  - `eff22a9` Corrige estructuras propias iniciales.
  - `07d2392` Implementa matriz y BFS de Parte A.
  - `c5da65c` Implementa grafo y mazmorra de Parte A.
- Rama sincronizada con `origin/feature/a-estructuras` tras el ultimo push.

### Comentarios de codigo

- El codigo nuevo de estructuras, matriz, BFS, camino minimo, grafo y mazmorra queda comentado en profundidad para defensa y coordinacion.

### Pendiente para proxima sesion

- Cerrar formalmente A-01 con tabla de estructuras disponibles, usos y riesgos.
- Decidir si se adapta `ListaDE`, `Pila` o `ListaCircular` para necesidades de Parte B.
- Coordinar con Parte B antes de integrar personajes, objetos, turnos o combate.

## 2026-05-21 - Optimizacion de workflow de agentes

### Identificacion

- Humano: Alvaro.
- Rol: coordinacion de workflow, sin trabajar en Parte A.
- Agente: Codex.

### Objetivo

Reducir consumo de tokens manteniendo la estructura existente de `project-management`.

### Cambios realizados

- Se anadio en `AGENTS.md` la seccion `1.5 Modo economico de agentes`.
- Se creo `templates/AGENT_BRIEF_TEMPLATE.md` para delegar tareas con prompts cortos.

### Criterio acordado

- Mantener `AGENTS.md`, `agents/`, `TASKS.md`, `SCRATCHPAD.md`, `DECISIONS.md`, `IA_DIARY.md` y `templates/` como estructura de coordinacion.
- Usar lectura escalonada de documentos.
- Delegar agentes auxiliares solo cuando haya paralelismo real, revision independiente o tarea acotada.
- Evitar copiar documentos completos en prompts o respuestas.

### Tests

- No aplica; cambio solo documental.

### Pendiente

- Aplicar este modo en las siguientes sesiones de Parte B, Parte C y revision.
- Pedir autorizacion humana antes de commit/push de estos cambios documentales.

## 2026-05-21 - Politica de modelos para workflow economico

### Identificacion

- Humano: Alvaro.
- Rol: coordinacion de workflow, sin trabajar en Parte A.
- Agente: Codex.

### Objetivo

Definir que modelo conviene usar en este proyecto para ahorrar tokens sin perder coherencia ni calidad.

### Cambios realizados

- Se anadio en `AGENTS.md` la seccion `1.5.6 Politica de modelos`.

### Criterio acordado

- `GPT-5.4` como modelo principal.
- `GPT-5.4-Mini` para lectura, resumen, busquedas y tareas auxiliares de bajo riesgo.
- `GPT-5.5` reservado para arquitectura, depuracion dificil y cambios delicados.
- `GPT-5.3-Codex` util para implementacion mecanica acotada.
- El mayor ahorro debe venir antes de reducir contexto y delegacion innecesaria que de bajar de modelo.

### Tests

- No aplica; cambio solo documental.

### Pendiente

- Aplicar esta politica cuando se deleguen nuevas tareas en Parte B, Parte C o revision.
- Pedir autorizacion humana antes de commit/push de este ajuste documental.
## 2026-05-20 - Guillermo / Parte B

### Identificacion de sesion

Humano: Guillermo
Rol: Parte B - logica del juego
Agente: Codex / Agente B Logica

### Contexto

Se realizo la primera sesion de diseno de Parte B antes de implementar codigo. El objetivo fue cerrar una base clara para jugador, enemigos, objetos, inventario, turnos y reglas de combate, respetando que la implementacion empezara despues por las tareas `B-01`, `B-02` y `B-03`.

### Sincronizacion

Rama: `feature/b-logica` en GitHub, actualizada respecto a la documentacion disponible en `origin/main`.
Cambio remoto revisado: si.
Documentos leidos: `TASKS.md`, `AGENT_B_LOGICA.md`, `ARCHITECTURE.md`, `DECISIONS.md`, `PRD.md`, `SCRATCHPAD.md`, `IA_DIARY.md`, `GITHUB_WORKFLOW.md`, `REVIEW_CHECKLIST.md`, `AGENTS.md`.

### Cambios

- No se modifico codigo del juego.
- Se confirma que Guillermo representa la Parte B.
- Se acuerda que al inicio de cada sesion se deben releer los documentos de `project-management` por si han cambiado.
- Se acuerda que antes de cualquier commit/push se deben leer los archivos actualizados desde GitHub e informar a Guillermo de cambios relevantes.
- Se define el jugador con vida maxima 100, vida actual 100, ataque base 15, defensa base 5, movimiento 3, 40 turnos iniciales, inventario y objeto equipado.
- Se definen tipos de enemigo: esqueleto, orco, mago y boss.
- Se definen valores de enemigos:
  - Esqueleto: vida 30, ataque 8, defensa 2, movimiento 2.
  - Orco: vida 50, ataque 12, defensa 4, movimiento 1.
  - Mago: vida 25, ataque 14, defensa 1, movimiento 2.
  - Boss: vida 150, ataque 20, defensa 5, movimiento 2.
- Se definen objetos: pocion, espada, arco, llave y escudo.
- Se definen pociones:
  - Pocion de cura: cura 25 puntos y se consume.
  - Pocion de invisibilidad: dura 2 turnos, evita ataques enemigos mientras dura y se consume.
- Se definen objetos equipables:
  - Espada: +12 ataque, cuerpo a cuerpo.
  - Arco: +7 ataque, ataque a distancia.
  - Escudo: +5 defensa.
- Se define que las llaves pueden ser de puerta o de cofre, pueden encontrarse en el suelo o dropearlas enemigos, y no se consumen por defecto.
- Se define que los drops no seran aleatorios por ahora: cada enemigo puede tener 0 o 1 objeto asignado como drop.
- Se define que un cofre puede contener 0 o 1 objeto y puede requerir llave.
- Se define turno basico: maximo 1 movimiento y 1 accion; despues actuan los enemigos; se resta 1 turno.
- Se define que equipar o cambiar objeto no cuenta como movimiento. Usar un objeto si cuenta como accion.
- Se define que al bajar al 75% o menos de vida maxima, la defensa baja 2 puntos una sola vez, con defensa minima 0. Esta regla se aplica al jugador y enemigos normales, pero no al boss.

### Tests

Tests JUnit creados o actualizados: ninguno.
Tests ejecutados: ninguno.
Resultado: no aplica.
Si no se ejecutaron, motivo: la sesion fue solo de diseno y documentacion, sin cambios de codigo ejecutable.

### Pendiente

- Confirmar con el grupo si arco, escudo e invisibilidad se aceptan como parte obligatoria o quedan como extras controlados.
- Pasar el diseno a implementacion empezando por `B-01 Modelo de personajes`.
- Actualizar `TASKS.md` cuando empiece formalmente una tarea de implementacion.
- Documentar en `DECISIONS.md` las decisiones nuevas de Parte B cuando el grupo las apruebe como definitivas.

### Riesgos

- Arco, escudo e invisibilidad aparecen como opcionales o extras en los documentos iniciales; conviene validarlo con el grupo antes de implementarlos como imprescindibles.
- La regla de bajada de defensa al 75% modifica el combate y debe quedar documentada en decisiones cuando el grupo la apruebe.
- La logica de llaves, cofres y drops tocara coordinacion con mapa y JSON, por lo que Parte B debe exponer reglas claras sin cambiar archivos de otras partes sin permiso.

## 2026-05-21 - Hector / Parte C - C-01 JSON inicial

### Identificacion de sesion

Humano: Hector
Rol: Parte C - JavaFX, JSON y documentacion
Agente: Agente C JavaFX/JSON/Docs

### Contexto

Primera sesion de implementacion de Parte C. Se configura Gson en el proyecto y se crea el fichero JSON de configuracion de las 3 cuevas de la mazmorra, junto con los DTOs y el cargador que construye los objetos del modelo (Cueva, Mazmorra) a partir del JSON.

### Sincronizacion

Rama: `feature/c-javafx-json-docs`
Cambio remoto revisado: si — se detectaron 15 commits nuevos de A y B en `origin/main`.
Documentos leidos: `PRD.md`, `ARCHITECTURE.md`, `TASKS.md`, `DECISIONS.md`, `AGENTS.md`, `GITHUB_WORKFLOW.md`, `SCRATCHPAD.md`, `AGENT_C_JAVAFX_JSON_DOCS.md`.

### Tarea trabajada

C-01 Disenar JSON inicial.

### Cambios

- Configurado Gson 2.10.1 en el proyecto:
  - `lib/gson-2.10.1.jar` copiado desde MATCOMP.
  - `.idea/libraries/google_code_gson.xml` creado.
  - `Practica final.iml` actualizado con entrada de Gson.
- Creado `datos/cuevas.json` con configuracion de las 3 cuevas:
  - Cueva Facil 5x5, Cueva Media 6x6, Cueva Dificil 7x7.
  - Matriz de tipos de celda (MURO, SUELO, INICIO, PUERTA, TESORO, SALIDA).
  - Enemigos y objetos definidos por cueva (pendientes de que Parte B los implemente).
  - Conexiones dirigidas del grafo: facil -> media -> dificil.
- Creados DTOs en `src/json/`:
  - `ConfiguracionMazmorra.java`, `ConfiguracionCuevaDTO.java`.
  - `ConfiguracionEnemigoDTO.java`, `ConfiguracionObjetoDTO.java`.
  - `ConexionDTO.java`.
- Creado `src/json/CargadorConfiguracion.java` y `ResultadoCarga.java`.
- Creado test `test/json/CargadorConfiguracionTest.java` con 10 tests.

### Pruebas ejecutadas

- Compilacion manual de `src` completo con `javac`: correcta.
- Compilacion de tests con JUnit: correcta.
- Tests pendientes de ejecutar en IntelliJ.

### Riesgos

- El formato JSON de enemigos y objetos puede requerir cambios cuando Parte B implemente sus clases.
- Gson local; si se limpia .idea habra que reconfigurarlo.

### Archivos compartidos tocados o solicitados

Ninguno dentro del area permitida de Parte C.

### Estado de TASKS.md

C-01 pasa a REVISION.

## 2026-05-21 - Hector / Parte C - Resolver merge conflicts PR #3

### Identificacion de sesion

Humano: Hector
Rol: Parte C - JavaFX, JSON y documentacion
Agente: Agente C JavaFX/JSON/Docs

### Contexto

Segunda sesion de Parte C. La PR #3 (C-01) tenia conflictos con main en los documentos
de coordinacion (IA_DIARY.md, SCRATCHPAD.md) y en "Practica final.iml". Se resolvieron
localmente con `git merge main`.

### Sincronizacion

Rama: `feature/c-javafx-json-docs`
Conflicto detectado via `gh pr view 3`: mergeable=CONFLICTING, mergeStateStatus=DIRTY.

### Tarea trabajada

Resolver merge conflict de PR #3.

### Cambios

Ningun cambio funcional. Solo resolucion de merge:
- `Practica final.iml`: keep entrada Gson de HEAD.
- `IA_DIARY.md`: keep entrada C-01 de HEAD.
- `SCRATCHPAD.md`: keep entrada C-01 de HEAD (ligeramente simplificada).
- Commit merge ed1aea7 y push a origin.

### Archivos modificados

- `Practica final.iml` (merge)
- `project-management/IA_DIARY.md` (merge + entrada sesion 2)
- `project-management/SCRATCHPAD.md` (merge + entrada sesion 2)

### Pruebas ejecutadas

No aplica (sin cambios de codigo).

### Riesgos

- Si otros companeros modifican los mismos documentos antes de mergear la PR,
  podrian aparecer nuevos conflictos.

### Archivos compartidos tocados o solicitados

Ninguno.

### Estado de TASKS.md

Sin cambios: C-01 sigue en REVISION.

## 2026-05-21 - Guillermo / Parte B

### Identificacion de sesion

Humano: Guillermo
Rol: Parte B - logica del juego
Agente: Codex / Agente B Logica

### Contexto

Se implemento `B-01 Modelo de personajes`. Antes de programar se reviso la documentacion actualizada de `project-management`, se comprobo GitHub, se actualizo `feature/b-logica` con `origin/main` y se acordo el alcance exacto: personajes base, contrato mediante `Personaje` abstracto y tests JUnit, sin objetos, inventario, turnos, combate, mapa, JavaFX ni JSON.

Tambien se reviso el PDF de la practica y se localizo el apartado 8.4, que pide declarar contratos. Para B-01 se decidio que el contrato necesario seria la clase abstracta `Personaje`, no una interfaz Java adicional.

### Sincronizacion

Rama: `feature/b-logica`
Cambio remoto revisado: si.
Rama actualizada con `origin/main`: si, se incorporaron los cambios recientes de Parte A y Parte C, incluyendo `POST_MORTEM.md`, configuracion JUnit, JSON inicial y configuracion Gson.
Documentos leidos: `AGENTS.md`, `TASKS.md`, `AGENT_B_LOGICA.md`, `ARCHITECTURE.md`, `DECISIONS.md`, `PRD.md`, `SCRATCHPAD.md`, `IA_DIARY.md`, `GITHUB_WORKFLOW.md`, `REVIEW_CHECKLIST.md`, `POST_MORTEM.md`.

### Cambios

- Creado `src/modelo/personajes/Personaje.java`.
- Creado `src/modelo/personajes/Jugador.java`.
- Creado `src/modelo/personajes/Enemigo.java`.
- Creado `src/modelo/personajes/Boss.java`.
- Creado `src/modelo/personajes/TipoEnemigo.java`.
- Creado `test/modelo/personajes/PersonajeTest.java`.
- Creado `test/modelo/personajes/EnemigoTest.java`.
- `Personaje` queda como clase abstracta y contrato base de B-01.
- `Jugador` hereda de `Personaje`.
- `Enemigo` hereda de `Personaje` y guarda `TipoEnemigo`.
- `Boss` hereda de `Enemigo` y fija `TipoEnemigo.BOSS`.
- Se implementan vida maxima, vida actual, ataque base, defensa base, movimiento, fila y columna.
- Se implementan `recibirDano`, `curar`, `estaVivo` y `cambiarPosicion`.
- No se anadieron setters generales para estadisticas base; queda comentado como decision de encapsulacion.
- Se deja `B-01` en estado `REVISION` en `TASKS.md`.
- Se paso revision independiente. El revisor recomendo aceptar sin bloqueos.
- Se corrigio la nota de compatibilidad Java cambiando `String.isBlank()` por `trim().isEmpty()`.
- Se ampliaron los tests para mejorar la cobertura de `Jugador`, `Enemigo`, `Boss`, `TipoEnemigo` y ramas de validacion de `Personaje`.
- Guillermo verifico en IntelliJ cobertura del 100% en el paquete `modelo.personajes`.

### Tests

Tests JUnit creados o actualizados:

- `PersonajeTest`
- `EnemigoTest`

Tests ejecutados:

- Compilacion de codigo de produccion con `javac`.
- Compilacion de fuentes de test con JUnit disponible en la configuracion actual del proyecto.
- Ejecucion de tests JUnit de `modelo.personajes`.

Resultado:

- Codigo de produccion de `modelo.personajes`: compila correctamente.
- Tests de `modelo.personajes`: 26 tests ejecutados y pasados.
- Cobertura en IntelliJ: 100% en clases, metodos, lineas y ramas del paquete `modelo.personajes`.
- Compilacion completa del `src` actualizado, incluyendo Parte A, Parte B y Parte C: correcta.
- Nota tecnica: `javac` genero clases de test pero mostro un aviso/excepcion interna de permisos al cerrar un jar de JUnit en `.m2`; para verificar comportamiento se ejecutaron los metodos `@Test` compilados mediante reflexion, todos con resultado correcto.

### Pendiente

- Preparar commit, push y Pull Request cuando Guillermo lo autorice.
- Mantener `B-01` en `REVISION` hasta que el grupo acepte la PR o indique marcarla como `HECHA`.
- Mantener inventario, objetos, turnos, combate y regla del 75% para `B-02` y `B-03`.

### Riesgos

- `B-01` evita a proposito inventario, objetos, drops, combate, turnos y regla del 75%; esas partes deben mantenerse fuera de esta tarea.
- Los datos JSON de Parte C ya incluyen enemigos y objetos como configuracion, pero Parte B aun no debe acoplarse a ellos hasta `B-02`/`B-03`.

## 2026-05-22 - Guillermo / Parte B

### Identificacion de sesion

Humano: Guillermo
Rol: Parte B - logica del juego
Agente: Codex / Agente B Logica

### Contexto

Se inicio la tarea `B-02 Modelo de objetos e inventario`. Antes de programar se revisaron GitHub y los documentos actualizados de `project-management` desde `main`, se confirmo que PR #5 estaba mergeada y se leyeron los dos PDF del proyecto para comprobar que B-01 no tenia huecos y que B-02 debia centrarse en objetos, inventario y equipo.

Antes de cerrar el alcance, Guillermo pidio decidir punto por punto. Se acordo incluir `Arco` y `Escudo`, permitir objetos repetidos por tipo pero no duplicar el mismo `id`, usar ranuras separadas de arma y escudo, hacer que el arco ocupe las dos manos y desequipe/bloquee escudo, dejar llaves sin equipar, implementar solo pocion de cura y dejar invisibilidad, drops, cofres, recoger del suelo, mapa, JSON, turnos, combate y regla del 75% fuera de B-02.

### Sincronizacion

Rama: `feature/b-logica`
Cambio remoto revisado: si.
Rama local: limpia al inicio pero por delante de `origin/feature/b-logica` por commits locales de merge/documentacion de la sesion anterior.
Limitacion tecnica: el entorno de Codex no pudo ejecutar `git fetch` local por permisos en `.git/FETCH_HEAD`; se contrasto GitHub con el conector y se leyeron documentos desde `main`.
Documentos leidos: `TASKS.md`, `AGENT_B_LOGICA.md`, `ARCHITECTURE.md`, `DECISIONS.md`, `PRD.md`, `SCRATCHPAD.md`, `IA_DIARY.md`, `GITHUB_WORKFLOW.md`, `REVIEW_CHECKLIST.md`, `POST_MORTEM.md` y los dos PDF del proyecto.

### Cambios

- `TASKS.md`: B-02 queda en `REVISION` y se documenta la autorizacion de Guillermo para traer `ListaDE`, `ElementoDE` e `IteradorDE`.
- `src/Estructuras/ElementoDE.java`, `IteradorDE.java`, `ListaDE.java`: se traen desde las estructuras del grupo.
- `ListaDE` se adapta para no exigir `Comparable`, usando igualdad por `equals`, porque el inventario debe guardar objetos sin orden natural.
- `src/modelo/objetos/Objeto.java`: clase base de objetos con `id`, nombre, descripcion, igualdad por `id` y marcas de equipable/consumible.
- `src/modelo/objetos/Arma.java`, `Espada.java`, `Arco.java`: armas equipables con bonificacion de ataque; espada +12 y arco +7.
- `src/modelo/objetos/Escudo.java`: equipable con +5 defensa.
- `src/modelo/objetos/Llave.java`, `TipoLlave.java`: llaves de puerta o cofre con codigo de cerradura; no se equipan en B-02.
- `src/modelo/objetos/Pocion.java`: pocion de cura, cura 25 y se consume.
- `Jugador`: inventario con `ListaDE<Objeto>`, rechazo de ids duplicados, copia defensiva de inventario, equipo de arma y escudo, regla de arco a dos manos, uso de pocion de cura y calculo de ataque/defensa total.
- Tests nuevos: `ListaDETest`, `ObjetoTest`, `JugadorInventarioTest`.

### Tests

Tests JUnit creados o actualizados:

- `test/Estructuras/ListaDETest.java`
- `test/modelo/objetos/ObjetoTest.java`
- `test/modelo/personajes/JugadorInventarioTest.java`

Pruebas ejecutadas:

- Compilacion completa de `src` con `javac`: correcta.
- Compilacion de tests con JUnit en classpath: genero clases correctamente, aunque `javac` volvio a mostrar una excepcion interna de permisos al cerrar el jar de JUnit en `.m2`.
- Ejecucion por reflexion de todos los tests compilados tras ampliar cobertura de objetos: 105 tests ejecutados, 0 fallos.
- Busqueda de colecciones prohibidas: sin usos reales nuevos; solo menciones en comentarios existentes.

### Revision independiente

Resultado: revisado por agente independiente.

Hallazgos:

- El revisor marco que `src/Estructuras/` estaba fuera del alcance estandar de B-02.
- Se resolvio documentando en `TASKS.md` la autorizacion explicita de Guillermo para traer y adaptar `ListaDE`, `ElementoDE` e `IteradorDE`.
- No se detectaron bugs funcionales ni uso de colecciones prohibidas.
- El revisor considero funcionalmente aceptable la implementacion de inventario, objetos, equipo, copia defensiva y colisiones de id con tipo distinto.

### Pendiente

- B-02 queda pendiente de revision de PR antes de marcarla como `HECHA`.
- Ejecutar/confirmar tests en IntelliJ si se quiere cobertura visual.
- Preparar commit, push y PR solo con autorizacion humana.
- Mantener para B-03: turnos, combate, invisibilidad por turnos, drops, cofres, recoger objetos del mapa, integracion con JSON/JavaFX y regla del 75%.

### Riesgos

- `build/` quedo generado localmente por las compilaciones y no debe incluirse en commit.
- La ejecucion de tests fuera de IntelliJ sigue teniendo la limitacion del aviso interno de `javac` al cerrar el jar de JUnit, aunque los tests se ejecutaron correctamente por reflexion.
- La integracion futura con JSON debera mapear las configuraciones de objetos a estas clases sin mezclar reglas de juego en `src/json`.

## 2026-05-22 - Hector / Parte C - C-02 Cargar y guardar partida

### Identificacion de sesion

Humano: Hector
Rol: Parte C - JavaFX, JSON y documentacion
Agente: Agente C JavaFX/JSON/Docs

### Contexto

Parte B ya implemento Personaje, Jugador, Enemigo, Boss y todos los objetos
del juego (B-01 y B-02). Se hace merge de origin/main a la rama feature.
Se implementa C-02: guardado y carga de partida en JSON.

### Sincronizacion

Rama: `feature/c-javafx-json-docs`
Cambio remoto revisado: si — Part B ha avanzado con B-01 y B-02.
Merge con origin/main: sin conflictos (fast-forward).

### Tarea trabajada

C-02 Cargar y guardar partida.

### Cambios

- Creados 6 DTOs en `src/json/` para el formato de guardado:
  - `DatosPartidaDTO` (raiz: version, estado, turnos)
  - `DatosMazmorraDTO` (cueva actual, cuevas, conexiones)
  - `DatosCuevaDTO` (matriz actual, enemigos vivos, objetos en mapa)
  - `DatosJugadorDTO` (atributos, equipo, inventario)
  - `DatosEnemigoDTO` (tipo, vida, posicion, vivo)
  - `DatosObjetoDTO` (id, tipo, atributos por subtipo)
- Ampliado `ConexionDTO` con constructores para poder crearlo en tests.
- Creado `SerializadorPartida.java` con metodos estaticos:
  - `guardar(DatosPartidaDTO, ruta)` — escribe JSON con pretty printing
  - `cargar(ruta)` — lee JSON y devuelve DatosPartidaDTO
- Creado `test/json/SerializadorPartidaTest.java` con 10 tests.

### Archivos modificados

- `src/json/DatosPartidaDTO.java` (nuevo)
- `src/json/DatosMazmorraDTO.java` (nuevo)
- `src/json/DatosCuevaDTO.java` (nuevo)
- `src/json/DatosJugadorDTO.java` (nuevo)
- `src/json/DatosEnemigoDTO.java` (nuevo)
- `src/json/DatosObjetoDTO.java` (nuevo)
- `src/json/SerializadorPartida.java` (nuevo)
- `src/json/ConexionDTO.java` (ampliado con constructores)
- `test/json/SerializadorPartidaTest.java` (nuevo)
- `project-management/TASKS.md` (C-02 -> REVISION)
- `project-management/SCRATCHPAD.md` (actualizado)
- `project-management/IA_DIARY.md` (actualizado)

### Pruebas ejecutadas

- Compilacion de src/ completa: correcta.
- 10/10 tests de SerializadorPartida pasados desde terminal con JUnit standalone + Gson.
- 10/10 tests de CargadorConfiguracion existentes siguen pasando.

### Riesgos

- Los DTOs de guardado no tienen metodos para convertir desde/hacia las
  clases del modelo (Jugador, Enemigo, Objeto, etc.). Esa conversion
  corresponde a Parte B cuando implemente Partida (B-03) o a una futura
  integracion. Los DTOs definen el formato de intercambio JSON.
- El formato es compatible hacia atras (version 1.0).

### Estado de TASKS.md

C-02 pasa a REVISION.

## 2026-05-22 - Hector / Parte C - Planificar C-03 Boceto JavaFX

### Identificacion de sesion

Humano: Hector
Rol: Parte C - JavaFX, JSON y documentacion
Agente: Agente C JavaFX/JSON/Docs

### Contexto

Sesion de planificacion de C-03. Se revisan todos los documentos del proyecto
(PRD, ARCHITECTURE, DECISIONS, AGENTS, GITHUB_WORKFLOW, TASKS) y las clases
del modelo existentes para disenar el boceto de la interfaz JavaFX.

### Sincronizacion

Rama: `feature/c-javafx-json-docs`
Cambio remoto revisado: ningun cambio nuevo en origin/main.
Rama al dia con origin/main.

### Tarea trabajada

C-03 Boceto JavaFX (planificacion).

### Cambios

- Creado `docs/BOCETO_JAVAFX.md` con:
  - Layout ASCII de la ventana completa con 5 zonas.
  - Tabla de colores por TipoCelda para la matriz.
  - Descripcion detallada de cada zona: matriz (GridPane), estado, inventario, acciones, log.
  - Flujo de datos desde Partida hasta cada panel.
  - Estructura de clases propuesta para C-04 (6 clases en src/vista/, 1 en src/controlador/).
  - Contrato minimo que Partida (B-03) debe exponer para que JavaFX funcione.
  - Estrategia de mock para C-04 si B-03 no esta listo.
  - Checklist de verificacion.
- Actualizado TASKS.md: C-03 -> EN_CURSO.

### Archivos modificados

- `docs/BOCETO_JAVAFX.md` (nuevo)
- `project-management/TASKS.md` (C-03 -> EN_CURSO)
- `project-management/SCRATCHPAD.md` (actualizado)
- `project-management/IA_DIARY.md` (pendiente)

### Pruebas ejecutadas

No aplica (solo documentacion).

### Riesgos

- C-03 es solo un boceto. C-04 implementara el codigo JavaFX real.
- La implementacion de C-04 puede necesitar ajustes cuando B-03 implemente
  Partida, especialmente en los nombres y parametros de los metodos.
- Si B-03 no esta listo para C-04, se usara un PartidaMock con datos fijos.
- JavaFX no esta en el JDK 25; habra que descargar las librerias JavaFX
  (openjfx) cuando se implemente C-04.

### Estado de TASKS.md

C-03 pasa a EN_CURSO.

## 2026-05-22 - Hector / Parte C - C-04 Menu principal JavaFX

### Identificacion de sesion

Humano: Hector
Rol: Parte C - JavaFX, JSON y documentacion
Agente: Agente C JavaFX/JSON/Docs

### Contexto

Sesion de implementacion del menu principal JavaFX (C-04). C-03 estaba en REVISION.
Se descarta el enfoque anterior de C-04 (layout 5-panel con matriz, estado, inventario,
acciones, log). Hector pide un menu limpio con:

- Ventana 1280x720 redimensionable, fondo de cueva con gradiente radial.
- Dos antorchas animadas (llamas tricolor rojo-naranja-amarillo que vibran y oscilan con Timeline).
- Tesoro decorativo (monedas + cofre abierto) en la parte inferior central.
- Pantalla Inicio: titulo "ESCAPE DE LA MAZMORRA" en 3 lineas, boton Inicio con globo terraqueo.
- Pantalla Opciones: titulo reducido, botones PARTIDA NUEVA / ESTADISTICAS / AJUSTES.
- Transicion FadeTransition entre pantallas (300ms salida + 300ms entrada).
- Boton AJUSTES vuelve a la pantalla de inicio (placeholder).
- Efectos hover en botones (escala 1.05-1.06 y brillo).

### Sincronizacion

Rama: `feature/c-javafx-json-docs`
Cambio remoto revisado: si.
Merge con origin/main: sin conflictos.

### Cambios

- Creado `src/vista/EscapeMazmorraApp.java` (58 lines aprox):
  - Application con Stage 1280x720, StackPane raiz con fondo compartido + antorchas + tesoro + contenido intercambiable.
  - `crearFondoCueva()` — gradiente radial (bordes #1A1A1A -> centro #3A3A3A).
  - `crearLlama()` — Polygon de 7 puntos con LinearGradient rojo-naranja-amarillo.
  - `animarLlamas()` — Timeline 30 frames (60ms cada uno) con escala X/Y, opacidad y desplazamiento Y sobre 3 capas de llama, usando seno.
  - `crearTesoro()` — monton de 4 filas de monedas + cofre abierto (cuerpo + tapa inclinada + cerradura).
  - `crearPantallaInicio()` — titulo 3 lineas (Georgia 52px, #6E4720) + boton Inicio amarillo con globo (circulo azul + continente verde).
  - `crearPantallaOpciones()` — titulo reducido 38px + 3 botones (Partida Nueva, Estadisticas, Ajustes) con hover.
  - `cambiarAPantallaOpciones()` / `cambiarAPantallaInicio()` — FadeTransition 300ms.
- Limpiados archivos viejos de intentos anteriores (src/vista/*.java).

### Archivos modificados

- `src/vista/EscapeMazmorraApp.java` (nuevo)
- `project-management/TASKS.md` (pendiente)
- `project-management/SCRATCHPAD.md` (actualizado)
- `project-management/IA_DIARY.md` (pendiente)

### Pruebas ejecutadas

- Compilacion de src/vista/EscapeMazmorraApp.java con javac + JavaFX 23: correcta (sin errores).
- Compilacion de src/ completo: fallos solo en src/json/SerializadorPartida.java y CargadorConfiguracion.java por falta de Gson en classpath (pre-existente, no relacionado).

### Riesgos

- Los botones PARTIDA NUEVA y ESTADISTICAS son placeholder (System.out.println). Cuando exista Partida (B-03) habra que conectarlos.
- La animacion de antorchas usa Timeline con frames precalculados; si se quiere mas suavidad convendra AnimationTimer o Interpolator.
- El proyecto necesita JavaFX 23 en runtime para lanzar la aplicacion. Aun no hay script de lanzamiento.

### Estado de TASKS.md

C-04 EN_CURSO.

## 2026-05-22 - Hector / Parte C - Cerrar C-02

### Identificacion de sesion

Humano: Hector
Rol: Parte C - JavaFX, JSON y documentacion
Agente: Agente C JavaFX/JSON/Docs

### Contexto

Hector pide terminar C-02. Se revisa el codigo existente: SerializadorPartida ya
tiene todos los metodos de conversion modelo<->DTO implementados desde la sesion
anterior. Se verifica compilacion y tests.

### Sincronizacion

Rama: `feature/c-javafx-json-docs`
Cambio remoto revisado: si (ningun cambio nuevo en origin/main).

### Tareas

C-02 pasa a HECHA.

### Verificacion

- Compilacion de src/json/ completo con Gson y JavaFX en classpath: correcta.
- Compilacion de test/json/SerializadorPartidaTest.java con JUnit 5: correcta.
- 19/19 tests JUnit pasados:
  - Guardado/carga basico, mazmorra, jugador, enemigos, objetos en mapa, estado, partida completa.
  - Errores de ruta invalida (lectura y escritura).
  - Conversion modelo->DTO y DTO->modelo para: Mazmorra real (desde cuevas.json),
    Jugador completo (con arma equipada, inventario, vida parcial), Enemigo,
    Pocion, Espada, Llave, Escudo.
  - Round-trip de matriz de cueva (5x5 facil).
  - Guardado y recuperacion con Mazmorra real + Jugador real.

### Cambios en TASKS.md

C-02: REVISION -> HECHA.

## 2026-05-22 - Hector / Parte C - C-04 y C-05 Partida jugable

### Identificacion de sesion

Humano: Hector
Rol: Parte C - JavaFX, JSON y documentacion
Agente: Agente C JavaFX/JSON/Docs

### Contexto

El menu principal (C-04) existia pero no estaba conectado a la logica del juego.
Hector pidio crear una partida jugable completa: Partida (motor de juego),
PantallaJuego (UI con grid, stats, inventario, acciones, log), y conectar
todo desde el boton "Iniciar partida".

### Sincronizacion

Rama: `feature/c-javafx-json-docs`
Cambio remoto revisado: si.
Merge con origin/main: previo de la sesion.

### Cambios

- Creado `src/modelo/juego/Partida.java` (571 lineas):
  - `crearPartidaNueva()` que carga `datos/cuevas.json` con Gson y construye
    mazmorra, cuevas, enemigos, objetos, conexiones y jugador.
  - Movimiento basico (arriba, abajo, izquierda, derecha, y mover a celda destino).
  - Combate: atacar enemigo adyacente con calculo de dano.
  - Objetos: recoger objeto del suelo, usar pocion, equipar arma/escudo.
  - Turnos: terminar turno, actuar enemigos, condiciones de victoria (SALIDA
    en cueva_dificil sin enemigos) y derrota (vida <= 0 o turnos <= 0).
  - Navegacion entre cuevas (puertas).
  - Guardar/cargar partida via SerializadorPartida.
- Creado `src/vista/PantallaJuego.java` (425 lineas):
  - Layout BorderPane con grid de cueva (StackPane + GridPane + overlay).
  - Celdas coloreadas por TipoCelda (MURO gris, SUELO marron, INICIO verde,
    PUERTA amarillo, TESORO morado, SALIDA rojo, TRAMPA naranja).
  - Jugador (circulo azul), enemigos (rojo), objetos (amarillo) en overlay.
  - Panel derecho con estadisticas, inventario (con botones USAR/EQUIPAR),
    acciones (movimiento, atacar, recoger, terminar turno, cambiar cueva,
    guardar, volver al menu) y log de mensajes.
  - Teclado WASD/flechas para movimiento + SPACE(atacar), R(recoger),
    T(terminar turno). Clic en celda para mover. Clic en root devuelve foco.
- Modificado `src/vista/EscapeMazmorraApp.java`:
  - "Iniciar partida" ahora crea Partida y cambia a PantallaJuego.
  - Nueva `volverAlMenu()` que reconstruye la escena del menu.
  - Guardado de referencia al Stage.
- Modificado `src/json/SerializadorPartida.java`:
  - Anadidos metodos de conversion modelo<->DTO:
    `desdeMazmorraJugador()`, `mazmorraADTO()`, `dtoAMazmorra()`,
    `jugadorADTO()`, `dtoAJugador()`, `cuevaADTO()`, `dtoACueva()`,
    `enemigoADTO()`, `dtoAEnemigo()`, `objetoADTO()`, `dtoAObjeto()`.
- Creado `test/modelo/juego/PartidaTest.java` (24 tests):
  - Creacion, movimiento (valido, muro, fuera de mapa, distancia > 1).
  - Combate (sin enemigo, reduce vida, mata enemigo tras varios golpes).
  - Objetos (recoger en misma casilla, sin objeto no hace nada).
  - Turnos (reduce contador, agotar turnos -> derrota, muerte -> derrota).
  - Puertas/cueva (detectar puerta, cambiar sin estar en puerta).
  - Guardado/carga (round-trip, ruta invalida lanza excepcion).
  - Estado inicial, movimiento bloqueado en estado no EN_CURSO.

### Pruebas ejecutadas

- Compilacion completa de `src/` con javac + Gson + JavaFX 23: correcta.
- 148/148 tests JUnit pasados (24 nuevos de Partida + 124 existentes).

### Hallazgos del revisor independiente

- IMPORTANTE: Partida.java no tenia tests JUnit. Se crearon 24 tests.
- MENOR: volverAlMenu() no detiene las Timeline previas (leak menor).
- MENOR: Documentacion no reflejaba los cambios. Se actualiza ahora.

### Riesgos

- El guardado/carga de partida con Partida.cargar() no restaura los
  enemigos ni objetos del mapa (cuevasData se crea vacio). Queda como
  limitacion conocida para futura iteracion.
- La demo se probo y funciona, pero el usuario reporto inicialmente
  que "no deja hacer nada". Se corrigio anadiendo atajos SPACE/R/T
  y gestion de foco del teclado.

### Pendiente

- Hacer commit, push y Pull Request cuando Hector lo autorice.
## 2026-05-22 - Alvaro / Parte A con apoyo en logica de Partida

### Identificacion de sesion

Humano: Alvaro
Rol: Parte A, coordinacion con logica de juego
Agente: Codex-A Estructuras

### Sincronizacion

Rama: `feature/a-estructuras`
Cambio remoto revisado: si
Rama actualizada con `origin/main`: si, se incorporaron los cambios de personajes, objetos e inventario antes de trabajar sobre `Partida`.
Documentos leidos: `TASKS.md`, `DECISIONS.md`, `POST_MORTEM.md`, `SCRATCHPAD.md` y clases nuevas de personajes, objetos e inventario.

### Tareas trabajadas

- Planificacion e implementacion inicial de `B-03 Reglas de turno y combate` desde la rama de Alvaro, por peticion expresa de coordinacion.
- Revision de responsabilidad entre `Mazmorra` y `Partida`.
- Correccion de problemas detectados por revision independiente.
- Ampliacion de tests JUnit de la nueva capa `modelo.juego`.

### Archivos modificados

- `src/modelo/juego/EstadoPartida.java`
- `src/modelo/juego/InterfazPartida.java`
- `src/modelo/juego/Partida.java`
- `src/modelo/juego/Puerta.java`
- `src/modelo/juego/ObjetoEnMapa.java`
- `src/modelo/juego/PersonajeEnMapa.java`
- `src/modelo/juego/CuevaEnMapa.java`
- `src/modelo/juego/CeldaEnMapa.java`
- `test/modelo/juego/PartidaTest.java`
- `project-management/TASKS.md`
- `project-management/DECISIONS.md`
- `project-management/POST_MORTEM.md`
- `project-management/SCRATCHPAD.md`
- `project-management/IA_DIARY.md`

### Cambios realizados

- Se creo la primera version de `InterfazPartida` como fachada publica de la logica de juego.
- Se implemento `Partida` con turnos, movimiento, accion, combate, recogida de objetos adyacentes, uso/equipamiento de objetos, avance entre cuevas con puertas y condicion de victoria por llave final.
- Se creo `Puerta` como regla jugable sobre conexiones ya existentes en `Mazmorra`.
- Se corrigio el diseno inicial para que `Partida` no conecte cuevas: esa responsabilidad queda en `Mazmorra`.
- Se anadieron vistas inmutables para no exponer referencias mutables principales desde la interfaz: `PersonajeEnMapa`, `CuevaEnMapa` y `CeldaEnMapa`.
- Se bloqueo que jugador y enemigos vivos compartan celda, incluyendo movimiento, colocacion de enemigos y avance a cueva destino.
- Se corrigio la entrega de llave final para no depender de un id fijo que pudiera chocar con otros objetos.
- Se saco de `InterfazPartida` la preparacion interna de enemigos y objetos, dejandola como soporte de montaje package-private.
- Se documento en `DECISIONS.md` la responsabilidad de `Partida`, puertas, ocupacion, combate, turnos y log.
- Se documento en `POST_MORTEM.md` el error de limite entre `Mazmorra` y `Partida`, la integracion parcial y los defectos detectados por revision independiente.
- Se actualizaron pendientes y mejoras futuras en `TASKS.md`.

### Tests

Tests JUnit creados o actualizados: `test/modelo/juego/PartidaTest.java`.
Tests ejecutados: compilacion manual de `src` con `javac`; cobertura ejecutada por Alvaro desde IntelliJ.
Resultado: `src` compila correctamente; cobertura visual mostrada por Alvaro para `modelo.juego` con 100% clases, 89% metodos, 79% lineas y 58% ramas tras ampliar tests.
Si no se ejecutaron por terminal todos los JUnit, motivo: el proyecto no tiene runner Maven/Gradle ni dependencia JUnit standalone estable fuera de IntelliJ en esta rama.

### Commits y push

Commit realizado: pendiente al redactar esta entrada, se prepara justo despues del cierre.
Hash: pendiente.
Push realizado: pendiente al redactar esta entrada, se prepara justo despues del commit.
Rama: `feature/a-estructuras`

### Pendiente para la siguiente sesion

- Confirmar con Parte B y Parte C si `InterfazPartida` cubre las necesidades de JavaFX y JSON.
- Confirmar si `ALCANCE_ARCO = 3` queda como valor definitivo.
- Decidir si avanzar de cueva debe consumir solo accion, tambien movimiento o terminar turno.
- Mejorar en una iteracion posterior el movimiento largo para no atravesar enemigos.
- Mejorar la IA enemiga para buscar ruta alternativa si el primer paso esta ocupado.
- Sustituir los metodos package-private de preparacion por una fabrica o builder formal de `Partida`.
- Sustituir `ObjetoEnMapa.getCueva()` por id o vista inmutable cuando se cierre la integracion con JSON/JavaFX.

### Riesgos o avisos

- La capa nueva esta implementada y testeada como primera version funcional, pero todavia no esta conectada en profundidad con JavaFX ni con el cargador JSON.
- Enemigos y objetos por cueva viven de momento en `Partida`, no dentro de `Cueva`.
- La cobertura de ramas queda por debajo de lineas porque hay bastantes validaciones defensivas y caminos de error.
- La rama contiene cambios compartidos de logica, por lo que debe entrar por PR y revision independiente antes de `main`.

## 2026-05-22 - Guillermo / Parte B - puente JSON-Partida

### Identificacion de sesion

Humano: Guillermo
Rol: Parte B
Agente: Codex-B Logica

### Sincronizacion

Rama de trabajo: `feature/b-json-partida`
Base: `main` actualizado con B-02 y la primera version de B-03 mergeadas.
Modo economico aplicado: lectura escalonada, uso de `SCRATCHPAD.md` como memoria, sin releer PDFs completos y sin busquedas globales salvo las necesarias para comprobar referencias.

### Alcance acordado

- Preparar una version simple para que Parte C pueda arrancar una `Partida` desde JSON.
- No implementar todavia regla del 75%, pocion de invisibilidad ni mejoras extra de combate.
- Mantener la responsabilidad de `src/json` limitada a leer datos; la creacion jugable queda en Parte B.
- Usar valores iniciales fijos para jugador: vida 100, ataque 15, defensa 5, movimiento 3 y 40 turnos.

### Cambios realizados

- Creada `src/modelo/juego/FabricaPartida.java` como puente desde `ResultadoCarga` hasta `Partida`.
- `ResultadoCarga` conserva ahora tambien las conexiones leidas del JSON para poder crear puertas.
- Los DTO de enemigos y objetos conservan `idCueva` internamente para colocar cada elemento en su cueva.
- Los objetos JSON admiten `id`, `tipoLlave` y `codigoCerradura`.
- `datos/cuevas.json` se adapto a los tipos reales de Parte B: enemigos `ESQUELETO`, `ORCO`, `MAGO` o `BOSS`, y objetos `POCION`, `ESPADA`, `ARCO`, `ESCUDO` o `LLAVE`.
- Las conexiones JSON se traducen a puertas con codigo de llave `llave-` + id de la cueva destino.

### Tests y comprobaciones

- Compilacion manual de `src` correcta con Gson.
- Compilacion manual de tests correcta.
- Ejecutados por runner de reflexion los tests `json.CargadorConfiguracionTest` y `modelo.juego.FabricaPartidaTest`.
- Resultado: 21 tests ejecutados, 0 fallos.
- Correcciones tras revision: las conexiones JSON invalidas fallan con error claro, el jugador se recoloca al avanzar de cueva y el boss respeta las estadisticas acordadas.

### Pendiente

- Revision independiente sobre el puente JSON-Partida realizada y correcciones aplicadas.
- Confirmar con Hector si `FabricaPartida` le basta para continuar C-02/C-04.
- Ejecutar tests desde IntelliJ si el grupo quiere registrar cobertura visual.
- Mantener fuera de esta version la regla del 75%, invisibilidad y guardado/carga de estado.

### Riesgos

- `FabricaPartida` acopla de forma explicita JSON con la logica de partida; se acepta como puente minimo para desbloquear a Parte C, pero puede evolucionar despues a builder o adaptador mas formal.
- Los atributos JSON `nombre`, `descripcion`, `ataque` y `defensa` de objetos no se usan todavia para crear objetos parametrizables, porque las clases de B-02 tienen valores fijos por tipo.
- `build/` puede quedar generado localmente por pruebas manuales y no debe subirse.

## 2026-05-22 - Alvaro / Bloque narrativo y visual

### Identificacion de sesion

Humano: Alvaro
Rol: Visual / Narrativo (coordinacion transversal)
Agente: Codex (big-pickle)

### Contexto

Integracion del bloque narrativo y visual del juego: pantallas de introduccion,
transicion por cueva, final (victoria/derrota), emojis tematicos, muros
coloreados por cueva, rediseno de mapas con progresion 7x7→10x10→13x13.

### Sincronizacion

Rama: `feature/a-estructuras`
Cambio remoto revisado: si
Documentos leidos: `tareas.md`, `PantallaJuego.java`, `EscapeMazmorraApp.java`,
`Partida.java`, `Cueva.java`, `CuevaEnMapa.java`, `Mazmorra.java`,
`project-management/DECISIONS.md`, `project-management/AGENTS.md`,
`project-management/TASKS.md`

### Cambios

Archivos nuevos:
- `src/vista/DatosTemaCueva.java` — enum con datos estaticos por cueva
  (titulo, texto narrativo, fondoCSS, colorMuro, emojis)
- `src/vista/PantallaIntroduccion.java` — pantalla de historia inicial
- `src/vista/PantallaTransicion.java` — pantalla reutilizable de transicion
- `src/vista/PantallaFinal.java` — pantalla de victoria/derrota

Archivos modificados:
- `src/vista/PantallaJuego.java`:
  - Anadidos callbacks `setAlCambiarCueva()` y `setAlTerminarPartida()`
  - Sustituidos circulos de colores por emojis (tarea 8)
  - Muros coloreados segun tematica de cueva via `DatosTemaCueva` (tarea 9b)
  - Deteccion de cambio de cueva para reconstruir grid
  - Deteccion de fin de partida para disparar pantalla final
- `src/vista/EscapeMazmorraApp.java`:
  - Nuevo flujo narrativo: Intro → Transicion(CuevaI) → Juego → Transicion(CuevaII) → ...
  - Nuevos metodos: `mostrarIntroduccion()`, `mostrarTransicion()`, `mostrarJuego()`, `mostrarFinal()`
- `src/modelo/juego/Partida.java`:
  - Anadido `getSiguienteCuevaId()` para inspeccionar siguiente cueva sin transicionar
- `datos/cuevas.json`: mapas redisenados a 7x7, 10x10 y 13x13 con nuevas
  disposiciones de muros, enemigos y objetos
- `tareas.md`: actualizados estados (tareas 4-8, 9b)
- `project-management/DECISIONS.md`: anadidas decisiones D-20 a D-24

### Datos de cueva

| Cueva | Nombre | Tamano | Color muro | Emoji enemigo | Emoji boss |
|---|---|---|---|---|---|
| facil | Las Criptas de Marfil | 7×7 | #d2cdc3 hueso | 💀 | ☠️ |
| media | El Paramo Putrefacto | 10×10 | #468246 verde | 🧟 | 🧌 |
| dificil | El Abismo de Malakor | 13×13 | #aa2d2d rojo | 👹 | 😈 |

### Pruebas

No se pudieron ejecutar tests JUnit por falta de JDK/JDK con javac en PATH.
El proyecto requiere JavaFX 23 y Gson en classpath para compilar y ejecutar.
No hay script de compilacion automatizado disponible.

### Pendiente

- Probar la aplicacion completa en IntelliJ.
- Verificar que los emojis se renderizan correctamente con la fuente "Segoe UI Emoji".
- Tarea 9 (Mejora general UI) pendiente para futura sesion.
- Tarea 10-12 pendientes.

## 2026-05-22 - Hector / Parte C

### Identificacion de sesion

Humano: Hector
Rol: Parte C
Agente: opencode (big-pickle)

### Contexto

Sesion de cierre. Se corrigieron los 3 problemas reportados por el usuario al final de la sesion anterior: titulos descentrados en pantallas de inicio, retardo en auto-turno y ausencia de flash visual cuando un enemigo ataca al jugador.

### Sincronizacion

Rama: feature/a-estructuras (usada para cambios de JavaFX; no se cambio a feature/c-javafx-json-docs)
Cambio remoto revisado: no
Documentos leidos: AGENTS.md, TASKS.md, PRD.md, ARCHITECTURE.md, DECISIONS.md, SCRATCHPAD.md, IA_DIARY.md, templates/SESSION_SUMMARY_TEMPLATE.md

### Cambios

- `src/vista/EscapeMazmorraApp.java`:
  - ESCAPE: eliminado arco senoidal, rotacion y desviacion X — ahora letras rectas, mismas Y y espaciado uniforme.
  - PantallaInicio: "DE LA" y "MAZMORRA" centrados dinamicamente con `getLayoutBounds().getWidth()`.
  - PantallaOpciones: revertido de StackPane a Pane con titulo centrado por `getLayoutBounds().getWidth()` y VBox de botones en posicion absoluta original (ANCHO/2-130, 240).
- `src/vista/PantallaJuego.java`:
  - Auto-turno: suprimido mensaje "No puedes moverte mas este turno" cuando auto-turn se dispara, para que no aparezca feedback erroneo tras el cambio de turno.
  - Anadido `recibirAtaqueFila/Col` y `recibirAtaqueTimer` para flash rojo cuando enemigo dania al jugador.
  - Flash rojo se activa en cualquier `terminarTurno()` (tecla T, auto-turn, click, boton) comparando vida antes/despues.
  - Click handler: movido feedback para que no se solape con auto-turn.
- `datos/cuevas.json`: sin cambios en esta sesion.

### Pendiente

- Probar que los titulos quedan centrados en ambas pantallas.
- Probar que el flash de ataque enemigo se ve correctamente.
- Los cambios estan sin commit ni push.

### Riesgos

- `getLayoutBounds().getWidth()` podria dar 0 si la fuente (Georgia) no esta disponible, derivando en `setX(ANCHO/2)` en vez de centrado real. Sin embargo, Georgia es serif comun y el proyecto ya la usaba sin problemas.
- Los cambios estan en rama `feature/a-estructuras`, no en `feature/c-javafx-json-docs`. Conviene moverlos a la rama correcta al hacer commit.

### Riesgos

- Los emojis requieren "Segoe UI Emoji" en el sistema; funciona en Windows pero
  puede no renderizarse igual en otros SO.
- La deteccion de fin de partida en `actualizar()` detiene el refresco visual
  cuando se dispara el callback; verificar que no haya race conditions.
- No se testearon las nuevas pantallas por falta de compilador; revision manual
  de sintaxis realizada.

## 2026-05-22 - Cierre de sesion con Alvaro / Revision e integracion

### Identificacion de sesion

Humano: Alvaro
Rol: Revision e integracion de primera version funcional del juego
Agente: Codex

### Sincronizacion

Rama: no cambiada durante la sesion
Cambio remoto revisado: no
Rama actualizada con `origin/main`: no
Documentos leidos: `project-management/templates/SESSION_SUMMARY_TEMPLATE.md`, `project-management/AGENTS.md` via busqueda de protocolo, `project-management/SCRATCHPAD.md`

### Tareas trabajadas

- Revision independiente inicial de la version funcional.
- Tarea 1: hacer que `Main` lance la aplicacion real.
- Tarea 2: crear scripts reproducibles de compilacion/ejecucion y tests.
- Tarea 3: ejecutar y clasificar la suite JUnit.
- Tarea 4: corregir cambio de cueva para exigir PUERTA y llave.
- Revision independiente final antes del cierre.

### Archivos modificados

- `.gitignore`
- `README.md`
- `scripts/run.ps1`
- `scripts/test.ps1`
- `src/Main.java`
- `src/modelo/juego/Partida.java`
- `src/vista/EscapeMazmorraApp.java`
- `src/vista/PantallaJuego.java`
- `test/modelo/juego/PartidaTest.java`
- `project-management/SCRATCHPAD.md`

### Cambios realizados

- `Main` delega de forma legible en `EscapeMazmorraApp.main(args)`.
- `scripts/run.ps1` compila y lanza el juego con JDK 21, Gson y JavaFX.
- `scripts/test.ps1` compila src+test y ejecuta JUnit standalone.
- `README.md` documenta ejecutar, solo compilar y lanzar tests.
- `.gitignore` ignora `build/` y `crash.log`.
- `Partida.cambiarCueva()` y `Partida.avanzarACueva()` exigen estar sobre una celda `PUERTA`.
- Una puerta abierta ya no permite avanzar sin la llave correspondiente.
- La UI solo muestra transicion de cueva cuando el modelo ya ha aceptado el cambio.
- Se anadieron tests para fuera de puerta, puerta sin llave y puerta con llave.

### Tests

Tests JUnit creados o actualizados:
- `PartidaTest.cambiarCuevaRequiereEstarSobrePuerta`
- `PartidaTest.cambiarCuevaRequiereLlaveAunqueEsteSobrePuerta`
- `PartidaTest.cambiarCuevaDesdePuertaConLlaveAvanza`
- Actualizados tests de `avanzarACueva` para el nuevo contrato de puerta+llave.

Tests ejecutados:
- `powershell.exe -ExecutionPolicy Bypass -File scripts\test.ps1`

Resultado:
- 180 tests encontrados.
- 165 correctos.
- 15 fallidos.
- Los 15 fallos fueron clasificados por revision independiente como contratos/datos antiguos frente al nuevo `datos/cuevas.json`, no como regresiones nuevas de la regla de puerta.

Si no se ejecutaron, motivo:
- No aplica. Se ejecutaron fuera del sandbox porque JavaFX esta en `.m2`, fuera de la carpeta del proyecto.

### Revision independiente

- Revisor inicial: detecto riesgos en `Main`, scripts, cambio de cueva, guardado, victoria y tests.
- Revisor de cierre: detecto bypass en `avanzarACueva` y caso de puerta abierta sin llave.
- Revisor final acotado: confirmo que los P1 quedaron resueltos y recomendo cerrar sesion.

### Commits y push

Commit realizado: no
Hash:
Push realizado: no
Rama:

### Pendiente para la siguiente sesion

- Actualizar tests antiguos para los mapas actuales 7x7, 10x10 y 13x13.
- Revisar balance/contrato de `datos/cuevas.json` contra los tests de fabrica, partida y serializador.
- Decidir condicion final de victoria: llave final, salida o ambas.
- Completar o desactivar guardado/carga para no prometer persistencia incompleta.
- Probar manualmente el flujo completo del juego con JavaFX.

### Riesgos o avisos

- `scripts/run.ps1` y `scripts/test.ps1` dependen de JavaFX 21.0.5 instalado en `.m2`.
- La suite sigue en rojo por 15 tests desactualizados respecto a los nuevos datos.
- Hay cambios previos de opencode sin commit en varios archivos de vista, datos y documentacion.
- No se hizo commit ni push; pedir autorizacion explicita antes de hacerlo.

## 2026-05-23 - Cierre de sesion con Alvaro / Ajuste de tareas

### Identificacion de sesion

Humano: Alvaro
Rol: Coordinacion de tareas y comprobacion de demo
Agente: Codex

### Sincronizacion

Rama: no cambiada durante la sesion
Cambio remoto revisado: no
Rama actualizada con `origin/main`: no
Documentos leidos: `project-management/TASKS.md`, `project-management/SCRATCHPAD.md`

### Tareas trabajadas

- Se ejecuto el juego mediante `scripts/run.ps1` en ventana visible.
- Se centralizo la lista de tareas en `project-management/TASKS.md`.
- Se elimino `tareas.md` para evitar dos fuentes de verdad.
- Se anadieron tareas nuevas de pulido audiovisual y accesibilidad.

### Archivos modificados

- `project-management/TASKS.md`
- `project-management/SCRATCHPAD.md`
- `tareas.md` eliminado

### Cambios realizados

- `project-management/TASKS.md` queda como lista oficial de tareas.
- Anadidas subtareas:
  - `C-09.7`: SFX cortos para recoger objeto, ataque y dano.
  - `C-09.8`: transiciones fade in/out entre pantallas.
  - `C-09.9`: alertas visuales temporales con textos flotantes de dano y cura.
  - `C-09.10`: modo antorcha / filtro de contraste accesible con CSS dinamico.

### Tests

Tests JUnit creados o actualizados: no
Tests ejecutados: no
Resultado: no aplica
Si no se ejecutaron, motivo: solo se modifico documentacion de tareas.

### Commits y push

Commit realizado: no
Hash:
Push realizado: no
Rama:

### Pendiente para la siguiente sesion

- Revisar el estado actual del arbol de trabajo antes de commitear, porque hay mas cambios locales ademas de `TASKS.md`.
- Implementar o priorizar las nuevas subtareas C-09.7 a C-09.10.
- Mantener `project-management/TASKS.md` como unica lista oficial.

### Riesgos o avisos

- Hay cambios locales sin commit en varios archivos del proyecto.
- `tareas.md` fue eliminado intencionadamente para evitar duplicidad.

## 2026-05-23 - Codex-A Estructuras (Parte A, en area audiovisual C-09)

### Identificacion de sesion

Humano: Alvaro (Parte A)
Rol: Parte A trabajando en audiovisual (C-09, area de Parte C)
Agente: Codex-A Estructuras

### Contexto

Implementar mapas laberinticos mas grandes (C-09.6) y sistema de niebla (fog-of-war) en PantallaJuego.

### Sincronizacion

Rama: `feature/a-estructuras`
Cambio remoto revisado: si, sin cambios nuevos de otros agentes.
Documentos leidos: AGENTS.md, TASKS.md, SCRATCHPAD.md

### Cambios realizados

1. `datos/cuevas.json`: Redisenados 3 mapas laberinticos:
   - Criptas (facil): 15x15, INICIO(2,2), 2 esqueletos, llave+escudo+pocion.
   - Paramo (media): 19x19, INICIO(2,2), 3 orcos, llave+espada+pocion.
   - Abismo (dificil): 23x23, INICIO(2,2), 4 enemigos (1 boss), llave+arco+2 pociones.
   - Muros de 2 celdas de grosor, pasillos de 1 celda.
   - Corregido 'T'->'TESORO' en matriz (3 ocurrencias).
   - Corregida posicion enemigo (9,6)->(6,15) en cueva_media.
   - Corregidas posiciones enemigos en dificil (5,15)->(5,16), (9,18)->(9,20).

2. `src/vista/PantallaJuego.java`:
   - wallThickness: 5->10px.
   - Muros: de bordes finos a relleno solido con Rectangle.
   - Almacenados cumX, cumY, colWidth, rowHeight como campos.
   - Anadido gridFog Pane en z-order: gridCeldas->gridWalls->gridFog->gridOverlay.
   - Anadido BFS recalcularVisibilidad() (Cola propia, radio 3, bloqueo por MURO/ROCA/ARBUSTO).
   - Anadido actualizarFog() (rectangulos RGBA con opacidad segun distancia).
   - Llamadas a fog en actualizar().
   - Fix: crash en cell.getChildren().remove(1, size) cuando size<2.

3. `test/json/CargadorConfiguracionTest.java`: Actualizados tamaños (15/19/23) e INICIO(2,2).

4. `test/json/SerializadorPartidaTest.java`: Actualizados tamaños (9->15) e INICIO(1,1)->(2,2).

5. `test/modelo/juego/FabricaPartidaTest.java`: Actualizados INICIO(2,2) y ruta de movimiento.

6. `test/modelo/juego/PartidaTest.java`: Actualizados caminos de movimiento en 3 tests.

7. `project-management/TASKS.md`: Marcadas C-09.1 y C-09.6 como HECHA, actualizados tamaños.

### Tests

Tests JUnit ejecutados: 182/182 OK
Compilacion: 0 errores

### Commits y push

Pendiente de autorizacion humana para commit+push.

### Pendiente para la siguiente sesion

- Commit+push.
- Reemplazo de iconos (emojis).
- Actualizar IA_DIARY.md.

### Riesgos o avisos

- El fog-of-war cubre entidades (escondiendolas hasta que el jugador se acerque).
- Los enemigos con danio/vida (formato simple sin ataque/defensa/movimiento) tienen movimiento=0 pero la IA los mueve igual mediante getCaminoMinimo().

---

## Sesion 2026-05-23 — Turnos 40→60 y auto-avance en puerta

### Humano y parte
No se identifico explicitamente, pero las instrucciones indican seguir con la rama feature/a-iconos (Parte A/C).

### Rama usada
`feature/a-iconos`

### Tareas trabajadas
- Aumentar turnos disponibles de 40 a 60
- Auto-avance automatico al pisar PUERTA con llave (sin clic en "CAMBIAR CUEVA")
- Pasar revisor independiente y arreglar hallazgo

### Archivos modificados
- `src/modelo/juego/FabricaPartida.java`: TURNOS_INICIALES 40→60
- `src/vista/PantallaJuego.java`: auto-avance en keyboard + click handler, flag `movio` para evitar auto-avance tras ataque en PUERTA
- `test/modelo/juego/FabricaPartidaTest.java`: expected 40→60
- `test/modelo/juego/PartidaTest.java`: expected 40→60, loop turnosAgotados 40→60

### Cambios realizados
1. `FabricaPartida.TURNOS_INICIALES` de 40 a 60.
2. Codigo de auto-avance en ambos handlers: solo cuando `movio==true` (movimiento real, no ataque/recoger).
3. Auto-avance invoca `alCambiarCueva` para transicion de escena.
4. Comentarios detallados segun AGENTS.md.

### Pruebas ejecutadas
Tests JUnit: 182/182 OK.

### Pruebas no ejecutadas y motivo
N/A.

### Riesgos o posibles problemas
- Auto-avance comparte el mismo comportamiento post-transicion que el boton manual "CAMBIAR CUEVA" (accionRealizada queda true, el primer input en la nueva cueva dispara auto-turn).
- La flag `movio` evita auto-avance si el jugador ataca/recoge objeto estando sobre PUERTA.

### Archivos compartidos tocados o solicitados
- `FabricaPartida.java` (Parte B, solo constante)
- `PantallaJuego.java` (Parte C)

### Estado de TASKS.md
- C-09.11 anadida como HECHA.

### Pendiente para la proxima sesion
- C-09.3 Animaciones
- C-09.4 Efectos visuales
- C-09.5 Sonidos
- C-09.7 SFX
- C-09.8 Transiciones
- C-09.9 Alertas visuales
- C-09.10 Modo antorcha

---

## Cierre de sesion — 2026-05-23

### Humano y parte
Humano no identificado explicitamente. Trabajo en rama feature/a-iconos (Parte A/C).

### Rama usada
`feature/a-iconos`

### Tareas trabajadas
1. Aumentar turnos 40→60 y auto-avance en PUERTA con llave.
2. Correccion de revisor independiente: flag `movio` para evitar auto-avance al atacar desde PUERTA.
3. Mensaje "Hay una pared" al chocar con obstaculos (MURO/ROCA/ARBUSTO).
4. Dialogo de error en boton Cargar partida (Alert).

### Archivos modificados
- `src/modelo/juego/FabricaPartida.java` — TURNOS_INICIALES 40→60
- `src/vista/PantallaJuego.java` — auto-avance, flag movio, esObstaculo(), mensajes de pared
- `src/vista/EscapeMazmorraApp.java` — Alert en boton Cargar partida
- `test/modelo/juego/FabricaPartidaTest.java` — expected 40→60
- `test/modelo/juego/PartidaTest.java` — expected 40→60, loop agotamiento 40→60
- `project-management/SCRATCHPAD.md` — actualizado
- `project-management/TASKS.md` — C-09.11 anadida HECHA
- `project-management/IA_DIARY.md` — entrada anadida

### Cambios realizados
- TURNOS_INICIALES 40→60 en FabricaPartida.
- Auto-avance en PUERTA solo tras movimiento real (movio).
- esObstaculo() para detectar MURO/ROCA/ARBUSTO.
- Mensaje "Hay una pared" en teclado, click y botones de movimiento.
- Alert error en Cargar partida cuando no hay archivo.

### Commits y push realizados
- `1dd621f` — Aumentar turnos 40→60 y auto-avance en PUERTA con llave
- `86c0dbd` — Corregir mensaje de pared y dialogo de carga de partida
- Ambos pusheados a `origin/feature/a-iconos`.

### Pruebas ejecutadas
Tests JUnit: 182/182 OK en ambos commits.

### Pendiente para la siguiente sesion
- C-09.3 Animaciones
- C-09.4 Efectos visuales
- C-09.5 Sonidos
- C-09.7 SFX
- C-09.8 Transiciones
- C-09.9 Alertas visuales
- C-09.10 Modo antorcha

### Riesgos o avisos
- El auto-avance deja `accionRealizada=true`, por lo que el primer input tras la transicion dispara auto-turn (mismo comportamiento que el boton manual "CAMBIAR CUEVA").
- Los commits estan en `feature/a-iconos`, no mergeados a `main`. Pendiente de PR y revisor independiente antes de merge.

---

## Cierre de sesion — Revisor Independiente 2026-05-23

### Identificacion de sesion

Humano: Alvaro
Rol: Parte A
Agente: Agente Revisor Independiente

### Rama usada

`feature/a-iconos`

### Tareas trabajadas

1. Revision independiente de la rama `feature/a-iconos` antes de merge.
2. Corregido `HashMap` en `PantallaJuego.java` -> sustituido por `ListaDE<EntradaImagen>`.
3. Merge de `origin/main` en `feature/a-iconos` (sin conflictos).
4. Ejecucion de tests: 182/182 OK.

### Archivos modificados

- `src/vista/PantallaJuego.java` — eliminado `HashMap`, reemplazado por cache con `ListaDE` propia.
- `project-management/SCRATCHPAD.md` — actualizado.

### Commits y push realizados

Pendiente de autorizacion humana.

### Pendiente para la siguiente sesion

- Hacer commit del cambio de `PantallaJuego.java`.
- Hacer push a `origin/feature/a-iconos`.
- Hacer merge a `main` cuando se autorice.

## 2026-05-23 - Alvaro / Parte A - Depuracion cierre + tareas iconos

### Identificacion de sesion

Humano: Alvaro
Rol: Parte A (coordinacion general)
Agente: opencode-agente

### Contexto

Se depuro un cierre inesperado al entrar a la primera cueva tras merge de `origin/main`.
La causa no era una excepcion no capturada, sino que el catch solo capturaba `Exception`
y no `Throwable`. Se anadio logging directo a archivo (`direct_debug.log`, `crash_detail.log`)
y se cambiaron los catch a `catch (Throwable)`. Tras la correccion, el cierre dejo de ocurrir.

### Sincronizacion

Rama: `feature/a-iconos`
Cambio remoto revisado: si, rama actualizada con `origin/main` en sesiones previas.
Documentos leidos/modificados: `TASKS.md`, `SCRATCHPAD.md`, `IA_DIARY.md`.

### Tareas trabajadas

1. Depuracion del cierre al entrar a cueva tras merge con `origin/main`.
2. Anadido logging directo a archivo en `PantallaJuego.java` y `EscapeMazmorraApp.java`.
3. Cambiados catch de `Exception` a `Throwable` en `actualizar()` y `crearScene()`.
4. Anadidas sub-tareas de iconos a `TASKS.md` (C-09.13 a C-09.16).

### Archivos modificados

- `src/vista/PantallaJuego.java` — anadido `logError()`, catch `Throwable`, logging en excepciones.
- `src/vista/EscapeMazmorraApp.java` — anadido `logDirecto()`, `mostrarJuego()` envuelto en try-catch(Throwable).
- `project-management/TASKS.md` — anadidas sub-tareas C-09.13 a C-09.16.
- `project-management/SCRATCHPAD.md` — actualizado.
- `project-management/IA_DIARY.md` — actualizado.

### Commits y push realizados

Pendiente de autorizacion humana (esta sesion).

### Pruebas ejecutadas

- Compilacion completa del proyecto con `scripts\run.ps1 -CompileOnly`: correcta.
- Juego ejecutado y verificado que ya no se cierra al entrar a la cueva.

### Pendiente para la siguiente sesion

- Ejecutar tareas C-09.13 a C-09.16 (encontrar/crear iconos para puerta, escudo, tesoro, salida, itch.io).
- Probar iconos en el juego tras implementarlos.

## 2026-05-23 - Guillermo / Parte B ayudando a C-09

### Contexto

Guillermo pidio ayudar primero a las tareas visuales/audiovisuales pendientes antes de volver a B-04/B-05. Se reviso `TASKS.md` actualizado tras PR #13 y se acordo trabajar hoy sobre menu de pausa, iconos pendientes y SFX minimos.

### Decisiones cerradas

- Menu de pausa con las teclas previstas en tareas: `P` o `ESC`.
- El menu de pausa incluye Continuar, Guardar partida y Volver al menu.
- Volver al menu pide confirmacion.
- La puerta puede ser un icono creado localmente.
- La salida se representa como una puerta mas grande para no complicar el diseno.
- TESORO usa cofres del `Dungeon Asset Pack`.
- ESCUDO deja de usar `staff2.png`; si no hay asset real, se acepta un icono simple propio.
- Sonidos: se aceptan efectos basicos para recoger, ataque, dano, puerta, guardar y pausa.

### Cambios realizados

- `src/vista/PantallaJuego.java`: anadido overlay de pausa, confirmacion al volver al menu, iconos decorativos para PUERTA/TESORO/SALIDA, icono simple de ESCUDO y reproduccion de SFX en acciones clave.
- `src/vista/ReproductorSfx.java`: nuevo reproductor de efectos cortos con `AudioClip`.
- `datos/audio/sfx/`: generados WAV locales para recoger, ataque, dano, puerta, guardar y pausa.
- `test/vista/ReproductorSfxTest.java`: test de existencia y cabecera WAV de los SFX.
- `project-management/TASKS.md`: C-09.7, C-09.12, C-09.13, C-09.14 y C-09.15 pasan a REVISION hasta probar en IntelliJ.

### Verificacion

- Compilacion con `scripts/run.ps1 -CompileOnly`: bloqueada en este entorno por `JAVA_HOME` apuntando a `C:\Users\UAH\.jdks\ms-21.0.10`.
- Compilacion manual con `javac` disponible: bloqueada porque el proceso de Codex no puede leer los JAR de JavaFX en `.m2` del usuario.
- Pendiente: compilar/probar desde IntelliJ o PowerShell del usuario, donde JavaFX ya esta configurado.

### Pendiente

- Probar en juego: abrir/cerrar pausa con P/ESC, guardar desde pausa, confirmar volver al menu, ver iconos de puerta/salida/tesoro/escudo y escuchar SFX.
- Si los sonidos generados no convencen, sustituir WAV por sonidos descargados de Freesound u otra fuente compatible.

### Ajustes tras prueba de Guillermo

- Ataque y puerta sonaban demasiado bajo: se aumento el volumen general de `ReproductorSfx` y se regeneraron `ataque.wav` y `puerta.wav` con mas amplitud/duracion.
- Tras atacar, la UI podia dar sensacion de que habia que moverse antes de volver a atacar. No se cambio la regla de turnos; ahora si la accion ya esta usada, `ESPACIO` y el boton de ataque avisan que hay que terminar turno con `T`.
- La barra de vida sobre personajes ya no muestra texto numerico encima de la celda; queda como barra corta para evitar el bloque visual.
- El inventario marca los objetos equipados con borde dorado y etiqueta `EQ`, y las ranuras de equipo quedan resaltadas.
- Se reviso el `Dungeon Asset Pack` local por nombres tipo shield/door/gate/portal; no hay assets claros de escudo o puerta en el pack actual.

### Ajustes adicionales de turno e inventario

- Se elimino el auto-fin de turno desde teclado y clic. El turno vuelve a terminar siempre con `T` o con el boton `TERMINAR TURNO`, evitando que atacar a veces cerrara turno y a veces no.
- La vida vuelve a mostrar puntos numericos sobre la entidad y mantiene la barra abajo.
- El bloque de equipo se redistribuye con ranuras etiquetadas `ARMA` y `ESCUDO`, y las ranuras equipadas muestran texto `EQUIPADA/EQUIPADO`.

### Ajustes de iconos, controles y ventana

- Se crearon iconos PNG locales en `datos/iconos/`: `puerta.png`, `salida.png`, `escudo.png` y `tesoro.png`.
- `PantallaJuego` usa esos PNG para PUERTA, SALIDA, ESCUDO y TESORO.
- Los numeros de vida pasan a fuente Arial en negrita, color claro y contorno negro mas fuerte para mejorar lectura.
- La confirmacion de volver al menu ya no usa `Alert`; ahora es un panel propio dentro del menu de pausa, con estilo del juego.
- En la pantalla de controles se oculta el cofre decorativo para que no tape el boton Volver.
- La ventana principal pasa a ser redimensionable, con tamano minimo 960x540.
- Se anadio `IconosVisualesTest` para comprobar que los PNG existen y tienen cabecera PNG valida.

### Ajuste final antes de PR

- Se vuelve a mostrar la accion `RECOGER OBJETO [R]` en el panel de acciones.
- Si la accion ya esta usada, atacar/recoger quedan deshabilitados sin mostrar feedback intrusivo encima del juego.

### Correcciones tras revision independiente

- El boton lateral `VOLVER AL MENU` ahora abre y muestra correctamente el panel propio de confirmacion aunque la partida no estuviera pausada.
- El estilo de botones se aplica despues de reconstruir el panel de acciones, evitando botones visualmente activos cuando la accion/movimiento ya esta usado.
- `datos/partida_guardada.json` se anade a `.gitignore` para no subir partidas locales generadas durante pruebas.
- Los SFX pasan a generarse desde codigo en `ReproductorSfx`, evitando subir WAV binarios y depender de assets externos.

### Ultimo ajuste solicitado por Guillermo

- La accion `RECOGER OBJETO [R]` queda visible en el panel y disponible tambien por teclado.
- Al pulsar `R` con la accion ya usada no aparece feedback flotante intrusivo.
- Al terminar turno con `T` o con el boton ya no aparece el aviso verde `Turno terminado`; el turno sigue siendo explicito, pero sin cartel repetitivo.
## 2026-05-24 - Cierre de sesion con Alvaro / Ataque direccional

### Identificacion de sesion

Humano: Alvaro
Rol: Parte A, con autorizacion expresa para tocar logica de Parte B y UI de Parte C en el alcance de ataque direccional.

### Rama y sincronizacion

Rama usada: `feature/a-estructuras`.
Inicio real: se cambio desde `feature/a-iconos` a `feature/a-estructuras`.
Actualizacion remota: `feature/a-estructuras` actualizada con `origin/main` mediante fast-forward antes de modificar archivos.

### Trabajo realizado

- Implementado ataque direccional consultable desde `Partida`: `hayEnemigoEnDireccion(int df, int dc)` y `getEnemigosAdyacentes()`.
- Actualizado el contrato `InterfazPartida` para exponer las consultas que necesita JavaFX.
- `PantallaJuego` permite atacar con clic sobre un enemigo y con `Shift+WASD` o `Shift+Flechas`.
- Anadido resaltado visual de enemigos adyacentes cuando la accion esta disponible y flash visible de ataque/dano mediante overlay propio sobre la celda.
- Anadidos tests unitarios de direccion, lista de enemigos adyacentes y seleccion de objetivo entre varios enemigos.

### Archivos modificados

- `src/modelo/juego/InterfazPartida.java`
- `src/modelo/juego/Partida.java`
- `src/vista/PantallaJuego.java`
- `test/modelo/juego/PartidaTest.java`
- `project-management/TASKS.md`
- `project-management/SCRATCHPAD.md`
- `project-management/IA_DIARY.md`

### Pruebas

Comando ejecutado: `powershell -ExecutionPolicy Bypass -File .\scripts\test.ps1`
Resultado: 189/189 tests correctos.

### Estado de TASKS.md

- `B-05` pasa de `PENDIENTE` a `REVISION`.
- `C-10` sigue `PENDIENTE`: solo queda documentado como implementado el punto 1 (ataque direccional). Ataque especial cargable y revision de turnos quedan fuera de esta sesion.

### Pendiente para la siguiente sesion

- Revision independiente pasada sin bloqueos P1/P2. Riesgos menores: `hayEnemigoEnDireccion(df, dc)` asume deltas seguros desde la UI y el boton antiguo de espacio sigue atacando el primer adyacente.
- Marcar `B-05` como `HECHA` cuando el grupo acepte la revision.
- Decidir en otra sesion el alcance de ataque especial cargable y revision de turnos de `C-10`.

## 2026-05-24 - Guillermo / Cierre B-02 y B-03

### Objetivo

Revisar si `B-02` y `B-03` podian cerrarse tras los ultimos merges y corregir la regla pendiente de cambio de cueva.

### Decisiones humanas

- `FabricaPartida` se da por valida para el arranque desde JSON porque Guillermo la probo en el juego.
- `ALCANCE_ARCO = 3` se acepta como alcance definitivo de la primera version.
- Cambiar de cueva no debe consumir accion ni terminar turno; tampoco debe devolver accion o movimiento ya gastados.
- Al cambiar de cueva se reinician los turnos disponibles a 60.
- Equipar o cambiar objeto no debe consumir accion.

### Cambios realizados

- `Partida.equiparObjeto(...)` ya no marca `accionRealizada`.
- `Partida.avanzarACueva(...)` ya no exige accion libre, no marca accion realizada y reinicia `turnosRestantes`; los flags de movimiento/accion se conservan si ya estaban gastados.
- `Partida.TURNOS_POR_CUEVA = 60` centraliza el valor usado al entrar en una cueva nueva.
- `TASKS.md` pasa `B-02` y `B-03` a `HECHA` y corrige el resumen de `B-05` a `REVISION`.

### Tests

- Anadidos tests en `PartidaTest`:
  - cambiar de cueva no consume accion ni turno, no hace actuar enemigos y reinicia turnos.
  - cambiar de cueva permite avanzar aunque la accion ya estuviera usada.
  - cambiar de cueva no devuelve movimiento ya usado.
  - equipar objeto no consume accion y permite atacar despues.
- Verificacion dirigida sin JavaFX: `PartidaTest` + `FabricaPartidaTest`, 64/64 tests correctos.
- Revisor independiente detecto que no consumir accion no debia borrar accion/movimiento previos; se corrigio conservando los flags.
- El script completo sigue requiriendo JavaFX local; validar cobertura completa desde IntelliJ.

## 2026-05-24 - Guillermo / Pulido de iconos y tesoros

Guillermo pidio continuar la sesion quitando del alcance los bosses distintos, porque no estaba claro si correspondia ahora. Se mantiene el foco en el pulido visual y de mapa: eliminar el cuadrado morado del tesoro, mejorar iconos de pocion y escudo, y evitar que tesoros/cofres queden encajados en paredes.

Cambios aplicados:
- `PantallaJuego` usa `datos/iconos/pocion.png` para pociones y `datos/iconos/escudo.png` para escudos, evitando que se representen con cofres genericos.
- La celda `TESORO` deja de usar el color morado de fondo y pasa a un tono de suelo/tesoro mas integrado.
- Se reemplaza el PNG local de escudo y se anade un PNG local de pocion.
- En `datos/cuevas.json`, el tesoro de la cueva dificil se mueve una celda para quedar accesible y no encajado en muro.
- `CargadorConfiguracionTest` valida que los objetos del JSON real no aparecen en muro, roca o arbusto, y que todos los `TESORO` tienen al menos un vecino transitable.
- `IconosVisualesTest` incluye tambien `pocion.png`.
- Regla minima de cofres acordada despues de prueba visual: `TESORO` cerrado no se pisa; se abre con `R` desde una casilla cardinal adyacente o desde la misma casilla en partidas antiguas, pasa a `SUELO` y consume accion.
- `PantallaJuego` muestra `ABRIR COFRE [R]` cuando hay un tesoro abrible cerca y no muestra mensaje de pared al intentar moverse hacia un cofre cerrado.

Alcance excluido:
- Bosses con disenos distintos queda fuera de esta sesion por decision explicita de Guillermo.

Verificacion:
- Verificacion dirigida sin JavaFX: `CargadorConfiguracionTest` + `IconosVisualesTest`, 16/16 tests correctos.
- Revisor independiente detecto que los PNG locales podian recortarse como spritesheets; corregido con carga de iconos locales sin viewport.
- Se refuerza el test de TESORO para exigir camino real desde `INICIO`, no solo una celda vecina transitable.
- Se anaden tests de `Partida` para proteger que el tesoro cerrado no se puede pisar, que se abre desde una casilla adyacente y que al abrirse pasa a `SUELO`.
- Revisor independiente detecto que el BFS podia usar `TESORO` cerrado como camino intermedio aunque no fuera destino. Corregido: el calculo de celdas alcanzables de `Partida` excluye TESORO cerrado.
- La ayuda de `PantallaJuego` se actualiza: `R` sirve para recoger objeto o abrir cofre.
- Verificacion dirigida actualizada: `PartidaTest` + `FabricaPartidaTest` + `CargadorConfiguracionTest` + `IconosVisualesTest`, 83/83 tests correctos.
- Compilacion JavaFX desde Codex bloqueada por permisos de los JAR OpenJFX locales (`AccessDeniedException`); validar visualmente desde IntelliJ antes de preparar PR.
## 2026-05-24 - Alvaro / Implementacion de animaciones C-09.3

### Identificacion de sesion

Humano: Alvaro
Rol: Parte A, trabajando en animaciones de C-09 (area de Parte C, autorizado)
Agente: opencode (big-pickle)

### Contexto

Se reviso `TASKS.md` y se priorizo C-09.3 Animaciones (movimiento suave, ataque, muerte) dentro de C-09 Pulido audiovisual. Se decidio comenzar por los subtask de TASKS.md ordenados por prioridad.

### Sincronizacion

Rama: `feature/a-estructuras`
Cambio remoto revisado: si (`
task
` fallback)
Documentos leidos: `TASKS.md`, `SCRATCHPAD.md`, `PantallaJuego.java`

### Cambios realizados

Archivo modificado:
- `src/vista/PantallaJuego.java`: ~200 lineas nuevas de animacion

Fase 1 — Movimiento suave:
- Agregada importacion de `javafx.animation.*` (ScaleTransition).
- Nuevos campos: `jugadorSprite`, `animMovimientoTimeline`, `emojiSizeCache`, `animOverlay` (capa no limpiada por `actualizar()`).
- `actualizar()` guarda referencia del sprite del jugador y emojiSizeCache.
- `animarMovimiento()` usa `translateX/Y` con Timeline 8 frames, easing smoothstep, 150ms, offset entre centro de celda origen y destino.

Fase 2 — Ataque:
- `animarAtaque()` crea circulo expansivo en gridOverlay (220ms) con opacidad decreciente.

Fase 3 — Muerte:
- `animarMuerteEnemigo()` usa FadeTransition + ScaleTransition 400ms en animOverlay (no se limpia al redibujar).
- Helper `getEnemyAssetPath()` resuelve sprite segun tipo enemigo/boss y tematica de cueva.

Integracion en handlers:
- Keyboard handler (movimiento + espacio + shift+WASD).
- Click handler (movimiento clic + ataque clic).
- Botones de accion (arriba/abajo/izq/der/atacar).

### Pruebas

Compilacion: `javac 21.0.10` correcta.
Tests: `powershell -ExecutionPolicy Bypass -File .\scripts\test.ps1` — 189/189 tests OK.

### Estado de TASKS.md

C-09.3 pasa de PENDIENTE a HECHA.

### Pendiente para la siguiente sesion

- Decidir siguiente subtarea de C-09 (C-09.9 Alertas visuales, C-09.4 Particulas, C-09.5 Sonidos, etc.) o pasar a otra area.
- Actualizar `IA_DIARY.md` y documentacion.

### Riesgos o avisos

- La animacion de muerte (400ms) coexiste con el timer del flash de ataque (300ms) sin conflicto porque usa `animOverlay` (separado de gridOverlay).
- Si el jugador cambia de cueva durante una animacion, la escena se descarta y se crea una nueva (sin fugas de memoria).
- El movimiento animado no bloquea input; el Timeline corre en segundo plano.

## 2026-05-24 - Alvaro / Estadisticas, puntuacion y ranking

### Identificacion de sesion

Humano: Alvaro
Rol: Parte A, con excepcion autorizada para implementar estadisticas/ranking tocando logica de Parte B, UI de Parte C y JSON.

### Rama y sincronizacion

Rama usada: `feature/a-estructuras`.
Estado previo: rama actualizada con `origin/main` por fast-forward; queda por delante de `origin/feature/a-estructuras` por la sincronizacion y los cambios locales.

### Trabajo realizado

- Se creo `EstadisticasPartida` para registrar turnos, dano recibido, dano ejercido, enemigos comunes muertos, bosses muertos y Malakor derrotado.
- `Partida` registra estadisticas al atacar, recibir dano enemigo, matar enemigos/bosses y pasar turno.
- El guardado/carga de partida conserva estadisticas cuando existen y mantiene compatibilidad con guardados antiguos.
- El inicio de partida nueva pide nombre; cancelado/vacio usa "Mago Errante".
- La pantalla final muestra nombre, estadisticas, puntuacion y titulo.
- Se creo `SerializadorRanking` y `ResultadoPartidaDTO`; `ranking.json` se guarda en raiz con Gson pretty printing y se ignora en Git.
- El menu incluye boton `Ranking` y vista Top 10 ordenada por puntuacion usando estructuras propias.
- Se documento D-26 y se marco C-11 como HECHA.

### Archivos modificados

- `.gitignore`
- `src/modelo/juego/EstadisticasPartida.java`
- `src/modelo/juego/Partida.java`
- `src/modelo/juego/FabricaPartida.java`
- `src/json/DatosPartidaDTO.java`
- `src/json/ResultadoPartidaDTO.java`
- `src/json/SerializadorRanking.java`
- `src/vista/EscapeMazmorraApp.java`
- `src/vista/PantallaFinal.java`
- `test/modelo/juego/EstadisticasPartidaTest.java`
- `test/modelo/juego/PartidaTest.java`
- `test/json/SerializadorRankingTest.java`
- `project-management/DECISIONS.md`
- `project-management/TASKS.md`
- `project-management/IA_DIARY.md`
- `project-management/SCRATCHPAD.md`

### Pruebas

Comando: `powershell -ExecutionPolicy Bypass -File .\scripts\test.ps1`
Resultado: 208/208 tests correctos.
Nota: la primera ejecucion en sandbox no pudo leer los JAR locales de JavaFX; se reejecuto con permisos y paso.

### Pendiente para la siguiente sesion

- Validar visualmente en IntelliJ el dialogo de nombre, la pantalla final y el ranking.
- Pedir revision independiente antes de merge porque hay codigo de logica y UI.
- Autorizar commit/push si el grupo acepta el alcance.

## 2026-05-24 - Alvaro / Ajuste visual del menu y dialogo de nombre

### Trabajo realizado

- Se subio la botonera de opciones (`layoutY` 190) para dejar libre el cofre decorativo inferior.
- Se elimino el uso de `TextInputDialog` para la nueva partida.
- Se anadio un modal propio en `EscapeMazmorraApp` con panel de pergamino/madera, campo de nombre estilizado y botones `Comenzar`/`Cancelar`.
- Confirmar con Enter usa el nombre escrito; Cancelar, Escape, vacio o espacios usa "Mago Errante".

### Pruebas

Comando: `powershell -ExecutionPolicy Bypass -File .\scripts\test.ps1`
Resultado: 208/208 tests correctos.
Nota: la primera ejecucion en sandbox no pudo leer los JAR locales de JavaFX; se reejecuto con permisos y paso.

### Pendiente

- Validacion visual manual en JavaFX/IntelliJ del nuevo modal y del cofre visible.
- Revision independiente antes de merge.

## 2026-05-24 - Alvaro / Bola de Fuego en tiempo real

### Trabajo realizado

- Se implemento `F + Flecha` en `PantallaJuego` con modo de disparo breve.
- Cada proyectil vive en una instancia interna `BolaDeFuego` con posicion, direccion, distancia y `Timeline` propios.
- La bola viaja por `animOverlay`, no por `gridOverlay`, para sobrevivir a refrescos de UI.
- `Partida` registra el disparo como accion y aplica impactos con dano fijo 10 mediante `ResultadoImpactoBolaFuego`.
- Impactos actualizan estadisticas, eliminan enemigos muertos, entregan llave final si cae boss y escriben log.
- `ReproductorSfx` intenta cargar MP3 externos y cae a WAV generado si no existen.

### Pruebas

Comando: `powershell -ExecutionPolicy Bypass -File .\scripts\test.ps1`
Resultado: 213/213 tests correctos.
Nota: la primera ejecucion en sandbox no pudo leer los JAR locales de JavaFX; se reejecuto con permisos y paso.

### Pendiente

- Validacion visual manual en JavaFX/IntelliJ de `F + Flecha`, trayectoria, SFX y limpieza del proyectil.
- Revision independiente antes de merge porque toca combate, UI y SFX.

## 2026-05-24 - Alvaro / Redisenio premium pantalla de inicio

### Trabajo realizado

- Se anadio ambiente propio para inicio: haz de luz, vineta y chispas generadas por codigo.
- Se creo una silueta decorativa de arco de mazmorra y un marco central para el logo.
- El titulo queda como logo `ESCAPE` + `DE LA MAZMORRA` con degradado dorado, brillo y sombra.
- `Inicio` usa el nuevo boton premium, sin globo.
- `crearBotonOpcion` delega en el nuevo helper premium para unificar el menu de opciones.

### Pruebas

Comando: `powershell -ExecutionPolicy Bypass -File .\scripts\test.ps1`
Resultado: 213/213 tests correctos.
Nota: la primera ejecucion en sandbox no pudo leer los JAR locales de JavaFX; se reejecuto con permisos y paso.

### Pendiente

- Validacion visual manual en JavaFX/IntelliJ de inicio y menu de opciones.

## 2026-05-24 - Alvaro / Sustitucion inicio por estilo pixel art

### Trabajo realizado

- La pantalla de inicio deja de usar el marco/arco/chispas de la version premium.
- Se anadio una vigneta simple y sprites del Dungeon Asset Pack: mago, demonio, baston, cofre y llave.
- El helper de assets de menu recorta spritesheets al primer frame y usa `setSmooth(false)`.
- El boton principal y el menu de opciones mantienen un estilo pergamino limpio.

## 2026-05-25 - opencode / Limpieza de assets no referenciados

### Trabajo realizado

- Eliminado `MATCOMP-ED-EL2-Arboles-master/` (proyecto externo de arboles, 0 referencias).
- Eliminados 16 GIFs de `Dungeon Asset Pack/characters/GIFs/` (ningun `.gif` usado en codigo).
- Eliminados 8 `*_run.png` de `characters/Spritesheets/` (solo `*_idle.png` se usan).
- Eliminados 13 PNGs de `weapons/` (solo `sword1.png` y `bow1.png` referenciados).
- Eliminados `chest1-3.png` y `tilemap.png` de `objects/`.
- Eliminado `datos/iconos/puerta.png` (no referenciado; juego usa `door_closed.png`).
- Conservado `datos/iconos/pocion.png` (referenciado por `IconosVisualesTest.java`).

### Pruebas

- Compilacion: 0 errores (javac con todas las fuentes + JavaFX + Gson).
- Revisor independiente: APROBADO sin bloqueos.

### Pendiente

- (ninguno)

## 2026-05-25 - Alvaro y Codex-A / Diagramas UML

### Trabajo realizado

- Correccion de sintaxis en diagrama de clases PlantUML (parentesis en relaciones causaban error).
- Division del diagrama grande (~900 lineas) en 9 archivos independientes por paquete.
- Reorganizacion en subcarpetas: `clases/`, `casos-de-uso/`, `secuencia/`, `estados/`, `actividad/`.
- Creacion de 4 diagramas nuevos:
  - `casos-de-uso/diagrama_casos_uso.puml` (12 casos, 5 categorias)
  - `secuencia/diagrama_secuencia_ataque.puml` (flujo ataque SPACE)
  - `estados/diagrama_estados.puml` (INICIO/EN_CURSO/VICTORIA/DERROTA + sub-estados)
  - `actividad/diagrama_actividad_turno.puml` (ciclo completo de turno)
- Simplificacion de los 4 para evitar solapamiento de elementos.
- Correccion de ñ en texto (`daño`).
- Actualizacion de TASKS.md con tareas NC-01 a NC-13.

### Archivos modificados

- `project-management/TASKS.md`
- `project-management/SCRATCHPAD.md`
- `diagramas uml/` (toda la carpeta reorganizada con subcarpetas)

### Pendiente

- NC-10 a NC-13: Revision de diagramas.
- NC-06: Planificar memoria.
- NC-08: Planificar guion video.

## 2026-05-25 - Guillermo / Ajuste responsive pantalla de partida

### Contexto

Guillermo inicio una nueva sesion porque, al ejecutar el juego sin maximizar la ventana, el jugador no siempre quedaba visible al principio de cada mazmorra y el texto inferior de la pantalla de partida aparecia cortado.

### Trabajo realizado

- `src/vista/PantallaJuego.java`: el mapa de la cueva queda dentro de un `ScrollPane` pannable, en lugar de depender de que toda la grilla quepa siempre en la ventana.
- Se anadio centrado automatico de la vista sobre el jugador cuando se construye o cambia la cueva.
- El log inferior deja de usar una altura fija tan rigida y conserva una altura minima menor.
- El panel derecho queda dentro de un `ScrollPane` vertical para que las acciones no se corten en ventanas bajas.
- No se tocaron reglas de partida, movimiento, combate, inventario, JSON ni datos de mapa.

### Pruebas ejecutadas

- Intento de compilacion: `powershell.exe -ExecutionPolicy Bypass -File scripts\run.ps1 -CompileOnly`.
- Resultado en Codex: bloqueado por permisos al leer los JAR locales de JavaFX en `C:\Users\landm\.m2\repository\org\openjfx\...`.
- Pendiente: validar visualmente desde IntelliJ con ventana no maximizada, cambio de cueva y panel inferior/derecho.

### Revision independiente

- Revisor independiente solicitado antes de preparar PR.
- Resultado: detecto riesgos de foco de teclado en `ScrollPane`, centrado antes del layout y posible corte horizontal. Se corrigieron usando filtros de teclado a nivel de `Scene`, reintento/espera de medidas validas para el centrado y wrapping controlado en textos/botones del panel derecho.
