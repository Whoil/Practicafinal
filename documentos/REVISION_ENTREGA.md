# Revisión de entrega

Fecha de revisión: 2026-05-26

## Resumen ejecutivo

El proyecto está preparado para una entrega ordenada desde `documentos/ENTREGA/`: memoria completa en Markdown y PDF, diario de IA resumido, JSON de ejemplo, diagramas UML con fuente PlantUML y PNG renderizado, y la carpeta completa de coordinación `project-management`. La memoria resumida queda como extra, no como documento principal.

## Estado por entregable

| Entregable | Estado | Evidencia | Acción recomendada |
|---|---|---|---|
| 1. Código fuente | Listo en repositorio local | `src/`, `test/`, `scripts/`, `datos/`, `README.md` | Subir/actualizar GitHub y generar ZIP limpio sin `build/`, `out/`, `.idea/` si no se piden. |
| 2. Documento de diseño | Cubierto por memoria completa y docs técnicos | `documentos/ENTREGA/MEMORIA.md`, `documentos/ENTREGA/MEMORIA.pdf`, `documentos/ENTREGA/project-management/ARCHITECTURE.md`, `documentos/ENTREGA/project-management/DECISIONS.md` | Entregar `documentos/ENTREGA/MEMORIA.pdf` como documento principal y conservar `MEMORIA.md` como fuente editable. |
| 3. UML | Fuentes PlantUML y PNG renderizados | `documentos/ENTREGA/diagramas uml/**/*.puml` y `documentos/ENTREGA/diagramas uml/**/*.png` | Entregar la carpeta completa de diagramas UML. |
| 4. JSON de ejemplo | Listo y agrupado | `documentos/ENTREGA/json_ejemplo/` | Entregar la carpeta completa de JSON de ejemplo. |
| 5. Pruebas | Listo | `test/` con 18 clases de test; suite ejecutada: 223/223 tests correctos | Guardar captura o texto del resultado de `scripts/test.ps1` para la defensa/entrega. |
| 6. Diario de IA | Listo con versión resumida y extendida | `documentos/ENTREGA/IA_DIARY_RESUMIDO.md`, `documentos/ENTREGA/project-management/IA_DIARY.md` | Entregar el resumen como documento principal y conservar el diario completo como anexo. |
| 7. Bocetos de interfaz | Listo | `documentos/BOCETO_JAVAFX.md`, `documentos/capturas/boceto_inicial_menu.png`, sección 5 de memoria | Incluir boceto inicial y boceto ASCII; si se puede, añadir una captura final del juego para comparar boceto vs resultado. |

## Documentos de entrega preparados

- `documentos/ENTREGA/MEMORIA.pdf`: memoria principal para entregar.
- `documentos/ENTREGA/MEMORIA.md`: fuente editable de la memoria.
- `documentos/ENTREGA/IA_DIARY_RESUMIDO.md`: versión resumida del diario de IA.
- `documentos/ENTREGA/diagramas uml/`: fuentes `.puml` y renderizados `.png`.
- `documentos/ENTREGA/json_ejemplo/`: JSON documental de prueba, cuevas, partida guardada y ranking.
- `documentos/ENTREGA/project-management/`: documentación completa de coordinación.
- `documentos/ENTREGA/extras/`: memoria resumida, boceto JavaFX y revisión de entrega.

## UML existente

Fuentes localizadas:

- `documentos/diagramas uml/clases/diagrama_clases.puml`: vista de paquetes y dependencias.
- `documentos/diagramas uml/clases/estructuras.puml`: estructuras propias.
- `documentos/diagramas uml/clases/juego.puml`: lógica principal de juego.
- `documentos/diagramas uml/clases/mapa.puml`: mapa, cueva, celda y posición.
- `documentos/diagramas uml/clases/personajes.puml`: personajes.
- `documentos/diagramas uml/clases/objetos.puml`: objetos.
- `documentos/diagramas uml/clases/json.puml`: DTOs, carga y serialización.
- `documentos/diagramas uml/clases/vista.puml`: capa JavaFX.
- `documentos/diagramas uml/clases/control.puml`: controladores.
- `documentos/diagramas uml/casos-de-uso/diagrama_casos_uso.puml`: acciones del jugador.
- `documentos/diagramas uml/actividad/diagrama_actividad_turno.puml`: ciclo de turno.
- `documentos/diagramas uml/estados/diagrama_estados.puml`: estados de partida.
- `documentos/diagramas uml/secuencia/diagrama_secuencia_ataque.puml`: ataque.
- `documentos/diagramas uml/secuencia/diagrama_secuencia_guardado_carga_json.puml`: guardado y carga JSON.
- `documentos/diagramas uml/componentes/diagrama_componentes_mvc.puml`: arquitectura MVC por componentes.
- `documentos/diagramas uml/clases/diagrama_inicial_juego_primera_version.puml`: diagrama histórico inicial.

Renders localizados junto a sus fuentes:

- Cada `.puml` de `documentos/diagramas uml/` tiene un `.png` con el mismo nombre base en la misma carpeta.
- La carpeta `documentos/ENTREGA/diagramas uml/` replica esa estructura para la entrega.

## Diagramas UML recomendados

No hace falta inventar muchos más diagramas. Para la entrega, lo más valioso es mantener juntos cada fuente `.puml` y su render `.png`. Se han añadido y renderizado los diagramas recomendados:

1. **Secuencia de guardado/carga JSON**: `Jugador -> Vista -> Partida -> SerializadorPartida -> JSON`. Es fácil de defender porque conecta interfaz, lógica y persistencia.
2. **Componentes/MVC**: vista, control, modelo, json, datos y estructuras propias. Complementa el diagrama de paquetes para explicar arquitectura en una diapositiva.

Prioridad real:

1. Mantener `documentos/ENTREGA/diagramas uml/` como carpeta principal de entrega de UML.
2. Conservar `documentos/diagramas uml/` como copia organizada de trabajo dentro de la documentación.
3. Evitar depender de `documentos/capturas/uml/` como ubicación principal de renders UML.

## Verificaciones realizadas

- JSON validado con `ConvertFrom-Json`:
  - `datos/cuevas.json`: correcto.
  - `datos/partida_guardada.json`: correcto.
  - `ranking.json`: correcto.
- JSON de ejemplo preparado en `documentos/ENTREGA/json_ejemplo/`:
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
- Revisar visualmente los PNG renderizados de UML antes de comprimir la entrega.
- Añadir una captura final de la interfaz si se quiere reforzar la comparación con bocetos.
- Revisar si conviene regenerar la memoria PDF cuando se cierre definitivamente el paquete de entrega.

