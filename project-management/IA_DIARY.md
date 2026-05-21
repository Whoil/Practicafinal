# Diario de Uso de IA

Este archivo servira como base para la entrega de metodologia.

Cada uso relevante de agentes debe registrarse.

## Formato

```markdown
## YYYY-MM-DD - Persona

### Agente o herramienta

Nombre del agente/herramienta.

### Objetivo

Que se queria conseguir.

### Prompt o resumen del prompt

Resumen claro de lo pedido.

### Resultado

Que produjo el agente.

### Cambios aceptados

Que se incorporo al proyecto.

### Cambios rechazados o modificados

Que no se acepto y por que.

### Critica

Valoracion del uso de IA y mejoras para siguientes prompts.
```

## 2026-05-20 - Coordinacion

### Agente o herramienta

Codex.

### Objetivo

Organizar metodologia de trabajo con agentes antes de conectar GitHub.

### Prompt o resumen del prompt

Se pidio estructurar la forma de trabajo del grupo, definir roles de agentes, archivos de coordinacion, reglas de revision y metodologia human-in-the-loop.

### Resultado

Se crearon documentos base:

- `PRD.md`
- `ARCHITECTURE.md`
- `AGENTS.md`
- `TASKS.md`
- `DECISIONS.md`
- `SCRATCHPAD.md`
- `REVIEW_CHECKLIST.md`
- `IA_DIARY.md`

### Cambios aceptados

Se acepta usar carpeta `project-management` como centro de coordinacion.

### Cambios rechazados o modificados

No se implemento codigo del juego. Se decidio preparar organizacion primero.

### Critica

La organizacion ayuda a reducir conflictos entre agentes, pero debe mantenerse actualizada. Si los agentes no actualizan `SCRATCHPAD.md` y `TASKS.md`, el sistema pierde valor.

## 2026-05-21 - Alvaro

### Agente o herramienta

Codex.

### Objetivo

Optimizar el workflow de agentes para reducir consumo de tokens sin abandonar la estructura de `project-management`.

### Prompt o resumen del prompt

Alvaro pidio dejar aparcada la Parte A y ajustar el workflow usado con agentes, manteniendo la estructura existente de la carpeta `project-management`.

### Resultado

Se documento un modo economico de agentes en `AGENTS.md` y se creo una plantilla breve para delegar tareas en `templates/AGENT_BRIEF_TEMPLATE.md`.

### Cambios aceptados

- Lectura escalonada de documentos.
- Delegacion solo cuando haya ventaja clara.
- Prompts compactos para agentes auxiliares.
- Respuestas breves con archivos relevantes, cambios y riesgos.

### Cambios rechazados o modificados

No se creo una estructura nueva fuera de `project-management`; se adapto el workflow a los documentos y carpetas ya existentes.

### Critica

La mejora reduce duplicacion de contexto, pero solo funcionara si los agentes registran buenos resumenes en `SCRATCHPAD.md` y evitan repetir investigaciones ya hechas.
