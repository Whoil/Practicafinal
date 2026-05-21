package Estructuras;

/**
 * Implementación del TAD Estructuras.Pila mediante una lista doblemente enlazada.
 * Una pila sigue la política LIFO (Last In, First Out), es decir,
 * el último elemento que entra es el primero que sale.
 * Esta clase hereda de  Estructuras.ListaDE<T>, por lo que reutiliza la
 * estructura de nodos doblemente enlazados y los atributos principales
 * de la lista, como:</p>
 *  primero: referencia al primer nodo de la estructura.
 *  ultimo: referencia al último nodo de la estructura.
 *  size: número de elementos almacenados.
 * Además, implementa la interfaz Estructuras.Pilas<T>, que define las
 * operaciones propias de una pila.
 * parámetro <T> tipo de dato almacenado en la pila. Debe implementar
 * Comparable<T> para poder comparar elementos en métodos como:
 * min(), max() o contains()
*/
public class Pila< T extends Comparable<T>>extends ListaDE<T> implements Pilas<T> {
    /**
     * Devuelve el elemento situdado en la cima de la pila sin eliminarlo.
     * La cime de esta pila coincide con el último nodo de la lista doblemente enlazada.
     * Retorna el dato almacenado en la cima de la pila, o nulo si la pila está vacía.
     */
    @Override
    public T peek() {
        if (isEmpty()) return null;
        return ultimo.dato;
    }


    /**
     * Devuelve el número de elementos almacenados actualmente en la pila.
     * Retorna el tamaño
     *
     */
    @Override
    public int getSize(){
        return size;
    }

    /**
     * Inserta un nuevo elemento en la cima de la pila
     * Se reutiliza el método add(dato) de la superclase.
     * Dado que la cima corresponde al final de la lista,
     * el nuevo elemento queda almacenado como último nodo
     * El parámetro es el dato elemento que se desea apilar
     */
    @Override
    public void push(T dato) {
        // La cima de la pila es "ultimo", igual que en pop().
        super.addLast(dato);
    }


    /**
     * Extrae y devuelve el elememto situado en la cima de la pila
     * SI la pila está vacía, no se puede extraer ningún elemento y se devuelve null.
     * Casos que se contemplan:
     * Si sólo hay un elemento, tras extraerlo la pila queda vacía.
     * Si hay varios elementos, el penúltimo pasa a ser el nuevo último.
     * Retorna el elemento extaido de la cima o null si está vacía
     */
    @Override
    public T pop() {
        if (isEmpty()) return null;
        //Guardamos el dato del último nodo, que representa la cima.
        T dato = ultimo.dato;
        //SI solo hay un nodo, la pila queda vacía tras eliminarlo
        if (primero == ultimo) {
            primero = ultimo = null;
        } else {
            //Si hay más de un nodo, el anterior al último pasas a ser el nuevo último
            ultimo = ultimo.anterior;
            ultimo.siguiente = null;
        }
        //Se actualiza el tamaño de la pila
        size--;
        return dato;
    }

    /**
     * Elimina todos los elementos de la pila.
     * Tras ejecutar el método, la estructura queda vacía:
     * No hay primer nodo, no hay último nodo y el tamaño es 0.
     */
    @Override
    public void clear(){
        primero=ultimo=null;
        size=0;
    }

    /**
     * Comprueba si un determinado elemento está en la pila.
     * La búsqueda se realiza recorriendo la estructura desde el priemr nodo hasta el último.
     * En cuanto se encuentra un nodo con el dato buscado, se devuelve true.
     * El parámetro es el dato elemento que se desea localizar
     *Retorna falso si no se encuentra el elemento
     *
     */

    @Override
    public boolean contains(T dato) {
        if (isEmpty())return false;
        ElementoDE<T> act = primero;while (act != null) {
            if (act.dato.compareTo(dato) == 0) {
                return true;
            }
            act = act.siguiente;
        }
        return false;
    }


    /**
     * Genera una representación den forma de cadena de contenido de la pila.
     * Los elementos se miestram desde la base hasta la cima, ya que
     * el recorrido se realiza desde primero hasta ultimo
     *Devuelve una cadena con el contenido completo de la pila.
     */
    @Override
    public String toString(){
        String pila= "[";
        ElementoDE<T> act= primero;
        while(act!=null){
            pila+=act.dato;
            if (act.siguiente!=null){
                pila+=", ";
            }
            act=act.siguiente;
        }
        pila+="]";
        return pila;
    }

    /**
     * Devuelve el elementos situado en la base de la pila sin ser eliminado.
     * La base de la pila se corresponde con el primer nodo de la lista.
     * Si la pila está vacía retorna null.
     */
    @Override
    public T bottom(){
        if (isEmpty()) return null;
        return primero.dato;
    }


    /**
     * Invierte el orde de los elementos de la pila
     * Este método intercambia, para cada nodo, las referencias siguiente y anterior.
     * Al finalizar el proceso, también se intercambian las referencias globales primero y ultimo.
     * La pila queda completamente invertida
     * Si la pila está vacía, no se raliza ninguna operación
     */
    @Override
    public void reverse(){
        if(isEmpty())return;
        ElementoDE<T> act=primero;
        ElementoDE<T> aux=null;
        while (act!=null){
            // Guardamos temporalmete la referencia anterior
            aux=act.anterior;
            // Intercambiamos los enlaces dle nodo actual.
            act.anterior=act.siguiente;
            act.siguiente=aux;

            //Avanzamos al siguiente nodo original.
            act=act.anterior;
        }
        //Finalmente intercambiamos primero con ultimo
        aux=primero;
        primero=ultimo;
        ultimo=aux;
    }

    /**
     * Busca un elemento en la pila y devuelve la posición relativa
     * comenzando desde la cima.
     * La posición 1 corresponde al elemento situado en la cima,
     * la posición 2 al inmediatamente inferior, etc.
     * El parámetro es el dato elemento que se desea buscar
     * Devuelve la posición del elemento contando desde la cima, o si no es encuentra
     * devuelve -1
     */
    @Override
    public int search(T dato){
        if (isEmpty()) return -1;
        ElementoDE<T> act=ultimo;
        int pos=1;
        while(act!=null){
            if (act.dato.compareTo(dato)==0){
                return pos;
            }
                act=act.anterior;
            pos++;
        }
        return -1;

    }

    /**
     * Crea y devuelve una copia de la pila.
     * La copia contiene los mismos datos y en el mismo orden que la pila original
     * El recorrido se hace desde la base hasta la cima, insertando cada elemento
     * con push(), por lo que el orden final queda preservado:
     * Devuelve una pila con el mismo contenido que la original.
     */
    @Override
    public Pila<T> copy(){
        Pila<T> copia=new Pila<T>();
        ElementoDE<T> act=primero;
        while (act!=null){
        copia.push(act.dato);
        act=act.siguiente;
    }
    return copia;
    }

    /**
     * Devuelve el menor elemento almacenado en la pila.
     * Para determinarlo, se recorre toda la estructura comparando
     * cada dato con el mínimo provisional encontrado hasta ese momento.
     *Retorna el valor mínimo de la pila, o null si está vacía.
     */
    @Override
    public T min(){
        if (isEmpty()) return null;
        ElementoDE<T> act= primero;
        T min =act.dato;
        while(act!=null) {
            if (act.dato.compareTo(min) < 0) {
                min = act.dato;
            }
            act = act.siguiente;
        }
        return min;
    }

    /**
     * Devuelve el mayor elemento almacenado en la pila.
     * Para calcularlo, se recorre la pila completa comparando cada
     * elemento con el máximo provisional.
     *Retorna el valor máximo de la pila, o  null si la pila está vacía.
     */
    @Override
    public T max(){
        if (isEmpty())return null;
        ElementoDE<T> act=primero;
        T max=act.dato;
        while (act!=null){
            if (act.dato.compareTo(max)>0){
                max=act.dato;
            }
            act=act.siguiente;
        }
        return max;
    }

}
