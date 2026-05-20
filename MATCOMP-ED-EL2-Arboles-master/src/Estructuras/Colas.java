package Estructuras;

public interface Colas<T extends Comparable<T>>{
    void offer(T dato);
    public T poll();
    public T peek();
    public boolean isEmpty();
    public int getSize();
    public void clear();
    public boolean contains(T dato);
    public String toString();
    public T last();
    public void rotate(int times);
    public Cola<T> copy();
    public T min();
    public T max();
}
