# Post Mortem del Proyecto

Documento vivo para registrar, durante el desarrollo, que esta saliendo bien, que esta saliendo mal y que decisiones conviene ajustar antes de la entrega final.

## Como usar este documento

- Anotar hechos concretos, no solo impresiones generales.
- Indicar fecha, parte responsable y contexto.
- Separar sintomas de causas.
- Convertir problemas repetidos en acciones concretas.
- Revisarlo antes de cerrar cada sesion importante.

## Entradas

### 2026-05-21 - Inicio del seguimiento

Parte responsable: Coordinacion / Parte A

#### Va bien

- Se ha separado el trabajo por partes: A estructuras, B logica, C JavaFX/JSON/documentacion.
- La rama `feature/a-estructuras` esta preparada y sincronizada.
- Se han detectado pronto riesgos tecnicos en las estructuras propias antes de construir la matriz de cuevas.
- Se ha corregido `ListaSE` para permitir tipos sin `Comparable`, lo que facilita usar `Celda`, `Cueva` y otras clases del juego.
- Se ha corregido `Pila` para respetar el comportamiento LIFO.
- La revision independiente detecto problemas reales antes del commit: archivos sin trackear, `Main` con feature preview, exposicion mutable de matriz y falta de tests directos de estructuras.

#### Va mal o requiere atencion

- El material base tenia interfaces faltantes en el paquete de grafo.
- Algunas estructuras arrastraban restricciones genericas demasiado fuertes para el modelo del juego.
- La verificacion con JUnit no esta automatizada con Maven o Gradle.
- En la sesion anterior, Alvaro indico que esta era la Parte A y pidio que se recordara. Hubo riesgo de depender de esa memoria de conversacion en vez de dejarlo siempre registrado de forma operativa.
- Se modificaron archivos de coordinacion que tambien leen otros agentes, lo que puede afectar a B, C o revision si no se comunica y controla bien.

#### Causas probables

- El proyecto reutiliza codigo de practicas anteriores que no estaba pensado originalmente para este juego.
- La configuracion del proyecto depende de IntelliJ y no de un sistema de build independiente.
- Se asumio que la continuidad de la conversacion bastaba para mantener contexto, cuando los agentes necesitan documentos compartidos claros y actualizados.
- Los documentos de `documentos/ENTREGA/project-management/` son utiles para coordinar, pero tambien son superficie compartida y requieren mas cuidado que un archivo exclusivo de Parte A.

#### Acciones decididas

- Mantener este archivo actualizado durante el desarrollo.
- Priorizar pruebas unitarias para cada estructura adaptada.
- Evitar que clases del modelo implementen `Comparable` si no tienen un orden natural real.
- Considerar mas adelante configurar Maven o Gradle si la ejecucion manual de tests se vuelve un bloqueo.
- Al empezar cada sesion, confirmar explicitamente parte y responsable aunque se haya dicho en una sesion anterior.
- Antes de modificar documentos compartidos por varios agentes, indicar el motivo y dejar claro que el cambio afecta a la coordinacion global.
- Usar `SCRATCHPAD.md`, `TASKS.md` y este post mortem para que la memoria importante no dependa solo del chat.
- Mantener la revision independiente antes de commit/push en cambios de codigo, porque ha evitado subir un bloque incompleto.

#### Pendiente de revisar

- Si `ListaDE`, `Cola` y `ListaCircular` tambien deben dejar de exigir `Comparable`.
- Si el grafo de cuevas debe mantener `Comparable<Cueva>` o pasar a comparar por `equals`.
- Como ejecutar JUnit de forma sencilla y repetible para todo el equipo.

### 2026-05-22 - Limite entre Mazmorra y Partida

Parte responsable: Parte B / Coordinacion con Parte A

#### Va mal o requiere atencion

- Al definir la primera version de `Partida`, se expuso en su interfaz un metodo para anadir puertas.
- Aunque no conectaba cuevas en el grafo, podia confundirse con responsabilidad de construir mapa o conectar cuevas.

#### Causa probable

- Se mezclo configuracion inicial de partida con acciones publicas de la partida.
- La diferencia entre conexion estructural (`Mazmorra`/`Grafo<Cueva>`) y requisito jugable (`Puerta`/llave) necesitaba quedar mas explicita.

#### Accion decidida

- `conectarCuevas` queda exclusivamente en `Mazmorra`.
- `Partida` no expone metodos publicos para conectar cuevas ni para crear conexiones.
- Las `Puerta` se pasan como configuracion inicial de `Partida` y se validan contra conexiones ya existentes en `Mazmorra`.
- `Partida` solo autoriza o rechaza el avance segun llave y estado de juego.

### 2026-05-22 - Integracion parcial de Partida

Parte responsable: Parte B / Coordinacion con Parte A y Parte C

#### Va mal o requiere atencion

- Se creo una primera version de `Partida`, `Puerta` y `ObjetoEnMapa`, pero la integracion no esta cerrada en profundidad.
- La logica nueva compila y cubre reglas basicas, pero todavia no esta conectada con el cargador JSON para construir partidas completas.
- JavaFX aun no consume `InterfazPartida`.
- Los enemigos y objetos por cueva viven en una capa interna de `Partida`, no en `Cueva` ni en una fabrica comun.

#### Causa probable

- Se avanzo la logica de B-03 antes de cerrar del todo la capa de construccion de partida entre B y C.
- El JSON actual crea configuracion/mazmorra, pero no define todavia el ensamblaje completo de jugador, enemigos, objetos posicionados, puertas y condiciones de victoria.

#### Accion decidida

- Comentar el codigo nuevo dejando claro que es una primera capa funcional y no la integracion final.
- Mantener `Partida` como nucleo de reglas, pero crear o planificar una fabrica/cargador de partida que conecte JSON con `Partida`.
- Antes de marcar B-03 como HECHA, revisar con Parte C como se construira `Partida` desde JSON y como JavaFX usara `InterfazPartida`.
- Evitar presentar esta version como "juego completo"; es base de logica pendiente de conexion final.

### 2026-05-22 - Defectos detectados en revision independiente de Partida

Parte responsable: Parte B / Revision independiente

#### Va mal o requiere atencion

- La primera version de `Partida` permitia estados incoherentes de ocupacion: jugador y enemigos podian acabar en la misma celda.
- `anadirEnemigo` y `anadirObjetoEnSuelo` estaban en `InterfazPartida`, aunque son metodos de preparacion/fabrica y no acciones de juego.
- `InterfazPartida` exponia referencias mutables demasiado potentes, como `Jugador`, `Mazmorra` y `Puerta`.
- La segunda revision detecto que `getCuevaActual()` seguia exponiendo `Cueva` mutable y que `avanzarACueva` podia solapar al jugador con enemigos de la cueva destino.
- `Puerta.abierta` se marcaba, pero no cambiaba la regla de avance.
- La llave final podia fallar si ya existia un objeto con id `llave-final` y otro codigo de cerradura.
- `ObjetoEnMapa.equals()` ignoraba fila y columna, aunque la clase existe para representar posicion.

#### Causa probable

- Se implemento rapido la primera capa funcional de reglas antes de cerrar todos los invariantes del tablero.
- Se mezclo montaje de partida con contrato publico de juego.
- Algunas decisiones estaban habladas, pero no se habian convertido aun en comprobaciones concretas de codigo.

#### Accion decidida

- Sacar los metodos de preparacion de `InterfazPartida`; quedan como soporte de montaje en `Partida`.
- Sacar del contrato publico las referencias directas a `Jugador`, `Mazmorra`, `Cueva`, `Celda` y `Puerta`, y exponer vistas mediante `PersonajeEnMapa`, `CuevaEnMapa` y `CeldaEnMapa`.
- Bloquear solapes entre jugador y enemigos en colocacion, movimiento de jugador y movimiento enemigo.
- Bloquear tambien el avance a una cueva destino si la celda de entrada del jugador ya esta ocupada por un enemigo.
- Hacer que una puerta abierta pueda atravesarse sin volver a exigir llave.
- Buscar la llave final por codigo, no depender del id fijo, y generar un id alternativo si el id por defecto ya existe.
- Incluir fila y columna en `ObjetoEnMapa.equals()` y `hashCode()`.
- Ampliar `PartidaTest` con casos de revision para estos defectos.

### 2026-05-24 - Sprites de cofre usados para rocas y arbustos

Parte responsable: Parte C / UI

#### Va mal o requiere atencion

- Al implementar los obstaculos ROCA y ARBUSTO en el mapa, no habia sprites adecuados en el Dungeon Asset Pack para representarlos.
- En lugar de informar del problema o pedir assets alternativos, se usaron `chest2.png` y `chest3.png` (sprites de cofre) como placeholder para rocas y arbustos.
- Esto provoco que en el juego aparecieran cofres sobre celdas de pared/obstaculo, confundiendo a los jugadores.

#### Causa probable

- Se priorizo tener un icono visible aunque fuera incorrecto en lugar de dejar la celda sin icono o pedir ayuda.
- No se reviso si el asset pack contenia sprites adecuados antes de elegir los placeholders.

#### Accion decidida

- Alvaro anadio manualmente los assets correctos (`rocks.png`, `bush.png`, `door_closed.png`) al Dungeon Asset Pack.
- El codigo se actualizo para usar estos assets en lugar de chest sprites.
- Recordar: si no existe un asset adecuado, preguntar antes de usar uno incorrecto como placeholder permanente.
