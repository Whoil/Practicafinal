package modelo.mapa;

/**
 * Contrato publico de una celda del mapa.
 *
 * Parte B y Parte C pueden consultar posicion, tipo y transitabilidad sin
 * conocer como se guarda la celda dentro de la matriz propia. El unico cambio
 * permitido desde fuera es modificar el TipoCelda, por ejemplo cuando una
 * cueva se construya desde JSON o cuando una puerta/trampa cambie de estado.
 */
public interface InterfazCelda {

    /**
     * Devuelve la posicion inmutable de la celda dentro de su cueva.
     *
     * La posicion identifica la casilla aunque cambie su tipo.
     */
    Posicion getPosicion();

    /**
     * Devuelve la fila de la celda.
     *
     * Es un acceso directo para no obligar a otros modulos a pedir primero la
     * Posicion completa.
     */
    int getFila();

    /**
     * Devuelve la columna de la celda.
     *
     * Junto con getFila(), ubica la celda dentro de la matriz.
     */
    int getColumna();

    /**
     * Devuelve el tipo actual de la celda.
     *
     * El tipo indica si es suelo, muro, puerta, trampa, tesoro, inicio o salida.
     */
    TipoCelda getTipo();

    /**
     * Cambia el tipo actual de la celda.
     *
     * Se usa para construir la cueva y para cambios estructurales simples. No
     * debe usarse para guardar personajes u objetos complejos sin coordinarlo
     * con Parte B.
     */
    void setTipo(TipoCelda tipo);

    /**
     * Indica si la celda puede pisarse.
     *
     * Ahora mismo MURO bloquea el paso; otros tipos se consideran transitables
     * para que la logica de juego decida sus efectos despues.
     */
    boolean esTransitable();
}
