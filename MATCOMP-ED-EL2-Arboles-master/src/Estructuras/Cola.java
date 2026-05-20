package Estructuras;

/**
 * Implementación del TAD Estructuras.Cola mediante una lista doblemente enlazada.
 *Una cola sigue la política FIFO (First In, First Out), es decir,
 * el primer elemento que entra es el primero que sale.
 *
 * Esta clase hereda de  Estructuras.ListaDE<T>, reutilizando la estructura
 * de nodos doblemente enlazados y los atributos principales:
 *primero: referencia al primer nodo (frente de la cola).
 *ultimo: referencia al último nodo (final de la cola).
 *size: número de elementos almacenados.
 * Además, implementa la interfaz Estructuras.Colas<T>, que define las
 * operaciones propias de una cola.
 * parámetro <T> tipo de dato almacenado en la cola. Debe implementar
 * Comparable<T> para permitir comparaciones.
 */
public class Cola<T extends Comparable<T>>extends ListaDE<T> implements Colas<T> {
    /**
     * Devuelve el elemento situado al frente de la cola sin eliminarlo.
     *El frente de la cola se corresponde con el primer nodo  primero.
     * Retorna el primer elemento, o null si la cola está vacía.
     */
    @Override
    public T peek(){
        if(isEmpty())return null;
        return primero.dato;
    }
    /**
     /**
     * Inserta un nuevo elemento al final de la cola.
     * Si la cola está vacía, el nuevo nodo pasa a ser el primero y el último.
     * Si no está vacía, el nuevo nodo se enlaza detrás del último.
     */

    @Override
    public void offer(T dato){
        ElementoDE<T> nuevo = new ElementoDE<>(dato);

        if (isEmpty()) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
            nuevo.setAnterior(ultimo);
            ultimo = nuevo;
        }

        size++;
    }
    /**
     * Extrae y devuelve el elemento situado al frente de la cola.
     * Casos contemplados:
     *Si la cola está vacía devuelve null.
     *Si hay un solo elemento  la cola queda vacía.
     *Si hay varios el segundo pasa a ser el nuevo primero.
     * Retorna el elemento eliminado del frente, o null si la cola está vacía.
     */

    @Override
    public T poll(){
        if (isEmpty())return null;
        // Guardamos el dato del frente
        T dato=primero.dato;
        // Caso: un único elemento
        if(primero==ultimo){
            primero=ultimo=null;

        }else{
            // Avanzamos el primero
            primero=primero.siguiente;
            primero.anterior=null;
        }
        size--;
        return dato;
    }
    /**
     * Elimina todos los elementos de la cola.
     * Tras la ejecución, la cola queda completamente vacía.
     */

    @Override
    public void clear(){
        primero=ultimo=null;
        size=0;
    }

    /**
     * Devuelve el número de elementos de la cola.
     * Retorna tamaño actual.
     */
    @Override
    public int getSize(){
        return size;
    }
    /**
     * Comprueba si un elemento está presente en la cola.
     *Se recorre la estructura desde el primero hasta el último.
     * Parámetro dato elemento a buscar.
     * Retorna true si se encuentra, false en caso contrario.
     */
    @Override
    public boolean contains(T dato){
        if (isEmpty())return false;
        ElementoDE<T> act=primero;
        while(act!=null){
            if (act.dato.compareTo(dato)==0){
                return true;
            }
            act=act.siguiente;
        }
        return false;
    }
    /**
     * Genera una representación en cadena de la cola.
     * Los elementos se muestran desde el frente hasta el final.
     * Retorna una cadena con el contenido de la cola.
     */
    @Override
    public String toString(){
        String cola="[";
        ElementoDE<T> act=primero;
        while(act!=null){
            cola+=act.dato;
            if(act.siguiente!=null){
                cola+=", ";
            }
            act=act.siguiente;
        }
        cola+="]";
        return cola;
    }

    /**
     * Devuelve el último elemento de la cola sin eliminarlo.
     * Retorna el último elemento, o null si la cola está vacía.
     */
    @Override
    public T last(){
        if (isEmpty()) return null;
        return ultimo.dato;
    }


    /**
     * Rota la cola un número determinado de veces.
     *Cada rotación mueve el elemento del frente al final de la cola.
     *Ejemplo:
     * [1, 2, 3, 4] → rotate(1) → [2, 3, 4, 1]
     * No se realiza ninguna operación si la cola está vacía
     * o tiene un único elemento.
     * Parámetro times número de rotaciones.
     */
    @Override
    public void rotate(int times){
        if (isEmpty() || primero==ultimo)return;
        for (int i = 0; i < times; i++){
            // Guardamos el primero
        ElementoDE<T> aux=primero;
            // Avanzamos el primero
            primero=primero.siguiente;
            primero.anterior=null;

            // Insertamos el antiguo primero al final
            ultimo.siguiente=aux;
            aux.anterior=ultimo;
            aux.siguiente=null;

            // Actualizamos el último
            ultimo=aux;

        }
    }
    /**
     * Crea una copia de la cola.
     * La copia mantiene el mismo orden de los elementos.
     * Retorna una nueva cola con el mismo contenido.
     */

    @Override
    public Cola<T> copy(){
        Cola<T> copia= new Cola<>();
        ElementoDE<T> act=primero;
        while (act!=null){
            copia.offer(act.dato);
            act=act.siguiente;
        }
        return copia;
    }
    /**
     * Devuelve el elemento mínimo de la cola.
     * Retorna el valor mínimo o null si está vacía.
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
     * Devuelve el elemento máximo de la cola.
     * Retorna el valor máximo o null si está vacía.
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

