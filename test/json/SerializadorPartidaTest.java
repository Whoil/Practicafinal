package json;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import Estructuras.ListaDE;
import Estructuras.MiIterador;
import modelo.juego.Mazmorra;
import modelo.mapa.Cueva;
import modelo.mapa.TipoCelda;
import modelo.objetos.Arma;
import modelo.objetos.Escudo;
import modelo.objetos.Espada;
import modelo.objetos.Llave;
import modelo.objetos.Objeto;
import modelo.objetos.Pocion;
import modelo.objetos.TipoLlave;
import modelo.personajes.Enemigo;
import modelo.personajes.Jugador;
import modelo.personajes.TipoEnemigo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas del guardado y carga de partida con SerializadorPartida.
 *
 * Verifica que los DTOs se serializan a JSON y se recuperan
 * correctamente (round-trip), y que los casos de error (ruta
 * invalida, fichero corrupto) lanzan las excepciones esperadas.
 */
class SerializadorPartidaTest {

    @TempDir
    Path tempDir;

    @Test
    void guardarYRecargarDatosBasicos() throws Exception {
        DatosPartidaDTO original = crearPartidaMinima();

        String ruta = tempDir.resolve("partida.json").toString();
        SerializadorPartida.guardar(original, ruta);

        DatosPartidaDTO recuperada = SerializadorPartida.cargar(ruta);
        assertNotNull(recuperada);
        assertEquals(original.getVersion(), recuperada.getVersion());
        assertEquals(original.getEstado(), recuperada.getEstado());
        assertEquals(original.getTurnosRestantes(), recuperada.getTurnosRestantes());
    }

    @Test
    void guardarYRecargarMazmorra() throws Exception {
        DatosCuevaDTO cueva = new DatosCuevaDTO(
                "cueva_facil", "Cueva Facil", 5, 5,
                new String[][]{
                        {"MURO", "MURO", "MURO", "MURO", "MURO"},
                        {"MURO", "INICIO", "SUELO", "PUERTA", "MURO"},
                        {"MURO", "SUELO", "SUELO", "SUELO", "MURO"},
                        {"MURO", "TRAMPA", "SUELO", "TESORO", "MURO"},
                        {"MURO", "MURO", "MURO", "SALIDA", "MURO"}
                },
                new DatosEnemigoDTO[0],
                new DatosObjetoDTO[0]);
        ConexionDTO conexion = new ConexionDTO("cueva_facil", "cueva_media", "puerta_1");
        DatosMazmorraDTO mazmorra = new DatosMazmorraDTO(
                "cueva_facil",
                new DatosCuevaDTO[]{cueva},
                new ConexionDTO[]{conexion});
        DatosJugadorDTO jugador = new DatosJugadorDTO(
                "Heroe", 100, 100, 15, 5, 3,
                1, 1, null, null, new DatosObjetoDTO[0]);
        DatosPartidaDTO original = new DatosPartidaDTO(
                "1.0", mazmorra, jugador, "EN_CURSO", 40);

        String ruta = tempDir.resolve("mazmorra.json").toString();
        SerializadorPartida.guardar(original, ruta);
        DatosPartidaDTO recuperada = SerializadorPartida.cargar(ruta);

        DatosMazmorraDTO mRec = recuperada.getMazmorra();
        assertNotNull(mRec);
        assertEquals("cueva_facil", mRec.getCuevaActual());
        assertEquals(1, mRec.getCuevas().length);
        assertEquals("Cueva Facil", mRec.getCuevas()[0].getNombre());
        assertEquals(5, mRec.getCuevas()[0].getFilas());
        assertEquals(5, mRec.getCuevas()[0].getColumnas());
        assertEquals(TipoCelda.INICIO.name(),
                mRec.getCuevas()[0].getMatriz()[1][1]);
        assertEquals(1, mRec.getConexiones().length);
        assertEquals("cueva_facil", mRec.getConexiones()[0].getOrigen());
        assertEquals("puerta_1", mRec.getConexiones()[0].getEtiqueta());
    }

    @Test
    void guardarYRecargarJugadorConAtributos() throws Exception {
        DatosObjetoDTO[] inventario = {
                new DatosObjetoDTO("obj_001", "POCION", "Pocion de vida",
                        "Recupera 30 de vida", -1, -1, 30, 0, 0, null),
                new DatosObjetoDTO("obj_002", "ESPADA", "Espada de hierro",
                        "Aumenta el ataque en 10", -1, -1, 0, 10, 0, null)
        };
        DatosJugadorDTO jugador = new DatosJugadorDTO(
                "Heroe", 75, 100, 15, 5, 3,
                2, 3, "obj_002", null, inventario);
        DatosPartidaDTO partida = new DatosPartidaDTO(
                "1.0",
                new DatosMazmorraDTO("cueva_facil",
                        new DatosCuevaDTO[0], new ConexionDTO[0]),
                jugador, "EN_CURSO", 30);

        String ruta = tempDir.resolve("jugador.json").toString();
        SerializadorPartida.guardar(partida, ruta);
        DatosPartidaDTO recuperada = SerializadorPartida.cargar(ruta);

        DatosJugadorDTO jRec = recuperada.getJugador();
        assertNotNull(jRec);
        assertEquals("Heroe", jRec.getNombre());
        assertEquals(75, jRec.getVidaActual());
        assertEquals(100, jRec.getVidaMaxima());
        assertEquals(15, jRec.getAtaqueBase());
        assertEquals(5, jRec.getDefensaBase());
        assertEquals(3, jRec.getMovimiento());
        assertEquals(2, jRec.getFila());
        assertEquals(3, jRec.getColumna());
        assertEquals("obj_002", jRec.getArmaEquipadaId());
        assertNull(jRec.getEscudoEquipadoId());
        assertEquals(2, jRec.getInventario().length);
        assertEquals("Pocion de vida", jRec.getInventario()[0].getNombre());
        assertEquals(30, jRec.getInventario()[0].getCura());
        assertEquals("Espada de hierro", jRec.getInventario()[1].getNombre());
        assertEquals(10, jRec.getInventario()[1].getBonificacionAtaque());
    }

    @Test
    void guardarYRecargarEnemigos() throws Exception {
        DatosEnemigoDTO[] enemigos = {
                new DatosEnemigoDTO("GOBLIN", "Goblin", 20, 20, 8, 2, 2, 2, 2, true),
                new DatosEnemigoDTO("BOSS", "Rey Goblin", 50, 50, 15, 5, 3, 3, 3, true)
        };
        DatosCuevaDTO cueva = new DatosCuevaDTO(
                "cueva_dificil", "Cueva Dificil", 7, 7,
                new String[7][7], enemigos, new DatosObjetoDTO[0]);
        DatosPartidaDTO partida = new DatosPartidaDTO(
                "1.0",
                new DatosMazmorraDTO("cueva_dificil",
                        new DatosCuevaDTO[]{cueva}, new ConexionDTO[0]),
                new DatosJugadorDTO("Heroe", 100, 100, 15, 5, 3,
                        0, 0, null, null, new DatosObjetoDTO[0]),
                "EN_CURSO", 40);

        String ruta = tempDir.resolve("enemigos.json").toString();
        SerializadorPartida.guardar(partida, ruta);
        DatosPartidaDTO recuperada = SerializadorPartida.cargar(ruta);

        DatosEnemigoDTO[] eRec = recuperada.getMazmorra().getCuevas()[0].getEnemigos();
        assertEquals(2, eRec.length);
        assertEquals("GOBLIN", eRec[0].getTipo());
        assertEquals(20, eRec[0].getVidaActual());
        assertTrue(eRec[0].isVivo());
        assertEquals("BOSS", eRec[1].getTipo());
        assertEquals(15, eRec[1].getAtaqueBase());
        assertEquals(3, eRec[1].getMovimiento());
    }

    @Test
    void guardarYRecargarObjetosEnMapa() throws Exception {
        DatosObjetoDTO[] objetosMapa = {
                new DatosObjetoDTO("obj_llave", "LLAVE", "Llave dorada",
                        "Abre la puerta del boss", 4, 4, 0, 0, 0, "DORADA"),
                new DatosObjetoDTO("obj_pocion", "POCION", "Pocion",
                        "Cura 30 de vida", 1, 3, 30, 0, 0, null),
                new DatosObjetoDTO("obj_escudo", "ESCUDO", "Escudo de madera",
                        "Aumenta defensa en 5", 2, 2, 0, 0, 5, null)
        };
        DatosCuevaDTO cueva = new DatosCuevaDTO(
                "cueva_media", "Cueva Media", 6, 6,
                new String[6][6], new DatosEnemigoDTO[0], objetosMapa);
        DatosPartidaDTO partida = new DatosPartidaDTO(
                "1.0",
                new DatosMazmorraDTO("cueva_media",
                        new DatosCuevaDTO[]{cueva}, new ConexionDTO[0]),
                new DatosJugadorDTO("Heroe", 100, 100, 15, 5, 3,
                        0, 0, null, null, new DatosObjetoDTO[0]),
                "EN_CURSO", 40);

        String ruta = tempDir.resolve("objetos.json").toString();
        SerializadorPartida.guardar(partida, ruta);
        DatosPartidaDTO recuperada = SerializadorPartida.cargar(ruta);

        DatosObjetoDTO[] oRec = recuperada.getMazmorra().getCuevas()[0].getObjetos();
        assertEquals(3, oRec.length);
        assertEquals("LLAVE", oRec[0].getTipo());
        assertEquals("DORADA", oRec[0].getTipoLlave());
        assertEquals(4, oRec[0].getFila());
        assertEquals("POCION", oRec[1].getTipo());
        assertEquals(30, oRec[1].getCura());
        assertEquals("ESCUDO", oRec[2].getTipo());
        assertEquals(5, oRec[2].getBonificacionDefensa());
    }

    @Test
    void guardarYRecargarEstadoVictoria() throws Exception {
        DatosPartidaDTO partida = new DatosPartidaDTO(
                "1.0",
                new DatosMazmorraDTO("cueva_dificil",
                        new DatosCuevaDTO[0], new ConexionDTO[0]),
                new DatosJugadorDTO("Heroe", 100, 100, 15, 5, 3,
                        0, 0, null, null, new DatosObjetoDTO[0]),
                "VICTORIA", 5);

        String ruta = tempDir.resolve("victoria.json").toString();
        SerializadorPartida.guardar(partida, ruta);
        DatosPartidaDTO recuperada = SerializadorPartida.cargar(ruta);

        assertEquals("VICTORIA", recuperada.getEstado());
        assertEquals(5, recuperada.getTurnosRestantes());
    }

    @Test
    void cargarRutaInvalidaLanzaExcepcion() {
        assertThrows(IOException.class,
                () -> SerializadorPartida.cargar("datos/no_existe.json"));
    }

    @Test
    void guardarRutaInvalidaLanzaExcepcion() {
        DatosPartidaDTO partida = crearPartidaMinima();
        assertThrows(IOException.class,
                () -> SerializadorPartida.guardar(partida, "/ruta/no/valida/partida.json"));
    }

    @Test
    void versionActualCoincide() {
        assertEquals("1.0", SerializadorPartida.getVersionActual());
    }

    @Test
    void guardarYRecargarPartidaCompleta() throws Exception {
        String[][] matriz = {
                {"MURO", "MURO", "MURO", "MURO", "MURO"},
                {"MURO", "INICIO", "SUELO", "SUELO", "MURO"},
                {"MURO", "SUELO", "MURO", "SUELO", "MURO"},
                {"MURO", "SUELO", "SUELO", "PUERTA", "MURO"},
                {"MURO", "MURO", "MURO", "MURO", "MURO"}
        };
        DatosEnemigoDTO[] enemigos = {
                new DatosEnemigoDTO("GOBLIN", "Goblin", 20, 20, 8, 2, 2, 2, 2, true)
        };
        DatosObjetoDTO[] objetos = {
                new DatosObjetoDTO("obj_pocion", "POCION", "Pocion",
                        "Cura 30", 1, 3, 30, 0, 0, null)
        };
        DatosCuevaDTO cueva = new DatosCuevaDTO(
                "cueva_facil", "Cueva Facil", 5, 5,
                matriz, enemigos, objetos);
        ConexionDTO conexion = new ConexionDTO("cueva_facil", "cueva_media", "puerta_1");
        DatosMazmorraDTO mazmorra = new DatosMazmorraDTO(
                "cueva_facil",
                new DatosCuevaDTO[]{cueva},
                new ConexionDTO[]{conexion});
        DatosObjetoDTO[] inventario = {
                new DatosObjetoDTO("obj_espada", "ESPADA", "Espada",
                        "Ataque +10", -1, -1, 0, 10, 0, null)
        };
        DatosJugadorDTO jugador = new DatosJugadorDTO(
                "Heroe", 85, 100, 15, 5, 3,
                1, 1, "obj_espada", null, inventario);
        DatosPartidaDTO original = new DatosPartidaDTO(
                "1.0", mazmorra, jugador, "EN_CURSO", 35);

        String ruta = tempDir.resolve("completa.json").toString();
        SerializadorPartida.guardar(original, ruta);
        DatosPartidaDTO rec = SerializadorPartida.cargar(ruta);

        assertEquals("1.0", rec.getVersion());
        assertEquals("EN_CURSO", rec.getEstado());
        assertEquals(35, rec.getTurnosRestantes());
        assertEquals("cueva_facil", rec.getMazmorra().getCuevaActual());
        assertEquals(1, rec.getMazmorra().getCuevas().length);
        assertEquals("INICIO", rec.getMazmorra().getCuevas()[0].getMatriz()[1][1]);
        assertEquals(1, rec.getMazmorra().getCuevas()[0].getEnemigos().length);
        assertEquals("GOBLIN", rec.getMazmorra().getCuevas()[0].getEnemigos()[0].getTipo());
        assertEquals(1, rec.getMazmorra().getCuevas()[0].getObjetos().length);
        assertEquals("POCION", rec.getMazmorra().getCuevas()[0].getObjetos()[0].getTipo());
        assertEquals(1, rec.getMazmorra().getConexiones().length);
        assertEquals("cueva_facil", rec.getMazmorra().getConexiones()[0].getOrigen());
        assertEquals("Heroe", rec.getJugador().getNombre());
        assertEquals(85, rec.getJugador().getVidaActual());
        assertEquals(1, rec.getJugador().getInventario().length);
        assertEquals("obj_espada", rec.getJugador().getArmaEquipadaId());
    }

    // ---------------------------------------------------------------
    // Tests de conversion modelo <-> DTO
    // ---------------------------------------------------------------

    @Test
    void convertirMazmorraRealADTOyRecuperar() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar("datos/cuevas.json");
        Mazmorra original = resultado.getMazmorra();

        DatosMazmorraDTO dto = SerializadorPartida.mazmorraADTO(original);
        assertNotNull(dto);
        assertEquals(3, dto.getCuevas().length);
        assertEquals("cueva_facil", dto.getCuevas()[0].getId());
        assertEquals(9, dto.getCuevas()[0].getFilas());
        assertEquals(9, dto.getCuevas()[0].getColumnas());

        Mazmorra recuperada = SerializadorPartida.dtoAMazmorra(dto);
        assertNotNull(recuperada);
        assertEquals(3, recuperada.getNumeroCuevas());
        assertEquals("cueva_facil", recuperada.getCuevaActual().getId());
        assertEquals("INICIO",
                dto.getCuevas()[0].getMatriz()[1][1]);
    }

    @Test
    void convertirJugadorCompletoADTOyRecuperar() {
        Jugador original = new Jugador("Heroe", 100, 15, 5, 3, 1, 1);
        original.recibirDano(25);

        Pocion pocion = new Pocion("obj_pocion");
        Espada espada = new Espada("obj_espada");
        original.agregarObjeto(pocion);
        original.agregarObjeto(espada);
        original.equiparArma(espada);

        DatosJugadorDTO dto = SerializadorPartida.jugadorADTO(original);
        assertEquals("Heroe", dto.getNombre());
        assertEquals(75, dto.getVidaActual());
        assertEquals(100, dto.getVidaMaxima());
        assertEquals(15, dto.getAtaqueBase());
        assertEquals(5, dto.getDefensaBase());
        assertEquals(3, dto.getMovimiento());
        assertEquals(1, dto.getFila());
        assertEquals(1, dto.getColumna());
        assertEquals("obj_espada", dto.getArmaEquipadaId());
        assertNull(dto.getEscudoEquipadoId());
        assertEquals(2, dto.getInventario().length);

        Jugador recuperado = SerializadorPartida.dtoAJugador(dto);
        assertEquals(original.getNombre(), recuperado.getNombre());
        assertEquals(original.getVidaActual(), recuperado.getVidaActual());
        assertEquals(original.getVidaMaxima(), recuperado.getVidaMaxima());
        assertEquals(original.getAtaqueBase(), recuperado.getAtaqueBase());
        assertEquals(original.getDefensaBase(), recuperado.getDefensaBase());
        assertEquals(original.getFila(), recuperado.getFila());
        assertEquals(original.getColumna(), recuperado.getColumna());
        assertNotNull(recuperado.getArmaEquipada());
        assertEquals("obj_espada", recuperado.getArmaEquipada().getId());
        assertEquals(2, recuperado.getCantidadObjetosInventario());
    }

    @Test
    void convertirEnemigoADTOyRecuperar() {
        Enemigo original = new Enemigo("Goblin", TipoEnemigo.ORCO,
                30, 10, 3, 2, 2, 2);
        original.recibirDano(5);

        DatosEnemigoDTO dto = SerializadorPartida.enemigoADTO(original);
        assertEquals("ORCO", dto.getTipo());
        assertEquals("Goblin", dto.getNombre());
        assertEquals(25, dto.getVidaActual());
        assertEquals(30, dto.getVidaMaxima());
        assertEquals(10, dto.getAtaqueBase());
        assertEquals(3, dto.getDefensaBase());
        assertEquals(2, dto.getMovimiento());
        assertEquals(2, dto.getFila());
        assertEquals(2, dto.getColumna());
        assertTrue(dto.isVivo());

        Enemigo recuperado = SerializadorPartida.dtoAEnemigo(dto);
        assertEquals(original.getNombre(), recuperado.getNombre());
        assertEquals(original.getTipoEnemigo(), recuperado.getTipoEnemigo());
        assertEquals(original.getVidaActual(), recuperado.getVidaActual());
        assertEquals(original.getVidaMaxima(), recuperado.getVidaMaxima());
        assertEquals(original.getFila(), recuperado.getFila());
        assertEquals(original.estaVivo(), recuperado.estaVivo());
    }

    @Test
    void convertirObjetoPocionADTOyRecuperar() {
        Pocion original = new Pocion("obj_pocion", 35);
        DatosObjetoDTO dto = SerializadorPartida.objetoADTO(original, 1, 3);
        assertEquals("POCION", dto.getTipo());
        assertEquals(35, dto.getCura());
        assertEquals(1, dto.getFila());
        assertEquals(3, dto.getColumna());

        Objeto recuperado = SerializadorPartida.dtoAObjeto(dto);
        assertTrue(recuperado instanceof Pocion);
        assertEquals(35, ((Pocion) recuperado).getPuntosCuracion());
    }

    @Test
    void convertirObjetoEspadaADTOyRecuperar() {
        Espada original = new Espada("obj_espada");
        DatosObjetoDTO dto = SerializadorPartida.objetoADTO(original, -1, -1);
        assertEquals("ESPADA", dto.getTipo());
        assertEquals(Espada.ATAQUE_EXTRA, dto.getBonificacionAtaque());

        Objeto recuperado = SerializadorPartida.dtoAObjeto(dto);
        assertTrue(recuperado instanceof Espada);
    }

    @Test
    void convertirObjetoLlaveADTOyRecuperar() {
        Llave original = new Llave("obj_llave", TipoLlave.PUERTA, "puerta_1");
        DatosObjetoDTO dto = SerializadorPartida.objetoADTO(original, 4, 4);
        assertEquals("LLAVE", dto.getTipo());
        assertEquals("PUERTA", dto.getTipoLlave());

        Objeto recuperado = SerializadorPartida.dtoAObjeto(dto);
        assertTrue(recuperado instanceof Llave);
    }

    @Test
    void convertirObjetoEscudoADTOyRecuperar() {
        Escudo original = new Escudo("obj_escudo");
        DatosObjetoDTO dto = SerializadorPartida.objetoADTO(original, 2, 2);
        assertEquals("ESCUDO", dto.getTipo());
        assertEquals(Escudo.DEFENSA_EXTRA, dto.getBonificacionDefensa());

        Objeto recuperado = SerializadorPartida.dtoAObjeto(dto);
        assertTrue(recuperado instanceof Escudo);
    }

    @Test
    void guardarYRecargarConMazmorraYJugadorReales() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar("datos/cuevas.json");
        Mazmorra mazmorra = resultado.getMazmorra();

        Jugador jugador = new Jugador("Heroe", 100, 15, 5, 3, 1, 1);
        jugador.recibirDano(20);
        Pocion pocion = new Pocion("obj_pocion");
        Llave llave = new Llave("obj_llave", TipoLlave.PUERTA, "puerta_boss");
        jugador.agregarObjeto(pocion);
        jugador.agregarObjeto(llave);

        DatosPartidaDTO partidaDTO = SerializadorPartida.desdeMazmorraJugador(
                mazmorra, jugador, "EN_CURSO", 35);

        String ruta = tempDir.resolve("partida_real.json").toString();
        SerializadorPartida.guardar(partidaDTO, ruta);
        DatosPartidaDTO cargada = SerializadorPartida.cargar(ruta);

        assertEquals("1.0", cargada.getVersion());
        assertEquals("EN_CURSO", cargada.getEstado());
        assertEquals(35, cargada.getTurnosRestantes());
        assertEquals(3, cargada.getMazmorra().getCuevas().length);
        assertEquals("Heroe", cargada.getJugador().getNombre());
        assertEquals(80, cargada.getJugador().getVidaActual());
        assertEquals(2, cargada.getJugador().getInventario().length);

        Mazmorra mRec = SerializadorPartida.dtoAMazmorra(cargada.getMazmorra());
        assertEquals(3, mRec.getNumeroCuevas());
        assertEquals("cueva_facil", mRec.getCuevaActual().getId());

        Jugador jRec = SerializadorPartida.dtoAJugador(cargada.getJugador());
        assertEquals(80, jRec.getVidaActual());
        assertEquals(2, jRec.getCantidadObjetosInventario());
    }

    @Test
    void matrizDeCuevaSeConservaEnRoundTrip() throws Exception {
        CargadorConfiguracion cargador = new CargadorConfiguracion();
        ResultadoCarga resultado = cargador.cargar("datos/cuevas.json");
        Cueva facil = resultado.getMazmorra().getCuevaPorId("cueva_facil");

        DatosCuevaDTO dto = SerializadorPartida.cuevaADTO(facil);
        assertEquals(9, dto.getFilas());
        assertEquals(9, dto.getColumnas());
        assertEquals("MURO", dto.getMatriz()[0][0]);
        assertEquals("INICIO", dto.getMatriz()[1][1]);

        Cueva recuperada = SerializadorPartida.dtoACueva(dto);
        assertEquals(facil.getId(), recuperada.getId());
        assertEquals(facil.getFilas(), recuperada.getFilas());
        assertEquals(facil.getColumnas(), recuperada.getColumnas());
        assertEquals(facil.getCelda(0, 0).getTipo(),
                recuperada.getCelda(0, 0).getTipo());
        assertEquals(facil.getCelda(1, 1).getTipo(),
                recuperada.getCelda(1, 1).getTipo());
    }

    private DatosPartidaDTO crearPartidaMinima() {
        DatosJugadorDTO jugador = new DatosJugadorDTO(
                "Heroe", 100, 100, 15, 5, 3,
                0, 0, null, null, new DatosObjetoDTO[0]);
        DatosMazmorraDTO mazmorra = new DatosMazmorraDTO(
                "cueva_facil", new DatosCuevaDTO[0], new ConexionDTO[0]);
        return new DatosPartidaDTO("1.0", mazmorra, jugador, "EN_CURSO", 40);
    }
}
