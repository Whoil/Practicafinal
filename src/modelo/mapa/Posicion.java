package modelo.mapa;

/**
 * Coordenada inmutable dentro de una cueva.
 *
 * Se separa de Celda para que algoritmos como BFS puedan trabajar con
 * posiciones candidatas sin tener que modificar la matriz. No implementa
 * Comparable porque una posicion del mapa no tiene un orden natural necesario:
 * solo necesitamos igualdad por fila y columna.
 */
public class Posicion implements InterfazPosicion {
    private final int fila;
    private final int columna;

    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    @Override
    public int getColumna() {
        return columna;
    }

    /**
     * Igualdad estructural: misma fila y misma columna.
     */
    @Override
    public boolean equals(Object otro) {
        if (!(otro instanceof Posicion)) {
            return false;
        }
        Posicion otra = (Posicion) otro;
        return fila == otra.fila && columna == otra.columna;
    }

    /**
     * Mantiene el contrato general de Java: si dos posiciones son iguales por
     * equals(), tambien deben tener el mismo hashCode().
     *
     * Esto no significa que usemos HashMap o HashSet en la parte evaluable. La
     * posicion sigue almacenandose en estructuras propias como ListaSE.
     */
    @Override
    public int hashCode() {
        return 31 * fila + columna;
    }

    @Override
    public String toString() {
        return "(" + fila + ", " + columna + ")";
    }
}
