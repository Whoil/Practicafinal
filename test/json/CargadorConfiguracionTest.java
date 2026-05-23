package json;

import Estructuras.ListaSE;
import modelo.juego.Mazmorra;
import modelo.mapa.Cueva;
import modelo.mapa.TipoCelda;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

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
    void cuevaFacilTieneTamano15x15() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        Cueva facil = resultado.getMazmorra().getCuevaPorId("cueva_facil");
        assertNotNull(facil);
        assertEquals(15, facil.getFilas());
        assertEquals(15, facil.getColumnas());
    }

    @Test
    void cuevaMediaTieneTamano19x19() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        Cueva media = resultado.getMazmorra().getCuevaPorId("cueva_media");
        assertNotNull(media);
        assertEquals(19, media.getFilas());
        assertEquals(19, media.getColumnas());
    }

    @Test
    void cuevaDificilTieneTamano23x23() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        Cueva dificil = resultado.getMazmorra().getCuevaPorId("cueva_dificil");
        assertNotNull(dificil);
        assertEquals(23, dificil.getFilas());
        assertEquals(23, dificil.getColumnas());
    }

    @Test
    void celdaInicioEsTransitable() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        Cueva facil = resultado.getMazmorra().getCuevaPorId("cueva_facil");
        assertTrue(facil.getCelda(2, 2).esTransitable());
        assertEquals(TipoCelda.INICIO, facil.getCelda(2, 2).getTipo());
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
        assertEquals("cueva_facil", enemigos.get(0).getIdCueva());
    }

    @Test
    void bossRespetaEstadisticasAcordadas() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        ConfiguracionEnemigoDTO boss = null;
        ListaSE<ConfiguracionEnemigoDTO> enemigos = resultado.getEnemigos();
        for (int indice = 0; indice < enemigos.getSize(); indice++) {
            ConfiguracionEnemigoDTO enemigo = enemigos.get(indice);
            if ("BOSS".equals(enemigo.getTipo())) {
                boss = enemigo;
            }
        }

        assertNotNull(boss);
        assertEquals(200, boss.getVida());
        assertEquals(22, boss.getAtaque());
        assertEquals(8, boss.getDefensa());
        assertEquals(3, boss.getMovimiento());
    }


    @Test
    void hayObjetosEnElResultado() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        ListaSE<ConfiguracionObjetoDTO> objetos = resultado.getObjetos();
        assertNotNull(objetos);
        assertTrue(objetos.getSize() > 0);
        assertEquals("cueva_facil", objetos.get(0).getIdCueva());
    }

    @Test
    void conservaConexionesParaCrearPuertasDePartida() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar(RUTA_JSON);

        assertNotNull(resultado.getConexiones());
        assertEquals(2, resultado.getConexiones().getSize());
        assertEquals("cueva_facil", resultado.getConexiones().get(0).getOrigen());
        assertEquals("cueva_media", resultado.getConexiones().get(0).getDestino());
    }

    @Test
    void cargaConRutaInvalidaLanzaExcepcion() {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        assertThrows(Exception.class, () -> cargador.cargar("datos/no_existe.json"));
    }

    @Test
    void rechazaConexionConCuevaInexistente() throws Exception {
        Path ruta = Files.createTempFile("cuevas-conexion-invalida", ".json");
        Files.writeString(ruta, """
                {
                  "nombre": "Mazmorra invalida",
                  "cuevas": [
                    {
                      "id": "cueva_unica",
                      "filas": 3,
                      "columnas": 3,
                      "matriz": [
                        ["MURO", "MURO", "MURO"],
                        ["MURO", "INICIO", "MURO"],
                        ["MURO", "MURO", "MURO"]
                      ]
                    }
                  ],
                  "conexiones": [
                    {
                      "origen": "cueva_unica",
                      "destino": "cueva_fantasma",
                      "etiqueta": "puerta_rota"
                    }
                  ]
                }
                """);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new CargadorConfiguracion().cargar(ruta.toString()));
        assertTrue(error.getMessage().contains("Conexion invalida"));
    }
}
