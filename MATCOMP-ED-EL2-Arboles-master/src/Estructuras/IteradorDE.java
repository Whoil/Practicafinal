package Estructuras;

public class IteradorDE<T> implements MiIterador<T> {
    private ElementoDE<T> actual;

    public IteradorDE(ElementoDE<T> comienzo) {
        actual = comienzo;
    }

    @Override
    public boolean hasNext(){
        return actual != null;
    }

    @Override
    public T next(){
        if (!hasNext()) return null;
        T dato = actual.getDato();
        actual = actual.getSiguiente();
        return dato;
    }

}
