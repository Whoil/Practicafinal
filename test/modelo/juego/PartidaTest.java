package modelo.juego;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import Estructuras.ListaDE;
import Estructuras.ListaSE;
import Estructuras.MiIterador;
import modelo.mapa.Cueva;
import modelo.mapa.TipoCelda;
import modelo.objetos.Espada;
import modelo.objetos.Objeto;
import modelo.objetos.Pocion;
import modelo.personajes.Enemigo;
import modelo.personajes.Jugador;

import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {

    @Test
    void crearPartidaNuevaDevuelvePartidaValida() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        assertNotNull(p);
        assertEquals("EN_CURSO", p.getEstado());
        assertEquals(40, p.getTurnosRestantes());
        assertNotNull(p.getJugador());
        assertEquals("Heroe", p.getJugador().getNombre());
        assertEquals(100, p.getJugador().getVidaActual());
        assertNotNull(p.getCuevaActual());
        assertEquals("cueva_facil", p.getCuevaActual().getId());
        assertNotNull(p.getMazmorra());
    }

    @Test
    void crearPartidaNuevaColocaJugadorEnInicio() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        Jugador j = p.getJugador();
        Cueva c = p.getCuevaActual();
        assertEquals(TipoCelda.INICIO, c.getCelda(j.getFila(), j.getColumna()).getTipo());
    }

    @Test
    void moverJugadorACasillaAdyacenteValida() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        Jugador j = p.getJugador();
        int colAntes = j.getColumna();
        assertTrue(p.moverJugador(j.getFila(), colAntes + 1));
        assertEquals(colAntes + 1, j.getColumna());
    }

    @Test
    void moverJugadorArribaFunciona() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        Jugador j = p.getJugador();
        p.moverJugador(j.getFila() + 1, j.getColumna());
        int filaAntes = j.getFila();
        assertTrue(p.moverJugadorArriba());
        assertEquals(filaAntes - 1, j.getFila());
    }

    @Test
    void moverJugadorAbajoFunciona() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        Jugador j = p.getJugador();
        assertTrue(p.moverJugadorAbajo());
        assertEquals(j.getFila(), 2);
    }

    @Test
    void moverJugadorContraMuroDevuelveFalse() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        Jugador j = p.getJugador();
        assertFalse(p.moverJugador(j.getFila() - 1, j.getColumna()));
    }

    @Test
    void moverJugadorFueraDelMapaDevuelveFalse() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        assertFalse(p.moverJugador(-1, 0));
    }

    @Test
    void moverJugadorADistanciaMayorAUnoDevuelveFalse() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        assertFalse(p.moverJugador(5, 5));
    }

    @Test
    void atacarSinEnemigoAdyacenteDevuelveFalse() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        assertFalse(p.atacar());
    }

    @Test
    void atacarEnemigoAdyacenteReduceSuVida() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        Jugador j = p.getJugador();
        p.moverJugador(j.getFila() + 1, j.getColumna());
        ListaDE<Enemigo> antes = p.getEnemigosActuales();
        assertEquals(1, antes.getSize());
        Enemigo e = antes.get(0);
        int vidaAntes = e.getVidaActual();
        assertTrue(p.atacar());
        assertTrue(e.getVidaActual() < vidaAntes);
    }

    @Test
    void atacarVariasVecesMataEnemigo() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        Jugador j = p.getJugador();
        p.moverJugador(j.getFila() + 1, j.getColumna());
        Enemigo e = p.getEnemigosActuales().get(0);
        while (e.estaVivo() && p.getEstado().equals("EN_CURSO")) {
            p.atacar();
        }
        assertFalse(e.estaVivo());
        assertEquals(0, p.getEnemigosActuales().getSize());
    }

    @Test
    void recogerObjetoEnLaMismaCasilla() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        Jugador j = p.getJugador();
        p.moverJugador(j.getFila(), j.getColumna() + 1);
        p.moverJugador(j.getFila(), j.getColumna() + 1);
        p.moverJugador(j.getFila() + 1, j.getColumna());
        assertTrue(p.hayObjetoEnPosicion());
        assertTrue(p.recogerObjeto());
    }

    @Test
    void recogerObjetoSinObjetoDevuelveFalse() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        assertFalse(p.hayObjetoEnPosicion());
        assertFalse(p.recogerObjeto());
    }

    @Test
    void terminarTurnoReduceTurnosRestantes() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        int antes = p.getTurnosRestantes();
        p.terminarTurno();
        assertEquals(antes - 1, p.getTurnosRestantes());
    }

    @Test
    void getEnemigosActualesSoloDevuelveVivos() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        ListaDE<Enemigo> enemigos = p.getEnemigosActuales();
        assertNotNull(enemigos);
        MiIterador<Enemigo> it = enemigos.getIterador();
        while (it.hasNext()) {
            assertTrue(it.next().estaVivo());
        }
    }

    @Test
    void getMensajesNoEsNulo() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        assertNotNull(p.getMensajes());
        assertTrue(p.getMensajes().getSize() > 0);
    }

    @Test
    void jugadorMuertoCambiaEstadoADerrota() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        Jugador j = p.getJugador();
        j.recibirDano(999);
        assertFalse(j.estaVivo());
        p.terminarTurno();
        assertEquals("DERROTA", p.getEstado());
    }

    @Test
    void turnosAgotadosCambianEstadoADerrota() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        for (int i = 0; i < 40; i++) {
            p.terminarTurno();
        }
        assertEquals("DERROTA", p.getEstado());
    }

    @Test
    void estaEnPuertaDevuelveFalseEnInicio() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        assertFalse(p.estaEnPuerta());
    }

    @Test
    void cambiarCuevaSinEstarEnPuertaDevuelveFalse() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        assertFalse(p.cambiarCueva());
    }

    @Test
    void guardarYRecargarPartida() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        p.moverJugador(p.getJugador().getFila() + 1, p.getJugador().getColumna());
        p.atacar();
        int turnosAntes = p.getTurnosRestantes();
        String estadoAntes = p.getEstado();

        String ruta = "datos/partida_test_temp.json";
        p.guardar(ruta);

        Partida cargada = Partida.cargar(ruta);
        assertNotNull(cargada);
        assertEquals(estadoAntes, cargada.getEstado());
        assertEquals(turnosAntes, cargada.getTurnosRestantes());
        assertEquals(p.getJugador().getVidaActual(), cargada.getJugador().getVidaActual());
        assertEquals(p.getJugador().getFila(), cargada.getJugador().getFila());
        assertEquals(p.getCuevaActual().getId(), cargada.getCuevaActual().getId());

        java.io.File f = new java.io.File(ruta);
        if (f.exists()) f.delete();
    }

    @Test
    void cargarRutaInvalidaLanzaExcepcion() {
        assertThrows(IOException.class, () -> Partida.cargar("datos/no_existe.json"));
    }

    @Test
    void estadoInicialEsEnCurso() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        assertEquals("EN_CURSO", p.getEstado());
    }

    @Test
    void moverJugadorEnEstadoNoEnCursoNoFunciona() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        p.getJugador().recibirDano(999);
        p.terminarTurno();
        assertFalse(p.moverJugador(1, 2));
    }
}
