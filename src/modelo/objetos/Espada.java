package modelo.objetos;

/**
 * Arma cuerpo a cuerpo acordada para Parte B.
 */
public class Espada extends Arma {
    public static final int ATAQUE_EXTRA = 12;

    public Espada(String id) {
        super(id, "Espada", "Aumenta el ataque del jugador en combate cercano", ATAQUE_EXTRA);
    }
}
