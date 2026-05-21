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
- Los documentos de `project-management/` son utiles para coordinar, pero tambien son superficie compartida y requieren mas cuidado que un archivo exclusivo de Parte A.

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
