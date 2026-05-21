package modelo.juego;

import Estructuras.Grafo;
import Estructuras.ListaSE;
import modelo.mapa.Cueva;

/**
 * Estructura global de cuevas de la partida.
 *
 * La mazmorra no hereda de Grafo: lo contiene. Esta decision mantiene separada
 * la estructura general de niveles del resto del juego y cumple el diseno:
 *
 *     Mazmorra contiene Grafo<Cueva>
 *
 * Cada nodo del grafo es una cueva y cada arco dirigido representa una puerta o
 * conexion de avance. Como las puertas son unidireccionales, avanzar de una
 * cueva a otra no crea automaticamente el camino de vuelta.
 *
 * Esta clase no gestiona jugador, enemigos, objetos, turnos ni condiciones de
 * victoria. Eso pertenece a Parte B. Aqui solo se administra la estructura de
 * cuevas y la cueva actual.
 */
public class Mazmorra implements InterfazMazmorra {
    /*
     * Fuente de verdad de cuevas y conexiones. No se mantiene una lista
     * duplicada de cuevas para evitar inconsistencias entre estructuras.
     */
    private final Grafo<Cueva> grafoCuevas;

    /*
     * Cueva activa en la partida. Puede ser null si aun no se ha fijado una
     * cueva inicial.
     */
    private Cueva cuevaActual;

    public Mazmorra() {
        this.grafoCuevas = new Grafo<>();
        this.cuevaActual = null;
    }

    /**
     * Anade una cueva al grafo de la mazmorra.
     *
     * Si la referencia recibida es null se devuelve false. La mazmorra solo
     * puede contener cuevas reales, porque otros metodos consultan su id,
     * conexiones y matriz.
     *
     * Si es la primera cueva registrada, tambien se establece como cueva actual
     * por defecto. Asi la mazmorra queda utilizable desde el primer nivel sin
     * obligar a otra parte a llamar inmediatamente a setCuevaActual().
     */
    @Override
    public boolean addCueva(Cueva cueva) {
        if (cueva == null) {
            return false;
        }
        boolean anadida = grafoCuevas.addNodo(cueva);
        if (anadida && cuevaActual == null) {
            cuevaActual = cueva;
        }
        return anadida;
    }

    /**
     * Crea una puerta dirigida entre dos cuevas.
     *
     * El grafo crea los nodos si no existian. Esto permite construir la
     * mazmorra a partir de conexiones leidas de JSON sin tener que hacer dos
     * pasadas obligatorias. Si no habia cueva actual y se crea el origen, se
     * usa ese origen como inicio por defecto.
     *
     * Si origen o destino son null, se devuelve false para evitar nodos nulos
     * dentro del grafo.
     */
    @Override
    public boolean conectarCuevas(Cueva origen, Cueva destino, String etiquetaPuerta) {
        if (origen == null || destino == null) {
            return false;
        }
        boolean sinCuevaActual = cuevaActual == null;
        boolean anadida = grafoCuevas.addArco(origen, destino, etiquetaPuerta);
        if (sinCuevaActual && grafoCuevas.existeNodo(origen)) {
            cuevaActual = origen;
        }
        return anadida;
    }

    @Override
    public boolean existeCueva(Cueva cueva) {
        if (cueva == null) {
            return false;
        }
        return grafoCuevas.existeNodo(cueva);
    }

    @Override
    public boolean existeConexion(Cueva origen, Cueva destino) {
        if (origen == null || destino == null) {
            return false;
        }
        return grafoCuevas.existeConexion(origen, destino);
    }

    /**
     * Busca por id recorriendo la lista propia de cuevas.
     *
     * No se usa HashMap para mantener la restriccion de estructuras. El coste
     * lineal es aceptable porque la practica trabaja con pocas cuevas.
     */
    @Override
    public Cueva getCuevaPorId(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        ListaSE<Cueva> cuevas = grafoCuevas.getNodos();
        for (int indice = 0; indice < cuevas.getSize(); indice++) {
            Cueva cueva = cuevas.get(indice);
            if (cueva.getId().equals(id)) {
                return cueva;
            }
        }
        return null;
    }

    @Override
    public ListaSE<Cueva> getCuevas() {
        return grafoCuevas.getNodos();
    }

    @Override
    public int getNumeroCuevas() {
        return grafoCuevas.getNumeroNodos();
    }

    @Override
    public int getNumeroConexiones() {
        return grafoCuevas.getNumeroArcos();
    }

    @Override
    public ListaSE<Cueva> getCuevasSiguientes(Cueva cueva) {
        if (cueva == null) {
            return new ListaSE<>();
        }
        return grafoCuevas.getAdyacentes(cueva);
    }

    @Override
    public Cueva getCuevaActual() {
        return cuevaActual;
    }

    @Override
    public boolean esCuevaActual(Cueva cueva) {
        if (cuevaActual == null || cueva == null) {
            return false;
        }
        return cuevaActual.equals(cueva);
    }

    /**
     * Cambia la cueva actual solo si pertenece a la mazmorra.
     *
     * Esto evita que Parte B o C dejen la partida apuntando a una cueva
     * externa que no forma parte del grafo.
     */
    @Override
    public boolean setCuevaActual(Cueva cueva) {
        if (cueva == null) {
            return false;
        }
        if (!existeCueva(cueva)) {
            return false;
        }
        cuevaActual = cueva;
        return true;
    }

    /**
     * Avanza desde la cueva actual a una cueva destino conectada directamente.
     *
     * No busca caminos largos. Para avanzar de nivel en nivel debe existir un
     * arco directo cuevaActual -> destino, respetando la direccion de la puerta.
     */
    @Override
    public boolean avanzarACueva(Cueva destino) {
        if (destino == null || cuevaActual == null || !existeConexion(cuevaActual, destino)) {
            return false;
        }
        cuevaActual = destino;
        return true;
    }

    @Override
    public boolean existeCamino(Cueva origen, Cueva destino) {
        if (origen == null || destino == null) {
            return false;
        }
        return grafoCuevas.existeCamino(origen, destino);
    }

    @Override
    public ListaSE<Cueva> getCaminoMinimo(Cueva origen, Cueva destino) {
        if (origen == null || destino == null) {
            return new ListaSE<>();
        }
        return grafoCuevas.caminoMinimo(origen, destino);
    }

    @Override
    public int getDistanciaMinima(Cueva origen, Cueva destino) {
        if (origen == null || destino == null) {
            return -1;
        }
        return grafoCuevas.getDistanciaMinima(origen, destino);
    }
}
