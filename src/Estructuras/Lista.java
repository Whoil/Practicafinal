package Estructuras;

public interface Lista<T> {
    void add(T dato);
    void addLast(T dato);
    T get(T dato);
    T get(int posicion);
    T del(T dato);
    T delFirst();
    T delLast();
    boolean isEmpty();
    int getSize();
    void clear();
    Lista<T> copy();
    MiIterador<T> getIterador();
}
