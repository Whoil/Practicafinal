package modelo.juego;

import Estructuras.ListaSE;
import modelo.mapa.Cueva;
import modelo.mapa.TipoCelda;
import modelo.objetos.Arco;
import modelo.objetos.Llave;
import modelo.objetos.Pocion;
import modelo.objetos.TipoLlave;
import modelo.personajes.Boss;
import modelo.personajes.Enemigo;
import modelo.personajes.Jugador;
import modelo.personajes.TipoEnemigo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {

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
    void puertaAbiertaPermiteAvanzarSinLlave() {
        Cueva origen = new Cueva("origen", 2, 2);
        Cueva destino = new Cueva("destino", 2, 2);
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.conectarCuevas(origen, destino, "p1");
        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 2, 0, 0);
        Puerta puerta = new Puerta("p1", origen, destino, "llave-p1");
        puerta.abrir();
        ListaSE<Puerta> puertas = new ListaSE<>();
        puertas.addLast(puerta);
        Partida partida = new Partida(mazmorra, jugador, 10, puertas);

        assertTrue(partida.avanzarACueva("destino"));
        assertEquals(destino.getId(), partida.getCuevaActual().getId());
    }

    @Test
    void avanzarACuevaNoPermiteEntrarSobreEnemigo() {
        Cueva origen = new Cueva("origen", 3, 3);
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
        assertFalse(partida.avanzarACueva("destino"));
        assertEquals(origen.getId(), partida.getCuevaActual().getId());
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

    private Mazmorra mazmorraCon(Cueva cueva) {
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.addCueva(cueva);
        return mazmorra;
    }
}
