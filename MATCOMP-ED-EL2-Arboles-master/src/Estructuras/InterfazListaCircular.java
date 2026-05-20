package Estructuras;

/*
 * Esta interfaz define las operaciones básicas de una lista circular.
 * Una lista circular es una estructura en la que el último elemento
 * apunta al primero, formando un ciclo.
 */
public interface InterfazListaCircular<T extends Comparable<T>> {

    /*
     * Comprueba si la lista está vacía.
     * Devuelve true si no hay elementos,
     * o false si contiene al menos uno.
     */
    boolean isEmpty();

    /*
     * Devuelve el número total de elementos en la lista.
     * Sirve para saber el tamaño actual de la estructura.
     */
    int getSize();

    /*
     * Añade un nuevo elemento a la lista.
     * El elemento se inserta en la posición correspondiente
     * según la implementación (por ejemplo, al final o de forma ordenada).
     */
    void add(T dato);

    /*
     * Busca un elemento en la lista.
     * Si lo encuentra, lo devuelve.
     * Si no existe, normalmente devolverá null.
     */
    T get(T dato);

    /*
     * Elimina un elemento de la lista.
     * Si el elemento existe, lo borra y lo devuelve.
     * Si no se encuentra, normalmente devolverá null.
     */
    T del(T dato);

    /*
     * Devuelve un iterador circular para recorrer la lista.
     * Este iterador permite avanzar por los elementos
     * y volver al inicio automáticamente al llegar al final.
     */
    InterfazIteradorCircular<T> iteratorCircular();
}
