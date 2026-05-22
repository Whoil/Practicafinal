package modelo.juego;

/**
 * Vista inmutable de un personaje situado en el mapa.
 *
 * Sirve para que la interfaz pueda pintar jugador o enemigos sin recibir la
 * referencia mutable real de `Jugador` o `Enemigo`.
 */
public class PersonajeEnMapa {
    private final String nombre;
    private final String tipo;
    private final int vidaActual;
    private final int vidaMaxima;
    private final int fila;
    private final int columna;

    public PersonajeEnMapa(String nombre, String tipo, int vidaActual, int vidaMaxima, int fila, int columna) {
        validarTexto(nombre, "nombre");
        validarTexto(tipo, "tipo");
        this.nombre = nombre;
        this.tipo = tipo;
        this.vidaActual = vidaActual;
        this.vidaMaxima = vidaMaxima;
        this.fila = fila;
        this.columna = columna;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    private static void validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
    }
}
