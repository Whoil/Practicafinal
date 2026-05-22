package Estructuras;

/**
 * Lista doblemente enlazada propia traida de las estructuras del grupo.
 *
 * La unica adaptacion importante respecto a la version heredada es quitar
 * Comparable. Para B-02 necesitamos guardar Objeto en el inventario y los
 * objetos del juego no tienen un orden natural. Por eso las busquedas comparan
 * con equals, igual que hace ListaSE.
 */
public class ListaDE<T> implements Lista<T> {
    protected ElementoDE<T> primero, ultimo;
    protected int size;

    public ListaDE() { // Constructor, crea una lista vacia
        this.primero = null;
        this.ultimo = null;
        this.size = 0;
    }

    @Override
    public void addLast(T dato) { // anade al final
        ElementoDE<T> nuevo = new ElementoDE<>(dato);
        if (ultimo == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
            nuevo.setAnterior(ultimo);
            ultimo = nuevo;
        }
        size++;
    }

    @Override
    public void add(T dato) {
        ElementoDE<T> nuevo = new ElementoDE<>(dato);
        if (primero == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            primero.setAnterior(nuevo);
            nuevo.setSiguiente(primero);
            primero = nuevo;
        }
        size++;
    }

    @Override
    public T get(T dato) {
        ElementoDE<T> actual = primero;
        while (actual != null) {
            if (sonIguales(actual.getDato(), dato)) {
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null; // Si no esta, devolvemos null
    }

    @Override
    public T del(T dato) {
        ElementoDE<T> actual = primero;
        while (actual != null) {
            if (sonIguales(actual.getDato(), dato)) {
                if (actual.getAnterior() == null) { // Borrar el primero
                    primero = actual.getSiguiente();
                    if (primero != null) {
                        primero.setAnterior(null);
                    } else {
                        ultimo = null;
                    }
                } else if (actual.getSiguiente() == null) { // Borrar el ultimo
                    ultimo = actual.getAnterior();
                    ultimo.setSiguiente(null);
                } else { // Borrar uno del medio
                    actual.getAnterior().setSiguiente(actual.getSiguiente());
                    actual.getSiguiente().setAnterior(actual.getAnterior());
                }
                size--;
                return actual.getDato();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return primero == null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public MiIterador<T> getIterador() {
        return new IteradorDE<>(primero);
    }

    public boolean existeDato(T dato){
        return get(dato) != null;
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

    public T getUltimo() { // devuelve el dato del ultimo elemento
        if (ultimo == null) return null;
        return ultimo.getDato();
    }

    @Override
    public void clear() { // limpia la lista
        primero = null;
        ultimo = null;
        size = 0;
    }

    @Override
    public T get(int posicion) {
        if (posicion < 0 || posicion >= size) return null;

        ElementoDE<T> actual = primero;
        int i = 0;

        while (i < posicion) {
            actual = actual.getSiguiente();
            i++;
        }
        return actual.getDato();
    }

    @Override
    public T delFirst() { // borra el primer elemento
        if (primero == null) return null;
        T dato = primero.getDato();
        primero = primero.getSiguiente();

        if (primero != null) {
            primero.setAnterior(null);
        } else {
            ultimo = null;
        }
        size--;
        return dato;
    }

    @Override
    public T delLast() { // borra el ultimo elemento
        if (ultimo == null) return null;
        T dato = ultimo.getDato();
        ultimo = ultimo.getAnterior();
        if (ultimo != null) {
            ultimo.setSiguiente(null);
        } else {
            primero = null;
        }
        size--;
        return dato;
    }

    @Override
    public ListaDE<T> copy() {
        ListaDE<T> copia = new ListaDE<>();

        ElementoDE<T> actual = primero;

        while (actual != null) {
            copia.addLast(actual.getDato());
            actual = actual.getSiguiente();
        }

        return copia;
    }

    private boolean sonIguales(T actual, T buscado) {
        if (actual == null) {
            return buscado == null;
        }
        return actual.equals(buscado);
    }
}
