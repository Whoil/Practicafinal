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

## 2026-05-20 - Guillermo

### Agente o herramienta

Codex / Agente B Logica.

### Objetivo

Preparar la primera sesion de Parte B y cerrar el diseno inicial de logica del juego antes de programar.

### Prompt o resumen del prompt

Guillermo se identifico como responsable de Parte B y pidio trabajar segun los documentos del proyecto. Se acordo releer `project-management` al inicio de cada sesion y revisar los archivos actualizados antes de cualquier commit/push. Despues se revisaron y decidieron, por partes, los elementos de jugador, enemigos, objetos, inventario, turnos y reglas basicas.

### Resultado

Se obtuvo un diseno inicial para Parte B:

- Jugador con vida 100, ataque 15, defensa 5, movimiento 3, 40 turnos, inventario y objeto equipado.
- Enemigos: esqueleto, orco, mago y boss, con valores base definidos.
- Objetos: pocion, espada, arco, llave y escudo.
- Pociones de cura e invisibilidad.
- Llaves de puertas o cofres, encontradas en el suelo o como drop.
- Drops fijos sin aleatoriedad.
- Cofres con 0 o 1 objeto.
- Turno con 1 movimiento y 1 accion.
- Equipar/cambiar objeto no consume movimiento; usar objeto consume accion.
- Penalizacion de defensa de 2 puntos al bajar al 75% o menos de vida, excepto boss.

### Cambios aceptados

Se aceptaron las decisiones de diseno anteriores como base de trabajo de Guillermo / Parte B para futuras tareas `B-01`, `B-02` y `B-03`.

### Cambios rechazados o modificados

Se rechazo seguir implementando codigo durante esta sesion. Tambien se decidio no usar aleatoriedad en los drops por ahora para reducir complejidad.

### Critica

La sesion fue util para concretar reglas antes de programar. Para proximas sesiones conviene confirmar con el grupo si arco, escudo e invisibilidad pasan a ser obligatorios o siguen siendo extras, porque los documentos iniciales los trataban como opcionales. Tambien se debe documentar formalmente la regla del 75% antes de implementarla. La interrupcion antes del primer intento de push ayudo a detectar que habia documentacion remota mas reciente; desde ahora se revisaran archivos actualizados antes de cualquier commit/push.
