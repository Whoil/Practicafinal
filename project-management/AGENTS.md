# Reglas Para Agentes

## 1. Reglas globales

Todos los agentes deben cumplir:

- Al inicio de cada sesion, pedir o confirmar quien es el humano que esta hablando y que parte del proyecto representa.
- No empezar a trabajar hasta tener claro si la sesion corresponde a Parte A, Parte B, Parte C o revision.
- Antes de trabajar, comprobar si GitHub contiene cambios nuevos de otros agentes.
- Antes de empezar a modificar codigo o documentacion, actualizar automaticamente la rama de trabajo con la ultima version de `origin/main` siempre que no haya conflictos.
- Leer `project-management/PRD.md`, `ARCHITECTURE.md`, `TASKS.md` y `DECISIONS.md` antes de proponer cambios.
- Leer tambien `SCRATCHPAD.md`, `GITHUB_WORKFLOW.md` y el archivo especifico del agente antes de modificar codigo.
- Comprobar si otros agentes han modificado los documentos de coordinacion desde la ultima sesion.
- No empezar a programar sin una tarea asignada en `TASKS.md`.
- No modificar archivos fuera de su area asignada.
- No modificar archivos compartidos sin autorizacion humana previa.
- No tomar decisiones criticas de arquitectura, mecanicas, estructuras, diseno, alcance, JSON, JavaFX o flujo Git. Los agentes programan y proponen; las decisiones las toma el grupo.
- No usar colecciones prohibidas.
- No usar arrays para representar la matriz de cuevas.
- No cambiar decisiones de arquitectura sin registrarlo en `DECISIONS.md`.
- Escribir codigo comentado en profundidad, especialmente en estructuras propias, algoritmos, reglas de juego, JSON y decisiones ya aprobadas por el grupo que no sean evidentes al leer el codigo.
- Actualizar `SCRATCHPAD.md` despues de cualquier cambio relevante.
- Actualizar `TASKS.md` si completa, bloquea o crea una tarea.
- Actualizar `IA_DIARY.md` si el cambio se ha producido con ayuda de IA.
- Mantener los documentos de coordinacion actualizados en GitHub mediante commit/push autorizado cuando haya cambios relevantes.
- Cada vez que realice un cambio significativo o cualquier cambio de documentacion, debe pedir explicitamente autorizacion humana para hacer commit y push.
- Antes de cerrar una sesion, registrar un resumen de cierre en `SCRATCHPAD.md`.
- No hacer commit ni push sin confirmacion humana.
- Ejecutar pruebas relevantes cuando sea posible.
- Si el agente escribe o modifica codigo no visual, debe crear o actualizar tests unitarios con JUnit y ejecutarlos.
- Si no puede ejecutar los tests JUnit, debe explicar el motivo en el resumen de sesion.
- Todo cambio de codigo debe pasar por revision de un Agente Revisor independiente antes de aceptarse para merge.
- Si una estructura propia necesaria no existe, preguntar antes de crearla.

## 1.1 Comentarios en el codigo

El codigo debe estar comentado en profundidad para que cualquier integrante pueda explicarlo en la defensa.

Debe comentarse especialmente:

- Estructuras propias y sus operaciones.
- Algoritmos como BFS, caminos minimos y turnos.
- Reglas de combate, victoria y derrota.
- Carga y guardado JSON.
- Metodos publicos importantes.
- Decisiones ya aprobadas por el grupo que puedan parecer raras o poco evidentes al leer el codigo.

No hace falta comentar cada linea trivial, pero si explicar la intencion, precondiciones, postcondiciones y pasos importantes de cada bloque complejo.

Importante:

```text
Comentar una decision no autoriza al agente a tomarla.
Los agentes solo pueden documentar decisiones ya aprobadas por el grupo.
Si aparece una decision nueva durante la implementacion, deben parar y pedir autorizacion humana.
```

## 1.2 Inicio de sesion

Al comenzar cada sesion, el humano debe decir quien es y que rol representa.

Ejemplos:

```text
Soy Alvaro, Parte A.
Soy Guille, Parte B.
Soy Hector, Parte C.
Soy [Nombre], estoy revisando una PR.
```

El agente debe responder confirmando:

```text
Confirmado: trabajamos como Parte A / Parte B / Parte C / Revisor.
Area permitida: ...
Tareas abiertas relevantes: ...
```

Si el humano no se identifica, el agente debe preguntarlo antes de hacer cambios.

Despues de identificar la parte, el agente debe confirmar la rama de trabajo correspondiente:

```text
Parte A -> feature/a-estructuras
Parte B -> feature/b-logica
Parte C -> feature/c-javafx-json-docs
Revision -> rama o PR indicada por el humano
```

El agente no debe modificar archivos ni preparar commits en una rama que no corresponda a la parte identificada, salvo autorizacion humana explicita.

## 1.3 Sincronizacion de documentacion

Los archivos de `project-management/` son el mecanismo principal para coordinar agentes.

Antes de trabajar, cada agente debe:

```text
1. Comprobar la rama actual.
2. Consultar si hay cambios remotos.
3. Integrar o pedir ayuda humana si hay cambios de otros agentes.
4. Leer los documentos actualizados.
5. Confirmar que su tarea sigue vigente.
6. Cambiar a su rama de trabajo asignada si no esta ya en ella.
7. Actualizar su rama con `origin/main` antes de programar.
```

La actualizacion de rama debe seguir esta regla:

```text
Si la actualizacion con origin/main no tiene conflictos, el agente debe hacerla antes de trabajar.
Si aparecen conflictos, debe parar y pedir autorizacion humana.
Si la actualizacion requiere push de la rama, debe pedir autorizacion humana para subirla.
```

Despues de trabajar, cada agente debe:

```text
1. Actualizar SCRATCHPAD.md.
2. Actualizar TASKS.md.
3. Actualizar IA_DIARY.md si procede.
4. Actualizar DECISIONS.md si hubo una decision humana nueva.
5. Indicar en el resumen si hay documentos pendientes de subir.
6. Pedir autorizacion humana explicita para commit/push si hubo cambios significativos o documentales.
```

Si detecta que otro agente modifico un documento de coordinacion, debe leerlo antes de continuar y avisar si afecta a su tarea.

## 1.4 Cambio significativo

Se considera cambio significativo cualquier cambio que:

- Afecte a la logica del juego.
- Afecte a la estructura o arquitectura del proyecto.
- Afecte a la documentacion de coordinacion, requisitos, decisiones, tareas o agentes.
- Tenga impacto en el trabajo de otros companeros.
- Tenga impacto en otras ramas, especialmente `main`.

Todo cambio significativo requiere:

```text
1. Actualizar documentacion si procede.
2. Registrar resumen en SCRATCHPAD.md.
3. Pedir autorizacion explicita para commit/push.
4. Si es codigo, incluir tests JUnit cuando aplique.
5. Si es codigo, pasar revision del Agente Revisor Independiente antes de merge.
```

## 1.5 Modo economico de agentes

Para reducir consumo de tokens sin romper la estructura de coordinacion, los agentes deben trabajar con contexto minimo y documentos compartidos como fuente de verdad.

Regla principal:

```text
No se duplica el contenido completo de project-management en cada prompt.
El agente lee solo los documentos necesarios para su rol, tarea y riesgo.
```

El workflow mantiene la estructura existente:

- `AGENTS.md` define reglas globales, roles y limites.
- `agents/AGENT_*.md` define el contrato especifico de cada agente.
- `TASKS.md` define que se puede trabajar y cuando termina.
- `SCRATCHPAD.md` guarda el estado vivo de sesiones y bloqueos.
- `DECISIONS.md` guarda decisiones humanas aprobadas.
- `IA_DIARY.md` registra el uso relevante de IA.
- `templates/` contiene formatos reutilizables para no rehacer prompts largos.

### 1.5.1 Lectura escalonada

Antes de trabajar, el agente debe leer por niveles:

```text
Nivel 1 obligatorio:
- AGENTS.md
- TASKS.md
- archivo especifico del agente en agents/

Nivel 2 si afecta diseno, arquitectura o alcance:
- PRD.md
- ARCHITECTURE.md
- DECISIONS.md

Nivel 3 si continua una sesion o toca coordinacion:
- SCRATCHPAD.md
- IA_DIARY.md
- GITHUB_WORKFLOW.md
```

Si la tarea es pequena y no toca codigo ni decisiones compartidas, el agente puede resumir que ha revisado solo el nivel necesario.

### 1.5.2 Uso de agentes auxiliares

El agente principal u orquestador solo debe lanzar agentes auxiliares cuando haya una ventaja clara:

- Dos o mas tareas independientes que puedan avanzar en paralelo.
- Una revision independiente de codigo ya terminado.
- Una busqueda acotada que no bloquee el siguiente paso del agente principal.
- Un cambio con archivos claramente separados por rol.

No debe lanzar un agente auxiliar para:

- Leer uno o dos archivos que puede revisar directamente.
- Hacer una busqueda simple con `rg`.
- Repetir una investigacion ya registrada en `SCRATCHPAD.md`.
- Tocar archivos compartidos sin autorizacion humana.

### 1.5.3 Prompt compacto obligatorio

Cuando se delegue a un agente auxiliar, debe usarse un prompt corto basado en `templates/AGENT_BRIEF_TEMPLATE.md`.

El prompt debe incluir:

- Rol del agente.
- Tarea concreta.
- Documentos o archivos que puede leer.
- Archivos que puede modificar, si procede.
- Archivos prohibidos.
- Salida esperada breve.

El agente auxiliar debe responder con:

```text
Resultado:
- ...

Archivos relevantes:
- ruta:linea

Cambios realizados:
- ...

Riesgos o dudas:
- ...
```

No debe copiar documentos completos en su respuesta.

### 1.5.4 Reutilizacion de contexto

Si un agente ya investigo una zona del proyecto, se debe reutilizar su resumen registrado en `SCRATCHPAD.md` antes de lanzar otro agente.

Si se necesita una segunda pregunta al mismo agente, debe enviarse como continuacion breve, no como prompt completo desde cero, siempre que el contexto siga siendo valido.

### 1.5.5 Cierre compacto

El cierre de sesion mantiene el formato obligatorio, pero debe ser breve:

- Solo listar archivos realmente modificados.
- No pegar salidas completas de tests salvo errores relevantes.
- Registrar decisiones nuevas en `DECISIONS.md`, no repetirlas enteras en `SCRATCHPAD.md`.
- Registrar en `IA_DIARY.md` solo usos relevantes, no cada busqueda trivial.

## 2. Colecciones prohibidas

No usar:

```text
ArrayList
LinkedList
HashMap
HashSet
Stack
Queue
PriorityQueue
TreeMap
```

No usar `java.util.*` como sustituto de estructuras propias.

## 3. Rol A - Agente de estructuras

Responsable de:

- Parte A del proyecto junto con Alvaro.
- Matriz propia.
- Cueva.
- Celda.
- Grafo de cuevas.
- BFS sobre celdas.
- Camino minimo.
- Tests de estructuras.
- Justificacion de costes.

Nota:

```text
Alvaro y Codex-A Estructuras quedan asignados a la Parte A.
No deben invadir Parte B o Parte C salvo autorizacion previa.
```

Archivos permitidos inicialmente:

```text
src/Estructuras/
src/modelo/mapa/
src/modelo/juego/Mazmorra.java
test/
project-management/
diagrama_inicial_juego.puml
```

No debe tocar:

- JavaFX.
- Controladores visuales.
- Reglas de combate salvo contrato necesario.
- JSON salvo que haya acuerdo explicito.

## 4. Rol B - Agente de logica

Responsable de:

- `Partida`.
- Turnos.
- `Personaje`, `Jugador`, `Enemigo`, `Boss`.
- Combate.
- Inventario.
- Objetos y efectos.
- Victoria y derrota.
- Tests de logica.

Archivos permitidos inicialmente:

```text
src/modelo/juego/
src/modelo/personajes/
src/modelo/objetos/
src/modelo/acciones/
src/excepciones/
test/
project-management/
diagrama_inicial_juego.puml
```

No debe tocar:

- Implementaciones base de estructuras.
- JavaFX.
- Formato JSON definitivo salvo contrato acordado.

## 5. Rol C - Agente JavaFX, JSON y documentacion

Responsable de:

- Interfaz JavaFX.
- Controladores.
- Carga JSON.
- Guardado JSON.
- Bocetos.
- Memoria.
- UML.
- Diario IA.
- Video/guion de entrega.

Archivos permitidos inicialmente:

```text
src/vista/
src/controlador/
src/json/
docs/
project-management/
diagrama_inicial_juego.puml
```

No debe tocar:

- Estructuras propias.
- BFS.
- Reglas internas de combate.
- Movimiento interno salvo mediante metodos publicos de `Partida`.

## 6. Rol R - Agente revisor independiente

Responsable de revisar cambios antes de aceptarlos.

Este agente es independiente de los agentes A, B y C. No debe revisar como si fuera el autor del codigo, sino como control externo del proyecto.

Debe comprobar:

- No hay colecciones prohibidas.
- No hay matriz con arrays Java.
- No se mezclan responsabilidades.
- El codigo compila.
- Hay tests cuando aplica.
- Los cambios de codigo no visual tienen tests JUnit asociados.
- Los tests JUnit se han ejecutado o se ha explicado por que no se pudieron ejecutar.
- El cambio respeta `PRD.md` y `ARCHITECTURE.md`.
- `TASKS.md` y `SCRATCHPAD.md` estan actualizados.

El agente revisor no implementa cambios grandes. Solo informa hallazgos y propone correcciones.

Regla:

```text
Todo cambio de codigo debe ser revisado por el Agente Revisor independiente antes de merge a main.
```

## 7. Human in the loop

Los humanos deciden:

- Aceptar cambios de arquitectura.
- Crear nuevas estructuras de datos.
- Autorizar commits.
- Autorizar push.
- Aceptar merges.
- Activar extras.

## 8. Archivos compartidos

Estos archivos o zonas requieren autorizacion humana antes de modificarse, aunque un agente los necesite:

```text
project-management/PRD.md
project-management/ARCHITECTURE.md
project-management/DECISIONS.md
project-management/TASKS.md
diagrama_inicial_juego.puml
src/modelo/juego/Partida.java
src/modelo/juego/Mazmorra.java
src/modelo/mapa/Cueva.java
src/modelo/mapa/Celda.java
src/modelo/personajes/Jugador.java
src/modelo/objetos/Objeto.java
```

Regla:

```text
Si un archivo afecta a mas de un rol, el agente debe pedir permiso antes de editarlo.
```

## 9. GitHub

Los agentes pueden:

- Consultar `git status`.
- Consultar diferencias con `git diff`.
- Consultar cambios remotos con `git fetch` o equivalente autorizado.
- Comparar su rama con `origin/main`.
- Crear una rama asignada si el humano lo autoriza.
- Preparar cambios en su rama.
- Proponer un mensaje de commit en espanol, claro y descriptivo.
- Hacer commit solo con autorizacion humana.
- Hacer push solo con autorizacion humana.
- Subir cambios de documentacion solo con autorizacion humana.
- Preparar un resumen para Pull Request.

Los agentes no pueden sin autorizacion explicita:

- Hacer push directo a `main`.
- Hacer merge a `main`.
- Hacer rebase.
- Hacer reset.
- Hacer force push.
- Borrar ramas.
- Resolver conflictos entre ramas de forma automatica.
- Modificar protecciones del repositorio.

Ramas recomendadas:

```text
main
feature/a-estructuras
feature/b-logica
feature/c-javafx-json-docs
```

Cada agente trabaja en su propia rama y no toca ramas de otros agentes.

Regla de commits:

```text
Los commits de cada parte se hacen en su rama asignada.
No se hacen commits directamente en main salvo tareas de coordinacion autorizadas.
```

## 10. Resumen obligatorio por sesion

Cada sesion de trabajo con un agente debe terminar con:

```text
Resumen:
- Tarea trabajada.
- Archivos modificados.
- Cambios realizados.
- Pruebas ejecutadas.
- Pruebas no ejecutadas y motivo.
- Riesgos o posibles problemas.
- Archivos compartidos tocados o solicitados.
- Estado de TASKS.md.
- Si pide autorizacion para commit/push.
```

Tambien debe actualizar:

```text
project-management/SCRATCHPAD.md
project-management/TASKS.md
project-management/IA_DIARY.md
```

## 11. Cierre de sesion

Antes de cerrar una sesion, el agente debe anadir un resumen en `SCRATCHPAD.md`.

El resumen de cierre debe incluir:

```text
- Humano y parte.
- Rama usada.
- Tareas trabajadas.
- Archivos modificados.
- Cambios realizados.
- Commits y push realizados, si los hubo.
- Pruebas ejecutadas o no ejecutadas.
- Pendiente para la siguiente sesion.
- Riesgos o avisos.
```

Si se modifica `SCRATCHPAD.md` para registrar el cierre, el agente debe pedir autorizacion explicita para commit/push.
