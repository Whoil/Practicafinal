package json;

/**
 * Arco dirigido entre dos cuevas leido del JSON de configuracion.
 * origen y destino son los ids de las cuevas que aparecen en el array
 * de cuevas del mismo fichero.
 */
public class ConexionDTO {
    private String origen;
    private String destino;
    private String etiqueta;

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
