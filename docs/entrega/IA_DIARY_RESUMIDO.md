# Diario de IA resumido

Este documento resume el uso de IA durante el desarrollo de **Escape de la Mazmorra**. El diario completo se conserva en `project-management/IA_DIARY.md`, donde aparecen registros mas extensos de prompts, sesiones, resultados y criticas. La metodologia general de trabajo con agentes se explica con mas detalle en la memoria del proyecto, especialmente en los apartados de metodologia, coordinacion y uso de IA.

## 1. Metodologia seguida

El uso de IA se organizo bajo un enfoque **human-in-the-loop**. Los agentes no trabajaban como autores autonomos sin control, sino como asistentes especializados que proponian, implementaban, revisaban o documentaban bajo supervision humana.

El equipo dividio el proyecto en tres areas principales:

- **Parte A - Estructuras, mapa y grafo**, coordinada por Alvaro.
- **Parte B - Logica del juego**, coordinada por Guillermo.
- **Parte C - JavaFX, JSON y documentacion**, coordinada por Hector.
- **Revision independiente**, usada para detectar riesgos de diseño, pruebas insuficientes, exposicion mutable o incumplimiento de restricciones.

Cada area podia apoyarse en un agente IA, pero el equipo humano mantenia las decisiones de alcance. Antes de trabajar, el agente debia leer la documentacion relevante. Al terminar, debia explicar cambios, pruebas, riesgos y pendientes.

La carpeta `project-management/` funciono como fuente de verdad. Alli se mantuvieron documentos como:

- `ARCHITECTURE.md`, con principios tecnicos.
- `DECISIONS.md`, con decisiones de diseño.
- `TASKS.md`, con tareas y estados.
- `SCRATCHPAD.md`, con diario tecnico de sesiones.
- `REVIEW_CHECKLIST.md`, con checklist de revision.
- `IA_DIARY.md`, con registro del uso de IA.

El flujo habitual era:

1. El humano indicaba la tarea y el contexto.
2. El agente revisaba documentacion y codigo relacionado.
3. Se acordaba un alcance concreto.
4. El agente implementaba o proponia cambios.
5. Se ejecutaban pruebas o verificaciones.
6. Se registraban resultados y riesgos.
7. El humano aceptaba, corregia o rechazaba la propuesta.

Esta metodologia intentaba evitar dos riesgos: que la IA tomara decisiones no autorizadas y que el equipo perdiera contexto entre sesiones. Por eso se insistio en documentar no solo lo que se hizo, sino tambien por que se hizo, que se descarto y que pruebas lo respaldaban.

## 2. Resumen cronologico de conversaciones y sesiones

### 2026-05-20 - Coordinacion inicial

La primera sesion relevante con IA se centro en organizar el proyecto antes de programar mas. Se pidio estructurar la forma de trabajo con agentes, definir roles, crear documentos de coordinacion y establecer una metodologia human-in-the-loop.

Como resultado se creo la base documental de `project-management/`: PRD, arquitectura, agentes, tareas, decisiones, scratchpad, checklist de revision y diario de IA. Esta decision fue aceptada porque daba al proyecto una memoria compartida. No se implemento codigo en esa sesion; se priorizo la organizacion.

La critica registrada fue que esta estructura solo seria util si se mantenia actualizada. La documentacion podia convertirse en una ventaja o en una carga segun la disciplina del equipo.

### 2026-05-20 - Diseño inicial de logica

Guillermo uso el agente de logica para preparar la Parte B. La conversacion sirvio para fijar reglas iniciales de jugador, enemigos, objetos, inventario, turnos y combate.

Se definieron valores base para jugador y enemigos, se propusieron objetos como pociones, espada, arco, llave y escudo, y se acordo un turno con movimiento y accion. Tambien se discutio que no convenia implementar aleatoriedad al principio, para reducir complejidad y facilitar pruebas.

La sesion fue util porque evito empezar a programar reglas sin criterio comun. Varias ideas quedaron pendientes de confirmacion, lo que muestra una buena practica: no todo lo que propone la IA debe entrar inmediatamente en codigo.

### 2026-05-21 - Workflow economico y politica de modelos

Alvaro pidio optimizar el uso de agentes para reducir consumo de contexto. Se adapto el workflow para leer documentos de forma escalonada, delegar solo cuando hubiera ventaja clara y usar prompts mas compactos.

Tambien se documento una politica de modelos: usar un modelo principal para trabajo normal, modelos mas pequeños para tareas auxiliares y modelos mas potentes solo para problemas dificiles o de alto impacto.

La mejora no afecto directamente al juego, pero si a la forma de trabajar. La critica fue clara: el ahorro real no depende solo del modelo elegido, sino de mantener buenos resumenes y evitar investigaciones repetidas.

### 2026-05-21 - JSON inicial y Gson

Hector uso el agente de Parte C para iniciar la tarea de configuracion JSON. Se pidio crear el JSON inicial de las tres cuevas, configurar Gson, definir DTOs, implementar un cargador y añadir tests.

El resultado fue `datos/cuevas.json`, la configuracion de Gson, DTOs de configuracion, `CargadorConfiguracion`, `ResultadoCarga` y tests asociados. En esa fase las cuevas eran mas pequeñas que en la version final, pero sirvieron como punto de partida para conectar datos externos con el modelo.

La sesion fue aceptada porque resolvia una necesidad importante: dejar de depender de datos creados manualmente en codigo o tests. La critica principal fue que en ese momento faltaba un runner JUnit standalone desde terminal, por lo que parte de la validacion dependia de IntelliJ.

### 2026-05-21 - Conflictos de merge en Parte C

Hector pidio ayuda para resolver conflictos de merge en una Pull Request. Los conflictos afectaban a documentos como `IA_DIARY.md`, `SCRATCHPAD.md` y al archivo del proyecto IntelliJ.

La IA ayudo a identificar los conflictos, conservar las entradas correctas de la sesion y actualizar la PR. No hubo cambios funcionales, pero la sesion fue importante porque mostro que los documentos vivos tambien generan conflictos y necesitan resolucion cuidadosa.

La critica fue que la coordinacion documental debe tener el mismo cuidado que el codigo. Si se pierden entradas del diario o del scratchpad, se pierde trazabilidad.

### 2026-05-22 - Primera version de Partida

Alvaro trabajo con un agente sobre la primera version funcional de `Partida`. La conversacion empezo con planificacion: `Partida` debia coordinar la logica del juego, pero no construir ni conectar la mazmorra desde cero.

Durante la sesion se cerraron reglas de movimiento, recogida, combate, turnos, puertas, llave final, victoria y log. Se detecto una frontera importante: conectar cuevas pertenece a `Mazmorra`, no a `Partida`. Esa correccion se registro porque evitaba que la fachada de partida asumiera responsabilidades estructurales.

Se implementaron clases como `InterfazPartida`, `EstadoPartida`, `Partida`, `Puerta` y varias vistas de mapa/personajes. Tambien se añadieron tests. La revision independiente ayudo a detectar riesgos de mutabilidad, metodos de preparacion demasiado publicos y posibles solapes de ocupacion.

Esta sesion muestra bien el valor de la IA cuando se combina con supervision: propuso e implemento, pero tambien se corrigieron sus limites de diseño durante la conversacion.

### 2026-05-22 - FabricaPartida e integracion JSON-logica

Guillermo pidio desbloquear a Parte C con una forma sencilla de crear una `Partida` desde el JSON cargado. Se creo `FabricaPartida`, encargada de recibir `ResultadoCarga`, localizar la celda de inicio, crear el jugador base, traducir conexiones a puertas, colocar enemigos y colocar objetos.

La decision fue mantener la fabrica estricta: si el JSON tiene configuraciones invalidas, debe lanzar error. Tambien se decidio que la fabrica podia conocer DTOs como adaptador inicial, aunque eso fuera revisable en una arquitectura mas grande.

La revision independiente detecto ajustes reales: no ignorar conexiones invalidas, recolocar al jugador al cambiar de cueva y corregir estadisticas del boss. Se aplicaron correcciones y tests de regresion.

### 2026-05-22 - Correcciones de UI

Hector uso IA para corregir problemas visuales: titulos descentrados, una pantalla de inicio con texto curvado poco legible, retardo en auto-turno y falta de efecto visual cuando un enemigo atacaba.

La IA propuso y aplico ajustes en `EscapeMazmorraApp.java` y `PantallaJuego.java`: centrar titulos midiendo texto, dejar `ESCAPE` en linea recta, corregir feedback erroneo y añadir flash rojo al recibir daño.

Esta sesion fue concreta y eficaz. La critica positiva fue que medir el ancho real del texto era mejor que usar constantes magicas.

### 2026-05-22 - Guardado completo y ayuda integrada

Otra sesion de Parte C se centro en guardado completo y ayuda dentro del juego. Se pidio crear DTOs de puertas, ampliar DTOs de partida y objetos, serializar enemigos, objetos y puertas, restaurar contenido de cueva al cargar, añadir overlay de ayuda y crear tests round-trip.

El resultado fue una mejora importante de persistencia. El guardado dejo de ser parcial y paso a conservar estado relevante de partida. La ayuda integrada hizo que el jugador pudiera consultar controles sin salir del juego.

La critica fue que la reconstruccion desde DTOs crecia en complejidad. Aun asi, era aceptable para el alcance del proyecto y se protegio con tests.

### 2026-05-22 y 2026-05-23 - Pulido audiovisual, mapas y turnos

Alvaro y otros integrantes trabajaron con IA en musica, obstaculos, mapas mas grandes, turnos y auto-avance. Se incorporo musica de fondo, se añadieron tipos de celda como `ROCA` y `ARBUSTO`, se rediseñaron mapas y se actualizo el limite de turnos.

Una decision relevante fue aumentar los turnos de 40 a 60 para que los mapas mas grandes fueran jugables. Tambien se implemento auto-avance al pisar puerta con llave, pero la revision detecto que podia activarse usando una variable incorrecta. Se corrigio con una bandera especifica de movimiento.

Estas sesiones muestran que el pulido visual puede afectar a la logica: mapas mas grandes obligan a ajustar turnos, posiciones, tests y reglas de avance.

### 2026-05-23 - Revisor independiente y restricciones de estructuras

En una revision se detecto el uso de `HashMap<String, Image>` en `PantallaJuego.java`. Aunque era comodo para cachear imagenes, chocaba con la restriccion de colecciones prohibidas. Se sustituyo por una estructura propia basada en `ListaDE`.

Este caso es importante para defender el proyecto. La IA no solo genero codigo, tambien ayudo a detectar incumplimientos de restricciones academicas. La correccion demostro que las reglas del enunciado se aplicaban incluso en zonas visuales.

### 2026-05-23 - Depuracion de cierre al entrar a cueva

Se investigo un problema donde el juego se cerraba al entrar en una cueva. El `crash.log` aparecia vacio, por lo que se mejoro el logging y se amplio la captura de errores de `Exception` a `Throwable`.

La sesion tambien identifico problemas de assets: faltaban iconos claros para puerta, escudo, tesoro y salida. Se crearon tareas nuevas para resolverlos.

La critica aqui fue que la depuracion visual requiere informacion clara. Si los logs no capturan el fallo, el tiempo de diagnostico aumenta mucho.

### 2026-05-23 - Pausa, iconos y sonidos

Guillermo uso IA para apoyar tareas audiovisuales: menu de pausa, confirmacion al volver al menu, iconos pendientes y SFX minimos. Se añadió pausa con `P` o `ESC`, menu de pausa, iconos para puerta, salida, tesoro y escudo, y reproductor de efectos.

Despues de pruebas visuales se ajustaron volumenes, mensajes, vida sobre entidades, inventario y feedback al terminar turno. Tambien se reemplazaron algunos recursos generados por archivos temporales para no versionar binarios innecesarios.

La sesion muestra un patron habitual: la primera version funcionaba, pero la prueba humana detecto detalles de experiencia que la IA no podia validar por completo.

### 2026-05-24 - Ataque direccional

Alvaro pidio implementar ataque direccional de forma acotada. Se añadieron consultas en `Partida` para detectar enemigos por direccion y enemigos adyacentes. En `PantallaJuego` se incorporo ataque con `Shift + WASD` o `Shift + Flechas`, ataque por clic y resaltado de enemigos atacables.

Se añadieron tests para direccion con enemigo, direccion vacia, varios enemigos adyacentes y daño solo al objetivo elegido. La suite paso correctamente.

La revision independiente no encontro bloqueos graves, pero dejo riesgos menores documentados. Esto refuerza la idea de que incluso cuando algo funciona, conviene registrar supuestos y limites.

### 2026-05-24 - Cierre de reglas B-02 y B-03

Guillermo reviso el estado real de reglas de logica tras varios merges. Se cerro que equipar o cambiar objeto no consume accion, y que cambiar de cueva no consume accion ni turno, aunque reinicia turnos de la nueva cueva.

La revision detecto inicialmente que borrar banderas de accion y movimiento al cambiar de cueva podia regalar una segunda accion o movimiento. Se corrigio y se añadió test de regresion.

Esta sesion muestra otra funcion valiosa del agente: no solo implementar, sino comprobar coherencia entre reglas nuevas y estado existente.

### 2026-05-24 - Iconos, tesoros y accesibilidad del mapa

Se corrigieron iconos de pociones y escudos, se elimino el color morado bajo `TESORO`, se movio un tesoro de la cueva dificil a una posicion accesible y se añadieron tests para comprobar que objetos y tesoros del JSON real no quedan en posiciones invalidas.

Tambien se decidio que los cofres no fueran pisables, sino abribles desde una celda cardinal adyacente. La revision posterior detecto que BFS podia atravesar un tesoro cerrado, y se corrigio excluyendo esos tesoros de las celdas alcanzables.

Esta conversacion fue importante porque conecto interfaz, mapa, logica y tests. Un detalle visual del cofre acabo convirtiendose en una regla jugable protegida por pruebas.

### 2026-05-24 - Animaciones

Alvaro pidio implementar animaciones en `PantallaJuego`: movimiento suave, ataque y muerte. La IA propuso hacerlo por fases con `Timeline`, overlays y transiciones JavaFX.

Se acepto crear un `animOverlay` separado para que las animaciones de muerte no se borraran al refrescar el grid. Tambien se uso `translateX/Y` sobre el sprite del jugador para no modificar la jerarquia de celdas.

La critica fue positiva: dividir la tarea en fases redujo riesgo y permitio mantener la logica intacta.

### 2026-05-24 - Estadisticas y ranking

Alvaro pidio implementar estadisticas, puntuacion y ranking local con Gson. Se creo `EstadisticasPartida`, se integro con `Partida`, se ampliaron DTOs de guardado y se incorporo una pantalla final con nombre, puntuacion, titulo y ranking.

La persistencia del ranking se hizo con Gson en `ranking.json`, ignorado por Git como dato local. La interfaz añadió un boton para mostrar el Top 10.

Esta sesion amplio el cierre de la experiencia: ya no solo habia victoria o derrota, sino una valoracion final de la partida.

### 2026-05-24 - Bola de fuego

Se implemento una mecanica avanzada de ataque a distancia con `F + Flecha`. La decision de balance fue que consumiera accion, no movimiento, y causara daño fijo.

La logica se mantuvo en `Partida` mediante metodos publicos y `ResultadoImpactoBolaFuego`, para que JavaFX no tocara enemigos internos directamente. La vista se encargo de animar la trayectoria y colisiones.

La critica fue que la frontera entre modelo y vista se mantuvo razonablemente: la vista anima, pero el modelo decide impactos y daño.

### 2026-05-24 - Iteraciones de menu principal

Se probaron varias versiones de pantalla inicial: una version premium con marco y luces, una direccion pixel art y finalmente una reversion a una composicion mas simple. Tambien se ajusto el modal de nombre del jugador para evitar el cuadro nativo de JavaFX y mantener estetica de pergamino.

Varias propuestas visuales fueron aceptadas parcialmente, modificadas o revertidas. Este bloque es un buen ejemplo de cambios rechazados o corregidos: que la IA pueda producir una version vistosa no significa que encaje con el estilo final del proyecto.

La decision final priorizo coherencia visual y claridad sobre exceso decorativo.

## 3. Cambios aceptados principales

Entre los cambios propuestos con ayuda de IA y aceptados en el proyecto destacan:

- Estructura documental de `project-management/`.
- Workflow con agentes por areas.
- Configuracion de Gson y JSON inicial.
- Cargador de configuracion y DTOs.
- `FabricaPartida`.
- Fachada `Partida` y vistas de estado para JavaFX.
- Guardado y carga completos.
- Ayuda integrada.
- Mejoras de mapa y obstaculos.
- Aumento de turnos por cueva.
- Menu de pausa.
- Iconos especificos.
- Ataque direccional.
- Cofres no pisables pero abribles desde al lado.
- Animaciones.
- Estadisticas y ranking.
- Bola de fuego.
- Ajustes finales de menu y nombre.
- Tests de regresion asociados a reglas nuevas o bugs detectados.

## 4. Cambios corregidos, rechazados o modificados

No todas las propuestas se aceptaron tal como salieron de la IA. Algunos cambios se corrigieron o se descartaron:

- Se rechazo que `Partida` conectara cuevas; esa responsabilidad pertenece a `Mazmorra`.
- Se evitaron metodos de preparacion dentro de la interfaz publica de partida.
- Se sustituyo `HashMap` por estructura propia para respetar restricciones.
- Se corrigio el auto-avance por puerta para que no se activara tras acciones que no fueran movimiento.
- Se corrigio que cambiar de cueva regalara una segunda accion o movimiento.
- Se ajustaron cofres para que no rompieran BFS ni movimiento.
- Se reemplazaron placeholders visuales confusos.
- Se revirtieron o modificaron versiones de menu que no encajaban con la estetica final.
- Se mantuvieron algunas ideas como mejoras futuras cuando aumentaban demasiado el alcance.

Estas correcciones son importantes porque muestran que el uso de IA no fue una aceptacion automatica. El equipo reviso, probo y decidio.

## 5. Verificaciones realizadas

La validacion acompañó al uso de IA. Segun las sesiones registradas, se ejecutaron pruebas parciales y completas en distintos momentos. La suite fue creciendo desde cifras iniciales hasta llegar a 223 tests finales.

Las verificaciones incluyeron:

- compilacion de `src`;
- compilacion de tests;
- tests de estructuras;
- tests de mapa y BFS;
- tests de `Partida`;
- tests de `FabricaPartida`;
- tests de JSON;
- tests de guardado/carga;
- tests de ranking;
- tests de iconos;
- pruebas visuales manuales desde JavaFX;
- revision independiente de cambios de riesgo.

No todas las verificaciones pudieron automatizarse desde el mismo entorno por dependencias locales de JavaFX. Cuando eso ocurrio, quedo registrado y se pidio validacion en IntelliJ o en PowerShell local.

## 6. Critica del uso de IA

El uso de IA fue util porque acelero tareas, ayudo a estructurar documentos, propuso implementaciones y detecto riesgos. Tambien ayudo a mantener continuidad entre sesiones largas.

Sin embargo, tuvo riesgos:

- podia asumir decisiones no confirmadas;
- podia tocar zonas fuera del area asignada;
- podia generar soluciones comodas pero contrarias a restricciones academicas;
- podia perder contexto si no leia documentacion actualizada;
- podia producir cambios visuales atractivos pero poco coherentes;
- podia dejar demasiada responsabilidad en clases grandes como `Partida`.

El proyecto mitigo esos riesgos con documentacion viva, reparto de responsabilidades, revision independiente, tests y aceptacion humana. El mayor aprendizaje es que la IA funciona mejor cuando se le da un contexto claro, una tarea acotada y criterios de aceptacion verificables.

## 7. Conclusion

El diario de IA demuestra que los agentes fueron una herramienta de apoyo real durante el proyecto. No solo generaron codigo: tambien ayudaron a planificar, revisar, documentar, depurar y reflexionar sobre el proceso.

La parte mas defendible del uso de IA no es haberla usado, sino haberlo hecho con control: registrando sesiones, explicando prompts, separando cambios aceptados y rechazados, ejecutando pruebas y manteniendo decisiones humanas.

Para consultar el registro completo, debe revisarse `project-management/IA_DIARY.md`. Para entender la metodologia general con mas detalle, debe consultarse la memoria ampliada del proyecto.

