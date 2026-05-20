package ParteB.Grafo;

import Estructuras.ListaSE;

public interface InterfazGrafo<T extends Comparable<T>> {
    ListaSE<Nodo<T>> getNodos();
    ListaSE<Arco<T>> getArcos();
    long getIdNodo();
    long getIdArco();
    Nodo<T> buscarNodo(T dato);
    boolean existeNodo(T dato);
    Nodo<T> addNodo(T dato);
    Arco<T> buscarArco(Nodo<T> origen, Nodo<T> destino, String dato);
    boolean existeArco(Nodo<T> origen, Nodo<T> destino, String dato);
    Arco<T> addArco(T datoOrigen, T datoDestino, String dato);
    ListaSE<T> getAdyacentes(T dato);
    ListaSE<T> recorridoBFS(T inicio);
    ListaSE<T> caminoMinimo(T inicio, T fin);
    ListaSE<T> getVecinosNoDirigido(T dato);
    ListaSE<T> recorridoBFSNoDirigido(T inicio);
    boolean isDisjunto();
}
