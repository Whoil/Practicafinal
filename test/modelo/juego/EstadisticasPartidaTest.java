package modelo.juego;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadisticasPartidaTest {

    @Test
    void calculaPuntuacionConVictoriaYDerrota() {
        EstadisticasPartida stats = new EstadisticasPartida();
        stats.registrarEnemigoComunMuerto();
        stats.registrarBossMuerto();
        stats.registrarDanoEjercido(200);
        stats.registrarDanoRecibido(50);
        stats.registrarTurnoJugado();
        stats.registrarTurnoJugado();

        assertEquals(690, stats.calcularPuntuacion(false));
        assertEquals(1690, stats.calcularPuntuacion(true));
    }

    @Test
    void asignaRangosEnLosLimites() {
        assertEquals("Escudero Novato", statsConDanoRecibido(1).calcularTitulo(false));
        assertEquals("Hidalgo Aventurero", new EstadisticasPartida().calcularTitulo(false));
        assertEquals("Hidalgo Aventurero", statsConDanoEjercido(799).calcularTitulo(false));
        assertEquals("Caballero de la Orden", statsConDanoEjercido(800).calcularTitulo(false));
        assertEquals("Caballero de la Orden", statsConDanoEjercido(1499).calcularTitulo(false));
        assertEquals("Paladín del Reino", statsConDanoEjercido(1500).calcularTitulo(false));
        assertEquals("Paladín del Reino", statsConDanoEjercido(2499).calcularTitulo(false));
        assertEquals("Maestro Hechicero", statsConDanoEjercido(2500).calcularTitulo(false));
    }

    @Test
    void concatenaEpitetoSiMalakorFueDerrotado() {
        EstadisticasPartida stats = new EstadisticasPartida();
        stats.registrarBossMuerto();

        assertTrue(stats.calcularTitulo(false).endsWith("(Salvador del Reino)"));
    }

    private EstadisticasPartida statsConDanoEjercido(int dano) {
        EstadisticasPartida stats = new EstadisticasPartida();
        stats.registrarDanoEjercido(dano);
        return stats;
    }

    private EstadisticasPartida statsConDanoRecibido(int dano) {
        EstadisticasPartida stats = new EstadisticasPartida();
        stats.registrarDanoRecibido(dano);
        return stats;
    }
}
