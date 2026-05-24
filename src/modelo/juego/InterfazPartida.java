package modelo.juego;

import Estructuras.ListaDE;
import Estructuras.ListaSE;
import modelo.objetos.Objeto;

/**
 * Contrato publico de la partida.
 *
 * Partida sera la fachada de logica del juego: JavaFX y JSON no deberian
 * modificar directamente jugador, mazmorra, inventario, enemigos u objetos en
 * el suelo. Deben consultar estado y pedir acciones por estos metodos de alto
 * nivel.
 *
 * Esta primera version define el contrato necesario para una partida jugable
 * minima.
 *
 * Aviso de integracion: este contrato ya expresa lo que necesita la logica,
 * pero todavia falta conectarlo en profundidad con JSON y JavaFX. Los metodos
 * de preparacion de contenidos quedan fuera de esta interfaz para no mezclarlos
 * con acciones del jugador.
 */
public interface InterfazPartida {

    /**
     * Devuelve una vista inmutable de la cueva activa.
     *
     * Es el mapa que la interfaz debe pintar y sobre el que se resuelven
     * movimiento, enemigos, objetos y puertas en el turno actual.
     */
    CuevaEnMapa getCuevaActual();

    /**
     * Devuelve una vista inmutable del jugador.
     */
    PersonajeEnMapa getJugadorEnMapa();

    /**
     * Devuelve una copia con vistas inmutables de los enemigos activos de la
     * cueva actual.
     */
    ListaSE<PersonajeEnMapa> getEnemigos();

    /**
     * Devuelve una copia con vistas inmutables de los enemigos vivos que estan
     * en las 8 celdas alrededor del jugador.
     */
    ListaSE<PersonajeEnMapa> getEnemigosAdyacentes();

    /**
     * Devuelve una copia de los objetos disponibles en el suelo de la cueva
     * actual, junto a su posicion.
     */
    ListaSE<ObjetoEnMapa> getObjetosEnSuelo();

    /**
     * Devuelve una copia del inventario del jugador.
     */
    ListaDE<Objeto> getInventario();

    /**
     * Devuelve los mensajes de la partida para que JavaFX pueda mostrar un log.
     */
    ListaSE<String> getLog();

    /**
     * Devuelve los turnos restantes antes de perder por agotamiento de tiempo.
     */
    int getTurnosRestantes();

    /**
     * Devuelve si la partida sigue en curso, ha terminado en victoria o ha
     * terminado en derrota.
     */
    EstadoPartida getEstado();

    /**
     * Indica si el jugador ya ha realizado su movimiento en el turno actual.
     */
    boolean isMovimientoRealizado();

    /**
     * Indica si el jugador ya ha realizado su accion en el turno actual.
     */
    boolean isAccionRealizada();

    /**
     * Calcula las celdas a las que puede moverse el jugador desde su posicion
     * actual usando el movimiento disponible.
     */
    ListaSE<CeldaEnMapa> getCeldasAlcanzablesJugador();

    /**
     * Intenta mover al jugador a la posicion indicada.
     *
     * Devuelve true si la posicion esta dentro de la cueva, es transitable, esta
     * dentro del alcance de movimiento y el jugador aun no se ha movido en este
     * turno.
     */
    boolean moverJugador(int fila, int columna);

    /**
     * Intenta atacar al enemigo situado en la posicion indicada.
     *
     * Devuelve true si habia un enemigo valido y se ha aplicado dano.
     */
    boolean atacar(int fila, int columna);

    /**
     * Indica si hay un enemigo vivo en una direccion relativa al jugador.
     *
     * Por ejemplo, (-1, 0) consulta arriba y (0, 1) consulta derecha. Este
     * metodo solo consulta estado: no consume accion ni modifica la partida.
     */
    boolean hayEnemigoEnDireccion(int df, int dc);

    /**
     * Intenta recoger un objeto del suelo por su identificador.
     *
     * El objeto debe estar en la cueva actual y en una celda adyacente al
     * jugador, contando diagonales y la propia celda del jugador.
     */
    boolean recogerObjeto(String idObjeto);

    /**
     * Abre un tesoro cercano al jugador.
     *
     * Devuelve true si el jugador estaba sobre una celda TESORO (para partidas
     * antiguas) o en una celda cardinal adyacente a un TESORO cerrado.
     */
    boolean abrirTesoro();

    /**
     * Indica si el jugador puede abrir un tesoro desde su posicion actual.
     */
    boolean hayTesoroCercano();

    /**
     * Intenta usar un objeto del inventario por su identificador.
     *
     * En la primera version el caso obligatorio sera usar pociones.
     */
    boolean usarObjeto(String idObjeto);

    /**
     * Intenta equipar un objeto del inventario por su identificador.
     *
     * En la primera version cubre arma y escudo.
     */
    boolean equiparObjeto(String idObjeto);

    /**
     * Intenta avanzar desde la cueva actual hasta otra cueva conectada.
     *
     * Para avanzar, el jugador debe tener la llave asociada a la puerta.
     */
    boolean avanzarACueva(String idCuevaDestino);

    /**
     * Indica si el jugador ya tiene la llave final de victoria.
     */
    boolean tieneLlaveFinal();

    /**
     * Finaliza el turno voluntariamente.
     *
     * Despues deben actuar los enemigos, restarse un turno y comprobarse
     * victoria o derrota.
     */
    boolean pasarTurno();

    /**
     * Devuelve la distancia en pasos desde el jugador a la celda PUERTA
     * mas cercana en la cueva actual.
     *
     * Si no hay ninguna PUERTA accesible o la cueva no tiene PUERTA,
     * devuelve -1.
     */
    int getDistanciaAPuerta();

    /**
     * Devuelve el numero minimo de cuevas que el jugador debe atravesar
     * (contando desde la cueva actual) para llegar a la cueva que contiene
     * la casilla de SALIDA.
     *
     * Devuelve -1 si no se encuentra la SALIDA o no hay camino.
     */
    int getDistanciaMinimaCuevasASalida();

    /**
     * Compra la vision del camino pagando 5 turnos.
     *
     * Calcula la ruta mas corta desde la posicion actual del jugador
     * hasta la PUERTA mas cercana dentro de la cueva actual y la almacena
     * para que la interfaz pueda dibujarla.
     *
     * Devuelve true si la compra se realizo con exito, false si no
     * hay suficientes turnos o ya se habia comprado antes.
     */
    boolean comprarVisionCamino();

    /**
     * Indica si el jugador ha comprado la vision del camino.
     */
    boolean isVisionCaminoComprada();

    /**
     * Devuelve el camino comprado como una lista de celdas vista
     * (CeldaEnMapa) listas para dibujar en la interfaz.
     *
     * Si no se ha comprado la vision o el camino esta vacio,
     * devuelve una lista vacia.
     */
    ListaSE<CeldaEnMapa> getCaminoComprado();

    /**
     * Devuelve la cadena de cuevas desde la actual hasta la que
     * contiene la SALIDA, como lista de IDs.
     *
     * Solo tiene contenido si se ha comprado la vision del camino.
     */
    ListaSE<String> getCaminoCompradoCuevas();
}
