package modelo.objetos;

/**
 * Clase base de todos los objetos del juego.
 *
 * El inventario necesita tratar pociones, armas, escudos y llaves de forma
 * comun. Por eso todos los objetos tienen un identificador estable, un nombre
 * visible y un texto breve de descripcion. Las reglas concretas de uso o equipo
 * viven en las subclases para no llenar esta clase base de casos especiales.
 */
public abstract class Objeto {
    private final String id;
    private final String nombre;
    private final String descripcion;

    protected Objeto(String id, String nombre, String descripcion) {
        validarTexto(id, "id");
        validarTexto(nombre, "nombre");
        validarTexto(descripcion, "descripcion");

        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Indica si el objeto puede ocupar una ranura de equipo.
     *
     * La respuesta por defecto es no, para que objetos como llaves o consumibles
     * no se equipen por accidente. Las subclases equipables lo sobrescriben.
     */
    public boolean esEquipable() {
        return false;
    }

    /**
     * Indica si el objeto desaparece al usarse.
     *
     * La clase base no asume consumo. Pocion lo sobrescribe porque su efecto
     * acordado es curar y retirarse del inventario.
     */
    public boolean esConsumible() {
        return false;
    }

    private static void validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
    }

    @Override
    public boolean equals(Object otro) {
        /*
         * Dos objetos son el mismo elemento de inventario si comparten id. Esto
         * permite tener dos pociones del mismo tipo con ids diferentes, pero
         * evita duplicar exactamente el mismo objeto.
         */
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof Objeto)) {
            return false;
        }
        Objeto otroObjeto = (Objeto) otro;
        return id.equals(otroObjeto.id);
    }

    @Override
    public int hashCode() {
        /*
         * Aunque no usamos HashMap ni HashSet, mantener hashCode coherente con
         * equals facilita pruebas, depuracion y futuras integraciones.
         */
        return id.hashCode();
    }

    @Override
    public String toString() {
        return nombre;
    }
}
