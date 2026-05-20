package Estructuras;

/*
 * Implementación de una lista circular genérica.
 * Los elementos deben poder compararse (Comparable)
 * para poder buscarlos y eliminarlos.
 */
public class ListaCircular<T extends Comparable<T>> implements InterfazListaCircular<T> {

    /*
     * Referencia al primer nodo de la lista.
     * Es el punto de entrada al recorrido.
     */
    protected NodoCircular<T> primero;

    /*
     * Referencia al último nodo de la lista.
     * Nos permite insertar rápidamente al final.
     */
    protected NodoCircular<T> ultimo;

    /*
     * Guarda el número actual de elementos en la lista.
     */
    protected int tamano = 0;

    @Override
    public boolean isEmpty() {

        /*
         * La lista está vacía si no hay primer nodo.
         */
        return primero == null;
    }

    @Override
    public int getSize() {

        /*
         * Devuelve el tamaño actual de la lista.
         */
        return tamano;
    }

    @Override
    public void add(T dato) {

        /*
         * Creamos un nuevo nodo con el dato a insertar.
         */
        NodoCircular<T> nuevo = new NodoCircular<>(dato);

        /*
         * Si la lista está vacía:
         * el nuevo nodo será el primero y el último.
         */
        if (primero == null) {
            primero = nuevo;
            ultimo = nuevo;

            /*
             * Al ser circular, el nodo apunta a sí mismo.
             */
            primero.siguiente = primero;
        } else {

            /*
             * Si ya hay elementos:
             * enlazamos el nuevo nodo al final.
             */
            ultimo.siguiente = nuevo;
            ultimo = nuevo;

            /*
             * El último siempre apunta al primero
             * para mantener la estructura circular.
             */
            ultimo.siguiente = primero;
        }

        /*
         * Aumentamos el tamaño de la lista.
         */
        tamano++;
    }

    @Override
    public T get(T dato) {

        /*
         * Si la lista está vacía, no hay nada que buscar.
         */
        if (primero == null) {
            return null;
        }

        /*
         * Empezamos desde el primer nodo.
         */
        NodoCircular<T> aux = primero;

        /*
         * Recorremos toda la lista.
         * Usamos do-while porque al menos hay un nodo.
         */
        do {

            /*
             * Si encontramos el dato, lo devolvemos.
             */
            if (aux.dato.compareTo(dato) == 0) {
                return aux.dato;
            }

            /*
             * Avanzamos al siguiente nodo.
             */
            aux = aux.siguiente;

        } while (aux != primero);

        /*
         * Si no se encuentra, devolvemos null.
         */
        return null;
    }

    @Override
    public T del(T dato) {

        /*
         * Si la lista está vacía, no hay nada que eliminar.
         */
        if (primero == null) {
            return null;
        }

        /*
         * Caso especial: solo hay un nodo.
         */
        if (primero == ultimo) {

            /*
             * Si coincide el dato, eliminamos el único nodo.
             */
            if (primero.dato.compareTo(dato) == 0) {
                T eliminado = primero.dato;

                /*
                 * La lista queda vacía.
                 */
                primero = null;
                ultimo = null;
                tamano--;

                return eliminado;
            }

            return null;
        }

        /*
         * Nodo actual que estamos revisando.
         */
        NodoCircular<T> actual = primero;

        /*
         * Nodo anterior al actual.
         * Empieza siendo el último.
         */
        NodoCircular<T> anterior = ultimo;

        /*
         * Recorremos toda la lista.
         */
        do {

            /*
             * Si encontramos el nodo a eliminar.
             */
            if (actual.dato.compareTo(dato) == 0) {
                T eliminado = actual.dato;

                /*
                 * Si es el primer nodo.
                 */
                if (actual == primero) {
                    primero = primero.siguiente;
                    ultimo.siguiente = primero;
                }

                /*
                 * Si es el último nodo.
                 */
                else if (actual == ultimo) {
                    ultimo = anterior;
                    ultimo.siguiente = primero;
                }

                /*
                 * Si está en medio, lo saltamos.
                 */
                else {
                    anterior.siguiente = actual.siguiente;
                }

                /*
                 * Reducimos el tamaño.
                 */
                tamano--;

                return eliminado;
            }

            /*
             * Avanzamos en el recorrido.
             */
            anterior = actual;
            actual = actual.siguiente;

        } while (actual != primero);

        /*
         * Si no se encuentra el dato.
         */
        return null;
    }

    @Override
    public InterfazIteradorCircular<T> iteratorCircular() {

        /*
         * Devuelve un iterador para recorrer la lista.
         */
        return new IteradorCircular<>(primero);
    }
}
