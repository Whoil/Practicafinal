package modelo.personajes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonajeTest {
    @Test
    void crearJugadorGuardaAtributosBase() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 2, 4);

        assertInstanceOf(Personaje.class, jugador);
        assertEquals("Guillermo", jugador.getNombre());
        assertEquals(100, jugador.getVidaMaxima());
        assertEquals(15, jugador.getAtaqueBase());
        assertEquals(5, jugador.getDefensaBase());
        assertEquals(3, jugador.getMovimiento());
        assertEquals(2, jugador.getFila());
        assertEquals(4, jugador.getColumna());
    }

    @Test
    void crearJugadorInicializaVidaActualConVidaMaxima() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 0, 0);

        assertEquals(100, jugador.getVidaActual());
    }

    @Test
    void estaVivoDevuelveTrueMientrasTieneVida() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 0, 0);

        assertTrue(jugador.estaVivo());
    }

    @Test
    void recibirDanoReduceVida() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 0, 0);

        jugador.recibirDano(25);

        assertEquals(75, jugador.getVidaActual());
    }

    @Test
    void recibirDanoNoPermiteVidaNegativa() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 0, 0);

        jugador.recibirDano(150);

        assertEquals(0, jugador.getVidaActual());
    }

    @Test
    void recibirDanoCeroNoCambiaVida() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 0, 0);

        jugador.recibirDano(0);

        assertEquals(100, jugador.getVidaActual());
    }

    @Test
    void curarAumentaVida() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 0, 0);
        jugador.recibirDano(40);

        jugador.curar(25);

        assertEquals(85, jugador.getVidaActual());
    }

    @Test
    void curarNoSuperaVidaMaxima() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 0, 0);
        jugador.recibirDano(10);

        jugador.curar(50);

        assertEquals(100, jugador.getVidaActual());
    }

    @Test
    void curarCeroNoCambiaVida() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 0, 0);
        jugador.recibirDano(10);

        jugador.curar(0);

        assertEquals(90, jugador.getVidaActual());
    }

    @Test
    void estaVivoDevuelveFalseCuandoVidaEsCero() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 0, 0);

        jugador.recibirDano(100);

        assertFalse(jugador.estaVivo());
    }

    @Test
    void cambiarPosicionActualizaFilaYColumna() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 0, 0);

        jugador.cambiarPosicion(3, 2);

        assertEquals(3, jugador.getFila());
        assertEquals(2, jugador.getColumna());
    }

    @Test
    void cambiarPosicionRechazaFilaNegativa() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> jugador.cambiarPosicion(-1, 0));
    }

    @Test
    void cambiarPosicionRechazaColumnaNegativa() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> jugador.cambiarPosicion(0, -1));
    }

    @Test
    void constructorRechazaNombreVacio() {
        assertThrows(IllegalArgumentException.class, () -> new Jugador("", 100, 15, 5, 3, 0, 0));
    }

    @Test
    void constructorRechazaNombreConSoloEspacios() {
        assertThrows(IllegalArgumentException.class, () -> new Jugador("   ", 100, 15, 5, 3, 0, 0));
    }

    @Test
    void constructorRechazaNombreNull() {
        assertThrows(IllegalArgumentException.class, () -> new Jugador(null, 100, 15, 5, 3, 0, 0));
    }

    @Test
    void constructorRechazaVidaMaximaNoPositiva() {
        assertThrows(IllegalArgumentException.class, () -> new Jugador("Guillermo", 0, 15, 5, 3, 0, 0));
    }

    @Test
    void constructorRechazaValoresNegativos() {
        assertThrows(IllegalArgumentException.class, () -> new Jugador("Guillermo", 100, -1, 5, 3, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new Jugador("Guillermo", 100, 15, -1, 3, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new Jugador("Guillermo", 100, 15, 5, -1, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new Jugador("Guillermo", 100, 15, 5, 3, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> new Jugador("Guillermo", 100, 15, 5, 3, 0, -1));
    }

    @Test
    void recibirDanoRechazaCantidadNegativa() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> jugador.recibirDano(-1));
    }

    @Test
    void curarRechazaCantidadNegativa() {
        Jugador jugador = new Jugador("Guillermo", 100, 15, 5, 3, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> jugador.curar(-1));
    }
}
