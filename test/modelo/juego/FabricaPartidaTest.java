package modelo.juego;

import json.CargadorConfiguracion;
import json.ResultadoCarga;
import modelo.objetos.Objeto;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FabricaPartidaTest {
    private static final String RUTA_JSON = "datos/cuevas.json";

    @Test
    void creaPartidaJugableDesdeResultadoCarga() throws Exception {
        ResultadoCarga resultado = new CargadorConfiguracion().cargar(RUTA_JSON);

        Partida partida = new FabricaPartida().crearPartida(resultado);

        assertEquals(EstadoPartida.EN_CURSO, partida.getEstado());
        assertEquals(40, partida.getTurnosRestantes());
        assertEquals("cueva_facil", partida.getCuevaActual().getId());
        assertEquals(1, partida.getJugadorEnMapa().getFila());
        assertEquals(1, partida.getJugadorEnMapa().getColumna());
        assertEquals(1, partida.getEnemigos().getSize());
        assertEquals(2, partida.getObjetosEnSuelo().getSize());
    }

    @Test
    void permiteRecogerLlaveYAvanzarConPuertaCreadaDesdeConexion() throws Exception {
        ResultadoCarga resultado = new CargadorConfiguracion().cargar(RUTA_JSON);
        Partida partida = new FabricaPartida().crearPartida(resultado);

        assertTrue(partida.moverJugador(3, 2));
        assertTrue(partida.recogerObjeto("llave-cueva-media"));
        assertTrue(partida.pasarTurno());
        assertTrue(partida.avanzarACueva("cueva_media"));

        assertEquals("cueva_media", partida.getCuevaActual().getId());
        assertEquals(5, partida.getJugadorEnMapa().getFila());
        assertEquals(4, partida.getJugadorEnMapa().getColumna());
    }

    @Test
    void colocaTiposDeEnemigosYObjetosDelJson() throws Exception {
        ResultadoCarga resultado = new CargadorConfiguracion().cargar(RUTA_JSON);
        Partida partida = new FabricaPartida().crearPartida(resultado);

        PersonajeEnMapa enemigo = partida.getEnemigos().get(0);
        Objeto objeto = partida.getObjetosEnSuelo().get(0).getObjeto();

        assertEquals("ESQUELETO", enemigo.getNombre());
        assertEquals("pocion-facil-1", objeto.getId());
    }

    @Test
    void rechazaResultadoNulo() {
        FabricaPartida fabrica = new FabricaPartida();

        assertThrows(IllegalArgumentException.class, () -> fabrica.crearPartida(null));
    }

    @Test
    void rechazaTipoDeObjetoDesconocidoDesdeJson() throws Exception {
        ResultadoCarga resultado = cargarJsonTemporal("""
                {
                  "nombre": "Mazmorra invalida",
                  "cuevas": [
                    {
                      "id": "cueva_test",
                      "filas": 3,
                      "columnas": 3,
                      "matriz": [
                        ["MURO", "MURO", "MURO"],
                        ["MURO", "INICIO", "MURO"],
                        ["MURO", "MURO", "MURO"]
                      ],
                      "objetos": [
                        {
                          "tipo": "GEMA",
                          "id": "gema-1",
                          "fila": 1,
                          "columna": 1
                        }
                      ]
                    }
                  ]
                }
                """);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new FabricaPartida().crearPartida(resultado));
        assertTrue(error.getMessage().contains("Tipo de objeto desconocido"));
    }

    @Test
    void rechazaTipoDeEnemigoDesconocidoDesdeJson() throws Exception {
        ResultadoCarga resultado = cargarJsonTemporal("""
                {
                  "nombre": "Mazmorra invalida",
                  "cuevas": [
                    {
                      "id": "cueva_test",
                      "filas": 3,
                      "columnas": 3,
                      "matriz": [
                        ["MURO", "MURO", "MURO"],
                        ["MURO", "INICIO", "MURO"],
                        ["MURO", "MURO", "MURO"]
                      ],
                      "enemigos": [
                        {
                          "tipo": "DRAGON",
                          "fila": 1,
                          "columna": 1,
                          "vida": 30,
                          "ataque": 10,
                          "defensa": 2,
                          "movimiento": 1
                        }
                      ]
                    }
                  ]
                }
                """);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new FabricaPartida().crearPartida(resultado));
        assertTrue(error.getMessage().contains("Tipo de enemigo desconocido"));
    }

    @Test
    void rechazaLlaveSinCodigoDeCerraduraDesdeJson() throws Exception {
        ResultadoCarga resultado = cargarJsonTemporal("""
                {
                  "nombre": "Mazmorra invalida",
                  "cuevas": [
                    {
                      "id": "cueva_test",
                      "filas": 3,
                      "columnas": 3,
                      "matriz": [
                        ["MURO", "MURO", "MURO"],
                        ["MURO", "INICIO", "MURO"],
                        ["MURO", "MURO", "MURO"]
                      ],
                      "objetos": [
                        {
                          "tipo": "LLAVE",
                          "id": "llave-incompleta",
                          "fila": 1,
                          "columna": 1,
                          "tipoLlave": "PUERTA"
                        }
                      ]
                    }
                  ]
                }
                """);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new FabricaPartida().crearPartida(resultado));
        assertTrue(error.getMessage().contains("codigoCerradura"));
    }

    @Test
    void rechazaObjetoColocadoEnMuroDesdeJson() throws Exception {
        ResultadoCarga resultado = cargarJsonTemporal("""
                {
                  "nombre": "Mazmorra invalida",
                  "cuevas": [
                    {
                      "id": "cueva_test",
                      "filas": 3,
                      "columnas": 3,
                      "matriz": [
                        ["MURO", "MURO", "MURO"],
                        ["MURO", "INICIO", "MURO"],
                        ["MURO", "MURO", "MURO"]
                      ],
                      "objetos": [
                        {
                          "tipo": "POCION",
                          "id": "pocion-muro",
                          "fila": 0,
                          "columna": 0,
                          "cura": 25
                        }
                      ]
                    }
                  ]
                }
                """);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new FabricaPartida().crearPartida(resultado));
        assertTrue(error.getMessage().contains("No se pudo colocar objeto"));
    }

    private ResultadoCarga cargarJsonTemporal(String contenido) throws Exception {
        Path ruta = Files.createTempFile("cuevas-test", ".json");
        Files.writeString(ruta, contenido);
        return new CargadorConfiguracion().cargar(ruta.toString());
    }
}
