package modelo.personajes;

/**
 * Personaje enemigo controlado por la logica del juego.
 *
 * En B-01 solo guarda el tipo de enemigo y los datos comunes heredados. Los
 * drops, objetos equipados, movimiento inteligente y reglas de ataque quedan
 * para B-02/B-03, donde pertenecen por responsabilidad.
 */
public class Enemigo extends Personaje {
    private final TipoEnemigo tipoEnemigo;
    private int turnosCongelado;

    public Enemigo(String nombre, TipoEnemigo tipoEnemigo, int vidaMaxima, int ataqueBase,
                   int defensaBase, int movimiento, int fila, int columna) {
        super(nombre, vidaMaxima, ataqueBase, defensaBase, movimiento, fila, columna);
        if (tipoEnemigo == null) {
            throw new IllegalArgumentException("El tipo de enemigo es obligatorio");
        }
        this.tipoEnemigo = tipoEnemigo;
        this.turnosCongelado = 0;
    }

    public TipoEnemigo getTipoEnemigo() {
        return tipoEnemigo;
    }

    public int getTurnosCongelado() {
        return turnosCongelado;
    }

    public boolean estaCongelado() {
        return turnosCongelado > 0;
    }

    public void congelar(int turnos) {
        if (turnos < 0) {
            throw new IllegalArgumentException("Los turnos de congelacion no pueden ser negativos");
        }
        turnosCongelado = turnos;
    }

    public void consumirTurnoCongelado() {
        if (turnosCongelado > 0) {
            turnosCongelado--;
        }
    }
}
