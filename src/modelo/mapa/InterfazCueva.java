package modelo.mapa;

import Estructuras.ListaSE;

/**
 * Contrato publico de Cueva para las otras partes del proyecto.
 *
 * La implementacion concreta queda en Cueva, pero este archivo resume los
 * metodos disponibles para logica, interfaz y JSON. Tambien deja claro que la
 * matriz evaluable se expone como ListaSE<ListaSE<Celda>>, no como array ni
 * como coleccion externa.
 */
public interface InterfazCueva {

    /**
     * Devuelve el identificador estable de la cueva.
     *
     * Este id servira para distinguir niveles dentro de la mazmorra y para que
     * JSON o el grafo de cuevas puedan referirse a una cueva concreta.
     */
    String getId();

    /**
     * Devuelve el numero de filas de la matriz.
     *
     * Parte C puede usarlo para dibujar la cueva y Parte B para validar
     * posiciones sin recorrer la estructura completa.
     */
    int getFilas();

    /**
     * Devuelve el numero de columnas de la matriz.
     *
     * Junto con getFilas(), define los limites rectangulares de la cueva.
     */
    int getColumnas();

    /**
     * Devuelve una vista estructural de la matriz propia de celdas.
     *
     * La estructura es ListaSE<ListaSE<Celda>>: lista de filas y, dentro de cada
     * fila, lista de celdas. No se devuelven arrays ni colecciones externas.
     * La implementacion devuelve una copia de las listas para que otros modulos
     * no puedan borrar filas o columnas internas de la cueva.
     */
    ListaSE<ListaSE<Celda>> getMatriz();

    /**
     * Indica si una posicion pertenece a la cueva.
     *
     * Devuelve false para coordenadas negativas o fuera de filas/columnas.
     * No lanza excepcion porque se usa mucho al comprobar vecinos de BFS.
     */
    boolean estaDentro(int fila, int columna);

    /**
     * Devuelve la celda situada en unas coordenadas concretas.
     *
     * Si la posicion esta fuera de la cueva, la implementacion lanza una
     * excepcion para detectar errores de uso.
     */
    Celda getCelda(int fila, int columna);

    /**
     * Cambia el tipo de una celda existente.
     *
     * No sustituye la celda dentro de la matriz: solo cambia su TipoCelda. Esto
     * permite construir mapas desde JSON o modificar casillas sin romper
     * referencias existentes.
     */
    void cambiarTipoCelda(int fila, int columna, TipoCelda tipo);

    /**
     * Indica si una posicion puede pisarse.
     *
     * Devuelve false si la posicion queda fuera de la cueva o si la celda no es
     * transitable, por ejemplo un MURO.
     */
    boolean esTransitable(int fila, int columna);

    /**
     * Calcula las celdas alcanzables desde una celda inicial usando BFS.
     *
     * pasosMaximos limita el numero de movimientos. La celda inicial se incluye
     * en el resultado para permitir que una unidad decida no moverse.
     */
    ListaSE<Celda> getCeldasAlcanzables(int filaInicio, int columnaInicio, int pasosMaximos);

    /**
     * Devuelve el camino minimo entre dos celdas, calculado con BFS.
     *
     * El camino incluye origen y destino. Si no existe camino transitable, la
     * lista devuelta esta vacia.
     */
    ListaSE<Celda> getCaminoMinimo(int filaInicio, int columnaInicio, int filaDestino, int columnaDestino);

    /**
     * Devuelve la distancia minima en pasos entre dos celdas.
     *
     * Si origen y destino son la misma celda, devuelve 0. Si no existe camino,
     * devuelve -1.
     */
    int getDistanciaMinima(int filaInicio, int columnaInicio, int filaDestino, int columnaDestino);
}
