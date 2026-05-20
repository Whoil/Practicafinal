# Checklist de Revision

Usar antes de aceptar cambios importantes, commits o merges.

## 0. Inicio de sesion y rol

- [ ] El humano se identifico al inicio de la sesion.
- [ ] El agente confirmo rol y area de trabajo antes de modificar archivos.
- [ ] La tarea corresponde al rol confirmado.
- [ ] El agente trabajo en la rama correspondiente a su parte.
- [ ] El agente comprobo si habia cambios remotos en GitHub.
- [ ] El agente leyo la documentacion actualizada antes de trabajar.

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
- [ ] Si hay codigo no visual nuevo o modificado, hay tests unitarios JUnit asociados.
- [ ] Los tests JUnit cubren casos normales y algun caso limite razonable.
- [ ] Los tests JUnit se ejecutaron y el resultado queda indicado.
- [ ] Si no se pudieron ejecutar tests JUnit, el motivo queda explicado.
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
- [ ] Los cambios de documentacion necesarios estan listos para commit/push autorizado.
- [ ] Se comprobo si otros agentes habian cambiado documentos relacionados.
- [ ] Si hubo cambios significativos o documentales, el agente pidio autorizacion explicita para commit/push.
- [ ] El mensaje de commit propuesto esta en espanol y describe claramente el cambio.
- [ ] El commit se hizo o se propone hacer en la rama de trabajo correcta, no directamente en `main`.
- [ ] El agente registro cierre de sesion en `SCRATCHPAD.md`.

## 6. Revision de entrega

- [ ] Hay log visible o consultable.
- [ ] Hay inventario visible o consultable.
- [ ] Hay gestion de errores basica.
- [ ] JSON de configuracion existe.
- [ ] Guardar/cargar partida existe.
- [ ] Hay memoria/UML/diario IA en progreso.

## 7. Revision independiente

- [ ] El cambio de codigo fue revisado por el Agente Revisor independiente.
- [ ] El revisor no es el mismo agente que implemento el cambio.
- [ ] El informe del revisor indica si recomienda aceptar o pedir cambios.
- [ ] Los hallazgos del revisor se resolvieron o quedaron aceptados por un humano.
