package Estructuras;

/**
 * Grafo propio dirigido basado en ListaSE y Cola propias.
 *
 * Este grafo prepara la base de A-03. La mazmorra podra contener un
 * Grafo<Cueva>, donde cada arco indique una puerta de avance entre niveles.
 * Como las puertas acordadas son unidireccionales, el grafo respeta la direccion
 * de los arcos en adyacentes, BFS y camino minimo.
 *
 * No usa HashMap, HashSet, ArrayList, LinkedList ni java.util.Queue. Las
 * busquedas se hacen recorriendo ListaSE, por lo que algunas operaciones son
     * O(n), pero el comportamiento es claro, propio y defendible.
 *
 * Politica de datos nulos: no se aceptan nodos null. Un grafo de la mazmorra
 * debe contener cuevas reales, no huecos. Rechazar null evita incoherencias en
 * BFS, camino minimo y reconstruccion de padres.
 */
public class Grafo<T> implements InterfazGrafo<T> {
    private final ListaSE<NodoGrafo<T>> nodos;
    private final ListaSE<ArcoGrafo<T>> arcos;
    private long siguienteIdNodo;
    private long siguienteIdArco;

    public Grafo() {
        this.nodos = new ListaSE<>();
        this.arcos = new ListaSE<>();
        this.siguienteIdNodo = 1;
        this.siguienteIdArco = 1;
    }

    @Override
    public boolean addNodo(T dato) {
        validarDatoNoNulo(dato, "dato");
        if (existeNodo(dato)) {
            return false;
        }
        nodos.addLast(new NodoGrafo<>(siguienteIdNodo, dato));
        siguienteIdNodo++;
        return true;
    }

    @Override
    public boolean addArco(T origen, T destino, String etiqueta) {
        validarDatoNoNulo(origen, "origen");
        validarDatoNoNulo(destino, "destino");
        NodoGrafo<T> nodoOrigen = obtenerOCrearNodo(origen);
        NodoGrafo<T> nodoDestino = obtenerOCrearNodo(destino);

        if (existeArco(origen, destino, etiqueta)) {
            return false;
        }

        ArcoGrafo<T> arco = new ArcoGrafo<>(siguienteIdArco, etiqueta, nodoOrigen, nodoDestino);
        arcos.addLast(arco);
        nodoOrigen.addArcoSalida(arco);
        siguienteIdArco++;
        return true;
    }

    @Override
    public boolean existeNodo(T dato) {
        return buscarNodo(dato) != null;
    }

    @Override
    public boolean existeConexion(T origen, T destino) {
        NodoGrafo<T> nodoOrigen = buscarNodo(origen);
        NodoGrafo<T> nodoDestino = buscarNodo(destino);
        if (nodoOrigen == null || nodoDestino == null) {
            return false;
        }

        ListaSE<ArcoGrafo<T>> salidas = nodoOrigen.getArcosSalidaInternos();
        for (int indice = 0; indice < salidas.getSize(); indice++) {
            ArcoGrafo<T> arco = salidas.get(indice);
            if (sonIguales(arco.getDestino().getDato(), nodoDestino.getDato())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existeArco(T origen, T destino, String etiqueta) {
        for (int indice = 0; indice < arcos.getSize(); indice++) {
            ArcoGrafo<T> arco = arcos.get(indice);
            if (sonIguales(arco.getOrigen().getDato(), origen)
                    && sonIguales(arco.getDestino().getDato(), destino)
                    && sonIguales(arco.getEtiqueta(), etiqueta)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ListaSE<T> getAdyacentes(T dato) {
        ListaSE<T> adyacentes = new ListaSE<>();
        NodoGrafo<T> nodo = buscarNodo(dato);
        if (nodo == null) {
            return adyacentes;
        }

        ListaSE<ArcoGrafo<T>> salidas = nodo.getArcosSalidaInternos();
        for (int indice = 0; indice < salidas.getSize(); indice++) {
            adyacentes.addLast(salidas.get(indice).getDestino().getDato());
        }
        return adyacentes;
    }

    @Override
    public ListaSE<T> recorridoBFS(T inicio) {
        ListaSE<T> visitados = new ListaSE<>();
        if (!existeNodo(inicio)) {
            return visitados;
        }

        Cola<T> pendientes = new Cola<>();
        pendientes.offer(inicio);
        visitados.addLast(inicio);

        while (!pendientes.isEmpty()) {
            T actual = pendientes.poll();
            ListaSE<T> adyacentes = getAdyacentes(actual);

            for (int indice = 0; indice < adyacentes.getSize(); indice++) {
                T adyacente = adyacentes.get(indice);
                if (!visitados.existeDato(adyacente)) {
                    visitados.addLast(adyacente);
                    pendientes.offer(adyacente);
                }
            }
        }

        return visitados;
    }

    @Override
    public boolean existeCamino(T inicio, T destino) {
        return recorridoBFS(inicio).existeDato(destino);
    }

    @Override
    public ListaSE<T> caminoMinimo(T inicio, T destino) {
        ListaSE<T> caminoVacio = new ListaSE<>();
        if (!existeNodo(inicio) || !existeNodo(destino)) {
            return caminoVacio;
        }

        Cola<T> pendientes = new Cola<>();
        ListaSE<T> visitados = new ListaSE<>();
        ListaSE<PasoGrafo<T>> padres = new ListaSE<>();

        pendientes.offer(inicio);
        visitados.addLast(inicio);
        padres.addLast(new PasoGrafo<>(inicio, null));

        while (!pendientes.isEmpty()) {
            T actual = pendientes.poll();
            if (sonIguales(actual, destino)) {
                return reconstruirCamino(destino, padres);
            }

            ListaSE<T> adyacentes = getAdyacentes(actual);
            for (int indice = 0; indice < adyacentes.getSize(); indice++) {
                T adyacente = adyacentes.get(indice);
                if (!visitados.existeDato(adyacente)) {
                    visitados.addLast(adyacente);
                    padres.addLast(new PasoGrafo<>(adyacente, actual));
                    pendientes.offer(adyacente);
                }
            }
        }

        return caminoVacio;
    }

    @Override
    public int getDistanciaMinima(T inicio, T destino) {
        ListaSE<T> camino = caminoMinimo(inicio, destino);
        if (camino.isEmpty()) {
            return -1;
        }
        return camino.getSize() - 1;
    }

    @Override
    public ListaSE<T> getNodos() {
        ListaSE<T> copia = new ListaSE<>();
        for (int indice = 0; indice < nodos.getSize(); indice++) {
            copia.addLast(nodos.get(indice).getDato());
        }
        return copia;
    }

    @Override
    public ListaSE<ArcoGrafo<T>> getArcos() {
        return arcos.copy();
    }

    @Override
    public int getNumeroNodos() {
        return nodos.getSize();
    }

    @Override
    public int getNumeroArcos() {
        return arcos.getSize();
    }

    private NodoGrafo<T> obtenerOCrearNodo(T dato) {
        NodoGrafo<T> nodo = buscarNodo(dato);
        if (nodo != null) {
            return nodo;
        }
        NodoGrafo<T> nuevo = new NodoGrafo<>(siguienteIdNodo, dato);
        nodos.addLast(nuevo);
        siguienteIdNodo++;
        return nuevo;
    }

    private NodoGrafo<T> buscarNodo(T dato) {
        for (int indice = 0; indice < nodos.getSize(); indice++) {
            NodoGrafo<T> nodo = nodos.get(indice);
            if (sonIguales(nodo.getDato(), dato)) {
                return nodo;
            }
        }
        return null;
    }

    private ListaSE<T> reconstruirCamino(T destino, ListaSE<PasoGrafo<T>> padres) {
        ListaSE<T> camino = new ListaSE<>();
        T actual = destino;

        while (actual != null) {
            camino.addLast(actual);
            actual = buscarPadre(actual, padres);
        }

        camino.invertir();
        return camino;
    }

    private T buscarPadre(T dato, ListaSE<PasoGrafo<T>> padres) {
        for (int indice = 0; indice < padres.getSize(); indice++) {
            PasoGrafo<T> paso = padres.get(indice);
            if (sonIguales(paso.getDato(), dato)) {
                return paso.getPadre();
            }
        }
        return null;
    }

    private boolean sonIguales(Object actual, Object buscado) {
        if (actual == null) {
            return buscado == null;
        }
        return actual.equals(buscado);
    }

    private void validarDatoNoNulo(T dato, String nombre) {
        if (dato == null) {
            throw new IllegalArgumentException("El " + nombre + " del grafo no puede ser null");
        }
    }

    /**
     * Relacion nodo -> padre usada para reconstruir camino minimo con BFS.
     *
     * Evita usar mapas externos; la busqueda del padre se hace linealmente en
     * una ListaSE.
     */
    private static class PasoGrafo<T> {
        private final T dato;
        private final T padre;

        private PasoGrafo(T dato, T padre) {
            this.dato = dato;
            this.padre = padre;
        }

        private T getDato() {
            return dato;
        }

        private T getPadre() {
            return padre;
        }
    }
}
