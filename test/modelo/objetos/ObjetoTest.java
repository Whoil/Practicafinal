package modelo.objetos;

import modelo.personajes.Jugador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjetoTest {

    @Test
    void armasYEscudoTienenBonificacionesAcordadas() {
        Espada espada = new Espada("espada-1");
        Arco arco = new Arco("arco-1");
        Escudo escudo = new Escudo("escudo-1");

        assertEquals(12, espada.getBonificacionAtaque());
        assertEquals(7, arco.getBonificacionAtaque());
        assertEquals(5, escudo.getBonificacionDefensa());
        assertTrue(espada.esEquipable());
        assertTrue(arco.esEquipable());
        assertTrue(escudo.esEquipable());
    }

    @Test
    void pocionCuraSinSuperarVidaMaximaYSeConsume() {
        Jugador jugador = new Jugador("Guille", 100, 15, 5, 3, 0, 0);
        Pocion pocion = new Pocion("pocion-1");
        jugador.recibirDano(10);

        assertTrue(pocion.usarSobre(jugador));

        assertEquals(100, jugador.getVidaActual());
        assertTrue(pocion.esConsumible());
        assertEquals(25, pocion.getPuntosCuracion());
    }

    @Test
    void llaveGuardaTipoYCodigoPeroNoEsEquipable() {
        Llave llave = new Llave("llave-1", TipoLlave.PUERTA, "puerta-media");
        Llave llaveCofre = new Llave("llave-2", TipoLlave.COFRE, "cofre-1");

        assertEquals(TipoLlave.PUERTA, llave.getTipoLlave());
        assertEquals("puerta-media", llave.getCodigoCerradura());
        assertEquals(TipoLlave.COFRE, llaveCofre.getTipoLlave());
        assertEquals("cofre-1", llaveCofre.getCodigoCerradura());
        assertFalse(llave.esEquipable());
        assertFalse(llave.esConsumible());
    }

    @Test
    void objetosComparanPorId() {
        Pocion una = new Pocion("pocion-1");
        Pocion otra = new Pocion("pocion-1");
        Pocion distinta = new Pocion("pocion-2");

        assertEquals(una, otra);
        assertEquals(una.hashCode(), otra.hashCode());
        assertNotEquals(una, distinta);
        assertEquals(una, una);
        assertNotEquals(una, null);
        assertNotEquals(una, "pocion-1");
        assertEquals("Pocion de cura", una.toString());
    }

    @Test
    void objetoBaseExponeDatosYNoEsEquipableNiConsumiblePorDefecto() {
        ObjetoPrueba objeto = new ObjetoPrueba("objeto-1", "Objeto de prueba", "Descripcion");

        assertEquals("objeto-1", objeto.getId());
        assertEquals("Objeto de prueba", objeto.getNombre());
        assertEquals("Descripcion", objeto.getDescripcion());
        assertFalse(objeto.esEquipable());
        assertFalse(objeto.esConsumible());
    }

    @Test
    void validaDatosObligatoriosYValoresInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new Pocion(""));
        assertThrows(IllegalArgumentException.class, () -> new Pocion("pocion", 0));
        assertThrows(IllegalArgumentException.class, () -> new Espada(null));
        assertThrows(IllegalArgumentException.class, () -> new ObjetoPrueba("objeto", "", "Descripcion"));
        assertThrows(IllegalArgumentException.class, () -> new ObjetoPrueba("objeto", "Nombre", null));
        assertThrows(IllegalArgumentException.class, () -> new Llave("llave", null, "puerta"));
        assertThrows(IllegalArgumentException.class, () -> new Llave("llave", TipoLlave.COFRE, " "));
        assertThrows(IllegalArgumentException.class, () -> new ArmaPrueba("arma", -1));
        assertThrows(IllegalArgumentException.class, () -> new Pocion("pocion").usarSobre(null));
    }

    private static class ArmaPrueba extends Arma {
        private ArmaPrueba(String id, int bonificacionAtaque) {
            super(id, "Arma de prueba", "Solo para validar errores", bonificacionAtaque);
        }
    }

    private static class ObjetoPrueba extends Objeto {
        private ObjetoPrueba(String id, String nombre, String descripcion) {
            super(id, nombre, descripcion);
        }
    }
}
