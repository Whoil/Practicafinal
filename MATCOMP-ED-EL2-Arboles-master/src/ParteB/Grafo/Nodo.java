package ParteB.Grafo;
import Estructuras.ListaSE;

public class Nodo<T extends Comparable<T>> implements InterfazNodo<T>, Comparable<Nodo<T>> {
    private long id; //id para identificar el nodo
    private T dato;
    private ListaSE<Arco<T>> arcosEntrada;
    private ListaSE<Arco<T>> arcosSalida;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public Nodo(long id, T dato) {
        this.id = id;
        this.dato = dato;
        this.arcosEntrada = new ListaSE<>();
        this.arcosSalida = new ListaSE<>();
    }

    public ListaSE<Arco<T>> getArcosEntrada() {
        return arcosEntrada;
    }

    public void setArcosEntrada(ListaSE<Arco<T>> arcosEntrada) {
        this.arcosEntrada = arcosEntrada;
    }

    public ListaSE<Arco<T>> getArcosSalida() {
        return arcosSalida;
    }

    public void setArcosSalida(ListaSE<Arco<T>> arcosSalida) {
        this.arcosSalida = arcosSalida;
    }
    public void addArcoEntrada(Arco<T> arco){
        arcosEntrada.addLast(arco); // añadimos al final de la lista para que queden en orden
    }
    public void addArcoSalida(Arco<T> arco){
        arcosSalida.addLast(arco); // añadimos al final de la lista para que queden en orden

    }
    @Override
    public int compareTo(Nodo<T> otro) {
        return this.dato.compareTo(otro.dato); // comparamos el dato de cada nodo para ver si son el mismo
    }
    @Override
    public String toString(){
        return "("+ id + ", " + dato + ")";
    }
}
