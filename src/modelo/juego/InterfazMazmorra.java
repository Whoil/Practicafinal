package modelo.juego;

import Estructuras.ListaSE;
import modelo.mapa.Cueva;

/**
 * Contrato publico de la mazmorra.
 *
 * Esta interfaz define como van a relacionarse las otras partes del proyecto
 * con la estructura global de cuevas, sin obligarlas a conocer los detalles
 * internos del Grafo<Cueva>. La decision arquitectonica sigue siendo:
 *
 *     Mazmorra contiene Grafo<Cueva>
 *
 * pero el grafo queda encapsulado. Parte B podra preguntar por la cueva actual,
 * avanzar a otra cueva si hay conexion dirigida y consultar caminos entre
 * cuevas. Parte C podra mostrar las cuevas disponibles sin modificar la
 * estructura interna.
 */
public interface InterfazMazmorra {

    /**
     * Anade una cueva a la mazmorra.
     *
     * Devuelve true si la cueva se ha anadido y false si ya existia otra cueva
     * con el mismo identificador logico. La igualdad de Cueva depende de su id.
     */
    boolean addCueva(Cueva cueva);

    /**
     * Crea una conexion dirigida entre dos cuevas.
     *
     * La direccion importa: conectar origen -> destino no permite volver desde
     * destino -> origen. Esto representa puertas de avance entre niveles.
     */
    boolean conectarCuevas(Cueva origen, Cueva destino, String etiquetaPuerta);

    /**
     * Indica si una cueva pertenece a la mazmorra.
     *
     * La comprobacion se basa en la igualdad de Cueva, que actualmente depende
     * del id. Es util para validar entradas antes de cambiar la cueva actual o
     * crear conexiones.
     */
    boolean existeCueva(Cueva cueva);

    /**
     * Indica si existe una conexion directa y dirigida entre dos cuevas.
     *
     * Devuelve true solo para origen -> destino. Si solo existe destino ->
     * origen, debe devolver false porque las puertas de la mazmorra son
     * unidireccionales.
     */
    boolean existeConexion(Cueva origen, Cueva destino);

    /**
     * Busca una cueva por su identificador estable.
     *
     * Este metodo es especialmente util para JSON e interfaz, porque permite
     * convertir un id textual en la instancia de Cueva registrada dentro de la
     * mazmorra.
     */
    Cueva getCuevaPorId(String id);

    /**
     * Devuelve una copia de la lista de cuevas registradas.
     *
     * La implementacion debera proteger su estructura interna para que otras
     * partes no puedan borrar nodos de la mazmorra directamente.
     */
    ListaSE<Cueva> getCuevas();

    /**
     * Devuelve el numero de cuevas registradas.
     *
     * Sirve para tests, validaciones de carga y comprobaciones de interfaz.
     */
    int getNumeroCuevas();

    /**
     * Devuelve el numero de conexiones dirigidas entre cuevas.
     *
     * Sirve para comprobar que el grafo de la mazmorra se ha construido o
     * cargado correctamente.
     */
    int getNumeroConexiones();

    /**
     * Devuelve las cuevas a las que se puede avanzar desde una cueva concreta.
     *
     * Solo tiene en cuenta conexiones salientes, respetando el grafo dirigido.
     */
    ListaSE<Cueva> getCuevasSiguientes(Cueva cueva);

    /**
     * Devuelve la cueva actual de la partida.
     *
     * Parte B usara esta referencia para aplicar reglas de movimiento, combate
     * o puertas dentro de la cueva activa. Parte C la usara para pintar el mapa.
     */
    Cueva getCuevaActual();

    /**
     * Indica si la cueva recibida es la cueva actual.
     *
     * Es una consulta comoda para interfaz y logica, por ejemplo para marcar en
     * pantalla que nivel esta activo.
     */
    boolean esCuevaActual(Cueva cueva);

    /**
     * Establece la cueva actual.
     *
     * Debe usarse con cuidado: la implementacion debera validar que la cueva
     * existe en la mazmorra antes de aceptarla.
     */
    boolean setCuevaActual(Cueva cueva);

    /**
     * Intenta avanzar desde la cueva actual hasta la cueva destino.
     *
     * Devuelve true si existe conexion dirigida desde la cueva actual hasta el
     * destino y, en ese caso, actualiza la cueva actual. Devuelve false si no
     * hay cueva actual, si el destino no existe o si la conexion no esta
     * permitida.
     */
    boolean avanzarACueva(Cueva destino);

    /**
     * Comprueba si existe un camino dirigido entre dos cuevas.
     *
     * Es util para validaciones de mapa, depuracion y posibles reglas futuras.
     */
    boolean existeCamino(Cueva origen, Cueva destino);

    /**
     * Devuelve el camino minimo entre dos cuevas.
     *
     * El camino se mide en numero de puertas atravesadas y respeta la direccion
     * de los arcos. Si no hay camino, devuelve una lista vacia.
     */
    ListaSE<Cueva> getCaminoMinimo(Cueva origen, Cueva destino);

    /**
     * Devuelve la distancia minima entre dos cuevas.
     *
     * La distancia es el numero de puertas del camino minimo. Si no hay camino,
     * devuelve -1.
     */
    int getDistanciaMinima(Cueva origen, Cueva destino);
}
