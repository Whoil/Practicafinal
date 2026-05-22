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

/**
 * Pantalla de transicion reutilizable — aparece antes de entrar a cada cueva.
 *
 * Muestra un titulo tematico, el texto narrativo de la cueva y un boton
 * "Entrar a la Cueva" que dispara la carga del mapa correspondiente.
 *
 * Se instancia con un DatosTemaCueva que define colores, fondo y textos,
 * y un callback que se ejecuta al pulsar el boton de entrada.
 */
public class PantallaTransicion {

    private static final int ANCHO = 1280;
    private static final int ALTO = 720;
    private static final String FONT = "Georgia, serif";

    private final DatosTemaCueva tema;
    private final Runnable alEntrar;
    private Scene scene;

    /**
     * @param tema     datos tematicos de la cueva (titulo, texto, colores, fondo)
     * @param alEntrar callback al pulsar "Entrar a la Cueva"
     */
    public PantallaTransicion(DatosTemaCueva tema, Runnable alEntrar) {
        this.tema = tema;
        this.alEntrar = alEntrar;
    }

    /**
     * Construye y devuelve la Scene de transicion.
     */
    public Scene crearScene() {
        StackPane raiz = new StackPane();
        raiz.setStyle("-fx-background-color: #000000;");

        // Fondo tematico
        StackPane fondo = new StackPane();
        fondo.setStyle("-fx-background-color: " + tema.getFondoCSS() + ";");
        fondo.setPrefSize(ANCHO, ALTO);

        // Bloque centrado con titulo + texto + boton
        VBox bloque = new VBox(30);
        bloque.setMaxWidth(750);
        bloque.setAlignment(Pos.CENTER);
        bloque.setPadding(new Insets(60));

        // Titulo
        Text titulo = new Text(tema.getTitulo());
        titulo.setFont(Font.font(FONT, FontWeight.BOLD, 32));
        titulo.setFill(tema.getColorMuro());
        titulo.setTextAlignment(TextAlignment.CENTER);

        DropShadow sombraTitulo = new DropShadow();
        sombraTitulo.setColor(Color.rgb(0, 0, 0, 0.8));
        sombraTitulo.setRadius(4);
        titulo.setEffect(sombraTitulo);

        // Cuerpo del texto
        Text cuerpo = new Text(tema.getTexto());
        cuerpo.setFont(Font.font(FONT, FontWeight.NORMAL, 18));
        cuerpo.setFill(Color.web("#f5f5f5"));
        cuerpo.setTextAlignment(TextAlignment.CENTER);
        cuerpo.setLineSpacing(6);
        cuerpo.setWrappingWidth(630);

        DropShadow sombraCuerpo = new DropShadow();
        sombraCuerpo.setColor(Color.rgb(0, 0, 0, 0.7));
        sombraCuerpo.setRadius(3);
        cuerpo.setEffect(sombraCuerpo);

        // Boton Entrar a la Cueva
        StackPane btnEntrar = crearBotonEntrar();

        bloque.getChildren().addAll(titulo, cuerpo, btnEntrar);

        raiz.getChildren().addAll(fondo, bloque);

        scene = new Scene(raiz, ANCHO, ALTO);
        return scene;
    }

    /**
     * Boton "Entrar a la Cueva" con estilo pergamino.
     */
    private StackPane crearBotonEntrar() {
        double w = 260;
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

        Text texto = new Text("\u2694 Entrar a la Cueva");
        texto.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        texto.setFill(Color.web("#2A1A0E"));

        StackPane grupo = new StackPane(rect, texto);
        grupo.setCursor(Cursor.HAND);

        grupo.setOnMouseEntered(e -> rect.setFill(Color.web("#B88950")));
        grupo.setOnMouseExited(e -> rect.setFill(Color.web("#C89D65")));
        grupo.setOnMouseClicked(e -> {
            if (alEntrar != null) alEntrar.run();
        });

        return grupo;
    }
}
