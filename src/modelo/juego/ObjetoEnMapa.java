package modelo.juego;

import modelo.mapa.Cueva;
import modelo.objetos.Objeto;

/**
 * Objeto colocado en una cueva concreta.
 *
 * Objeto no guarda posicion porque el mismo objeto puede estar en inventario o
 * en el suelo. Esta clase representa solo el caso "objeto en el mapa".
 *
 * Es una pieza de integracion parcial: JSON y JavaFX tendran que usarla o
 * convertirla cuando se conecte la partida completa. No sustituye a Objeto.
 */
public class ObjetoEnMapa {
    private final Objeto objeto;
    private final Cueva cueva;
    private final int fila;
    private final int columna;

    public ObjetoEnMapa(Objeto objeto, Cueva cueva, int fila, int columna) {
        if (objeto == null) {
            throw new IllegalArgumentException("El objeto es obligatorio");
        }
        if (cueva == null) {
            throw new IllegalArgumentException("La cueva es obligatoria");
        }
        if (!cueva.estaDentro(fila, columna)) {
            throw new IllegalArgumentException("La posicion del objeto debe estar dentro de la cueva");
        }
        this.objeto = objeto;
        this.cueva = cueva;
        this.fila = fila;
        this.columna = columna;
    }

    public Objeto getObjeto() {
        return objeto;
    }

    public Cueva getCueva() {
        return cueva;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public boolean equals(Object otro) {
        if (!(otro instanceof ObjetoEnMapa)) {
            return false;
        }
        ObjetoEnMapa otroObjeto = (ObjetoEnMapa) otro;
        return objeto.equals(otroObjeto.objeto)
                && cueva.equals(otroObjeto.cueva)
                && fila == otroObjeto.fila
                && columna == otroObjeto.columna;
    }

    @Override
    public int hashCode() {
        int resultado = objeto.hashCode();
        resultado = 31 * resultado + cueva.hashCode();
        resultado = 31 * resultado + fila;
        resultado = 31 * resultado + columna;
        return resultado;
    }
}
