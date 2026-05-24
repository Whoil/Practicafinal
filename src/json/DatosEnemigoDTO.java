package json;

/**
 * Estado actual de un enemigo para guardado/recuperacion.
 *
 * Incluye tanto los atributos base del personaje (vida, ataque, defensa,
 * movimiento) como el estado mutable (vida actual, si sigue vivo) y su
 * posicion actual dentro de la cueva.
 */
public class DatosEnemigoDTO {
    private String tipo;
    private String nombre;
    private int vidaActual;
    private int vidaMaxima;
    private int ataqueBase;
    private int defensaBase;
    private int movimiento;
    private int fila;
    private int columna;
    private boolean vivo;
    private int turnosCongelado;

    public DatosEnemigoDTO() {
    }

    public DatosEnemigoDTO(String tipo, String nombre,
                           int vidaActual, int vidaMaxima,
                           int ataqueBase, int defensaBase,
                           int movimiento, int fila, int columna,
                           boolean vivo) {
        this(tipo, nombre, vidaActual, vidaMaxima, ataqueBase, defensaBase,
                movimiento, fila, columna, vivo, 0);
    }

    public DatosEnemigoDTO(String tipo, String nombre,
                           int vidaActual, int vidaMaxima,
                           int ataqueBase, int defensaBase,
                           int movimiento, int fila, int columna,
                           boolean vivo, int turnosCongelado) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.vidaActual = vidaActual;
        this.vidaMaxima = vidaMaxima;
        this.ataqueBase = ataqueBase;
        this.defensaBase = defensaBase;
        this.movimiento = movimiento;
        this.fila = fila;
        this.columna = columna;
        this.vivo = vivo;
        this.turnosCongelado = turnosCongelado;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getAtaqueBase() {
        return ataqueBase;
    }

    public int getDefensaBase() {
        return defensaBase;
    }

    public int getMovimiento() {
        return movimiento;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public boolean isVivo() {
        return vivo;
    }

    public int getTurnosCongelado() {
        return turnosCongelado;
    }
}
