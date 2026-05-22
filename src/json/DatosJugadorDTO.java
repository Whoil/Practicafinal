package json;

/**
 * Estado del jugador para guardado/recuperacion.
 *
 * Contiene todos los atributos de personaje (vida, ataque, defensa,
 * movimiento, posicion) ademas del inventario (lista de objetos) y
 * las referencias a los objetos equipados en cada ranura. Las armas
 * y escudos equipados se identifican por el id del objeto dentro del
 * inventario, o null si la ranura esta vacia.
 */
public class DatosJugadorDTO {
    private String nombre;
    private int vidaActual;
    private int vidaMaxima;
    private int ataqueBase;
    private int defensaBase;
    private int movimiento;
    private int fila;
    private int columna;
    private String armaEquipadaId;
    private String escudoEquipadoId;
    private DatosObjetoDTO[] inventario;

    public DatosJugadorDTO() {
    }

    public DatosJugadorDTO(String nombre, int vidaActual, int vidaMaxima,
                           int ataqueBase, int defensaBase, int movimiento,
                           int fila, int columna,
                           String armaEquipadaId, String escudoEquipadoId,
                           DatosObjetoDTO[] inventario) {
        this.nombre = nombre;
        this.vidaActual = vidaActual;
        this.vidaMaxima = vidaMaxima;
        this.ataqueBase = ataqueBase;
        this.defensaBase = defensaBase;
        this.movimiento = movimiento;
        this.fila = fila;
        this.columna = columna;
        this.armaEquipadaId = armaEquipadaId;
        this.escudoEquipadoId = escudoEquipadoId;
        this.inventario = inventario;
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

    public String getArmaEquipadaId() {
        return armaEquipadaId;
    }

    public String getEscudoEquipadoId() {
        return escudoEquipadoId;
    }

    public DatosObjetoDTO[] getInventario() {
        return inventario;
    }
}
