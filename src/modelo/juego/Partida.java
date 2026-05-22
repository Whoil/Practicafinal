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
        }
    }
}
