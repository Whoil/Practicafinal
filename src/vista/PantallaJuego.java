package vista;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
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
import modelo.juego.Partida;
import modelo.juego.Partida.ObjetoEnMapa;
import modelo.mapa.Celda;
import modelo.mapa.Cueva;
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
    private Pane gridOverlay;
    private Text txtVida, txtAtaque, txtDefensa, txtTurnos, txtCueva, txtEstado;
    private VBox inventarioBox;
    private VBox logBox;
    private Text txtCuevaNombre;

    // Acciones desactivables
    private VBox accionesBox;

    public PantallaJuego(Partida partida, Stage stage, Runnable volverAlMenu) {
        this.partida = partida;
        this.stage = stage;
        this.volverAlMenu = volverAlMenu;
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
        gridOverlay = new Pane();
        gridOverlay.setMouseTransparent(true);
        gridLayer.getChildren().addAll(gridCeldas, gridOverlay);
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
            KeyCode k = e.getCode();
            if (k == KeyCode.W || k == KeyCode.UP) { partida.moverJugadorArriba(); actualizar(); }
            else if (k == KeyCode.S || k == KeyCode.DOWN) { partida.moverJugadorAbajo(); actualizar(); }
            else if (k == KeyCode.A || k == KeyCode.LEFT) { partida.moverJugadorIzquierda(); actualizar(); }
            else if (k == KeyCode.D || k == KeyCode.RIGHT) { partida.moverJugadorDerecha(); actualizar(); }
            else if (k == KeyCode.SPACE) { partida.atacar(); actualizar(); }
            else if (k == KeyCode.R) { partida.recogerObjeto(); actualizar(); }
            else if (k == KeyCode.T) { partida.terminarTurno(); actualizar(); }
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
        gridCeldas.setHgap(1);
        gridCeldas.setVgap(1);

        Cueva cueva = partida.getCuevaActual();
        if (cueva == null) return;

        int filas = cueva.getFilas();
        int cols = cueva.getColumnas();
        double cellSize = Math.min(600.0 / cols, 600.0 / filas);
        cellSize = Math.min(cellSize, 80);

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                Celda celda = cueva.getCelda(f, c);
                Rectangle rect = new Rectangle(cellSize - 1, cellSize - 1);
                rect.setFill(colorParaTipo(celda.getTipo()));
                rect.setStroke(Color.rgb(60, 60, 60));
                rect.setStrokeWidth(0.5);

                StackPane cellPane = new StackPane(rect);
                // Click para moverse
                final int ff = f, cc = c;
                cellPane.setOnMouseClicked(e -> {
                    partida.moverJugador(ff, cc);
                    actualizar();
                });
                cellPane.setCursor(Cursor.HAND);
                gridCeldas.add(cellPane, c, f);
            }
        }

        gridOverlay.setPrefSize(cols * cellSize, filas * cellSize);
    }

    // ---------------------------------------------------------------
    // Actualizacion (redibujar cada vez que cambia el estado)
    // ---------------------------------------------------------------

    private void actualizar() {
        // Overlay de entidades (jugador, enemigos, objetos)
        gridOverlay.getChildren().clear();
        Cueva cueva = partida.getCuevaActual();
        if (cueva == null) return;

        int filas = cueva.getFilas();
        int cols = cueva.getColumnas();
        double cellSize = Math.min(600.0 / cols, 600.0 / filas);
        cellSize = Math.min(cellSize, 80);

        // Jugador (circulo azul)
        Jugador j = partida.getJugador();
        Circle playerCircle = new Circle(
                j.getColumna() * cellSize + cellSize / 2,
                j.getFila() * cellSize + cellSize / 2,
                cellSize * 0.35);
        playerCircle.setFill(Color.rgb(64, 164, 223, 0.9));
        playerCircle.setStroke(Color.WHITE);
        playerCircle.setStrokeWidth(2);
        DropShadow sombraP = new DropShadow();
        sombraP.setRadius(8);
        sombraP.setColor(Color.rgb(64, 164, 223, 0.5));
        playerCircle.setEffect(sombraP);
        gridOverlay.getChildren().add(playerCircle);

        // Enemigos (circulos rojos)
        ListaDE<Enemigo> enemigos = partida.getEnemigosActuales();
        MiIterador<Enemigo> itE = enemigos.getIterador();
        while (itE.hasNext()) {
            Enemigo e = itE.next();
            Circle enemyCircle = new Circle(
                    e.getColumna() * cellSize + cellSize / 2,
                    e.getFila() * cellSize + cellSize / 2,
                    cellSize * 0.3);
            enemyCircle.setFill(Color.rgb(220, 50, 50, 0.9));
            enemyCircle.setStroke(Color.rgb(80, 0, 0));
            enemyCircle.setStrokeWidth(1.5);
            gridOverlay.getChildren().add(enemyCircle);
        }

        // Objetos en el mapa (circulos amarillos pequenos)
        ListaDE<ObjetoEnMapa> objetos = partida.getObjetosActuales();
        MiIterador<ObjetoEnMapa> itO = objetos.getIterador();
        while (itO.hasNext()) {
            ObjetoEnMapa om = itO.next();
            Circle itemCircle = new Circle(
                    om.getColumna() * cellSize + cellSize / 2,
                    om.getFila() * cellSize + cellSize / 2,
                    cellSize * 0.15);
            itemCircle.setFill(Color.rgb(255, 215, 0, 0.9));
            itemCircle.setStroke(Color.rgb(180, 140, 0));
            itemCircle.setStrokeWidth(1);
            gridOverlay.getChildren().add(itemCircle);
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
                btnUsar.setOnMouseClicked(e -> {
                    partida.usarPocion(obj.getId());
                    actualizar();
                });
                itemRow.getChildren().add(btnUsar);
            }
            if (obj.esEquipable()) {
                Text btnEquip = crearBotonTexto("[EQUIPAR]");
                btnEquip.setOnMouseClicked(e -> {
                    partida.equiparItem(obj.getId());
                    actualizar();
                });
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
        agregarBotonAccion("ARRIBA [W]", () -> { partida.moverJugadorArriba(); actualizar(); });
        HBox movHoriz = new HBox(4);
        movHoriz.setAlignment(Pos.CENTER);
        Text btnIzq = crearBotonTexto("< IZQ [A]");
        btnIzq.setOnMouseClicked(e -> { partida.moverJugadorIzquierda(); actualizar(); });
        Text btnDer = crearBotonTexto("DER [D] >");
        btnDer.setOnMouseClicked(e -> { partida.moverJugadorDerecha(); actualizar(); });
        movHoriz.getChildren().addAll(btnIzq, btnDer);
        accionesBox.getChildren().add(movHoriz);
        agregarBotonAccion("ABAJO [S]", () -> { partida.moverJugadorAbajo(); actualizar(); });

        agregarBotonAccion("ATACAR [ESPACIO]", () -> { partida.atacar(); actualizar(); });
        agregarBotonAccion("RECOGER [R]", () -> { partida.recogerObjeto(); actualizar(); });
        agregarBotonAccion("TERMINAR TURNO [T]", () -> { partida.terminarTurno(); actualizar(); });

        Text btnPuerta = crearBotonTexto("CAMBIAR CUEVA (en PUERTA)");
        btnPuerta.setOnMouseClicked(e -> { partida.cambiarCueva(); actualizar(); });
        accionesBox.getChildren().add(btnPuerta);

        Text btnGuardar = crearBotonTexto("GUARDAR PARTIDA");
        btnGuardar.setOnMouseClicked(e -> {
            try {
                partida.guardar("datos/partida_guardada.json");
                partida.getMensajes().addLast("Partida guardada.");
            } catch (Exception ex) {
                partida.getMensajes().addLast("Error al guardar: " + ex.getMessage());
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
    }

    // ---------------------------------------------------------------
    // Helpers de UI
    // ---------------------------------------------------------------

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
     * Mapa de TipoCelda a colores para el grid.
     */
    private Color colorParaTipo(TipoCelda tipo) {
        switch (tipo) {
            case MURO:   return Color.rgb(70, 70, 70);
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
