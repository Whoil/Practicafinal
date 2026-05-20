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
