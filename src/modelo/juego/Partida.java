package modelo.juego;

import java.io.IOException;

import Estructuras.ListaDE;
import Estructuras.ListaSE;
import Estructuras.MiIterador;
import json.CargadorConfiguracion;
import json.ConexionDTO;
import json.DatosCuevaDTO;
import json.DatosEnemigoDTO;
import json.DatosMazmorraDTO;
import json.DatosObjetoDTO;
import json.DatosPartidaDTO;
import json.DatosPuertaDTO;
import json.DatosJugadorDTO;
import json.ResultadoCarga;
import json.SerializadorPartida;
import modelo.mapa.Celda;
import modelo.mapa.Cueva;
import modelo.mapa.TipoCelda;
import modelo.objetos.Arco;
import modelo.objetos.Arma;
import modelo.objetos.Escudo;
import modelo.objetos.Llave;
import modelo.objetos.Objeto;
import modelo.objetos.Pocion;
import modelo.objetos.TipoLlave;
import modelo.personajes.Boss;
import modelo.personajes.Enemigo;
import modelo.personajes.Jugador;
import modelo.personajes.TipoEnemigo;
import modelo.objetos.Espada;



/**
 * Controla la logica principal de una partida.
 *
 * Partida no crea la mazmorra desde cero: recibe una Mazmorra ya construida y
 * coordina el estado jugable que vive sobre ella. En esta primera version, los
 * enemigos y objetos se guardan por cueva dentro de Partida para no cargar a
 * Cueva con responsabilidades de logica. Esa decision deja margen para mover
 * esos contenidos a Cueva en una iteracion futura.
 */
public class Partida implements InterfazPartida {
    /*
     * IMPORTANTE - Integracion actual:
     *
     * Esta clase es una primera version funcional de la logica de partida, pero
     * todavia no esta conectada en profundidad con todo el proyecto. En
     * concreto:
     *
     * - JSON ya puede crear una Mazmorra, pero aun no construye una Partida
     *   completa con jugador, enemigos, objetos posicionados y puertas.
     * - JavaFX todavia no consume InterfazPartida.
     * - Cueva sigue siendo solo matriz/terreno; no guarda sus propios enemigos
     *   ni objetos.
     * - Los contenidos por cueva viven en Partida como capa temporal para
     *   poder avanzar B-03 sin romper la responsabilidad de Cueva.
     *
     * Por tanto, esta clase sirve como nucleo de reglas, no como integracion
     * final de toda la aplicacion.
     */
    public static final int ALCANCE_ARCO = 3;
    public static final int ALCANCE_ARQUERO = 6;
    public static final int TURNOS_CONGELACION = 3;
    public static final int TURNOS_POR_CUEVA = 60;
    public static final String CODIGO_LLAVE_FINAL_DEFECTO = "LLAVE_FINAL";
    public static final String ID_LLAVE_FINAL_DEFECTO = "llave-final";

    private final Mazmorra mazmorra;
    private final Jugador jugador;
    private final ListaSE<ContenidoCueva> contenidosPorCueva;
    private final ListaSE<Puerta> puertas;
    private final ListaSE<String> log;
    private final ListaSE<DisparoEnemigo> disparosEnemigosPendientes;
    private final String codigoLlaveFinal;
    private final EstadisticasPartida estadisticas;

    /*
     * Estos flags controlan la regla acordada de "un movimiento" y "una
     * accion" por turno. Aun asi, el turno no se cierra automaticamente:
     * solamente se resetean cuando el usuario llama a pasarTurno().
     */
    private int turnosRestantes;
    private EstadoPartida estado;
    private boolean movimientoRealizado;
    private boolean accionRealizada;

    /*
     * Cache de la vision comprada del camino.
     *
     * visionCaminoComprada indica si el jugador pago 5 turnos para ver
     * la ruta a la puerta mas cercana. caminoComprado guarda la secuencia
     * de celdas (modelo) de la cueva actual hasta la PUERTA.
     */
    private boolean visionCaminoComprada = false;
    private ListaSE<Celda> caminoComprado = new ListaSE<>();
    /*
     * Cadena de cuevas desde la actual hasta la que contiene la SALIDA.
     * Solo se rellena al comprar la vision (comprarVisionCamino).
     */
    private ListaSE<String> caminoCompradoCuevas = new ListaSE<>();

    public Partida(Mazmorra mazmorra, Jugador jugador, int turnosRestantes) {
        this(mazmorra, jugador, turnosRestantes, CODIGO_LLAVE_FINAL_DEFECTO);
    }

    public Partida(Mazmorra mazmorra, Jugador jugador, int turnosRestantes, String codigoLlaveFinal) {
        this(mazmorra, jugador, turnosRestantes, codigoLlaveFinal, new ListaSE<>());
    }

    public Partida(Mazmorra mazmorra, Jugador jugador, int turnosRestantes, ListaSE<Puerta> puertasIniciales) {
        this(mazmorra, jugador, turnosRestantes, CODIGO_LLAVE_FINAL_DEFECTO, puertasIniciales);
    }

    public Partida(Mazmorra mazmorra, Jugador jugador, int turnosRestantes, String codigoLlaveFinal, ListaSE<Puerta> puertasIniciales) {
        this(mazmorra, jugador, turnosRestantes, codigoLlaveFinal, puertasIniciales, null);
    }

    public Partida(Mazmorra mazmorra, Jugador jugador, int turnosRestantes, String codigoLlaveFinal,
                   ListaSE<Puerta> puertasIniciales, EstadisticasPartida estadisticasIniciales) {
        if (mazmorra == null) {
            throw new IllegalArgumentException("La mazmorra es obligatoria");
        }
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador es obligatorio");
        }
        if (turnosRestantes <= 0) {
            throw new IllegalArgumentException("Los turnos restantes deben ser positivos");
        }
        if (mazmorra.getCuevaActual() == null) {
            throw new IllegalArgumentException("La mazmorra debe tener una cueva actual");
        }
        validarTexto(codigoLlaveFinal, "codigoLlaveFinal");

        this.mazmorra = mazmorra;
        this.jugador = jugador;
        this.turnosRestantes = turnosRestantes;
        this.codigoLlaveFinal = codigoLlaveFinal;
        this.estado = EstadoPartida.EN_CURSO;
        this.movimientoRealizado = false;
        this.accionRealizada = false;
        this.contenidosPorCueva = new ListaSE<>();
        this.puertas = new ListaSE<>();
        this.log = new ListaSE<>();
        this.disparosEnemigosPendientes = new ListaSE<>();
        this.estadisticas = new EstadisticasPartida(estadisticasIniciales);

        cargarPuertasIniciales(puertasIniciales);
        registrarLog("Partida iniciada");
        comprobarVictoriaODerrota();
    }

    Mazmorra getMazmorra() {
        return mazmorra;
    }

    @Override
    public CuevaEnMapa getCuevaActual() {
        return crearVistaCueva(getCuevaActualInterna());
    }

    private Cueva getCuevaActualInterna() {
        return mazmorra.getCuevaActual();
    }

    @Override
    public PersonajeEnMapa getJugadorEnMapa() {
        return crearVistaJugador();
    }

    @Override
    public ListaSE<PersonajeEnMapa> getEnemigos() {
        ListaSE<PersonajeEnMapa> vistas = new ListaSE<>();
        ListaSE<Enemigo> enemigos = obtenerOCrearContenidoActual().getEnemigos();
        for (int indice = 0; indice < enemigos.getSize(); indice++) {
            Enemigo enemigo = enemigos.get(indice);
            if (enemigo != null && enemigo.estaVivo()) {
                vistas.addLast(crearVistaEnemigo(enemigo));
            }
        }
        return vistas;
    }

    @Override
    public ListaSE<PersonajeEnMapa> getEnemigosAdyacentes() {
        ListaSE<PersonajeEnMapa> vistas = new ListaSE<>();
        ListaSE<Enemigo> enemigos = obtenerOCrearContenidoActual().getEnemigos();
        for (int indice = 0; indice < enemigos.getSize(); indice++) {
            Enemigo enemigo = enemigos.get(indice);
            if (enemigo != null && enemigo.estaVivo()
                    && !jugadorEstaEn(enemigo.getFila(), enemigo.getColumna())
                    && estaEnCeldaCercanaAlJugador(enemigo.getFila(), enemigo.getColumna())) {
                vistas.addLast(crearVistaEnemigo(enemigo));
            }
        }
        return vistas;
    }

    @Override
    public ListaSE<ObjetoEnMapa> getObjetosEnSuelo() {
        return obtenerOCrearContenidoActual().getObjetosEnSuelo().copy();
    }

    ListaSE<Puerta> getPuertas() {
        return puertas.copy();
    }

    @Override
    public ListaDE<Objeto> getInventario() {
        return jugador.getInventario();
    }

    @Override
    public ListaSE<String> getLog() {
        return log.copy();
    }

    @Override
    public int getTurnosRestantes() {
        return turnosRestantes;
    }

    @Override
    public EstadoPartida getEstado() {
        return estado;
    }

    @Override
    public boolean isMovimientoRealizado() {
        return movimientoRealizado;
    }

    @Override
    public boolean isAccionRealizada() {
        return accionRealizada;
    }

    boolean anadirEnemigo(Cueva cueva, Enemigo enemigo) {
        /*
         * Metodo de preparacion de partida.
         *
         * No debe confundirse con una accion del jugador. Existe para que tests
         * o una futura fabrica/cargador puedan montar los contenidos iniciales
         * mientras Cueva no tenga una lista propia de enemigos.
         */
        if (cueva == null || enemigo == null || !mazmorra.existeCueva(cueva)) {
            return false;
        }
        if (!cueva.esTransitable(enemigo.getFila(), enemigo.getColumna())) {
            return false;
        }
        if (hayEnemigoEn(cueva, enemigo.getFila(), enemigo.getColumna())) {
            return false;
        }
        if (cueva.equals(getCuevaActualInterna()) && jugadorEstaEn(enemigo.getFila(), enemigo.getColumna())) {
            return false;
        }
        obtenerOCrearContenido(cueva).getEnemigos().addLast(enemigo);
        registrarLog("Enemigo registrado: " + enemigo.getNombre());
        return true;
    }

    boolean anadirObjetoEnSuelo(Cueva cueva, Objeto objeto, int fila, int columna) {
        /*
         * Metodo de preparacion de partida.
         *
         * Los objetos del inventario no tienen posicion. Por eso, cuando estan
         * en el mapa, se envuelven en ObjetoEnMapa. Esta decision evita meter
         * fila/columna dentro de Objeto, pero obliga a conectar despues esta
         * capa con JSON y JavaFX.
         */
        if (cueva == null || objeto == null || !mazmorra.existeCueva(cueva)) {
            return false;
        }
        if (!cueva.esTransitable(fila, columna)) {
            return false;
        }
        ObjetoEnMapa objetoEnMapa = new ObjetoEnMapa(objeto, cueva, fila, columna);
        obtenerOCrearContenido(cueva).getObjetosEnSuelo().addLast(objetoEnMapa);
        registrarLog("Objeto colocado: " + objeto.getNombre());
        return true;
    }

    private boolean registrarPuertaInicial(Puerta puerta) {
        /*
         * Las puertas se cargan como configuracion inicial de Partida.
         *
         * Partida no crea conexiones entre cuevas. Solo acepta puertas si la
         * conexion dirigida ya existe en Mazmorra. Asi se mantiene claro que
         * Mazmorra construye el grafo y Partida solo aplica requisitos de llave.
         */
        if (puerta == null) {
            return false;
        }
        if (!mazmorra.existeCueva(puerta.getOrigen()) || !mazmorra.existeCueva(puerta.getDestino())) {
            return false;
        }
        if (!mazmorra.existeConexion(puerta.getOrigen(), puerta.getDestino())) {
            return false;
        }
        if (puertas.existeDato(puerta)) {
            return false;
        }
        puertas.addLast(puerta);
        return true;
    }

    @Override
    public boolean moverJugador(int fila, int columna) {
        if (!puedeAceptarAccion() || movimientoRealizado) {
            return false;
        }

        Cueva cuevaActual = getCuevaActualInterna();
        if (cuevaActual == null || !cuevaActual.esTransitable(fila, columna)) {
            return false;
        }
        if (cuevaActual.getCelda(fila, columna).getTipo() == TipoCelda.TESORO) {
            return false;
        }
        if (hayEnemigoEn(cuevaActual, fila, columna)) {
            return false;
        }

        // Restringido a distancia Manhattan == 1: solo ortogonal, prohibido diagonal y saltos
        if (Math.abs(fila - jugador.getFila()) + Math.abs(columna - jugador.getColumna()) != 1) {
            return false;
        }

        jugador.cambiarPosicion(fila, columna);
        movimientoRealizado = true;
        registrarLog("Jugador movido a " + fila + ", " + columna);

        // Auto-recoger objeto si hay uno en la misma celda (no gasta accion)
        ObjetoEnMapa objSuelo = getObjetoEn(fila, columna);
        if (objSuelo != null && !jugador.tieneObjetoConId(objSuelo.getObjeto().getId())) {
            jugador.agregarObjeto(objSuelo.getObjeto());
            obtenerOCrearContenidoActual().getObjetosEnSuelo().del(objSuelo);
            registrarLog("Objeto recogido: " + objSuelo.getObjeto().getNombre());
        }

        comprobarVictoriaODerrota();
        return true;
    }

    @Override
    public boolean atacar(int fila, int columna) {
        if (!puedeAceptarAccion() || accionRealizada) {
            return false;
        }

        Enemigo enemigo = buscarEnemigoEn(fila, columna);
        if (enemigo == null || !enemigo.estaVivo() || !jugadorPuedeAtacar(enemigo)) {
            return false;
        }

        int vidaAntes = enemigo.getVidaActual();
        int dano = calcularDano(jugador.getAtaqueTotal(), enemigo.getDefensaBase());
        enemigo.recibirDano(dano);
        int danoReal = vidaAntes - enemigo.getVidaActual();
        estadisticas.registrarDanoEjercido(danoReal);
        accionRealizada = true;
        registrarLog("Jugador ataca a " + enemigo.getNombre() + " e inflige " + dano + " de daño");

        if (!enemigo.estaVivo()) {
            obtenerOCrearContenidoActual().getEnemigos().del(enemigo);
            registrarLog(enemigo.getNombre() + " derrotado");
            if (enemigo instanceof Boss) {
                estadisticas.registrarBossMuerto();
                entregarLlaveFinal();
            } else {
                estadisticas.registrarEnemigoComunMuerto();
            }
        }

        comprobarVictoriaODerrota();
        return true;
    }

    public boolean puedeDispararBolaFuego() {
        return puedeAceptarAccion() && !accionRealizada;
    }

    public boolean registrarDisparoBolaFuego() {
        if (!puedeDispararBolaFuego()) {
            return false;
        }
        accionRealizada = true;
        registrarLog("Bola de Fuego lanzada");
        return true;
    }

    public boolean puedeDispararBolaHielo() {
        return puedeAceptarAccion() && !accionRealizada;
    }

    public boolean registrarDisparoBolaHielo() {
        if (!puedeDispararBolaHielo()) {
            return false;
        }
        accionRealizada = true;
        registrarLog("Bola de Hielo lanzada");
        return true;
    }

    public ResultadoImpactoBolaFuego impactarBolaFuego(int fila, int columna, int dano) {
        if (estado != EstadoPartida.EN_CURSO || dano <= 0) {
            return ResultadoImpactoBolaFuego.sinImpacto();
        }

        Enemigo enemigo = buscarEnemigoEn(fila, columna);
        if (enemigo == null || !enemigo.estaVivo()) {
            return ResultadoImpactoBolaFuego.sinImpacto();
        }

        int vidaAntes = enemigo.getVidaActual();
        enemigo.recibirDano(dano);
        int danoReal = vidaAntes - enemigo.getVidaActual();
        estadisticas.registrarDanoEjercido(danoReal);
        boolean eraBoss = enemigo instanceof Boss;
        boolean murio = !enemigo.estaVivo();

        registrarLog("Tu Bola de Fuego impacta al enemigo haciendo " + danoReal + " de daño");
        if (murio) {
            obtenerOCrearContenidoActual().getEnemigos().del(enemigo);
            registrarLog(enemigo.getNombre() + " derrotado");
            if (eraBoss) {
                estadisticas.registrarBossMuerto();
                entregarLlaveFinal();
            } else {
                estadisticas.registrarEnemigoComunMuerto();
            }
        }

        comprobarVictoriaODerrota();
        return ResultadoImpactoBolaFuego.impacto(enemigo.getNombre(), danoReal, murio, eraBoss);
    }

    public ResultadoImpactoBolaHielo impactarBolaHielo(int fila, int columna) {
        if (estado != EstadoPartida.EN_CURSO) {
            return ResultadoImpactoBolaHielo.sinImpacto();
        }

        Enemigo enemigo = buscarEnemigoEn(fila, columna);
        if (enemigo == null || !enemigo.estaVivo()) {
            return ResultadoImpactoBolaHielo.sinImpacto();
        }

        enemigo.congelar(TURNOS_CONGELACION);
        registrarLog("Tu Bola de Hielo congela a " + enemigo.getNombre()
                + " durante " + TURNOS_CONGELACION + " turnos");
        return ResultadoImpactoBolaHielo.impacto(enemigo.getNombre(), TURNOS_CONGELACION);
    }

    @Override
    public boolean recogerObjeto(String idObjeto) {
        if (!puedeAceptarAccion() || accionRealizada || textoVacio(idObjeto)) {
            return false;
        }

        ContenidoCueva contenido = obtenerOCrearContenidoActual();
        ObjetoEnMapa objetoEnMapa = buscarObjetoEnSuelo(idObjeto, contenido);
        if (objetoEnMapa == null || !estaEnCeldaCercanaAlJugador(objetoEnMapa.getFila(), objetoEnMapa.getColumna())) {
            return false;
        }
        if (jugador.tieneObjetoConId(objetoEnMapa.getObjeto().getId())) {
            return false;
        }

        jugador.agregarObjeto(objetoEnMapa.getObjeto());
        contenido.getObjetosEnSuelo().del(objetoEnMapa);
        accionRealizada = true;
        registrarLog("Objeto recogido: " + objetoEnMapa.getObjeto().getNombre());
        comprobarVictoriaODerrota();
        return true;
    }

    @Override
    public boolean usarObjeto(String idObjeto) {
        if (!puedeAceptarAccion() || accionRealizada || textoVacio(idObjeto)) {
            return false;
        }

        Objeto objeto = buscarObjetoInventario(idObjeto);
        if (!(objeto instanceof Pocion)) {
            return false;
        }

        boolean usado = jugador.usarPocion((Pocion) objeto);
        if (usado) {
            accionRealizada = true;
            registrarLog("Objeto usado: " + objeto.getNombre());
            comprobarVictoriaODerrota();
        }
        return usado;
    }

    @Override
    public boolean equiparObjeto(String idObjeto) {
        if (!puedeAceptarAccion() || textoVacio(idObjeto)) {
            return false;
        }

        Objeto objeto = buscarObjetoInventario(idObjeto);
        boolean equipado = false;
        if (objeto instanceof Arma) {
            equipado = jugador.equiparArma((Arma) objeto);
        } else if (objeto instanceof Escudo) {
            equipado = jugador.equiparEscudo((Escudo) objeto);
        }

        if (equipado) {
            registrarLog("Objeto equipado: " + objeto.getNombre());
        }
        return equipado;
    }

    @Override
    public boolean avanzarACueva(String idCuevaDestino) {
        if (!puedeAceptarAccion() || textoVacio(idCuevaDestino)) {
            return false;
        }
        if (!jugadorEstaSobreTipo(TipoCelda.PUERTA)) {
            return false;
        }

        Cueva origen = getCuevaActualInterna();
        Cueva destino = mazmorra.getCuevaPorId(idCuevaDestino);
        Puerta puerta = buscarPuerta(origen, destino);
        Llave llave = puerta != null ? buscarLlavePorCodigo(puerta.getCodigoLlave()) : null;
        if (puerta == null || llave == null) {
            return false;
        }
        Celda celdaEntrada = buscarCeldaEntradaLibre(destino);
        if (celdaEntrada == null) {
            return false;
        }

        puerta.abrir();
        jugador.quitarObjeto(llave);
        boolean avanzado = mazmorra.avanzarACueva(destino);
        if (avanzado) {
            jugador.cambiarPosicion(celdaEntrada.getFila(), celdaEntrada.getColumna());
            turnosRestantes = TURNOS_POR_CUEVA;
            visionCaminoComprada = false;
            caminoComprado = new ListaSE<>();
            caminoCompradoCuevas = new ListaSE<>();
            registrarLog("Jugador avanza a la cueva " + destino.getId());
        }
        return avanzado;
    }

    @Override
    public boolean tieneLlaveFinal() {
        return buscarLlavePorCodigo(codigoLlaveFinal) != null;
    }

    @Override
    public boolean pasarTurno() {
        if (estado != EstadoPartida.EN_CURSO) {
            return false;
        }

        actuarEnemigos();
        estadisticas.registrarTurnoJugado();
        turnosRestantes--;
        movimientoRealizado = false;
        accionRealizada = false;
        registrarLog("Turno finalizado. Turnos restantes: " + turnosRestantes);
        comprobarVictoriaODerrota();
        return true;
    }

    private boolean puedeAceptarAccion() {
        comprobarVictoriaODerrota();
        return estado == EstadoPartida.EN_CURSO;
    }

    private void cargarPuertasIniciales(ListaSE<Puerta> puertasIniciales) {
        if (puertasIniciales == null) {
            return;
        }
        for (int indice = 0; indice < puertasIniciales.getSize(); indice++) {
            Puerta puerta = puertasIniciales.get(indice);
            if (!registrarPuertaInicial(puerta)) {
                throw new IllegalArgumentException("Puerta inicial no valida");
            }
        }
    }

    private void actuarEnemigos() {
        ContenidoCueva contenido = obtenerOCrearContenidoActual();
        for (int indice = 0; indice < contenido.getEnemigos().getSize(); indice++) {
            Enemigo enemigo = contenido.getEnemigos().get(indice);
            if (enemigo != null && enemigo.estaVivo()) {
                actuarEnemigo(enemigo);
                if (!jugador.estaVivo()) {
                    return;
                }
            }
        }
    }

    private void actuarEnemigo(Enemigo enemigo) {
        if (enemigo.estaCongelado()) {
            enemigo.consumirTurnoCongelado();
            registrarLog(enemigo.getNombre() + " está congelado y no puede actuar");
            return;
        }

        if (estaEnCeldaCercana(enemigo.getFila(), enemigo.getColumna(), jugador.getFila(), jugador.getColumna())) {
            int dano = calcularDano(enemigo.getAtaqueBase(), jugador.getDefensaTotal());
            int vidaAntes = jugador.getVidaActual();
            jugador.recibirDano(dano);
            estadisticas.registrarDanoRecibido(vidaAntes - jugador.getVidaActual());
            registrarLog(enemigo.getNombre() + " ataca al jugador e inflige " + dano + " de daño");
            return;
        }

        if (enemigo.getTipoEnemigo() == TipoEnemigo.ARQUERO && arqueroPuedeDisparar(enemigo)) {
            int dano = calcularDano(enemigo.getAtaqueBase(), jugador.getDefensaTotal());
            int vidaAntes = jugador.getVidaActual();
            jugador.recibirDano(dano);
            int danoReal = vidaAntes - jugador.getVidaActual();
            estadisticas.registrarDanoRecibido(danoReal);
            disparosEnemigosPendientes.addLast(new DisparoEnemigo(
                    enemigo.getFila(), enemigo.getColumna(), jugador.getFila(), jugador.getColumna(),
                    danoReal, enemigo.getNombre()));
            registrarLog(enemigo.getNombre() + " dispara al jugador e inflige " + dano + " de daño");
            return;
        }

        Cueva cuevaActual = getCuevaActualInterna();
        ListaSE<Celda> camino = cuevaActual.getCaminoMinimo(
                enemigo.getFila(),
                enemigo.getColumna(),
                jugador.getFila(),
                jugador.getColumna());

        if (camino.getSize() > 1) {
            Celda siguiente = camino.get(1);
            if (jugadorEstaEn(siguiente.getFila(), siguiente.getColumna())
                    || hayOtroEnemigoEn(enemigo, cuevaActual, siguiente.getFila(), siguiente.getColumna())) {
                return;
            }
            enemigo.cambiarPosicion(siguiente.getFila(), siguiente.getColumna());
            registrarLog(enemigo.getNombre() + " se acerca al jugador");
        }
    }

    private boolean arqueroPuedeDisparar(Enemigo enemigo) {
        int filaEnemigo = enemigo.getFila();
        int columnaEnemigo = enemigo.getColumna();
        int filaJugador = jugador.getFila();
        int columnaJugador = jugador.getColumna();
        boolean mismaFila = filaEnemigo == filaJugador;
        boolean mismaColumna = columnaEnemigo == columnaJugador;
        if (!mismaFila && !mismaColumna) {
            return false;
        }
        int distancia = Math.abs(filaEnemigo - filaJugador) + Math.abs(columnaEnemigo - columnaJugador);
        if (distancia <= 1 || distancia > ALCANCE_ARQUERO) {
            return false;
        }

        int df = Integer.compare(filaJugador, filaEnemigo);
        int dc = Integer.compare(columnaJugador, columnaEnemigo);
        Cueva cueva = getCuevaActualInterna();
        int fila = filaEnemigo + df;
        int columna = columnaEnemigo + dc;
        while (fila != filaJugador || columna != columnaJugador) {
            if (esBloqueanteLineaDisparo(cueva, fila, columna)) {
                return false;
            }
            fila += df;
            columna += dc;
        }
        return true;
    }

    private boolean esBloqueanteLineaDisparo(Cueva cueva, int fila, int columna) {
        if (cueva == null || !cueva.estaDentro(fila, columna)) {
            return true;
        }
        TipoCelda tipo = cueva.getCelda(fila, columna).getTipo();
        return tipo == TipoCelda.MURO || tipo == TipoCelda.ROCA
                || tipo == TipoCelda.ARBUSTO || tipo == TipoCelda.TESORO;
    }

    private boolean jugadorPuedeAtacar(Enemigo enemigo) {
        if (estaEnCeldaCercanaAlJugador(enemigo.getFila(), enemigo.getColumna())) {
            return true;
        }
        if (jugador.getArmaEquipada() instanceof Arco) {
            int distancia = getCuevaActualInterna().getDistanciaMinima(
                    jugador.getFila(),
                    jugador.getColumna(),
                    enemigo.getFila(),
                    enemigo.getColumna());
            return distancia > 0 && distancia <= ALCANCE_ARCO;
        }
        return false;
    }

    private PersonajeEnMapa crearVistaJugador() {
        return new PersonajeEnMapa(
                jugador.getNombre(),
                "JUGADOR",
                jugador.getVidaActual(),
                jugador.getVidaMaxima(),
                jugador.getFila(),
                jugador.getColumna());
    }

    private PersonajeEnMapa crearVistaEnemigo(Enemigo enemigo) {
        return new PersonajeEnMapa(
                enemigo.getNombre(),
                enemigo.getTipoEnemigo().name(),
                enemigo.getVidaActual(),
                enemigo.getVidaMaxima(),
                enemigo.getFila(),
                enemigo.getColumna());
    }

    private CuevaEnMapa crearVistaCueva(Cueva cueva) {
        ListaSE<CeldaEnMapa> celdas = new ListaSE<>();
        if (cueva != null) {
            for (int fila = 0; fila < cueva.getFilas(); fila++) {
                for (int columna = 0; columna < cueva.getColumnas(); columna++) {
                    celdas.addLast(crearVistaCelda(cueva.getCelda(fila, columna)));
                }
            }
            return new CuevaEnMapa(cueva.getId(), cueva.getFilas(), cueva.getColumnas(), celdas);
        }
        throw new IllegalStateException("La partida no tiene cueva actual");
    }

    private ListaSE<CeldaEnMapa> crearVistasCeldas(ListaSE<Celda> celdas) {
        ListaSE<CeldaEnMapa> vistas = new ListaSE<>();
        for (int indice = 0; indice < celdas.getSize(); indice++) {
            vistas.addLast(crearVistaCelda(celdas.get(indice)));
        }
        return vistas;
    }

    private CeldaEnMapa crearVistaCelda(Celda celda) {
        return new CeldaEnMapa(celda.getFila(), celda.getColumna(), celda.getTipo());
    }

    private Celda buscarCeldaEntradaLibre(Cueva cueva) {
        Celda inicio = buscarCeldaLibrePorTipo(cueva, TipoCelda.INICIO);
        if (inicio != null) {
            return inicio;
        }

        Celda puerta = buscarCeldaLibrePorTipo(cueva, TipoCelda.PUERTA);
        if (puerta != null) {
            return puerta;
        }

        for (int fila = 0; fila < cueva.getFilas(); fila++) {
            for (int columna = 0; columna < cueva.getColumnas(); columna++) {
                if (cueva.esTransitable(fila, columna) && !hayEnemigoEn(cueva, fila, columna)) {
                    return cueva.getCelda(fila, columna);
                }
            }
        }
        return null;
    }

    private Celda buscarCeldaLibrePorTipo(Cueva cueva, TipoCelda tipo) {
        for (int fila = 0; fila < cueva.getFilas(); fila++) {
            for (int columna = 0; columna < cueva.getColumnas(); columna++) {
                Celda celda = cueva.getCelda(fila, columna);
                if (celda.getTipo() == tipo && celda.esTransitable()
                        && !hayEnemigoEn(cueva, fila, columna)) {
                    return celda;
                }
            }
        }
        return null;
    }

    private int calcularDano(int ataque, int defensa) {
        int dano = ataque - defensa;
        if (dano < 1) {
            return 1;
        }
        return dano;
    }

    private Enemigo buscarEnemigoEn(int fila, int columna) {
        ListaSE<Enemigo> enemigos = obtenerOCrearContenidoActual().getEnemigos();
        for (int indice = 0; indice < enemigos.getSize(); indice++) {
            Enemigo enemigo = enemigos.get(indice);
            if (enemigo.getFila() == fila && enemigo.getColumna() == columna) {
                return enemigo;
            }
        }
        return null;
    }

    private boolean hayEnemigoEn(Cueva cueva, int fila, int columna) {
        ContenidoCueva contenido = buscarContenido(cueva);
        if (contenido == null) {
            return false;
        }
        ListaSE<Enemigo> enemigos = contenido.getEnemigos();
        for (int indice = 0; indice < enemigos.getSize(); indice++) {
            Enemigo enemigo = enemigos.get(indice);
            if (enemigo != null && enemigo.estaVivo()
                    && enemigo.getFila() == fila && enemigo.getColumna() == columna) {
                return true;
            }
        }
        return false;
    }

    private boolean hayOtroEnemigoEn(Enemigo enemigoActual, Cueva cueva, int fila, int columna) {
        ContenidoCueva contenido = buscarContenido(cueva);
        if (contenido == null) {
            return false;
        }
        ListaSE<Enemigo> enemigos = contenido.getEnemigos();
        for (int indice = 0; indice < enemigos.getSize(); indice++) {
            Enemigo enemigo = enemigos.get(indice);
            if (enemigo != null && enemigo != enemigoActual && enemigo.estaVivo()
                    && enemigo.getFila() == fila && enemigo.getColumna() == columna) {
                return true;
            }
        }
        return false;
    }

    private boolean jugadorEstaEn(int fila, int columna) {
        return jugador.getFila() == fila && jugador.getColumna() == columna;
    }

    private ObjetoEnMapa buscarObjetoEnSuelo(String idObjeto, ContenidoCueva contenido) {
        ListaSE<ObjetoEnMapa> objetos = contenido.getObjetosEnSuelo();
        for (int indice = 0; indice < objetos.getSize(); indice++) {
            ObjetoEnMapa objetoEnMapa = objetos.get(indice);
            if (objetoEnMapa.getObjeto().getId().equals(idObjeto)) {
                return objetoEnMapa;
            }
        }
        return null;
    }

    private Objeto buscarObjetoInventario(String idObjeto) {
        ListaDE<Objeto> inventario = jugador.getInventario();
        for (int indice = 0; indice < inventario.getSize(); indice++) {
            Objeto objeto = inventario.get(indice);
            if (objeto.getId().equals(idObjeto)) {
                return objeto;
            }
        }
        return null;
    }

    private Llave buscarLlavePorCodigo(String codigoLlave) {
        ListaDE<Objeto> inventario = jugador.getInventario();
        for (int indice = 0; indice < inventario.getSize(); indice++) {
            Objeto objeto = inventario.get(indice);
            if (objeto instanceof Llave) {
                Llave llave = (Llave) objeto;
                if (llave.getTipoLlave() == TipoLlave.PUERTA && llave.getCodigoCerradura().equals(codigoLlave)) {
                    return llave;
                }
            }
        }
        return null;
    }

    private Puerta buscarPuerta(Cueva origen, Cueva destino) {
        for (int indice = 0; indice < puertas.getSize(); indice++) {
            Puerta puerta = puertas.get(indice);
            if (puerta.conecta(origen, destino)) {
                return puerta;
            }
        }
        return null;
    }

    private boolean tieneLlaveParaPuerta(Puerta puerta) {
        return buscarLlavePorCodigo(puerta.getCodigoLlave()) != null;
    }

    private void entregarLlaveFinal() {
        if (!tieneLlaveFinal()) {
            jugador.agregarObjeto(new Llave(generarIdLlaveFinal(), TipoLlave.PUERTA, codigoLlaveFinal));
            registrarLog("Llave final conseguida");
        }
    }

    private String generarIdLlaveFinal() {
        if (!jugador.tieneObjetoConId(ID_LLAVE_FINAL_DEFECTO)) {
            return ID_LLAVE_FINAL_DEFECTO;
        }

        int contador = 2;
        String idCandidato = ID_LLAVE_FINAL_DEFECTO + "-" + contador;
        while (jugador.tieneObjetoConId(idCandidato)) {
            contador++;
            idCandidato = ID_LLAVE_FINAL_DEFECTO + "-" + contador;
        }
        return idCandidato;
    }

    private void comprobarVictoriaODerrota() {
        if (estado == EstadoPartida.VICTORIA) {
            return;
        }
        if (tieneLlaveFinal() && jugadorEstaSobreTipo(TipoCelda.SALIDA)) {
            estado = EstadoPartida.VICTORIA;
            registrarLog("Victoria: el jugador sale por la puerta con la llave final");
        } else if (!jugador.estaVivo() || turnosRestantes <= 0) {
            estado = EstadoPartida.DERROTA;
            registrarLog("Derrota");
        }
    }

    private ContenidoCueva obtenerOCrearContenidoActual() {
        Cueva cuevaActual = getCuevaActualInterna();
        if (cuevaActual == null) {
            throw new IllegalStateException("La partida no tiene cueva actual");
        }
        return obtenerOCrearContenido(cuevaActual);
    }

    private ContenidoCueva obtenerOCrearContenido(Cueva cueva) {
        /*
         * Capa temporal de contenidos por cueva.
         *
         * Esta estructura interna permite tener enemigos y objetos separados
         * por nivel sin modificar Cueva todavia. Si mas adelante el grupo
         * decide que Cueva debe contener directamente sus enemigos/objetos,
         * este metodo sera el punto natural para migrar esa responsabilidad.
         */
        ContenidoCueva contenido = buscarContenido(cueva);
        if (contenido == null) {
            contenido = new ContenidoCueva(cueva);
            contenidosPorCueva.addLast(contenido);
        }
        return contenido;
    }

    private ContenidoCueva buscarContenido(Cueva cueva) {
        for (int indice = 0; indice < contenidosPorCueva.getSize(); indice++) {
            ContenidoCueva contenido = contenidosPorCueva.get(indice);
            if (contenido.getCueva().equals(cueva)) {
                return contenido;
            }
        }
        return null;
    }

    public boolean hayEnemigoEn(int fila, int columna) {
        Cueva cueva = getCuevaActualInterna();
        if (cueva == null) return false;
        return hayEnemigoEn(cueva, fila, columna);
    }

    @Override
    public boolean hayEnemigoEnDireccion(int df, int dc) {
        return hayEnemigoEn(jugador.getFila() + df, jugador.getColumna() + dc);
    }

    public boolean hayObjetoEnPosicion() {
        return getObjetoEn(jugador.getFila(), jugador.getColumna()) != null;
    }

    public ObjetoEnMapa getObjetoEn(int fila, int columna) {
        ContenidoCueva contenido = obtenerOCrearContenidoActual();
        ListaSE<ObjetoEnMapa> objetos = contenido.getObjetosEnSuelo();
        for (int i = 0; i < objetos.getSize(); i++) {
            ObjetoEnMapa o = objetos.get(i);
            if (o.getFila() == fila && o.getColumna() == columna) return o;
        }
        return null;
    }

    public Enemigo getEnemigoAdyacente() {
        ContenidoCueva contenido = obtenerOCrearContenidoActual();
        int pf = jugador.getFila(), pc = jugador.getColumna();
        ListaSE<Enemigo> enemigos = contenido.getEnemigos();
        for (int i = 0; i < enemigos.getSize(); i++) {
            Enemigo e = enemigos.get(i);
            if (!e.estaVivo()) continue;
            if (Math.abs(e.getFila() - pf) + Math.abs(e.getColumna() - pc) == 1) return e;
        }
        return null;
    }

    public static Partida crearPartidaNueva() throws IOException {
        return crearPartidaNueva("Jugador");
    }

    public static Partida crearPartidaNueva(String nombreJugador) throws IOException {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar("datos/cuevas.json");
        FabricaPartida fabrica = new FabricaPartida();
        return fabrica.crearPartida(resultado, nombreJugador);
    }

    public static Partida cargarPartida(String ruta) throws IOException {
        DatosPartidaDTO dto = SerializadorPartida.cargar(ruta);
        Mazmorra mazmorra = SerializadorPartida.dtoAMazmorra(dto.getMazmorra());
        Jugador jugador = SerializadorPartida.dtoAJugador(dto.getJugador());
        int turnosRestantes = dto.getTurnosRestantes();
        ListaSE<Puerta> puertas = new ListaSE<>();
        if (dto.getMazmorra().getConexiones() != null) {
            for (ConexionDTO conexion : dto.getMazmorra().getConexiones()) {
                Cueva origen = mazmorra.getCuevaPorId(conexion.getOrigen());
                Cueva destino = mazmorra.getCuevaPorId(conexion.getDestino());
                if (origen != null && destino != null && mazmorra.existeConexion(origen, destino)) {
                    String idPuerta = conexion.getEtiqueta();
                    if (idPuerta == null || idPuerta.trim().isEmpty()) {
                        idPuerta = "puerta-" + origen.getId() + "-" + destino.getId();
                    }
                    puertas.addLast(new Puerta(idPuerta, origen, destino, "llave-" + destino.getId()));
                }
            }
        }
        Partida partida = new Partida(mazmorra, jugador, turnosRestantes,
                CODIGO_LLAVE_FINAL_DEFECTO, puertas, dto.getEstadisticas());
        partida.estado = EstadoPartida.valueOf(dto.getEstado());

        if (dto.getMazmorra().getCuevas() != null) {
            for (DatosCuevaDTO cuevaDTO : dto.getMazmorra().getCuevas()) {
                Cueva cueva = mazmorra.getCuevaPorId(cuevaDTO.getId());
                if (cueva == null) continue;
                ContenidoCueva contenido = new ContenidoCueva(cueva);

                if (cuevaDTO.getEnemigos() != null) {
                    for (DatosEnemigoDTO eDTO : cuevaDTO.getEnemigos()) {
                        Enemigo enemigo;
                        if ("BOSS".equals(eDTO.getTipo())) {
                            enemigo = new Boss(eDTO.getNombre(),
                                eDTO.getVidaMaxima(), eDTO.getAtaqueBase(),
                                eDTO.getDefensaBase(), eDTO.getMovimiento(),
                                eDTO.getFila(), eDTO.getColumna());
                        } else {
                            enemigo = new Enemigo(eDTO.getNombre(),
                                TipoEnemigo.valueOf(eDTO.getTipo()),
                                eDTO.getVidaMaxima(), eDTO.getAtaqueBase(),
                                eDTO.getDefensaBase(), eDTO.getMovimiento(),
                                eDTO.getFila(), eDTO.getColumna());
                        }
                        int danoRecibido = enemigo.getVidaMaxima() - eDTO.getVidaActual();
                        if (danoRecibido > 0) {
                            enemigo.recibirDano(danoRecibido);
                        }
                        if (!eDTO.isVivo() && enemigo.estaVivo()) {
                            enemigo.recibirDano(enemigo.getVidaMaxima());
                        }
                        enemigo.congelar(eDTO.getTurnosCongelado());
                        contenido.agregarEnemigo(enemigo);
                    }
                }

                if (cuevaDTO.getObjetos() != null) {
                    for (DatosObjetoDTO oDTO : cuevaDTO.getObjetos()) {
                        Objeto obj = reconstruirObjeto(oDTO);
                        if (obj != null) {
                            contenido.agregarObjeto(new ObjetoEnMapa(obj, cueva,
                                oDTO.getFila(), oDTO.getColumna()));
                        }
                    }
                }

                partida.contenidosPorCueva.addLast(contenido);
            }
        }

        return partida;
    }

    private static Objeto reconstruirObjeto(DatosObjetoDTO oDTO) {
        switch (oDTO.getTipo()) {
            case "POCION":
                return new Pocion(oDTO.getId(), oDTO.getCura());
            case "ARMA":
                if (oDTO.getBonificacionAtaque() >= Espada.ATAQUE_EXTRA) {
                    return new Espada(oDTO.getId());
                } else {
                    return new Arco(oDTO.getId());
                }
            case "ESCUDO":
                return new Escudo(oDTO.getId());
            case "LLAVE":
                TipoLlave tipo = TipoLlave.valueOf(oDTO.getTipoLlave());
                String codigo = oDTO.getCodigoCerradura();
                if (codigo == null || codigo.trim().isEmpty()) {
                    codigo = "llave-" + oDTO.getId();
                }
                return new Llave(oDTO.getId(), tipo, codigo);
            default:
                return null;
        }
    }

    public boolean moverJugadorArriba() {
        return moverJugador(jugador.getFila() - 1, jugador.getColumna());
    }

    public boolean moverJugadorAbajo() {
        return moverJugador(jugador.getFila() + 1, jugador.getColumna());
    }

    public boolean moverJugadorIzquierda() {
        return moverJugador(jugador.getFila(), jugador.getColumna() - 1);
    }

    public boolean moverJugadorDerecha() {
        return moverJugador(jugador.getFila(), jugador.getColumna() + 1);
    }

    public boolean atacar() {
        Enemigo enemigo = getEnemigoAdyacente();
        if (enemigo == null) return false;
        return atacar(enemigo.getFila(), enemigo.getColumna());
    }

    public boolean recogerObjeto() {
        int f = jugador.getFila();
        int c = jugador.getColumna();
        ObjetoEnMapa objeto = getObjetoEn(f, c);
        if (objeto == null) return false;
        return recogerObjeto(objeto.getObjeto().getId());
    }

    @Override
    public boolean abrirTesoro() {
        if (!puedeAceptarAccion() || accionRealizada) {
            return false;
        }

        Cueva cueva = getCuevaActualInterna();
        Celda tesoro = buscarTesoroAbrible(cueva);
        if (tesoro == null) {
            return false;
        }

        cueva.cambiarTipoCelda(tesoro.getFila(), tesoro.getColumna(), TipoCelda.SUELO);
        Objeto recompensa = crearRecompensaTesoro(cueva.getId(), tesoro.getFila(), tesoro.getColumna());
        jugador.agregarObjeto(recompensa);
        accionRealizada = true;
        registrarLog("Cofre abierto: obtienes " + recompensa.getNombre());
        return true;
    }

    @Override
    public boolean hayTesoroCercano() {
        return buscarTesoroAbrible(getCuevaActualInterna()) != null;
    }

    private Objeto crearRecompensaTesoro(String idCueva, int fila, int columna) {
        String sufijo = idCueva + "-" + fila + "-" + columna;
        if ("cueva_media".equals(idCueva)) {
            return new Escudo("cofre-escudo-" + sufijo);
        }
        if ("cueva_dificil".equals(idCueva)) {
            return new Espada("cofre-espada-" + sufijo);
        }
        return new Pocion("cofre-pocion-" + sufijo);
    }

    public boolean terminarTurno() {
        return pasarTurno();
    }

    public Jugador getJugador() {
        return jugador;
    }

    public ListaDE<Enemigo> getEnemigosActuales() {
        ListaDE<Enemigo> vivos = new ListaDE<>();
        ContenidoCueva contenido = obtenerOCrearContenidoActual();
        ListaSE<Enemigo> enemigos = contenido.getEnemigos();
        for (int i = 0; i < enemigos.getSize(); i++) {
            Enemigo e = enemigos.get(i);
            if (e != null && e.estaVivo()) {
                vivos.addLast(e);
            }
        }
        return vivos;
    }

    public ListaSE<DisparoEnemigo> consumirDisparosEnemigosPendientes() {
        ListaSE<DisparoEnemigo> copia = disparosEnemigosPendientes.copy();
        disparosEnemigosPendientes.clear();
        return copia;
    }

    public ListaDE<ObjetoEnMapa> getObjetosActuales() {
        ListaDE<ObjetoEnMapa> objetos = new ListaDE<>();
        ContenidoCueva contenido = obtenerOCrearContenidoActual();
        ListaSE<ObjetoEnMapa> enSuelo = contenido.getObjetosEnSuelo();
        for (int i = 0; i < enSuelo.getSize(); i++) {
            objetos.addLast(enSuelo.get(i));
        }
        return objetos;
    }

    public ListaSE<String> getMensajes() {
        return log;
    }

    public boolean usarPocion(String id) {
        return usarObjeto(id);
    }

    public boolean equiparItem(String id) {
        return equiparObjeto(id);
    }

    public boolean cambiarCueva() {
        if (!puedeCambiarCueva()) {
            return false;
        }
        Cueva destino = getSiguienteCuevaInterna();
        return avanzarACueva(destino.getId());
    }

    public boolean puedeCambiarCueva() {
        if (!puedeAceptarAccion() || !jugadorEstaSobreTipo(TipoCelda.PUERTA)) {
            return false;
        }
        Cueva actual = getCuevaActualInterna();
        Cueva destino = getSiguienteCuevaInterna();
        if (actual == null || destino == null) {
            return false;
        }
        Puerta puerta = buscarPuerta(actual, destino);
        return puerta != null && tieneLlaveParaPuerta(puerta);
    }

    /**
     * Devuelve el id de la siguiente cueva conectada sin transicionar.
     *
     * Permite que la interfaz muestre una pantalla de transicion antes
     * de llamar a cambiarCueva(). Si no hay siguiente cueva (porque es
     * la ultima o no hay conexion), devuelve null.
     */
    public String getSiguienteCuevaId() {
        Cueva siguiente = getSiguienteCuevaInterna();
        return siguiente != null ? siguiente.getId() : null;
    }

    public void guardar(String ruta) throws IOException {
        DatosMazmorraDTO mazmorraDTO = SerializadorPartida.mazmorraADTO(mazmorra);
        DatosCuevaDTO[] cuevasDTO = mazmorraDTO.getCuevas();
        for (int i = 0; i < cuevasDTO.length; i++) {
            Cueva cueva = mazmorra.getCuevaPorId(cuevasDTO[i].getId());
            if (cueva == null) continue;
            ContenidoCueva contenido = buscarContenido(cueva);
            if (contenido == null) continue;

            ListaSE<Enemigo> enemigos = contenido.getEnemigos();
            DatosEnemigoDTO[] enemigosDTO = new DatosEnemigoDTO[enemigos.getSize()];
            for (int j = 0; j < enemigos.getSize(); j++) {
                Enemigo e = enemigos.get(j);
                enemigosDTO[j] = new DatosEnemigoDTO(
                    e.getTipoEnemigo().name(), e.getNombre(),
                    e.getVidaActual(), e.getVidaMaxima(),
                    e.getAtaqueBase(), e.getDefensaBase(),
                    e.getMovimiento(), e.getFila(), e.getColumna(),
                    e.estaVivo(), e.getTurnosCongelado());
            }
            cuevasDTO[i].setEnemigos(enemigosDTO);

            ListaSE<ObjetoEnMapa> objetos = contenido.getObjetosEnSuelo();
            DatosObjetoDTO[] objetosDTO = new DatosObjetoDTO[objetos.getSize()];
            for (int j = 0; j < objetos.getSize(); j++) {
                ObjetoEnMapa o = objetos.get(j);
                Objeto obj = o.getObjeto();
                String tipoObjeto;
                if (obj instanceof Pocion) tipoObjeto = "POCION";
                else if (obj instanceof Arma) tipoObjeto = "ARMA";
                else if (obj instanceof Escudo) tipoObjeto = "ESCUDO";
                else if (obj instanceof Llave) tipoObjeto = "LLAVE";
                else tipoObjeto = "DESCONOCIDO";
                int cura = (obj instanceof Pocion) ? ((Pocion) obj).getPuntosCuracion() : 0;
                int bonifAtk = (obj instanceof Arma) ? ((Arma) obj).getBonificacionAtaque() : 0;
                int bonifDef = (obj instanceof Escudo) ? ((Escudo) obj).getBonificacionDefensa() : 0;
                String tipoLlave = (obj instanceof Llave) ? ((Llave) obj).getTipoLlave().name() : null;
                String codCerradura = (obj instanceof Llave) ? ((Llave) obj).getCodigoCerradura() : null;
                objetosDTO[j] = new DatosObjetoDTO(
                    obj.getId(), tipoObjeto, obj.getNombre(),
                    obj.getDescripcion(), o.getFila(), o.getColumna(),
                    cura, bonifAtk, bonifDef, tipoLlave, codCerradura);
            }
            cuevasDTO[i].setObjetos(objetosDTO);
        }

        DatosPuertaDTO[] puertasDTO = new DatosPuertaDTO[puertas.getSize()];
        for (int i = 0; i < puertas.getSize(); i++) {
            Puerta p = puertas.get(i);
            puertasDTO[i] = new DatosPuertaDTO(
                p.getId(), p.getOrigen().getId(),
                p.getDestino().getId(), p.getCodigoLlave(),
                p.isAbierta());
        }

        DatosJugadorDTO jugadorDTO = SerializadorPartida.jugadorADTO(jugador);
        DatosPartidaDTO dto = new DatosPartidaDTO(
            SerializadorPartida.getVersionActual(),
            mazmorraDTO, jugadorDTO, estado.name(), turnosRestantes,
            puertasDTO, estadisticas);
        SerializadorPartida.guardar(dto, ruta);
    }

    public EstadisticasPartida getEstadisticas() {
        return new EstadisticasPartida(estadisticas);
    }

    // ---------------------------------------------------------------
    // Vision del camino (C-10)
    // ---------------------------------------------------------------

    @Override
    public int getDistanciaAPuerta() {
        Cueva cueva = getCuevaActualInterna();
        if (cueva == null) {
            return -1;
        }
        Jugador j = getJugador();
        int mejorDist = Integer.MAX_VALUE;
        for (int f = 0; f < cueva.getFilas(); f++) {
            for (int c = 0; c < cueva.getColumnas(); c++) {
                if (cueva.getCelda(f, c).getTipo() == TipoCelda.PUERTA) {
                    int d = cueva.getDistanciaMinima(j.getFila(), j.getColumna(), f, c);
                    if (d >= 0 && d < mejorDist) {
                        mejorDist = d;
                    }
                }
            }
        }
        return mejorDist == Integer.MAX_VALUE ? -1 : mejorDist;
    }

    @Override
    public int getDistanciaMinimaCuevasASalida() {
        Cueva actual = getCuevaActualInterna();
        if (actual == null) {
            return -1;
        }
        Cueva cuevaSalida = buscarCuevaConSalida();
        if (cuevaSalida == null) {
            return -1;
        }
        if (actual.equals(cuevaSalida)) {
            return 0;
        }
        ListaSE<Cueva> camino = mazmorra.getCaminoMinimo(actual, cuevaSalida);
        if (camino == null || camino.isEmpty()) {
            return -1;
        }
        return camino.getSize() - 1;
    }

    @Override
    public boolean comprarVisionCamino() {
        if (visionCaminoComprada) {
            return false;
        }
        if (turnosRestantes < 5) {
            return false;
        }
        turnosRestantes -= 5;

        Cueva cueva = getCuevaActualInterna();
        Jugador j = getJugador();
        Cueva cuevaSalida = buscarCuevaConSalida();

        // Calcular cadena de cuevas hasta la SALIDA
        caminoCompradoCuevas = new ListaSE<>();
        if (cuevaSalida != null && !cueva.equals(cuevaSalida)) {
            ListaSE<Cueva> rutaCuevas = mazmorra.getCaminoMinimo(cueva, cuevaSalida);
            if (rutaCuevas != null) {
                for (int i = 0; i < rutaCuevas.getSize(); i++) {
                    caminoCompradoCuevas.addLast(rutaCuevas.get(i).getId());
                }
            }
        } else if (cuevaSalida != null) {
            caminoCompradoCuevas.addLast(cueva.getId());
        }

        // Calcular camino en la cueva actual hacia la salida
        boolean esCuevaSalida = cuevaSalida != null && cueva.equals(cuevaSalida);

        if (esCuevaSalida) {
            // En la cueva con SALIDA — camino directo a la celda SALIDA
            int salidaF = -1;
            int salidaC = -1;
            for (int f = 0; f < cueva.getFilas() && salidaF < 0; f++) {
                for (int c = 0; c < cueva.getColumnas() && salidaF < 0; c++) {
                    if (cueva.getCelda(f, c).getTipo() == TipoCelda.SALIDA) {
                        salidaF = f;
                        salidaC = c;
                    }
                }
            }
            if (salidaF >= 0) {
                caminoComprado = cueva.getCaminoMinimo(j.getFila(), j.getColumna(), salidaF, salidaC);
            } else {
                caminoComprado = new ListaSE<>();
            }
        } else {
            // Cueva normal — camino a la PUERTA mas cercana
            int mejorDist = Integer.MAX_VALUE;
            int mejorF = -1;
            int mejorC = -1;
            for (int f = 0; f < cueva.getFilas(); f++) {
                for (int c = 0; c < cueva.getColumnas(); c++) {
                    if (cueva.getCelda(f, c).getTipo() == TipoCelda.PUERTA) {
                        int d = cueva.getDistanciaMinima(j.getFila(), j.getColumna(), f, c);
                        if (d >= 0 && d < mejorDist) {
                            mejorDist = d;
                            mejorF = f;
                            mejorC = c;
                        }
                    }
                }
            }
            if (mejorF >= 0) {
                caminoComprado = cueva.getCaminoMinimo(j.getFila(), j.getColumna(), mejorF, mejorC);
            } else {
                caminoComprado = new ListaSE<>();
            }
        }

        visionCaminoComprada = true;
        registrarLog("Camino a la salida revelado (5 turnos)");
        return true;
    }

    @Override
    public boolean isVisionCaminoComprada() {
        return visionCaminoComprada;
    }

    @Override
    public ListaSE<CeldaEnMapa> getCaminoComprado() {
        if (!visionCaminoComprada || caminoComprado == null) {
            return new ListaSE<>();
        }
        return crearVistasCeldas(caminoComprado);
    }

    @Override
    public ListaSE<String> getCaminoCompradoCuevas() {
        if (!visionCaminoComprada || caminoCompradoCuevas == null) {
            return new ListaSE<>();
        }
        return caminoCompradoCuevas;
    }

    private Cueva buscarCuevaConSalida() {
        ListaSE<Cueva> todas = mazmorra.getCuevas();
        for (int i = 0; i < todas.getSize(); i++) {
            Cueva c = todas.get(i);
            for (int f = 0; f < c.getFilas(); f++) {
                for (int col = 0; col < c.getColumnas(); col++) {
                    if (c.getCelda(f, col).getTipo() == TipoCelda.SALIDA) {
                        return c;
                    }
                }
            }
        }
        return null;
    }

    // ---------------------------------------------------------------
    // Auxiliares privados
    // ---------------------------------------------------------------

    private boolean estaEnCeldaCercanaAlJugador(int fila, int columna) {
        return estaEnCeldaCercana(jugador.getFila(), jugador.getColumna(), fila, columna);
    }

    private boolean jugadorEstaSobreTipo(TipoCelda tipo) {
        Cueva cueva = getCuevaActualInterna();
        if (cueva == null || tipo == null || !cueva.estaDentro(jugador.getFila(), jugador.getColumna())) {
            return false;
        }
        return cueva.getCelda(jugador.getFila(), jugador.getColumna()).getTipo() == tipo;
    }

    private Celda buscarTesoroAbrible(Cueva cueva) {
        if (cueva == null) {
            return null;
        }
        int fila = jugador.getFila();
        int columna = jugador.getColumna();
        if (esTesoroEn(cueva, fila, columna)) {
            return cueva.getCelda(fila, columna);
        }
        if (esTesoroEn(cueva, fila - 1, columna)) {
            return cueva.getCelda(fila - 1, columna);
        }
        if (esTesoroEn(cueva, fila + 1, columna)) {
            return cueva.getCelda(fila + 1, columna);
        }
        if (esTesoroEn(cueva, fila, columna - 1)) {
            return cueva.getCelda(fila, columna - 1);
        }
        if (esTesoroEn(cueva, fila, columna + 1)) {
            return cueva.getCelda(fila, columna + 1);
        }
        return null;
    }

    private boolean esTesoroEn(Cueva cueva, int fila, int columna) {
        return cueva.estaDentro(fila, columna)
                && cueva.getCelda(fila, columna).getTipo() == TipoCelda.TESORO;
    }

    private Cueva getSiguienteCuevaInterna() {
        Cueva actual = getCuevaActualInterna();
        if (actual == null) {
            return null;
        }
        ListaSE<Cueva> siguientes = mazmorra.getCuevasSiguientes(actual);
        if (siguientes == null || siguientes.getSize() == 0) {
            return null;
        }
        return siguientes.get(0);
    }

    private boolean estaEnCeldaCercana(int filaA, int columnaA, int filaB, int columnaB) {
        return distanciaAbsoluta(filaA - filaB) <= 1 && distanciaAbsoluta(columnaA - columnaB) <= 1;
    }

    private int distanciaAbsoluta(int valor) {
        if (valor < 0) {
            return -valor;
        }
        return valor;
    }

    private void registrarLog(String mensaje) {
        log.addLast(mensaje);
    }

    private static boolean textoVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    private static void validarTexto(String texto, String campo) {
        if (textoVacio(texto)) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
    }

    private static class ContenidoCueva {
        /*
         * Registro interno, no parte del modelo final.
         *
         * Agrupa lo que "vive" en una cueva durante la partida. Esta clase
         * existe para no usar HashMap ni colecciones externas y para no tocar
         * aun la clase Cueva con responsabilidades de Parte B.
         */
        private final Cueva cueva;
        private final ListaSE<Enemigo> enemigos;
        private final ListaSE<ObjetoEnMapa> objetosEnSuelo;

        private ContenidoCueva(Cueva cueva) {
            this.cueva = cueva;
            this.enemigos = new ListaSE<>();
            this.objetosEnSuelo = new ListaSE<>();
        }

        private Cueva getCueva() {
            return cueva;
        }

        private ListaSE<Enemigo> getEnemigos() {
            return enemigos;
        }

        private ListaSE<ObjetoEnMapa> getObjetosEnSuelo() {
            return objetosEnSuelo;
        }

        private void agregarEnemigo(Enemigo enemigo) {
            enemigos.addLast(enemigo);
        }

        private void agregarObjeto(ObjetoEnMapa objeto) {
            objetosEnSuelo.addLast(objeto);
        }
    }

}
