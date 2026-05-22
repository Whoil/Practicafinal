package modelo.juego;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import Estructuras.ListaDE;
import Estructuras.ListaSE;
import Estructuras.MiIterador;
import modelo.mapa.Cueva;
import modelo.mapa.TipoCelda;
import modelo.objetos.Arco;
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
        assertEquals(40, p.getTurnosRestantes());
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
    void moverJugadorArribaFunciona() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        Jugador j = p.getJugador();
        p.moverJugador(j.getFila() + 1, j.getColumna());
        p.terminarTurno();
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
        while (e.estaVivo() && p.getEstado() == EstadoPartida.EN_CURSO) {
            p.atacar();
            if (e.estaVivo()) {
                p.terminarTurno();
            }
        }
        assertFalse(e.estaVivo());
        assertEquals(0, p.getEnemigosActuales().getSize());
    }

    @Test
    void recogerObjetoEnLaMismaCasilla() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        Jugador j = p.getJugador();
        assertTrue(p.moverJugador(1, 3));
        p.terminarTurno();
        assertTrue(p.moverJugador(2, 3));
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
        assertEquals(EstadoPartida.DERROTA, p.getEstado());
    }

    @Test
    void turnosAgotadosCambianEstadoADerrota() throws Exception {
        Partida p = Partida.crearPartidaNueva();
        for (int i = 0; i < 40; i++) {
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

        assertTrue(partida.moverJugador(0, 2));
        assertEquals(0, jugador.getFila());
        assertEquals(2, jugador.getColumna());
        assertFalse(partida.moverJugador(1, 2));
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
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 30, 5, 2, 1, 1);
        Partida partida = new Partida(mazmorra, jugador, 10);
        Boss boss = new Boss("Boss", 10, 20, 0, 1, 2, 2);

        assertTrue(partida.anadirEnemigo(cueva, boss));
        assertTrue(partida.atacar(2, 2));

        assertEquals(EstadoPartida.VICTORIA, partida.getEstado());
        assertTrue(partida.tieneLlaveFinal());
    }

    @Test
    void llaveFinalNoFallaSiExisteOtroObjetoConElIdPorDefecto() {
        Cueva cueva = new Cueva("c1", 3, 3);
        Mazmorra mazmorra = mazmorraCon(cueva);
        Jugador jugador = new Jugador("Heroe", 100, 30, 5, 2, 1, 1);
        jugador.agregarObjeto(new Llave(Partida.ID_LLAVE_FINAL_DEFECTO, TipoLlave.PUERTA, "otra-cerradura"));
        Partida partida = new Partida(mazmorra, jugador, 10);
        Boss boss = new Boss("Boss", 10, 20, 0, 1, 2, 2);

        assertTrue(partida.anadirEnemigo(cueva, boss));
        assertTrue(partida.atacar(2, 2));

        assertEquals(EstadoPartida.VICTORIA, partida.getEstado());
        assertTrue(partida.tieneLlaveFinal());
        assertEquals(2, jugador.getCantidadObjetosInventario());
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

    private Mazmorra mazmorraCon(Cueva cueva) {
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.addCueva(cueva);
        return mazmorra;
    }
}
