package ParteB.Grafo;

public interface InterfazArco<T extends Comparable<T>> {
    long getId();
    void setId(long id);
    String getDato();
    void setDato(String dato);
    Nodo<T> getOrigen();
    void setOrigen(Nodo<T> origen);
    Nodo<T> getDestino();
    void setDestino(Nodo<T> destino);
}
