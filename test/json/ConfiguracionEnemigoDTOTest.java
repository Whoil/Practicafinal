package json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguracionEnemigoDTOTest {

    @Test
    void gettersDevuelvenValoresPorDefecto() {
        ConfiguracionEnemigoDTO dto = new ConfiguracionEnemigoDTO();
        assertNull(dto.getTipo());
        assertNull(dto.getIdCueva());
        assertEquals(0, dto.getFila());
        assertEquals(0, dto.getColumna());
        assertEquals(0, dto.getVida());
        assertEquals(0, dto.getAtaque());
        assertEquals(0, dto.getDefensa());
        assertEquals(0, dto.getMovimiento());
    }

    @Test
    void setIdCuevaModificaElValor() {
        ConfiguracionEnemigoDTO dto = new ConfiguracionEnemigoDTO();
        dto.setIdCueva("cueva_facil");
        assertEquals("cueva_facil", dto.getIdCueva());
    }
}
