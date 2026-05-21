package modelo.mapa;

/**
 * Contrato publico de una posicion de la cueva.
 *
 * Esta interfaz existe para que las otras partes del grupo sepan que una
 * posicion solo ofrece coordenadas de lectura. Nadie debe modificar fila o
 * columna una vez creada, porque las posiciones se usan para identificar
 * celdas durante BFS y camino minimo.
 */
public interface InterfazPosicion {

    /**
     * Devuelve la fila de la posicion.
     *
     * Las filas empiezan en 0 y crecen hacia abajo en la matriz.
     */
    int getFila();

    /**
     * Devuelve la columna de la posicion.
     *
     * Las columnas empiezan en 0 y crecen hacia la derecha en la matriz.
     */
    int getColumna();
}
