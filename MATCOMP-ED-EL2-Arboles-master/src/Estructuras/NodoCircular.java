package Estructuras;

/*
 * Esta clase representa un nodo de una lista circular.
 * Cada nodo guarda un dato y una referencia al siguiente nodo.
 */
public class NodoCircular<T> {

    /*
     * Dato almacenado en el nodo.
     * Es el valor que contiene este elemento de la lista.
     */
    protected T dato;

    /*
     * Referencia al siguiente nodo de la lista.
     * En una lista circular, el último nodo apunta al primero.
     */
    protected NodoCircular<T> siguiente;

    /*
     * Constructor del nodo.
     * Inicializa el nodo con un dato y sin enlace al siguiente (null).
     * Luego, la lista se encargará de enlazarlo correctamente.
     */
    public NodoCircular(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }
}
