# Diario de IA resumido

Este documento resume el uso de IA durante el desarrollo de **Escape de la Mazmorra**. El diario completo se conserva en `documentos/ENTREGA/project-management/IA_DIARY.md`, donde aparecen registros más extensos de prompts, sesiones, resultados y críticas. La metodología general de trabajo con agentes se explica con más detalle en la memoria del proyecto, especialmente en los apartados de metodología, coordinación y uso de IA.

## 1. Metodología seguida

El uso de IA se organizó bajo un enfoque **human-in-the-loop**. Los agentes no trabajaban como autores autónomos sin control, sino como asistentes especializados que proponían, implementaban, revisaban o documentaban bajo supervisión humana.

El equipo dividió el proyecto en tres áreas principales:

- **Parte A - Estructuras, mapa y grafo**, coordinada por Álvaro.
- **Parte B - Lógica del juego**, coordinada por Guillermo.
- **Parte C - JavaFX, JSON y documentación**, coordinada por Héctor.
- **Revisión independiente**, usada para detectar riesgos de diseño, pruebas insuficientes, exposición mutable o incumplimiento de restricciones.

Cada área podía apoyarse en un agente IA, pero el equipo humano mantenía las decisiones de alcance. Antes de trabajar, el agente debía leer la documentación relevante. Al terminar, debía explicar cambios, pruebas, riesgos y pendientes.

La carpeta `documentos/ENTREGA/project-management/` funcionó como fuente de verdad. Allí se mantuvieron documentos como:

- `ARCHITECTURE.md`, con principios técnicos.
- `DECISIONS.md`, con decisiones de diseño.
- `TASKS.md`, con tareas y estados.
- `SCRATCHPAD.md`, con diario técnico de sesiones.
- `REVIEW_CHECKLIST.md`, con checklist de revisión.
- `IA_DIARY.md`, con registro del uso de IA.

El flujo habitual era:

1. El humano indicaba la tarea y el contexto.
2. El agente revisaba documentación y código relacionado.
3. Se acordaba un alcance concreto.
4. El agente implementaba o proponía cambios.
5. Se ejecutaban pruebas o verificaciones.
6. Se registraban resultados y riesgos.
7. El humano aceptaba, corregía o rechazaba la propuesta.

Esta metodología intentaba evitar dos riesgos: que la IA tomara decisiones no autorizadas y que el equipo perdiera contexto entre sesiones. Por eso se insistió en documentar no solo lo que se hizo, sino también por qué se hizo, qué se descartó y qué pruebas lo respaldaban.

## 2. Resumen cronológico de conversaciones y sesiones

### 2026-05-20 - Coordinación inicial

La primera sesión relevante con IA se centró en organizar el proyecto antes de programar más. Se pidió estructurar la forma de trabajo con agentes, definir roles, crear documentos de coordinación y establecer una metodología human-in-the-loop.

Como resultado se creó la base documental de `documentos/ENTREGA/project-management/`: PRD, arquitectura, agentes, tareas, decisiones, scratchpad, checklist de revisión y diario de IA. Esta decisión fue aceptada porque daba al proyecto una memoria compartida. No se implementó código en esa sesión; se priorizó la organización.

La crítica registrada fue que esta estructura solo sería útil si se mantenía actualizada. La documentación podía convertirse en una ventaja o en una carga según la disciplina del equipo.

### 2026-05-20 - Diseño inicial de lógica

Guillermo usó el agente de lógica para preparar la Parte B. La conversación sirvió para fijar reglas iniciales de jugador, enemigos, objetos, inventario, turnos y combate.

Se definieron valores base para jugador y enemigos, se propusieron objetos como pociones, espada, arco, llave y escudo, y se acordó un turno con movimiento y acción. También se discutió que no convenía implementar aleatoriedad al principio, para reducir complejidad y facilitar pruebas.

La sesión fue útil porque evitó empezar a programar reglas sin criterio común. Varias ideas quedaron pendientes de confirmación, lo que muestra una buena práctica: no todo lo que propone la IA debe entrar inmediatamente en código.

### 2026-05-21 - Workflow económico y política de modelos

Álvaro pidió optimizar el uso de agentes para reducir consumo de contexto. Se adaptó el workflow para leer documentos de forma escalonada, delegar solo cuando hubiera ventaja clara y usar prompts más compactos.

También se documentó una política de modelos: usar un modelo principal para trabajo normal, modelos más pequeños para tareas auxiliares y modelos más potentes solo para problemas difíciles o de alto impacto.

La mejora no afectó directamente al juego, pero sí a la forma de trabajar. La crítica fue clara: el ahorro real no depende solo del modelo elegido, sino de mantener buenos resúmenes y evitar investigaciones repetidas.

### 2026-05-21 - JSON inicial y Gson

Héctor usó el agente de Parte C para iniciar la tarea de configuración JSON. Se pidió crear el JSON inicial de las tres cuevas, configurar Gson, definir DTOs, implementar un cargador y añadir tests.

El resultado fue `datos/cuevas.json`, la configuración de Gson, DTOs de configuración, `CargadorConfiguracion`, `ResultadoCarga` y tests asociados. En esa fase las cuevas eran más pequeñas que en la versión final, pero sirvieron como punto de partida para conectar datos externos con el modelo.

La sesión fue aceptada porque resolvía una necesidad importante: dejar de depender de datos creados manualmente en código o tests. La crítica principal fue que en ese momento faltaba un runner JUnit standalone desde terminal, por lo que parte de la validación dependía de IntelliJ.

### 2026-05-21 - Conflictos de merge en Parte C

Héctor pidió ayuda para resolver conflictos de merge en una Pull Request. Los conflictos afectaban a documentos como `IA_DIARY.md`, `SCRATCHPAD.md` y al archivo del proyecto IntelliJ.

La IA ayudó a identificar los conflictos, conservar las entradas correctas de la sesión y actualizar la PR. No hubo cambios funcionales, pero la sesión fue importante porque mostró que los documentos vivos también generan conflictos y necesitan resolución cuidadosa.

La crítica fue que la coordinación documental debe tener el mismo cuidado que el código. Si se pierden entradas del diario o del scratchpad, se pierde trazabilidad.

### 2026-05-22 - Primera versión de Partida

Álvaro trabajó con un agente sobre la primera versión funcional de `Partida`. La conversación empezó con planificación: `Partida` debía coordinar la lógica del juego, pero no construir ni conectar la mazmorra desde cero.

Durante la sesión se cerraron reglas de movimiento, recogida, combate, turnos, puertas, llave final, victoria y log. Se detectó una frontera importante: conectar cuevas pertenece a `Mazmorra`, no a `Partida`. Esa corrección se registró porque evitaba que la fachada de partida asumiera responsabilidades estructurales.

Se implementaron clases como `InterfazPartida`, `EstadoPartida`, `Partida`, `Puerta` y varias vistas de mapa/personajes. También se añadieron tests. La revisión independiente ayudó a detectar riesgos de mutabilidad, métodos de preparación demasiado públicos y posibles solapes de ocupación.

Esta sesión muestra bien el valor de la IA cuando se combina con supervisión: propuso e implementó, pero también se corrigieron sus límites de diseño durante la conversación.

### 2026-05-22 - FabricaPartida e integración JSON-lógica

Guillermo pidió desbloquear a Parte C con una forma sencilla de crear una `Partida` desde el JSON cargado. Se creó `FabricaPartida`, encargada de recibir `ResultadoCarga`, localizar la celda de inicio, crear el jugador base, traducir conexiones a puertas, colocar enemigos y colocar objetos.

La decisión fue mantener la fábrica estricta: si el JSON tiene configuraciones inválidas, debe lanzar error. También se decidió que la fábrica podía conocer DTOs como adaptador inicial, aunque eso fuera revisable en una arquitectura más grande.

La revisión independiente detectó ajustes reales: no ignorar conexiones inválidas, recolocar al jugador al cambiar de cueva y corregir estadísticas del boss. Se aplicaron correcciones y tests de regresión.

### 2026-05-22 - Correcciones de UI

Héctor usó IA para corregir problemas visuales: títulos descentrados, una pantalla de inicio con texto curvado poco legible, retardo en auto-turno y falta de efecto visual cuando un enemigo atacaba.

La IA propuso y aplicó ajustes en `EscapeMazmorraApp.java` y `PantallaJuego.java`: centrar títulos midiendo texto, dejar `ESCAPE` en línea recta, corregir feedback erróneo y añadir flash rojo al recibir daño.

Esta sesión fue concreta y eficaz. La crítica positiva fue que medir el ancho real del texto era mejor que usar constantes mágicas.

### 2026-05-22 - Guardado completo y ayuda integrada

Otra sesión de Parte C se centró en guardado completo y ayuda dentro del juego. Se pidió crear DTOs de puertas, ampliar DTOs de partida y objetos, serializar enemigos, objetos y puertas, restaurar contenido de cueva al cargar, añadir overlay de ayuda y crear tests round-trip.

El resultado fue una mejora importante de persistencia. El guardado dejó de ser parcial y pasó a conservar estado relevante de partida. La ayuda integrada hizo que el jugador pudiera consultar controles sin salir del juego.

La crítica fue que la reconstrucción desde DTOs crecía en complejidad. Aun así, era aceptable para el alcance del proyecto y se protegió con tests.

### 2026-05-22 y 2026-05-23 - Pulido audiovisual, mapas y turnos

Álvaro y otros integrantes trabajaron con IA en música, obstáculos, mapas más grandes, turnos y auto-avance. Se incorporó música de fondo, se añadieron tipos de celda como `ROCA` y `ARBUSTO`, se rediseñaron mapas y se actualizó el límite de turnos.

Una decisión relevante fue aumentar los turnos de 40 a 60 para que los mapas más grandes fueran jugables. También se implementó auto-avance al pisar puerta con llave, pero la revisión detectó que podía activarse usando una variable incorrecta. Se corrigió con una bandera específica de movimiento.

Estas sesiones muestran que el pulido visual puede afectar a la lógica: mapas más grandes obligan a ajustar turnos, posiciones, tests y reglas de avance.

### 2026-05-23 - Revisor independiente y restricciones de estructuras

En una revisión se detectó el uso de `HashMap<String, Image>` en `PantallaJuego.java`. Aunque era cómodo para cachear imágenes, chocaba con la restricción de colecciones prohibidas. Se sustituyó por una estructura propia basada en `ListaDE`.

Este caso es importante para defender el proyecto. La IA no solo generó código, también ayudó a detectar incumplimientos de restricciones académicas. La corrección demostró que las reglas del enunciado se aplicaban incluso en zonas visuales.

### 2026-05-23 - Depuración de cierre al entrar a cueva

Se investigó un problema donde el juego se cerraba al entrar en una cueva. El `crash.log` aparecía vacío, por lo que se mejoró el logging y se amplió la captura de errores de `Exception` a `Throwable`.

La sesión también identificó problemas de assets: faltaban iconos claros para puerta, escudo, tesoro y salida. Se crearon tareas nuevas para resolverlos.

La crítica aquí fue que la depuración visual requiere información clara. Si los logs no capturan el fallo, el tiempo de diagnóstico aumenta mucho.

### 2026-05-23 - Pausa, iconos y sonidos

Guillermo usó IA para apoyar tareas audiovisuales: menú de pausa, confirmación al volver al menú, iconos pendientes y SFX mínimos. Se añadió pausa con `P` o `ESC`, menú de pausa, iconos para puerta, salida, tesoro y escudo, y reproductor de efectos.

Después de pruebas visuales se ajustaron volúmenes, mensajes, vida sobre entidades, inventario y feedback al terminar turno. También se reemplazaron algunos recursos generados por archivos temporales para no versionar binarios innecesarios.

La sesión muestra un patrón habitual: la primera versión funcionaba, pero la prueba humana detectó detalles de experiencia que la IA no podía validar por completo.

### 2026-05-24 - Ataque direccional

Álvaro pidió implementar ataque direccional de forma acotada. Se añadieron consultas en `Partida` para detectar enemigos por dirección y enemigos adyacentes. En `PantallaJuego` se incorporó ataque con `Shift + WASD` o `Shift + Flechas`, ataque por clic y resaltado de enemigos atacables.

Se añadieron tests para dirección con enemigo, dirección vacía, varios enemigos adyacentes y daño solo al objetivo elegido. La suite pasó correctamente.

La revisión independiente no encontró bloqueos graves, pero dejó riesgos menores documentados. Esto refuerza la idea de que incluso cuando algo funciona, conviene registrar supuestos y límites.

### 2026-05-24 - Cierre de reglas B-02 y B-03

Guillermo revisó el estado real de reglas de lógica tras varios merges. Se cerró que equipar o cambiar objeto no consume acción, y que cambiar de cueva no consume acción ni turno, aunque reinicia turnos de la nueva cueva.

La revisión detectó inicialmente que borrar banderas de acción y movimiento al cambiar de cueva podía regalar una segunda acción o movimiento. Se corrigió y se añadió test de regresión.

Esta sesión muestra otra función valiosa del agente: no solo implementar, sino comprobar coherencia entre reglas nuevas y estado existente.

### 2026-05-24 - Iconos, tesoros y accesibilidad del mapa

Se corrigieron iconos de pociones y escudos, se eliminó el color morado bajo `TESORO`, se movió un tesoro de la cueva difícil a una posición accesible y se añadieron tests para comprobar que objetos y tesoros del JSON real no quedan en posiciones inválidas.

También se decidió que los cofres no fueran pisables, sino abribles desde una celda cardinal adyacente. La revisión posterior detectó que BFS podía atravesar un tesoro cerrado, y se corrigió excluyendo esos tesoros de las celdas alcanzables.

Esta conversación fue importante porque conectó interfaz, mapa, lógica y tests. Un detalle visual del cofre acabó convirtiéndose en una regla jugable protegida por pruebas.

### 2026-05-24 - Animaciones

Álvaro pidió implementar animaciones en `PantallaJuego`: movimiento suave, ataque y muerte. La IA propuso hacerlo por fases con `Timeline`, overlays y transiciones JavaFX.

Se aceptó crear un `animOverlay` separado para que las animaciones de muerte no se borraran al refrescar el grid. También se usó `translateX/Y` sobre el sprite del jugador para no modificar la jerarquía de celdas.

La crítica fue positiva: dividir la tarea en fases redujo riesgo y permitió mantener la lógica intacta.

### 2026-05-24 - Estadísticas y ranking

Álvaro pidió implementar estadísticas, puntuación y ranking local con Gson. Se creó `EstadisticasPartida`, se integró con `Partida`, se ampliaron DTOs de guardado y se incorporó una pantalla final con nombre, puntuación, título y ranking.

La persistencia del ranking se hizo con Gson en `ranking.json`, ignorado por Git como dato local. La interfaz añadió un botón para mostrar el Top 10.

Esta sesión amplió el cierre de la experiencia: ya no solo había victoria o derrota, sino una valoración final de la partida.

### 2026-05-24 - Bola de fuego

Se implementó una mecánica avanzada de ataque a distancia con `F + Flecha`. La decisión de balance fue que consumiera acción, no movimiento, y causara daño fijo.

La lógica se mantuvo en `Partida` mediante métodos públicos y `ResultadoImpactoBolaFuego`, para que JavaFX no tocara enemigos internos directamente. La vista se encargó de animar la trayectoria y colisiones.

La crítica fue que la frontera entre modelo y vista se mantuvo razonablemente: la vista anima, pero el modelo decide impactos y daño.

### 2026-05-24 - Iteraciones de menú principal

Se probaron varias versiones de pantalla inicial: una versión premium con marco y luces, una dirección pixel art y finalmente una reversión a una composición más simple. También se ajustó el modal de nombre del jugador para evitar el cuadro nativo de JavaFX y mantener estética de pergamino.

Varias propuestas visuales fueron aceptadas parcialmente, modificadas o revertidas. Este bloque es un buen ejemplo de cambios rechazados o corregidos: que la IA pueda producir una versión vistosa no significa que encaje con el estilo final del proyecto.

La decisión final priorizó coherencia visual y claridad sobre exceso decorativo.

## 3. Cambios aceptados principales

Entre los cambios propuestos con ayuda de IA y aceptados en el proyecto destacan:

- Estructura documental de `documentos/ENTREGA/project-management/`.
- Workflow con agentes por áreas.
- Configuración de Gson y JSON inicial.
- Cargador de configuración y DTOs.
- `FabricaPartida`.
- Fachada `Partida` y vistas de estado para JavaFX.
- Guardado y carga completos.
- Ayuda integrada.
- Mejoras de mapa y obstáculos.
- Aumento de turnos por cueva.
- Menú de pausa.
- Iconos específicos.
- Ataque direccional.
- Cofres no pisables pero abribles desde al lado.
- Animaciones.
- Estadísticas y ranking.
- Bola de fuego.
- Ajustes finales de menú y nombre.
- Tests de regresión asociados a reglas nuevas o bugs detectados.

## 4. Cambios corregidos, rechazados o modificados

No todas las propuestas se aceptaron tal como salieron de la IA. Algunos cambios se corrigieron o se descartaron:

- Se rechazó que `Partida` conectara cuevas; esa responsabilidad pertenece a `Mazmorra`.
- Se evitaron métodos de preparación dentro de la interfaz pública de partida.
- Se sustituyó `HashMap` por estructura propia para respetar restricciones.
- Se corrigió el auto-avance por puerta para que no se activara tras acciones que no fueran movimiento.
- Se corrigió que cambiar de cueva regalara una segunda acción o movimiento.
- Se ajustaron cofres para que no rompieran BFS ni movimiento.
- Se reemplazaron placeholders visuales confusos.
- Se revirtieron o modificaron versiones de menú que no encajaban con la estética final.
- Se mantuvieron algunas ideas como mejoras futuras cuando aumentaban demasiado el alcance.

Estas correcciones son importantes porque muestran que el uso de IA no fue una aceptación automática. El equipo revisó, probó y decidió.

## 5. Verificaciones realizadas

La validación acompañó al uso de IA. Según las sesiones registradas, se ejecutaron pruebas parciales y completas en distintos momentos. La suite fue creciendo desde cifras iniciales hasta llegar a 223 tests finales.

Las verificaciones incluyeron:

- compilación de `src`;
- compilación de tests;
- tests de estructuras;
- tests de mapa y BFS;
- tests de `Partida`;
- tests de `FabricaPartida`;
- tests de JSON;
- tests de guardado/carga;
- tests de ranking;
- tests de iconos;
- pruebas visuales manuales desde JavaFX;
- revisión independiente de cambios de riesgo.

No todas las verificaciones pudieron automatizarse desde el mismo entorno por dependencias locales de JavaFX. Cuando eso ocurrió, quedó registrado y se pidió validación en IntelliJ o en PowerShell local.

## 6. Crítica del uso de IA

El uso de IA fue útil porque aceleró tareas, ayudó a estructurar documentos, propuso implementaciones y detectó riesgos. También ayudó a mantener continuidad entre sesiones largas.

Sin embargo, tuvo riesgos:

- podía asumir decisiones no confirmadas;
- podía tocar zonas fuera del área asignada;
- podía generar soluciones cómodas pero contrarias a restricciones académicas;
- podía perder contexto si no leía documentación actualizada;
- podía producir cambios visuales atractivos pero poco coherentes;
- podía dejar demasiada responsabilidad en clases grandes como `Partida`.

El proyecto mitigó esos riesgos con documentación viva, reparto de responsabilidades, revisión independiente, tests y aceptación humana. El mayor aprendizaje es que la IA funciona mejor cuando se le da un contexto claro, una tarea acotada y criterios de aceptación verificables.

## 7. Conclusión

El diario de IA demuestra que los agentes fueron una herramienta de apoyo real durante el proyecto. No solo generaron código: también ayudaron a planificar, revisar, documentar, depurar y reflexionar sobre el proceso.

La parte más defendible del uso de IA no es haberla usado, sino haberlo hecho con control: registrando sesiones, explicando prompts, separando cambios aceptados y rechazados, ejecutando pruebas y manteniendo decisiones humanas.

Para consultar el registro completo, debe revisarse `documentos/ENTREGA/project-management/IA_DIARY.md`. Para entender la metodología general con más detalle, debe consultarse la memoria ampliada del proyecto.

