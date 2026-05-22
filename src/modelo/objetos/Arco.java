package modelo.objetos;

/**
 * Arma de ataque a distancia acordada para Parte B.
 *
 * La distancia real de ataque queda fuera de B-02 y se resolvera en combate.
 */
public class Arco extends Arma {
    public static final int ATAQUE_EXTRA = 7;

    public Arco(String id) {
        super(id, "Arco", "Aumenta el ataque del jugador con un arma a distancia", ATAQUE_EXTRA);
    }
}
