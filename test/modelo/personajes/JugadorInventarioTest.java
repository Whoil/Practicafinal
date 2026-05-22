package modelo.personajes;

import modelo.objetos.Arco;
import modelo.objetos.Escudo;
import modelo.objetos.Espada;
import modelo.objetos.Llave;
import modelo.objetos.Pocion;
import modelo.objetos.TipoLlave;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JugadorInventarioTest {

    @Test
    void inventarioEmpiezaVacioYPermiteObjetosDelMismoTipoConDistintoId() {
        Jugador jugador = crearJugador();
        Pocion primera = new Pocion("pocion-1");
        Pocion segunda = new Pocion("pocion-2");

        jugador.agregarObjeto(primera);
        jugador.agregarObjeto(segunda);

        assertEquals(2, jugador.getCantidadObjetosInventario());
        assertTrue(jugador.tieneObjeto(primera));
        assertTrue(jugador.tieneObjetoConId("pocion-2"));
    }

    @Test
    void noPermiteDuplicarElMismoId() {
        Jugador jugador = crearJugador();

        jugador.agregarObjeto(new Pocion("pocion-1"));

        assertThrows(IllegalArgumentException.class, () -> jugador.agregarObjeto(new Pocion("pocion-1")));
    }

    @Test
    void getInventarioDevuelveCopiaDefensiva() {
        Jugador jugador = crearJugador();
        Pocion pocion = new Pocion("pocion-1");
        jugador.agregarObjeto(pocion);

        jugador.getInventario().clear();

        assertEquals(1, jugador.getCantidadObjetosInventario());
        assertTrue(jugador.tieneObjeto(pocion));
    }

    @Test
    void equiparEspadaYEscudoSumaAtaqueYDefensa() {
        Jugador jugador = crearJugador();
        Espada espada = new Espada("espada-1");
        Escudo escudo = new Escudo("escudo-1");
        jugador.agregarObjeto(espada);
        jugador.agregarObjeto(escudo);

        assertTrue(jugador.equiparArma(espada));
        assertTrue(jugador.equiparEscudo(escudo));

        assertSame(espada, jugador.getArmaEquipada());
        assertSame(escudo, jugador.getEscudoEquipado());
        assertEquals(27, jugador.getAtaqueTotal());
        assertEquals(10, jugador.getDefensaTotal());
    }

    @Test
    void equiparArcoDesequipaEscudoYBloqueaEquiparEscudo() {
        Jugador jugador = crearJugador();
        Espada espada = new Espada("espada-1");
        Arco arco = new Arco("arco-1");
        Escudo escudo = new Escudo("escudo-1");
        jugador.agregarObjeto(espada);
        jugador.agregarObjeto(arco);
        jugador.agregarObjeto(escudo);
        jugador.equiparArma(espada);
        jugador.equiparEscudo(escudo);

        assertTrue(jugador.equiparArma(arco));

        assertSame(arco, jugador.getArmaEquipada());
        assertNull(jugador.getEscudoEquipado());
        assertEquals(22, jugador.getAtaqueTotal());
        assertEquals(5, jugador.getDefensaTotal());
        assertFalse(jugador.equiparEscudo(escudo));
    }

    @Test
    void noEquipaObjetosAusentesYDestaquiparDevuelveEstado() {
        Jugador jugador = crearJugador();
        Espada espada = new Espada("espada-1");
        Escudo escudo = new Escudo("escudo-1");

        assertFalse(jugador.equiparArma(espada));
        assertFalse(jugador.equiparEscudo(escudo));
        assertFalse(jugador.desequiparArma());
        assertFalse(jugador.desequiparEscudo());

        jugador.agregarObjeto(espada);
        jugador.agregarObjeto(escudo);
        jugador.equiparArma(espada);
        jugador.equiparEscudo(escudo);

        assertTrue(jugador.desequiparArma());
        assertTrue(jugador.desequiparEscudo());
        assertNull(jugador.getArmaEquipada());
        assertNull(jugador.getEscudoEquipado());
    }

    @Test
    void quitarObjetoEquipadoTambienLoDesequipa() {
        Jugador jugador = crearJugador();
        Espada espada = new Espada("espada-1");
        jugador.agregarObjeto(espada);
        jugador.equiparArma(espada);

        assertSame(espada, jugador.quitarObjeto(espada));

        assertNull(jugador.getArmaEquipada());
        assertEquals(0, jugador.getCantidadObjetosInventario());
    }

    @Test
    void usarPocionCuraYLaConsume() {
        Jugador jugador = crearJugador();
        Pocion pocion = new Pocion("pocion-1");
        jugador.agregarObjeto(pocion);
        jugador.recibirDano(40);

        assertTrue(jugador.usarPocion(pocion));

        assertEquals(85, jugador.getVidaActual());
        assertEquals(0, jugador.getCantidadObjetosInventario());
    }

    @Test
    void usarPocionAusenteDevuelveFalse() {
        Jugador jugador = crearJugador();

        assertFalse(jugador.usarPocion(new Pocion("pocion-1")));
    }

    @Test
    void operacionesConMismoIdPeroTipoDistintoNoProvocanCastIncorrecto() {
        Jugador jugador = crearJugador();
        Espada espada = new Espada("objeto-1");
        jugador.agregarObjeto(espada);

        assertFalse(jugador.usarPocion(new Pocion("objeto-1")));
        assertFalse(jugador.equiparEscudo(new Escudo("objeto-1")));
        assertSame(espada, jugador.getInventario().get(espada));
    }

    @Test
    void llavesSeGuardanPeroNoSeEquipan() {
        Jugador jugador = crearJugador();
        Llave llave = new Llave("llave-1", TipoLlave.PUERTA, "puerta-media");

        jugador.agregarObjeto(llave);

        assertTrue(jugador.tieneObjetoConId("llave-1"));
        assertEquals(1, jugador.getCantidadObjetosInventario());
    }

    @Test
    void validaArgumentosInvalidosEnInventario() {
        Jugador jugador = crearJugador();

        assertThrows(IllegalArgumentException.class, () -> jugador.agregarObjeto(null));
        assertThrows(IllegalArgumentException.class, () -> jugador.tieneObjeto(null));
        assertThrows(IllegalArgumentException.class, () -> jugador.quitarObjeto(null));
        assertThrows(IllegalArgumentException.class, () -> jugador.equiparArma(null));
        assertThrows(IllegalArgumentException.class, () -> jugador.equiparEscudo(null));
        assertThrows(IllegalArgumentException.class, () -> jugador.usarPocion(null));
        assertThrows(IllegalArgumentException.class, () -> jugador.tieneObjetoConId(" "));
    }

    private Jugador crearJugador() {
        return new Jugador("Guille", 100, 15, 5, 3, 0, 0);
    }
}
