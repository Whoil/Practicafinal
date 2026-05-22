package json;

/**
 * Estado completo de la mazmorra para guardado/recuperacion.
 *
 * Incluye todas las cuevas con su estado actual (matriz de celdas,
 * enemigos vivos y objetos en el suelo) y el grafo de conexiones
 * entre cuevas. La cueva actual se identifica por su id de texto
 * para facilitar la serializacion JSON.
 */
public class DatosMazmorraDTO {
    private String cuevaActual;
    private DatosCuevaDTO[] cuevas;
    private ConexionDTO[] conexiones;

    public DatosMazmorraDTO() {
    }

    public DatosMazmorraDTO(String cuevaActual, DatosCuevaDTO[] cuevas,
                            ConexionDTO[] conexiones) {
        this.cuevaActual = cuevaActual;
        this.cuevas = cuevas;
        this.conexiones = conexiones;
    }

    public String getCuevaActual() {
        return cuevaActual;
    }

    public DatosCuevaDTO[] getCuevas() {
        return cuevas;
    }

    public ConexionDTO[] getConexiones() {
        return conexiones;
    }
}
