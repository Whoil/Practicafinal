# Tareas pendientes — Escape de la Mazmorra

## Prioridad Alta (Core UX)

### 1. Feedback visual de acciones rechazadas ✅
Cuando el jugador pulsa una tecla/botón y la acción es rechazada (moverse dos veces, atacar sin enemigo, etc.), la UI muestra un mensaje temporal (2s) sobre el grid con color rojo/verde según éxito.
- Dónde: `PantallaJuego.java` — `mostrarFeedback()`, `ejecutarAccion()`
- Teclado y botones chequean el `boolean` de retorno

### 2. Botón de "Terminar Turno" más visible ✅
El botón ahora tiene fondo dorado semitransparente, borde `#FFD700`, bordes redondeados, y texto "=== TERMINAR TURNO [T] ===".
- Dónde: `PantallaJuego.java` — panel de acciones

### 2b. Elegir direccion del ataque (pendiente)
Ahora `Partida.atacar()` selecciona automaticamente el primer enemigo adyacente encontrado. Si el jugador tiene varios enemigos cerca, debe poder decidir a quien atacar.
- Propuesta: permitir ataque direccional con flechas/WASD tras pulsar atacar, o botones `Atacar arriba/abajo/izquierda/derecha`.
- Modelo: exponer un metodo claro tipo `atacarDireccion(df, dc)` o reutilizar `atacar(fila, columna)` desde la UI.
- UI: mostrar feedback si no hay enemigo en la direccion elegida.
- Tests: cubrir varios enemigos adyacentes y verificar que solo recibe dano el enemigo seleccionado.
- Donde: `Partida.java`, `PantallaJuego.java`, `PartidaTest.java`

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

### 4. Pantallas de Victoria / Derrota ✅
Cuando el jugador gana o pierde, mostrar una pantalla dedicada con mensaje narrativo y opción de volver al menú.
- Dónde: `PantallaFinal.java` (nueva clase)
- Pantalla de victoria: fondo dorado, título "VICTORIA" en #d4af37, texto épico de la derrota de Malakor
- Pantalla de derrota: fondo rojo oscuro, título "HAS MUERTO" en #8b0000, texto trágico
- Integrado en el flujo narrativo de `EscapeMazmorraApp.java`

## Prioridad Media (Contenido)

### 5. Más habitaciones y mapas más grandes ✅
- Mapas rediseñados con progresión de tamaño: 7×7 (fácil), 10×10 (media), 13×13 (difícil)
- Enemigos y objetos reposicionados según la nueva escala
- Dónde: `datos/cuevas.json`

### 6. Textos de historia por habitación ✅
Al entrar a una nueva cueva o iniciar partida, se muestran pantallas narrativas con texto contextual:
- **Introducción**: historia inicial del Mago convocado para derrotar a Malakor
- **Transiciones**: una pantalla reutilizable (`PantallaTransicion`) antes de cada cueva con título, texto y fondo temático
- Dónde: `PantallaIntroduccion.java`, `PantallaTransicion.java`, `DatosTemaCueva.java`

### 7. Tematizar habitaciones ✅
Cada cueva tiene identidad visual completa:
- **Criptas de Marfil**: fondo gris ceniza, muros color hueso (#d2cdc3), enemigos esqueleto (💀)
- **Páramo Putrefacto**: fondo verde pantano, muros verde podrido (#468246), enemigos zombie (🧟)
- **Abismo de Malakor**: fondo rojo ígneo, muros rojo sangre (#aa2d2d), enemigos demonio (👹)
- Datos centralizados en `DatosTemaCueva.java`
- Pantallas de transición con fondo degradado temático

## Prioridad Media (Visual)

### 8. Iconos con emojis / imágenes ✅
Círculos de colores reemplazados por emojis temáticos:
- Jugador (Mago): 🧙
- Enemigos por cueva: 💀 (Criptas), 🧟 (Páramo), 👹 (Abismo)
- Bosses: ☠️ (Guardián Osario), 🧌 (Zombie lord), 😈 (Malakor)
- Objetos: 🧪 (pociones), 🔑 (llaves), 🛡️ (escudos), 🏹 (arcos), 🗡️ (armas)
- Dónde: `PantallaJuego.java` — `actualizar()` y nuevo método `emojiParaObjeto()`

### 9. Mejora general de la UI del juego ⏳ (pendiente)
- Tipografía consistente
- Colores más pulidos
- Paneles con bordes redondeados
- Fondo con ambientación (oscuro, mazmorra)
- Animaciones suaves
- Dónde: `PantallaJuego.java`, `EscapeMazmorraApp.java`

### 9b. Muros coloreados por mazmorra ✅
Las celdas MURO se renderizan con el color temático de la cueva actual en lugar del gris genérico:
- Criptas de Marfil: #d2cdc3 (hueso)
- Páramo Putrefacto: #468246 (verde podrido)
- Abismo de Malakor: #aa2d2d (rojo sangre)
- Todos los muros mantienen el mismo tamaño que el resto de celdas
- Dónde: `PantallaJuego.java` — `colorParaTipo()` y `DatosTemaCueva.java`

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
