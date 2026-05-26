# Revisión de entrega

Fecha de revisión: 2026-05-26

## Resumen ejecutivo

El proyecto está bastante avanzado para entrega: hay código fuente, memoria en Markdown y PDF, JSON de ejemplo, pruebas JUnit, diario de IA y bocetos de interfaz. Además, se ha preparado una carpeta en `docs/entrega/` con memoria resumida, diario de IA resumido, JSON de ejemplo y material documental para el profesor. La principal mejora pendiente sigue siendo empaquetar el ZIP limpio, confirmar enlace GitHub y renderizar todos los UML si se dispone de herramienta PlantUML.

## Estado por entregable

| Entregable | Estado | Evidencia | Acción recomendada |
|---|---|---|---|
| 1. Código fuente | Listo en repositorio local | `src/`, `test/`, `scripts/`, `datos/`, `README.md` | Subir/actualizar GitHub y generar ZIP limpio sin `build/`, `out/`, `.idea/` si no se piden. |
| 2. Documento de diseño | Cubierto por memoria, resumen y docs técnicos | `docs/MEMORIA.md`, `docs/MEMORIA.pdf`, `docs/entrega/MEMORIA_RESUMIDA.md`, `project-management/ARCHITECTURE.md`, `project-management/DECISIONS.md` | Usar `MEMORIA_RESUMIDA.md` como versión principal y `MEMORIA.md`/PDF como ampliación. |
| 3. UML | Fuentes PlantUML listas; dos UML nuevos añadidos con PNG | `diagramas uml/**/*.puml`, incluyendo secuencia de guardado/carga y componentes MVC; `docs/capturas/uml/*.png` | Entregar fuentes `.puml` y PNG disponibles en `docs/capturas/uml/`. |
| 4. JSON de ejemplo | Listo y agrupado | `docs/entrega/json_ejemplo/` | Entregar la carpeta completa de JSON de ejemplo. |
| 5. Pruebas | Listo | `test/` con 18 clases de test; suite ejecutada: 223/223 tests correctos | Guardar captura o texto del resultado de `scripts/test.ps1` para la defensa/entrega. |
| 6. Diario de IA | Listo con versión resumida | `project-management/IA_DIARY.md`, `docs/entrega/IA_DIARY_RESUMIDO.md` | Entregar el resumen como documento principal y conservar el diario completo como anexo. |
| 7. Bocetos de interfaz | Listo | `docs/BOCETO_JAVAFX.md`, `docs/capturas/boceto_inicial_menu.png`, sección 5 de memoria | Incluir boceto inicial y boceto ASCII; si se puede, añadir una captura final del juego para comparar boceto vs resultado. |

## Documentos de entrega preparados

- `docs/entrega/MEMORIA_RESUMIDA.md`: versión más corta de la memoria.
- `docs/entrega/IA_DIARY_RESUMIDO.md`: versión ampliada del diario de IA, con metodología y resumen de conversaciones.
- `docs/entrega/json_ejemplo/README_JSON_EJEMPLO.md`: explica los JSON preparados para entregar.
- `docs/entrega/json_ejemplo/json_prueba.json`: JSON documental de prueba.
- `docs/entrega/json_ejemplo/cuevas_ejemplo.json`: copia de la configuración real.
- `docs/entrega/json_ejemplo/partida_guardada_ejemplo.json`: ejemplo de guardado de partida incluido en la entrega.
- `docs/entrega/json_ejemplo/ranking_ejemplo.json`: copia de ranking.

## UML existente

Fuentes localizadas:

- `diagramas uml/clases/diagrama_clases.puml`: vista de paquetes y dependencias.
- `diagramas uml/clases/estructuras.puml`: estructuras propias.
- `diagramas uml/clases/juego.puml`: lógica principal de juego.
- `diagramas uml/clases/mapa.puml`: mapa, cueva, celda y posición.
- `diagramas uml/clases/personajes.puml`: personajes.
- `diagramas uml/clases/objetos.puml`: objetos.
- `diagramas uml/clases/json.puml`: DTOs, carga y serialización.
- `diagramas uml/clases/vista.puml`: capa JavaFX.
- `diagramas uml/clases/control.puml`: controladores.
- `diagramas uml/casos-de-uso/diagrama_casos_uso.puml`: acciones del jugador.
- `diagramas uml/actividad/diagrama_actividad_turno.puml`: ciclo de turno.
- `diagramas uml/estados/diagrama_estados.puml`: estados de partida.
- `diagramas uml/secuencia/diagrama_secuencia_ataque.puml`: ataque.
- `diagramas uml/secuencia/diagrama_secuencia_guardado_carga_json.puml`: guardado y carga JSON.
- `diagramas uml/componentes/diagrama_componentes_mvc.puml`: arquitectura MVC por componentes.
- `diagramas uml/clases/diagrama_inicial_juego_primera_version.puml`: diagrama histórico inicial.

Renders localizados en memoria:

- `docs/capturas/uml/diagrama_estados.png`
- `docs/capturas/uml/diagrama_secuencia_ataque.png`
- `docs/capturas/uml/diagrama_secuencia_guardado_carga_json.png`
- `docs/capturas/uml/diagrama_componentes_mvc.png`

## Diagramas UML recomendados

No hace falta inventar muchos más diagramas. Para la entrega, lo más valioso es completar renders de los que ya existen. Se han añadido los dos diagramas recomendados:

1. **Secuencia de guardado/carga JSON**: `Jugador -> Vista -> Partida -> SerializadorPartida -> JSON`. Es fácil de defender porque conecta interfaz, lógica y persistencia.
2. **Componentes/MVC**: vista, control, modelo, json, datos y estructuras propias. Complementa el diagrama de paquetes para explicar arquitectura en una diapositiva.

Prioridad real:

1. Renderizar todos los `.puml` finales si hay herramienta PlantUML disponible.
2. Incluir los dos nuevos `.puml` y sus PNG ya generados.
3. Mantener `docs/capturas/uml/` como carpeta de imágenes UML para la memoria y presentación.

## Verificaciones realizadas

- JSON validado con `ConvertFrom-Json`:
  - `datos/cuevas.json`: correcto.
  - `datos/partida_guardada.json`: correcto.
  - `ranking.json`: correcto.
- JSON de ejemplo preparado en `docs/entrega/json_ejemplo/`:
  - `json_prueba.json`
  - `cuevas_ejemplo.json`
  - `partida_guardada_ejemplo.json`
  - `ranking_ejemplo.json`
- Tests ejecutados con `powershell.exe -ExecutionPolicy Bypass -File scripts\test.ps1`:
  - 223 tests encontrados.
  - 223 tests iniciados.
  - 223 tests correctos.
  - 0 fallos.

## Pendientes antes de entregar

- Confirmar enlace final de GitHub.
- Crear ZIP de entrega desde una copia limpia.
- Renderizar los UML que solo existen como `.puml` si se dispone de PlantUML o herramienta equivalente.
- Añadir una captura final de la interfaz si se quiere reforzar la comparación con bocetos.
- Revisar si conviene regenerar la memoria PDF cuando se cierre definitivamente el paquete de entrega.

