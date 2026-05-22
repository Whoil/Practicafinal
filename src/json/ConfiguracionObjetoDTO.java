package json;

/**
 * Datos de un objeto definidos en el JSON de configuracion.
 * Parte B decidira como convertir estos datos en objetos del juego
 * (Pocion, Llave, Espada, Escudo, etc.).
 */
public class ConfiguracionObjetoDTO {
    private String idCueva;
    private String id;
    private String tipo;
    private int fila;
    private int columna;
    private String nombre;
    private String descripcion;
    private String tipoLlave;
    private String codigoCerradura;
    private int cura;
    private int ataque;
    private int defensa;

    public String getIdCueva() {
        return idCueva;
    }

    void setIdCueva(String idCueva) {
        this.idCueva = idCueva;
    }

    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipoLlave() {
        return tipoLlave;
    }

    public String getCodigoCerradura() {
        return codigoCerradura;
    }

    public int getCura() {
        return cura;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }
}
