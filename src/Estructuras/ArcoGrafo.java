package Estructuras;

/**
 * Arco dirigido del grafo propio.
 *
 * En A-03 representara una puerta o conexion entre cuevas. Es inmutable para
 * que una conexion creada no cambie de origen/destino desde fuera del grafo.
 */
public class ArcoGrafo<T> {
    private final long id;
    private final String etiqueta;
    private final NodoGrafo<T> origen;
    private final NodoGrafo<T> destino;

    public ArcoGrafo(long id, String etiqueta, NodoGrafo<T> origen, NodoGrafo<T> destino) {
        this.id = id;
        this.etiqueta = etiqueta;
        this.origen = origen;
        this.destino = destino;
    }

    public long getId() {
        return id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public NodoGrafo<T> getOrigen() {
        return origen;
    }

    public NodoGrafo<T> getDestino() {
        return destino;
    }

    @Override
    public String toString() {
        return origen + " --" + etiqueta + "--> " + destino;
    }
}
