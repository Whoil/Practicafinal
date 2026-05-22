package modelo.juego;

import Estructuras.ListaDE;
import Estructuras.ListaSE;
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
    public static final String CODIGO_LLAVE_FINAL_DEFECTO = "LLAVE_FINAL";
    public static final String ID_LLAVE_FINAL_DEFECTO = "llave-final";

    private final Mazmorra mazmorra;
    private final Jugador jugador;
    private final ListaSE<ContenidoCueva> contenidosPorCueva;
    private final ListaSE<Puerta> puertas;
    private final ListaSE<String> log;
    private final String codigoLlaveFinal;

    /*
     * Estos flags controlan la regla acordada de "un movimiento" y "una
     * accion" por turno. Aun asi, el turno no se cierra automaticamente:
     * solamente se resetean cuando el usuario llama a pasarTurno().
     */
    private int turnosRestantes;
    private EstadoPartida estado;
    private boolean movimientoRealizado;
    private boolean accionRealizada;

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

    @Override
    public ListaSE<CeldaEnMapa> getCeldasAlcanzablesJugador() {
        Cueva cuevaActual = getCuevaActualInterna();
        if (cuevaActual == null) {
            return new ListaSE<>();
        }
        return crearVistasCeldas(cuevaActual.getCeldasAlcanzables(
                jugador.getFila(),
                jugador.getColumna(),
                jugador.getMovimiento()));
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
        if (hayEnemigoEn(cuevaActual, fila, columna)) {
            return false;
        }

        CeldaEnMapa destino = crearVistaCelda(cuevaActual.getCelda(fila, columna));
        if (!getCeldasAlcanzablesJugador().existeDato(destino)) {
            return false;
        }

        jugador.cambiarPosicion(fila, columna);
        movimientoRealizado = true;
        registrarLog("Jugador movido a " + fila + ", " + columna);
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

        int dano = calcularDano(jugador.getAtaqueTotal(), enemigo.getDefensaBase());
        enemigo.recibirDano(dano);
        accionRealizada = true;
        registrarLog("Jugador ataca a " + enemigo.getNombre() + " e inflige " + dano + " de dano");

        if (!enemigo.estaVivo()) {
            obtenerOCrearContenidoActual().getEnemigos().del(enemigo);
            registrarLog(enemigo.getNombre() + " derrotado");
            if (enemigo instanceof Boss) {
                entregarLlaveFinal();
            }
        }

        comprobarVictoriaODerrota();
        return true;
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
        if (!puedeAceptarAccion() || accionRealizada || textoVacio(idObjeto)) {
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
            accionRealizada = true;
            registrarLog("Objeto equipado: " + objeto.getNombre());
        }
        return equipado;
    }

    @Override
    public boolean avanzarACueva(String idCuevaDestino) {
        if (!puedeAceptarAccion() || accionRealizada || textoVacio(idCuevaDestino)) {
            return false;
        }

        Cueva origen = getCuevaActualInterna();
        Cueva destino = mazmorra.getCuevaPorId(idCuevaDestino);
        Puerta puerta = buscarPuerta(origen, destino);
        if (puerta == null || !tieneLlaveParaPuerta(puerta)) {
            return false;
        }
        Celda celdaEntrada = buscarCeldaEntradaLibre(destino);
        if (celdaEntrada == null) {
            return false;
        }

        puerta.abrir();
        boolean avanzado = mazmorra.avanzarACueva(destino);
        if (avanzado) {
            jugador.cambiarPosicion(celdaEntrada.getFila(), celdaEntrada.getColumna());
            accionRealizada = true;
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
        if (estaEnCeldaCercana(enemigo.getFila(), enemigo.getColumna(), jugador.getFila(), jugador.getColumna())) {
            int dano = calcularDano(enemigo.getAtaqueBase(), jugador.getDefensaTotal());
            jugador.recibirDano(dano);
            registrarLog(enemigo.getNombre() + " ataca al jugador e inflige " + dano + " de dano");
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
        return puerta.isAbierta() || buscarLlavePorCodigo(puerta.getCodigoLlave()) != null;
    }

    private void entregarLlaveFinal() {
        if (!tieneLlaveFinal()) {
            jugador.agregarObjeto(new Llave(generarIdLlaveFinal(), TipoLlave.PUERTA, codigoLlaveFinal));
            registrarLog("Llave final conseguida");
        }
        estado = EstadoPartida.VICTORIA;
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
        if (tieneLlaveFinal()) {
            estado = EstadoPartida.VICTORIA;
            registrarLog("Victoria: el jugador tiene la llave final");
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

    private boolean estaEnCeldaCercanaAlJugador(int fila, int columna) {
        return estaEnCeldaCercana(jugador.getFila(), jugador.getColumna(), fila, columna);
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
    }
}
