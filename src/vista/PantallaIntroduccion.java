package vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Pantalla de introduccion — historia inicial al pulsar "Nueva Partida".
 *
 * Muestra el texto narrativo de fondo sobre el viaje del Mago,
 * con un fondo oscuro de cueva y un boton "Continuar" para avanzar
 * a la primera transicion de cueva.
 */
public class PantallaIntroduccion {

    private static final int ANCHO = 1280;
    private static final int ALTO = 720;
    private static final String FONT = "Georgia, serif";

    private final Runnable alContinuar;
    private Scene scene;

    /**
     * @param alContinuar callback cuando el jugador pulsa "Continuar"
     */
    public PantallaIntroduccion(Runnable alContinuar) {
        this.alContinuar = alContinuar;
    }

    /**
     * Construye y devuelve la Scene de introduccion.
     */
    public Scene crearScene() {
        StackPane raiz = new StackPane();
        raiz.setStyle("-fx-background-color: #000000;");

        // Fondo con gradiente radial oscuro
        StackPane fondo = new StackPane();
        fondo.setStyle(
            "-fx-background-color: radial-gradient(center 50% 50%, radius 60%, #1a1310 0%, #000000 100%);"
        );
        fondo.setPrefSize(ANCHO, ALTO);

        // Bloque de texto centrado con margenes amplios
        VBox bloqueTexto = new VBox(30);
        bloqueTexto.setMaxWidth(800);
        bloqueTexto.setAlignment(Pos.CENTER);
        bloqueTexto.setPadding(new Insets(60));

        // Texto narrativo
        Text texto = new Text(
            "\"El Reino de Eldoria se apaga. Desde las profundidades de la tierra, "
          + "el Rey Demonio Malakor ha despertado, extendiendo una plaga de oscuridad "
          + "que marchita los campos y alza a los muertos.\n\n"
          + "Desesperado, el Rey de los hombres ha convocado al ultimo Gran Mago "
          + "del consejo. Tu mision es clara, pero suicida: descender por las tres "
          + "cavernas del Inframundo, purgar el mal que habita en ellas y destruir "
          + "a Malakor antes de que el ultimo turno de la humanidad llegue a su fin.\n\n"
          + "Prepara tus hechizos, Mago. El escape de la mazmorra comienza ahora.\""
        );
        texto.setFont(Font.font(FONT, FontWeight.NORMAL, 18));
        texto.setFill(Color.web("#f5f5f5"));
        texto.setTextAlignment(TextAlignment.CENTER);
        texto.setLineSpacing(5);
        texto.setWrappingWidth(680);

        DropShadow sombraTexto = new DropShadow();
        sombraTexto.setColor(Color.rgb(0, 0, 0, 0.8));
        sombraTexto.setRadius(4);
        texto.setEffect(sombraTexto);

        StackPane btnContinuar = crearBotonContinuar();

        bloqueTexto.getChildren().addAll(texto, btnContinuar);

        raiz.getChildren().addAll(fondo, bloqueTexto);

        scene = new Scene(raiz, ANCHO, ALTO);
        return scene;
    }

    /**
     * Boton "Continuar ->" con estilo pergamino.
     */
    private StackPane crearBotonContinuar() {
        double w = 180;
        double h = 50;

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

        Text texto = new Text("Continuar ->");
        texto.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        texto.setFill(Color.web("#2A1A0E"));

        StackPane grupo = new StackPane(rect, texto);
        grupo.setCursor(Cursor.HAND);

        grupo.setOnMouseEntered(e -> rect.setFill(Color.web("#B88950")));
        grupo.setOnMouseExited(e -> rect.setFill(Color.web("#C89D65")));
        grupo.setOnMouseClicked(e -> {
            if (alContinuar != null) alContinuar.run();
        });

        return grupo;
    }
}
