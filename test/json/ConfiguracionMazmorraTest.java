package json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguracionMazmorraTest {

    @Test
    void getNombreDevuelveNullPorDefecto() {
        ConfiguracionMazmorra config = new ConfiguracionMazmorra();
        assertNull(config.getNombre());
    }

    @Test
    void getCuevasDevuelveNullPorDefecto() {
        ConfiguracionMazmorra config = new ConfiguracionMazmorra();
        assertNull(config.getCuevas());
    }

    @Test
    void getConexionesDevuelveNullPorDefecto() {
        ConfiguracionMazmorra config = new ConfiguracionMazmorra();
        assertNull(config.getConexiones());
    }
}
