package Estructuras;

/*
 * Esta clase implementa un iterador circular para recorrer una lista circular.
 * Permite avanzar nodo a nodo y, al llegar al final, controla que no se repita
 * indefinidamente el recorrido completo.
 */
public class IteradorCircular<T extends Comparable<T>> implements InterfazIteradorCircular<T> {

    /*
     * Referencia al primer nodo de la lista.
     * Se usa para saber dónde empieza la lista y detectar
     * cuándo hemos dado una vuelta completa.
     */
    private NodoCircular<T> primero;

    /*
     * Nodo actual en el que se encuentra el iterador.
     * Va avanzando conforme se llama a next().
     */
    private NodoCircular<T> actual;

    /*
     * Indica si estamos en la primera vuelta del recorrido.
     * Sirve para permitir empezar desde el principio sin cortar el recorrido.
     */
    private boolean primeraVuelta;

    /*
     * Constructor del iterador.
     * Inicializa el iterador apuntando al primer nodo de la lista.
     */
    public IteradorCircular(NodoCircular<T> primero) {
        this.primero = primero;
        this.actual = primero;
        this.primeraVuelta = true;
    }

    @Override
    public boolean hasNext() {

        /*
         * Si la lista está vacía (no hay nodos),
         * no hay nada que recorrer.
         */
        if (actual == null) {
            return false;
        }

        /*
         * Podemos seguir avanzando si:
         * - Estamos en la primera vuelta, o
         * - Aún no hemos vuelto al nodo inicial.
         *
         * Esto evita recorrer la lista en bucle infinito.
         */
        return primeraVuelta || actual != primero;
    }

    @Override
    public T next() {

        /*
         * Si no hay más elementos disponibles,
         * devolvemos null.
         */
        if (!hasNext()) {
            return null;
        }

        /*
         * Guardamos el dato del nodo actual
         * antes de avanzar.
         */
        T dato = actual.dato;

        /*
         * Pasamos al siguiente nodo de la lista.
         */
        actual = actual.siguiente;

        /*
         * Indicamos que ya hemos comenzado el recorrido.
         */
        primeraVuelta = false;

        return dato;
    }

    @Override
    public void reset() {

        /*
         * Reiniciamos el iterador:
         * volvemos al primer nodo y marcamos
         * que es la primera vuelta otra vez.
         */
        actual = primero;
        primeraVuelta = true;
    }
}
