package ParteA;

import Estructuras.ListaSE;

// Subclase del árbol binario de búsqueda preparada para trabajar con enteros.
// Hereda los métodos del árbol genérico y añade operaciones propias para números.
public class ArbolBinarioDeBusquedaEnteros extends ArbolBinarioDeBusqueda<Integer> {

    // Calcula la suma de todos los números guardados en el árbol.
    public int getSuma() {
        // Empieza a sumar desde la raíz del árbol.
        return getSuma(getRaiz());
    }

    // Método auxiliar recursivo que suma los datos desde un nodo concreto.
    private int getSuma(Nodo<Integer> actual) {
        // Si el nodo es null, no hay ningún número que sumar.
        if (actual == null) {
            return 0;
        }

        // Suma el dato del nodo actual, más todo lo que haya a la izquierda,
        // más todo lo que haya a la derecha.
        return actual.getDato()
                + getSuma(actual.getIzquierda())
                + getSuma(actual.getDerecha());
    }

    // Calcula la suma de los números que contiene una lista.
    // Se usa para comprobar que los recorridos del árbol tienen la misma suma.
    public int getSumaLista(ListaSE<Integer> lista) {
        // Variable donde se va acumulando la suma.
        int suma = 0;

        // Recorre la lista desde la primera posición hasta la última.
        for (int i = 0; i < lista.getSize(); i++) {
            // Coge el número de la posición actual y lo añade a la suma.
            suma += lista.get(i);
        }

        // Devuelve la suma final de todos los elementos de la lista.
        return suma;
    }
}


