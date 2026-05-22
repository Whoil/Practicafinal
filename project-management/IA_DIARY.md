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

## 2026-05-21 - Alvaro

### Agente o herramienta

Codex.

### Objetivo

Fijar una politica de modelos para el proyecto que reduzca coste sin romper la coordinacion ni la calidad.

### Prompt o resumen del prompt

Alvaro pregunto si convenia usar GPT-5.5 o una version anterior para ahorrar tokens y despues pidio dejar la politica cerrada dentro de `project-management`.

### Resultado

Se documento en `AGENTS.md` una politica de modelos ligada al modo economico de agentes.

### Cambios aceptados

- `GPT-5.4` como modelo principal.
- `GPT-5.4-Mini` para tareas auxiliares ligeras.
- `GPT-5.5` solo para tareas dificiles o de alto impacto.
- `GPT-5.3-Codex` para implementacion mecanica acotada.
- Priorizar primero ahorro por contexto y workflow antes que por cambio de modelo.

### Cambios rechazados o modificados

No se impuso GPT-5.5 como modelo general del proyecto porque el coste adicional no compensa en el trabajo diario.

### Critica

La politica queda clara y util, pero su efecto real depende mas de la disciplina con el contexto y la delegacion que del nombre del modelo elegido.
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


## 2026-05-21 - Hector

### Agente o herramienta

Agente C JavaFX/JSON/Docs.

### Objetivo

Realizar la tarea C-01: disenar el JSON inicial de configuracion de las 3 cuevas de la mazmorra, incluyendo la configuracion de Gson en el proyecto y la creacion del cargador que construye los objetos del modelo.

### Prompt o resumen del prompt

Hector se identifico como Parte C y pidio iniciar con la tarea C-01. Se acordo el plan: configurar Gson, crear el JSON, los DTOs, el cargador y los tests. Hector tambien recordo actualizar SCRATCHPAD.md, TASKS.md e IA_DIARY.md.

### Resultado

- Gson 2.10.1 configurado en el proyecto (lib/, .iml, .idea/libraries).
- Fichero `datos/cuevas.json` con las 3 cuevas (facil 5x5, media 6x6, dificil 7x7), matrices de tipos de celda, enemigos, objetos y conexiones del grafo.
- 5 DTOs en `src/json/` para mapear el JSON.
- `CargadorConfiguracion.java` que lee el JSON con Gson y construye objetos Cueva y Mazmorra del modelo de Parte A.
- `ResultadoCarga.java` que devuelve la Mazmorra junto con las configuraciones de enemigos y objetos para que Parte B las use despues.
- 10 tests unitarios en `test/json/CargadorConfiguracionTest.java`.

### Cambios aceptados

- Todo el codigo de C-01: JSON, DTOs, cargador y tests.
- La configuracion de Gson en el proyecto.

### Cambios rechazados o modificados

Ninguno. Todo el plan se ejecuto segun lo acordado.

### Critica

La tarea salio segun lo planeado. La compilacion fue correcta tanto de src como de tests. Los tests no pudieron ejecutarse desde terminal por falta de runner JUnit standalone, pero se dejaron preparados para IntelliJ. Para futuras sesiones, convendria tener los mensajes de commit preparados y pedir autorizacion humana al final de la sesion.

## 2026-05-21 - Hector (sesion 2)

### Agente o herramienta

Agente C JavaFX/JSON/Docs.

### Objetivo

Resolver merge conflicts de la PR #3, actualizar la documentacion y cerrar la sesion.

### Prompt o resumen del prompt

Hector volvio al Agente C pidiendo ayuda para resolver conflictos de merge en la PR #3
(feature/c-javafx-json-docs -> main). Los conflictos estaban en IA_DIARY.md, SCRATCHPAD.md
y "Practica final.iml". Tras resolverlos, se pidio actualizar ambos documentos con el registro
de esta sesion.

### Resultado

- Identificados conflictos en 3 archivos al hacer `git merge main`.
- Resueltos: se mantuvo la entrada Gson en .iml (HEAD), se conservaron las entradas de sesion
  C-01 en los diarios (HEAD), se descartaron las versiones vacias de main.
- Commit del merge y push a origin.
- PR #3 actualizada: estado OPEN, mergeability en calculo por GitHub.
- Entradas de esta sesion anadidas a IA_DIARY.md y SCRATCHPAD.md.

### Cambios aceptados

- Merge commit ed1aea7 con la resolucion de conflictos.
- Ningun cambio funcional en el codigo.

### Cambios rechazados o modificados

Ninguno.

### Critica

Los conflictos fueron triviales: main tenia versiones vacias de los documentos de Parte C
porque la rama feature incorporaba contenido que main aun no tenia. Para evitar estos
conflictos en el futuro, conviene hacer `git merge main` en la feature branch antes de
crear la PR, o mantener los documentos sincronizados periodicamente.

## 2026-05-21 - Guillermo

### Agente o herramienta

Codex / Agente B Logica.

### Objetivo

Implementar la tarea `B-01 Modelo de personajes` respetando los documentos de coordinacion, el PDF de la practica y el alcance acordado con Guillermo.

### Prompt o resumen del prompt

Guillermo inicio una nueva sesion como responsable de Parte B. Pidio revisar GitHub y releer `project-management` antes de proponer o modificar nada. Despues pidio buscar en el PDF la indicacion sobre contratos/interfaces. Tras confirmar que el PDF exige declarar contratos necesarios, se acordo usar `Personaje` abstracto como contrato base para B-01 y no crear interfaces Java innecesarias.

Tambien se acordo que antes de programar se cerraria el alcance, que la regla del 75% quedaria para combate, que no se incluirian objetos ni inventario en B-01, y que al final se usaria revision independiente. Antes del commit se volvio a actualizar la rama con los cambios de Parte C.

### Resultado

Se implemento el modelo base de personajes:

- `Personaje`
- `Jugador`
- `Enemigo`
- `Boss`
- `TipoEnemigo`

Se crearon tests JUnit:

- `PersonajeTest`
- `EnemigoTest`

Guillermo verifico en IntelliJ cobertura del 100% para el paquete `modelo.personajes`. Se dejo `B-01` en estado `REVISION` porque la aceptacion final depende de la PR.

El Agente Revisor Independiente recomendo aceptar sin bloqueos. La nota de compatibilidad Java se corrigio sustituyendo `String.isBlank()` por `trim().isEmpty()`. Tambien se corrigio una errata de comentario en `TipoEnemigo` y se anadieron comentarios a las validaciones privadas de `Personaje`.

### Cambios aceptados

- `Personaje` como clase abstracta y contrato base.
- Constructores libres para no fijar valores por defecto dentro de las clases.
- Sin setters generales para estadisticas base.
- Comentarios en clases y metodos importantes, no en getters.
- Tests en `test/modelo/personajes/`, aprovechando la configuracion JUnit incorporada desde `main`.

### Cambios rechazados o modificados

- No se implemento `atacar`, porque pertenece a `B-03 Combate y turnos`.
- No se implemento inventario ni objeto equipado, porque pertenecen a `B-02`.
- No se implemento regla de bajada de defensa al 75%, porque se acordo dejarla para `B-03`.
- No se creo una interfaz Java adicional para `Personaje`, porque la clase abstracta ya cubre el contrato necesario de B-01.

### Critica

La sesion fue util para ajustar el trabajo al PDF: primero contratos y alcance, luego implementacion. El principal cuidado fue actualizar la rama con los cambios recientes de Parte A y Parte C antes de preparar la PR, porque `main` habia incorporado JSON, Gson, documentos actualizados y tests de otra parte. Tambien conviene mantener la disciplina de no mezclar B-01 con objetos, combate o turnos aunque parezcan cercanos.

## 2026-05-22 - Guillermo

### Agente o herramienta

Codex / Agente B Logica.

### Objetivo

Iniciar e implementar la tarea `B-02 Modelo de objetos e inventario`, manteniendo el alcance separado de B-03 y revisando antes los dos PDF del proyecto.

### Prompt o resumen del prompt

Guillermo pidio empezar la sesion del dia revisando GitHub y `project-management`, despues leer los dos PDF del proyecto para comprobar que estaba hecho y que faltaba en Parte B. Tras confirmar que B-01 estaba cubierta y que B-02 debia centrarse en objetos e inventario, Guillermo pidio decidir el alcance punto por punto antes de programar.

Se acordo incluir `Pocion`, `Arma`, `Espada`, `Arco`, `Escudo` y `Llave`; permitir objetos del mismo tipo si tienen ids distintos; rechazar ids duplicados; usar ranuras separadas de arma y escudo; hacer que el arco ocupe las dos manos; dejar llaves sin equipar; implementar solo pocion de cura; y dejar drops, cofres, mapa, JSON, JavaFX, turnos, combate, invisibilidad y regla del 75% fuera de B-02.

### Resultado

Se implemento la base de B-02:

- `ListaDE`, `ElementoDE` e `IteradorDE` traidos desde las estructuras del grupo.
- `ListaDE` adaptada para no exigir `Comparable`, usando `equals`, porque el inventario necesita guardar objetos sin orden natural.
- Paquete `modelo.objetos` con `Objeto`, `Pocion`, `Arma`, `Espada`, `Arco`, `Escudo`, `Llave` y `TipoLlave`.
- `Jugador` ampliado con inventario `ListaDE<Objeto>`, equipo de arma/escudo, ataque/defensa total, uso de pocion de cura y reglas de duplicados por id.
- Tests JUnit nuevos para `ListaDE`, objetos e inventario/equipo del jugador.
- Revision independiente completada sin bloqueos funcionales.
- Verificacion local por reflexion: 105 tests ejecutados, 0 fallos.

### Cambios aceptados

- Incluir `Arco` y `Escudo` en B-02.
- Permitir objetos repetidos por tipo pero no duplicar el mismo `id`.
- Arco a dos manos: al equiparlo desequipa el escudo y bloquea equipar escudo mientras siga equipado.
- Llaves no equipables: quedan en inventario con tipo y codigo de cerradura.
- Pocion de cura de 25 puntos consumible.
- `getInventario()` devuelve copia defensiva.

### Cambios rechazados o modificados

- No se implementa pocion de invisibilidad porque depende de turnos y ataques enemigos.
- No se implementan drops, cofres ni recoger del suelo porque dependen de combate/mapa.
- No se integra con JSON ni JavaFX en B-02.
- No se implementa combate, turnos ni regla del 75%.
- El revisor independiente marco que `src/Estructuras/` estaba fuera del alcance normal de B-02; se documento la autorizacion expresa de Guillermo en `TASKS.md`.

### Critica

La sesion fue util para frenar antes de programar y cerrar decisiones pequenas pero importantes: especialmente equipo por ranuras, arco a dos manos y llaves sin equipar. El principal ajuste fue traer `ListaDE` desde las estructuras del grupo sin mantener `Comparable`, porque exigir orden natural a `Objeto` habria sido artificial. Para futuras sesiones conviene mantener esta misma disciplina: decidir primero, implementar despues, y documentar cualquier excepcion de alcance compartido antes del commit.

## 2026-05-22 - Alvaro

### Agente o herramienta

Codex-A Estructuras, trabajando con permiso de coordinacion sobre la primera version de `Partida`.

### Objetivo

Planificar y dejar implementada una primera version funcional de la interfaz y la logica de partida, despues de integrar desde `main` la parte ya terminada de personajes, objetos e inventario.

### Prompt o resumen del prompt

Alvaro pidio actualizar la rama con `main`, releer `TASKS.md` y revisar las nuevas clases de personajes, objetos e inventario. Despues se decidio planificar antes de implementar: `Partida` debia ser la clase que coordina la logica del juego, pero no la responsable de conectar cuevas ni de crear la mazmorra desde cero.

Durante la sesion se cerraron reglas de movimiento, recogida, combate, turnos, puertas, llave final, victoria y log. Alvaro detecto un error importante de diseno: metodos como `conectarCuevas` no debian estar en `Partida`, porque pertenecen a `Mazmorra`. Ese error se corrigio y se registro en el postmortem. Tambien se pidio revision independiente sobre las clases nuevas, se corrigieron los problemas importantes y se dejaron mejoras futuras en `TASKS.md`.

### Resultado

Se implemento una primera capa de logica de partida:

- `InterfazPartida`
- `EstadoPartida`
- `Partida`
- `Puerta`
- `ObjetoEnMapa`
- `PersonajeEnMapa`
- `CuevaEnMapa`
- `CeldaEnMapa`

La partida coordina jugador, mazmorra, enemigos, objetos en suelo, puertas, acciones, turnos, combate, victoria, derrota y log. La interfaz publica evita exponer referencias mutables principales mediante vistas para personaje, cueva y celda.

Se anadieron tests JUnit en `PartidaTest` y Alvaro mostro cobertura de IntelliJ para `modelo.juego`: 100% de clases, 89% de metodos, 79% de lineas y 58% de ramas.

### Cambios aceptados

- `Partida` recibe una `Mazmorra` ya creada; no la construye desde cero.
- `Mazmorra` conecta cuevas; `Partida` solo valida avance segun puertas y llaves.
- Las puertas son configuracion inicial de partida sobre conexiones ya existentes.
- El jugador puede recoger objetos en celdas adyacentes.
- El ataque cuerpo a cuerpo permite diagonales.
- El arco permite atacar a distancia.
- El dano se calcula como ataque menos defensa, con minimo 1.
- Enemigos adyacentes atacan; si no estan adyacentes, se acercan usando camino minimo.
- El turno termina cuando el jugador llama a `pasarTurno()`.
- Para avanzar entre cuevas hace falta llave, salvo que la puerta ya este abierta.
- Para ganar hace falta la llave final obtenida al derrotar al boss final.
- El log vive de momento en `Partida`.
- Jugador y enemigos vivos no pueden compartir celda.

### Cambios rechazados o modificados

- Se elimino del diseno la idea de que `Partida` conecte cuevas.
- Se sacaron de `InterfazPartida` metodos de preparacion como anadir enemigos u objetos.
- Se dejo como mejora futura mover enemigos y objetos a una estructura mas definitiva si el diseno final lo pide.
- Se dejo como mejora futura impedir que el movimiento largo atraviese enemigos.
- Se dejo como mejora futura que la IA busque rutas alternativas si el primer paso esta ocupado.
- Se dejo como mejora futura sustituir referencias a `Cueva` en `ObjetoEnMapa` por id o vista inmutable.

### Critica

La sesion corrigio una frontera de responsabilidades a tiempo: `Partida` estaba creciendo hacia tareas estructurales que ya pertenecian a `Mazmorra`. La revision independiente tambien fue util porque detecto riesgos de mutabilidad y ocupacion que podian romper la primera version jugable. Queda como aviso que esta capa todavia no esta conectada en profundidad con JSON ni JavaFX, asi que debe presentarse como base funcional de logica, no como juego cerrado.

## 2026-05-22 - Guillermo

### Agente o herramienta

Codex-B Logica, continuando desde `feature/b-json-partida`.

### Objetivo

Desbloquear a Parte C con una forma simple de crear una `Partida` desde el JSON ya cargado, sin ampliar hoy combate, regla del 75% ni efectos por turnos.

### Resultado

Se anadio `FabricaPartida`, que recibe `ResultadoCarga`, busca la celda `INICIO`, crea el jugador base, traduce conexiones a puertas, coloca enemigos y coloca objetos en suelo. Tambien se ajustaron los DTO y el JSON de ejemplo para conservar la cueva de cada elemento y datos minimos de llaves.

### Decisiones

- La fabrica es estricta: configuraciones invalidas lanzan `IllegalArgumentException`.
- Las puertas usan codigos `llave-` + id de cueva destino.
- Los valores del jugador quedan fijos en la fabrica para esta primera version.
- No se implementan todavia invisibilidad, regla del 75% ni guardado/carga de estado.

### Pruebas

Se comprobaron por terminal compilacion de `src`, compilacion de tests y ejecucion por reflexion de `CargadorConfiguracionTest` y `FabricaPartidaTest`: 21 tests, 0 fallos.

La revision independiente detecto tres ajustes reales: no ignorar conexiones JSON invalidas, recolocar al jugador al cambiar de cueva y corregir las estadisticas del boss del JSON. Se aplicaron las tres correcciones y se anadieron tests de regresion.

### Critica

La solucion es deliberadamente pequena y util para coordinacion: evita meter reglas de juego en `src/json` y ofrece a Hector un punto de entrada claro. El coste es que `FabricaPartida` conoce DTOs de JSON desde la capa de juego, algo aceptable como adaptador inicial pero revisable cuando el guardado/carga de estado este mas definido.
