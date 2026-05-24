package modelo.juego;

public final class ConstantesJuego {

    public static final int DANO_BOLA_FUEGO = 10;
    public static final int RANGO_BOLA_FUEGO = 5;

    public static final int RADIO_VISION = 3;
    public static final double[] OPACIDAD_FOG = { 0.0, 0.15, 0.40, 0.75 };

    public enum HechizoPendiente {
        NINGUNO,
        FUEGO,
        HIELO
    }

    private ConstantesJuego() {}
}
