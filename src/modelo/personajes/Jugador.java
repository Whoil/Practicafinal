package modelo.personajes;

/**
 * Personaje controlado por el usuario.
 *
 * En B-01 solo mantiene los datos heredados de Personaje. El inventario y el
 * objeto equipado ya estan decididos en el diseno de Parte B, pero se dejan
 * para B-02 para no mezclar el modelo base de personajes con la gestion de
 * objetos.
 */
public class Jugador extends Personaje {
    public Jugador(String nombre, int vidaMaxima, int ataqueBase, int defensaBase,
                   int movimiento, int fila, int columna) {
        super(nombre, vidaMaxima, ataqueBase, defensaBase, movimiento, fila, columna);
    }
}
