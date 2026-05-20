package ParteB.Grafo;

public class Arco<T extends Comparable<T>> implements InterfazArco<T>, Comparable<Arco<T>> {
    private long id;
    private String dato; // aporta informacion del arco
    private Nodo<T> origen;
    private Nodo<T> destino;

    public Arco(long id, String dato, Nodo<T> origen, Nodo<T> destino) {
        this.id = id;
        this.dato = dato;
        this.origen = origen;
        this.destino = destino;
    }
    @Override
    public String toString() {
        return origen + "--" + dato + "--> " + destino;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public Nodo<T> getOrigen() {
        return origen;
    }

    public void setOrigen(Nodo<T> origen) {
        this.origen = origen;
    }

    public Nodo<T> getDestino() {
        return destino;
    }

    public void setDestino(Nodo<T> destino) {
        this.destino = destino;
    }
    @Override
    public int compareTo(Arco<T> otro) {
        if (this.id < otro.getId()) {
            return -1;
        } else if (this.id > otro.getId()) {
            return 1;
        } else {
            return 0;
        }
    }

}
