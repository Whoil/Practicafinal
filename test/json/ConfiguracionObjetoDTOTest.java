package json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguracionObjetoDTOTest {

    @Test
    void gettersDevuelvenValoresPorDefecto() {
        ConfiguracionObjetoDTO dto = new ConfiguracionObjetoDTO();
        assertNull(dto.getIdCueva());
        assertNull(dto.getId());
        assertNull(dto.getTipo());
        assertEquals(0, dto.getFila());
        assertEquals(0, dto.getColumna());
        assertNull(dto.getNombre());
        assertNull(dto.getDescripcion());
        assertNull(dto.getTipoLlave());
        assertNull(dto.getCodigoCerradura());
        assertEquals(0, dto.getCura());
        assertEquals(0, dto.getAtaque());
        assertEquals(0, dto.getDefensa());
    }

    @Test
    void setIdCuevaModificaElValor() {
        ConfiguracionObjetoDTO dto = new ConfiguracionObjetoDTO();
        dto.setIdCueva("cueva_media");
        assertEquals("cueva_media", dto.getIdCueva());
    }
}
