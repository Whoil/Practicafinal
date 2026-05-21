package Estructuras;

/**
 * Contrato publico del grafo propio dirigido.
 *
 * Para A-03 se usara como Grafo<Cueva>: cada nodo sera una cueva y cada arco
 * representara una puerta o conexion de avance. El grafo es dirigido porque se
 * ha decidido que al pasar al siguiente nivel no se vuelve automaticamente al
 * anterior.
 */
public interface InterfazGrafo<T> {

    /**
     * Inserta un nodo si no existia previamente.
     *
     * La igualdad se decide con equals(), no con Comparable, para que clases
     * del juego como Cueva no tengan que inventar un orden natural.
     * No se aceptan datos null porque null no puede representar una cueva o
     * entidad valida dentro del grafo.
     */
    boolean addNodo(T dato);

    /**
     * Inserta un arco dirigido desde origen hasta destino.
     *
     * Si origen o destino no existian, se crean. La etiqueta permite describir
     * la conexion, por ejemplo "puerta-nivel-1-2".
     * Origen y destino no pueden ser null.
     */
    boolean addArco(T origen, T destino, String etiqueta);

    /**
     * Indica si existe un nodo con ese dato.
     */
    boolean existeNodo(T dato);

    /**
     * Indica si existe cualquier conexion dirigida origen -> destino.
     *
     * No exige etiqueta concreta, por lo que es comodo para saber si una cueva
     * puede avanzar a otra.
     */
    boolean existeConexion(T origen, T destino);

    /**
     * Indica si existe una conexion dirigida origen -> destino con una etiqueta
     * concreta.
     */
    boolean existeArco(T origen, T destino, String etiqueta);

    /**
     * Devuelve los datos de los nodos conectados por arcos salientes.
     *
     * En una mazmorra, son las cuevas a las que se puede avanzar desde la cueva
     * actual.
     */
    ListaSE<T> getAdyacentes(T dato);

    /**
     * Recorre el grafo por BFS respetando la direccion de los arcos.
     */
    ListaSE<T> recorridoBFS(T inicio);

    /**
     * Indica si destino es alcanzable desde inicio siguiendo arcos dirigidos.
     */
    boolean existeCamino(T inicio, T destino);

    /**
     * Devuelve el camino minimo desde inicio hasta destino, medido en numero de
     * arcos. Si no hay camino, devuelve una lista vacia.
     */
    ListaSE<T> caminoMinimo(T inicio, T destino);

    /**
     * Devuelve la distancia minima en arcos desde inicio hasta destino.
     *
     * Si no hay camino, devuelve -1. Si inicio y destino son el mismo nodo,
     * devuelve 0.
     */
    int getDistanciaMinima(T inicio, T destino);

    /**
     * Devuelve una copia de los datos de los nodos del grafo.
     */
    ListaSE<T> getNodos();

    /**
     * Devuelve una copia de la lista de arcos del grafo.
     */
    ListaSE<ArcoGrafo<T>> getArcos();

    /**
     * Devuelve el numero de nodos.
     */
    int getNumeroNodos();

    /**
     * Devuelve el numero de arcos.
     */
    int getNumeroArcos();
}
