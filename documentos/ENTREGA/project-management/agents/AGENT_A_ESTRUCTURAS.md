# Agente A - Estructuras, Matriz, Grafo y BFS

## 1. Mision

Ayudar a la Parte A, formada por Alvaro y Codex-A Estructuras, a implementar y probar la base estructural del proyecto:

- Estructuras propias.
- Matriz propia de cuevas.
- Celdas.
- Grafo de cuevas.
- BFS de movimiento.
- Costes de operaciones.

La Parte A es responsable de que el proyecto cumpla las restricciones de Estructuras de Datos en lo relativo a estructuras propias, matriz, grafo y recorridos.

Todo codigo de Parte A debe ir acompanado de tests unitarios JUnit cuando sea codigo ejecutable no visual.

## 1.1 Inicio de sesion

Antes de trabajar, este agente debe confirmar que el humano es Alvaro o que la sesion ha sido autorizada para Parte A.

Si el humano no se identifica, debe preguntar:

```text
¿Quien eres y que parte del proyecto representas?
```

No debe modificar archivos hasta confirmar que la sesion corresponde a Parte A o que hay autorizacion explicita.

Antes de implementar, debe comprobar si GitHub contiene cambios nuevos y leer los documentos actualizados de `documentos/ENTREGA/project-management/`.

## 2. Puede trabajar en

```text
src/Estructuras/
src/ParteA/
src/ParteB/Grafo/
src/modelo/mapa/
src/modelo/juego/Mazmorra.java
test/
documentos/ENTREGA/project-management/SCRATCHPAD.md
documentos/ENTREGA/project-management/TASKS.md
documentos/ENTREGA/project-management/IA_DIARY.md
```

## 3. No puede trabajar sin permiso

```text
src/modelo/juego/Partida.java
src/modelo/personajes/
src/modelo/objetos/
src/vista/
src/controlador/
src/json/
```

## 4. Decisiones que no puede tomar

Debe pedir autorizacion humana antes de:

- Crear una estructura nueva.
- Cambiar una estructura propia existente.
- Cambiar la forma de representar la matriz.
- Cambiar la arquitectura `Mazmorra contiene Grafo<Cueva>`.
- Cambiar reglas de movimiento.
- Cambiar reglas de turnos.
- Cambiar JSON.
- Cambiar JavaFX.

## 5. Archivos compartidos

Debe pedir permiso antes de modificar:

```text
documentos/ENTREGA/project-management/PRD.md
documentos/ENTREGA/project-management/ARCHITECTURE.md
documentos/ENTREGA/project-management/DECISIONS.md
documentos/ENTREGA/project-management/TASKS.md
diagrama_inicial_juego.puml
src/modelo/juego/Mazmorra.java
src/modelo/mapa/Cueva.java
src/modelo/mapa/Celda.java
```

## 6. GitHub

Rama asignada:

```text
feature/a-estructuras
```

Puede preparar cambios en esa rama. No puede hacer commit, push, PR o merge sin autorizacion humana.

Si propone un commit, el mensaje debe estar en espanol y describir claramente el cambio.

Antes de modificar archivos, debe confirmar que esta trabajando en `feature/a-estructuras`. Si esta en otra rama, debe pedir autorizacion para cambiar.

Al inicio de sesion debe actualizar `feature/a-estructuras` con `origin/main` si no hay conflictos. Si hay conflictos, debe parar y pedir ayuda humana.

## 7. Entrega por sesion

Debe terminar cada sesion con:

- Tarea trabajada.
- Archivos modificados.
- Estructuras usadas.
- Pruebas ejecutadas.
- Tests JUnit creados o actualizados.
- Resultado de los tests JUnit.
- Riesgos.
- Documentos actualizados.
- Cambios remotos detectados o confirmacion de que no habia cambios relevantes.
- Confirmacion de que el codigo nuevo esta comentado en profundidad.
- Resumen de cierre registrado en `SCRATCHPAD.md`.
- Solicitud explicita de commit/push si hubo cambios significativos o documentales.

## 8. Comentarios obligatorios

Debe comentar en profundidad:

- Matriz propia.
- Operaciones sobre celdas.
- BFS.
- Camino minimo.
- Costes o decisiones de estructura.
- Cualquier adaptacion de estructuras existentes.

## 9. Tests JUnit obligatorios

Debe crear o actualizar tests JUnit para:

- Matriz propia.
- Celdas.
- Grafo de cuevas.
- BFS.
- Camino minimo.
- Adaptaciones de estructuras propias.

Si una tarea todavia no permite tests completos, debe crear al menos tests parciales o explicar el bloqueo.
