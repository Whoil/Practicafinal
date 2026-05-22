package Estructuras;

public class ElementoDE<T> {
    protected T dato;
    protected ElementoDE<T> anterior;
    protected ElementoDE<T> siguiente;

    public ElementoDE(T dato) {
        this.dato = dato;
        anterior = null;
        siguiente = null;
    }

    public T getDato() {
        return dato;
    }

    public ElementoDE<T> getAnterior() {
        return anterior;
    }

    public void setAnterior(ElementoDE<T> anterior) {
        this.anterior = anterior;
    }

    public ElementoDE<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(ElementoDE<T> siguiente) {
        this.siguiente = siguiente;
    }
}
