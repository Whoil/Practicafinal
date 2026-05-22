package modelo.mapa;

/**
 * Unidad minima de la matriz de una cueva.
 *
 * Una celda conoce su posicion fija dentro de la matriz y su tipo actual. La
 * posicion no cambia porque mover una celda dentro de la matriz haria mucho mas
 * dificil razonar sobre BFS, puertas, trampas y renderizado. Lo que cambia es
 * el contenido o significado de la casilla mediante TipoCelda.
 */
public class Celda implements InterfazCelda {
    /*
     * La posicion identifica a la celda dentro de una cueva. Se guarda como
     * objeto para poder pasar coordenadas por el codigo sin depender de arrays
     * ni pares sueltos de enteros.
     */
    private final Posicion posicion;

    /*
     * Tipo actual de terreno o contenido estructural. Parte B podra decidir mas
     * adelante si personajes u objetos viven dentro de Celda o en otra capa.
     */
    private TipoCelda tipo;

    public Celda(int fila, int columna, TipoCelda tipo) {
        this.posicion = new Posicion(fila, columna);
        this.tipo = tipo;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    @Override
    public int getFila() {
        return posicion.getFila();
    }

    @Override
    public int getColumna() {
        return posicion.getColumna();
    }

    @Override
    public TipoCelda getTipo() {
        return tipo;
    }

    @Override
    public void setTipo(TipoCelda tipo) {
        this.tipo = tipo;
    }

    /**
     * Regla estructural minima de movimiento.
     *
     * MURO, ROCA y ARBUSTO bloquean el paso. Otros tipos como PUERTA, TRAMPA o
     * TESORO son transitables porque el jugador puede entrar en ellos y despues
     * la logica de Parte B decidira que efecto se aplica.
     */
    public boolean esTransitable() {
        return tipo != TipoCelda.MURO
                && tipo != TipoCelda.ROCA
                && tipo != TipoCelda.ARBUSTO;
    }

    /**
     * Dos celdas se consideran la misma si ocupan la misma posicion.
     *
     * No se incluye el tipo en la igualdad porque una celda puede cambiar de
     * SUELO a TRAMPA o PUERTA sin dejar de ser la casilla (fila, columna).
     */
    @Override
    public boolean equals(Object otro) {
        if (!(otro instanceof Celda)) {
            return false;
        }
        Celda otra = (Celda) otro;
        return posicion.equals(otra.posicion);
    }

    /**
     * Mantiene el contrato general de Java junto a equals().
     *
     * No introduce ninguna estructura hash en el proyecto; solo evita que una
     * celda igual por posicion tenga un hash incoherente si alguna herramienta
     * de test o depuracion lo consulta.
     */
    @Override
    public int hashCode() {
        return posicion.hashCode();
    }

    @Override
    public String toString() {
        return posicion + ":" + tipo;
    }
}
