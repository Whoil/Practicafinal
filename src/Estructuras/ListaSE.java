package Estructuras;

/**
 * Lista simplemente enlazada propia.
 *
 * Esta estructura es una de las bases de Parte A. Se usa para representar
 * secuencias sin recurrir a ArrayList, LinkedList ni otras colecciones de Java.
 * La implementacion solo mantiene referencia al primer nodo, por lo que:
 *
 * - insertar al principio es O(1)
 * - insertar al final es O(n)
 * - acceder por posicion es O(n)
 * - buscar por valor es O(n)
 *
 * A diferencia de la version inicial de la practica anterior, esta lista no
 * exige que T implemente Comparable. Una lista general no necesita ordenar sus
 * elementos; para buscar o borrar basta con igualdad mediante equals. Esto es
 * importante para el juego porque clases como Celda, Cueva o Posicion no deben
 * fingir un orden natural si solo necesitan almacenarse en una estructura.
 */
public class ListaSE<T> implements Lista<T> {
    /*
     * Primer nodo de la lista. Desde aqui se recorren todos los elementos.
     */
    protected ElementoSE<T> primero;

    /*
     * Tamano mantenido de forma incremental para consultar getSize() en O(1).
     */
    protected int size;

    public ListaSE() {
        this.primero = null;
        this.size = 0;
    }

    @Override
    public void add(T dato) {
        /*
         * Insertar al principio solo requiere enlazar el nuevo nodo con el
         * antiguo primero. Por eso esta operacion es constante.
         */
        ElementoSE<T> nuevo = new ElementoSE<>(dato);
        nuevo.setSiguiente(primero);
        primero = nuevo;
        size++;
    }

    @Override
    public void addLast(T dato) {
        /*
         * Como la lista no guarda referencia al ultimo nodo, para insertar al
         * final hay que recorrer desde primero hasta encontrar el nodo final.
         */
        ElementoSE<T> nuevo = new ElementoSE<>(dato);
        if (primero == null) {
            primero = nuevo;
        } else {
            ElementoSE<T> actual = primero;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        size++;
    }

    @Override
    public T get(T dato) {
        /*
         * Busqueda secuencial. Se compara por equals para permitir tipos sin
         * Comparable, como las futuras clases del mapa.
         */
        ElementoSE<T> actual = primero;
        while (actual != null) {
            if (sonIguales(actual.getDato(), dato)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    @Override
    public T get(int posicion) {
        /*
         * Acceso por indice basado en recorrido. En la matriz de Cueva se usa
         * dos veces: una para localizar la fila y otra para localizar la celda.
         */
        if (posicion < 0 || posicion >= size) {
            return null;
        }
        ElementoSE<T> actual = primero;
        int indice = 0;
        while (indice < posicion) {
            actual = actual.getSiguiente();
            indice++;
        }
        return actual.getDato();
    }

    @Override
    public T del(T dato) {
        /*
         * Para borrar en una lista simplemente enlazada se necesita recordar el
         * nodo anterior. Asi se puede saltar el nodo eliminado sin recorrer la
         * lista hacia atras, cosa que esta estructura no permite.
         */
        ElementoSE<T> actual = primero;
        ElementoSE<T> anterior = null;
        while (actual != null) {
            if (sonIguales(actual.getDato(), dato)) {
                if (anterior == null) {
                    primero = actual.getSiguiente();
                } else {
                    anterior.setSiguiente(actual.getSiguiente());
                }
                size--;
                return actual.getDato();
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        return null;
    }

    @Override
    public T delFirst() {
        if (primero == null) {
            return null;
        }
        T dato = primero.getDato();
        primero = primero.getSiguiente();
        size--;
        return dato;
    }

    @Override
    public T delLast() {
        if (primero == null) {
            return null;
        }
        if (primero.getSiguiente() == null) {
            T dato = primero.getDato();
            primero = null;
            size--;
            return dato;
        }
        ElementoSE<T> actual = primero;
        while (actual.getSiguiente().getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        T dato = actual.getSiguiente().getDato();
        actual.setSiguiente(null);
        size--;
        return dato;
    }

    @Override
    public boolean isEmpty() {
        return primero == null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void clear() {
        primero = null;
        size = 0;
    }

    @Override
    public ListaSE<T> copy() {
        ListaSE<T> copia = new ListaSE<>();
        ElementoSE<T> actual = primero;
        while (actual != null) {
            copia.addLast(actual.getDato());
            actual = actual.getSiguiente();
        }
        return copia;
    }

    @Override
    public MiIterador<T> getIterador() {
        return new IteradorSE<>(primero);
    }

    public boolean existeDato(T dato) {
        return get(dato) != null;
    }

    public void invertir() {
        /*
         * Inversion iterativa de enlaces. Se guarda el siguiente antes de
         * cambiarlo para no perder el resto de la cadena.
         */
        ElementoSE<T> anterior = null;
        ElementoSE<T> actual = primero;
        while (actual != null) {
            ElementoSE<T> siguiente = actual.getSiguiente();
            actual.setSiguiente(anterior);
            anterior = actual;
            actual = siguiente;
        }
        primero = anterior;
    }

    private boolean sonIguales(T actual, T buscado) {
        if (actual == null) {
            return buscado == null;
        }
        return actual.equals(buscado);
    }

    @Override
    public String toString() {
        String texto = "[";
        ElementoSE<T> actual = primero;
        while (actual != null) {
            texto += actual.getDato();
            if (actual.getSiguiente() != null) {
                texto += ", ";
            }
            actual = actual.getSiguiente();
        }
        texto += "]";
        return texto;
    }
}
