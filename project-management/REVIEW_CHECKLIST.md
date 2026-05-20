# Checklist de Revision

Usar antes de aceptar cambios importantes, commits o merges.

## 1. Restricciones de estructuras

- [ ] No usa `ArrayList`.
- [ ] No usa `LinkedList`.
- [ ] No usa `HashMap`.
- [ ] No usa `HashSet`.
- [ ] No usa `Stack`.
- [ ] No usa `Queue`.
- [ ] No usa `PriorityQueue`.
- [ ] No usa `TreeMap`.
- [ ] No usa `java.util.*` para sustituir estructuras propias.
- [ ] La matriz de cuevas no usa `Celda[][]`.
- [ ] Las listas, colas, pilas y grafos usados son propios.

## 2. Arquitectura

- [ ] Respeta `ARCHITECTURE.md`.
- [ ] Respeta `DECISIONS.md`.
- [ ] No mezcla JavaFX con logica interna.
- [ ] No mezcla JSON con reglas de juego.
- [ ] Las acciones principales pasan por `Partida` o clase equivalente.
- [ ] Las clases tienen responsabilidades claras.

## 3. Area de trabajo

- [ ] El agente solo toco archivos permitidos por su tarea.
- [ ] Si toco otra zona, lo justifico y pidio permiso.
- [ ] No rompio trabajo de otro integrante.

## 4. Funcionalidad

- [ ] El cambio cumple el criterio de terminado de `TASKS.md`.
- [ ] Hay pruebas para logica no visual cuando aplica.
- [ ] No rompe tests existentes.
- [ ] El codigo compila o se indica por que no se pudo comprobar.
- [ ] El codigo esta comentado en profundidad en metodos, algoritmos y decisiones ya aprobadas por el grupo que no sean evidentes.
- [ ] Los comentarios ayudan a explicar el codigo en la memoria o defensa.

## 5. Documentacion

- [ ] `SCRATCHPAD.md` actualizado.
- [ ] `TASKS.md` actualizado.
- [ ] `DECISIONS.md` actualizado si hubo decision nueva.
- [ ] `IA_DIARY.md` actualizado si se uso IA para producir el cambio.
- [ ] UML actualizado si cambia estructura importante.

## 6. Revision de entrega

- [ ] Hay log visible o consultable.
- [ ] Hay inventario visible o consultable.
- [ ] Hay gestion de errores basica.
- [ ] JSON de configuracion existe.
- [ ] Guardar/cargar partida existe.
- [ ] Hay memoria/UML/diario IA en progreso.
