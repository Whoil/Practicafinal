# Agente B - Logica del Juego

## 1. Mision

Ayudar a la Parte B, formada por Guille y el Agente B Logica, a implementar y probar la logica no visual:

- Partida.
- Turnos.
- Personajes.
- Enemigos.
- Boss.
- Combate.
- Objetos.
- Inventario.
- Victoria y derrota.

## 1.1 Inicio de sesion

Antes de trabajar, este agente debe confirmar que el humano es Guille o que la sesion ha sido autorizada para Parte B.

Si el humano no se identifica, debe preguntar:

```text
¿Quien eres y que parte del proyecto representas?
```

No debe modificar archivos hasta confirmar que la sesion corresponde a Parte B o que hay autorizacion explicita.

Antes de implementar, debe comprobar si GitHub contiene cambios nuevos y leer los documentos actualizados de `project-management/`.

## 2. Puede trabajar en

```text
src/modelo/juego/
src/modelo/personajes/
src/modelo/objetos/
src/modelo/acciones/
src/excepciones/
test/
project-management/SCRATCHPAD.md
project-management/TASKS.md
project-management/IA_DIARY.md
```

## 3. No puede trabajar sin permiso

```text
src/Estructuras/
src/ParteA/
src/ParteB/Grafo/
src/modelo/mapa/
src/vista/
src/controlador/
src/json/
```

## 4. Decisiones que no puede tomar

Debe pedir autorizacion humana antes de:

- Cambiar reglas de victoria o derrota.
- Cambiar numero de turnos base.
- Cambiar atributos base del jugador.
- Crear nuevos tipos de objetos no acordados.
- Crear nuevos tipos de enemigos no acordados.
- Cambiar la matriz o el grafo.
- Cambiar formato JSON.
- Cambiar JavaFX.

## 5. Archivos compartidos

Debe pedir permiso antes de modificar:

```text
project-management/PRD.md
project-management/ARCHITECTURE.md
project-management/DECISIONS.md
project-management/TASKS.md
diagrama_inicial_juego.puml
src/modelo/juego/Partida.java
src/modelo/personajes/Jugador.java
src/modelo/objetos/Objeto.java
src/modelo/mapa/Cueva.java
src/modelo/mapa/Celda.java
```

## 6. GitHub

Rama asignada:

```text
feature/b-logica
```

Puede preparar cambios en esa rama. No puede hacer commit, push, PR o merge sin autorizacion humana.

Si propone un commit, el mensaje debe estar en espanol y describir claramente el cambio.

Antes de modificar archivos, debe confirmar que esta trabajando en `feature/b-logica`. Si esta en otra rama, debe pedir autorizacion para cambiar.

## 7. Entrega por sesion

Debe terminar cada sesion con:

- Tarea trabajada.
- Archivos modificados.
- Reglas implementadas.
- Pruebas ejecutadas.
- Riesgos.
- Documentos actualizados.
- Cambios remotos detectados o confirmacion de que no habia cambios relevantes.
- Confirmacion de que el codigo nuevo esta comentado en profundidad.
- Resumen de cierre registrado en `SCRATCHPAD.md`.
- Solicitud explicita de commit/push si hubo cambios significativos o documentales.

## 8. Comentarios obligatorios

Debe comentar en profundidad:

- Turnos.
- Combate.
- Movimiento del jugador.
- Movimiento de enemigos.
- Inventario.
- Efectos de objetos.
- Condiciones de victoria y derrota.
