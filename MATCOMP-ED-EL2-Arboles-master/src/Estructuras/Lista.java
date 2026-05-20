package Estructuras;

// Interfaz para listas
public interface Lista<T> {

    void add(T dato);           // Inserta (en vuestro caso, al principio)
    void addLast(T dato);       // Inserta al final
    T get(T dato);              // Busca por valor
    T get(int posicion);        // Acceso por posición
    T del(T dato);              // Borra por valor
    T delFirst();               // Borra el primero
    T delLast();                // Borra el último
    boolean isEmpty();          // Comprueba si está vacía
    int getSize();              // Devuelve el tamaño
    void clear();               // Vacía la lista
    Lista<T> copy();            // Devuelve una copia de la lista
    MiIterador<T> getIterador();// Devuelve un iterador
}