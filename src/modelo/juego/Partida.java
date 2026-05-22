package modelo.juego;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

import Estructuras.ListaDE;
import Estructuras.ListaSE;
import Estructuras.MiIterador;
import json.ConfiguracionCuevaDTO;
import json.ConfiguracionEnemigoDTO;
import json.ConfiguracionMazmorra;
import json.ConfiguracionObjetoDTO;
import json.ConexionDTO;
import json.DatosPartidaDTO;
import json.SerializadorPartida;
import modelo.mapa.Celda;
import modelo.mapa.Cueva;
import modelo.mapa.TipoCelda;
import modelo.objetos.Arma;
import modelo.objetos.Arco;
import modelo.objetos.Escudo;
import modelo.objetos.Espada;
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
import modelo.personajes.TipoEnemigo;

/**
 * Partida — Estado completo de una partida jugable.
 *
 * Gestiona la mazmorra, el jugador, los enemigos y objetos de cada cueva,
 * los turnos, el combate basico, la recogida de objetos, la navegacion
 * entre cuevas y las condiciones de victoria/derrota.
 *
 * Metodo principal de creacion:
 *   Partida.crearPartidaNueva()   —  Carga desde datos/cuevas.json
 */
public class Partida {

    // ---------------------------------------------------------------
    // Datos por cueva
    // ---------------------------------------------------------------

    /**
     * Wrapper que asocia un objeto del mapa con su posicion (fila, columna).
     */
    public static class ObjetoEnMapa {
        private final Objeto objeto;
        private final int fila;
        private final int columna;
        ObjetoEnMapa(Objeto objeto, int fila, int columna) {
            this.objeto = objeto;
            this.fila = fila;
            this.columna = columna;
        }
        public Objeto getObjeto() { return objeto; }
        public int getFila() { return fila; }
        public int getColumna() { return columna; }
    }

    private static class CuevaData {
        final String cuevaId;
        final ListaDE<Enemigo> enemigos = new ListaDE<>();
        final ListaDE<ObjetoEnMapa> objetosMapa = new ListaDE<>();
        CuevaData(String id) { this.cuevaId = id; }
    }

    // ---------------------------------------------------------------
    // Estado de la partida
    // ---------------------------------------------------------------

    private Mazmorra mazmorra;
    private Jugador jugador;
    private int turnosRestantes;
    private String estado;
    private ListaSE<CuevaData> cuevasData;
    private ListaSE<String> mensajes;

    private Partida() { }

    // ---------------------------------------------------------------
    // Fabrica
    // ---------------------------------------------------------------

    /**
     * Crea una partida nueva cargando la configuracion desde datos/cuevas.json.
     */
    public static Partida crearPartidaNueva() throws IOException {
        Gson gson = new Gson();
        ConfiguracionMazmorra config;
        try (FileReader lector = new FileReader("datos/cuevas.json")) {
            config = gson.fromJson(lector, ConfiguracionMazmorra.class);
        }
        if (config == null || config.getCuevas() == null || config.getCuevas().length == 0) {
            throw new IllegalArgumentException("El JSON no contiene cuevas validas");
        }

        Partida p = new Partida();
        p.mazmorra = new Mazmorra();
        p.turnosRestantes = 40;
        p.estado = "EN_CURSO";
        p.mensajes = new ListaSE<>();
        p.cuevasData = new ListaSE<>();
        int contObj = 0;
        int contEnem = 0;

        for (ConfiguracionCuevaDTO cfg : config.getCuevas()) {
            Cueva cueva = new Cueva(cfg.getId(), cfg.getFilas(), cfg.getColumnas());
            String[][] matriz = cfg.getMatriz();
            for (int f = 0; f < cfg.getFilas(); f++) {
                for (int c = 0; c < cfg.getColumnas(); c++) {
                    try {
                        cueva.cambiarTipoCelda(f, c, TipoCelda.valueOf(matriz[f][c]));
                    } catch (IllegalArgumentException e) {
                        cueva.cambiarTipoCelda(f, c, TipoCelda.SUELO);
                    }
                }
            }
            p.mazmorra.addCueva(cueva);

            CuevaData data = new CuevaData(cueva.getId());

            // Enemigos
            if (cfg.getEnemigos() != null) {
                for (ConfiguracionEnemigoDTO ec : cfg.getEnemigos()) {
                    TipoEnemigo tipo = mapearTipo(ec.getTipo());
                    String nombre = tipo.name() + "_" + contEnem;
                    Enemigo enemigo;
                    if (tipo == TipoEnemigo.BOSS) {
                        enemigo = new Boss(nombre, ec.getVida(), ec.getAtaque(),
                                ec.getDefensa(), ec.getMovimiento(),
                                ec.getFila(), ec.getColumna());
                    } else {
                        enemigo = new Enemigo(nombre, tipo, ec.getVida(),
                                ec.getAtaque(), ec.getDefensa(),
                                ec.getMovimiento(),
                                ec.getFila(), ec.getColumna());
                    }
                    data.enemigos.addLast(enemigo);
                    contEnem++;
                }
            }

            // Objetos (guardamos con posicion)
            if (cfg.getObjetos() != null) {
                for (ConfiguracionObjetoDTO oc : cfg.getObjetos()) {
                    Objeto obj = crearObjeto(oc, "obj_" + contObj);
                    if (obj != null) {
                        data.objetosMapa.addLast(new ObjetoEnMapa(obj,
                                oc.getFila(), oc.getColumna()));
                        contObj++;
                    }
                }
            }

            p.cuevasData.addLast(data);
        }

        // Conexiones
        if (config.getConexiones() != null) {
            for (ConexionDTO conn : config.getConexiones()) {
                Cueva origen = p.mazmorra.getCuevaPorId(conn.getOrigen());
                Cueva destino = p.mazmorra.getCuevaPorId(conn.getDestino());
                if (origen != null && destino != null) {
                    p.mazmorra.conectarCuevas(origen, destino, conn.getEtiqueta());
                }
            }
        }

        // Jugador en INICIO de la primera cueva
        Cueva primera = p.mazmorra.getCuevaActual();
        p.jugador = new Jugador("Heroe", 100, 15, 5, 3, 1, 1);
        if (primera != null) {
            for (int f = 0; f < primera.getFilas(); f++) {
                for (int c = 0; c < primera.getColumnas(); c++) {
                    if (primera.getCelda(f, c).getTipo() == TipoCelda.INICIO) {
                        p.jugador.cambiarPosicion(f, c);
                        break;
                    }
                }
            }
        }

        p.mensajes.addLast("Bienvenido a ESCAPE DE LA MAZMORRA");
        p.mensajes.addLast("Llega a la SALIDA de la cueva dificil para escapar.");
        return p;
    }

    // ---------------------------------------------------------------
    // Movimiento del jugador
    // ---------------------------------------------------------------

    public boolean moverJugador(int fila, int columna) {
        if (!"EN_CURSO".equals(estado)) return false;
        Cueva c = getCuevaActual();
        if (c == null) return false;
        int df = fila - jugador.getFila();
        int dc = columna - jugador.getColumna();
        if (Math.abs(df) + Math.abs(dc) != 1) return false;
        if (!c.estaDentro(fila, columna)) return false;
        if (!c.esTransitable(fila, columna)) {
            mensajes.addLast("No puedes moverte ahi, hay un muro.");
            return false;
        }
        jugador.cambiarPosicion(fila, columna);
        ObjetoEnMapa om = getObjetoEn(fila, columna);
        if (om != null) {
            mensajes.addLast("Hay " + om.getObjeto().getNombre() + " aqui.");
        }
        return true;
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

    // ---------------------------------------------------------------
    // Combate
    // ---------------------------------------------------------------

    public boolean atacar() {
        if (!"EN_CURSO".equals(estado)) return false;
        Enemigo e = getEnemigoAdyacente();
        if (e == null) {
            mensajes.addLast("No hay enemigos adyacentes.");
            return false;
        }
        int dano = jugador.getAtaqueTotal() - e.getDefensaBase();
        if (dano < 1) dano = 1;
        e.recibirDano(dano);
        mensajes.addLast("Atacas a " + e.getNombre() + " (" + dano + " dano).");
        if (!e.estaVivo()) {
            mensajes.addLast(e.getNombre() + " ha muerto.");
        }
        return true;
    }

    // ---------------------------------------------------------------
    // Objetos
    // ---------------------------------------------------------------

    public boolean recogerObjeto() {
        if (!"EN_CURSO".equals(estado)) return false;
        CuevaData data = getCuevaDataActual();
        if (data == null) return false;
        int f = jugador.getFila();
        int c = jugador.getColumna();
        ObjetoEnMapa om = null;
        MiIterador<ObjetoEnMapa> it = data.objetosMapa.getIterador();
        while (it.hasNext()) {
            ObjetoEnMapa o = it.next();
            if (o.getFila() == f && o.getColumna() == c) {
                om = o;
                break;
            }
        }
        if (om == null) {
            mensajes.addLast("No hay nada que recoger aqui.");
            return false;
        }
        try {
            jugador.agregarObjeto(om.getObjeto());
            data.objetosMapa.del(om);
            mensajes.addLast("Has recogido: " + om.getObjeto().getNombre());
            return true;
        } catch (IllegalArgumentException e) {
            mensajes.addLast("No puedes recogerlo (id duplicado).");
            return false;
        }
    }

    public boolean usarPocion(String id) {
        if (!"EN_CURSO".equals(estado)) return false;
        ListaDE<Objeto> inv = jugador.getInventario();
        MiIterador<Objeto> it = inv.getIterador();
        while (it.hasNext()) {
            Objeto o = it.next();
            if (o.getId().equals(id) && o instanceof Pocion) {
                boolean ok = jugador.usarPocion((Pocion) o);
                if (ok) mensajes.addLast("Has usado " + o.getNombre() + ".");
                else mensajes.addLast("No se pudo usar la pocion.");
                return ok;
            }
        }
        mensajes.addLast("No tienes esa pocion.");
        return false;
    }

    public boolean equiparItem(String id) {
        if (!"EN_CURSO".equals(estado)) return false;
        ListaDE<Objeto> inv = jugador.getInventario();
        MiIterador<Objeto> it = inv.getIterador();
        while (it.hasNext()) {
            Objeto o = it.next();
            if (o.getId().equals(id)) {
                if (o instanceof Arma) {
                    boolean ok = jugador.equiparArma((Arma) o);
                    mensajes.addLast(ok ? "Equipado: " + o.getNombre() : "No se pudo equipar " + o.getNombre());
                    return ok;
                } else if (o instanceof Escudo) {
                    boolean ok = jugador.equiparEscudo((Escudo) o);
                    mensajes.addLast(ok ? "Equipado: " + o.getNombre() : "No se pudo equipar " + o.getNombre());
                    return ok;
                } else {
                    mensajes.addLast("Ese objeto no se equipa.");
                    return false;
                }
            }
        }
        mensajes.addLast("No tienes ese objeto.");
        return false;
    }

    // ---------------------------------------------------------------
    // Navegacion entre cuevas
    // ---------------------------------------------------------------

    public boolean estaEnPuerta() {
        Cueva c = getCuevaActual();
        if (c == null) return false;
        return c.getCelda(jugador.getFila(), jugador.getColumna()).getTipo() == TipoCelda.PUERTA;
    }

    public boolean cambiarCueva() {
        if (!"EN_CURSO".equals(estado)) return false;
        if (!estaEnPuerta()) {
            mensajes.addLast("No estas en una puerta.");
            return false;
        }
        Cueva actual = getCuevaActual();
        ListaSE<Cueva> siguientes = mazmorra.getCuevasSiguientes(actual);
        if (siguientes == null || siguientes.getSize() == 0) {
            mensajes.addLast("No hay salida desde aqui.");
            return false;
        }
        Cueva destino = siguientes.get(0);
        mazmorra.avanzarACueva(destino);
        boolean puesto = false;
        for (int f = 0; f < destino.getFilas(); f++) {
            for (int c = 0; c < destino.getColumnas(); c++) {
                if (destino.getCelda(f, c).getTipo() == TipoCelda.INICIO) {
                    jugador.cambiarPosicion(f, c);
                    puesto = true;
                    break;
                }
            }
            if (puesto) break;
        }
        if (!puesto) jugador.cambiarPosicion(1, 1);
        mensajes.addLast("Has entrado en " + destino.getId() + ".");
        return true;
    }

    // ---------------------------------------------------------------
    // Turnos
    // ---------------------------------------------------------------

    public void terminarTurno() {
        if (!"EN_CURSO".equals(estado)) return;

        CuevaData data = getCuevaDataActual();
        if (data != null) {
            MiIterador<Enemigo> it = data.enemigos.getIterador();
            while (it.hasNext()) {
                Enemigo e = it.next();
                if (e.estaVivo()) actuarEnemigo(e);
            }
        }

        turnosRestantes--;
        mensajes.addLast("Turnos: " + turnosRestantes);

        if (!jugador.estaVivo()) {
            estado = "DERROTA";
            mensajes.addLast("¡Has muerto! GAME OVER.");
            return;
        }

        Cueva actual = getCuevaActual();
        if (actual != null && "cueva_dificil".equals(actual.getId())) {
            Celda celda = actual.getCelda(jugador.getFila(), jugador.getColumna());
            if (celda.getTipo() == TipoCelda.SALIDA) {
                boolean vivos = false;
                if (data != null) {
                    MiIterador<Enemigo> it = data.enemigos.getIterador();
                    while (it.hasNext()) {
                        if (it.next().estaVivo()) { vivos = true; break; }
                    }
                }
                if (!vivos) {
                    estado = "VICTORIA";
                    mensajes.addLast("¡Has escapado! VICTORIA.");
                } else {
                    mensajes.addLast("Derrota a los enemigos antes de escapar.");
                }
            }
        }

        if (turnosRestantes <= 0 && "EN_CURSO".equals(estado)) {
            estado = "DERROTA";
            mensajes.addLast("Se acabaron los turnos. GAME OVER.");
        }
    }

    private void actuarEnemigo(Enemigo e) {
        int df = jugador.getFila() - e.getFila();
        int dc = jugador.getColumna() - e.getColumna();
        int dist = Math.abs(df) + Math.abs(dc);
        if (dist == 1) {
            int dano = Math.max(1, e.getAtaqueBase() - jugador.getDefensaTotal());
            jugador.recibirDano(dano);
            mensajes.addLast(e.getNombre() + " te ataca (" + dano + " dano).");
        } else {
            Cueva c = getCuevaActual();
            int mejorF = e.getFila(), mejorC = e.getColumna(), mejorDist = dist;
            int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
            for (int[] d : dirs) {
                int nf = e.getFila() + d[0];
                int nc = e.getColumna() + d[1];
                if (c.estaDentro(nf, nc) && c.esTransitable(nf, nc)) {
                    if (nf == jugador.getFila() && nc == jugador.getColumna()) continue;
                    int nd = Math.abs(jugador.getFila() - nf) + Math.abs(jugador.getColumna() - nc);
                    if (nd < mejorDist) { mejorDist = nd; mejorF = nf; mejorC = nc; }
                }
            }
            if (mejorF != e.getFila() || mejorC != e.getColumna()) {
                e.cambiarPosicion(mejorF, mejorC);
            }
        }
    }

    // ---------------------------------------------------------------
    // Guardado / carga
    // ---------------------------------------------------------------

    public void guardar(String ruta) throws IOException {
        DatosPartidaDTO dto = SerializadorPartida.desdeMazmorraJugador(
                mazmorra, jugador, estado, turnosRestantes);
        SerializadorPartida.guardar(dto, ruta);
    }

    public static Partida cargar(String ruta) throws IOException {
        DatosPartidaDTO dto = SerializadorPartida.cargar(ruta);
        Partida p = new Partida();
        p.mazmorra = SerializadorPartida.dtoAMazmorra(dto.getMazmorra());
        p.jugador = SerializadorPartida.dtoAJugador(dto.getJugador());
        p.turnosRestantes = dto.getTurnosRestantes();
        p.estado = dto.getEstado();
        p.mensajes = new ListaSE<>();
        p.cuevasData = new ListaSE<>();
        p.mensajes.addLast("Partida cargada.");
        ListaSE<Cueva> cuevas = p.mazmorra.getCuevas();
        MiIterador<Cueva> it = cuevas.getIterador();
        while (it.hasNext()) {
            p.cuevasData.addLast(new CuevaData(it.next().getId()));
        }
        return p;
    }

    // ---------------------------------------------------------------
    // Consultas
    // ---------------------------------------------------------------

    public Cueva getCuevaActual() { return mazmorra.getCuevaActual(); }
    public Jugador getJugador() { return jugador; }
    public Mazmorra getMazmorra() { return mazmorra; }
    public String getEstado() { return estado; }
    public int getTurnosRestantes() { return turnosRestantes; }
    public ListaSE<String> getMensajes() { return mensajes; }

    public ListaDE<Enemigo> getEnemigosActuales() {
        ListaDE<Enemigo> vivos = new ListaDE<>();
        CuevaData data = getCuevaDataActual();
        if (data == null) return vivos;
        MiIterador<Enemigo> it = data.enemigos.getIterador();
        while (it.hasNext()) {
            Enemigo e = it.next();
            if (e.estaVivo()) vivos.addLast(e);
        }
        return vivos;
    }

    public ListaDE<ObjetoEnMapa> getObjetosActuales() {
        CuevaData data = getCuevaDataActual();
        if (data == null) return new ListaDE<>();
        return data.objetosMapa;
    }

    public Enemigo getEnemigoAdyacente() {
        CuevaData data = getCuevaDataActual();
        if (data == null) return null;
        int pf = jugador.getFila(), pc = jugador.getColumna();
        MiIterador<Enemigo> it = data.enemigos.getIterador();
        while (it.hasNext()) {
            Enemigo e = it.next();
            if (!e.estaVivo()) continue;
            if (Math.abs(e.getFila() - pf) + Math.abs(e.getColumna() - pc) == 1) return e;
        }
        return null;
    }

    public ObjetoEnMapa getObjetoEn(int fila, int columna) {
        CuevaData data = getCuevaDataActual();
        if (data == null) return null;
        MiIterador<ObjetoEnMapa> it = data.objetosMapa.getIterador();
        while (it.hasNext()) {
            ObjetoEnMapa o = it.next();
            if (o.getFila() == fila && o.getColumna() == columna) return o;

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

    public boolean hayObjetoEnPosicion() {
        return getObjetoEn(jugador.getFila(), jugador.getColumna()) != null;
    }

    // ---------------------------------------------------------------
    // Auxiliares privados
    // ---------------------------------------------------------------

    private CuevaData getCuevaDataActual() {
        Cueva actual = getCuevaActual();
        if (actual == null) return null;
        MiIterador<CuevaData> it = cuevasData.getIterador();
        while (it.hasNext()) {
            CuevaData d = it.next();
            if (d.cuevaId.equals(actual.getId())) return d;
        }
        return null;
    }

    private static TipoEnemigo mapearTipo(String tipo) {
        if (tipo == null) return TipoEnemigo.ESQUELETO;
        switch (tipo.toUpperCase()) {
            case "BOSS": return TipoEnemigo.BOSS;
            case "ORCO": return TipoEnemigo.ORCO;
            case "MAGO": return TipoEnemigo.MAGO;
            default: return TipoEnemigo.ESQUELETO;
        }
    }

    private static Objeto crearObjeto(ConfiguracionObjetoDTO oc, String id) {
        if (oc.getTipo() == null) return null;
        switch (oc.getTipo().toUpperCase()) {
            case "POCION": return new Pocion(id, oc.getCura() > 0 ? oc.getCura() : Pocion.CURACION_BASE);
            case "ESPADA": return new Espada(id);
            case "ARCO": return new Arco(id);
            case "ESCUDO": return new Escudo(id);
            case "LLAVE": return new Llave(id, TipoLlave.PUERTA, id);
            default: return null;
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
