package ParteB.Grafo;

import Estructuras.ListaSE;

public interface InterfazNodo<T extends Comparable<T>> {
    long getId();
    void setId(long id);
    T getDato();
    void setDato(T dato);
    ListaSE<Arco<T>> getArcosEntrada();
    void setArcosEntrada(ListaSE<Arco<T>> arcosEntrada);
    ListaSE<Arco<T>> getArcosSalida();
    void setArcosSalida(ListaSE<Arco<T>> arcosSalida);
    void addArcoEntrada(Arco<T> arco);
    void addArcoSalida(Arco<T> arco);
}
