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
