package Estructuras;

public class ElementoSE<T> {
    private T dato;
    private ElementoSE<T> siguiente;

    public ElementoSE(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public T getDato() {
        return dato;
    }


    public ElementoSE<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(ElementoSE<T> siguiente) {
        this.siguiente = siguiente;
    }


}
