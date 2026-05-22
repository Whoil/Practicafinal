package json;

/**
 * Raiz del JSON de guardado de partida.
 *
 * Contiene la version del formato, la mazmorra con el estado actual de
 * todas las cuevas, el jugador con su inventario y equipo, y el estado
 * global de la partida (en curso, victoria o derrota).
 */
public class DatosPartidaDTO {
    private String version;
    private DatosMazmorraDTO mazmorra;
    private DatosJugadorDTO jugador;
    private String estado;
    private int turnosRestantes;
    private DatosPuertaDTO[] puertas;

    public DatosPartidaDTO() {
    }

    public DatosPartidaDTO(String version, DatosMazmorraDTO mazmorra,
                           DatosJugadorDTO jugador, String estado,
                           int turnosRestantes) {
        this(version, mazmorra, jugador, estado, turnosRestantes, null);
    }

    public DatosPartidaDTO(String version, DatosMazmorraDTO mazmorra,
                           DatosJugadorDTO jugador, String estado,
                           int turnosRestantes,
                           DatosPuertaDTO[] puertas) {
        this.version = version;
        this.mazmorra = mazmorra;
        this.jugador = jugador;
        this.estado = estado;
        this.turnosRestantes = turnosRestantes;
        this.puertas = puertas;
    }

    public String getVersion() { return version; }
    public DatosMazmorraDTO getMazmorra() { return mazmorra; }
    public DatosJugadorDTO getJugador() { return jugador; }
    public String getEstado() { return estado; }
    public int getTurnosRestantes() { return turnosRestantes; }
    public DatosPuertaDTO[] getPuertas() { return puertas; }
}
