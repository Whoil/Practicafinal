package vista;

import javafx.scene.paint.Color;

/**
 * Datos estaticos de tematica por cueva.
 *
 * Cada constante asocia un id de cueva del JSON con su titulo narrativo,
 * texto de transicion, colores, emojis y fondo tematico.
 * Esto centraliza la configuracion visual para que PantallaJuego,
 * PantallaTransicion y PantallaFinal consuman los mismos valores sin
 * duplicar logica.
 */
public enum DatosTemaCueva {

    CRIPTAS("cueva_facil",
            "CUEVA I: LAS CRIPTAS DE MARFIL",
            "El viaje comienza en los niveles superiores de la fosa. "
          + "Aqui yacen los antiguos guerreros del reino, cuyos cuerpos "
          + "han sido profanados por la magia negra de Malakor. "
          + "El crujido de los huesos resuena en la oscuridad: "
          + "hordas de esqueletos custodian la entrada al abismo. "
          + "Encuentra la llave, sobrevive al Guardian Osario y abre "
          + "las puertas hacia lo profundo.",
            "radial-gradient(center 50% 50%, radius 60%, #2e2e2e 0%, #0a0a0a 100%)",
            Color.rgb(210, 205, 195),
            "characters/Spritesheets/skeleton_idle.png",
            "characters/Spritesheets/shaman1_idle.png",
            "\uD83D\uDD2A"),

    PARAMO("cueva_media",
            "CUEVA II: EL PARAMO PUTREFACTO",
            "El olor a azufre y carne descompuesta inunda el aire. "
          + "Has descendido a las fosas comunes, donde los caidos "
          + "no descansan en paz; caminan. "
          + "Los zombies, lentos pero implacables, bloquean los pasillos "
          + "de esta cueva humeda. "
          + "No dejes que te acorralen en las esquinas de la matriz. "
          + "La criatura que lidera esta putrefaccion te espera al final.",
            "radial-gradient(center 50% 50%, radius 60%, #192819 0%, #050a05 100%)",
            Color.rgb(70, 130, 70),
            "characters/Spritesheets/orc1_idle.png",
            "characters/Spritesheets/orc2_idle.png",
            "\uD83E\uDDEA"),

    ABISMO("cueva_dificil",
            "CUEVA III: EL ABISMO DE MALAKOR",
            "Has llegado al corazon del Inframundo. "
          + "El suelo tiembla bajo tus pies y las paredes rezuman fuego. "
          + "Ya no hay muertos vivientes; estas en el territorio "
          + "de los engendros del caos. "
          + "Demonios menores custodian los tesoros finales y protegen "
          + "el santuario de su senor. "
          + "Tu mana es escaso, tus turnos estan contados. "
          + "Derrota al Rey Demonio Malakor para arrebatarle la llave "
          + "final y escapar con vida de la mazmorra.",
            "radial-gradient(center 50% 50%, radius 60%, #3a0d0d 0%, #0a0202 100%)",
            Color.rgb(170, 45, 45),
            "characters/Spritesheets/demon_idle.png",
            "characters/Spritesheets/warlock_idle.png",
            "\uD83D\uDD25");

    private final String cuevaId;
    private final String titulo;
    private final String texto;
    private final String fondoCSS;
    private final Color colorMuro;
    private final String assetEnemigo;
    private final String assetBoss;
    private final String emojiDecoracion;

    DatosTemaCueva(String cuevaId, String titulo, String texto,
                   String fondoCSS, Color colorMuro,
                   String assetEnemigo, String assetBoss,
                   String emojiDecoracion) {
        this.cuevaId = cuevaId;
        this.titulo = titulo;
        this.texto = texto;
        this.fondoCSS = fondoCSS;
        this.colorMuro = colorMuro;
        this.assetEnemigo = assetEnemigo;
        this.assetBoss = assetBoss;
        this.emojiDecoracion = emojiDecoracion;
    }

    public String getCuevaId() {
        return cuevaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTexto() {
        return texto;
    }

    public String getFondoCSS() {
        return fondoCSS;
    }

    public Color getColorMuro() {
        return colorMuro;
    }

    public String getAssetEnemigo() {
        return assetEnemigo;
    }

    public String getAssetBoss() {
        return assetBoss;
    }

    public String getEmojiDecoracion() {
        return emojiDecoracion;
    }

    /**
     * Busca el tema correspondiente a un id de cueva.
     *
     * @param cuevaId id de la cueva (ej. "cueva_facil")
     * @return DatosTemaCueva correspondiente, o CRIPTAS como fallback
     */
    public static DatosTemaCueva paraCuevaId(String cuevaId) {
        for (DatosTemaCueva tema : values()) {
            if (tema.cuevaId.equals(cuevaId)) {
                return tema;
            }
        }
        return CRIPTAS;
    }
}
