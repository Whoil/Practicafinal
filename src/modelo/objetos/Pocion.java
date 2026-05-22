package modelo.objetos;

import modelo.personajes.Jugador;

/**
 * Pocion de cura consumible.
 *
 * El uso de una pocion cuenta como accion en el sistema de turnos, pero esa
 * contabilidad pertenece a B-03. Aqui solo se aplica el efecto sobre el jugador
 * y se devuelve si la pocion debe retirarse del inventario.
 */
public class Pocion extends Objeto {
    public static final int CURACION_BASE = 25;

    private final int puntosCuracion;

    public Pocion(String id) {
        this(id, CURACION_BASE);
    }

    public Pocion(String id, int puntosCuracion) {
        super(id, "Pocion de cura", "Recupera vida y se consume al usarla");
        validarPositivo(puntosCuracion, "puntosCuracion");
        this.puntosCuracion = puntosCuracion;
    }

    public int getPuntosCuracion() {
        return puntosCuracion;
    }

    /**
     * Aplica la curacion al jugador y avisa de que la pocion debe consumirse.
     */
    public boolean usarSobre(Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador es obligatorio");
        }
        jugador.curar(puntosCuracion);
        return true;
    }

    @Override
    public boolean esConsumible() {
        return true;
    }

    private static void validarPositivo(int valor, String campo) {
        if (valor <= 0) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser positivo");
        }
    }
}
