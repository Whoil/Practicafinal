package Estructuras;

/**
 * Cola propia basada en nodos enlazados.
 *
 * Una cola sigue la politica FIFO: el primer elemento que entra es el primer
 * elemento que sale. Esta estructura es necesaria para el BFS de Parte A,
 * porque BFS explora por niveles y necesita procesar las posiciones en el
 * mismo orden en que fueron descubiertas.
 *
 * No se usa java.util.Queue, LinkedList ni ninguna coleccion externa. Tampoco
 * se exige Comparable, porque los elementos de una cola no necesitan ordenarse:
 * una Posicion o un paso de BFS solo debe almacenarse y recuperarse en orden de
 * llegada.
 */
public class Cola<T> implements InterfazCola<T> {
    /*
     * Frente de la cola. poll() extrae desde aqui.
     */
    private ElementoSE<T> primero;

    /*
     * Final de la cola. offer() inserta aqui para mantener orden FIFO sin
     * recorrer toda la estructura.
     */
    private ElementoSE<T> ultimo;

    /*
     * Tamano mantenido en O(1), util para tests y para comprobar si queda
     * trabajo pendiente en algoritmos como BFS.
     */
    private int size;

    public Cola() {
        this.primero = null;
        this.ultimo = null;
        this.size = 0;
    }

    /**
     * Inserta un dato al final de la cola.
     *
     * Si la cola estaba vacia, el nuevo nodo es a la vez primero y ultimo. Si
     * ya habia elementos, el antiguo ultimo apunta al nuevo nodo y despues se
     * actualiza la referencia ultimo.
     */
    public void offer(T dato) {
        ElementoSE<T> nuevo = new ElementoSE<>(dato);
        if (isEmpty()) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
        size++;
    }

    /**
     * Extrae el dato situado al frente de la cola.
     *
     * Esta es la operacion clave para BFS: siempre se procesa antes la posicion
     * que fue descubierta antes. Si al extraer se vacia la cola, ultimo tambien
     * se pone a null para no dejar referencias antiguas.
     */
    public T poll() {
        if (isEmpty()) {
            return null;
        }
        T dato = primero.getDato();
        primero = primero.getSiguiente();
        if (primero == null) {
            ultimo = null;
        }
        size--;
        return dato;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return primero.getDato();
    }

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
        ultimo = null;
        size = 0;
    }
}
