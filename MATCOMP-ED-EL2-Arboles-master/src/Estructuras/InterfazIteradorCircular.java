package Estructuras;

/*
 * Esta interfaz define el comportamiento de un iterador circular.
 * Un iterador circular permite recorrer elementos y, al llegar al final,
 * puede volver al inicio para seguir recorriendo.
 */
public interface InterfazIteradorCircular<T> {

    /*
     * Comprueba si hay más elementos disponibles en el recorrido.
     * Devuelve true si aún quedan elementos por visitar,
     * o false si no hay ninguno (por ejemplo, si la estructura está vacía).
     */
    boolean hasNext();

    /*
     * Devuelve el siguiente elemento del recorrido.
     * Cada llamada avanza al siguiente elemento.
     * En un iterador circular, al llegar al final,
     * normalmente continúa desde el principio.
     */
    T next();

    /*
     * Reinicia el iterador.
     * Hace que el recorrido vuelva al inicio,
     * como si no se hubiera recorrido ningún elemento.
     */
    void reset();
}
