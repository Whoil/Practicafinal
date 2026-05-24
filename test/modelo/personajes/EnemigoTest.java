package modelo.personajes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnemigoTest {
    @Test
    void enemigoGuardaTipoEnemigo() {
        Enemigo enemigo = new Enemigo("Esqueleto", TipoEnemigo.ESQUELETO, 30, 8, 2, 2, 1, 1);

        assertInstanceOf(Personaje.class, enemigo);
        assertEquals(TipoEnemigo.ESQUELETO, enemigo.getTipoEnemigo());
        assertEquals("Esqueleto", enemigo.getNombre());
        assertEquals(30, enemigo.getVidaMaxima());
        assertEquals(30, enemigo.getVidaActual());
        assertEquals(8, enemigo.getAtaqueBase());
        assertEquals(2, enemigo.getDefensaBase());
        assertEquals(2, enemigo.getMovimiento());
        assertEquals(1, enemigo.getFila());
        assertEquals(1, enemigo.getColumna());
    }

    @Test
    void enemigoPermiteTiposAcordados() {
        assertEquals(TipoEnemigo.ORCO,
                new Enemigo("Orco", TipoEnemigo.ORCO, 50, 12, 4, 1, 2, 3).getTipoEnemigo());
        assertEquals(TipoEnemigo.MAGO,
                new Enemigo("Mago", TipoEnemigo.MAGO, 25, 14, 1, 2, 3, 4).getTipoEnemigo());
        assertEquals(TipoEnemigo.ARQUERO,
                new Enemigo("Arquero", TipoEnemigo.ARQUERO, 18, 6, 1, 1, 3, 4).getTipoEnemigo());
    }

    @Test
    void enemigoGestionaCongelacion() {
        Enemigo enemigo = new Enemigo("Arquero", TipoEnemigo.ARQUERO, 18, 6, 1, 1, 1, 1);

        enemigo.congelar(3);
        assertEquals(3, enemigo.getTurnosCongelado());
        enemigo.consumirTurnoCongelado();

        assertEquals(2, enemigo.getTurnosCongelado());
    }

    @Test
    void enemigoRechazaTipoNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Enemigo("Enemigo", null, 30, 8, 2, 2, 1, 1));
    }

    @Test
    void bossTieneTipoBoss() {
        Boss boss = new Boss("Boss final", 150, 20, 5, 2, 6, 6);

        assertEquals(TipoEnemigo.BOSS, boss.getTipoEnemigo());
        assertEquals("Boss final", boss.getNombre());
        assertEquals(150, boss.getVidaMaxima());
        assertEquals(20, boss.getAtaqueBase());
        assertEquals(5, boss.getDefensaBase());
        assertEquals(2, boss.getMovimiento());
        assertEquals(6, boss.getFila());
        assertEquals(6, boss.getColumna());
    }

    @Test
    void bossEsUnEnemigo() {
        Boss boss = new Boss("Boss final", 150, 20, 5, 2, 6, 6);

        assertInstanceOf(Enemigo.class, boss);
    }

    @Test
    void enumTipoEnemigoContieneTodosLosTiposAcordados() {
        TipoEnemigo[] tipos = TipoEnemigo.values();

        assertEquals(5, tipos.length);
        assertEquals(TipoEnemigo.ESQUELETO, TipoEnemigo.valueOf("ESQUELETO"));
        assertEquals(TipoEnemigo.ORCO, TipoEnemigo.valueOf("ORCO"));
        assertEquals(TipoEnemigo.MAGO, TipoEnemigo.valueOf("MAGO"));
        assertEquals(TipoEnemigo.ARQUERO, TipoEnemigo.valueOf("ARQUERO"));
        assertEquals(TipoEnemigo.BOSS, TipoEnemigo.valueOf("BOSS"));
    }
}
