package ParteB.Grafo;

import Estructuras.ListaSE;
import Estructuras.MiIterador;
import Estructuras.Cola;

public class Grafo<T extends Comparable<T>> implements InterfazGrafo<T> {
    private ListaSE<Nodo<T>> nodos;
    private ListaSE<Arco<T>> arcos;
    private long idNodo; // siguiente id disponible para nodo
    private long idArco; // siguiente id disponible para arco

    public Grafo() {
        this.nodos = new ListaSE<>();
        this.arcos = new ListaSE<>();
        this.idNodo = 1;
        this.idArco = 1;
    }

    public ListaSE<Nodo<T>> getNodos() {
        return nodos;
    }

    public ListaSE<Arco<T>> getArcos() {
        return arcos;
    }

    public long getIdNodo() {
        return idNodo;
    }

    public long getIdArco() {
        return idArco;
    }

    public Nodo<T> buscarNodo(T dato){
        MiIterador<Nodo<T>> iterador = nodos.getIterador(); // creamos un iterador para recorrer la lista de nodos
        while(iterador.hasNext()){ // mientras el nodo no sea nulo, seguimos buscando
            Nodo<T> actual = iterador.next();
            if (actual.getDato().compareTo(dato) == 0){ // si encontramos un nodo con el mismo dato, lo devolvemos
                return actual;
            }
        }
        return null; // si no se encuentra el nodo que buscamos, se devuelve null
    }

    public boolean existeNodo(T dato){
        if (buscarNodo(dato) != null){ //  si al buscar devuelve distinto de null, existe ese nodo
            return true;
        }
        return false;
    }

    public Nodo<T> addNodo(T dato){
        if (!existeNodo(dato)){
            Nodo<T> nuevo = new Nodo<>(idNodo, dato);
            nodos.addLast(nuevo);
            idNodo ++;
            return nuevo; // si no existe el nodo, se crea, se añade y se devuelve
        }
        return buscarNodo(dato); // si existe el nodo, se devuelve el nodo existente
    }

    public Arco<T> buscarArco(Nodo<T> origen, Nodo<T> destino, String dato){
        MiIterador<Arco<T>> iterador = arcos.getIterador(); // creamos un iterador para la lista de arcos
        while(iterador.hasNext()){ // mientras el arco al que esta apuntando el iterador no sea nulo seguimos
            Arco<T> actual = iterador.next();
            if (actual.getDato().compareTo(dato) == 0 && actual.getOrigen().compareTo(origen) == 0 && actual.getDestino().compareTo(destino) == 0){
                return actual; // si el dato, origen y destino coinciden con el del arco que buscamos, devolvemos este
            }
        }
        return null; // si no encontramos el arco con el dato, origen y destino que buscamos, devuelve null
    }
    public boolean existeArco(Nodo<T> origen, Nodo<T> destino, String dato){
        if (buscarArco(origen, destino, dato) != null){
            return true; // si al buscar el arco lo encontramos, este existe y devuelve true
        }
        return false; // si no lo encontramos devuelve false
    }

    public Arco<T> addArco(T datoOrigen, T datoDestino, String dato){
        Nodo<T> origen = addNodo(datoOrigen); // buscamos o creamos los nodos de origen y destino
        Nodo<T> destino = addNodo(datoDestino);
        if (!existeArco(origen, destino, dato)){
            Arco<T> nuevo = new Arco<>(idArco, dato, origen, destino); // creamos un nuevo arco si no existe ya
            arcos.addLast(nuevo); // añadimos este arco al final de la lista de arcos;
            origen.addArcoSalida(nuevo);
            destino.addArcoEntrada(nuevo);
            idArco ++;
            return nuevo;
        }
        return buscarArco(origen, destino, dato); // si el arco ya existe, lo devolvemos

    }
    public ListaSE<T> getAdyacentes(T dato){
        Nodo<T> nodo = buscarNodo(dato);
        if (nodo == null){
            return new ListaSE<>();
        }
        ListaSE<Arco<T>> arcosSalida = nodo.getArcosSalida();
        MiIterador<Arco<T>> iterador = arcosSalida.getIterador();
        ListaSE<T> adyacentes = new ListaSE<>();
        while(iterador.hasNext()){
            Arco<T> actual = iterador.next();
            adyacentes.addLast(actual.getDestino().getDato());
        }
        return adyacentes;
    }

    @Override
    public String toString() {
        MiIterador<Arco<T>> iterador = arcos.getIterador();
        String texto = "";
        while(iterador.hasNext()){ // recorremos cada arco con un iterador y lo guardamos para devolverlos todos a la vez
            Arco<T> actual = iterador.next();
            texto = texto + actual.toString() + "\n";
        }
        return texto;
    }

    public ListaSE<T> recorridoBFS(T inicio) {
        // Recorremos el grafo desde un nodo usando una cola, para visitar por niveles
        ListaSE<T> visitados = new ListaSE<>();
        Cola<T> cola = new Cola<>();
        if (!existeNodo(inicio)) {
            return visitados;
        }
        cola.offer(inicio);
        visitados.addLast(inicio);

        while (cola.getSize() != 0) {
            T actual = cola.poll();
            ListaSE<T> adyacentes = getAdyacentes(actual);
            MiIterador<T> iterador = adyacentes.getIterador();
            while (iterador.hasNext()) {
                T adyacente = iterador.next();
                if (!visitados.existeDato(adyacente)) {
                    visitados.addLast(adyacente);
                    cola.offer(adyacente);
                }
            }
        }
        return visitados;
    }

    public ListaSE<T> caminoMinimo(T inicio, T fin) {
        // Hacemos un BFS guardando el nodo anterior de cada nodo visitado
        // Asi luego podemos reconstruir el camino desde el final hasta el inicio
        ListaSE<T> visitados = new ListaSE<>();
        ListaSE<T> padresNodo = new ListaSE<>();
        ListaSE<T> padresAnterior = new ListaSE<>();
        Cola<T> cola = new Cola<>();

        if (!existeNodo(inicio) || !existeNodo(fin)) {
            return new ListaSE<>();
        }
        cola.offer(inicio);
        visitados.addLast(inicio);
        padresNodo.addLast(inicio);
        padresAnterior.addLast(null);
        while (!cola.isEmpty()) {
            T actual = cola.poll();

            if (actual.compareTo(fin) == 0) {
                return reconstruirCamino(fin, padresNodo, padresAnterior);
            }
            ListaSE<T> adyacentes = getAdyacentes(actual);
            MiIterador<T> iterador = adyacentes.getIterador();

            while (iterador.hasNext()) {
                T adyacente = iterador.next();

                if (!visitados.existeDato(adyacente)) {
                    visitados.addLast(adyacente);
                    cola.offer(adyacente);
                    padresNodo.addLast(adyacente);
                    padresAnterior.addLast(actual);
                }
            }
        }
        return new ListaSE<>();
    }

    private ListaSE<T> reconstruirCamino(T fin, ListaSE<T> padresNodo, ListaSE<T> padresAnterior) {
        // Reconstruimos el camino yendo hacia atras
        // Al final invertimos la lista
        ListaSE<T> camino = new ListaSE<>();
        T actual = fin;

        while (actual != null) {
            camino.addLast(actual);
            T anterior = null;
            MiIterador<T> iteradorNodos = padresNodo.getIterador();
            MiIterador<T> iteradorPadres = padresAnterior.getIterador();
            while (iteradorNodos.hasNext() && iteradorPadres.hasNext()) {
                T nodo = iteradorNodos.next();
                T padre = iteradorPadres.next();
                if (nodo.compareTo(actual) == 0) {
                    anterior = padre;
                }
            }
            actual = anterior;
        }
        camino.invertir();
        return camino;
    }

    public ListaSE<T> getVecinosNoDirigido(T dato) {
        // Para comprobar si el grafo es disjunto miramos el grafo como no dirigido, por eso cogemos tanto los arcos que salen como los que entran
        Nodo<T> nodo = buscarNodo(dato);
        ListaSE<T> vecinos = new ListaSE<>();

        if (nodo == null) {
            return vecinos;
        }
        ListaSE<Arco<T>> arcosSalida = nodo.getArcosSalida();
        MiIterador<Arco<T>> iteradorSalida = arcosSalida.getIterador();
        while (iteradorSalida.hasNext()) {
            Arco<T> actual = iteradorSalida.next();
            T destino = actual.getDestino().getDato();

            if (!vecinos.existeDato(destino)) {
                vecinos.addLast(destino);
            }
        }

        ListaSE<Arco<T>> arcosEntrada = nodo.getArcosEntrada();
        MiIterador<Arco<T>> iteradorEntrada = arcosEntrada.getIterador();

        while (iteradorEntrada.hasNext()) {
            Arco<T> actual = iteradorEntrada.next();
            T origen = actual.getOrigen().getDato();

            if (!vecinos.existeDato(origen)) {
                vecinos.addLast(origen);
            }
        }

        return vecinos;
    }

    public ListaSE<T> recorridoBFSNoDirigido(T inicio) {
        // Es el mismo BFS, pero usando vecinos no dirigidos en vez de solo adyacentes
        ListaSE<T> visitados = new ListaSE<>();
        Cola<T> cola = new Cola<>();

        if (!existeNodo(inicio)) {
            return visitados;
        }

        cola.offer(inicio);
        visitados.addLast(inicio);

        while (!cola.isEmpty()) {
            T actual = cola.poll();
            ListaSE<T> vecinos = getVecinosNoDirigido(actual);
            MiIterador<T> iterador = vecinos.getIterador();

            while (iterador.hasNext()) {
                T vecino = iterador.next();

                if (!visitados.existeDato(vecino)) {
                    visitados.addLast(vecino);
                    cola.offer(vecino);
                }
            }
        }

        return visitados;
    }

    public boolean isDisjunto() {
        // Un grafo es disjunto si desde el primer nodo no llegamos a todos los nodos
        // Usamos el recorrido no dirigido para comprobar componentes separadas
        if (nodos.isEmpty()) {
            return false;
        }

        Nodo<T> primero = nodos.get(0);
        ListaSE<T> visitados = recorridoBFSNoDirigido(primero.getDato());

        return visitados.getSize() != nodos.getSize(); // Si desde un nodo puedo llegar a todos los demas, es no disjunto
    }
}
