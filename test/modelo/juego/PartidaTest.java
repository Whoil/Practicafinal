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

import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {

    @Test
    void crearPartidaNuevaDevuelvePartidaValida() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        assertNotNull(p);
        assertEquals(EstadoPartida.EN_CURSO, p.getEstado());
        assertEquals(60, p.getTurnosRestantes());
        assertNotNull(p.getJugador());
        assertEquals("Jugador", p.getJugador().getNombre());
        assertEquals(100, p.getJugador().getVidaActual());
        assertNotNull(p.getCuevaActual());
        assertEquals("cueva_facil", p.getCuevaActual().getId());
        assertNotNull(p.getMazmorra());
    }

    @Test
    void crearPartidaNuevaColocaJugadorEnInicio() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        Jugador j = p.getJugador();
        CuevaEnMapa c = p.getCuevaActual();
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
    void moverJugadorArribaFunciona() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        assertTrue(partida.moverJugadorArriba());
        assertEquals(0, jugador.getFila());
    }

    @Test
    void moverJugadorAbajoFunciona() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        Jugador j = p.getJugador();
        assertTrue(p.moverJugador(3, 2));
        assertTrue(p.terminarTurno());
        assertTrue(p.moverJugadorAbajo());
        assertEquals(4, j.getFila());
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
    void atacarSinEnemigoAdyacenteDevuelveFalse() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 0, 0);
        Partida partida = new Partida(mazmorra, jugador, 10);
        assertFalse(partida.atacar());
    }

    @Test
    void atacarEnemigoAdyacenteReduceSuVida() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        ListaDE<Enemigo> antes = p.getEnemigosActuales();
        assertTrue(antes.getSize() > 0);
        Enemigo e = antes.get(0);
        int vidaAntes = e.getVidaActual();
        assertTrue(p.moverJugador(3, 2));
        assertTrue(p.terminarTurno());
        assertTrue(p.moverJugador(4, 2));
        assertTrue(p.terminarTurno());
        assertTrue(p.atacar());
        assertTrue(e.getVidaActual() < vidaAntes);
    }

    @Test
    void atacarVariasVecesMataEnemigo() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        Enemigo e = p.getEnemigosActuales().get(0);
        while (e.estaVivo() && p.getEstado() == EstadoPartida.EN_CURSO) {
            p.atacar();
            if (e.estaVivo()) {
                p.terminarTurno();
            }
        }
        assertFalse(e.estaVivo());
    }

    @Test
    void recogerObjetoEnLaMismaCasilla() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 0, 0);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Pocion pocion = new Pocion("p1");

        assertTrue(partida.anadirObjetoEnSuelo(cueva, pocion, 0, 1));
        assertFalse(partida.hayObjetoEnPosicion());
        assertTrue(partida.moverJugador(0, 1));
        assertFalse(partida.hayObjetoEnPosicion());
        assertEquals(1, jugador.getCantidadObjetosInventario());
    }

    @Test
    void recogerObjetoSinObjetoDevuelveFalse() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        assertFalse(p.hayObjetoEnPosicion());
        assertFalse(p.recogerObjeto());
    }

    @Test
    void abrirTesoroConvierteCeldaEnSueloYConsumeAccion() {
        Cueva cueva = new Cueva("c1", 3, 3);
        cueva.cambiarTipoCelda(1, 1, TipoCelda.TESORO);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);

        assertTrue(partida.abrirTesoro());
        assertEquals(TipoCelda.SUELO, partida.getCuevaActual().getCelda(1, 1).getTipo());
        assertTrue(partida.isAccionRealizada());
        assertFalse(partida.abrirTesoro());
    }

    @Test
    void tesoroCerradoNoSePuedePisarPeroSeAbreDesdeCasillaAdyacente() {
        Cueva cueva = new Cueva("c1", 3, 3);
        cueva.cambiarTipoCelda(1, 2, TipoCelda.TESORO);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);

        assertTrue(partida.hayTesoroCercano());
        assertFalse(partida.moverJugador(1, 2));
        assertEquals(1, jugador.getColumna());

        assertTrue(partida.abrirTesoro());
        assertEquals(TipoCelda.SUELO, partida.getCuevaActual().getCelda(1, 2).getTipo());
        assertTrue(partida.moverJugador(1, 2));
        assertEquals(2, jugador.getColumna());
    }

    @Test
    void tesoroCerradoNoPermiteUsarloComoCaminoIntermedio() {
        Cueva cueva = new Cueva("c1", 3, 4);
        cueva.cambiarTipoCelda(1, 2, TipoCelda.TESORO);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 3, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);

        assertFalse(partida.moverJugador(1, 3));
        assertEquals(1, jugador.getColumna());
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
        assertEquals(EstadoPartida.DERROTA, p.getEstado());
    }

    @Test
    void turnosAgotadosCambianEstadoADerrota() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        for (int i = 0; i < 60; i++) {
            p.terminarTurno();
        }
        assertEquals(EstadoPartida.DERROTA, p.getEstado());
    }

    @Test
    void cambiarCuevaSinLlaveDevuelveFalse() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        assertFalse(p.cambiarCueva());
    }

    @Test
    void guardarPartidaCreaArchivo() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        p.moverJugador(p.getJugador().getFila() + 1, p.getJugador().getColumna());
        p.atacar();

        String ruta = "datos/partida_test_temp.json";
        p.guardar(ruta);

        java.io.File f = new java.io.File(ruta);
        assertTrue(f.exists());
        assertTrue(f.length() > 0);
        if (f.exists()) f.delete();
    }

    @Test
    void guardarConRutaInvalidaLanzaExcepcion() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        assertThrows(IOException.class, () -> p.guardar(""));
    }

    @Test
    void estadoInicialEsEnCurso() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        assertEquals(EstadoPartida.EN_CURSO, p.getEstado());
    }

    @Test
    void moverJugadorEnEstadoNoEnCursoNoFunciona() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        p.getJugador().recibirDano(999);
        p.terminarTurno();
        assertFalse(p.moverJugador(1, 2));
    }

    @Test
    void getJugadorEnMapaDevuelveVistaDelJugador() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 2);
        jugador.recibirDano(25);
        Partida partida = new Partida(mazmorra, jugador, 10);

        PersonajeEnMapa vista = partida.getJugadorEnMapa();

        assertEquals("Heroe", vista.getNombre());
        assertEquals("JUGADOR", vista.getTipo());
        assertEquals(75, vista.getVidaActual());
        assertEquals(100, vista.getVidaMaxima());
        assertEquals(1, vista.getFila());
        assertEquals(2, vista.getColumna());
    }

    @Test
    void getEnemigosDevuelveVistasDeEnemigosVivos() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 2, 1, 0, 0);

        assertTrue(partida.anadirEnemigo(cueva, enemigo));

        ListaSE<PersonajeEnMapa> enemigos = partida.getEnemigos();
        assertEquals(1, enemigos.getSize());
        assertEquals("Esqueleto", enemigos.get(0).getNombre());
        assertEquals("ESQUELETO", enemigos.get(0).getTipo());
        assertEquals(0, enemigos.get(0).getFila());
        assertEquals(0, enemigos.get(0).getColumna());
    }

    @Test
    void getCuevaActualDevuelveVistaConCopiaDeCeldas() {
        Cueva cueva = new Cueva("c1", 2, 2);
        cueva.cambiarTipoCelda(0, 1, TipoCelda.MURO);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 0, 0);
        Partida partida = new Partida(mazmorra, jugador, 10);

        CuevaEnMapa vista = partida.getCuevaActual();
        ListaSE<CeldaEnMapa> celdas = vista.getCeldas();
        celdas.clear();

        assertEquals("c1", vista.getId());
        assertEquals(2, vista.getFilas());
        assertEquals(2, vista.getColumnas());
        assertEquals(4, vista.getCeldas().getSize());
        assertEquals(TipoCelda.SUELO, vista.getCeldas().get(0).getTipo());
        assertEquals(TipoCelda.MURO, vista.getCeldas().get(1).getTipo());
    }

    @Test
    void celdaEnMapaComparaPorPosicion() {
        CeldaEnMapa suelo = new CeldaEnMapa(1, 2, TipoCelda.SUELO);
        CeldaEnMapa muroMismaPosicion = new CeldaEnMapa(1, 2, TipoCelda.MURO);
        CeldaEnMapa otraCelda = new CeldaEnMapa(2, 1, TipoCelda.SUELO);

        assertEquals(1, suelo.getFila());
        assertEquals(2, suelo.getColumna());
        assertEquals(TipoCelda.SUELO, suelo.getTipo());
        assertEquals(suelo, muroMismaPosicion);
        assertEquals(suelo.hashCode(), muroMismaPosicion.hashCode());
        assertNotEquals(suelo, otraCelda);
    }

    @Test
    void objetoEnMapaIncluyePosicionEnIgualdad() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Pocion pocion = new Pocion("p1");
        ObjetoEnMapa objeto = new ObjetoEnMapa(pocion, cueva, 0, 0);
        ObjetoEnMapa mismaPosicion = new ObjetoEnMapa(pocion, cueva, 0, 0);
        ObjetoEnMapa otraPosicion = new ObjetoEnMapa(pocion, cueva, 0, 1);

        assertSame(pocion, objeto.getObjeto());
        assertEquals(cueva, objeto.getCueva());
        assertEquals(0, objeto.getFila());
        assertEquals(0, objeto.getColumna());
        assertEquals(objeto, mismaPosicion);
        assertEquals(objeto.hashCode(), mismaPosicion.hashCode());
        assertNotEquals(objeto, otraPosicion);
    }

    @Test
    void puertaExponeDatosYEstadoAbierto() {
        Cueva origen = new Cueva("origen", 2, 2);
        Cueva destino = new Cueva("destino", 2, 2);
        Puerta puerta = new Puerta("p1", origen, destino, "llave-p1");

        assertEquals("p1", puerta.getId());
        assertEquals(origen, puerta.getOrigen());
        assertEquals(destino, puerta.getDestino());
        assertEquals("llave-p1", puerta.getCodigoLlave());
        assertFalse(puerta.isAbierta());
        assertTrue(puerta.conecta(origen, destino));

        puerta.abrir();

        assertTrue(puerta.isAbierta());
    }

    @Test
    void moverJugadorUsaAlcanceDeMovimientoYSoloPermiteUnMovimientoPorTurno() {
        Cueva cueva = new Cueva("c1", 4, 4);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 0, 0);
        Partida partida = new Partida(mazmorra, jugador, 10);

        assertTrue(partida.moverJugador(0, 1));
        assertEquals(0, jugador.getFila());
        assertEquals(1, jugador.getColumna());
        assertFalse(partida.moverJugador(0, 0));
    }

    @Test
    void moverJugadorNoPermiteCeldaOcupadaPorEnemigo() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 2, 1, 1, 2);

        assertTrue(partida.anadirEnemigo(cueva, enemigo));
        assertFalse(partida.moverJugador(1, 2));
        assertEquals(1, jugador.getFila());
        assertEquals(1, jugador.getColumna());
    }

    @Test
    void anadirEnemigoNoPermiteSolaparPersonajes() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 2, 1, 0, 0);
        Enemigo otroEnemigo = new Enemigo("Orco", TipoEnemigo.ORCO, 30, 8, 2, 1, 0, 0);
        Enemigo sobreJugador = new Enemigo("Mago", TipoEnemigo.MAGO, 30, 8, 2, 1, 1, 1);

        assertTrue(partida.anadirEnemigo(cueva, enemigo));
        assertFalse(partida.anadirEnemigo(cueva, otroEnemigo));
        assertFalse(partida.anadirEnemigo(cueva, sobreJugador));
    }

    @Test
    void recogerObjetoPermiteCeldasAdyacentes() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Pocion pocion = new Pocion("p1");

        assertTrue(partida.anadirObjetoEnSuelo(cueva, pocion, 0, 0));
        assertTrue(partida.recogerObjeto("p1"));

        assertEquals(1, jugador.getCantidadObjetosInventario());
        assertTrue(partida.getObjetosEnSuelo().isEmpty());
    }

    @Test
    void avanzarACuevaRequiereLlaveDeLaPuerta() {
        Cueva origen = new Cueva("origen", 2, 2);
        origen.cambiarTipoCelda(0, 0, TipoCelda.PUERTA);
        Cueva destino = new Cueva("destino", 2, 2);
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.conectarCuevas(origen, destino, "p1");
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 0, 0);
        Puerta puerta = new Puerta("p1", origen, destino, "llave-p1");
        ListaSE<Puerta> puertas = new ListaSE<>();
        puertas.addLast(puerta);
        Partida partida = new Partida(mazmorra, jugador, 10, puertas);

        assertFalse(partida.avanzarACueva("destino"));

        jugador.agregarObjeto(new Llave("llave-p1", TipoLlave.PUERTA, "llave-p1"));

        assertTrue(partida.avanzarACueva("destino"));
        assertEquals(destino.getId(), partida.getCuevaActual().getId());
    }

    @Test
    void puertaAbiertaSinLlaveNoPermiteAvanzar() {
        Cueva origen = new Cueva("origen", 2, 2);
        origen.cambiarTipoCelda(0, 0, TipoCelda.PUERTA);
        Cueva destino = new Cueva("destino", 2, 2);
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.conectarCuevas(origen, destino, "p1");
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 0, 0);
        Puerta puerta = new Puerta("p1", origen, destino, "llave-p1");
        puerta.abrir();
        ListaSE<Puerta> puertas = new ListaSE<>();
        puertas.addLast(puerta);
        Partida partida = new Partida(mazmorra, jugador, 10, puertas);

        assertFalse(partida.avanzarACueva("destino"));
        assertEquals(origen.getId(), partida.getCuevaActual().getId());
    }

    @Test
    void avanzarACuevaEvitaEntrarSobreEnemigo() {
        Cueva origen = new Cueva("origen", 3, 3);
        origen.cambiarTipoCelda(1, 1, TipoCelda.PUERTA);
        Cueva destino = new Cueva("destino", 3, 3);
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.conectarCuevas(origen, destino, "p1");
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Puerta puerta = new Puerta("p1", origen, destino, "llave-p1");
        ListaSE<Puerta> puertas = new ListaSE<>();
        puertas.addLast(puerta);
        Partida partida = new Partida(mazmorra, jugador, 10, puertas);
        Enemigo enemigoDestino = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 2, 1, 1, 1);
        jugador.agregarObjeto(new Llave("llave-p1", TipoLlave.PUERTA, "llave-p1"));

        assertTrue(partida.anadirEnemigo(destino, enemigoDestino));
        assertTrue(partida.avanzarACueva("destino"));
        assertEquals(destino.getId(), partida.getCuevaActual().getId());
        assertNotEquals(1, partida.getJugador().getFila());
        assertNotEquals(1, partida.getJugador().getColumna());
    }

    @Test
    void puertaInicialSinConexionEnMazmorraSeRechaza() {
        Cueva origen = new Cueva("origen", 2, 2);
        Cueva destino = new Cueva("destino", 2, 2);
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.addCueva(origen);
        mazmorra.addCueva(destino);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 0, 0);
        ListaSE<Puerta> puertas = new ListaSE<>();
        puertas.addLast(new Puerta("p1", origen, destino, "llave-p1"));

        assertThrows(IllegalArgumentException.class, () -> new Partida(mazmorra, jugador, 10, puertas));
    }

    @Test
    void ataqueAdyacenteDerrotaBossYEntregaLlaveFinal() {
        Cueva cueva = new Cueva("c1", 3, 3);
        cueva.cambiarTipoCelda(0, 0, TipoCelda.SALIDA);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 30, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Boss boss = new Boss("Boss", 10, 20, 0, 1, 2, 2);

        assertTrue(partida.anadirEnemigo(cueva, boss));
        assertTrue(partida.atacar(2, 2));
        assertTrue(partida.tieneLlaveFinal());
        assertEquals(EstadoPartida.EN_CURSO, partida.getEstado());

        assertTrue(partida.moverJugador(0, 1));
        assertTrue(partida.terminarTurno());
        assertTrue(partida.moverJugador(0, 0));

        assertEquals(EstadoPartida.VICTORIA, partida.getEstado());
    }

    @Test
    void llaveFinalNoFallaSiExisteOtroObjetoConElIdPorDefecto() {
        Cueva cueva = new Cueva("c1", 3, 3);
        cueva.cambiarTipoCelda(0, 0, TipoCelda.SALIDA);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 30, 5, 2, 1, 1);
        jugador.agregarObjeto(new Llave(Partida.ID_LLAVE_FINAL_DEFECTO, TipoLlave.PUERTA, "otra-cerradura"));
        Partida partida = new Partida(mazmorra, jugador, 10);
        Boss boss = new Boss("Boss", 10, 20, 0, 1, 2, 2);

        assertTrue(partida.anadirEnemigo(cueva, boss));
        assertTrue(partida.atacar(2, 2));
        assertTrue(partida.tieneLlaveFinal());
        assertEquals(EstadoPartida.EN_CURSO, partida.getEstado());
        assertEquals(2, jugador.getCantidadObjetosInventario());

        assertTrue(partida.moverJugador(0, 1));
        assertTrue(partida.terminarTurno());
        assertTrue(partida.moverJugador(0, 0));

        assertEquals(EstadoPartida.VICTORIA, partida.getEstado());
    }

    @Test
    void arcoPermiteAtacarADistancia() {
        Cueva cueva = new Cueva("c1", 5, 5);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 0, 0);
        Arco arco = new Arco("arco1");
        jugador.agregarObjeto(arco);
        assertTrue(jugador.equiparArma(arco));
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 2, 1, 0, 3);

        assertTrue(partida.anadirEnemigo(cueva, enemigo));
        assertTrue(partida.atacar(0, 3));

        assertTrue(enemigo.getVidaActual() < enemigo.getVidaMaxima());
    }

    @Test
    void hayEnemigoEnDireccion_conEnemigo_devuelveTrue() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 2, 1, 0, 1);

        assertTrue(partida.anadirEnemigo(cueva, enemigo));

        assertTrue(partida.hayEnemigoEnDireccion(-1, 0));
    }

    @Test
    void hayEnemigoEnDireccion_sinEnemigo_devuelveFalse() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 2, 1, 0, 1);

        assertTrue(partida.anadirEnemigo(cueva, enemigo));

        assertFalse(partida.hayEnemigoEnDireccion(0, 1));
    }

    @Test
    void getEnemigosAdyacentes_devuelveVarios() {
        Cueva cueva = new Cueva("c1", 4, 4);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo arriba = new Enemigo("Arriba", TipoEnemigo.ESQUELETO, 30, 8, 2, 1, 0, 1);
        Enemigo derecha = new Enemigo("Derecha", TipoEnemigo.ORCO, 30, 8, 2, 1, 1, 2);
        Enemigo diagonal = new Enemigo("Diagonal", TipoEnemigo.MAGO, 30, 8, 2, 1, 2, 2);
        Enemigo lejos = new Enemigo("Lejos", TipoEnemigo.ESQUELETO, 30, 8, 2, 1, 3, 3);

        assertTrue(partida.anadirEnemigo(cueva, arriba));
        assertTrue(partida.anadirEnemigo(cueva, derecha));
        assertTrue(partida.anadirEnemigo(cueva, diagonal));
        assertTrue(partida.anadirEnemigo(cueva, lejos));

        assertEquals(3, partida.getEnemigosAdyacentes().getSize());
    }

    @Test
    void atacarDireccionConVariosEnemigosSoloDanaElElegido() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo arriba = new Enemigo("Arriba", TipoEnemigo.ESQUELETO, 30, 8, 2, 1, 0, 1);
        Enemigo derecha = new Enemigo("Derecha", TipoEnemigo.ORCO, 30, 8, 2, 1, 1, 2);

        assertTrue(partida.anadirEnemigo(cueva, arriba));
        assertTrue(partida.anadirEnemigo(cueva, derecha));

        assertTrue(partida.atacar(jugador.getFila(), jugador.getColumna() + 1));

        assertEquals(30, arriba.getVidaActual());
        assertTrue(derecha.getVidaActual() < derecha.getVidaMaxima());
    }

    @Test
    void atacarDireccionSinEnemigoDevuelveFalse() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);

        assertFalse(partida.hayEnemigoEnDireccion(0, 1));
        assertFalse(partida.atacar(jugador.getFila(), jugador.getColumna() + 1));
    }

    @Test
    void pasarTurnoHaceActuarEnemigosYConsumeTurno() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 0, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 3);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 2, 1, 1, 2);

        assertTrue(partida.anadirEnemigo(cueva, enemigo));
        assertTrue(partida.pasarTurno());

        assertEquals(2, partida.getTurnosRestantes());
        assertEquals(92, jugador.getVidaActual());
    }

    @Test
    void enemigoNoSeMueveSobreOtroEnemigo() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 0, 2, 0, 2);
        Partida partida = new Partida(mazmorra, jugador, 3);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 2, 1, 0, 0);
        Enemigo bloqueo = new Enemigo("Orco", TipoEnemigo.ORCO, 30, 8, 2, 1, 0, 1);

        assertTrue(partida.anadirEnemigo(cueva, enemigo));
        assertTrue(partida.anadirEnemigo(cueva, bloqueo));
        assertTrue(partida.pasarTurno());

        assertEquals(0, enemigo.getFila());
        assertEquals(0, enemigo.getColumna());
    }

    @Test
    void cambiarCuevaRequiereEstarSobrePuerta() {
        Cueva origen = new Cueva("origen", 3, 3);
        origen.cambiarTipoCelda(2, 2, TipoCelda.PUERTA);
        Cueva destino = new Cueva("destino", 3, 3);
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.conectarCuevas(origen, destino, "p1");
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        jugador.agregarObjeto(new Llave("llave-p1", TipoLlave.PUERTA, "llave-p1"));
        ListaSE<Puerta> puertas = new ListaSE<>();
        puertas.addLast(new Puerta("p1", origen, destino, "llave-p1"));
        Partida partida = new Partida(mazmorra, jugador, 10, puertas);

        assertFalse(partida.puedeCambiarCueva());
        assertFalse(partida.cambiarCueva());
        assertEquals("origen", partida.getCuevaActual().getId());
    }

    @Test
    void cambiarCuevaRequiereLlaveAunqueEsteSobrePuerta() {
        Cueva origen = new Cueva("origen", 3, 3);
        origen.cambiarTipoCelda(1, 1, TipoCelda.PUERTA);
        Cueva destino = new Cueva("destino", 3, 3);
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.conectarCuevas(origen, destino, "p1");
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        ListaSE<Puerta> puertas = new ListaSE<>();
        puertas.addLast(new Puerta("p1", origen, destino, "llave-p1"));
        Partida partida = new Partida(mazmorra, jugador, 10, puertas);

        assertFalse(partida.puedeCambiarCueva());
        assertFalse(partida.cambiarCueva());
        assertFalse(partida.avanzarACueva("destino"));
        assertEquals("origen", partida.getCuevaActual().getId());
    }

    @Test
    void cambiarCuevaDesdePuertaConLlaveAvanza() {
        Cueva origen = new Cueva("origen", 3, 3);
        origen.cambiarTipoCelda(1, 1, TipoCelda.PUERTA);
        Cueva destino = new Cueva("destino", 3, 3);
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.conectarCuevas(origen, destino, "p1");
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        jugador.agregarObjeto(new Llave("llave-p1", TipoLlave.PUERTA, "llave-p1"));
        ListaSE<Puerta> puertas = new ListaSE<>();
        puertas.addLast(new Puerta("p1", origen, destino, "llave-p1"));
        Partida partida = new Partida(mazmorra, jugador, 10, puertas);

        assertTrue(partida.puedeCambiarCueva());
        assertTrue(partida.cambiarCueva());
        assertEquals("destino", partida.getCuevaActual().getId());
    }

    @Test
    void cambiarCuevaNoConsumeTurnoYReiniciaTurnosSinHacerActuarEnemigos() {
        Cueva origen = new Cueva("origen", 3, 3);
        origen.cambiarTipoCelda(1, 1, TipoCelda.PUERTA);
        Cueva destino = new Cueva("destino", 3, 3);
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.conectarCuevas(origen, destino, "p1");
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        jugador.agregarObjeto(new Llave("llave-p1", TipoLlave.PUERTA, "llave-p1"));
        ListaSE<Puerta> puertas = new ListaSE<>();
        puertas.addLast(new Puerta("p1", origen, destino, "llave-p1"));
        Partida partida = new Partida(mazmorra, jugador, 4, puertas);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 2, 1, 1, 2);

        assertTrue(partida.anadirEnemigo(origen, enemigo));
        assertTrue(partida.cambiarCueva());

        assertEquals("destino", partida.getCuevaActual().getId());
        assertEquals(Partida.TURNOS_POR_CUEVA, partida.getTurnosRestantes());
        assertEquals(100, jugador.getVidaActual());
    }

    @Test
    void cambiarCuevaPermiteAvanzarAunqueLaAccionYaEsteUsada() {
        Cueva origen = new Cueva("origen", 3, 3);
        origen.cambiarTipoCelda(1, 1, TipoCelda.PUERTA);
        Cueva destino = new Cueva("destino", 3, 3);
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.conectarCuevas(origen, destino, "p1");
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        jugador.agregarObjeto(new Llave("llave-p1", TipoLlave.PUERTA, "llave-p1"));
        ListaSE<Puerta> puertas = new ListaSE<>();
        puertas.addLast(new Puerta("p1", origen, destino, "llave-p1"));
        Partida partida = new Partida(mazmorra, jugador, 10, puertas);
        Enemigo enemigo = new Enemigo("Orco", TipoEnemigo.ORCO, 50, 12, 4, 1, 1, 2);

        assertTrue(partida.anadirEnemigo(origen, enemigo));
        assertTrue(partida.atacar(1, 2));
        assertTrue(partida.isAccionRealizada());

        assertTrue(partida.puedeCambiarCueva());
        assertTrue(partida.cambiarCueva());
        assertEquals("destino", partida.getCuevaActual().getId());
        assertTrue(partida.isAccionRealizada());
        assertFalse(partida.atacar(1, 2));
    }

    @Test
    void cambiarCuevaNoDevuelveMovimientoYaUsado() {
        Cueva origen = new Cueva("origen", 3, 3);
        origen.cambiarTipoCelda(1, 2, TipoCelda.PUERTA);
        Cueva destino = new Cueva("destino", 3, 3);
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.conectarCuevas(origen, destino, "p1");
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        jugador.agregarObjeto(new Llave("llave-p1", TipoLlave.PUERTA, "llave-p1"));
        ListaSE<Puerta> puertas = new ListaSE<>();
        puertas.addLast(new Puerta("p1", origen, destino, "llave-p1"));
        Partida partida = new Partida(mazmorra, jugador, 10, puertas);

        assertTrue(partida.moverJugadorDerecha());
        assertTrue(partida.isMovimientoRealizado());
        assertTrue(partida.cambiarCueva());

        assertTrue(partida.isMovimientoRealizado());
        assertFalse(partida.moverJugadorDerecha());
    }

    @Test
    void equiparObjetoNoConsumeAccionYPermiteAtacarDespues() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Espada espada = new Espada("espada-1");
        jugador.agregarObjeto(espada);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 2, 1, 1, 2);

        assertTrue(partida.anadirEnemigo(cueva, enemigo));
        assertTrue(partida.equiparObjeto("espada-1"));
        assertFalse(partida.isAccionRealizada());

        assertTrue(partida.atacar(1, 2));
        assertTrue(partida.isAccionRealizada());
        assertTrue(enemigo.getVidaActual() < enemigo.getVidaMaxima());
    }

    @Test
    void estadisticasRegistranAtaqueTurnoYDanoRecibido() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 0, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 0, 1, 1, 2);

        assertTrue(partida.anadirEnemigo(cueva, enemigo));
        assertTrue(partida.atacar(1, 2));
        assertTrue(partida.pasarTurno());

        EstadisticasPartida stats = partida.getEstadisticas();
        assertEquals(1, stats.getTurnosJugados());
        assertEquals(15, stats.getDanoEjercido());
        assertEquals(8, stats.getDanoRecibido());
    }

    @Test
    void estadisticasRegistranMuertesComunesYBosses() {
        Cueva cueva = new Cueva("c1", 4, 4);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 50, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 10, 8, 0, 1, 1, 2);
        Boss boss = new Boss("Malakor", 10, 20, 0, 1, 2, 2);

        assertTrue(partida.anadirEnemigo(cueva, enemigo));
        assertTrue(partida.anadirEnemigo(cueva, boss));
        assertTrue(partida.atacar(1, 2));
        assertTrue(partida.pasarTurno());
        assertTrue(partida.atacar(2, 2));

        EstadisticasPartida stats = partida.getEstadisticas();
        assertEquals(1, stats.getEnemigosMuertos());
        assertEquals(1, stats.getBossesMuertos());
        assertTrue(stats.isMalakorDerrotado());
    }

    @Test
    void guardarYCargarRoundTripPreservaContenidos(@TempDir Path tempDir) throws Exception {
        Partida original = Partida.crearPartidaNueva();
        original.getJugador().recibirDano(30);

        String ruta = tempDir.resolve("roundtrip.json").toString();
        original.guardar(ruta);

        Partida cargada = Partida.cargarPartida(ruta);
        assertNotNull(cargada);
        assertEquals(original.getJugador().getNombre(), cargada.getJugador().getNombre());
        assertEquals(original.getJugador().getVidaActual(), cargada.getJugador().getVidaActual());
        assertEquals(original.getEstado(), cargada.getEstado());
        assertEquals(original.getTurnosRestantes(), cargada.getTurnosRestantes());
    }

    @Test
    void guardarYCargarRoundTripPreservaEnemigosDeFabrica(@TempDir Path tempDir) throws Exception {
        Partida original = Partida.crearPartidaNueva();
        int enemigosFabrica = original.getEnemigos().getSize();
        assertTrue(enemigosFabrica > 0, "La partida de fabrica debe tener enemigos");

        String ruta = tempDir.resolve("roundtrip_fabrica.json").toString();
        original.guardar(ruta);

        Partida cargada = Partida.cargarPartida(ruta);
        assertEquals(enemigosFabrica, cargada.getEnemigos().getSize(),
            "Los enemigos de fabrica deben preservarse al cargar");
    }

    @Test
    void guardarYCargarRoundTripPreservaEstadisticas(@TempDir Path tempDir) throws Exception {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 0, 2, 1, 1);
        Partida original = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 0, 1, 1, 2);
        assertTrue(original.anadirEnemigo(cueva, enemigo));
        assertTrue(original.atacar(1, 2));
        assertTrue(original.pasarTurno());

        String ruta = tempDir.resolve("stats.json").toString();
        original.guardar(ruta);

        Partida cargada = Partida.cargarPartida(ruta);
        assertEquals(1, cargada.getEstadisticas().getTurnosJugados());
        assertEquals(15, cargada.getEstadisticas().getDanoEjercido());
        assertEquals(8, cargada.getEstadisticas().getDanoRecibido());
    }

    @Test
    void registrarDisparoBolaFuegoConsumeAccionPeroNoMovimiento() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);

        assertTrue(partida.puedeDispararBolaFuego());
        assertTrue(partida.registrarDisparoBolaFuego());
        assertTrue(partida.isAccionRealizada());
        assertFalse(partida.isMovimientoRealizado());
        assertFalse(partida.registrarDisparoBolaFuego());
        assertTrue(partida.moverJugador(1, 2));
    }

    @Test
    void impactarBolaFuegoAplicaDanoFijoYEstadisticas() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 0, 1, 1, 2);

        assertTrue(partida.anadirEnemigo(cueva, enemigo));
        ResultadoImpactoBolaFuego resultado = partida.impactarBolaFuego(1, 2, 10);

        assertTrue(resultado.hayImpacto());
        assertEquals("Esqueleto", resultado.getNombreEnemigo());
        assertEquals(10, resultado.getDanoReal());
        assertEquals(20, enemigo.getVidaActual());
        assertEquals(10, partida.getEstadisticas().getDanoEjercido());
        assertFalse(resultado.isEnemigoMuerto());
    }

    @Test
    void impactarBolaFuegoMataEnemigoComunYLoElimina() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 6, 8, 0, 1, 1, 2);

        assertTrue(partida.anadirEnemigo(cueva, enemigo));
        ResultadoImpactoBolaFuego resultado = partida.impactarBolaFuego(1, 2, 10);

        assertTrue(resultado.hayImpacto());
        assertTrue(resultado.isEnemigoMuerto());
        assertFalse(resultado.isBoss());
        assertEquals(6, resultado.getDanoReal());
        assertEquals(0, partida.getEnemigosActuales().getSize());
        assertEquals(1, partida.getEstadisticas().getEnemigosMuertos());
    }

    @Test
    void impactarBolaFuegoMataBossYMarcaMalakor() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Boss boss = new Boss("Malakor", 6, 20, 0, 1, 1, 2);

        assertTrue(partida.anadirEnemigo(cueva, boss));
        ResultadoImpactoBolaFuego resultado = partida.impactarBolaFuego(1, 2, 10);

        assertTrue(resultado.hayImpacto());
        assertTrue(resultado.isEnemigoMuerto());
        assertTrue(resultado.isBoss());
        assertEquals(1, partida.getEstadisticas().getBossesMuertos());
        assertTrue(partida.getEstadisticas().isMalakorDerrotado());
        assertTrue(partida.tieneLlaveFinal());
    }

    @Test
    void impactarBolaFuegoSinEnemigoNoModificaEstadisticas() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);

        ResultadoImpactoBolaFuego resultado = partida.impactarBolaFuego(0, 0, 10);

        assertFalse(resultado.hayImpacto());
        assertEquals(0, partida.getEstadisticas().getDanoEjercido());
        assertEquals(0, partida.getEstadisticas().getEnemigosMuertos());
    }

    @Test
    void abrirTesoroFacilAnadePocionAlInventario() {
        Cueva cueva = new Cueva("cueva_facil", 3, 3);
        cueva.cambiarTipoCelda(1, 1, TipoCelda.TESORO);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);

        assertTrue(partida.abrirTesoro());

        assertEquals(TipoCelda.SUELO, partida.getCuevaActual().getCelda(1, 1).getTipo());
        assertEquals(1, jugador.getCantidadObjetosInventario());
        assertInstanceOf(Pocion.class, jugador.getInventario().get(0));
        assertFalse(partida.abrirTesoro());
    }

    @Test
    void abrirTesoroMedioAnadeEscudoYTesoroDificilAnadeEspada() {
        Cueva cuevaMedia = new Cueva("cueva_media", 3, 3);
        cuevaMedia.cambiarTipoCelda(1, 1, TipoCelda.TESORO);
        Jugador jugadorMedia = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partidaMedia = new Partida(mazmorraCon(cuevaMedia), jugadorMedia, 10);

        assertTrue(partidaMedia.abrirTesoro());
        assertInstanceOf(Escudo.class, jugadorMedia.getInventario().get(0));

        Cueva cuevaDificil = new Cueva("cueva_dificil", 3, 3);
        cuevaDificil.cambiarTipoCelda(1, 1, TipoCelda.TESORO);
        Jugador jugadorDificil = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partidaDificil = new Partida(mazmorraCon(cuevaDificil), jugadorDificil, 10);

        assertTrue(partidaDificil.abrirTesoro());
        assertInstanceOf(Espada.class, jugadorDificil.getInventario().get(0));
    }

    @Test
    void registrarDisparoBolaHieloConsumeAccionPeroNoMovimiento() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);

        assertTrue(partida.puedeDispararBolaHielo());
        assertTrue(partida.registrarDisparoBolaHielo());
        assertTrue(partida.isAccionRealizada());
        assertFalse(partida.isMovimientoRealizado());
        assertFalse(partida.registrarDisparoBolaHielo());
        assertTrue(partida.moverJugador(1, 2));
    }

    @Test
    void impactarBolaHieloCongelaTresTurnosSinDano() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 0, 1, 1, 2);

        assertTrue(partida.anadirEnemigo(cueva, enemigo));
        ResultadoImpactoBolaHielo resultado = partida.impactarBolaHielo(1, 2);

        assertTrue(resultado.hayImpacto());
        assertEquals(3, resultado.getTurnosCongelado());
        assertEquals(30, enemigo.getVidaActual());
        assertEquals(0, partida.getEstadisticas().getDanoEjercido());
    }

    @Test
    void enemigoCongeladoPierdeTresTurnosYLuegoActua() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 0, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 0, 1, 1, 2);
        assertTrue(partida.anadirEnemigo(cueva, enemigo));
        assertTrue(partida.impactarBolaHielo(1, 2).hayImpacto());

        assertTrue(partida.pasarTurno());
        assertTrue(partida.pasarTurno());
        assertTrue(partida.pasarTurno());
        assertEquals(100, jugador.getVidaActual());
        assertEquals(0, enemigo.getTurnosCongelado());

        assertTrue(partida.pasarTurno());
        assertEquals(92, jugador.getVidaActual());
    }

    @Test
    void guardarYCargarPreservaCongelacion(@TempDir Path tempDir) throws Exception {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 1, 1);
        Partida original = new Partida(mazmorra, jugador, 10);
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 0, 1, 1, 2);
        assertTrue(original.anadirEnemigo(cueva, enemigo));
        assertTrue(original.impactarBolaHielo(1, 2).hayImpacto());

        String ruta = tempDir.resolve("hielo.json").toString();
        original.guardar(ruta);

        Partida cargada = Partida.cargarPartida(ruta);
        assertEquals(3, cargada.getEnemigosActuales().get(0).getTurnosCongelado());
    }

    @Test
    void arqueroDisparaEnLineaRectaConProyectilPendiente() {
        Cueva cueva = new Cueva("c1", 3, 7);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 0, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo arquero = new Enemigo("Arquero", TipoEnemigo.ARQUERO, 18, 6, 1, 1, 1, 5);
        assertTrue(partida.anadirEnemigo(cueva, arquero));

        assertTrue(partida.pasarTurno());

        assertEquals(94, jugador.getVidaActual());
        ListaSE<DisparoEnemigo> disparos = partida.consumirDisparosEnemigosPendientes();
        assertEquals(1, disparos.getSize());
        assertEquals(1, disparos.get(0).getFilaOrigen());
        assertEquals(5, disparos.get(0).getColumnaOrigen());
    }

    @Test
    void arqueroNoDisparaSiUnMuroBloqueaLaLinea() {
        Cueva cueva = new Cueva("c1", 3, 7);
        cueva.cambiarTipoCelda(1, 3, TipoCelda.MURO);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 0, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo arquero = new Enemigo("Arquero", TipoEnemigo.ARQUERO, 18, 6, 1, 1, 1, 5);
        assertTrue(partida.anadirEnemigo(cueva, arquero));

        assertTrue(partida.pasarTurno());

        assertEquals(100, jugador.getVidaActual());
        assertEquals(0, partida.consumirDisparosEnemigosPendientes().getSize());
    }

    @Test
    void arqueroCongeladoNoDisparaNiSeMueve() {
        Cueva cueva = new Cueva("c1", 3, 7);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 15, 0, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Enemigo arquero = new Enemigo("Arquero", TipoEnemigo.ARQUERO, 18, 6, 1, 1, 1, 5);
        assertTrue(partida.anadirEnemigo(cueva, arquero));
        assertTrue(partida.impactarBolaHielo(1, 5).hayImpacto());

        assertTrue(partida.pasarTurno());

        assertEquals(100, jugador.getVidaActual());
        assertEquals(1, arquero.getFila());
        assertEquals(5, arquero.getColumna());
        assertEquals(0, partida.consumirDisparosEnemigosPendientes().getSize());
    }

    private Mazmorra mazmorraCon(Cueva cueva) {
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.addCueva(cueva);
        return mazmorra;
    }
}
