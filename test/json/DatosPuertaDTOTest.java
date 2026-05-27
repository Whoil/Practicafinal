package json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatosPuertaDTOTest {

    @Test
    void constructorVacioInicializaCorrectamente() {
        DatosPuertaDTO dto = new DatosPuertaDTO();
        assertNull(dto.getId());
        assertNull(dto.getOrigen());
        assertNull(dto.getDestino());
        assertNull(dto.getCodigoLlave());
        assertFalse(dto.isAbierta());
    }

    @Test
    void constructorConParametrosAsignaValores() {
        DatosPuertaDTO dto = new DatosPuertaDTO("p1", "cueva_facil", "cueva_media", "llave_plata", true);
        assertEquals("p1", dto.getId());
        assertEquals("cueva_facil", dto.getOrigen());
        assertEquals("cueva_media", dto.getDestino());
        assertEquals("llave_plata", dto.getCodigoLlave());
        assertTrue(dto.isAbierta());
    }

    @Test
    void puertaCerradaPorDefecto() {
        DatosPuertaDTO dto = new DatosPuertaDTO("p2", "a", "b", null, false);
        assertFalse(dto.isAbierta());
        assertNull(dto.getCodigoLlave());
    }
}
