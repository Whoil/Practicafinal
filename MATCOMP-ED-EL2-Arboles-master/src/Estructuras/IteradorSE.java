package Estructuras;

public class IteradorSE<T> implements MiIterador<T> {
    private ElementoSE<T> actual;

    public IteradorSE(ElementoSE<T> comienzo) {
        actual = comienzo;
    }
    @Override
    public boolean hasNext(){
        return actual != null;
    }
    @Override
    public T next(){
        if (!hasNext()){
            return null;
        }
        T dato = actual.getDato();
        actual = actual.getSiguiente();
        return dato;
    }
}
