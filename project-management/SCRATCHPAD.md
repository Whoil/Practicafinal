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
