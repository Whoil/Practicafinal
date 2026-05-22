package vista;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * EscapeMazmorraApp — Menu principal del juego "ESCAPE DE LA MAZMORRA".
 *
 * Estetica rustica con sombras, degradados, antorchas animadas,
 * tesoro decorativo y transiciones suaves entre pantallas.
 * Todo el estilado se aplica via codigo (setStyle / setFill / setEffect)
 * para evitar la apariencia por defecto de JavaFX Modena.
 */
public class EscapeMazmorraApp extends Application {

    private static final int ANCHO = 1280;
    private static final int ALTO = 720;
    private static final String FONT_FAMILY = "Georgia, serif";
    private static final Color MARRON_TITULO = Color.web("#6E4720");

    private StackPane raiz;
    private Pane contenidoActual;
    private Timeline animacionIzquierda;
    private Timeline animacionDerecha;
    private Stage stage;

    /** Partida activa durante el flujo narrativo. */
    private modelo.juego.Partida partida;

    @Override
    public void start(Stage stage) {
        // Redirigir System.err a consola + crash.log
        try {
            java.io.PrintStream originalErr = System.err;
            java.io.PrintStream fileOut = new java.io.PrintStream(
                new java.io.FileOutputStream("crash.log", true));
            System.setErr(new java.io.PrintStream(
                new java.io.OutputStream() {
                    @Override public void write(int b) {
                        originalErr.write(b);
                        fileOut.write(b);
                    }
                    @Override public void write(byte[] buf, int off, int len) {
                        originalErr.write(buf, off, len);
                        fileOut.write(buf, off, len);
                        fileOut.flush();
                    }
                    @Override public void flush() { originalErr.flush(); fileOut.flush(); }
                    @Override public void close() { fileOut.close(); }
                }));
        } catch (Exception ignored) {}
        Thread.setDefaultUncaughtExceptionHandler((t, ex) -> {
            System.err.println("[CRASH] Hilo: " + t.getName());
            ex.printStackTrace();
            Platform.runLater(() -> {
                if (stage != null && stage.isShowing()) {
                    try {
                        stage.close();
                    } catch (Exception ignored) {}
                }
            });
        });
        raiz = new StackPane();

        // Capa 1 — Fondo de cueva
        raiz.getChildren().add(crearFondoCueva());

        // Capa 2 — Antorchas
        Pane antorchaIzq = crearAntorchaCompleta(160, 260);
        Pane antorchaDer = crearAntorchaCompleta(ANCHO - 160, 260);
        raiz.getChildren().addAll(antorchaIzq, antorchaDer);

        // Capa 3 — Tesoro
        raiz.getChildren().add(crearTesoro());

        // Capa 4 — Contenido intercambiable
        contenidoActual = mostrarPantallaInicio();
        raiz.getChildren().add(contenidoActual);

        this.stage = stage;
        Scene escena = new Scene(raiz, ANCHO, ALTO);
        stage.setTitle("ESCAPE DE LA MAZMORRA");
        stage.setScene(escena);
        stage.setResizable(false);
        stage.show();

        animacionIzquierda.play();
        animacionDerecha.play();
    }

    @Override
    public void stop() {
        if (animacionIzquierda != null) animacionIzquierda.stop();
        if (animacionDerecha != null) animacionDerecha.stop();
    }

    // -----------------------------------------------------------------
    //  FONDO DE CUEVA
    // -----------------------------------------------------------------

    /**
     * Gradiente radial desde un gris claro cenital hacia negro absoluto
     * en los bordes, simulando una cueva iluminada desde el centro.
     */
    private Pane crearFondoCueva() {
        RadialGradient grad = new RadialGradient(
                0, 0, 0.5, 0.5, 0.75, true, CycleMethod.NO_CYCLE,
                new Stop(0.00, Color.web("#3C3C3C")),
                new Stop(0.25, Color.web("#2E2E2E")),
                new Stop(0.55, Color.web("#1E1E1E")),
                new Stop(0.80, Color.web("#121212")),
                new Stop(1.00, Color.web("#0A0A0A"))
        );
        Rectangle rect = new Rectangle(ANCHO, ALTO);
        rect.setFill(grad);
        return new Pane(rect);
    }

    // -----------------------------------------------------------------
    //  ANTECHAS — soporte de madera, abrazadera metalica, llama triple
    // -----------------------------------------------------------------

    /**
     * Conjunto completo de antorcha: soporte + llama animada.
     */
    private Pane crearAntorchaCompleta(double cx, double sueloY) {
        Pane grupo = new Pane();

        double poloX = cx - 4;
        double baseY = sueloY + 30;

        // Soporte de madera (polo vertical)
        Rectangle madera = new Rectangle(poloX, baseY - 180, 8, 180);
        madera.setFill(Color.web("#4A2F1A"));
        madera.setStroke(Color.web("#2A1A0E"));
        madera.setStrokeWidth(1.5);
        madera.setArcWidth(3);
        madera.setArcHeight(3);

        // Abrazadera metalica
        Rectangle abrazadera = new Rectangle(poloX - 2, baseY - 185, 12, 14);
        abrazadera.setFill(Color.web("#6B6B6B"));
        abrazadera.setStroke(Color.web("#3A3A3A"));
        abrazadera.setStrokeWidth(1);
        abrazadera.setArcWidth(2);
        abrazadera.setArcHeight(2);

        // Leña / base de la llama
        Polygon lenia = new Polygon();
        lenia.getPoints().addAll(
                cx - 10.0, baseY - 168.0,
                cx - 14.0, baseY - 154.0,
                cx - 6.0,  baseY - 148.0,
                cx + 6.0,  baseY - 148.0,
                cx + 14.0, baseY - 154.0,
                cx + 10.0, baseY - 168.0
        );
        lenia.setFill(Color.web("#3A2010"));
        lenia.setStroke(Color.web("#1A0E05"));

        // Llama exterior (rojo oscuro)
        Polygon llamaExt = new Polygon();
        llamaExt.getPoints().addAll(
                cx - 16.0, baseY - 152.0,
                cx - 22.0, baseY - 180.0,
                cx - 14.0, baseY - 210.0,
                cx + 0.0,  baseY - 230.0,
                cx + 14.0, baseY - 210.0,
                cx + 22.0, baseY - 180.0,
                cx + 16.0, baseY - 152.0
        );
        llamaExt.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#FF6347")),
                new Stop(1.0, Color.web("#8B0000"))
        ));
        llamaExt.setStroke(Color.web("#660000"));
        llamaExt.setStrokeWidth(0.5);

        // Llama media (naranja)
        Polygon llamaMed = new Polygon();
        llamaMed.getPoints().addAll(
                cx - 10.0, baseY - 156.0,
                cx - 16.0, baseY - 184.0,
                cx - 10.0, baseY - 212.0,
                cx + 0.0,  baseY - 228.0,
                cx + 10.0, baseY - 212.0,
                cx + 16.0, baseY - 184.0,
                cx + 10.0, baseY - 156.0
        );
        llamaMed.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#FFA500")),
                new Stop(1.0, Color.web("#FF4500"))
        ));
        llamaMed.setStroke(Color.web("#CC3300"));
        llamaMed.setStrokeWidth(0.5);

        // Llama interior (amarillo)
        Polygon llamaInt = new Polygon();
        llamaInt.getPoints().addAll(
                cx - 6.0,  baseY - 160.0,
                cx - 10.0, baseY - 188.0,
                cx - 6.0,  baseY - 214.0,
                cx + 0.0,  baseY - 225.0,
                cx + 6.0,  baseY - 214.0,
                cx + 10.0, baseY - 188.0,
                cx + 6.0,  baseY - 160.0
        );
        llamaInt.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#FFEE58")),
                new Stop(1.0, Color.web("#FF8C00"))
        ));
        llamaInt.setStroke(Color.web("#CC7000"));
        llamaInt.setStrokeWidth(0.5);

        grupo.getChildren().addAll(madera, abrazadera, lenia, llamaExt, llamaMed, llamaInt);

        // Animacion con Timeline — 30 frames precalculados trigonometricamente
        Timeline anim = new Timeline();
        anim.setCycleCount(Timeline.INDEFINITE);
        for (int i = 0; i < 30; i++) {
            double a = i * 2.0 * Math.PI / 30;
            double escalaX = 1.0 + 0.08 * Math.sin(a * 2.1 + 0.3);
            double escalaY = 1.0 + 0.10 * Math.sin(a * 1.7 + 1.2);
            double transY = 4.0 * Math.sin(a * 3.3 + 0.7);
            double opacidadExtra = 0.85 + 0.15 * Math.sin(a * 4.1);

            KeyFrame kf = new KeyFrame(Duration.millis(i * 60), e -> {
                llamaExt.setScaleX(escalaX);
                llamaExt.setScaleY(escalaY);
                llamaExt.setTranslateY(transY);
                llamaExt.setOpacity(opacidadExtra);

                llamaMed.setScaleX(escalaX * 1.03);
                llamaMed.setScaleY(escalaY * 1.05);
                llamaMed.setTranslateY(transY * 0.8);
                llamaMed.setOpacity(opacidadExtra * 0.95);

                llamaInt.setScaleX(escalaX * 1.06);
                llamaInt.setScaleY(escalaY * 1.10);
                llamaInt.setTranslateY(transY * 0.6);
                llamaInt.setOpacity(opacidadExtra * 0.9);

                // Brillo de la abrazadera (simula reflejo del fuego)
                double brillo = 0.4 + 0.25 * Math.sin(a * 2.5);
                String grisValor = String.format("%d", (int)(40 + brillo * 60));
                abrazadera.setFill(Color.web("rgb(" + grisValor + "," + grisValor + "," + grisValor + ")"));
            });
            anim.getKeyFrames().add(kf);
        }

        if (cx < ANCHO / 2) {
            animacionIzquierda = anim;
        } else {
            animacionDerecha = anim;
        }

        return grupo;
    }

    // -----------------------------------------------------------------
    //  TESORO — monton de monedas + cofre abierto
    // -----------------------------------------------------------------

    private Pane crearTesoro() {
        Pane grupo = new Pane();
        double baseX = ANCHO / 2.0;
        double baseY = ALTO - 25;

        // Colina de monedas — 5 filas en piramide
        for (int fila = 0; fila < 5; fila++) {
            int numMonedas = 11 - fila * 2;
            for (int col = 0; col < numMonedas; col++) {
                double cx = baseX - (numMonedas - 1) * 7 + col * 14;
                double cy = baseY - fila * 9 + Math.sin(col) * 2;
                Circle moneda = new Circle(cx, cy, 5);
                double variante = 0.85 + (col % 3) * 0.10;
                int r = (int)(200 * variante);
                int g = (int)(170 * variante);
                int b = (int)(0);
                moneda.setFill(Color.rgb(Math.min(r, 255), Math.min(g, 255), b));
                moneda.setStroke(Color.rgb(139, 101, 8));
                moneda.setStrokeWidth(0.5);
                grupo.getChildren().add(moneda);
            }
        }

        // Cofre — cuerpo
        double cofreX = baseX - 30;
        double cofreY = baseY - 60;
        Rectangle cuerpo = new Rectangle(cofreX, cofreY, 60, 35);
        cuerpo.setFill(Color.web("#6B3A1F"));
        cuerpo.setStroke(Color.web("#3A1E0E"));
        cuerpo.setStrokeWidth(2);
        cuerpo.setArcWidth(4);
        cuerpo.setArcHeight(4);

        // Refuerzos metalicos del cofre
        Rectangle refuerzoH = new Rectangle(cofreX + 2, cofreY + 14, 56, 5);
        refuerzoH.setFill(Color.web("#8B7355"));
        refuerzoH.setStroke(Color.web("#5C4A33"));
        refuerzoH.setStrokeWidth(0.5);

        Rectangle refuerzoV = new Rectangle(cofreX + 27, cofreY + 2, 6, 31);
        refuerzoV.setFill(Color.web("#8B7355"));
        refuerzoV.setStroke(Color.web("#5C4A33"));
        refuerzoV.setStrokeWidth(0.5);

        // Cerradura
        Rectangle cerradura = new Rectangle(cofreX + 24, cofreY + 10, 12, 10);
        cerradura.setFill(Color.web("#FFD700"));
        cerradura.setStroke(Color.web("#B8860B"));
        cerradura.setStrokeWidth(1);
        cerradura.setArcWidth(2);
        cerradura.setArcHeight(2);

        Circle ojoCerradura = new Circle(cofreX + 30, cofreY + 15, 2.5);
        ojoCerradura.setFill(Color.web("#1A1A1A"));

        // Tapa abierta (inclinada)
        Polygon tapa = new Polygon();
        tapa.getPoints().addAll(
                cofreX - 6.0,  cofreY,
                cofreX + 66.0, cofreY,
                cofreX + 56.0, cofreY - 28.0,
                cofreX + 4.0,  cofreY - 28.0
        );
        tapa.setFill(Color.web("#7A4222"));
        tapa.setStroke(Color.web("#3A1E0E"));
        tapa.setStrokeWidth(1.5);

        // Bisagras
        Circle bisagraIzq = new Circle(cofreX + 8, cofreY, 3);
        bisagraIzq.setFill(Color.web("#8B7355"));
        bisagraIzq.setStroke(Color.web("#5C4A33"));
        bisagraIzq.setStrokeWidth(0.5);

        Circle bisagraDer = new Circle(cofreX + 52, cofreY, 3);
        bisagraDer.setFill(Color.web("#8B7355"));
        bisagraDer.setStroke(Color.web("#5C4A33"));
        bisagraDer.setStrokeWidth(0.5);

        grupo.getChildren().addAll(cuerpo, refuerzoH, refuerzoV, cerradura, ojoCerradura, tapa, bisagraIzq, bisagraDer);

        return grupo;
    }

    // -----------------------------------------------------------------
    //  PANTALLA 1 — MENU DE INICIO
    // -----------------------------------------------------------------

    /**
     * Construye la pantalla de inicio con el titulo en tres lineas
     * (la primera con efecto arco simulado), y el boton Inicio con
     * globo terraqueo. Ningun otro elemento es interactivo.
     */
    private Pane mostrarPantallaInicio() {
        Pane panel = new Pane();
        panel.setPrefSize(ANCHO, ALTO);
        panel.setPickOnBounds(false);

        // ---- Titulo: "ESCAPE" en linea recta ----
        String palabra = "ESCAPE";
        double centroX = ANCHO / 2.0;
        double baseY = 150;
        double anchoLetra = 44;
        double inicioX = centroX - (palabra.length() * anchoLetra) / 2.0;

        for (int i = 0; i < palabra.length(); i++) {
            Text letra = new Text(String.valueOf(palabra.charAt(i)));
            letra.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 56));
            letra.setFill(MARRON_TITULO);
            letra.setY(baseY);
            letra.setX(inicioX + i * anchoLetra);
            DropShadow sombra = new DropShadow();
            sombra.setColor(Color.rgb(0, 0, 0, 0.8));
            sombra.setRadius(6);
            sombra.setOffsetX(2);
            sombra.setOffsetY(2);
            letra.setEffect(sombra);
            panel.getChildren().add(letra);
        }

        // ---- "DE LA" ----
        Text deLa = new Text("DE LA");
        deLa.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 48));
        deLa.setFill(MARRON_TITULO);
        deLa.setX((ANCHO - deLa.getLayoutBounds().getWidth()) / 2.0);
        deLa.setY(225);
        DropShadow sombra2 = new DropShadow();
        sombra2.setColor(Color.rgb(0, 0, 0, 0.8));
        sombra2.setRadius(6);
        sombra2.setOffsetX(2);
        sombra2.setOffsetY(2);
        deLa.setEffect(sombra2);
        panel.getChildren().add(deLa);

        // ---- "MAZMORRA" ----
        Text mazmorra = new Text("MAZMORRA");
        mazmorra.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 56));
        mazmorra.setFill(MARRON_TITULO);
        mazmorra.setX((ANCHO - mazmorra.getLayoutBounds().getWidth()) / 2.0);
        mazmorra.setY(290);
        DropShadow sombra3 = new DropShadow();
        sombra3.setColor(Color.rgb(0, 0, 0, 0.8));
        sombra3.setRadius(6);
        sombra3.setOffsetX(2);
        sombra3.setOffsetY(2);
        mazmorra.setEffect(sombra3);
        panel.getChildren().add(mazmorra);

        // ---- Boton Inicio ----
        Pane botonInicio = crearBotonInicio();
        botonInicio.setLayoutX(ANCHO / 2.0 - 110);
        botonInicio.setLayoutY(400);
        panel.getChildren().add(botonInicio);

        return panel;
    }

    /**
     * Boton "Inicio" con globo terraqueo. Unico elemento interactivo
     * de la pantalla de inicio. Cursor.HAND solo en este rectangulo.
     */
    private Pane crearBotonInicio() {
        double w = 220;
        double h = 56;

        Rectangle rect = new Rectangle(0, 0, w, h);
        rect.setFill(Color.web("#FFD700"));
        rect.setStroke(Color.web("#B8860B"));
        rect.setStrokeWidth(2.5);
        rect.setArcWidth(10);
        rect.setArcHeight(10);
        DropShadow sombraBoton = new DropShadow();
        sombraBoton.setColor(Color.rgb(0, 0, 0, 0.7));
        sombraBoton.setRadius(5);
        sombraBoton.setOffsetX(2);
        sombraBoton.setOffsetY(2);
        rect.setEffect(sombraBoton);

        // Borde interior decorativo
        Rectangle bordeInt = new Rectangle(4, 4, w - 8, h - 8);
        bordeInt.setFill(null);
        bordeInt.setStroke(Color.web("#DAA520"));
        bordeInt.setStrokeWidth(1);
        bordeInt.setArcWidth(6);
        bordeInt.setArcHeight(6);

        Text texto = new Text("Inicio");
        texto.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 24));
        texto.setFill(Color.web("#2A1A0E"));
        texto.setX(40);
        texto.setY(36);

        // Globo terraqueo
        Circle globo = new Circle(w - 45, 28, 14);
        globo.setFill(Color.web("#1E90FF"));
        globo.setStroke(Color.web("#005A9E"));
        globo.setStrokeWidth(1.5);

        // Continentes
        Polygon continente1 = new Polygon();
        continente1.getPoints().addAll(
                w - 56.0, 20.0,
                w - 48.0, 18.0,
                w - 44.0, 22.0,
                w - 46.0, 30.0,
                w - 54.0, 28.0,
                w - 56.0, 24.0
        );
        continente1.setFill(Color.web("#32CD32"));

        Polygon continente2 = new Polygon();
        continente2.getPoints().addAll(
                w - 38.0, 22.0,
                w - 34.0, 20.0,
                w - 32.0, 26.0,
                w - 36.0, 30.0,
                w - 38.0, 28.0
        );
        continente2.setFill(Color.web("#2E8B57"));

        Pane grupo = new Pane(rect, bordeInt, texto, globo, continente1, continente2);

        // Cursor mano solo sobre este boton
        grupo.setCursor(Cursor.HAND);

        // Hover
        grupo.setOnMouseEntered(e -> {
            rect.setFill(Color.web("#FFEC80"));
            ScaleTransition st = new ScaleTransition(Duration.millis(150), grupo);
            st.setToX(1.06);
            st.setToY(1.06);
            st.play();
        });
        grupo.setOnMouseExited(e -> {
            rect.setFill(Color.web("#FFD700"));
            ScaleTransition st = new ScaleTransition(Duration.millis(150), grupo);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        grupo.setOnMouseClicked(e -> cambiarAOpciones());
        return grupo;
    }

    // -----------------------------------------------------------------
    //  PANTALLA 2 — OPCIONES
    // -----------------------------------------------------------------

    private Pane mostrarPantallaOpciones() {
        Pane panel = new Pane();
        panel.setPrefSize(ANCHO, ALTO);
        panel.setPickOnBounds(false);

        // Titulo centrado dinamicamente
        Text titulo = new Text("ESCAPE DE LA MAZMORRA");
        titulo.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 34));
        titulo.setFill(MARRON_TITULO);
        titulo.setY(120);
        DropShadow sombraT = new DropShadow();
        sombraT.setColor(Color.rgb(0, 0, 0, 0.8));
        sombraT.setRadius(6);
        sombraT.setOffsetX(2);
        sombraT.setOffsetY(2);
        titulo.setEffect(sombraT);
        titulo.setX((ANCHO - titulo.getLayoutBounds().getWidth()) / 2.0);
        panel.getChildren().add(titulo);

        // Botonera centrada con VBox
        VBox vbox = new VBox(25);
        vbox.setAlignment(Pos.CENTER);
        vbox.setLayoutX(ANCHO / 2.0 - 130);
        vbox.setLayoutY(240);

        vbox.getChildren().add(crearBotonOpcion("Iniciar partida", e -> {
            try {
                partida = modelo.juego.Partida.crearPartidaNueva();
                mostrarIntroduccion();
            } catch (Exception ex) {
                System.err.println("Error al crear partida: " + ex.getMessage());
                ex.printStackTrace();
            }
        }));
        vbox.getChildren().add(crearBotonOpcion("Estadisticas", e -> {
            System.out.println("Estadisticas");
        }));
        vbox.getChildren().add(crearBotonOpcion("Ajustes", e -> {
            cambiarAInicio();
        }));

        panel.getChildren().add(vbox);
        return panel;
    }

    /**
     * Boton con aspecto de madera/pergamino (#C89D65), bordes oscuros
     * irregulares simulados, y efecto hover con escala y oscurecimiento.
     */
    private Pane crearBotonOpcion(String texto, javafx.event.EventHandler<javafx.scene.input.MouseEvent> accion) {
        double w = 260;
        double h = 60;

        Rectangle rect = new Rectangle(0, 0, w, h);
        rect.setFill(Color.web("#C89D65"));
        rect.setStroke(Color.web("#5C3A1E"));
        rect.setStrokeWidth(2.5);
        rect.setArcWidth(6);
        rect.setArcHeight(6);

        // Sombra del boton
        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.7));
        sombra.setRadius(6);
        sombra.setOffsetX(2);
        sombra.setOffsetY(2);
        rect.setEffect(sombra);

        // Borde interior decorativo (simula irregularidad)
        Rectangle bordeInt = new Rectangle(5, 5, w - 10, h - 10);
        bordeInt.setFill(null);
        bordeInt.setStroke(Color.web("#7A5025"));
        bordeInt.setStrokeWidth(1.2);
        bordeInt.setArcWidth(4);
        bordeInt.setArcHeight(4);

        // Esquinas simulando remaches
        Circle[] remaches = new Circle[4];
        double[][] posRem = {{8, 8}, {w - 8, 8}, {8, h - 8}, {w - 8, h - 8}};
        for (int i = 0; i < 4; i++) {
            remaches[i] = new Circle(posRem[i][0], posRem[i][1], 2.5);
            remaches[i].setFill(Color.web("#8B7355"));
            remaches[i].setStroke(Color.web("#5C4A33"));
            remaches[i].setStrokeWidth(0.5);
        }

        Text label = new Text(texto);
        label.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 20));
        label.setFill(Color.web("#2A1A0E"));
        label.setX(20);
        label.setY(38);

        Pane grupo = new Pane(rect);
        grupo.getChildren().add(bordeInt);
        grupo.getChildren().addAll(remaches);
        grupo.getChildren().add(label);

        grupo.setCursor(Cursor.HAND);

        grupo.setOnMouseEntered(e -> {
            rect.setFill(Color.web("#B88950"));
            ScaleTransition st = new ScaleTransition(Duration.millis(150), grupo);
            st.setToX(1.06);
            st.setToY(1.06);
            st.play();
        });
        grupo.setOnMouseExited(e -> {
            rect.setFill(Color.web("#C89D65"));
            ScaleTransition st = new ScaleTransition(Duration.millis(150), grupo);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        grupo.setOnMouseClicked(accion);
        return grupo;
    }

    // -----------------------------------------------------------------
    //  NAVEGACION CON FADE TRANSITION
    // -----------------------------------------------------------------

    private void cambiarAOpciones() {
        animarTransicion(mostrarPantallaOpciones());
    }

    private void cambiarAInicio() {
        animarTransicion(mostrarPantallaInicio());
    }

    private void animarTransicion(Pane nuevaPantalla) {
        FadeTransition salida = new FadeTransition(Duration.millis(350), contenidoActual);
        salida.setFromValue(1.0);
        salida.setToValue(0.0);
        salida.setOnFinished(e -> {
            raiz.getChildren().remove(contenidoActual);
            contenidoActual = nuevaPantalla;
            contenidoActual.setOpacity(0.0);
            raiz.getChildren().add(contenidoActual);

            FadeTransition entrada = new FadeTransition(Duration.millis(350), contenidoActual);
            entrada.setFromValue(0.0);
            entrada.setToValue(1.0);
            entrada.play();
        });
        salida.play();
    }

    // -----------------------------------------------------------------
    //  FLUJO NARRATIVO — Introduccion, Transicion, Juego, Final
    // -----------------------------------------------------------------

    /**
     * Muestra la pantalla de introduccion con la historia inicial.
     */
    private void mostrarIntroduccion() {
        PantallaIntroduccion intro = new PantallaIntroduccion(() -> {
            mostrarTransicion("cueva_facil");
        });
        Scene scene = intro.crearScene();
        scene.setOnMouseClicked(e -> scene.getRoot().requestFocus());
        stage.setScene(scene);
    }

    /**
     * Muestra la pantalla de transicion correspondiente a una cueva.
     *
     * Si es la primera cueva (cueva_facil), al pulsar "Entrar" solo
     * muestra el juego porque la partida ya esta inicializada con ella.
     * Para las siguientes, llama a partida.cambiarCueva() antes de
     * mostrar el juego.
     */
    private void mostrarTransicion(String cuevaId) {
        DatosTemaCueva tema = DatosTemaCueva.paraCuevaId(cuevaId);
        PantallaTransicion transicion = new PantallaTransicion(tema, () -> {
            // Si no es la primera cueva, avanzar la partida
            if (!"cueva_facil".equals(cuevaId)) {
                partida.cambiarCueva();
            }
            mostrarJuego();
        });
        Scene scene = transicion.crearScene();
        scene.setOnMouseClicked(e -> scene.getRoot().requestFocus());
        stage.setScene(scene);
    }

    /**
     * Muestra la pantalla de juego (PantallaJuego) vinculada a la
     * partida actual. Configura los callbacks para cambio de cueva
     * y fin de partida.
     */
    private void mostrarJuego() {
        PantallaJuego pj = new PantallaJuego(partida, stage, this::volverAlMenu);

        // Callback al cambiar de cueva: mostrar transicion a la siguiente
        pj.setAlCambiarCueva(() -> {
            String siguienteId = partida.getSiguienteCuevaId();
            if (siguienteId != null) {
                mostrarTransicion(siguienteId);
            }
        });

        // Callback al terminar la partida: mostrar pantalla final
        pj.setAlTerminarPartida(() -> {
            boolean victoria = partida.getEstado() == modelo.juego.EstadoPartida.VICTORIA;
            mostrarFinal(victoria);
        });

        Scene gameScene = pj.crearScene();
        stage.setScene(gameScene);
    }

    /**
     * Muestra la pantalla de victoria o derrota segun el resultado.
     */
    private void mostrarFinal(boolean victoria) {
        PantallaFinal pantallaFinal = new PantallaFinal(victoria, this::volverAlMenu);
        Scene scene = pantallaFinal.crearScene();
        scene.setOnMouseClicked(e -> scene.getRoot().requestFocus());
        stage.setScene(scene);
    }

    /**
     * Vuelve al menu principal desde la pantalla de juego.
     * Reconstruye la escena del menu.
     */
    private void volverAlMenu() {
        raiz = new StackPane();
        raiz.getChildren().add(crearFondoCueva());

        Pane antorchaIzq = crearAntorchaCompleta(160, 260);
        Pane antorchaDer = crearAntorchaCompleta(ANCHO - 160, 260);
        raiz.getChildren().addAll(antorchaIzq, antorchaDer);
        raiz.getChildren().add(crearTesoro());

        contenidoActual = mostrarPantallaInicio();
        raiz.getChildren().add(contenidoActual);

        Scene menuScene = new Scene(raiz, ANCHO, ALTO);
        stage.setScene(menuScene);
        animacionIzquierda.play();
        animacionDerecha.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
