package ParteA;
import Estructuras.ListaSE;

public interface ArbolBinario < T extends Comparable<T>>{
    boolean isEmpty();

    void add(T dato);

    Nodo<T> getRaiz();

    int getAlturaRaiz();

    int getGrado();

    ListaSE<T> getListaPreOrden();

    ListaSE<T> getListaOrdenCentral();

    ListaSE<T> getListaPostOrden();

    ArbolBinarioDeBusqueda<T> getSubArbolIzquierda();

    ArbolBinarioDeBusqueda<T> getSubArbolDerecha();

    ListaSE<T> getCamino(T dato);

    ListaSE<T> getListaDatosNivel(int nivel);

    boolean isArbolHomogeneo();

    boolean isArbolCompleto();

    boolean isArbolCasiCompleto();

    boolean isEquilibrado();

}
