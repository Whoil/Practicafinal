package control;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
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
import java.io.File;
import java.io.IOException;

import json.ResultadoPartidaDTO;
import json.SerializadorRanking;
import vista.ReproductorMusica;

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
    private Pane tesoroDecorativo;
    private Stage stage;

    private ControladorFlujo flujo;

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
        tesoroDecorativo = crearTesoro();
        raiz.getChildren().add(tesoroDecorativo);

        // Capa 4 — Contenido intercambiable
        contenidoActual = mostrarPantallaInicio();
        raiz.getChildren().add(contenidoActual);

        // Iniciar musica de fondo
        ReproductorMusica.getInstancia().reproducir();

        flujo = new ControladorFlujo(stage, this::volverAlMenu);
        this.stage = stage;
        Scene escena = new Scene(raiz, ANCHO, ALTO);
        stage.setTitle("ESCAPE DE LA MAZMORRA");
        stage.setScene(escena);
        fijarVentanaMenu(stage);
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

    private Pane crearAmbienteInicio() {
        Pane ambiente = new Pane();
        ambiente.setPrefSize(ANCHO, ALTO);
        ambiente.setMouseTransparent(true);

        Rectangle hazLuz = new Rectangle(ANCHO, ALTO);
        hazLuz.setFill(new RadialGradient(
                0, 0, 0.5, 0.36, 0.52, true, CycleMethod.NO_CYCLE,
                new Stop(0.00, Color.rgb(214, 158, 72, 0.22)),
                new Stop(0.34, Color.rgb(111, 70, 33, 0.12)),
                new Stop(0.72, Color.rgb(0, 0, 0, 0.0)),
                new Stop(1.00, Color.rgb(0, 0, 0, 0.35))
        ));

        Rectangle vineta = new Rectangle(ANCHO, ALTO);
        vineta.setFill(new RadialGradient(
                0, 0, 0.5, 0.5, 0.82, true, CycleMethod.NO_CYCLE,
                new Stop(0.00, Color.rgb(0, 0, 0, 0.0)),
                new Stop(0.58, Color.rgb(0, 0, 0, 0.12)),
                new Stop(1.00, Color.rgb(0, 0, 0, 0.68))
        ));

        ambiente.getChildren().addAll(hazLuz, vineta);
        for (int i = 0; i < 28; i++) {
            double x = 340 + (i * 43) % 600;
            double y = 118 + (i * 67) % 300;
            double r = 1.2 + (i % 4) * 0.45;
            Circle chispa = new Circle(x, y, r);
            chispa.setFill(Color.rgb(255, 198, 91, 0.24 + (i % 3) * 0.09));
            chispa.setMouseTransparent(true);
            ambiente.getChildren().add(chispa);
        }
        return ambiente;
    }

    private Pane crearVignetaInicioPixel() {
        Pane ambiente = new Pane();
        ambiente.setPrefSize(ANCHO, ALTO);
        ambiente.setMouseTransparent(true);

        Rectangle oscuridad = new Rectangle(ANCHO, ALTO);
        oscuridad.setFill(Color.rgb(0, 0, 0, 0.18));

        Rectangle foco = new Rectangle(ANCHO, ALTO);
        foco.setFill(new RadialGradient(
                0, 0, 0.5, 0.38, 0.72, true, CycleMethod.NO_CYCLE,
                new Stop(0.00, Color.rgb(210, 154, 74, 0.17)),
                new Stop(0.45, Color.rgb(59, 35, 18, 0.08)),
                new Stop(1.00, Color.rgb(0, 0, 0, 0.58))
        ));

        ambiente.getChildren().addAll(oscuridad, foco);
        return ambiente;
    }

    private Pane crearArcoMazmorra() {
        Pane arco = new Pane();
        arco.setPrefSize(540, 360);
        arco.setMouseTransparent(true);

        Rectangle pilarIzq = new Rectangle(28, 140, 82, 220);
        Rectangle pilarDer = new Rectangle(430, 140, 82, 220);
        Rectangle dintel = new Rectangle(80, 60, 380, 105);
        Rectangle hueco = new Rectangle(132, 116, 276, 244);

        Color piedra = Color.rgb(28, 26, 24, 0.62);
        Color borde = Color.rgb(126, 95, 54, 0.28);
        pilarIzq.setFill(piedra);
        pilarDer.setFill(piedra);
        dintel.setFill(Color.rgb(35, 31, 28, 0.62));
        hueco.setFill(Color.rgb(5, 5, 5, 0.28));

        for (Rectangle r : new Rectangle[] {pilarIzq, pilarDer, dintel, hueco}) {
            r.setStroke(borde);
            r.setStrokeWidth(2);
            r.setArcWidth(10);
            r.setArcHeight(10);
        }

        arco.getChildren().addAll(pilarIzq, pilarDer, dintel, hueco);
        return arco;
    }

    private Pane crearMarcoDecorativo(double ancho, double alto, Color relleno) {
        Pane marco = new Pane();
        marco.setPrefSize(ancho, alto);
        marco.setMouseTransparent(true);

        Rectangle fondo = new Rectangle(0, 0, ancho, alto);
        fondo.setFill(relleno);
        fondo.setStroke(Color.rgb(124, 84, 42, 0.75));
        fondo.setStrokeWidth(3);
        fondo.setArcWidth(12);
        fondo.setArcHeight(12);

        Rectangle interior = new Rectangle(12, 12, ancho - 24, alto - 24);
        interior.setFill(null);
        interior.setStroke(Color.rgb(218, 165, 32, 0.38));
        interior.setStrokeWidth(1.4);
        interior.setArcWidth(8);
        interior.setArcHeight(8);

        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.82));
        sombra.setRadius(18);
        sombra.setOffsetY(5);
        fondo.setEffect(sombra);

        marco.getChildren().addAll(fondo, interior);
        return marco;
    }

    private Text crearTituloPremium(String texto, int tamano) {
        Text titulo = new Text(texto);
        titulo.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, tamano));
        titulo.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.00, Color.web("#FFF0A6")),
                new Stop(0.38, Color.web("#D8A13B")),
                new Stop(1.00, Color.web("#6E4720"))
        ));
        titulo.setStroke(Color.rgb(38, 21, 9, 0.85));
        titulo.setStrokeWidth(Math.max(0.7, tamano / 42.0));

        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.92));
        sombra.setRadius(8);
        sombra.setOffsetX(3);
        sombra.setOffsetY(4);

        DropShadow brillo = new DropShadow();
        brillo.setColor(Color.rgb(255, 190, 75, 0.55));
        brillo.setRadius(12);
        brillo.setSpread(0.16);
        brillo.setInput(sombra);
        titulo.setEffect(brillo);
        return titulo;
    }

    private Pane crearSeparadorDorado(double ancho) {
        Pane sep = new Pane();
        sep.setPrefSize(ancho, 16);
        sep.setMouseTransparent(true);

        Rectangle linea = new Rectangle(28, 7, ancho - 56, 2);
        linea.setFill(Color.rgb(218, 165, 32, 0.72));

        Circle izq = new Circle(14, 8, 5);
        Circle der = new Circle(ancho - 14, 8, 5);
        izq.setFill(Color.rgb(218, 165, 32, 0.82));
        der.setFill(Color.rgb(218, 165, 32, 0.82));
        izq.setStroke(Color.rgb(75, 43, 16));
        der.setStroke(Color.rgb(75, 43, 16));

        sep.getChildren().addAll(linea, izq, der);
        return sep;
    }

    private Pane crearBotonPremium(String texto, double w, double h,
            javafx.event.EventHandler<javafx.scene.input.MouseEvent> accion) {
        Rectangle rect = new Rectangle(0, 0, w, h);
        rect.setFill(Color.web("#C89D65"));
        rect.setStroke(Color.web("#4B2D16"));
        rect.setStrokeWidth(2.4);
        rect.setArcWidth(6);
        rect.setArcHeight(6);

        Rectangle bordeInt = new Rectangle(6, 6, w - 12, h - 12);
        bordeInt.setFill(null);
        bordeInt.setStroke(Color.web("#7A5025"));
        bordeInt.setStrokeWidth(1.1);
        bordeInt.setArcWidth(4);
        bordeInt.setArcHeight(4);

        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.72));
        sombra.setRadius(6);
        sombra.setOffsetX(2);
        sombra.setOffsetY(3);
        rect.setEffect(sombra);

        Text label = new Text(texto);
        label.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, h >= 60 ? 23 : 20));
        label.setFill(Color.web("#2A1A0E"));
        label.setX((w - label.getLayoutBounds().getWidth()) / 2.0);
        label.setY(h / 2.0 + label.getLayoutBounds().getHeight() / 3.0);

        Circle remacheIzq = new Circle(14, h / 2.0, 2.8);
        Circle remacheDer = new Circle(w - 14, h / 2.0, 2.8);
        remacheIzq.setFill(Color.web("#8B7355"));
        remacheDer.setFill(Color.web("#8B7355"));
        remacheIzq.setStroke(Color.web("#4B2D16"));
        remacheDer.setStroke(Color.web("#4B2D16"));

        Pane grupo = new Pane(rect, bordeInt, remacheIzq, remacheDer, label);
        grupo.setPrefSize(w, h);
        grupo.setCursor(Cursor.HAND);
        grupo.setOnMouseEntered(e -> {
            rect.setFill(Color.web("#D8B178"));
            ScaleTransition st = new ScaleTransition(Duration.millis(150), grupo);
            st.setToX(1.035);
            st.setToY(1.035);
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

    private ImageView crearAssetPixelMenu(String ruta, double ancho, double alto) {
        try {
            File archivo = new File(ruta);
            if (!archivo.exists()) {
                return new ImageView();
            }
            Image img = new Image(archivo.toURI().toString());
            ImageView view = new ImageView(img);
            double imgW = img.getWidth();
            double imgH = img.getHeight();
            if (imgW > imgH * 2.5) {
                double frameW = imgW / 4.0;
                view.setViewport(new javafx.geometry.Rectangle2D(0, 0, frameW, imgH));
                view.setFitWidth(Math.min(ancho, frameW * (alto / imgH)));
                view.setFitHeight(alto);
            } else {
                view.setFitWidth(ancho);
                view.setFitHeight(alto);
            }
            view.setPreserveRatio(true);
            view.setSmooth(false);
            view.setMouseTransparent(true);
            return view;
        } catch (RuntimeException ex) {
            return new ImageView();
        }
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

    private Pane mostrarPantallaInicio() {
        mostrarTesoroDecorativo(true);
        Pane panel = new Pane();
        panel.setPrefSize(ANCHO, ALTO);
        panel.setPickOnBounds(false);

        // ---- Titulo: "ESCAPE" ----
        Text escape = new Text("ESCAPE");
        escape.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 56));
        escape.setFill(MARRON_TITULO);
        escape.setX((ANCHO - escape.getLayoutBounds().getWidth()) / 2.0);
        escape.setY(150);
        DropShadow sombra1 = new DropShadow();
        sombra1.setColor(Color.rgb(0, 0, 0, 0.8));
        sombra1.setRadius(6);
        sombra1.setOffsetX(2);
        sombra1.setOffsetY(2);
        escape.setEffect(sombra1);
        panel.getChildren().add(escape);

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

        Pane botonInicio = crearBotonInicio();
        botonInicio.setLayoutX(ANCHO / 2.0 - 110);
        botonInicio.setLayoutY(400);

        panel.getChildren().add(botonInicio);

        return panel;
    }

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

        Circle globo = new Circle(w - 45, 28, 14);
        globo.setFill(Color.web("#1E90FF"));
        globo.setStroke(Color.web("#005A9E"));
        globo.setStrokeWidth(1.5);

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
        grupo.setCursor(Cursor.HAND);

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
        mostrarTesoroDecorativo(true);
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
        vbox.setLayoutY(190);

        vbox.getChildren().add(crearBotonOpcion("Iniciar partida", e -> {
            mostrarDialogoNombreJugador();
        }));
        vbox.getChildren().add(crearBotonOpcion("Cargar partida", e -> {
            try {
                modelo.juego.Partida p = modelo.juego.Partida.cargarPartida("datos/partida_guardada.json");
                flujo.setPartida(p);
                flujo.setNombreJugador(p.getJugador().getNombre());
                flujo.mostrarIntroduccion();
            } catch (Exception ex) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Error al cargar");
                alerta.setHeaderText("No se pudo cargar la partida");
                alerta.setContentText("No existe una partida guardada en datos/partida_guardada.json.\n\n" +
                    "Primero debes jugar y guardar la partida desde el menu de juego.");
                alerta.showAndWait();
            }
        }));
        vbox.getChildren().add(crearBotonOpcion("Controles", e -> {
            animarTransicion(mostrarPantallaControles());
        }));
        vbox.getChildren().add(crearBotonOpcion("Ranking", e -> {
            animarTransicion(mostrarPantallaRanking());
        }));
        vbox.getChildren().add(crearBotonOpcion("Salir", e -> {
            Platform.exit();
        }));

        panel.getChildren().add(vbox);
        return panel;
    }

    private void mostrarDialogoNombreJugador() {
        StackPane overlay = new StackPane();
        overlay.setPrefSize(ANCHO, ALTO);
        overlay.setPickOnBounds(true);

        Rectangle velo = new Rectangle(ANCHO, ALTO);
        velo.setFill(Color.rgb(0, 0, 0, 0.62));

        Pane pergamino = new Pane();
        pergamino.setPrefSize(540, 300);
        pergamino.setMaxSize(540, 300);

        Rectangle fondo = new Rectangle(0, 0, 540, 300);
        fondo.setFill(Color.web("#C89D65"));
        fondo.setStroke(Color.web("#5C3A1E"));
        fondo.setStrokeWidth(3);
        fondo.setArcWidth(10);
        fondo.setArcHeight(10);

        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.8));
        sombra.setRadius(14);
        sombra.setOffsetX(4);
        sombra.setOffsetY(5);
        fondo.setEffect(sombra);

        Rectangle bordeInterior = new Rectangle(12, 12, 516, 276);
        bordeInterior.setFill(null);
        bordeInterior.setStroke(Color.web("#7A5025"));
        bordeInterior.setStrokeWidth(1.5);
        bordeInterior.setArcWidth(8);
        bordeInterior.setArcHeight(8);

        Text titulo = new Text("Nombre del Mago");
        titulo.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 30));
        titulo.setFill(Color.web("#2A1A0E"));
        titulo.setX((540 - titulo.getLayoutBounds().getWidth()) / 2.0);
        titulo.setY(72);

        Text indicacion = new Text("Introduce tu nombre de Mago:");
        indicacion.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 19));
        indicacion.setFill(Color.web("#3A2412"));
        indicacion.setX((540 - indicacion.getLayoutBounds().getWidth()) / 2.0);
        indicacion.setY(120);

        TextField campoNombre = new TextField();
        campoNombre.setPromptText("Mago Errante");
        campoNombre.setPrefSize(350, 44);
        campoNombre.setLayoutX(95);
        campoNombre.setLayoutY(142);
        campoNombre.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 18));
        campoNombre.setStyle(
            "-fx-background-color: #F1D6A5;" +
            "-fx-text-fill: #2A1A0E;" +
            "-fx-prompt-text-fill: #7A5025;" +
            "-fx-border-color: #5C3A1E;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-highlight-fill: #B88950;"
        );

        Pane botonComenzar = crearBotonDialogo("Comenzar", e -> {
            iniciarPartidaNuevaDesdeNombre(campoNombre.getText(), overlay);
        });
        botonComenzar.setLayoutX(80);
        botonComenzar.setLayoutY(220);

        Pane botonCancelar = crearBotonDialogo("Cancelar", e -> {
            iniciarPartidaNuevaDesdeNombre(null, overlay);
        });
        botonCancelar.setLayoutX(280);
        botonCancelar.setLayoutY(220);

        pergamino.getChildren().addAll(
            fondo, bordeInterior, titulo, indicacion, campoNombre, botonComenzar, botonCancelar
        );

        overlay.getChildren().addAll(velo, pergamino);
        overlay.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                iniciarPartidaNuevaDesdeNombre(null, overlay);
            }
        });
        campoNombre.setOnAction(e -> iniciarPartidaNuevaDesdeNombre(campoNombre.getText(), overlay));

        raiz.getChildren().add(overlay);
        Platform.runLater(() -> {
            overlay.requestFocus();
            campoNombre.requestFocus();
        });
    }

    private void iniciarPartidaNuevaDesdeNombre(String nombreIntroducido, StackPane overlay) {
        if (overlay != null) {
            raiz.getChildren().remove(overlay);
        }

        String nombre = (nombreIntroducido == null || nombreIntroducido.trim().isEmpty())
            ? "Mago Errante" : nombreIntroducido.trim();

        try {
            modelo.juego.Partida p = modelo.juego.Partida.crearPartidaNueva(nombre);
            flujo.setPartida(p);
            flujo.setNombreJugador(nombre);
            flujo.mostrarIntroduccion();
        } catch (Exception ex) {
            System.err.println("Error al crear partida: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private Pane crearBotonDialogo(String texto, javafx.event.EventHandler<javafx.scene.input.MouseEvent> accion) {
        double w = 180;
        double h = 48;

        Rectangle rect = new Rectangle(0, 0, w, h);
        rect.setFill(Color.web("#5C3A1E"));
        rect.setStroke(Color.web("#2A1A0E"));
        rect.setStrokeWidth(2);
        rect.setArcWidth(7);
        rect.setArcHeight(7);

        Rectangle bordeInt = new Rectangle(5, 5, w - 10, h - 10);
        bordeInt.setFill(null);
        bordeInt.setStroke(Color.web("#C89D65"));
        bordeInt.setStrokeWidth(1);
        bordeInt.setArcWidth(5);
        bordeInt.setArcHeight(5);

        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.65));
        sombra.setRadius(5);
        sombra.setOffsetX(2);
        sombra.setOffsetY(2);
        rect.setEffect(sombra);

        Text label = new Text(texto);
        label.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 18));
        label.setFill(Color.web("#F1D6A5"));
        label.setX((w - label.getLayoutBounds().getWidth()) / 2.0);
        label.setY(31);

        Pane grupo = new Pane(rect, bordeInt, label);
        grupo.setCursor(Cursor.HAND);
        grupo.setOnMouseEntered(e -> rect.setFill(Color.web("#7A5025")));
        grupo.setOnMouseExited(e -> rect.setFill(Color.web("#5C3A1E")));
        grupo.setOnMouseClicked(accion);
        return grupo;
    }

    private Pane crearBotonOpcion(String texto, javafx.event.EventHandler<javafx.scene.input.MouseEvent> accion) {
        return crearBotonPremium(texto, 260, 60, accion);
    }

    // -----------------------------------------------------------------
    //  PANTALLA 3 — CONTROLES
    // -----------------------------------------------------------------

    /**
     * Panel informativo con los controles basicos y la estructura del juego.
     * Se accede desde el boton "Controles" del menu de opciones.
     */
    private Pane mostrarPantallaControles() {
        mostrarTesoroDecorativo(false);
        Pane panel = new Pane();
        panel.setPrefSize(ANCHO, ALTO);
        panel.setPickOnBounds(false);

        Text titulo = new Text("CONTROLES");
        titulo.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 40));
        titulo.setFill(MARRON_TITULO);
        titulo.setX((ANCHO - titulo.getLayoutBounds().getWidth()) / 2.0);
        titulo.setY(80);
        DropShadow sombraT = new DropShadow();
        sombraT.setColor(Color.rgb(0, 0, 0, 0.8));
        sombraT.setRadius(6);
        sombraT.setOffsetX(2);
        sombraT.setOffsetY(2);
        titulo.setEffect(sombraT);
        panel.getChildren().add(titulo);

        String[] lineas = {
            "MOVIMIENTO:     WASD / Flechas",
            "ATACAR:         SPACE (barra espaciadora)",
            "RECOGER OBJETO: R",
            "TERMINAR TURNO: T",
            "ABRIR PUERTA:   Estando en celda de puerta con la llave",
            "CAMBIAR CUEVA:  Pulsar el boton CAMBIAR CUEVA en pantalla",
            "",
            "ESTRUCTURA DEL JUEGO",
            "",
            "El mago debe atravesar 3 cuevas de dificultad creciente:",
            "  LAS CRIPTAS DE MARFIL (facil)",
            "  EL PARAMO PUTREFACTO (media)",
            "  EL ABISMO DE MALAKOR (dificil)",
            "",
            "En cada cueva debera derrotar enemigos, conseguir llaves",
            "y llegar a la puerta de salida para avanzar a la siguiente.",
            "Para ganar la partida: derrota al boss final, consigue la",
            "llave maestra y sal por la puerta de SALIDA."
        };

        VBox texto = new VBox(4);
        texto.setLayoutX(0);
        texto.setLayoutY(120);
        for (String linea : lineas) {
            Text t = new Text(linea);
            t.setFont(Font.font(FONT_FAMILY, FontWeight.NORMAL, 14));
            t.setFill(Color.web("#C89D65"));
            HBox fila = new HBox(t);
            fila.setAlignment(Pos.CENTER);
            fila.setPrefWidth(ANCHO);
            texto.getChildren().add(fila);
        }
        panel.getChildren().add(texto);

        Pane botonVolver = crearBotonOpcion("Volver", e -> {
            animarTransicion(mostrarPantallaOpciones());
        });
        botonVolver.setLayoutX(ANCHO / 2.0 - 130);
        botonVolver.setLayoutY(620);
        panel.getChildren().add(botonVolver);

        return panel;
    }

    private Pane mostrarPantallaRanking() {
        mostrarTesoroDecorativo(false);
        Pane panel = new Pane();
        panel.setPrefSize(ANCHO, ALTO);
        panel.setPickOnBounds(false);

        Text titulo = new Text("RANKING");
        titulo.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 42));
        titulo.setFill(MARRON_TITULO);
        titulo.setY(80);
        DropShadow sombraT = new DropShadow();
        sombraT.setColor(Color.rgb(0, 0, 0, 0.8));
        sombraT.setRadius(6);
        sombraT.setOffsetX(2);
        sombraT.setOffsetY(2);
        titulo.setEffect(sombraT);
        titulo.setX((ANCHO - titulo.getLayoutBounds().getWidth()) / 2.0);
        panel.getChildren().add(titulo);

        VBox tabla = new VBox(8);
        tabla.setLayoutX(210);
        tabla.setLayoutY(120);
        tabla.setPrefWidth(860);
        tabla.getChildren().add(crearFilaRanking("Puesto", "Nombre", "Puntuación", "Título", true));

        try {
            ResultadoPartidaDTO[] top = SerializadorRanking.obtenerTop10();
            if (top.length == 0) {
                tabla.getChildren().add(crearFilaRanking("-", "Sin partidas registradas", "-", "-", false));
            } else {
                for (int i = 0; i < top.length; i++) {
                    ResultadoPartidaDTO resultado = top[i];
                    tabla.getChildren().add(crearFilaRanking(
                            String.valueOf(i + 1),
                            resultado.getNombreJugador(),
                            String.valueOf(resultado.getPuntuacion()),
                            resultado.getTitulo(),
                            false));
                }
            }
        } catch (IOException ex) {
            tabla.getChildren().add(crearFilaRanking("-", "No se pudo leer ranking.json", "-", "-", false));
        }
        panel.getChildren().add(tabla);

        Pane botonVolver = crearBotonOpcion("Volver al Menú", e -> {
            animarTransicion(mostrarPantallaOpciones());
        });
        botonVolver.setLayoutX(ANCHO / 2.0 - 130);
        botonVolver.setLayoutY(625);
        panel.getChildren().add(botonVolver);
        return panel;
    }

    private HBox crearFilaRanking(String puesto, String nombre, String puntuacion,
                                  String titulo, boolean cabecera) {
        HBox fila = new HBox(12);
        fila.setAlignment(Pos.CENTER_LEFT);
        fila.setPrefWidth(860);
        fila.setMinHeight(42);
        fila.setStyle(cabecera
                ? "-fx-background-color: rgba(212,175,55,0.25); -fx-border-color: #d4af37;"
                : "-fx-background-color: rgba(20,12,6,0.70); -fx-border-color: rgba(200,157,101,0.55);");
        fila.getChildren().add(crearTextoCelda(puesto, 80, cabecera));
        fila.getChildren().add(crearTextoCelda(nombre, 230, cabecera));
        fila.getChildren().add(crearTextoCelda(puntuacion, 130, cabecera));
        fila.getChildren().add(crearTextoCelda(titulo, 360, cabecera));
        return fila;
    }

    private Text crearTextoCelda(String texto, double ancho, boolean cabecera) {
        Text t = new Text(texto != null ? texto : "");
        t.setFont(Font.font(FONT_FAMILY, cabecera ? FontWeight.BOLD : FontWeight.NORMAL, cabecera ? 17 : 15));
        t.setFill(cabecera ? Color.web("#f3d37a") : Color.web("#f5f0df"));
        t.setWrappingWidth(ancho);
        return t;
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

    private void mostrarTesoroDecorativo(boolean visible) {
        if (tesoroDecorativo != null) {
            tesoroDecorativo.setVisible(visible);
        }
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
        tesoroDecorativo = crearTesoro();
        raiz.getChildren().add(tesoroDecorativo);

        contenidoActual = mostrarPantallaInicio();
        raiz.getChildren().add(contenidoActual);

        Scene menuScene = new Scene(raiz, ANCHO, ALTO);
        stage.setScene(menuScene);
        fijarVentanaMenu(stage);
        animacionIzquierda.play();
        animacionDerecha.play();
    }

    /**
     * Mantiene las pantallas de menu en un tamano fijo. Si el usuario viene de
     * una ventana maximizada o pantalla completa, se restaura antes de pintar.
     */
    private void fijarVentanaMenu(Stage stage) {
        stage.setFullScreen(false);
        stage.setMaximized(false);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
