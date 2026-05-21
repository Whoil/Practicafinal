package Estructuras;

/**
 * Nodo interno del grafo propio.
 *
 * Guarda el dato del dominio, por ejemplo una Cueva, y sus arcos de salida.
 * No exige Comparable porque el grafo no ordena nodos: solo necesita comparar
 * igualdad mediante equals().
 */
public class NodoGrafo<T> {
    private final long id;
    private final T dato;
    private final ListaSE<ArcoGrafo<T>> arcosSalida;

    public NodoGrafo(long id, T dato) {
        this.id = id;
        this.dato = dato;
        this.arcosSalida = new ListaSE<>();
    }

    public long getId() {
        return id;
    }

    public T getDato() {
        return dato;
    }

    /**
     * Devuelve una copia de la lista de arcos salientes.
     *
     * La copia protege la lista interna del nodo para que otros modulos no
     * puedan borrar conexiones directamente.
     */
    public ListaSE<ArcoGrafo<T>> getArcosSalida() {
        return arcosSalida.copy();
    }

    void addArcoSalida(ArcoGrafo<T> arco) {
        arcosSalida.addLast(arco);
    }

    ListaSE<ArcoGrafo<T>> getArcosSalidaInternos() {
        return arcosSalida;
    }

    @Override
    public String toString() {
        return "(" + id + ", " + dato + ")";
    }
}
