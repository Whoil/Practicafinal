package vista;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import Estructuras.ListaDE;
import Estructuras.ListaSE;
import Estructuras.MiIterador;
import modelo.juego.CeldaEnMapa;
import modelo.juego.CuevaEnMapa;
import modelo.juego.ObjetoEnMapa;
import modelo.juego.Partida;
import modelo.mapa.TipoCelda;
import modelo.objetos.Objeto;
import modelo.objetos.Pocion;
import modelo.personajes.Enemigo;
import modelo.personajes.Jugador;

/**
 * PantallaJuego — Interfaz grafica de la partida.
 *
 * Muestra la cueva actual con el grid de celdas coloreadas,
 * el jugador (circulo azul), los enemigos (circulos rojos),
 * los objetos (circulos amarillos), y los paneles de estadisticas,
 * inventario, acciones y log de eventos.
 *
 * Soporta teclado: WASD / flechas para moverse.
 */
public class PantallaJuego {

    private static final String FONT = "Georgia, serif";
    private static final int ANCHO = 1280;
    private static final int ALTO = 720;

    private final Partida partida;
    private final Stage stage;
    private final Runnable volverAlMenu;

    // Nodos JavaFX que se actualizan dinamicamente
    private Pane gridLayer;
    private GridPane gridCeldas;
    private Pane gridWalls;
    private Pane gridOverlay;
    private StackPane[][] celdas;
    private Text txtVida, txtAtaque, txtDefensa, txtTurnos, txtCueva, txtEstado;
    private VBox inventarioBox;
    private VBox logBox;
    private Text txtCuevaNombre;

    // Acciones desactivables
    private VBox accionesBox;

    // Feedback visual
    private Text txtFeedback;
    private StackPane feedbackPane;
    private Timeline feedbackTimer;

    // Callbacks para navegacion narrativa
    private Runnable alCambiarCueva;
    private Runnable alTerminarPartida;

    // Flag para evitar bucles de deteccion de fin de partida
    private boolean partidaFinalizada = false;

    // Ultima cueva renderizada para detectar cambios de cueva
    private String ultimaCuevaId = null;

    // Efecto visual de ataque (posicion y contador)
    private int ataqueFila = -1, ataqueCol = -1;
    private Timeline ataqueTimer;

    // Efecto visual cuando el jugador recibe dano
    private int recibirAtaqueFila = -1, recibirAtaqueCol = -1;
    private Timeline recibirAtaqueTimer;

    public PantallaJuego(Partida partida, Stage stage, Runnable volverAlMenu) {
        this.partida = partida;
        this.stage = stage;
        this.volverAlMenu = volverAlMenu;
    }

    /**
     * Establece el callback que se ejecuta al cambiar de cueva.
     * La pantalla de juego delega la transicion narrativa al
     * controlador principal (EscapeMazmorraApp).
     */
    public void setAlCambiarCueva(Runnable alCambiarCueva) {
        this.alCambiarCueva = alCambiarCueva;
    }

    /**
     * Establece el callback que se ejecuta cuando la partida
     * termina (victoria o derrota).
     */
    public void setAlTerminarPartida(Runnable alTerminarPartida) {
        this.alTerminarPartida = alTerminarPartida;
    }

    /**
     * Construye y devuelve la Scene de juego.
     */
    public Scene crearScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1A1A1A;");

        // Layout horizontal: grid (izquierda) + panel derecho
        HBox centro = new HBox(10);
        centro.setPadding(new Insets(10));

        // --- Grid de la cueva ---
        VBox gridConLog = new VBox(5);
        gridLayer = new StackPane();
        gridCeldas = new GridPane();
        gridWalls = new Pane();
        gridWalls.setMouseTransparent(true);
        gridOverlay = new Pane();
        gridOverlay.setMouseTransparent(true);
        gridLayer.getChildren().addAll(gridCeldas, gridWalls, gridOverlay);

        txtFeedback = new Text("");
        txtFeedback.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        txtFeedback.setFill(Color.WHITE);
        feedbackPane = new StackPane(txtFeedback);
        feedbackPane.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.7), new CornerRadii(8), Insets.EMPTY)));
        feedbackPane.setStyle("-fx-padding: 8 16; -fx-border-color: #888; -fx-border-radius: 8; -fx-border-width: 1;");
        feedbackPane.setVisible(false);
        feedbackPane.setUserData(txtFeedback);
        StackPane.setAlignment(feedbackPane, Pos.TOP_CENTER);
        StackPane.setMargin(feedbackPane, new Insets(10, 0, 0, 0));
        gridLayer.getChildren().add(feedbackPane);
        gridConLog.getChildren().add(gridLayer);

        // --- Log (debajo del grid) ---
        logBox = new VBox(2);
        logBox.setPrefHeight(120);
        logBox.setMaxHeight(120);
        logBox.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-padding: 6; -fx-border-color: #444; -fx-border-width: 1;");
        ScrollPane logScroll = new ScrollPane(logBox);
        logScroll.setPrefHeight(120);
        logScroll.setMaxHeight(120);
        logScroll.setFitToWidth(true);
        logScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        logScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        gridConLog.getChildren().add(logScroll);

        centro.getChildren().add(gridConLog);

        // --- Panel derecho ---
        VBox panelDer = new VBox(8);
        panelDer.setPrefWidth(340);
        panelDer.setPadding(new Insets(4));
        panelDer.setStyle("-fx-background-color: rgba(0,0,0,0.4); -fx-border-color: #444; -fx-border-width: 1; -fx-padding: 8;");

        // Cueva nombre
        txtCuevaNombre = new Text();
        txtCuevaNombre.setFont(Font.font(FONT, FontWeight.BOLD, 16));
        txtCuevaNombre.setFill(Color.web("#C89D65"));
        panelDer.getChildren().add(txtCuevaNombre);

        // Stats
        VBox statsBox = new VBox(2);
        statsBox.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-padding: 6; -fx-border-color: #555; -fx-border-width: 1;");
        Text tituloStats = new Text("ESTADISTICAS");
        tituloStats.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        tituloStats.setFill(Color.web("#FFD700"));
        txtVida = labelStats("Vida");
        txtAtaque = labelStats("Ataque");
        txtDefensa = labelStats("Defensa");
        txtTurnos = labelStats("Turnos");
        txtCueva = labelStats("Cueva");
        txtEstado = labelStats("Estado");
        statsBox.getChildren().addAll(tituloStats, txtVida, txtAtaque, txtDefensa, txtTurnos, txtCueva, txtEstado);
        panelDer.getChildren().add(statsBox);

        // Inventario
        VBox invBox = new VBox(2);
        invBox.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-padding: 6; -fx-border-color: #555; -fx-border-width: 1;");
        Text tituloInv = new Text("INVENTARIO");
        tituloInv.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        tituloInv.setFill(Color.web("#FFD700"));
        inventarioBox = new VBox(2);
        ScrollPane invScroll = new ScrollPane(inventarioBox);
        invScroll.setPrefHeight(120);
        invScroll.setMaxHeight(120);
        invScroll.setFitToWidth(true);
        invScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        invBox.getChildren().addAll(tituloInv, invScroll);
        panelDer.getChildren().add(invBox);

        // Acciones
        VBox accBox = new VBox(4);
        accBox.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-padding: 6; -fx-border-color: #555; -fx-border-width: 1;");
        Text tituloAcc = new Text("ACCIONES");
        tituloAcc.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        tituloAcc.setFill(Color.web("#FFD700"));
        accionesBox = new VBox(4);
        accBox.getChildren().addAll(tituloAcc, accionesBox);
        panelDer.getChildren().add(accBox);

        centro.getChildren().add(panelDer);
        root.setCenter(centro);

        // Teclado (movimiento WASD/flechas + acciones)
        Scene scene = new Scene(root, ANCHO, ALTO);
        scene.setOnKeyPressed(e -> {
            try {
                KeyCode k = e.getCode();
                boolean ok = true;
                String msg = null;
                Jugador jug = partida.getJugador();
                int pf = jug.getFila(), pc = jug.getColumna();
                if (k == KeyCode.W || k == KeyCode.UP) {
                    if (partida.hayEnemigoEn(pf - 1, pc)) { ok = false; msg = "Hay un enemigo ahi"; }
                    else { ok = partida.moverJugadorArriba(); if (!ok) msg = "No puedes moverte mas este turno"; }
                } else if (k == KeyCode.S || k == KeyCode.DOWN) {
                    if (partida.hayEnemigoEn(pf + 1, pc)) { ok = false; msg = "Hay un enemigo ahi"; }
                    else { ok = partida.moverJugadorAbajo(); if (!ok) msg = "No puedes moverte mas este turno"; }
                } else if (k == KeyCode.A || k == KeyCode.LEFT) {
                    if (partida.hayEnemigoEn(pf, pc - 1)) { ok = false; msg = "Hay un enemigo ahi"; }
                    else { ok = partida.moverJugadorIzquierda(); if (!ok) msg = "No puedes moverte mas este turno"; }
                } else if (k == KeyCode.D || k == KeyCode.RIGHT) {
                    if (partida.hayEnemigoEn(pf, pc + 1)) { ok = false; msg = "Hay un enemigo ahi"; }
                    else { ok = partida.moverJugadorDerecha(); if (!ok) msg = "No puedes moverte mas este turno"; }
                } else if (k == KeyCode.SPACE) {
                    Enemigo target = partida.getEnemigoAdyacente();
                    if (target != null) {
                        ataqueFila = target.getFila();
                        ataqueCol = target.getColumna();
                        ok = partida.atacar();
                        if (ok) {
                            iniciarEfectoAtaque();
                        } else {
                            ataqueFila = ataqueCol = -1;
                            msg = "No puedes atacar ahora";
                        }
                    } else {
                        ok = false;
                        msg = "No hay enemigo para atacar";
                    }
                }
                else if (k == KeyCode.R) { ok = partida.recogerObjeto(); if (!ok) msg = "No hay objeto que recoger aqui"; }
                else if (k == KeyCode.T) {
                    int hpAntes = partida.getJugador().getVidaActual();
                    ok = partida.terminarTurno();
                    if (ok) {
                        if (partida.getJugador().getVidaActual() < hpAntes) {
                            Jugador j2 = partida.getJugador();
                            iniciarEfectoRecibirAtaque(j2.getFila(), j2.getColumna());
                        }
                        msg = "Turno terminado";
                    } else {
                        msg = "No puedes terminar el turno ahora";
                    }
                }
                actualizar();

                // Auto-terminar turno
                if (partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO) {
                    boolean autoTurno = partida.isMovimientoRealizado() && partida.isAccionRealizada();
                    autoTurno = autoTurno || (partida.isMovimientoRealizado()
                            && msg != null && msg.contains("No puedes moverte mas"));
                    if (autoTurno) {
                        int hpAntes = partida.getJugador().getVidaActual();
                        partida.terminarTurno();
                        if (partida.getJugador().getVidaActual() < hpAntes) {
                            Jugador j2 = partida.getJugador();
                            iniciarEfectoRecibirAtaque(j2.getFila(), j2.getColumna());
                        }
                        actualizar();
                        if (msg != null && msg.contains("No puedes moverte mas")) {
                            msg = null;
                        }
                    }
                }

                if (msg != null) {
                    mostrarFeedback(msg, ok ? Color.LIGHTGREEN : Color.rgb(255, 120, 100));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarFeedback("Error: " + ex.getMessage(), Color.RED);
            }
        });

        // Que el clic en cualquier parte devuelva el foco al root (teclado)
        root.setFocusTraversable(true);
        root.setOnMouseClicked(e -> root.requestFocus());

        // Construir grid inicial y paneles
        construirGrid();
        actualizar();
        root.requestFocus();

        return scene;
    }

    // ---------------------------------------------------------------
    // Construccion del grid de celdas
    // ---------------------------------------------------------------

    private void construirGrid() {
        gridCeldas.getChildren().clear();
        gridWalls.getChildren().clear();
        gridCeldas.setHgap(1);
        gridCeldas.setVgap(1);

        CuevaEnMapa cueva = partida.getCuevaActual();
        if (cueva == null) return;

        int filas = cueva.getFilas();
        int cols = cueva.getColumnas();
        double cellSize = Math.min(600.0 / cols, 600.0 / filas);
        cellSize = Math.min(cellSize, 80);

        celdas = new StackPane[filas][cols];

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                CeldaEnMapa celda = cueva.getCelda(f, c);
                Rectangle rect;
                if (celda.getTipo() == TipoCelda.MURO) {
                    rect = new Rectangle(cellSize, cellSize);
                    rect.setFill(Color.TRANSPARENT);
                    rect.setStroke(null);
                } else {
                    rect = new Rectangle(cellSize, cellSize);
                    rect.setFill(colorParaTipo(celda.getTipo()));
                    rect.setStroke(Color.rgb(60, 60, 60));
                    rect.setStrokeWidth(0.5);
                }

                StackPane cellPane = new StackPane(rect);
                cellPane.setPrefSize(cellSize, cellSize);
                celdas[f][c] = cellPane;
                // Click para moverse
                final int ff = f, cc = c;
                cellPane.setOnMouseClicked(e -> {
                    boolean ok;
                    String clickMsg = null;
                    if (partida.hayEnemigoEn(ff, cc)) {
                        ok = false;
                        clickMsg = "Hay un enemigo ahi";
                    } else {
                        ok = partida.moverJugador(ff, cc);
                        if (!ok) clickMsg = "No puedes moverte alli";
                    }
                    actualizar();
                    if (ok && partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO
                            && partida.isMovimientoRealizado() && partida.isAccionRealizada()) {
                        int hpAntes = partida.getJugador().getVidaActual();
                        partida.terminarTurno();
                        if (partida.getJugador().getVidaActual() < hpAntes) {
                            Jugador j2 = partida.getJugador();
                            iniciarEfectoRecibirAtaque(j2.getFila(), j2.getColumna());
                        }
                        actualizar();
                    } else if (clickMsg != null) {
                        mostrarFeedback(clickMsg, Color.rgb(255, 120, 100));
                    }
                });
                cellPane.setCursor(Cursor.HAND);
                gridCeldas.add(cellPane, c, f);
            }
        }

        gridOverlay.setPrefSize(cols * cellSize, filas * cellSize);

        // Dibujar muros finos en gridWalls
        double grosor = 5;
        Color colorMuro = DatosTemaCueva.paraCuevaId(cueva.getId()).getColorMuro();
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                CeldaEnMapa celdaActual = cueva.getCelda(f, c);
                if (celdaActual == null || celdaActual.getTipo() != TipoCelda.MURO) continue;
                double x = c * (cellSize + 1);
                double y = f * (cellSize + 1);
                // Arriba
                CeldaEnMapa arriba = f > 0 ? cueva.getCelda(f - 1, c) : null;
                if (f == 0 || arriba == null || arriba.getTipo() != TipoCelda.MURO) {
                    Rectangle w = new Rectangle(cellSize, grosor);
                    w.setFill(colorMuro); w.setStroke(null);
                    w.setLayoutX(x); w.setLayoutY(y);
                    gridWalls.getChildren().add(w);
                }
                // Abajo
                CeldaEnMapa abajo = f < filas - 1 ? cueva.getCelda(f + 1, c) : null;
                if (f == filas - 1 || abajo == null || abajo.getTipo() != TipoCelda.MURO) {
                    Rectangle w = new Rectangle(cellSize, grosor);
                    w.setFill(colorMuro); w.setStroke(null);
                    w.setLayoutX(x); w.setLayoutY(y + cellSize - grosor);
                    gridWalls.getChildren().add(w);
                }
                // Izquierda
                CeldaEnMapa izq = c > 0 ? cueva.getCelda(f, c - 1) : null;
                if (c == 0 || izq == null || izq.getTipo() != TipoCelda.MURO) {
                    Rectangle w = new Rectangle(grosor, cellSize);
                    w.setFill(colorMuro); w.setStroke(null);
                    w.setLayoutX(x); w.setLayoutY(y);
                    gridWalls.getChildren().add(w);
                }
                // Derecha
                CeldaEnMapa der = c < cols - 1 ? cueva.getCelda(f, c + 1) : null;
                if (c == cols - 1 || der == null || der.getTipo() != TipoCelda.MURO) {
                    Rectangle w = new Rectangle(grosor, cellSize);
                    w.setFill(colorMuro); w.setStroke(null);
                    w.setLayoutX(x + cellSize - grosor); w.setLayoutY(y);
                    gridWalls.getChildren().add(w);
                }
            }
        }
    }

    // ---------------------------------------------------------------
    // Actualizacion (redibujar cada vez que cambia el estado)
    // ---------------------------------------------------------------

    private void actualizar() {
        try {
            // Detectar fin de partida (victoria o derrota)
            if (!partidaFinalizada && partida.getEstado() != modelo.juego.EstadoPartida.EN_CURSO) {
                partidaFinalizada = true;
                if (alTerminarPartida != null) {
                    alTerminarPartida.run();
                }
                return;
            }

            // Overlay de entidades (jugador, enemigos, objetos)
            gridOverlay.getChildren().clear();
            CuevaEnMapa cueva = partida.getCuevaActual();
            if (cueva == null) return;

            int filas = cueva.getFilas();
            int cols = cueva.getColumnas();
            double cellSize = Math.min(600.0 / cols, 600.0 / filas);
            cellSize = Math.min(cellSize, 80);

            // Reconstruir el grid si la cueva ha cambiado
            String cuevaIdActual = cueva.getId();
            if (!cuevaIdActual.equals(ultimaCuevaId)) {
                ultimaCuevaId = cuevaIdActual;
                construirGrid();
            }

            // Limpiar emojis anteriores de las celdas (conservar fondo)
            if (celdas != null) {
                for (int f = 0; f < filas && f < celdas.length; f++) {
                    if (celdas[f] == null) continue;
                    for (int c = 0; c < cols && c < celdas[f].length; c++) {
                        StackPane cell = celdas[f][c];
                        if (cell != null) {
                            cell.getChildren().remove(1, cell.getChildren().size());
                        }
                    }
                }
            }

            // Restaurar colores de todas las celdas y aplicar flash de ataque
            if (celdas != null) {
                for (int f = 0; f < filas && f < celdas.length; f++) {
                    if (celdas[f] == null) continue;
                    for (int c = 0; c < cols && c < celdas[f].length; c++) {
                        StackPane cell = celdas[f][c];
                        if (cell != null && !cell.getChildren().isEmpty()
                                && cell.getChildren().get(0) instanceof Rectangle) {
                            Rectangle r = (Rectangle) cell.getChildren().get(0);
                            CeldaEnMapa celdaActual = cueva.getCelda(f, c);
                            r.setFill(celdaActual != null ? colorParaTipo(celdaActual.getTipo()) : Color.TRANSPARENT);
                        }
                    }
                }
                if (ataqueFila >= 0 && ataqueFila < filas && ataqueCol >= 0 && ataqueCol < cols) {
                    StackPane cell = celdas[ataqueFila][ataqueCol];
                    if (cell != null && !cell.getChildren().isEmpty()
                            && cell.getChildren().get(0) instanceof Rectangle) {
                        ((Rectangle) cell.getChildren().get(0)).setFill(Color.rgb(255, 200, 200));
                    }
                }
                if (recibirAtaqueFila >= 0 && recibirAtaqueFila < filas && recibirAtaqueCol >= 0 && recibirAtaqueCol < cols) {
                    StackPane cell = celdas[recibirAtaqueFila][recibirAtaqueCol];
                    if (cell != null && !cell.getChildren().isEmpty()
                            && cell.getChildren().get(0) instanceof Rectangle) {
                        ((Rectangle) cell.getChildren().get(0)).setFill(Color.rgb(255, 80, 80));
                    }
                }
            }

        // Obtener tema visual segun la cueva actual
        DatosTemaCueva tema = DatosTemaCueva.paraCuevaId(cuevaIdActual);
        double emojiSize = Math.min(cellSize * 0.65, 40);

        // Jugador (emoji de mago)
        Jugador j = partida.getJugador();
        Text playerEmoji = crearEmoji("\uD83E\uDDD9", emojiSize);
        DropShadow sombraP = new DropShadow();
        sombraP.setRadius(6);
        sombraP.setColor(Color.rgb(0, 0, 0, 0.6));
        playerEmoji.setEffect(sombraP);
        if (j.getFila() >= 0 && j.getFila() < filas && j.getColumna() >= 0 && j.getColumna() < cols) {
            celdas[j.getFila()][j.getColumna()].getChildren().add(playerEmoji);
        }

        // Enemigos (emoji segun tematica y tipo)
        ListaDE<Enemigo> enemigos = partida.getEnemigosActuales();
        MiIterador<Enemigo> itE = enemigos.getIterador();
        while (itE.hasNext()) {
            Enemigo e = itE.next();
            boolean esBoss = e instanceof modelo.personajes.Boss;
            String emoji = esBoss ? tema.getEmojiBoss() : tema.getEmojiEnemigo();
            Text enemyEmoji = crearEmoji(emoji, emojiSize);
            if (esBoss) {
                DropShadow sombraBoss = new DropShadow();
                sombraBoss.setRadius(10);
                sombraBoss.setColor(Color.rgb(200, 50, 50, 0.5));
                enemyEmoji.setEffect(sombraBoss);
            }
            if (e.getFila() >= 0 && e.getFila() < filas && e.getColumna() >= 0 && e.getColumna() < cols) {
                celdas[e.getFila()][e.getColumna()].getChildren().add(enemyEmoji);
            }
        }

        // Objetos en el mapa (emoji segun tipo)
        ListaDE<ObjetoEnMapa> objetos = partida.getObjetosActuales();
        MiIterador<ObjetoEnMapa> itO = objetos.getIterador();
        while (itO.hasNext()) {
            ObjetoEnMapa om = itO.next();
            String emoji = emojiParaObjeto(om.getObjeto());
            Text itemEmoji = crearEmoji(emoji, emojiSize * 0.6);
            if (om.getFila() >= 0 && om.getFila() < filas && om.getColumna() >= 0 && om.getColumna() < cols) {
                celdas[om.getFila()][om.getColumna()].getChildren().add(itemEmoji);
            }
        }

        // Barras de vida sobre entidades
        if (j.getFila() >= 0 && j.getFila() < filas && j.getColumna() >= 0 && j.getColumna() < cols) {
            agregarBarraVida(celdas[j.getFila()][j.getColumna()], j.getVidaActual(), j.getVidaMaxima(), cellSize);
        }
        MiIterador<Enemigo> itHp = enemigos.getIterador();
        while (itHp.hasNext()) {
            Enemigo e = itHp.next();
            if (e.estaVivo() && e.getFila() >= 0 && e.getFila() < filas && e.getColumna() >= 0 && e.getColumna() < cols) {
                agregarBarraVida(celdas[e.getFila()][e.getColumna()], e.getVidaActual(), e.getVidaMaxima(), cellSize);
            }
        }

        // Stats
        txtVida.setText("Vida: " + j.getVidaActual() + "/" + j.getVidaMaxima());
        txtAtaque.setText("Ataque: " + j.getAtaqueTotal() + (j.getArmaEquipada() != null ? " (" + j.getArmaEquipada().getNombre() + ")" : ""));
        txtDefensa.setText("Defensa: " + j.getDefensaTotal() + (j.getEscudoEquipado() != null ? " (" + j.getEscudoEquipado().getNombre() + ")" : ""));
        txtTurnos.setText("Turnos restantes: " + partida.getTurnosRestantes());
        txtCueva.setText("Cueva: " + cueva.getId());
        txtEstado.setText("Estado: " + partida.getEstado());
        txtCuevaNombre.setText("CUEVA: " + cueva.getId().toUpperCase());

        // Inventario
        inventarioBox.getChildren().clear();
        ListaDE<Objeto> inv = j.getInventario();
        MiIterador<Objeto> itInv = inv.getIterador();
        while (itInv.hasNext()) {
            Objeto obj = itInv.next();
            HBox itemRow = new HBox(5);
            Text txtObj = new Text(obj.getNombre());
            txtObj.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
            txtObj.setFill(Color.WHITE);
            itemRow.getChildren().add(txtObj);

            if (obj instanceof Pocion) {
                Text btnUsar = crearBotonTexto("[USAR]");
                btnUsar.setOnMouseClicked(e -> ejecutarAccion(partida.usarPocion(obj.getId()), "No puedes usar esto ahora"));
                itemRow.getChildren().add(btnUsar);
            }
            if (obj.esEquipable()) {
                Text btnEquip = crearBotonTexto("[EQUIPAR]");
                btnEquip.setOnMouseClicked(e -> ejecutarAccion(partida.equiparItem(obj.getId()), "No puedes equipar esto ahora"));
                itemRow.getChildren().add(btnEquip);
            }

            inventarioBox.getChildren().add(itemRow);
        }
        if (inv.getSize() == 0) {
            Text vacio = new Text("(vacio)");
            vacio.setFont(Font.font(FONT, FontWeight.NORMAL, 12));
            vacio.setFill(Color.GRAY);
            inventarioBox.getChildren().add(vacio);
        }

        // Acciones
        accionesBox.getChildren().clear();
        agregarBotonAccion("ARRIBA [W]", () -> ejecutarAccion(partida.moverJugadorArriba(), "No puedes moverte mas este turno"));
        HBox movHoriz = new HBox(4);
        movHoriz.setAlignment(Pos.CENTER);
        Text btnIzq = crearBotonTexto("< IZQ [A]");
        btnIzq.setOnMouseClicked(e -> ejecutarAccion(partida.moverJugadorIzquierda(), "No puedes moverte mas este turno"));
        Text btnDer = crearBotonTexto("DER [D] >");
        btnDer.setOnMouseClicked(e -> ejecutarAccion(partida.moverJugadorDerecha(), "No puedes moverte mas este turno"));
        movHoriz.getChildren().addAll(btnIzq, btnDer);
        accionesBox.getChildren().add(movHoriz);
        agregarBotonAccion("ABAJO [S]", () -> ejecutarAccion(partida.moverJugadorAbajo(), "No puedes moverte mas este turno"));

        agregarBotonAccion("ATACAR [ESPACIO]", () -> ejecutarAccion(partida.atacar(), "No hay enemigo para atacar"));
        agregarBotonAccion("RECOGER [R]", () -> ejecutarAccion(partida.recogerObjeto(), "No hay objeto que recoger"));

        // Boton Terminar Turno destacado
        Text btnTurno = crearBotonTexto("=== TERMINAR TURNO [T] ===");
        btnTurno.setFill(Color.web("#FFD700"));
        btnTurno.setStyle("-fx-background-color: rgba(200,150,50,0.3); -fx-padding: 4 8; -fx-border-color: #FFD700; -fx-border-width: 1; -fx-border-radius: 4;");
        btnTurno.setOnMouseClicked(e -> {
            int hpAntes = partida.getJugador().getVidaActual();
            boolean ok = partida.terminarTurno();
            if (ok) {
                if (partida.getJugador().getVidaActual() < hpAntes) {
                    Jugador j2 = partida.getJugador();
                    iniciarEfectoRecibirAtaque(j2.getFila(), j2.getColumna());
                }
                mostrarFeedback("Turno terminado", Color.LIGHTGREEN);
            } else {
                mostrarFeedback("No puedes terminar el turno ahora", Color.rgb(255, 120, 100));
            }
            actualizar();
        });
        accionesBox.getChildren().add(btnTurno);

        Text btnPuerta = crearBotonTexto("CAMBIAR CUEVA (en PUERTA)");
        btnPuerta.setOnMouseClicked(e -> {
            String siguienteId = partida.getSiguienteCuevaId();
            if (siguienteId != null && alCambiarCueva != null) {
                alCambiarCueva.run();
            } else {
                ejecutarAccion(partida.cambiarCueva(), "No hay puerta accesible");
            }
        });
        accionesBox.getChildren().add(btnPuerta);

        Text btnGuardar = crearBotonTexto("GUARDAR PARTIDA");
        btnGuardar.setOnMouseClicked(e -> {
            try {
                partida.guardar("datos/partida_guardada.json");
                partida.getMensajes().addLast("Partida guardada.");
                mostrarFeedback("Partida guardada", Color.LIGHTGREEN);
            } catch (Exception ex) {
                partida.getMensajes().addLast("Error al guardar: " + ex.getMessage());
                mostrarFeedback("Error al guardar", Color.rgb(255, 120, 100));
            }
            actualizar();
        });
        accionesBox.getChildren().add(btnGuardar);

        Text btnMenu = crearBotonTexto("VOLVER AL MENU");
        btnMenu.setOnMouseClicked(e -> volverAlMenu.run());
        accionesBox.getChildren().add(btnMenu);

        // Atajos de teclado adicionales
        // (Ya estan los WASD/flechas en la Scene)

        // Log
        logBox.getChildren().clear();
        ListaSE<String> msgs = partida.getMensajes();
        int inicio = Math.max(0, msgs.getSize() - 6);
        for (int i = inicio; i < msgs.getSize(); i++) {
            Text msg = new Text("> " + msgs.get(i));
            msg.setFont(Font.font("Monospaced", FontWeight.NORMAL, 11));
            msg.setFill(Color.rgb(180, 220, 180));
            logBox.getChildren().add(msg);
        }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ---------------------------------------------------------------
    // Helpers de UI
    // ---------------------------------------------------------------

    private void ejecutarAccion(boolean ok, String mensajeError) {
        if (!ok) {
            mostrarFeedback(mensajeError, Color.rgb(255, 120, 100));
        }
        actualizar();
    }

    private void mostrarFeedback(String mensaje, Color color) {
        txtFeedback.setText(mensaje);
        txtFeedback.setFill(color);
        feedbackPane.setVisible(true);
        if (feedbackTimer != null) {
            feedbackTimer.stop();
        }
        feedbackTimer = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            feedbackPane.setVisible(false);
        }));
        feedbackTimer.setCycleCount(1);
        feedbackTimer.play();
    }

    private Text labelStats(String nombre) {
        Text t = new Text(nombre + ": ");
        t.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        t.setFill(Color.WHITE);
        return t;
    }

    private Text crearBotonTexto(String texto) {
        Text t = new Text(texto);
        t.setFont(Font.font(FONT, FontWeight.BOLD, 12));
        t.setFill(Color.web("#C89D65"));
        t.setCursor(Cursor.HAND);
        t.setOnMouseEntered(e -> t.setFill(Color.web("#FFD700")));
        t.setOnMouseExited(e -> t.setFill(Color.web("#C89D65")));
        return t;
    }

    private void agregarBotonAccion(String texto, Runnable accion) {
        Text t = crearBotonTexto(texto);
        t.setOnMouseClicked(e -> accion.run());
        accionesBox.getChildren().add(t);
    }

    /**
     * Devuelve el emoji correspondiente a un tipo de objeto.
     */
    private String emojiParaObjeto(Objeto obj) {
        if (obj instanceof modelo.objetos.Pocion) return "\uD83E\uDDEA";
        if (obj instanceof modelo.objetos.Llave) return "\uD83D\uDD11";
        if (obj instanceof modelo.objetos.Escudo) return "\uD83D\uDEE1\uFE0F";
        if (obj instanceof modelo.objetos.Arco) return "\uD83C\uDFF9";
        if (obj instanceof modelo.objetos.Arma) return "\uD83D\uDDE1\uFE0F";
        return "\uD83D\uDC8E";
    }

    /**
     * Crea un Text con emoji centrado para meter en una celda.
     */
    private Text crearEmoji(String emoji, double tamanio) {
        Text t = new Text(emoji);
        t.setFont(Font.font("Segoe UI Emoji", FontWeight.NORMAL, tamanio));
        return t;
    }

    private void iniciarEfectoAtaque() {
        if (ataqueTimer != null) {
            ataqueTimer.stop();
        }
        ataqueTimer = new Timeline(new KeyFrame(Duration.millis(300), ev -> {
            ataqueFila = ataqueCol = -1;
            actualizar();
        }));
        ataqueTimer.setCycleCount(1);
        ataqueTimer.play();
    }

    private void iniciarEfectoRecibirAtaque(int fila, int col) {
        recibirAtaqueFila = fila;
        recibirAtaqueCol = col;
        if (recibirAtaqueTimer != null) {
            recibirAtaqueTimer.stop();
        }
        recibirAtaqueTimer = new Timeline(new KeyFrame(Duration.millis(400), ev -> {
            recibirAtaqueFila = recibirAtaqueCol = -1;
            actualizar();
        }));
        recibirAtaqueTimer.setCycleCount(1);
        recibirAtaqueTimer.play();
    }

    private void agregarBarraVida(StackPane cell, int vidaActual, int vidaMaxima, double cellSize) {
        double anchoBarra = cellSize * 0.8;
        double altoBarra = 5;
        double yBarra = cellSize * 0.15;

        Rectangle fondo = new Rectangle(anchoBarra, altoBarra);
        fondo.setFill(Color.rgb(40, 40, 40));
        fondo.setStroke(Color.rgb(80, 80, 80));
        fondo.setStrokeWidth(0.5);
        StackPane.setAlignment(fondo, Pos.BOTTOM_CENTER);
        StackPane.setMargin(fondo, new Insets(0, 0, yBarra, 0));

        double pct = Math.max(0, (double) vidaActual / vidaMaxima);
        Rectangle relleno = new Rectangle(anchoBarra * pct, altoBarra);
        Color colorBarra = pct > 0.5 ? Color.LIGHTGREEN : (pct > 0.25 ? Color.ORANGE : Color.RED);
        relleno.setFill(colorBarra);
        StackPane.setAlignment(relleno, Pos.BOTTOM_LEFT);
        StackPane.setMargin(relleno, new Insets(0, 0, yBarra, cellSize * 0.1));

        Text hpText = new Text(vidaActual + "/" + vidaMaxima);
        hpText.setFont(Font.font("Monospaced", FontWeight.BOLD, 9));
        hpText.setFill(Color.WHITE);
        StackPane.setAlignment(hpText, Pos.BOTTOM_CENTER);
        StackPane.setMargin(hpText, new Insets(0, 0, yBarra - 10, 0));

        cell.getChildren().addAll(fondo, relleno, hpText);
    }

    /**
     * Mapa de TipoCelda a colores para el grid.
     * Los muros usan el color tematico de la cueva actual.
     */
    private Color colorParaTipo(TipoCelda tipo) {
        // Para los muros usar el color tematico de la cueva actual
        if (tipo == TipoCelda.MURO) {
            CuevaEnMapa cueva = partida.getCuevaActual();
            if (cueva != null) {
                return DatosTemaCueva.paraCuevaId(cueva.getId()).getColorMuro();
            }
            return Color.rgb(70, 70, 70);
        }
        switch (tipo) {
            case SUELO:  return Color.rgb(160, 130, 90);
            case INICIO: return Color.rgb(60, 160, 60);
            case PUERTA: return Color.rgb(200, 170, 40);
            case TESORO: return Color.rgb(140, 60, 180);
            case SALIDA: return Color.rgb(200, 50, 50);
            case TRAMPA: return Color.rgb(220, 120, 40);
            default:     return Color.rgb(100, 100, 100);
        }
    }
}
