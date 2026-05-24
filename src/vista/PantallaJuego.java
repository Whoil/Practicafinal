package vista;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import java.io.File;
import javafx.application.Platform;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import Estructuras.Cola;
import Estructuras.ListaDE;
import Estructuras.ListaSE;
import Estructuras.MiIterador;
import modelo.juego.CeldaEnMapa;
import modelo.juego.CuevaEnMapa;
import modelo.juego.DisparoEnemigo;
import modelo.juego.ObjetoEnMapa;
import modelo.juego.Partida;
import modelo.juego.PersonajeEnMapa;
import modelo.juego.ResultadoImpactoBolaFuego;
import modelo.juego.ResultadoImpactoBolaHielo;
import modelo.mapa.TipoCelda;
import modelo.objetos.Arma;
import modelo.objetos.Escudo;
import modelo.objetos.Objeto;
import modelo.objetos.Pocion;
import modelo.personajes.Enemigo;
import modelo.personajes.Jugador;
import modelo.personajes.TipoEnemigo;
import modelo.juego.ConstantesJuego;
import control.JuegoController;

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
    private Pane gridCeldas;
    private Pane gridWalls;
    private Pane gridOverlay;
    private StackPane[][] celdas;
    private Text txtVida, txtAtaque, txtDefensa, txtTurnos, txtCueva, txtEstado, txtFase;
    private Text txtDistPuerta, txtDistSalida;
    private GridPane inventarioGrid;
    private StackPane[] inventarioSlots;
    private StackPane slotArma, slotEscudo;
    private VBox logContainer;
    private ScrollPane logScrollStyled;
    private int ultimosMensajesLog;
    private Text txtCuevaNombre;
    private Text btnArriba, btnAbajo, btnIzq, btnDer, btnAtacar;

    // Acciones desactivables
    private VBox accionesBox;

    // Feedback visual
    private Text txtFeedback;
    private StackPane feedbackPane;
    private Timeline feedbackTimer;

    // Ayuda overlay
    private StackPane ayudaPane;
    private boolean ayudaVisible = false;

    // Menu de pausa overlay
    private StackPane pausaPane;
    private VBox pausaContenido;
    private boolean pausaVisible = false;

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

    // Animacion de movimiento suave: sprite reutilizado y timeline
    private ImageView jugadorSprite;
    private Timeline animMovimientoTimeline;

    // Tamano del sprite del jugador (cache para animaciones)
    private double spriteSizeCache;

    // Capa separada para animaciones persistentes (no se limpia en actualizar)
    private Pane animOverlay;

    // Enemigo capturado antes de atacar (para animacion de muerte post-ataque)
    private Enemigo targetAntesAtaque;

    private final JuegoController controlador;

    /**
     * Entrada del cache de imagenes usando ListaDE propia.
     */
    private static class EntradaImagen {
        private final String clave;
        private final Image valor;
        EntradaImagen(String c, Image v) { this.clave = c; this.valor = v; }
        boolean coincide(String c) { return clave.equals(c); }
        Image getValor() { return valor; }
    }

    // Cache de imagenes para los assets del Dungeon Asset Pack
    private static final ListaDE<EntradaImagen> IMAGE_CACHE = new ListaDE<>();
    private static final String ASSETS_BASE = "Dungeon Asset Pack" + File.separator;

    // Sistema de vision limitada (fog-of-war)
    private int[][] visibilidad;
    private Pane gridFog;
    private double[] cumX, cumY, colWidth, rowHeight;

    public PantallaJuego(Partida partida, Stage stage, Runnable volverAlMenu) {
        this.partida = partida;
        this.stage = stage;
        this.volverAlMenu = volverAlMenu;
        this.controlador = new JuegoController(this, partida);
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

    public Runnable getAlCambiarCueva() {
        return alCambiarCueva;
    }

    public boolean isPausaVisible() {
        return pausaVisible;
    }

    public int getAtaqueFila() {
        return ataqueFila;
    }

    public void setAtaqueFila(int fila) {
        this.ataqueFila = fila;
    }

    public int getAtaqueCol() {
        return ataqueCol;
    }

    public void setAtaqueCol(int col) {
        this.ataqueCol = col;
    }

    public Enemigo getTargetAntesAtaque() {
        return targetAntesAtaque;
    }

    public void setTargetAntesAtaque(Enemigo target) {
        this.targetAntesAtaque = target;
    }

    /**
     * Construye y devuelve la Scene de juego.
     */
    public Scene crearScene() {
        StackPane rootStack = new StackPane();
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1A1A1A;");
        rootStack.getChildren().add(root);

        // Layout horizontal: grid (izquierda) + panel derecho
        HBox centro = new HBox(10);
        centro.setPadding(new Insets(10));

        // --- Grid de la cueva ---
        VBox gridConLog = new VBox(5);
        gridLayer = new StackPane();
        gridCeldas = new Pane();
        gridWalls = new Pane();
        gridWalls.setMouseTransparent(true);
        gridFog = new Pane();
        gridFog.setMouseTransparent(true);
        gridOverlay = new Pane();
        gridOverlay.setMouseTransparent(true);
        animOverlay = new Pane();
        animOverlay.setMouseTransparent(true);
        gridLayer.getChildren().addAll(gridCeldas, gridWalls, gridFog, gridOverlay, animOverlay);

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

        // --- Log estilizado (debajo del grid) ---
        logContainer = new VBox(2);
        logContainer.setStyle("-fx-padding: 4;");
        logScrollStyled = new ScrollPane(logContainer);
        logScrollStyled.setPrefHeight(140);
        logScrollStyled.setMaxHeight(140);
        logScrollStyled.setFitToWidth(true);
        logScrollStyled.setStyle("-fx-background: transparent; -fx-background-color: #1a1410; -fx-border-color: #4a3b32; -fx-border-width: 2;");
        logScrollStyled.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        logScrollStyled.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        ultimosMensajesLog = 0;
        gridConLog.getChildren().add(logScrollStyled);

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
        txtFase = labelStats("Fase");
        txtDistPuerta = labelStats("Puerta");
        txtDistSalida = labelStats("Salida");
        statsBox.getChildren().addAll(tituloStats, txtVida, txtAtaque, txtDefensa, txtTurnos, txtCueva, txtEstado, txtFase, txtDistPuerta, txtDistSalida);
        panelDer.getChildren().add(statsBox);

        // Inventario (ranuras de equipo + grid)
        panelDer.getChildren().add(construirInventarioVisual());

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

        // Teclado — delegado al controlador
        Scene scene = new Scene(rootStack, ANCHO, ALTO);
        scene.setOnKeyPressed(controlador::handleKeyPressed);
        scene.setOnKeyReleased(controlador::handleKeyReleased);

        // Que el clic en cualquier parte devuelva el foco al root (teclado)
        root.setFocusTraversable(true);
        root.setOnMouseClicked(e -> root.requestFocus());

        // --- Panel de ayuda (superpuesto, oculto inicialmente) ---
        VBox ayudaContent = new VBox(8);
        ayudaContent.setAlignment(Pos.TOP_CENTER);
        ayudaContent.setPadding(new Insets(20));
        ayudaContent.setStyle("-fx-background-color: rgba(0,0,0,0.85); -fx-border-color: #C89D65; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        ayudaContent.setMaxWidth(500);

        Text tituloAyuda = new Text("CONTROLES");
        tituloAyuda.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        tituloAyuda.setFill(Color.web("#FFD700"));

        Text controlesAyuda = new Text(
            "W / Flecha arriba  - Mover arriba\n" +
            "S / Flecha abajo   - Mover abajo\n" +
            "A / Flecha izq.    - Mover izquierda\n" +
            "D / Flecha der.    - Mover derecha\n" +
            "ESPACIO            - Atacar enemigo adyacente\n" +
            "F + Flecha         - Bola de Fuego\n" +
            "C + Flecha         - Bola de Hielo\n" +
            "R                  - Recoger objeto / abrir cofre\n" +
            "T                  - Terminar turno\n" +
            "H                  - Mostrar / ocultar esta ayuda\n\n" +
            "Click en celda     - Moverse a esa celda\n" +
            "Click en objeto    - Usar / equipar desde inventario\n\n" +
            "Estructura del juego:\n" +
            "  3 cuevas conectadas por puertas.\n" +
            "  Derrota al boss en la ultima cueva para\n" +
            "  obtener la llave final.\n" +
            "  Luego pisa la SALIDA para ganar."
        );
        controlesAyuda.setFont(Font.font(FONT, FontWeight.NORMAL, 14));
        controlesAyuda.setFill(Color.WHITE);
        controlesAyuda.setLineSpacing(3);

        Text cerrarAyuda = crearBotonTexto("CERRAR [H]");
        cerrarAyuda.setOnMouseClicked(e -> toggleAyuda());

        ayudaContent.getChildren().addAll(tituloAyuda, controlesAyuda, cerrarAyuda);
        ayudaPane = new StackPane(ayudaContent);
        ayudaPane.setAlignment(Pos.CENTER);
        ayudaPane.setVisible(false);
        rootStack.getChildren().add(ayudaPane);

        pausaPane = construirMenuPausa();
        pausaPane.setVisible(false);
        rootStack.getChildren().add(pausaPane);

        // Construir grid inicial y paneles
        try {
            construirGrid();
            actualizar();
        } catch (Throwable e) {
            logError("Error en crearScene: " + e.getClass().getName() + " - " + e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                logError("  at " + ste.toString());
            }
            e.printStackTrace();
        }
        root.requestFocus();

        return scene;
    }

    // ---------------------------------------------------------------
    // Construccion del grid de celdas
    // ---------------------------------------------------------------

    private void construirGrid() {
        gridCeldas.getChildren().clear();
        gridWalls.getChildren().clear();
        if (gridFog != null) gridFog.getChildren().clear();

        CuevaEnMapa cueva = partida.getCuevaActual();
        if (cueva == null) return;

        int filas = cueva.getFilas();
        int cols = cueva.getColumnas();
        double cellSize = Math.min(600.0 / cols, 600.0 / filas);
        cellSize = Math.min(cellSize, 80);
        double wallThickness = 10;
        Color colorMuro = DatosTemaCueva.paraCuevaId(cueva.getId()).getColorMuro();

        // Calcular anchos de columna: solo MURO -> wallThickness, else cellSize
        colWidth = new double[cols];
        for (int c = 0; c < cols; c++) {
            boolean soloMuro = true;
            for (int f = 0; f < filas; f++) {
                if (cueva.getCelda(f, c).getTipo() != TipoCelda.MURO) {
                    soloMuro = false;
                    break;
                }
            }
            colWidth[c] = soloMuro ? wallThickness : cellSize;
        }

        // Calcular altos de fila: solo MURO -> wallThickness, else cellSize
        rowHeight = new double[filas];
        for (int f = 0; f < filas; f++) {
            boolean soloMuro = true;
            for (int c = 0; c < cols; c++) {
                if (cueva.getCelda(f, c).getTipo() != TipoCelda.MURO) {
                    soloMuro = false;
                    break;
                }
            }
            rowHeight[f] = soloMuro ? wallThickness : cellSize;
        }

        // Posiciones acumuladas
        cumX = new double[cols];
        for (int c = 1; c < cols; c++) {
            cumX[c] = cumX[c-1] + colWidth[c-1];
        }
        cumY = new double[filas];
        for (int f = 1; f < filas; f++) {
            cumY[f] = cumY[f-1] + rowHeight[f-1];
        }

        double totalWidth = cumX[cols-1] + colWidth[cols-1];
        double totalHeight = cumY[filas-1] + rowHeight[filas-1];

        celdas = new StackPane[filas][cols];

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                CeldaEnMapa celda = cueva.getCelda(f, c);
                double px = cumX[c];
                double py = cumY[f];
                double w = colWidth[c];
                double h = rowHeight[f];

                // Fondo solido para toda celda (muro o no)
                Rectangle fondo = new Rectangle(w, h);
                if (celda.getTipo() == TipoCelda.MURO) {
                    fondo.setFill(colorMuro);
                } else {
                    fondo.setFill(colorParaTipo(celda.getTipo()));
                    fondo.setStroke(Color.rgb(60, 60, 60));
                    fondo.setStrokeWidth(0.5);
                }
                fondo.setLayoutX(px);
                fondo.setLayoutY(py);
                gridCeldas.getChildren().add(fondo);

                if (celda.getTipo() != TipoCelda.MURO) {
                    // StackPane para entidades y clicks sobre suelos transitables
                    StackPane cellPane = new StackPane();
                    cellPane.setPrefSize(w, h);
                    cellPane.setLayoutX(px);
                    cellPane.setLayoutY(py);
                    celdas[f][c] = cellPane;
                    // Click para moverse — delegado al controlador
                    final int ff = f, cc = c;
                    cellPane.setOnMouseClicked(e -> controlador.handleCellClick(ff, cc, pausaVisible));
                    cellPane.setCursor(Cursor.HAND);
                    gridCeldas.getChildren().add(cellPane);
                }
            }
        }

        gridOverlay.setPrefSize(totalWidth, totalHeight);
        gridCeldas.setPrefSize(totalWidth, totalHeight);
        gridWalls.setPrefSize(0, 0);
        gridFog.setPrefSize(totalWidth, totalHeight);
        StackPane.setAlignment(gridCeldas, Pos.TOP_LEFT);
        StackPane.setAlignment(gridWalls, Pos.TOP_LEFT);
        StackPane.setAlignment(gridFog, Pos.TOP_LEFT);
        StackPane.setAlignment(gridOverlay, Pos.TOP_LEFT);
    }

    // ---------------------------------------------------------------
    // Actualizacion (redibujar cada vez que cambia el estado)
    // ---------------------------------------------------------------

    private void logError(String msg) {
        try {
            java.io.FileWriter fw = new java.io.FileWriter("crash_detail.log", true);
            fw.write(java.time.LocalDateTime.now() + " " + msg + "\n");
            fw.close();
        } catch (Exception e2) {
            System.err.println("[logError] No se pudo escribir en crash_detail.log: " + e2.getMessage());
        }
    }

    public void actualizar() {
        try {
            logError("actualizar() inicio");
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

            // Limpiar iconos anteriores de las celdas
            if (celdas != null) {
                for (int f = 0; f < filas && f < celdas.length; f++) {
                    if (celdas[f] == null) continue;
                    for (int c = 0; c < cols && c < celdas[f].length; c++) {
                        StackPane cell = celdas[f][c];
                        if (cell != null) {
                            cell.getChildren().clear();
                        }
                    }
                }
            }

            // Recalcular visibilidad (fog-of-war) desde la posicion del jugador
            recalcularVisibilidad();

        // Obtener tema visual segun la cueva actual
        DatosTemaCueva tema = DatosTemaCueva.paraCuevaId(cuevaIdActual);
        this.spriteSizeCache = Math.min(cellSize * 0.65, 40);
        double spriteSize = this.spriteSizeCache;

        // Iconos decorativos de celdas especiales.
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                if (celdas[f][c] == null) continue;
                TipoCelda tipo = cueva.getCelda(f, c).getTipo();
                Node iconoCelda = crearIconoCeldaEspecial(tipo, spriteSize);
                if (iconoCelda != null) {
                    celdas[f][c].getChildren().add(iconoCelda);
                }
            }
        }

        // Jugador (icono del asset pack: knight)
        Jugador j = partida.getJugador();
        this.jugadorSprite = crearSpriteAssets("characters" + File.separator + "Spritesheets" + File.separator + "knight_idle.png", spriteSize);
        if (j.getFila() >= 0 && j.getFila() < filas && j.getColumna() >= 0 && j.getColumna() < cols) {
            celdas[j.getFila()][j.getColumna()].getChildren().add(jugadorSprite);
        }

        // Enemigos (icono del asset pack segun tematica y tipo)
        ListaDE<Enemigo> enemigos = partida.getEnemigosActuales();
        MiIterador<Enemigo> itE = enemigos.getIterador();
        while (itE.hasNext()) {
            Enemigo e = itE.next();
            boolean esBoss = e instanceof modelo.personajes.Boss;
            String asset = getEnemyAssetPath(e);
            ImageView enemyIcon = crearSpriteAssets(asset, spriteSize);
            if (esBoss) {
                DropShadow sombraBoss = new DropShadow();
                sombraBoss.setRadius(10);
                sombraBoss.setColor(Color.rgb(200, 50, 50, 0.5));
                enemyIcon.setEffect(sombraBoss);
            } else if (e.getTipoEnemigo() == TipoEnemigo.ARQUERO) {
                DropShadow sombraArquero = new DropShadow();
                sombraArquero.setRadius(8);
                sombraArquero.setColor(Color.rgb(80, 220, 140, 0.65));
                enemyIcon.setEffect(sombraArquero);
            }
            if (e.getFila() >= 0 && e.getFila() < filas && e.getColumna() >= 0 && e.getColumna() < cols) {
                celdas[e.getFila()][e.getColumna()].getChildren().add(enemyIcon);
            }
        }
        agregarResaltadoEnemigosAdyacentes(filas, cols, cellSize);
        agregarFlashCelda(ataqueFila, ataqueCol, filas, cols, cellSize, Color.rgb(255, 220, 120, 0.32), Color.rgb(255, 230, 160, 0.95));
        agregarFlashCelda(recibirAtaqueFila, recibirAtaqueCol, filas, cols, cellSize, Color.rgb(255, 80, 80, 0.30), Color.rgb(255, 80, 80, 0.90));

        // Objetos en el mapa (icono del asset pack segun tipo)
        ListaDE<ObjetoEnMapa> objetos = partida.getObjetosActuales();
        MiIterador<ObjetoEnMapa> itO = objetos.getIterador();
        while (itO.hasNext()) {
            ObjetoEnMapa om = itO.next();
            Node itemIcon = crearIconoObjeto(om.getObjeto(), spriteSize * 0.6);
            if (om.getFila() >= 0 && om.getFila() < filas && om.getColumna() >= 0 && om.getColumna() < cols) {
                celdas[om.getFila()][om.getColumna()].getChildren().add(itemIcon);
            }
        }

        // Obstaculos (roca, arbusto) — icono del asset pack sobre la celda
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                if (celdas[f][c] == null) continue;
                TipoCelda tc = cueva.getCelda(f, c).getTipo();
                if (tc == TipoCelda.ROCA) {
                    ImageView obsIcon = crearSpriteArchivo("Dungeon Asset Pack" + File.separator + "rocks.png", spriteSize * 0.7, false);
                    celdas[f][c].getChildren().add(obsIcon);
                } else if (tc == TipoCelda.ARBUSTO) {
                    ImageView obsIcon = crearSpriteArchivo("Dungeon Asset Pack" + File.separator + "bush.png", spriteSize * 0.7, false);
                    celdas[f][c].getChildren().add(obsIcon);
                }
            }
        }

        // Niebla de guerra: oculta celdas fuera del radio de vision
        actualizarFog();

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

        // Path overlay (C-10): dibujar el camino comprado sobre el grid
        if (partida.isVisionCaminoComprada() && cumX != null && colWidth != null && rowHeight != null) {
            ListaSE<CeldaEnMapa> camino = partida.getCaminoComprado();
            if (camino != null) {
                for (int i = 0; i < camino.getSize(); i++) {
                    CeldaEnMapa celdaCamino = camino.get(i);
                    int cf = celdaCamino.getFila();
                    int cc = celdaCamino.getColumna();
                    if (cf >= 0 && cf < cumY.length && cc >= 0 && cc < cumX.length) {
                        boolean esDestino = (i == camino.getSize() - 1);
                        Rectangle pathRect = new Rectangle(colWidth[cc], rowHeight[cf]);
                        if (esDestino) {
                            pathRect.setFill(Color.rgb(255, 215, 0, 0.45));
                            pathRect.setStroke(Color.rgb(255, 215, 0, 1.0));
                            pathRect.setStrokeWidth(3);
                        } else {
                            pathRect.setFill(Color.rgb(100, 200, 255, 0.30));
                            pathRect.setStroke(Color.rgb(100, 200, 255, 0.7));
                            pathRect.setStrokeWidth(1.5);
                        }
                        pathRect.setLayoutX(cumX[cc]);
                        pathRect.setLayoutY(cumY[cf]);
                        pathRect.setMouseTransparent(true);
                        gridOverlay.getChildren().add(pathRect);
                    }
                }
            }
        }

        // Stats
        txtVida.setText("Vida: " + j.getVidaActual() + "/" + j.getVidaMaxima());
        txtAtaque.setText("Ataque: " + j.getAtaqueTotal() + (j.getArmaEquipada() != null ? " (" + j.getArmaEquipada().getNombre() + ")" : ""));
        txtDefensa.setText("Defensa: " + j.getDefensaTotal() + (j.getEscudoEquipado() != null ? " (" + j.getEscudoEquipado().getNombre() + ")" : ""));
        txtTurnos.setText("Turnos restantes: " + partida.getTurnosRestantes());
        txtCueva.setText("Cueva: " + cueva.getId());
        txtEstado.setText("Estado: " + partida.getEstado());
        int distPuerta = partida.getDistanciaAPuerta();
        txtDistPuerta.setText("Puerta: " + (distPuerta >= 0 ? distPuerta + " pasos" : "N/A"));
        int distSalida = partida.getDistanciaMinimaCuevasASalida();
        String rutaSalida;
        if (distSalida < 0) {
            rutaSalida = "N/A";
        } else if (distSalida == 0) {
            rutaSalida = "AQUI";
        } else if (partida.isVisionCaminoComprada()) {
            ListaSE<String> cuevas = partida.getCaminoCompradoCuevas();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cuevas.getSize(); i++) {
                if (sb.length() > 0) sb.append(" > ");
                sb.append(cuevas.get(i));
            }
            rutaSalida = sb.toString();
        } else {
            rutaSalida = distSalida + " cuevas";
        }
        txtDistSalida.setText("Salida: " + rutaSalida);
        actualizarFase();
        txtCuevaNombre.setText("CUEVA: " + cueva.getId().toUpperCase());

        // Inventario visual
        actualizarInventarioVisual();

        // Acciones
        accionesBox.getChildren().clear();
        btnArriba = crearBotonTexto("ARRIBA [W]");
        btnArriba.setOnMouseClicked(e -> {
            int pf = partida.getJugador().getFila(), pc = partida.getJugador().getColumna();
            if (esTesoro(partida.getCuevaActual().getCelda(pf - 1, pc))) { ejecutarAccion(false, null); return; }
            int oldF = pf, oldC = pc;
            if (esObstaculo(partida.getCuevaActual().getCelda(pf - 1, pc))) { ejecutarAccion(false, "Hay una pared"); return; }
            boolean movOk = partida.moverJugadorArriba();
            ejecutarAccion(movOk, "No puedes moverte mas este turno");
            if (movOk && partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO) {
                animarMovimiento(oldF, oldC);
            }
        });
        accionesBox.getChildren().add(btnArriba);
        HBox movHoriz = new HBox(4);
        movHoriz.setAlignment(Pos.CENTER);
        btnIzq = crearBotonTexto("< IZQ [A]");
        btnIzq.setOnMouseClicked(e -> {
            int pf = partida.getJugador().getFila(), pc = partida.getJugador().getColumna();
            if (esTesoro(partida.getCuevaActual().getCelda(pf, pc - 1))) { ejecutarAccion(false, null); return; }
            int oldF = pf, oldC = pc;
            if (esObstaculo(partida.getCuevaActual().getCelda(pf, pc - 1))) { ejecutarAccion(false, "Hay una pared"); return; }
            boolean movOk = partida.moverJugadorIzquierda();
            ejecutarAccion(movOk, "No puedes moverte mas este turno");
            if (movOk && partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO) {
                animarMovimiento(oldF, oldC);
            }
        });
        btnDer = crearBotonTexto("DER [D] >");
        btnDer.setOnMouseClicked(e -> {
            int pf = partida.getJugador().getFila(), pc = partida.getJugador().getColumna();
            if (esTesoro(partida.getCuevaActual().getCelda(pf, pc + 1))) { ejecutarAccion(false, null); return; }
            int oldF = pf, oldC = pc;
            if (esObstaculo(partida.getCuevaActual().getCelda(pf, pc + 1))) { ejecutarAccion(false, "Hay una pared"); return; }
            boolean movOk = partida.moverJugadorDerecha();
            ejecutarAccion(movOk, "No puedes moverte mas este turno");
            if (movOk && partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO) {
                animarMovimiento(oldF, oldC);
            }
        });
        movHoriz.getChildren().addAll(btnIzq, btnDer);
        accionesBox.getChildren().add(movHoriz);
        btnAbajo = crearBotonTexto("ABAJO [S]");
        btnAbajo.setOnMouseClicked(e -> {
            int pf = partida.getJugador().getFila(), pc = partida.getJugador().getColumna();
            if (esTesoro(partida.getCuevaActual().getCelda(pf + 1, pc))) { ejecutarAccion(false, null); return; }
            int oldF = pf, oldC = pc;
            if (esObstaculo(partida.getCuevaActual().getCelda(pf + 1, pc))) { ejecutarAccion(false, "Hay una pared"); return; }
            boolean movOk = partida.moverJugadorAbajo();
            ejecutarAccion(movOk, "No puedes moverte mas este turno");
            if (movOk && partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO) {
                animarMovimiento(oldF, oldC);
            }
        });
        accionesBox.getChildren().add(btnAbajo);

        btnAtacar = crearBotonTexto(partida.isAccionRealizada()
                ? "ATACAR [ACCION USADA]"
                : "ATACAR [ESPACIO]");
        btnAtacar.setOnMouseClicked(e -> {
            if (partida.isAccionRealizada()) {
                return;
            }
            Enemigo targetBtn = partida.getEnemigoAdyacente();
            if (targetBtn != null) {
                targetAntesAtaque = targetBtn;
                ataqueFila = targetBtn.getFila();
                ataqueCol = targetBtn.getColumna();
            }
            boolean okAtq = partida.atacar();
            actualizar();
            if (okAtq && ataqueFila >= 0 && partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO) {
                int pfj = partida.getJugador().getFila();
                int pcj = partida.getJugador().getColumna();
                animarProyectil(pfj, pcj, ataqueFila, ataqueCol);
                animarAtaque(ataqueFila, ataqueCol);
                if (targetAntesAtaque != null && !targetAntesAtaque.estaVivo()) {
                    animarMuerteEnemigo(ataqueFila, ataqueCol, getEnemyAssetPath(targetAntesAtaque));
                }
                targetAntesAtaque = null;
            }
            ejecutarAccion(okAtq, "No hay enemigo para atacar");
        });
        accionesBox.getChildren().add(btnAtacar);

        Text btnRecoger = crearBotonTexto(partida.isAccionRealizada()
                ? "RECOGER OBJETO [ACCION USADA]"
                : partida.hayTesoroCercano() ? "ABRIR COFRE [R]" : "RECOGER OBJETO [R]");
        btnRecoger.setOnMouseClicked(e -> {
            if (partida.isAccionRealizada()) {
                return;
            }
            boolean ok = partida.recogerObjeto();
            if (!ok && partida.hayTesoroCercano()) {
                ok = partida.abrirTesoro();
            }
            if (ok) ReproductorSfx.getInstancia().reproducirRecoger();
            ejecutarAccion(ok, "No hay objeto que recoger aqui");
        });
        aplicarEstiloBoton(btnRecoger, !partida.isAccionRealizada());
        accionesBox.getChildren().add(btnRecoger);

        // Boton Ver Camino (C-10)
        boolean yaComprado = partida.isVisionCaminoComprada();
        Text btnVerCamino = crearBotonTexto(yaComprado ? "CAMINO REVELADO ✓" : "VER CAMINO (5 TURNOS)");
        if (yaComprado) {
            btnVerCamino.setFill(Color.LIGHTGREEN);
        }
        btnVerCamino.setOnMouseClicked(e -> {
            if (partida.isVisionCaminoComprada()) {
                mostrarFeedback("Ya has comprado la vision del camino", Color.web("#C89D65"));
                return;
            }
            if (partida.getTurnosRestantes() < 5) {
                mostrarFeedback("No tienes suficientes turnos (necesitas 5)", Color.rgb(255, 120, 100));
                return;
            }
            boolean ok = partida.comprarVisionCamino();
            if (ok) {
                ReproductorSfx.getInstancia().reproducirRecoger();
                mostrarFeedback("Camino revelado por 5 turnos", Color.LIGHTGREEN);
            } else {
                mostrarFeedback("No se pudo comprar la vision", Color.rgb(255, 120, 100));
            }
            actualizar();
        });
        aplicarEstiloBoton(btnVerCamino, !yaComprado);
        accionesBox.getChildren().add(btnVerCamino);

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
                    ReproductorSfx.getInstancia().reproducirDano();
                    iniciarEfectoRecibirAtaque(j2.getFila(), j2.getColumna());
                }
            } else {
                mostrarFeedback("No puedes terminar el turno ahora", Color.rgb(255, 120, 100));
            }
            actualizar();
        });
        accionesBox.getChildren().add(btnTurno);

        Text btnPuerta = crearBotonTexto("CAMBIAR CUEVA (en PUERTA)");
        btnPuerta.setOnMouseClicked(e -> {
            boolean cambioRealizado = partida.cambiarCueva();
            if (cambioRealizado && alCambiarCueva != null) {
                ReproductorSfx.getInstancia().reproducirPuerta();
                alCambiarCueva.run();
            } else {
                ejecutarAccion(false, "Necesitas estar en la puerta y tener su llave");
            }
        });
        accionesBox.getChildren().add(btnPuerta);

        Text btnGuardar = crearBotonTexto("GUARDAR PARTIDA");
        btnGuardar.setOnMouseClicked(e -> guardarPartida());
        accionesBox.getChildren().add(btnGuardar);

        Text btnAyuda = crearBotonTexto("AYUDA [H]");
        btnAyuda.setOnMouseClicked(e -> toggleAyuda());
        accionesBox.getChildren().add(btnAyuda);

        Text btnMenu = crearBotonTexto("VOLVER AL MENU");
        btnMenu.setOnMouseClicked(e -> confirmarVolverAlMenu());
        accionesBox.getChildren().add(btnMenu);
        actualizarBotonesAccion();

        // Atajos de teclado adicionales
        // (Ya estan los WASD/flechas en la Scene)

        // Log estilizado
        actualizarLogEstilizado();
        } catch (Throwable ex) {
            logError("Error en actualizar: " + ex.getClass().getName() + " - " + ex.getMessage());
            for (StackTraceElement ste : ex.getStackTrace()) {
                logError("  at " + ste.toString());
            }
            ex.printStackTrace();
        }
    }

    // ---------------------------------------------------------------
    // Sistema de vision limitada (fog-of-war)
    // ---------------------------------------------------------------

    /**
     * Calcula que celdas son visibles desde el jugador usando BFS.
     * La vision se propaga a traves de celdas transitables (SUELO, PUERTA, etc.)
     * hasta ConstantesJuego.RADIO_VISION de distancia. Muros y obstaculos (ROCA, ARBUSTO)
     * bloquean la propagacion pero son marcados como visibles en el borde.
     */
    private void recalcularVisibilidad() {
        CuevaEnMapa cueva = partida.getCuevaActual();
        if (cueva == null) return;
        int filas = cueva.getFilas();
        int cols = cueva.getColumnas();

        if (visibilidad == null || visibilidad.length != filas || visibilidad[0].length != cols) {
            visibilidad = new int[filas][cols];
        }
        for (int f = 0; f < filas; f++)
            for (int c = 0; c < cols; c++)
                visibilidad[f][c] = -1;

        Jugador j = partida.getJugador();
        int pf = j.getFila();
        int pc = j.getColumna();
        if (pf < 0 || pc < 0) return;

        Cola<int[]> cola = new Cola<>();
        visibilidad[pf][pc] = 0;
        cola.offer(new int[]{pf, pc, 0});

        int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};

        while (!cola.isEmpty()) {
            int[] dato = cola.poll();
            int f = dato[0];
            int c = dato[1];
            int dist = dato[2];

            if (dist >= ConstantesJuego.RADIO_VISION) continue;

            for (int[] d : dirs) {
                int nf = f + d[0];
                int nc = c + d[1];
                if (nf < 0 || nf >= filas || nc < 0 || nc >= cols) continue;
                if (visibilidad[nf][nc] != -1) continue;

                CeldaEnMapa celda = cueva.getCelda(nf, nc);
                TipoCelda tipo = celda.getTipo();
                int nuevaDist = dist + 1;

                visibilidad[nf][nc] = nuevaDist;

                if (tipo != TipoCelda.MURO && tipo != TipoCelda.ROCA && tipo != TipoCelda.ARBUSTO) {
                    if (nuevaDist < ConstantesJuego.RADIO_VISION) {
                        cola.offer(new int[]{nf, nc, nuevaDist});
                    }
                }
            }
        }
    }

    /**
     * Dibuja rectangulos de niebla sobre las celdas no visibles.
     * La opacidad del rectangulo depende de la distancia BFS:
     *   dist 0 (jugador): 0%
     *   dist 1:          15%
     *   dist 2:          40%
     *   dist 3:          75%
     *   -1 (no visible): 100%
     * La niebla se situa encima de gridCeldas (suelos + entidades)
     * y debajo de gridOverlay, tapando automaticamente lo que no
     * debe verse sin necesidad de modificar la logica de entidades.
     */
    private void actualizarFog() {
        gridFog.getChildren().clear();
        if (visibilidad == null || cumX == null) return;

        CuevaEnMapa cueva = partida.getCuevaActual();
        if (cueva == null) return;
        int filas = cueva.getFilas();
        int cols = cueva.getColumnas();

        for (int f = 0; f < filas && f < cumY.length; f++) {
            for (int c = 0; c < cols && c < cumX.length; c++) {
                int dist = visibilidad[f][c];
                double opacidad;
                if (dist < 0 || dist >= ConstantesJuego.OPACIDAD_FOG.length) {
                    opacidad = 1.0;
                } else {
                    opacidad = ConstantesJuego.OPACIDAD_FOG[dist];
                }

                if (opacidad > 0) {
                    Rectangle fogRect = new Rectangle(colWidth[c], rowHeight[f]);
                    fogRect.setFill(Color.rgb(0, 0, 0, opacidad));
                    fogRect.setLayoutX(cumX[c]);
                    fogRect.setLayoutY(cumY[f]);
                    gridFog.getChildren().add(fogRect);
                }
            }
        }
    }

    // ---------------------------------------------------------------
    // Helpers de UI
    // ---------------------------------------------------------------

    public void toggleAyuda() {
        ayudaVisible = !ayudaVisible;
        ayudaPane.setVisible(ayudaVisible);
    }

    private StackPane construirMenuPausa() {
        pausaContenido = new VBox(14);
        pausaContenido.setAlignment(Pos.CENTER);
        pausaContenido.setPadding(new Insets(28));
        pausaContenido.setMaxWidth(360);
        pausaContenido.setStyle("-fx-background-color: rgba(18,14,12,0.94);"
                + "-fx-border-color: #C89D65; -fx-border-width: 2;"
                + "-fx-border-radius: 10; -fx-background-radius: 10;");
        mostrarContenidoPausaPrincipal();

        StackPane overlay = new StackPane(pausaContenido);
        overlay.setAlignment(Pos.CENTER);
        overlay.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.55), CornerRadii.EMPTY, Insets.EMPTY)));
        return overlay;
    }

    private void mostrarContenidoPausaPrincipal() {
        pausaContenido.getChildren().clear();

        Text titulo = new Text("PAUSA");
        titulo.setFont(Font.font(FONT, FontWeight.BOLD, 28));
        titulo.setFill(Color.web("#FFD700"));

        Text continuar = crearBotonTexto("CONTINUAR [P/ESC]");
        continuar.setOnMouseClicked(e -> togglePausa());

        Text guardar = crearBotonTexto("GUARDAR PARTIDA");
        guardar.setOnMouseClicked(e -> guardarPartida());

        Text menu = crearBotonTexto("VOLVER AL MENU");
        menu.setOnMouseClicked(e -> confirmarVolverAlMenu());

        pausaContenido.getChildren().addAll(titulo, continuar, guardar, menu);
    }

    public void togglePausa() {
        pausaVisible = !pausaVisible;
        if (ayudaVisible) {
            ayudaVisible = false;
            ayudaPane.setVisible(false);
        }
        pausaPane.setVisible(pausaVisible);
        ReproductorSfx.getInstancia().reproducirPausa();
    }

    private void guardarPartida() {
        try {
            partida.guardar("datos/partida_guardada.json");
            partida.getMensajes().addLast("Partida guardada.");
            ReproductorSfx.getInstancia().reproducirGuardar();
            mostrarFeedback("Partida guardada", Color.LIGHTGREEN);
        } catch (Exception ex) {
            partida.getMensajes().addLast("Error al guardar: " + ex.getMessage());
            mostrarFeedback("Error al guardar", Color.rgb(255, 120, 100));
        }
        actualizar();
    }

    private void confirmarVolverAlMenu() {
        pausaVisible = true;
        if (ayudaVisible) {
            ayudaVisible = false;
            ayudaPane.setVisible(false);
        }
        pausaPane.setVisible(true);
        pausaContenido.getChildren().clear();
        Text titulo = new Text("VOLVER AL MENU");
        titulo.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        titulo.setFill(Color.web("#FFD700"));

        Text aviso = new Text("Guarda antes si quieres conservar el progreso.");
        aviso.setFont(Font.font(FONT, FontWeight.NORMAL, 13));
        aviso.setFill(Color.WHITE);

        Text volver = crearBotonTexto("CONFIRMAR");
        volver.setOnMouseClicked(e -> volverAlMenu.run());

        Text cancelar = crearBotonTexto("CANCELAR");
        cancelar.setOnMouseClicked(e -> mostrarContenidoPausaPrincipal());

        pausaContenido.getChildren().addAll(titulo, aviso, volver, cancelar);
    }

    /**
     * Comprueba si una celda es un obstaculo que bloquea el paso
     * (MURO, ROCA o ARBUSTO).
     */
    public boolean esObstaculo(CeldaEnMapa celda) {
        TipoCelda t = celda.getTipo();
        return t == TipoCelda.MURO || t == TipoCelda.ROCA || t == TipoCelda.ARBUSTO;
    }

    public boolean esTesoro(CeldaEnMapa celda) {
        return celda != null && celda.getTipo() == TipoCelda.TESORO;
    }

    private void ejecutarAccion(boolean ok, String mensajeError) {
        if (!ok && mensajeError != null) {
            mostrarFeedback(mensajeError, Color.rgb(255, 120, 100));
        }
        actualizar();
    }

    public void mostrarFeedback(String mensaje, Color color) {
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

    private void actualizarFase() {
        boolean mov = partida.isMovimientoRealizado();
        boolean acc = partida.isAccionRealizada();
        modelo.juego.EstadoPartida est = partida.getEstado();
        if (est != modelo.juego.EstadoPartida.EN_CURSO) {
            txtFase.setText("Fase: —");
            txtFase.setFill(Color.GRAY);
        } else if (mov && acc) {
            txtFase.setText("Fase: Listo \u2713\u2713");
            txtFase.setFill(Color.GOLD);
        } else if (mov) {
            txtFase.setText("Fase: Movido \u2713");
            txtFase.setFill(Color.LIGHTGREEN);
        } else if (acc) {
            txtFase.setText("Fase: Atacado \u2713");
            txtFase.setFill(Color.LIGHTGREEN);
        } else {
            txtFase.setText("Fase: Pendiente");
            txtFase.setFill(Color.web("#CCCCCC"));
        }
    }

    private void actualizarBotonesAccion() {
        boolean mov = partida.isMovimientoRealizado();
        boolean acc = partida.isAccionRealizada();
        aplicarEstiloBoton(btnArriba, !mov);
        aplicarEstiloBoton(btnAbajo, !mov);
        aplicarEstiloBoton(btnIzq, !mov);
        aplicarEstiloBoton(btnDer, !mov);
        aplicarEstiloBoton(btnAtacar, !acc);
    }

    private void aplicarEstiloBoton(Text t, boolean habilitado) {
        if (t == null) return;
        if (habilitado) {
            t.setOpacity(1.0);
            t.setCursor(Cursor.HAND);
        } else {
            t.setOpacity(0.35);
            t.setCursor(Cursor.DEFAULT);
        }
    }

    // ---------------------------------------------------------------
    // Inventario visual con ranuras
    // ---------------------------------------------------------------

    private VBox construirInventarioVisual() {
        VBox invBox = new VBox(4);
        invBox.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-padding: 6; -fx-border-color: #555; -fx-border-width: 1;");

        Text tituloInv = new Text("INVENTARIO");
        tituloInv.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        tituloInv.setFill(Color.web("#FFD700"));
        invBox.getChildren().add(tituloInv);

        Text tituloEquipo = new Text("Equipo:");
        tituloEquipo.setFont(Font.font(FONT, FontWeight.NORMAL, 11));
        tituloEquipo.setFill(Color.web("#C89D65"));
        HBox equipoBox = new HBox(8);
        slotArma = crearSlotInventario();
        slotEscudo = crearSlotInventario();
        equipoBox.getChildren().addAll(crearBloqueEquipo("ARMA", slotArma), crearBloqueEquipo("ESCUDO", slotEscudo));
        invBox.getChildren().addAll(tituloEquipo, equipoBox);

        inventarioGrid = new GridPane();
        inventarioGrid.setHgap(4);
        inventarioGrid.setVgap(4);
        inventarioSlots = new StackPane[12];
        for (int i = 0; i < 12; i++) {
            StackPane slot = crearSlotInventario();
            inventarioSlots[i] = slot;
            inventarioGrid.add(slot, i % 4, i / 4);
        }
        invBox.getChildren().add(inventarioGrid);

        return invBox;
    }

    private VBox crearBloqueEquipo(String etiqueta, StackPane slot) {
        VBox bloque = new VBox(3);
        bloque.setAlignment(Pos.CENTER);
        Text texto = new Text(etiqueta);
        texto.setFont(Font.font("Monospaced", FontWeight.BOLD, 9));
        texto.setFill(Color.web("#FFD700"));
        bloque.getChildren().addAll(slot, texto);
        return bloque;
    }

    private StackPane crearSlotInventario() {
        StackPane slot = new StackPane();
        slot.setPrefSize(58, 58);
        slot.setMinSize(58, 58);
        slot.setMaxSize(58, 58);
        aplicarEstiloSlotInventario(slot, false, false);
        return slot;
    }

    private void aplicarEstiloSlotInventario(StackPane slot, boolean equipado, boolean equipoPrincipal) {
        String borde = equipado || equipoPrincipal ? "#FFD700" : "#4a3b32";
        String fondo = equipoPrincipal ? "#201814" : "#2b221e";
        String grosor = equipado || equipoPrincipal ? "4px" : "3px";
        slot.setStyle("-fx-border-color: " + borde + "; -fx-border-width: " + grosor + ";"
                + "-fx-background-color: " + fondo + "; -fx-border-radius: 4px;"
                + "-fx-background-radius: 4px;");
    }

    private void actualizarInventarioVisual() {
        Jugador jug = partida.getJugador();
        ListaDE<Objeto> inv = jug.getInventario();

        for (StackPane slot : inventarioSlots) {
            slot.getChildren().clear();
            aplicarEstiloSlotInventario(slot, false, false);
            slot.setUserData(null);
            slot.setOnMouseClicked(null);
            slot.setCursor(Cursor.DEFAULT);
        }
        aplicarEstiloSlotInventario(slotArma, false, true);
        aplicarEstiloSlotInventario(slotEscudo, false, true);
        mostrarSlotEquipoVacio(slotArma, "ARMA");
        mostrarSlotEquipoVacio(slotEscudo, "ESC");

        MiIterador<Objeto> it = inv.getIterador();
        int idx = 0;
        while (it.hasNext() && idx < 12) {
            Objeto obj = it.next();
            StackPane slot = inventarioSlots[idx];
            slot.getChildren().add(crearIconoObjeto(obj, 42));
            boolean equipado = objetoEstaEquipado(obj, jug);
            if (equipado) {
                aplicarEstiloSlotInventario(slot, true, false);
                Text marca = new Text("EQ");
                marca.setFont(Font.font("Monospaced", FontWeight.BOLD, 10));
                marca.setFill(Color.BLACK);
                StackPane etiqueta = new StackPane(marca);
                etiqueta.setBackground(new Background(new BackgroundFill(
                        Color.web("#FFD700"), new CornerRadii(3), Insets.EMPTY)));
                etiqueta.setPadding(new Insets(1, 3, 1, 3));
                StackPane.setAlignment(etiqueta, Pos.TOP_RIGHT);
                StackPane.setMargin(etiqueta, new Insets(2));
                slot.getChildren().add(etiqueta);
            }
            slot.setUserData(obj);
            final String objId = obj.getId();
            final boolean esConsumible = obj instanceof Pocion;
            final boolean esEquipable = obj.esEquipable();
            if (esConsumible || esEquipable) {
                slot.setCursor(Cursor.HAND);
                slot.setOnMouseClicked(e -> {
                    boolean ok;
                    if (esConsumible) {
                        ok = partida.usarPocion(objId);
                    } else {
                        ok = partida.equiparItem(objId);
                    }
                    if (ok) {
                        if (esConsumible) {
                            ReproductorSfx.getInstancia().reproducirRecoger();
                        } else {
                            ReproductorSfx.getInstancia().reproducirGuardar();
                        }
                        mostrarFeedback(obj.getNombre() + " " + (esConsumible ? "usado" : "equipado"), Color.LIGHTGREEN);
                    } else {
                        mostrarFeedback("No puedes " + (esConsumible ? "usar" : "equipar") + " esto ahora", Color.rgb(255, 120, 100));
                    }
                    actualizar();
                });
            }
            idx++;
        }

        slotArma.getChildren().clear();
        slotEscudo.getChildren().clear();
        Arma arma = jug.getArmaEquipada();
        Escudo escudo = jug.getEscudoEquipado();
        if (arma != null) {
            slotArma.getChildren().clear();
            slotArma.getChildren().add(crearIconoObjeto(arma, 42));
            Text marca = new Text("EQUIPADA");
            marca.setFont(Font.font("Monospaced", FontWeight.BOLD, 8));
            marca.setFill(Color.web("#FFD700"));
            StackPane.setAlignment(marca, Pos.BOTTOM_CENTER);
            StackPane.setMargin(marca, new Insets(0, 0, 2, 0));
            slotArma.getChildren().add(marca);
        } else {
            mostrarSlotEquipoVacio(slotArma, "ARMA");
        }
        if (escudo != null) {
            slotEscudo.getChildren().clear();
            slotEscudo.getChildren().add(crearIconoObjeto(escudo, 42));
            Text marca = new Text("EQUIPADO");
            marca.setFont(Font.font("Monospaced", FontWeight.BOLD, 8));
            marca.setFill(Color.web("#FFD700"));
            StackPane.setAlignment(marca, Pos.BOTTOM_CENTER);
            StackPane.setMargin(marca, new Insets(0, 0, 2, 0));
            slotEscudo.getChildren().add(marca);
        } else {
            mostrarSlotEquipoVacio(slotEscudo, "ESC");
        }
    }

    private void mostrarSlotEquipoVacio(StackPane slot, String texto) {
        slot.getChildren().clear();
        Text marca = new Text(texto);
        marca.setFont(Font.font("Monospaced", FontWeight.BOLD, 11));
        marca.setFill(Color.web("#6f5a42"));
        slot.getChildren().add(marca);
    }

    private boolean objetoEstaEquipado(Objeto obj, Jugador jug) {
        if (obj == null || jug == null) {
            return false;
        }
        Arma arma = jug.getArmaEquipada();
        Escudo escudo = jug.getEscudoEquipado();
        return mismoObjeto(obj, arma) || mismoObjeto(obj, escudo);
    }

    private boolean mismoObjeto(Objeto a, Objeto b) {
        return a != null && b != null && a.getId().equals(b.getId());
    }

    // ---------------------------------------------------------------
    // Log estilizado con colores por tipo de evento
    // ---------------------------------------------------------------

    private void actualizarLogEstilizado() {
        ListaSE<String> msgs = partida.getMensajes();
        int total = msgs.getSize();
        if (total <= ultimosMensajesLog) return;

        for (int i = ultimosMensajesLog; i < total; i++) {
            String texto = msgs.get(i);
            String tipo = clasificarMensaje(texto);
            TextFlow linea = new TextFlow();
            Text txt = new Text("> " + texto + "\n");
            txt.setFont(Font.font("Monospaced", FontWeight.NORMAL, 11));
            switch (tipo) {
                case "JUGADOR": txt.setFill(Color.web("#64B5F6")); break;
                case "ENEMIGO": txt.setFill(Color.web("#EF5350")); break;
                case "OBJETO":  txt.setFill(Color.web("#FFD700")); break;
                case "FUEGO":   txt.setFill(Color.web("#FF9800")); break;
                case "HIELO":   txt.setFill(Color.web("#66D9EF")); break;
                default:        txt.setFill(Color.web("#CCCCCC")); break;
            }
            linea.getChildren().add(txt);
            logContainer.getChildren().add(linea);
        }
        ultimosMensajesLog = total;

        Platform.runLater(() -> logScrollStyled.setVvalue(1.0));
    }

    private String clasificarMensaje(String msg) {
        if (msg == null) return "SISTEMA";
        String m = msg.toLowerCase();
        if (m.contains("bola de fuego")) {
            return "FUEGO";
        }
        if (m.contains("bola de hielo") || m.contains("congelado") || m.contains("congela")) {
            return "HIELO";
        }
        if (m.contains("ataca al jugador") || m.contains("se acerca al jugador")
                || m.contains("dispara al jugador")) {
            return "ENEMIGO";
        }
        if (m.contains("ataca a ") || m.contains("inflige") || m.contains("derrotado")) {
            return "JUGADOR";
        }
        if (m.contains("objeto recogido") || m.contains("objeto equipado")
                || m.contains("objeto usado") || m.contains("llave")) {
            return "OBJETO";
        }
        return "SISTEMA";
    }

    /**
     * Devuelve la ruta del asset (relativa a ASSETS_BASE) para un tipo de objeto.
     */
    private Node crearIconoObjeto(Objeto obj, double tamanio) {
        if (obj instanceof modelo.objetos.Pocion) {
            return crearSpriteAssets("TinyBits Inventory Pack - Potions.png", tamanio);
        }
        if (obj instanceof modelo.objetos.Escudo) {
            return crearSpriteArchivo("datos" + File.separator + "iconos" + File.separator + "escudo.png", tamanio, false);
        }
        ImageView icono = crearSpriteAssets(assetParaObjeto(obj), tamanio);
        icono.setSmooth(false);
        return icono;
    }

    private Node crearIconoCeldaEspecial(TipoCelda tipo, double tamanio) {
        if (tipo == TipoCelda.PUERTA) {
            return crearSpriteArchivo("Dungeon Asset Pack" + File.separator + "door_closed.png", tamanio * 1.05, false);
        }
        if (tipo == TipoCelda.SALIDA) {
            return crearSpriteArchivo("datos" + File.separator + "iconos" + File.separator + "salida.png", tamanio * 1.2, false);
        }
        if (tipo == TipoCelda.TESORO) {
            return crearSpriteArchivo("datos" + File.separator + "iconos" + File.separator + "tesoro.png", tamanio, false);
        }
        return null;
    }

    private String assetParaObjeto(Objeto obj) {
        if (obj instanceof modelo.objetos.Llave) return "objects" + File.separator + "key.png";
        if (obj instanceof modelo.objetos.Arco) return "weapons" + File.separator + "bow1.png";
        if (obj instanceof modelo.objetos.Arma) return "weapons" + File.separator + "sword1.png";
        return "objects" + File.separator + "chest4.png";
    }

    /**
     * Busca una imagen en el cache lineal (ListaDE).
     */
    private static Image buscarEnCache(String ruta) {
        MiIterador<EntradaImagen> it = IMAGE_CACHE.getIterador();
        while (it.hasNext()) {
            EntradaImagen e = it.next();
            if (e.coincide(ruta)) return e.getValor();
        }
        return null;
    }

    /**
     * Carga un PNG del Dungeon Asset Pack y devuelve un ImageView.
     * Para spritesheets con varias frames, muestra solo la primera frame.
     */
    private ImageView crearSpriteAssets(String assetPath, double tamanio) {
        String fullPath = ASSETS_BASE + assetPath;
        return crearSpriteArchivo(fullPath, tamanio);
    }

    private ImageView crearSpriteArchivo(String fullPath, double tamanio) {
        return crearSpriteArchivo(fullPath, tamanio, true);
    }

    private ImageView crearSpriteArchivo(String fullPath, double tamanio, boolean recortarSpritesheet) {
        Image img = buscarEnCache(fullPath);
        if (img == null) {
            try {
                File f = new File(fullPath);
                img = new Image(f.toURI().toString());
                IMAGE_CACHE.addLast(new EntradaImagen(fullPath, img));
            } catch (Exception e) {
                return new ImageView();
            }
        }
        ImageView iv = new ImageView(img);

        double imgW = img.getWidth();
        double imgH = img.getHeight();

        // Los assets del pack pueden ser spritesheets; los PNG locales ya son iconos finales.
        if (recortarSpritesheet && imgH > 18) {
            int numFrames = (imgW > imgH * 2.5) ? 4 : 2;
            double frameW = imgW / numFrames;
            iv.setViewport(new Rectangle2D(0, 0, frameW, imgH));
            double scale = tamanio / Math.max(frameW, imgH);
            iv.setFitWidth(frameW * scale);
            iv.setFitHeight(imgH * scale);
        } else {
            // Objeto individual: escalado uniforme
            double scale = tamanio / Math.max(imgW, imgH);
            iv.setFitWidth(imgW * scale);
            iv.setFitHeight(imgH * scale);
        }
        iv.setPreserveRatio(true);
        return iv;
    }

    public boolean dispararBolaFuego(int df, int dc) {
        if (df == 0 && dc == 0 || animOverlay == null || cumX == null) {
            return false;
        }
        if (!partida.registrarDisparoBolaFuego()) {
            return false;
        }
        Jugador jugador = partida.getJugador();
        ReproductorSfx.getInstancia().reproducirDisparoBolaFuego();
        new BolaDeFuego(jugador.getFila(), jugador.getColumna(), df, dc,
                partida.getCuevaActual().getId()).iniciar();
        actualizar();
        return true;
    }

    public boolean dispararBolaHielo(int df, int dc) {
        if (df == 0 && dc == 0 || animOverlay == null || cumX == null) {
            return false;
        }
        if (!partida.registrarDisparoBolaHielo()) {
            return false;
        }
        Jugador jugador = partida.getJugador();
        ReproductorSfx.getInstancia().reproducirDisparoBolaFuego();
        new BolaDeHielo(jugador.getFila(), jugador.getColumna(), df, dc,
                partida.getCuevaActual().getId()).iniciar();
        actualizar();
        return true;
    }

    private Enemigo buscarEnemigoActual(int fila, int columna) {
        ListaDE<Enemigo> enems = partida.getEnemigosActuales();
        MiIterador<Enemigo> it = enems.getIterador();
        while (it.hasNext()) {
            Enemigo e = it.next();
            if (e.getFila() == fila && e.getColumna() == columna) {
                return e;
            }
        }
        return null;
    }

    private boolean esBloqueanteBolaFuego(CuevaEnMapa cueva, int fila, int columna) {
        if (cueva == null || fila < 0 || columna < 0
                || fila >= cueva.getFilas() || columna >= cueva.getColumnas()) {
            return true;
        }
        TipoCelda tipo = cueva.getCelda(fila, columna).getTipo();
        return tipo == TipoCelda.MURO || tipo == TipoCelda.ROCA || tipo == TipoCelda.ARBUSTO;
    }

    private Node crearNodoBolaFuego(double tamanio) {
        File sprite = new File("datos" + File.separator + "iconos" + File.separator + "bola_fuego.png");
        if (sprite.exists()) {
            ImageView img = crearSpriteArchivo(sprite.getPath(), tamanio, false);
            img.setSmooth(false);
            return img;
        }

        double radio = Math.max(5, tamanio * 0.22);
        Circle halo = new Circle(radio * 1.5);
        halo.setFill(Color.rgb(255, 120, 0, 0.28));
        halo.setStroke(Color.rgb(255, 210, 70, 0.75));
        halo.setStrokeWidth(1.5);

        Circle nucleo = new Circle(radio);
        nucleo.setFill(Color.rgb(255, 95, 15, 0.95));
        nucleo.setStroke(Color.rgb(255, 238, 120));
        nucleo.setStrokeWidth(2);

        StackPane bola = new StackPane(halo, nucleo);
        bola.setPrefSize(radio * 3, radio * 3);
        return bola;
    }

    private Node crearNodoBolaHielo(double tamanio) {
        double radio = Math.max(5, tamanio * 0.22);
        Circle halo = new Circle(radio * 1.55);
        halo.setFill(Color.rgb(110, 210, 255, 0.26));
        halo.setStroke(Color.rgb(190, 245, 255, 0.85));
        halo.setStrokeWidth(1.5);

        Circle nucleo = new Circle(radio);
        nucleo.setFill(Color.rgb(120, 220, 255, 0.94));
        nucleo.setStroke(Color.WHITE);
        nucleo.setStrokeWidth(2);

        StackPane bola = new StackPane(halo, nucleo);
        bola.setPrefSize(radio * 3, radio * 3);
        return bola;
    }

    public boolean atacarCelda(int fila, int columna) {
        ataqueFila = fila;
        ataqueCol = columna;
        targetAntesAtaque = null;
        ListaDE<Enemigo> enems = partida.getEnemigosActuales();
        MiIterador<Enemigo> it = enems.getIterador();
        while (it.hasNext()) {
            Enemigo e = it.next();
            if (e.getFila() == fila && e.getColumna() == columna) {
                targetAntesAtaque = e;
                break;
            }
        }
        boolean ok = partida.atacar(fila, columna);
        if (ok) {
            ReproductorSfx.getInstancia().reproducirAtaque();
            iniciarEfectoAtaque();
        } else {
            ataqueFila = ataqueCol = -1;
        }
        return ok;
    }

    private void agregarResaltadoEnemigosAdyacentes(int filas, int cols, double cellSize) {
        if (partida.isAccionRealizada()) {
            return;
        }
        ListaSE<PersonajeEnMapa> enemigosAdyacentes = partida.getEnemigosAdyacentes();
        for (int i = 0; i < enemigosAdyacentes.getSize(); i++) {
            PersonajeEnMapa enemigo = enemigosAdyacentes.get(i);
            agregarAnilloCelda(enemigo.getFila(), enemigo.getColumna(), filas, cols, cellSize,
                    Color.rgb(255, 215, 0, 0.16), Color.rgb(255, 215, 0, 0.95), 3.0);
        }
    }

    private void agregarFlashCelda(int fila, int columna, int filas, int cols, double cellSize,
            Color relleno, Color borde) {
        agregarAnilloCelda(fila, columna, filas, cols, cellSize, relleno, borde, 4.0);
    }

    private void agregarAnilloCelda(int fila, int columna, int filas, int cols, double cellSize,
            Color relleno, Color borde, double grosor) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= cols
                || celdas == null || celdas[fila] == null || celdas[fila][columna] == null) {
            return;
        }
        Rectangle anillo = new Rectangle(cellSize * 0.82, cellSize * 0.82);
        anillo.setFill(relleno);
        anillo.setStroke(borde);
        anillo.setStrokeWidth(grosor);
        anillo.setArcWidth(8);
        anillo.setArcHeight(8);
        anillo.setMouseTransparent(true);
        celdas[fila][columna].getChildren().add(anillo);
    }

    public void iniciarEfectoAtaque() {
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

    public void iniciarEfectoRecibirAtaque(int fila, int col) {
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

    // ---------------------------------------------------------------
    // Animaciones: movimiento suave, ataque y muerte
    // ---------------------------------------------------------------

    /**
     * Animacion de movimiento suave del jugador entre dos celdas.
     * Usa translateX/Y sobre el sprite del jugador para desplazarlo
     * visualmente desde la celda anterior a la nueva, con easing
     * smoothstep (suave al inicio y al final).
     */
    public void animarMovimiento(int oldF, int oldC) {
        if (animMovimientoTimeline != null || jugadorSprite == null || cumX == null) return;
        int newF = partida.getJugador().getFila();
        int newC = partida.getJugador().getColumna();
        if (oldF == newF && oldC == newC) return;

        double offsetX = (cumX[oldC] + colWidth[oldC] / 2.0) - (cumX[newC] + colWidth[newC] / 2.0);
        double offsetY = (cumY[oldF] + rowHeight[oldF] / 2.0) - (cumY[newF] + rowHeight[newF] / 2.0);

        jugadorSprite.setTranslateX(offsetX);
        jugadorSprite.setTranslateY(offsetY);

        animMovimientoTimeline = new Timeline();
        int frames = 8;
        double duracionMs = 150;
        for (int i = 1; i <= frames; i++) {
            double t = (double) i / frames;
            double easeT = t * t * (3.0 - 2.0 * t);
            final double tx = offsetX * (1.0 - easeT);
            final double ty = offsetY * (1.0 - easeT);
            KeyFrame kf = new KeyFrame(Duration.millis(duracionMs * t), e -> {
                jugadorSprite.setTranslateX(tx);
                jugadorSprite.setTranslateY(ty);
            });
            animMovimientoTimeline.getKeyFrames().add(kf);
        }
        animMovimientoTimeline.setOnFinished(e -> {
            jugadorSprite.setTranslateX(0);
            jugadorSprite.setTranslateY(0);
            animMovimientoTimeline = null;
        });
        animMovimientoTimeline.setCycleCount(1);
        animMovimientoTimeline.play();
    }

    /**
     * Animacion visual de impacto al atacar: circulo expansivo que
     * aparece en la celda del enemigo, crece y se desvanece.
     */
    public void animarProyectil(int playerF, int playerC, int enemyF, int enemyC) {
        if (cumX == null || gridOverlay == null) return;
        double startX = cumX[playerC] + colWidth[playerC] / 2.0;
        double startY = cumY[playerF] + rowHeight[playerF] / 2.0;
        double endX = cumX[enemyC] + colWidth[enemyC] / 2.0;
        double endY = cumY[enemyF] + rowHeight[enemyF] / 2.0;

        Circle bolt = new Circle(startX, startY, 5);
        bolt.setFill(Color.rgb(255, 220, 80));
        bolt.setStroke(Color.rgb(255, 255, 200));
        bolt.setStrokeWidth(1.5);
        bolt.setMouseTransparent(true);
        gridOverlay.getChildren().add(bolt);

        Timeline tl = new Timeline(
            new KeyFrame(Duration.millis(0), e -> {
                bolt.setCenterX(startX);
                bolt.setCenterY(startY);
                bolt.setOpacity(1.0);
            }),
            new KeyFrame(Duration.millis(140), e -> {
                bolt.setCenterX(endX);
                bolt.setCenterY(endY);
                bolt.setOpacity(0.3);
            }),
            new KeyFrame(Duration.millis(170), e -> {
                gridOverlay.getChildren().remove(bolt);
            })
        );
        tl.setCycleCount(1);
        tl.play();
    }

    public void animarAtaque(int enemyF, int enemyC) {
        if (cumX == null || gridOverlay == null) return;
        double cx = cumX[enemyC] + colWidth[enemyC] / 2.0;
        double cy = cumY[enemyF] + rowHeight[enemyF] / 2.0;
        double tam = Math.min(colWidth[enemyC], rowHeight[enemyF]) * 0.4;

        Circle impacto = new Circle(cx, cy, tam * 0.15);
        impacto.setFill(Color.rgb(255, 220, 80, 0.85));
        impacto.setStroke(Color.rgb(255, 255, 200, 0.95));
        impacto.setStrokeWidth(2.5);
        impacto.setMouseTransparent(true);
        gridOverlay.getChildren().add(impacto);

        Timeline tl = new Timeline(
            new KeyFrame(Duration.millis(0), e -> {
                impacto.setRadius(tam * 0.1);
                impacto.setOpacity(1.0);
            }),
            new KeyFrame(Duration.millis(120), e -> {
                impacto.setRadius(tam * 0.7);
                impacto.setOpacity(0.3);
            }),
            new KeyFrame(Duration.millis(220), e -> {
                gridOverlay.getChildren().remove(impacto);
            })
        );
        tl.setCycleCount(1);
        tl.play();
    }

    /**
     * Animacion de muerte de un enemigo: se crea un sprite en el
     * overlay de animaciones en la posicion del enemigo, se desvanece
     * con FadeTransition y se encoge con ScaleTransition.
     * Usa animOverlay (capa no limpiada por actualizar) para que
     * la animacion no se interrumpa al redibujar el grid.
     */
    public void animarMuerteEnemigo(int fila, int columna, String assetPath) {
        if (animOverlay == null || cumX == null) return;

        double px = cumX[columna] + (colWidth[columna] - spriteSizeCache) / 2.0;
        double py = cumY[fila] + (rowHeight[fila] - spriteSizeCache) / 2.0;

        ImageView sprite = crearSpriteAssets(assetPath, spriteSizeCache);
        sprite.setLayoutX(px);
        sprite.setLayoutY(py);
        sprite.setMouseTransparent(true);
        animOverlay.getChildren().add(sprite);

        FadeTransition fade = new FadeTransition(Duration.millis(400), sprite);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(400), sprite);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(0.3);
        scale.setToY(0.3);

        fade.setOnFinished(e -> animOverlay.getChildren().remove(sprite));
        fade.play();
        scale.play();
    }

    public void animarDisparosEnemigos(ListaSE<DisparoEnemigo> disparos) {
        if (disparos == null) {
            return;
        }
        for (int i = 0; i < disparos.getSize(); i++) {
            animarDisparoEnemigo(disparos.get(i));
        }
    }

    private void animarDisparoEnemigo(DisparoEnemigo disparo) {
        if (disparo == null || animOverlay == null || cumX == null) {
            return;
        }
        int fo = disparo.getFilaOrigen();
        int co = disparo.getColumnaOrigen();
        int fd = disparo.getFilaDestino();
        int cd = disparo.getColumnaDestino();
        if (fo < 0 || co < 0 || fd < 0 || cd < 0
                || fo >= rowHeight.length || fd >= rowHeight.length
                || co >= colWidth.length || cd >= colWidth.length) {
            return;
        }

        double startX = cumX[co] + colWidth[co] / 2.0;
        double startY = cumY[fo] + rowHeight[fo] / 2.0;
        double endX = cumX[cd] + colWidth[cd] / 2.0;
        double endY = cumY[fd] + rowHeight[fd] / 2.0;

        Circle proyectil = new Circle(startX, startY, Math.max(4, spriteSizeCache * 0.12));
        proyectil.setFill(Color.rgb(170, 255, 135, 0.95));
        proyectil.setStroke(Color.rgb(235, 255, 190));
        proyectil.setStrokeWidth(1.5);
        proyectil.setMouseTransparent(true);
        animOverlay.getChildren().add(proyectil);

        Timeline tl = new Timeline(
                new KeyFrame(Duration.millis(0), e -> {
                    proyectil.setCenterX(startX);
                    proyectil.setCenterY(startY);
                    proyectil.setOpacity(1.0);
                }),
                new KeyFrame(Duration.millis(180), e -> {
                    proyectil.setCenterX(endX);
                    proyectil.setCenterY(endY);
                    proyectil.setOpacity(0.75);
                }),
                new KeyFrame(Duration.millis(260), e -> {
                    proyectil.setOpacity(0.0);
                    animOverlay.getChildren().remove(proyectil);
                })
        );
        tl.setCycleCount(1);
        tl.play();
    }

    private class BolaDeFuego {
        private int fila;
        private int columna;
        private final int df;
        private final int dc;
        private final String cuevaId;
        private int distanciaRecorrida;
        private final Node nodo;
        private final Timeline timeline;

        BolaDeFuego(int filaInicial, int columnaInicial, int df, int dc, String cuevaId) {
            this.fila = filaInicial;
            this.columna = columnaInicial;
            this.df = df;
            this.dc = dc;
            this.cuevaId = cuevaId;
            this.distanciaRecorrida = 0;
            double tamanio = Math.min(colWidth[columnaInicial], rowHeight[filaInicial]) * 0.48;
            this.nodo = crearNodoBolaFuego(tamanio);
            this.nodo.setMouseTransparent(true);
            this.timeline = new Timeline(new KeyFrame(Duration.millis(120), e -> avanzar()));
            this.timeline.setCycleCount(Timeline.INDEFINITE);
        }

        void iniciar() {
            posicionarNodo();
            animOverlay.getChildren().add(nodo);
            timeline.play();
        }

        private void avanzar() {
            CuevaEnMapa cueva = partida.getCuevaActual();
            if (cueva == null || !cuevaId.equals(cueva.getId())) {
                destruir(false);
                return;
            }
            if (distanciaRecorrida >= ConstantesJuego.RANGO_BOLA_FUEGO) {
                destruir(false);
                return;
            }

            int siguienteFila = fila + df;
            int siguienteColumna = columna + dc;

            if (esBloqueanteBolaFuego(cueva, siguienteFila, siguienteColumna)) {
                destruir(true);
                return;
            }

            fila = siguienteFila;
            columna = siguienteColumna;
            distanciaRecorrida++;
            posicionarNodo();

            Enemigo objetivo = buscarEnemigoActual(fila, columna);
            if (objetivo != null) {
                String assetMuerte = getEnemyAssetPath(objetivo);
                ResultadoImpactoBolaFuego resultado =
                        partida.impactarBolaFuego(fila, columna, ConstantesJuego.DANO_BOLA_FUEGO);
                destruir(true);
                actualizar();
                if (resultado.hayImpacto() && partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO) {
                    animarAtaque(fila, columna);
                    if (resultado.isEnemigoMuerto()) {
                        animarMuerteEnemigo(fila, columna, assetMuerte);
                    }
                }
            }
        }

        private void posicionarNodo() {
            if (fila < 0 || columna < 0 || fila >= rowHeight.length || columna >= colWidth.length) {
                return;
            }
            double ancho = nodo.getBoundsInLocal().getWidth();
            double alto = nodo.getBoundsInLocal().getHeight();
            if (nodo instanceof ImageView) {
                ImageView imageView = (ImageView) nodo;
                ancho = imageView.getFitWidth();
                alto = imageView.getFitHeight();
            } else if (nodo instanceof StackPane) {
                StackPane stack = (StackPane) nodo;
                ancho = stack.getPrefWidth();
                alto = stack.getPrefHeight();
            }
            if (ancho <= 0) {
                ancho = colWidth[columna] * 0.48;
            }
            if (alto <= 0) {
                alto = rowHeight[fila] * 0.48;
            }
            double x = cumX[columna] + (colWidth[columna] - ancho) / 2.0;
            double y = cumY[fila] + (rowHeight[fila] - alto) / 2.0;
            nodo.setLayoutX(x);
            nodo.setLayoutY(y);
        }

        private void destruir(boolean conImpacto) {
            timeline.stop();
            animOverlay.getChildren().remove(nodo);
            if (conImpacto) {
                ReproductorSfx.getInstancia().reproducirImpactoBolaFuego();
            }
        }
    }

    private class BolaDeHielo {
        private int fila;
        private int columna;
        private final int df;
        private final int dc;
        private final String cuevaId;
        private int distanciaRecorrida;
        private final Node nodo;
        private final Timeline timeline;

        BolaDeHielo(int filaInicial, int columnaInicial, int df, int dc, String cuevaId) {
            this.fila = filaInicial;
            this.columna = columnaInicial;
            this.df = df;
            this.dc = dc;
            this.cuevaId = cuevaId;
            this.distanciaRecorrida = 0;
            double tamanio = Math.min(colWidth[columnaInicial], rowHeight[filaInicial]) * 0.48;
            this.nodo = crearNodoBolaHielo(tamanio);
            this.nodo.setMouseTransparent(true);
            this.timeline = new Timeline(new KeyFrame(Duration.millis(120), e -> avanzar()));
            this.timeline.setCycleCount(Timeline.INDEFINITE);
        }

        void iniciar() {
            posicionarNodo();
            animOverlay.getChildren().add(nodo);
            timeline.play();
        }

        private void avanzar() {
            CuevaEnMapa cueva = partida.getCuevaActual();
            if (cueva == null || !cuevaId.equals(cueva.getId())) {
                destruir(false);
                return;
            }
            if (distanciaRecorrida >= ConstantesJuego.RANGO_BOLA_FUEGO) {
                destruir(false);
                return;
            }

            int siguienteFila = fila + df;
            int siguienteColumna = columna + dc;

            if (esBloqueanteBolaFuego(cueva, siguienteFila, siguienteColumna)) {
                destruir(true);
                return;
            }

            fila = siguienteFila;
            columna = siguienteColumna;
            distanciaRecorrida++;
            posicionarNodo();

            Enemigo objetivo = buscarEnemigoActual(fila, columna);
            if (objetivo != null) {
                ResultadoImpactoBolaHielo resultado = partida.impactarBolaHielo(fila, columna);
                destruir(true);
                actualizar();
                if (resultado.hayImpacto() && partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO) {
                    agregarFlashCelda(fila, columna, celdas.length, celdas[0].length, spriteSizeCache,
                            Color.rgb(80, 220, 255, 0.28), Color.rgb(185, 245, 255, 0.95));
                }
            }
        }

        private void posicionarNodo() {
            if (fila < 0 || columna < 0 || fila >= rowHeight.length || columna >= colWidth.length) {
                return;
            }
            double ancho = nodo.getBoundsInLocal().getWidth();
            double alto = nodo.getBoundsInLocal().getHeight();
            if (nodo instanceof StackPane) {
                StackPane stack = (StackPane) nodo;
                ancho = stack.getPrefWidth();
                alto = stack.getPrefHeight();
            }
            if (ancho <= 0) {
                ancho = colWidth[columna] * 0.48;
            }
            if (alto <= 0) {
                alto = rowHeight[fila] * 0.48;
            }
            double x = cumX[columna] + (colWidth[columna] - ancho) / 2.0;
            double y = cumY[fila] + (rowHeight[fila] - alto) / 2.0;
            nodo.setLayoutX(x);
            nodo.setLayoutY(y);
        }

        private void destruir(boolean conImpacto) {
            timeline.stop();
            animOverlay.getChildren().remove(nodo);
            if (conImpacto) {
                ReproductorSfx.getInstancia().reproducirImpactoBolaFuego();
            }
        }
    }

    /**
     * Devuelve la ruta del asset sprite para un enemigo, segun
     * si es boss o no y la tematica de la cueva actual.
     */
    public String getEnemyAssetPath(Enemigo e) {
        boolean esBoss = e instanceof modelo.personajes.Boss;
        DatosTemaCueva tema = DatosTemaCueva.paraCuevaId(partida.getCuevaActual().getId());
        if (esBoss) {
            return tema.getAssetBoss();
        }
        if (e != null && e.getTipoEnemigo() == TipoEnemigo.ARQUERO) {
            return "characters" + File.separator + "Spritesheets" + File.separator + "shaman2_idle.png";
        }
        return tema.getAssetEnemigo();
    }

    private void agregarBarraVida(StackPane cell, int vidaActual, int vidaMaxima, double cellSize) {
        double anchoBarra = cellSize * 0.72;
        double altoBarra = 4;
        double yBarra = 2;

        Text hpText = new Text(vidaActual + "/" + vidaMaxima);
        hpText.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 10));
        hpText.setFill(Color.web("#FFF2A8"));
        hpText.setStroke(Color.BLACK);
        hpText.setStrokeWidth(0.9);
        StackPane.setAlignment(hpText, Pos.TOP_CENTER);
        StackPane.setMargin(hpText, new Insets(-1, 0, 0, 0));

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
        StackPane.setAlignment(relleno, Pos.BOTTOM_CENTER);
        double margenDerecho = anchoBarra * (1 - pct);
        StackPane.setMargin(relleno, new Insets(0, margenDerecho, yBarra, 0));

        cell.getChildren().addAll(hpText, fondo, relleno);
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
            case TESORO: return Color.rgb(150, 118, 78);
            case SALIDA: return Color.rgb(200, 50, 50);
            case TRAMPA: return Color.rgb(220, 120, 40);
            case ROCA:   return Color.rgb(100, 100, 110);
            case ARBUSTO:return Color.rgb(50, 120, 50);
            default:     return Color.rgb(100, 100, 100);
        }
    }
}
