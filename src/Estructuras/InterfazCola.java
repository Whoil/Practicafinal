package Estructuras;

/**
 * Contrato publico de la cola propia.
 *
 * Se usa especialmente para BFS. La interfaz deja claro que las operaciones
 * disponibles son las de una cola FIFO basica y que no dependemos de
 * java.util.Queue ni de otras estructuras externas.
 */
public interface InterfazCola<T> {

    /**
     * Inserta un dato al final de la cola.
     *
     * En una cola FIFO, este dato sera procesado despues de todos los que ya
     * estaban encolados.
     */
    void offer(T dato);

    /**
     * Extrae y devuelve el dato del frente de la cola.
     *
     * Si la cola esta vacia, devuelve null.
     */
    T poll();

    /**
     * Consulta el dato del frente sin eliminarlo.
     *
     * Si la cola esta vacia, devuelve null.
     */
    T peek();

    /**
     * Indica si la cola no contiene elementos.
     */
    boolean isEmpty();

    /**
     * Devuelve el numero actual de elementos encolados.
     */
    int getSize();

    /**
     * Elimina todos los elementos de la cola.
     */
    void clear();
}
