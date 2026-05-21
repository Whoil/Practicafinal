package Estructuras;
// Clase genérica de lista simplemente enlazada.
// No exige Comparable porque una lista no necesita ordenar sus datos.
// Las busquedas y borrados comparan por igualdad con equals.

public class ListaSE<T> implements Lista<T> {
    protected ElementoSE<T> primero; // Referencia al primer nodo de la lista.
    protected int size;

    public ListaSE() { // Constructor: crea una lista vacía.
        this.primero = null;
        this.size = 0;
    }

    @Override
    public void add(T dato) {
        ElementoSE<T> nuevo = new ElementoSE<>(dato); // Se crea un nuevo nodo con el dato recibido.
        if (primero != null) {
            nuevo.setSiguiente(primero);
            primero = nuevo; // Ahora el nuevo nodo pasa a ser el primero
        }else{
            primero = nuevo; // Si la lista estaba vacía, el nuevo nodo es el primero
        }
        size++;
    }

    @Override
    public T get(T dato) {
        ElementoSE<T> actual = primero; //Empezamos a recorrer desde el primer nodo
        while (actual != null){

            if (sonIguales(actual.getDato(), dato)){
                return actual.getDato(); // Devolvemos el dato encontrado
            }
            actual = actual.getSiguiente(); // Avanzamos al siguiente nodo
        }
        return null; // Si no se encontró el dato, devolvemos null
    }

    @Override
    public T del(T dato) {
        ElementoSE<T> actual = primero; //actual recorre la lista
        ElementoSE<T> anterior = null; // anterior guarda el nodo anterior a actual
        while (actual != null){

            if (sonIguales(actual.getDato(), dato)){
                if (anterior == null){ // Si anterior es null, significa que actual es el primer nodo
                    primero = actual.getSiguiente();  // El primer nodo pasa a ser el siguiente del actual
                }else {
                    // Si no es el primero, hacemos que el anterior apunte
                    // al siguiente del actual, saltándose el nodo borrado
                    anterior.setSiguiente(actual.getSiguiente());
                }
                size--;  // Reducimos el tamaño de la lista
                return actual.getDato(); // Devolvemos el dato eliminado
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return primero == null;
    }

    @Override
    public int getSize() { // Devuelve el número de elementos almacenados
        return size;
    }

    @Override
    public MiIterador<T> getIterador() { // Crea y devuelve un iterador que empieza en el primer nodo
        return new IteradorSE<>(primero);
    }

    public boolean existeDato(T dato){
        return get(dato) != null;  // Si devuelve algo distinto de null, el dato existe
    }
    private boolean sonIguales(T actual, T buscado) {
        if (actual == null) {
            return buscado == null;
        }
        return actual.equals(buscado);
    }

    public void invertir(){ // Invertir una lista, el primero pasa a ser el último, etc.
        ElementoSE<T> anterior = null;
        ElementoSE<T> actual = primero;
        ElementoSE<T> siguiente;

        while(actual != null){ // Recorremos toda la lista
            siguiente = actual.getSiguiente(); // Guardamos el siguiente nodo antes de cambiar enlaces
            actual.setSiguiente(anterior); // Invertimos el enlace: ahora el actual apunta al anterior
            anterior = actual; // Avanzamos anterior al nodo actual
            actual = siguiente; // Avanzamos actual al siguiente nodo original
        }
        primero = anterior; // Al terminar, anterior apunta al nuevo primero de la lista
    }
    @Override
    public String toString() {
        String texto = "[";
        ElementoSE<T> actual = primero;

        while (actual != null) {
            texto += actual.getDato();
            if (actual.getSiguiente()!= null) {
                texto += ", ";
            }
            actual = actual.getSiguiente();
        }

        texto += "]";
        return texto;
    }
    public void addLast(T dato) {
        ElementoSE<T> nuevo = new ElementoSE<>(dato); // Creamos el nuevo nodo

        if (primero == null) { // Si la lista está vacía
            primero = nuevo;   // El nuevo nodo es el primero
        } else {
            ElementoSE<T> actual = primero;
            while (actual.getSiguiente() != null) { // Recorremos hasta el último nodo
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo); // El último apunta al nuevo
        }
        size++;
    }
    public T get(int posicion) {
        if (posicion < 0 || posicion >= size) return null;
        // Si la posición no es válida
        ElementoSE<T> actual = primero;
        int i = 0;
        // Avanzamos hasta la posición deseada
        while (i < posicion) {
            actual = actual.getSiguiente();
            i++;
        }
        return actual.getDato(); // Devolvemos el dato encontrado
    }

    public T delFirst() {
        if (primero == null) return null; // Estructuras.Lista vacía
        T dato = primero.getDato(); // Guardamos el dato a devolver
        primero = primero.getSiguiente(); // El segundo pasa a ser el primero
        size--;
        return dato;
    }
    public T delLast() {
        if (primero == null) return null; // Estructuras.Lista vacía
        if (primero.getSiguiente() == null) { // Si solo hay un elemento
            T dato = primero.getDato();
            primero = null;
            size--;
            return dato;
        }
        ElementoSE<T> actual = primero;
        while (actual.getSiguiente().getSiguiente() != null) { // Recorremos hasta el penúltimo nodo
            actual = actual.getSiguiente();
        }
        T dato = actual.getSiguiente().getDato(); // Guardamos el último
        actual.setSiguiente(null); // Cortamos el enlace
        size--;
        return dato;
    }
    public void clear() {
        primero = null; // Eliminamos la referencia al primer nodo
        size = 0;       // Reiniciamos el tamaño
    }
    public ListaSE<T> copy() {
        ListaSE<T> copia = new ListaSE<>(); // Nueva lista vacía
        ElementoSE<T> actual = primero;
        while (actual != null) {
            copia.addLast(actual.getDato()); // Usamos addLast para no invertir

            actual = actual.getSiguiente();
        }
        return copia;
    }


}
