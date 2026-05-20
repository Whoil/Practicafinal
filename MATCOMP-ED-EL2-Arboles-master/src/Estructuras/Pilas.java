package Estructuras;

public interface Pilas<T extends Comparable<T>> {
    void push(T dato);
    public T pop();
    public T peek();
    public boolean isEmpty();
    public int getSize();
    public void clear();
    public boolean contains(T dato);
    public String toString();
    public T bottom();
    public void reverse();
    public int search(T dato);
    public Pilas<T> copy();
    public T min();
    public T max();
}
