package Estructuras;

public class ListaDE<T extends Comparable<T>> implements Lista<T> {
    protected ElementoDE<T> primero,ultimo;
    protected int size;

    public ListaDE() { // Constructor,crea una lista vacía
        this.primero = null;
        this.ultimo = null;
        this.size = 0;
    }

    public void addLast(T dato) { // añade al final
        ElementoDE<T> nuevo = new ElementoDE<>(dato); // Creamos el nodo
        if (ultimo == null) { // Estructuras.Lista vacía
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo); // El último apunta al nuevo
            nuevo.setAnterior(ultimo); // El nuevo apunta hacia atrás
            ultimo = nuevo; // Actualizamos el último
        }
        size++;
    }
    @Override
    public void add(T dato) {
        ElementoDE<T> nuevo = new ElementoDE<>(dato); // Creamos un nuevo nodo con el dato
        if (primero == null){ // Si la lista está vacía, el nuevo nodo será el primero y el último
            primero = nuevo;
            ultimo = nuevo;
        }else{ // Si no está vacía, insertamos al principio
            primero.setAnterior(nuevo);
            nuevo.setSiguiente(primero);
            primero = nuevo;

        }
        size++;

    }

    @Override
    public T get(T dato) {
        ElementoDE<T> actual = primero;
        while(actual != null){
            if(actual.getDato().compareTo(dato) == 0){
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null; // Si no está, devolvemos null
    }

    @Override
    public T del(T dato) {
        ElementoDE<T> actual = primero;
        while (actual != null){
            if (actual.getDato().compareTo(dato) == 0){
                if (actual.getAnterior() == null){ // Borrar el primero
                    primero = actual.getSiguiente();
                    if (primero != null){
                        primero.setAnterior(null);
                    }else{
                        ultimo = null;
                    }
                } else if (actual.getSiguiente() == null){ // Borrar el último
                    ultimo = actual.getAnterior();
                    ultimo.setSiguiente(null);
                } else {  // Borrar uno del medio
                    actual.getAnterior().setSiguiente(actual.getSiguiente());
                    actual.getSiguiente().setAnterior(actual.getAnterior());
                }
                size--; // Disminuimos el tamaño
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return primero == null; // La lista está vacía si no hay primer nodo
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public MiIterador<T> getIterador() {
        return new IteradorDE<>(primero); // Devuelve un iterador empezando por el primero
    }

    public boolean existeDato(T dato){
        return get(dato) != null; // Si get devuelve algo distinto de null, existe fel dato
    }


    @Override
    public String toString() {
        String texto = "[";
        ElementoDE<T> actual = primero;

        while (actual != null) {
            texto += actual.getDato();
            if (actual.getSiguiente() != null) {
                texto += ", ";
            }
            actual = actual.getSiguiente();
        }

        texto += "]";
        return texto;
    }
    public T getPrimero() { // devuelve el dato del primer elemento
        if (primero == null) return null;
        return primero.getDato();
    }

    public T getUltimo() { // devuelve el dato del último elemento
        if (ultimo == null) return null;
        return ultimo.getDato();
    }
    @Override
    public void clear() { // limpia la lista
        primero = null;
        ultimo = null;
        size = 0;
    }
    public T get(int posicion) {
        if (posicion < 0 || posicion >= size) return null; // si la posicion no está entre 0 y el tamaño, devuelve null

        ElementoDE<T> actual = primero;
        int i = 0;

        while (i < posicion) { // recorremos hasta llegar al elemento buscado
            actual = actual.getSiguiente();
            i++;
        }
        return actual.getDato();
    }

    public T delFirst() { // borra el primer element
        if (primero == null) return null;
        T dato = primero.getDato();
        primero = primero.getSiguiente();

        if (primero != null) {
            primero.setAnterior(null);
        } else {
            ultimo = null;
        }
        size--; // Disminuimos el tamaño
        return dato;
    }
    public T delLast() { // borra el último element
        if (ultimo == null) return null;
        T dato = ultimo.getDato();
        ultimo = ultimo.getAnterior();
        if (ultimo != null) {
            ultimo.setSiguiente(null);
        } else {
            primero = null;
        }
        size--;  // Disminuimos el tamaño
        return dato;
    }
    public ListaDE<T> copy() {
        ListaDE<T> copia = new ListaDE<>(); // Creamos una nueva lista vacía

        ElementoDE<T> actual = primero; // Empezamos desde el primer nodo de la lista original

        while (actual != null) { // Recorremos toda la lista
            copia.addLast(actual.getDato()); // Añadimos cada dato al final de la copia(para no invertirla)
            actual = actual.getSiguiente(); // Avanzamos al siguiente nodo
        }

        return copia; // Devolvemos la nueva lista copiada
    }


}
