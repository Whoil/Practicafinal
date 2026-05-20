package Estructuras;

public class Main {

    public static void main(String[] args) {
        // Creamos varios alumnos para hacer las pruebas
        Alumno a1 = new Alumno("111A", "Héctor", 8.2);
        Alumno a2 = new Alumno("222B", "Álvaro", 7.1);
        Alumno a3 = new Alumno("333C", "Guillermo", 9.0);
        Alumno a4 = new Alumno("444D", "Lucía", 6.8);
        Alumno a5 = new Alumno("555E", "Marta", 7.9);

        // === LISTA SIMPLE ===
        System.out.println("=== LISTA SE ===");
        ListaSE<Alumno> listaSE = new ListaSE<>();

        // Probamos métodos con la lista vacía
        System.out.println(listaSE.isEmpty());
        System.out.println(listaSE.getSize());
        System.out.println(listaSE.get(a1));
        System.out.println(listaSE.del(a1));
        System.out.println(listaSE.existeDato(a1));

        // Añadimos elementos
        listaSE.add(a1);
        listaSE.add(a2);
        listaSE.add(a3);

        // Probamos métodos con elementos dentro
        System.out.println(listaSE.isEmpty());
        System.out.println(listaSE.getSize());
        System.out.println(listaSE.get(a2));
        System.out.println(listaSE.existeDato(a1));
        System.out.println(listaSE.del(a2));

        // Recorremos la lista con el iterador
        MiIterador<Alumno> it1 = listaSE.getIterador();
        while (it1.hasNext()) {
            System.out.println(it1.next());
        }

        // Invertimos la lista y la volvemos a mostrar
        listaSE.invertir();
        System.out.println("Estructuras.ListaSE invertida:");
        MiIterador<Alumno> it2 = listaSE.getIterador();
        while (it2.hasNext()) {
            System.out.println(it2.next());
        }

        // ================= LISTA DOBLE =================
        System.out.println("\n===== LISTA DE =====");
        ListaDE<Alumno> listaDE = new ListaDE<>();

        // Probamos con la lista vacía
        System.out.println(listaDE.isEmpty());
        System.out.println(listaDE.getSize());
        System.out.println(listaDE.get(a1));
        System.out.println(listaDE.del(a1));
        System.out.println(listaDE.existeDato(a1));

        // Añadimos elementos
        listaDE.add(a1);
        listaDE.add(a2);
        listaDE.add(a3);

        // Probamos sus métodos
        System.out.println(listaDE.isEmpty());
        System.out.println(listaDE.getSize());
        System.out.println(listaDE.get(a2));
        System.out.println(listaDE.existeDato(a3));
        System.out.println(listaDE.del(a3));
        System.out.println(listaDE.del(a1));

        // Mostramos el contenido restante
        MiIterador<Alumno> it3 = listaDE.getIterador();
        while (it3.hasNext()) {
            System.out.println(it3.next());
        }

        // === LISTA CIRCULAR ===
        System.out.println("\n===== LISTA CIRCULAR =====");
        ListaCircular<Alumno> listaC = new ListaCircular<>();

        // Probamos con la lista circular vacía
        System.out.println(listaC.isEmpty());
        System.out.println(listaC.getSize());
        System.out.println(listaC.get(a1));
        System.out.println(listaC.del(a1));

        // Añadimos elementos
        listaC.add(a1);
        listaC.add(a2);
        listaC.add(a3);

        // Probamos métodos principales
        System.out.println(listaC.isEmpty());
        System.out.println(listaC.getSize());
        System.out.println(listaC.get(a2));
        System.out.println(listaC.del(a1));

        // Recorremos la lista circular
        InterfazIteradorCircular<Alumno> itC1 = listaC.iteratorCircular();
        while (itC1.hasNext()) {
            System.out.println(itC1.next());
        }

        // Eliminamos lo que queda para comprobar que funciona
        System.out.println(listaC.del(a3));
        System.out.println(listaC.del(a2));
        System.out.println(listaC.isEmpty());

        // === ITERADOR SIMPLE ===
        System.out.println("\n===== ITERADOR SE =====");

        // Creamos nodos manualmente para probar el iterador
        ElementoSE<Alumno> e1 = new ElementoSE<>(a1);
        ElementoSE<Alumno> e2 = new ElementoSE<>(a2);
        ElementoSE<Alumno> e3 = new ElementoSE<>(a3);

        // Probamos getters
        System.out.println(e1.getDato());
        System.out.println(e1.getSiguiente());

        // Enlazamos los nodos
        e1.setSiguiente(e2);
        e2.setSiguiente(e3);

        // Probamos el iterador simple
        IteradorSE<Alumno> itSE = new IteradorSE<>(e1);
        System.out.println(itSE.hasNext());
        System.out.println(itSE.next());
        System.out.println(itSE.next());
        System.out.println(itSE.next());
        System.out.println(itSE.hasNext());
        System.out.println(itSE.next());

        // === ITERADOR DOBLE
        System.out.println("\n===== ITERADOR DE =====");

        // Creamos nodos dobles
        ElementoDE<Alumno> d1 = new ElementoDE<>(a1);
        ElementoDE<Alumno> d2 = new ElementoDE<>(a2);
        ElementoDE<Alumno> d3 = new ElementoDE<>(a3);

        // Probamos getters
        System.out.println(d1.getDato());
        System.out.println(d1.getAnterior());
        System.out.println(d1.getSiguiente());

        // Enlazamos los nodos
        d1.setSiguiente(d2);
        d2.setAnterior(d1);
        d2.setSiguiente(d3);
        d3.setAnterior(d2);

        // Comprobamos enlaces
        System.out.println(d1.getSiguiente().getDato());
        System.out.println(d2.getAnterior().getDato());

        // Probamos el iterador doble
        IteradorDE<Alumno> itDE = new IteradorDE<>(d1);
        System.out.println(itDE.hasNext());
        System.out.println(itDE.next());
        System.out.println(itDE.next());
        System.out.println(itDE.next());
        System.out.println(itDE.hasNext());
        System.out.println(itDE.next());

        // === ITERADOR CIRCULAR ===
        System.out.println("\n===== ITERADOR CIRCULAR =====");

        // Creamos nodos circulares
        NodoCircular<Alumno> n1 = new NodoCircular<>(a1);
        NodoCircular<Alumno> n2 = new NodoCircular<>(a2);
        NodoCircular<Alumno> n3 = new NodoCircular<>(a3);

        // Probamos acceso a los atributos
        System.out.println(n1.dato);
        System.out.println(n1.siguiente);

        // Hacemos la estructura circular
        n1.siguiente = n2;
        n2.siguiente = n3;
        n3.siguiente = n1;

        System.out.println(n1.siguiente.dato);

        // Probamos el iterador circular
        IteradorCircular<Alumno> itCircular = new IteradorCircular<>(n1);
        System.out.println(itCircular.hasNext());
        System.out.println(itCircular.next());
        System.out.println(itCircular.next());
        System.out.println(itCircular.next());
        System.out.println(itCircular.hasNext());
        System.out.println(itCircular.next());

        // Probamos reset()
        itCircular.reset();
        System.out.println(itCircular.hasNext());
        System.out.println(itCircular.next());

        // === COLA ===
        System.out.println("\n===== COLA =====");
        Cola<Alumno> cola = new Cola<>();

        // Probamos métodos con la cola vacía
        System.out.println(cola.isEmpty());
        System.out.println(cola.getSize());
        System.out.println(cola.peek());
        System.out.println(cola.poll());
        System.out.println(cola.last());
        System.out.println(cola.contains(a1));
        System.out.println(cola.min());
        System.out.println(cola.max());
        System.out.println(cola.toString());

        // Añadimos elementos a la cola
        cola.offer(a1);
        cola.offer(a2);
        cola.offer(a3);
        cola.offer(a4);

        // Probamos todos los métodos importantes
        System.out.println(cola);
        System.out.println(cola.peek());
        System.out.println(cola.last());
        System.out.println(cola.getSize());
        System.out.println(cola.contains(a2));
        System.out.println(cola.contains(a5));
        System.out.println(cola.min());
        System.out.println(cola.max());

        // Giramos la cola
        cola.rotate(1);
        System.out.println(cola);

        cola.rotate(2);
        System.out.println(cola);

        // Hacemos una copia
        Cola<Alumno> copiaCola = cola.copy();
        System.out.println(copiaCola);

        // Sacamos un elemento
        System.out.println(cola.poll());
        System.out.println(cola);

        // Vaciamos la cola
        cola.clear();
        System.out.println(cola);
        System.out.println(cola.isEmpty());

        // PILA
        System.out.println("\n===== PILA =====");
        Pila<Alumno> pila = new Pila<>();

        // Probamos métodos con la pila vacía
        System.out.println(pila.isEmpty());
        System.out.println(pila.getSize());
        System.out.println(pila.peek());
        System.out.println(pila.pop());
        System.out.println(pila.bottom());
        System.out.println(pila.contains(a1));
        System.out.println(pila.search(a1));
        System.out.println(pila.min());
        System.out.println(pila.max());
        System.out.println(pila.toString());

        // Añadimos elementos a la pila
        pila.push(a1);
        pila.push(a2);
        pila.push(a3);
        pila.push(a4);

        // Probamos todos los métodos principales
        System.out.println(pila);
        System.out.println(pila.peek());
        System.out.println(pila.bottom());
        System.out.println(pila.getSize());
        System.out.println(pila.contains(a3));
        System.out.println(pila.contains(a5));
        System.out.println(pila.search(a4));
        System.out.println(pila.search(a2));
        System.out.println(pila.search(a1));
        System.out.println(pila.search(a5));
        System.out.println(pila.min());
        System.out.println(pila.max());

        // Hacemos una copia
        Pila<Alumno> copiaPila = pila.copy();
        System.out.println(copiaPila);

        // Sacamos el elemento de arriba
        System.out.println(pila.pop());
        System.out.println(pila);

        // Invertimos la pila
        pila.reverse();
        System.out.println(pila);

        // Vaciamos la pila
        pila.clear();
        System.out.println(pila);
        System.out.println(pila.isEmpty());
    }
}
