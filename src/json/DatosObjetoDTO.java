package json;

/**
 * Estado de un objeto para guardado/recuperacion.
 *
 * Un mismo DTO sirve tanto para objetos en el mapa (fila/columna >= 0)
 * como para objetos en el inventario del jugador (fila/columna = -1).
 * Los atributos especificos de cada subtipo (cura de pocion,
 * bonificacion de arma/escudo, tipo de llave) se incluyen todos en
 * un unico DTO con valores cero o null cuando no aplican, lo que
 * simplifica la serializacion con Gson al no requerir polimorfismo.
 */
public class DatosObjetoDTO {
    private String id;
    private String tipo;
    private String nombre;
    private String descripcion;
    private int fila;
    private int columna;
    private int cura;
    private int bonificacionAtaque;
    private int bonificacionDefensa;
    private String tipoLlave;
    private String codigoCerradura;

    public DatosObjetoDTO() {
    }

    public DatosObjetoDTO(String id, String tipo, String nombre,
                          String descripcion, int fila, int columna,
                          int cura, int bonificacionAtaque,
                          int bonificacionDefensa, String tipoLlave) {
        this(id, tipo, nombre, descripcion, fila, columna, cura,
             bonificacionAtaque, bonificacionDefensa, tipoLlave, null);
    }

    public DatosObjetoDTO(String id, String tipo, String nombre,
                          String descripcion, int fila, int columna,
                          int cura, int bonificacionAtaque,
                          int bonificacionDefensa, String tipoLlave,
                          String codigoCerradura) {
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fila = fila;
        this.columna = columna;
        this.cura = cura;
        this.bonificacionAtaque = bonificacionAtaque;
        this.bonificacionDefensa = bonificacionDefensa;
        this.tipoLlave = tipoLlave;
        this.codigoCerradura = codigoCerradura;
    }

    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getCura() {
        return cura;
    }

    public int getBonificacionAtaque() {
        return bonificacionAtaque;
    }

    public int getBonificacionDefensa() {
        return bonificacionDefensa;
    }

    public String getTipoLlave() {
        return tipoLlave;
    }

    public String getCodigoCerradura() {
        return codigoCerradura;
    }
}
