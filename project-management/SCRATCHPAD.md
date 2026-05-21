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
