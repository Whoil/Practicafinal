package vista;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatosTemaCuevaTest {

    @Test
    void criptasTieneIdFacil() {
        assertEquals("cueva_facil", DatosTemaCueva.CRIPTAS.getCuevaId());
    }

    @Test
    void paramoTieneIdMedia() {
        assertEquals("cueva_media", DatosTemaCueva.PARAMO.getCuevaId());
    }

    @Test
    void abismoTieneIdDificil() {
        assertEquals("cueva_dificil", DatosTemaCueva.ABISMO.getCuevaId());
    }

    @Test
    void paraCuevaIdDevuelveCriptasPorDefecto() {
        assertEquals(DatosTemaCueva.CRIPTAS, DatosTemaCueva.paraCuevaId("cueva_facil"));
    }

    @Test
    void paraCuevaIdDevuelveParamo() {
        assertEquals(DatosTemaCueva.PARAMO, DatosTemaCueva.paraCuevaId("cueva_media"));
    }

    @Test
    void paraCuevaIdDevuelveAbismo() {
        assertEquals(DatosTemaCueva.ABISMO, DatosTemaCueva.paraCuevaId("cueva_dificil"));
    }

    @Test
    void paraCuevaIdDesconocidaDevuelveCriptas() {
        assertEquals(DatosTemaCueva.CRIPTAS, DatosTemaCueva.paraCuevaId("no_existe"));
    }

    @Test
    void valoresNoSonNulos() {
        for (DatosTemaCueva tema : DatosTemaCueva.values()) {
            assertNotNull(tema.getCuevaId());
            assertNotNull(tema.getTitulo());
            assertNotNull(tema.getTexto());
            assertNotNull(tema.getFondoCSS());
            assertNotNull(tema.getColorMuro());
            assertNotNull(tema.getAssetEnemigo());
            assertNotNull(tema.getAssetBoss());
            assertNotNull(tema.getEmojiDecoracion());
        }
    }

    @Test
    void titulosSonDistintos() {
        assertNotEquals(DatosTemaCueva.CRIPTAS.getTitulo(), DatosTemaCueva.PARAMO.getTitulo());
        assertNotEquals(DatosTemaCueva.PARAMO.getTitulo(), DatosTemaCueva.ABISMO.getTitulo());
    }
}
