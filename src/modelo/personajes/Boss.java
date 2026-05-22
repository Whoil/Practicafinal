package modelo.personajes;

/**
 * Enemigo especial final.
 *
 * El boss se modela como un Enemigo porque comparte estado y comportamiento con
 * el resto. Por ahora solo fija el tipo BOSS; sus reglas especiales de combate
 * y victoria se implementaran mas adelante, cuando toque B-03.
 */
public class Boss extends Enemigo {
    public Boss(String nombre, int vidaMaxima, int ataqueBase, int defensaBase,
                int movimiento, int fila, int columna) {
        super(nombre, TipoEnemigo.BOSS, vidaMaxima, ataqueBase, defensaBase, movimiento, fila, columna);
    }
}
