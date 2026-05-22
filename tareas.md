# Tareas pendientes — Escape de la Mazmorra

## Prioridad Alta (Core UX)

### 1. Feedback visual de acciones rechazadas ✅
Cuando el jugador pulsa una tecla/botón y la acción es rechazada (moverse dos veces, atacar sin enemigo, etc.), la UI muestra un mensaje temporal (2s) sobre el grid con color rojo/verde según éxito.
- Dónde: `PantallaJuego.java` — `mostrarFeedback()`, `ejecutarAccion()`
- Teclado y botones chequean el `boolean` de retorno

### 2. Botón de "Terminar Turno" más visible ✅
El botón ahora tiene fondo dorado semitransparente, borde `#FFD700`, bordes redondeados, y texto "=== TERMINAR TURNO [T] ===".
- Dónde: `PantallaJuego.java` — panel de acciones

### 3. Pantalla de Inicio funcional ⏳ (planificado, pendiente de implementar)
**Navegación actual**: `Inicio → Opciones → Iniciar partida ✓ / Estadísticas (no-op) / Ajustes (vuelve a Inicio)`

**Cambios necesarios en `EscapeMazmorraApp.java`**:

| Botón | Acción |
|---|---|
| Iniciar partida | Ya funciona |
| **Cargar partida** | FileChooser → `SerializadorPartida.cargar()` → construir `Partida` → lanzar juego |
| Ajustes | Navegar a nueva pantalla `mostrarPantallaAjustes()` |
| **Salir** | `stage.close()` |

**Nueva pantalla `mostrarPantallaAjustes()`**:
- Controles básicos (placeholder) + botón "Volver" con fade transition

**Duda técnica — Cargar partida**:
- `Partida.cargar(ruta)` no existe. Implementar:
  1. `SerializadorPartida.cargar(ruta)` → `DatosPartidaDTO`
  2. Convertir con `dtoAMazmorra()`, `dtoAJugador()`, etc.
  3. Reconstruir puertas, enemigos, objetos
- Alternativa: aplazar y poner botón deshabilitado

**Archivos implicados**: `EscapeMazmorraApp.java`, `Partida.java` (nuevo método estático `cargar`)

### 4. Pantallas de Victoria / Derrota ⏳ (pendiente — siguiente sesión junto con bloque de tareas 5-12)
Cuando el jugador gana o pierde, mostrar una pantalla dedicada con mensaje, estadísticas y opciones (volver al menú, reintentar).
- Dónde: Nueva clase o en `EscapeMazmorraApp.java`

## Prioridad Media (Contenido)

### 5. Más habitaciones y mapas más grandes
- Aumentar el número de cuevas en `datos/cuevas.json`
- Aumentar el tamaño de las cuevas actuales (7x7 o más)
- Variar la dificultad y disposición de enemigos/objetos

### 6. Textos de historia por habitación
Al entrar a una nueva cueva, mostrar un panel/diálogo con texto narrativo que contextualice la habitación.
- Nuevo campo `historia` en el JSON de cuevas
- Nuevo diálogo/pantalla en `PantallaJuego.java`

### 7. Tematizar habitaciones
Dar identidad visual a cada cueva (colores de fondo diferentes, tipos de enemigos temáticos, decoración única).
- Dónde: `datos/cuevas.json` + `PantallaJuego.java`

## Prioridad Media (Visual)

### 8. Iconos con emojis / imágenes
Reemplazar los círculos de colores por emojis o sprites pequeños:
- Jugador: 🧙 / 🗡️
- Enemigos: 🧟 / 👹 / 💀
- Objetos: ❤️ / 🔑 / 🛡️ / 🏹
- Pociones: 🧪
- Dónde: `PantallaJuego.java` — `actualizar()` y `colorParaTipo()`

### 9. Mejora general de la UI del juego
- Tipografía consistente
- Colores más pulidos
- Paneles con bordes redondeados
- Fondo con ambientación (oscuro, mazmorra)
- Animaciones suaves
- Dónde: `PantallaJuego.java`, `EscapeMazmorraApp.java`

## Prioridad Baja (Calidad de vida)

### 10. Guardado/Carga desde la UI
Botón "Guardar partida" en el menú de pausa o acciones; opción "Cargar" en el menú principal con selector de archivos.
- Dónde: `PantallaJuego.java`, `EscapeMazmorraApp.java`

### 11. Tutorial o ayuda integrada
Primera vez que se juega, mostrar un overlay con los controles básicos (WASD, T, Espacio, R).
- Dónde: `PantallaJuego.java` o nueva clase `TutorialOverlay`

### 12. Efectos de sonido / Música
Añadir sonidos para pasos, ataques, recoger objetos, cambio de habitación, música de fondo.
- Nuevos recursos + clase `AudioManager`
