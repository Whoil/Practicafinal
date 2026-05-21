package modelo.mapa;

import Estructuras.Cola;
import Estructuras.ListaSE;

/**
 * Representa una sala o nivel jugable de la mazmorra.
 *
 * La decision importante de Parte A es que la cueva contiene una matriz propia,
 * no un array de Java ni una coleccion de la biblioteca estandar. La matriz se
 * modela como una lista simplemente enlazada de filas, donde cada fila es otra
 * lista simplemente enlazada de celdas:
 *
 *     ListaSE<ListaSE<Celda>>
 *
 * Esto cumple la restriccion de estructuras de datos y deja claro que el mapa
 * pertenece al dominio del juego, no a una estructura externa. El coste de
 * acceder a una celda por coordenadas es O(fila + columna), porque primero se
 * recorre la lista de filas y despues la lista de celdas de esa fila. Ese coste
 * es aceptable para mapas pequenos de la practica y es facil de defender.
 */
public class Cueva implements InterfazCueva {
    /*
     * Identificador estable de la cueva. Mas adelante puede coincidir con el
     * id usado por JSON o por el Grafo<Cueva> de la mazmorra.
     */
    private final String id;

    /*
     * Dimensiones fijas. Se guardan separadas de la lista para validar limites
     * sin tener que recorrer la matriz completa cada vez.
     */
    private final int filas;
    private final int columnas;

    /*
     * Matriz propia evaluable: cada nodo externo apunta a una fila y cada fila
     * contiene sus celdas en orden de columna. No se expone ningun array.
     */
    private final ListaSE<ListaSE<Celda>> matriz;

    /**
     * Crea una cueva rectangular inicializada con celdas de suelo.
     *
     * El id es obligatorio porque identifica a la cueva dentro de
     * Grafo<Cueva> y permite que Mazmorra la busque por texto. Se rechazan ids
     * nulos o en blanco para evitar nodos imposibles de comparar o recuperar.
     *
     * Se rechazan dimensiones nulas o negativas porque una cueva sin filas o
     * sin columnas no podria recorrerse con BFS ni representarse en pantalla.
     */
    public Cueva(String id, int filas, int columnas) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("La cueva debe tener un id no vacio");
        }
        if (filas <= 0 || columnas <= 0) {
            throw new IllegalArgumentException("La cueva debe tener dimensiones positivas");
        }
        this.id = id;
        this.filas = filas;
        this.columnas = columnas;
        this.matriz = crearMatrizInicial(filas, columnas);
    }

    /**
     * Construye la matriz fila a fila.
     *
     * No se utiliza ningun array auxiliar: cada fila se crea como ListaSE<Celda>
     * y se anade al final de la lista externa para conservar el orden natural
     * de lectura del mapa: fila 0, fila 1, fila 2...
     */
    private ListaSE<ListaSE<Celda>> crearMatrizInicial(int totalFilas, int totalColumnas) {
        ListaSE<ListaSE<Celda>> nuevaMatriz = new ListaSE<>();

        /*
         * La matriz de la cueva se representa como una lista de filas y cada
         * fila como una lista de celdas. Asi se evita usar arrays Java para el
         * mapa evaluable y mantenemos una estructura propia explicable.
         */
        for (int fila = 0; fila < totalFilas; fila++) {
            ListaSE<Celda> filaCeldas = new ListaSE<>();
            for (int columna = 0; columna < totalColumnas; columna++) {
                filaCeldas.addLast(new Celda(fila, columna, TipoCelda.SUELO));
            }
            nuevaMatriz.addLast(filaCeldas);
        }

        return nuevaMatriz;
    }

    public String getId() {
        return id;
    }

    @Override
    public int getFilas() {
        return filas;
    }

    @Override
    public int getColumnas() {
        return columnas;
    }

    @Override
    public ListaSE<ListaSE<Celda>> getMatriz() {
        /*
         * Se devuelve una copia de la estructura de listas para proteger la
         * forma rectangular de la cueva. Las celdas son las mismas instancias
         * porque otros modulos deben poder consultar su estado actual, pero no
         * pueden borrar filas/columnas internas haciendo clear() sobre la matriz
         * original.
         */
        ListaSE<ListaSE<Celda>> copia = new ListaSE<>();
        for (int fila = 0; fila < matriz.getSize(); fila++) {
            copia.addLast(matriz.get(fila).copy());
        }
        return copia;
    }

    /**
     * Comprueba si unas coordenadas pertenecen a la cueva.
     *
     * Este metodo no lanza excepcion porque se usa tambien en calculos de
     * vecinos: durante un BFS es normal preguntar por posiciones candidatas que
     * pueden quedar fuera del mapa.
     */
    public boolean estaDentro(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    /**
     * Devuelve la celda almacenada en una posicion concreta.
     *
     * El acceso recorre dos listas enlazadas: primero la lista externa hasta la
     * fila indicada y luego la fila hasta la columna. Si la posicion no existe,
     * se lanza una excepcion para detectar pronto errores de logica.
     */
    public Celda getCelda(int fila, int columna) {
        validarPosicion(fila, columna);
        return matriz.get(fila).get(columna);
    }

    /**
     * Cambia el tipo de una celda sin sustituir el objeto Celda.
     *
     * Mantener la misma instancia es util porque otras partes del juego podrian
     * tener una referencia a esa celda durante un turno o durante un calculo de
     * movimiento.
     */
    public void cambiarTipoCelda(int fila, int columna, TipoCelda tipo) {
        getCelda(fila, columna).setTipo(tipo);
    }

    /**
     * Indica si una posicion puede pisarse.
     *
     * Si la posicion esta fuera de la cueva devuelve false. Esto simplifica el
     * futuro BFS: los vecinos fuera del mapa se descartan como no transitables
     * sin necesidad de capturar excepciones.
     */
    public boolean esTransitable(int fila, int columna) {
        return estaDentro(fila, columna) && getCelda(fila, columna).esTransitable();
    }

    /**
     * Calcula las celdas alcanzables desde una posicion usando BFS.
     *
     * BFS (Breadth First Search, busqueda en anchura) explora primero las
     * celdas a distancia 0, despues las de distancia 1, despues distancia 2,
     * etc. Por eso encaja con movimiento por pasos: cuando una celda se saca de
     * la cola, sabemos que se ha alcanzado con el menor numero de pasos posible
     * dentro de esta cueva.
     *
     * No se crea un Grafo<Celda> permanente. Las vecinas se calculan de forma
     * implicita desde las coordenadas actuales: arriba, abajo, izquierda y
     * derecha. No hay diagonales.
     *
     * Estructuras usadas:
     * - Cola<PasoBFS>: frontera pendiente de explorar.
     * - ListaSE<Posicion>: posiciones ya visitadas.
     * - ListaSE<Celda>: resultado final para logica, tests o interfaz.
     *
     * Coste aproximado: O(filas * columnas * costeVisitado). Como visitados es
     * una ListaSE, comprobar repetidos cuesta O(n). Es aceptable para mapas
     * pequenos y mantiene la restriccion de no usar HashSet.
     */
    public ListaSE<Celda> getCeldasAlcanzables(int filaInicio, int columnaInicio, int pasosMaximos) {
        ListaSE<Celda> alcanzables = new ListaSE<>();
        if (pasosMaximos < 0 || !esTransitable(filaInicio, columnaInicio)) {
            return alcanzables;
        }

        Cola<PasoBFS> pendientes = new Cola<>();
        ListaSE<Posicion> visitadas = new ListaSE<>();
        Posicion inicio = new Posicion(filaInicio, columnaInicio);

        pendientes.offer(new PasoBFS(inicio, 0));
        visitadas.addLast(inicio);

        while (!pendientes.isEmpty()) {
            PasoBFS paso = pendientes.poll();
            Posicion actual = paso.getPosicion();
            alcanzables.addLast(getCelda(actual.getFila(), actual.getColumna()));

            if (paso.getDistancia() < pasosMaximos) {
                anadirVecinoSiValido(actual.getFila() - 1, actual.getColumna(), paso.getDistancia() + 1, pendientes, visitadas);
                anadirVecinoSiValido(actual.getFila() + 1, actual.getColumna(), paso.getDistancia() + 1, pendientes, visitadas);
                anadirVecinoSiValido(actual.getFila(), actual.getColumna() - 1, paso.getDistancia() + 1, pendientes, visitadas);
                anadirVecinoSiValido(actual.getFila(), actual.getColumna() + 1, paso.getDistancia() + 1, pendientes, visitadas);
            }
        }

        return alcanzables;
    }

    /**
     * Calcula el camino minimo entre dos celdas de la misma cueva.
     *
     * Se usa BFS porque todas las celdas transitables cuestan un paso. Con esa
     * condicion, la primera vez que BFS llega al destino, el camino encontrado
     * es el mas corto en numero de celdas recorridas.
     *
     * El camino devuelto incluye la celda inicial y la celda destino. Por
     * ejemplo, si origen y destino son la misma celda, el resultado contiene
     * una unica celda. Esto es coherente con la regla acordada: quedarse en la
     * celda actual cuenta como una opcion valida del turno.
     *
     * Este metodo tambien permite calcular distancia para ataques a rango:
     * si el camino existe, la distancia en pasos es camino.getSize() - 1. Si no
     * existe camino, el resultado es una lista vacia.
     */
    public ListaSE<Celda> getCaminoMinimo(int filaInicio, int columnaInicio, int filaDestino, int columnaDestino) {
        ListaSE<Celda> caminoVacio = new ListaSE<>();
        if (!esTransitable(filaInicio, columnaInicio) || !esTransitable(filaDestino, columnaDestino)) {
            return caminoVacio;
        }

        Posicion inicio = new Posicion(filaInicio, columnaInicio);
        Posicion destino = new Posicion(filaDestino, columnaDestino);

        Cola<PasoBFS> pendientes = new Cola<>();
        ListaSE<Posicion> visitadas = new ListaSE<>();
        ListaSE<PasoCamino> padres = new ListaSE<>();

        pendientes.offer(new PasoBFS(inicio, 0));
        visitadas.addLast(inicio);
        padres.addLast(new PasoCamino(inicio, null));

        while (!pendientes.isEmpty()) {
            PasoBFS paso = pendientes.poll();
            Posicion actual = paso.getPosicion();

            if (actual.equals(destino)) {
                return reconstruirCamino(destino, padres);
            }

            anadirVecinoCaminoSiValido(actual, actual.getFila() - 1, actual.getColumna(), pendientes, visitadas, padres);
            anadirVecinoCaminoSiValido(actual, actual.getFila() + 1, actual.getColumna(), pendientes, visitadas, padres);
            anadirVecinoCaminoSiValido(actual, actual.getFila(), actual.getColumna() - 1, pendientes, visitadas, padres);
            anadirVecinoCaminoSiValido(actual, actual.getFila(), actual.getColumna() + 1, pendientes, visitadas, padres);
        }

        return caminoVacio;
    }

    /**
     * Devuelve la distancia minima en pasos entre dos celdas.
     *
     * La distancia es el numero de movimientos necesarios, no el numero de
     * celdas del camino. Por eso se resta 1 al tamano del camino. Si no hay
     * camino, se devuelve -1 para distinguirlo de distancia 0, que significa
     * origen igual a destino.
     */
    public int getDistanciaMinima(int filaInicio, int columnaInicio, int filaDestino, int columnaDestino) {
        ListaSE<Celda> camino = getCaminoMinimo(filaInicio, columnaInicio, filaDestino, columnaDestino);
        if (camino.isEmpty()) {
            return -1;
        }
        return camino.getSize() - 1;
    }

    /**
     * Valida y encola una posicion vecina durante BFS.
     *
     * La posicion solo entra en la cola si cumple tres condiciones:
     * esta dentro del mapa, es transitable y no habia sido visitada antes.
     * Marcarla como visitada en el momento de encolar evita duplicados si dos
     * caminos distintos llegan a la misma celda en el mismo nivel de BFS.
     */
    private void anadirVecinoSiValido(
            int fila,
            int columna,
            int distancia,
            Cola<PasoBFS> pendientes,
            ListaSE<Posicion> visitadas) {

        Posicion posicion = new Posicion(fila, columna);
        if (esTransitable(fila, columna) && !visitadas.existeDato(posicion)) {
            visitadas.addLast(posicion);
            pendientes.offer(new PasoBFS(posicion, distancia));
        }
    }

    /**
     * Variante de encolado usada por camino minimo.
     *
     * Ademas de marcar la posicion como visitada y meterla en la cola, guarda
     * desde que posicion se llego a ella. Esa relacion hijo -> padre permite
     * reconstruir el camino al final sin usar mapas externos.
     */
    private void anadirVecinoCaminoSiValido(
            Posicion padre,
            int fila,
            int columna,
            Cola<PasoBFS> pendientes,
            ListaSE<Posicion> visitadas,
            ListaSE<PasoCamino> padres) {

        Posicion posicion = new Posicion(fila, columna);
        if (esTransitable(fila, columna) && !visitadas.existeDato(posicion)) {
            visitadas.addLast(posicion);
            padres.addLast(new PasoCamino(posicion, padre));
            pendientes.offer(new PasoBFS(posicion, 0));
        }
    }

    /**
     * Reconstruye el camino desde el destino hasta el inicio usando la lista de
     * padres calculada por BFS.
     *
     * Primero se anaden las celdas hacia atras: destino, padre, abuelo... Luego
     * se invierte la ListaSE para devolver el orden natural: inicio -> destino.
     */
    private ListaSE<Celda> reconstruirCamino(Posicion destino, ListaSE<PasoCamino> padres) {
        ListaSE<Celda> camino = new ListaSE<>();
        Posicion actual = destino;

        while (actual != null) {
            camino.addLast(getCelda(actual.getFila(), actual.getColumna()));
            actual = buscarPadre(actual, padres);
        }

        camino.invertir();
        return camino;
    }

    /**
     * Busca el padre de una posicion dentro de la lista de relaciones del BFS.
     *
     * Se usa busqueda lineal con ListaSE para respetar la restriccion de no
     * usar HashMap. El coste es mayor, pero el algoritmo queda claro y propio.
     */
    private Posicion buscarPadre(Posicion posicion, ListaSE<PasoCamino> padres) {
        for (int indice = 0; indice < padres.getSize(); indice++) {
            PasoCamino paso = padres.get(indice);
            if (paso.getPosicion().equals(posicion)) {
                return paso.getPadre();
            }
        }
        return null;
    }

    /**
     * Validacion estricta para operaciones que necesitan una celda real.
     */
    private void validarPosicion(int fila, int columna) {
        if (!estaDentro(fila, columna)) {
            throw new IndexOutOfBoundsException("Posicion fuera de la cueva: " + fila + ", " + columna);
        }
    }

    /**
     * Dos cuevas se consideran la misma si tienen el mismo identificador.
     *
     * Esta decision es importante para Grafo<Cueva>: la estructura del grafo
     * usa equals() para evitar nodos duplicados, por lo que el id actua como
     * clave logica del nivel. No se comparan filas, columnas ni matriz porque
     * dos referencias al mismo nivel deben identificarse por su id estable.
     */
    @Override
    public boolean equals(Object otro) {
        if (!(otro instanceof Cueva)) {
            return false;
        }
        Cueva otra = (Cueva) otro;
        return id.equals(otra.id);
    }

    /**
     * Mantiene el contrato de Java junto a equals().
     *
     * No implica usar HashMap o HashSet; el grafo propio sigue usando ListaSE.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Registro interno del BFS.
     *
     * Guarda una posicion y la distancia en pasos desde el origen. Esta clase
     * evita usar dos colas paralelas o mapas externos para recordar distancias.
     */
    private static class PasoBFS {
        private final Posicion posicion;
        private final int distancia;

        private PasoBFS(Posicion posicion, int distancia) {
            this.posicion = posicion;
            this.distancia = distancia;
        }

        private Posicion getPosicion() {
            return posicion;
        }

        private int getDistancia() {
            return distancia;
        }
    }

    /**
     * Relacion entre una posicion descubierta por BFS y la posicion desde la
     * que se llego a ella. Sustituye a un mapa de padres externo.
     */
    private static class PasoCamino {
        private final Posicion posicion;
        private final Posicion padre;

        private PasoCamino(Posicion posicion, Posicion padre) {
            this.posicion = posicion;
            this.padre = padre;
        }

        private Posicion getPosicion() {
            return posicion;
        }

        private Posicion getPadre() {
            return padre;
        }
    }
}
