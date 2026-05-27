# Tareas refactor MVC

Prioridades de las tareas pendientes.

## Alta

- [x] **Extraer controlador de juego** — Crear `control/JuegoController.java` que maneje la entrada de teclado/ratón. `PantallaJuego` debe registrar el controlador como `EventHandler` en lugar de tener la lógica inline.
- [x] **Mover constantes de juego** — `DANO_BOLA_FUEGO`, `RANGO_BOLA_FUEGO` y `HechizoPendiente` trasladadas a `modelo/juego/ConstantesJuego.java`.
- [x] **Mover `EscapeMazmorraApp`** a paquete `control/`.
- [x] **Extraer lógica de navegación** — Creado `control/ControladorFlujo.java` con la orquestación de pantallas (introducción, transición, juego, final).
- [x] **Desacoplar constantes de vista** — `RADIO_VISION` y `OPACIDAD_FOG` movidas a `ConstantesJuego.java`.

## Baja

- [ ] **Observador para actualización de vista** — Usar propiedades de JavaFX o un patrón observer para que la vista reaccione automáticamente a cambios del modelo, sin llamar a `actualizar()` manualmente.
