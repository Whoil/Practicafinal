package json;

/**
 * Raiz del JSON de configuracion de la mazmorra.
 * Gson deserializa directamente este objeto desde datos/cuevas.json.
 * Este DTO es solo un puente entre el JSON y el modelo del juego.
 */
public class ConfiguracionMazmorra {
    private String nombre;
    private ConfiguracionCuevaDTO[] cuevas;
    private ConexionDTO[] conexiones;

    public String getNombre() {
        return nombre;
    }

    public ConfiguracionCuevaDTO[] getCuevas() {
        return cuevas;
    }

    public ConexionDTO[] getConexiones() {
        return conexiones;
    }
}
