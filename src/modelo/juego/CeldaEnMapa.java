package modelo.juego;

import modelo.mapa.TipoCelda;

/**
 * Vista inmutable de una celda.
 *
 * JavaFX puede usarla para pintar terreno y posiciones alcanzables sin recibir
 * la `Celda` real, que es mutable.
 */
public class CeldaEnMapa {
    private final int fila;
    private final int columna;
    private final TipoCelda tipo;

    public CeldaEnMapa(int fila, int columna, TipoCelda tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de celda es obligatorio");
        }
        this.fila = fila;
        this.columna = columna;
        this.tipo = tipo;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public TipoCelda getTipo() {
        return tipo;
    }

    @Override
    public boolean equals(Object otro) {
        if (!(otro instanceof CeldaEnMapa)) {
            return false;
        }
        CeldaEnMapa otraCelda = (CeldaEnMapa) otro;
        return fila == otraCelda.fila && columna == otraCelda.columna;
    }

    @Override
    public int hashCode() {
        return 31 * fila + columna;
    }
}
