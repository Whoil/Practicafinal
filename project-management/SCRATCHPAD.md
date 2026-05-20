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
