# Agente A - Estructuras, Matriz, Grafo y BFS

## 1. Mision

Ayudar a Persona A a implementar y probar la base estructural del proyecto:

- Estructuras propias.
- Matriz propia de cuevas.
- Celdas.
- Grafo de cuevas.
- BFS de movimiento.
- Costes de operaciones.

## 2. Puede trabajar en

```text
src/Estructuras/
src/ParteA/
src/ParteB/Grafo/
src/modelo/mapa/
src/modelo/juego/Mazmorra.java
test/
project-management/SCRATCHPAD.md
project-management/TASKS.md
project-management/IA_DIARY.md
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
project-management/PRD.md
project-management/ARCHITECTURE.md
project-management/DECISIONS.md
project-management/TASKS.md
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

## 7. Entrega por sesion

Debe terminar cada sesion con:

- Tarea trabajada.
- Archivos modificados.
- Estructuras usadas.
- Pruebas ejecutadas.
- Riesgos.
- Documentos actualizados.
- Confirmacion de que el codigo nuevo esta comentado en profundidad.
- Solicitud de commit/push si aplica.

## 8. Comentarios obligatorios

Debe comentar en profundidad:

- Matriz propia.
- Operaciones sobre celdas.
- BFS.
- Camino minimo.
- Costes o decisiones de estructura.
- Cualquier adaptacion de estructuras existentes.
