package json;

import Estructuras.ListaSE;
import modelo.juego.Mazmorra;
import modelo.mapa.Cueva;
import modelo.mapa.TipoCelda;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de carga del JSON de configuracion de la mazmorra.
 * Verifica que el fichero datos/cuevas.json se lee correctamente y
 * que los objetos del juego se construyen con los valores esperados.
 */
class CargadorConfiguracionTest {

    private static final String RUTA_JSON = "datos/cuevas.json";

    @Test
    void cargaTresCuevas() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        Mazmorra mazmorra = resultado.getMazmorra();
        assertNotNull(mazmorra);
        assertEquals(3, mazmorra.getNumeroCuevas());
    }

    @Test
    void cuevaFacilTieneTamano5x5() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        Cueva facil = resultado.getMazmorra().getCuevaPorId("cueva_facil");
        assertNotNull(facil);
        assertEquals(5, facil.getFilas());
        assertEquals(5, facil.getColumnas());
    }

    @Test
    void cuevaMediaTieneTamano6x6() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        Cueva media = resultado.getMazmorra().getCuevaPorId("cueva_media");
        assertNotNull(media);
        assertEquals(6, media.getFilas());
        assertEquals(6, media.getColumnas());
    }

    @Test
    void cuevaDificilTieneTamano7x7() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        Cueva dificil = resultado.getMazmorra().getCuevaPorId("cueva_dificil");
        assertNotNull(dificil);
        assertEquals(7, dificil.getFilas());
        assertEquals(7, dificil.getColumnas());
    }

    @Test
    void celdaInicioEsTransitable() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        Cueva facil = resultado.getMazmorra().getCuevaPorId("cueva_facil");
        assertTrue(facil.getCelda(1, 1).esTransitable());
        assertEquals(TipoCelda.INICIO, facil.getCelda(1, 1).getTipo());
    }

    @Test
    void murosNoSonTransitables() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        Cueva facil = resultado.getMazmorra().getCuevaPorId("cueva_facil");
        assertFalse(facil.getCelda(0, 0).esTransitable());
        assertEquals(TipoCelda.MURO, facil.getCelda(0, 0).getTipo());
    }

    @Test
    void conexionesEntreCuevasCreadas() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        Mazmorra mazmorra = resultado.getMazmorra();
        Cueva facil = mazmorra.getCuevaPorId("cueva_facil");
        Cueva media = mazmorra.getCuevaPorId("cueva_media");
        Cueva dificil = mazmorra.getCuevaPorId("cueva_dificil");

        assertTrue(mazmorra.existeConexion(facil, media));
        assertTrue(mazmorra.existeConexion(media, dificil));
    }

    @Test
    void hayEnemigosEnElResultado() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        ListaSE<ConfiguracionEnemigoDTO> enemigos = resultado.getEnemigos();
        assertNotNull(enemigos);
        assertTrue(enemigos.getSize() > 0);
    }

    @Test
    void hayObjetosEnElResultado() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        ListaSE<ConfiguracionObjetoDTO> objetos = resultado.getObjetos();
        assertNotNull(objetos);
        assertTrue(objetos.getSize() > 0);
    }

    @Test
    void cargaConRutaInvalidaLanzaExcepcion() {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        assertThrows(Exception.class, () -> cargador.cargar("datos/no_existe.json"));
    }
}
