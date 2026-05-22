package modelo.objetos;

/**
 * Objeto que permite abrir una puerta o un cofre concreto.
 *
 * En B-02 solo se modela la llave y su destino. Abrir puertas o cofres requiere
 * integracion con mapa y queda fuera de esta tarea.
 */
public class Llave extends Objeto {
    private final TipoLlave tipoLlave;
    private final String codigoCerradura;

    public Llave(String id, TipoLlave tipoLlave, String codigoCerradura) {
        super(id, "Llave", "Permite abrir una cerradura concreta");
        if (tipoLlave == null) {
            throw new IllegalArgumentException("El tipo de llave es obligatorio");
        }
        validarTexto(codigoCerradura, "codigoCerradura");
        this.tipoLlave = tipoLlave;
        this.codigoCerradura = codigoCerradura;
    }

    public TipoLlave getTipoLlave() {
        return tipoLlave;
    }

    public String getCodigoCerradura() {
        return codigoCerradura;
    }

    private static void validarTexto(String texto, String campo) {
        /*
         * El codigo de cerradura sera el puente con puertas o cofres cuando se
         * integren mapa y acciones. Por eso no puede quedar vacio.
         */
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
    }
}
