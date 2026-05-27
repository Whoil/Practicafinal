package json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguracionCuevaDTOTest {

    @Test
    void gettersDevuelvenValoresPorDefecto() {
        ConfiguracionCuevaDTO dto = new ConfiguracionCuevaDTO();
        assertNull(dto.getId());
        assertNull(dto.getNombre());
        assertNull(dto.getDificultad());
        assertEquals(0, dto.getFilas());
        assertEquals(0, dto.getColumnas());
        assertNull(dto.getMatriz());
        assertNull(dto.getEnemigos());
        assertNull(dto.getObjetos());
    }
}
