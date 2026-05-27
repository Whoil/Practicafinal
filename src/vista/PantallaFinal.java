package vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import Estructuras.ListaSE;
import javafx.scene.control.ScrollPane;
import json.ResultadoPartidaDTO;

/**
 * Pantalla de desenlace — Victoria o Derrota.
 *
 * Se crea con un booleano que indica si el jugador gano o perdio,
 * muestra el texto narrativo correspondiente y un boton para volver
 * al menu principal.
 */
public class PantallaFinal {

    private static final int ANCHO = 1280;
    private static final int ALTO = 720;
    private static final String FONT = "Georgia, serif";

    private final boolean victoria;
    private final ResultadoPartidaDTO resultado;
    private final Runnable volverAlMenu;
    private final ListaSE<String> log;
    private Scene scene;

    /**
     * @param victoria     true para pantalla de victoria, false para derrota
     * @param resultado    datos estadisticos de la partida
     * @param log          registro de eventos de la partida
     * @param volverAlMenu callback al pulsar "Volver al Menu Principal"
     */
    public PantallaFinal(boolean victoria, ResultadoPartidaDTO resultado, ListaSE<String> log, Runnable volverAlMenu) {
        this.victoria = victoria;
        this.resultado = resultado;
        this.log = log;
        this.volverAlMenu = volverAlMenu;
    }

    /**
     * Construye y devuelve la Scene final segun el resultado.
     */
    public Scene crearScene() {
        StackPane raiz = new StackPane();
        raiz.setStyle("-fx-background-color: #000000;");

        // Fondo segun resultado
        StackPane fondo = new StackPane();
        if (victoria) {
            fondo.setStyle(
                "-fx-background-color: radial-gradient(center 50% 50%, radius 60%, #2a2a1a 0%, #0a0a00 100%);"
            );
        } else {
            fondo.setStyle(
                "-fx-background-color: radial-gradient(center 50% 50%, radius 60%, #1a0a0a 0%, #000000 100%);"
            );
        }
        fondo.setPrefSize(ANCHO, ALTO);

        // Bloque centrado
        VBox bloque = new VBox(25);
        bloque.setMaxWidth(800);
        bloque.setAlignment(Pos.CENTER);
        bloque.setPadding(new Insets(60));

        // Titulo
        Text titulo = new Text(victoria ? "VICTORIA" : "HAS MUERTO");
        titulo.setFont(Font.font(FONT, FontWeight.BOLD, 48));
        titulo.setFill(victoria ? Color.web("#d4af37") : Color.web("#8b0000"));
        titulo.setTextAlignment(TextAlignment.CENTER);

        DropShadow sombraTitulo = new DropShadow();
        sombraTitulo.setColor(Color.rgb(0, 0, 0, 0.8));
        sombraTitulo.setRadius(6);
        titulo.setEffect(sombraTitulo);

        // Texto narrativo
        Text cuerpo = new Text(obtenerTexto() + "\n\n" + obtenerTextoEstadisticas());
        cuerpo.setFont(Font.font(FONT, FontWeight.NORMAL, 18));
        cuerpo.setFill(Color.web("#f5f5f5"));
        cuerpo.setTextAlignment(TextAlignment.CENTER);
        cuerpo.setLineSpacing(6);
        cuerpo.setWrappingWidth(680);

        DropShadow sombraCuerpo = new DropShadow();
        sombraCuerpo.setColor(Color.rgb(0, 0, 0, 0.7));
        sombraCuerpo.setRadius(3);
        cuerpo.setEffect(sombraCuerpo);

        // Boton volver al menu
        StackPane btnVolver = crearBotonVolver();

        bloque.getChildren().addAll(titulo, cuerpo, btnVolver);

        // Log de eventos - siempre visible
        if (log != null && log.getSize() > 0) {
            Text tituloLog = new Text("── Registro de eventos ──");
            tituloLog.setFont(Font.font("Georgia", FontWeight.NORMAL, 14));
            tituloLog.setFill(Color.web("#888888"));

            VBox logBox = new VBox(2);
            logBox.setPadding(new Insets(5));
            for (int i = 0; i < log.getSize(); i++) {
                Text linea = new Text(log.get(i));
                linea.setFont(Font.font("Consolas", 10));
                linea.setFill(Color.web("#aaaaaa"));
                logBox.getChildren().add(linea);
            }

            ScrollPane scrollLog = new ScrollPane(logBox);
            scrollLog.setMaxHeight(160);
            scrollLog.setPrefWidth(700);
            scrollLog.setFitToWidth(true);
            scrollLog.setStyle("-fx-background: transparent; -fx-background-color: rgba(0,0,0,0.5); -fx-background-insets: 0;");
            scrollLog.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollLog.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            bloque.getChildren().addAll(tituloLog, scrollLog);
        }

        // ScrollPane para permitir ver todo el contenido si la pantalla es pequeña
        ScrollPane scrollRaiz = new ScrollPane(bloque);
        scrollRaiz.setFitToWidth(true);
        scrollRaiz.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollRaiz.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollRaiz.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        raiz.getChildren().addAll(fondo, scrollRaiz);

        scene = new Scene(raiz, ANCHO, ALTO);
        return scene;
    }

    /**
     * Devuelve el texto narrativo segun victoria o derrota.
     */
    private String obtenerTexto() {
        if (victoria) {
            return "\"El Rey Demonio Malakor ha caido! "
                 + "Con un ultimo y devastador conjuro, desintegras la armadura "
                 + "del tirano, cuyo grito de agonia hace temblar los cimientos "
                 + "de la mazmorra.\n\n"
                 + "La plaga de oscuridad que asfixiaba al Reino de Eldoria "
                 + "comienza a disiparse y las almas atrapadas en el inframundo "
                 + "por fin encuentran el descanso.\n\n"
                 + "Con la llave final en tus manos, emerges de las profundidades "
                 + "hacia la superficie, donde la luz del sol vuelve a brillar "
                 + "en los campos. Has completado lo imposible, Mago: "
                 + "la humanidad esta a salvo y tu nombre sera recordado "
                 + "eternamente como el heroe que escapo de la mazmorra.\"";
        } else {
            return "\"Tu magia se extingue... El ultimo turno ha expirado "
                 + "y tus fuerzas te abandonan en la fria oscuridad de la cueva.\n\n"
                 + "La risa maquiavelica de Malakor reverbera en las paredes "
                 + "de piedra mientras tu baculo cae al suelo, quebrado. "
                 + "El Reino de Eldoria ha perdido a su ultimo bastion "
                 + "de esperanza; las hordas del inframundo marcharan ahora "
                 + "sobre la superficie sin que nadie pueda contenerlas.\n\n"
                 + "Tu cuerpo pasa a formar parte de la coleccion de huesos "
                 + "de la fosa, atrapado para siempre en los pasillos "
                 + "de la mazmorra. El mal ha triunfado.\"";
        }
    }

    private String obtenerTextoEstadisticas() {
        if (resultado == null) {
            return "";
        }
        return "Mago: " + resultado.getNombreJugador()
             + "\nTurnos jugados: " + resultado.getTurnosJugados()
             + "   Daño ejercido: " + resultado.getDanoEjercido()
             + "   Daño recibido: " + resultado.getDanoRecibido()
             + "\nEnemigos derrotados: " + resultado.getEnemigosMuertos()
             + "   Bosses derrotados: " + resultado.getBossesMuertos()
             + "   Malakor derrotado: " + (resultado.isMalakorDerrotado() ? "si" : "no")
             + "\nPuntuación final: " + resultado.getPuntuacion()
             + "\nTítulo: " + resultado.getTitulo();
    }

    /**
     * Boton "Volver al Menu Principal" con estilo pergamino.
     */
    private StackPane crearBotonVolver() {
        double w = 320;
        double h = 55;

        Rectangle rect = new Rectangle(w, h);
        rect.setFill(Color.web("#C89D65"));
        rect.setStroke(Color.web("#5C3A1E"));
        rect.setStrokeWidth(2.5);
        rect.setArcWidth(8);
        rect.setArcHeight(8);

        DropShadow sombra = new DropShadow();
        sombra.setColor(Color.rgb(0, 0, 0, 0.7));
        sombra.setRadius(5);
        sombra.setOffsetX(2);
        sombra.setOffsetY(2);
        rect.setEffect(sombra);

        Text texto = new Text("Volver al Menú Principal");
        texto.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        texto.setFill(Color.web("#2A1A0E"));

        StackPane grupo = new StackPane(rect, texto);
        grupo.setCursor(Cursor.HAND);

        grupo.setOnMouseEntered(e -> rect.setFill(Color.web("#B88950")));
        grupo.setOnMouseExited(e -> rect.setFill(Color.web("#C89D65")));
        grupo.setOnMouseClicked(e -> {
            if (volverAlMenu != null) volverAlMenu.run();
        });

        return grupo;
    }
}
