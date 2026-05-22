package modelo.juego;

import Estructuras.ListaSE;

/**
 * Vista inmutable de la cueva activa.
 *
 * Evita exponer `Cueva` a la interfaz grafica. `Cueva` contiene metodos de
 * mutacion de terreno que deben quedar bajo control del modelo y la partida.
 */
public class CuevaEnMapa {
    private final String id;
    private final int filas;
    private final int columnas;
    private final ListaSE<CeldaEnMapa> celdas;

    public CuevaEnMapa(String id, int filas, int columnas, ListaSE<CeldaEnMapa> celdas) {
        validarTexto(id, "id");
        if (filas <= 0 || columnas <= 0) {
            throw new IllegalArgumentException("La cueva debe tener dimensiones positivas");
        }
        if (celdas == null) {
            throw new IllegalArgumentException("Las celdas son obligatorias");
        }
        this.id = id;
        this.filas = filas;
        this.columnas = columnas;
        this.celdas = celdas.copy();
    }

    public String getId() {
        return id;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public ListaSE<CeldaEnMapa> getCeldas() {
        return celdas.copy();
    }

    public CeldaEnMapa getCelda(int fila, int columna) {
        for (int i = 0; i < celdas.getSize(); i++) {
            CeldaEnMapa celda = celdas.get(i);
            if (celda.getFila() == fila && celda.getColumna() == columna) {
                return celda;
            }
        }
        return null;
    }

    private static void validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
    }
}
