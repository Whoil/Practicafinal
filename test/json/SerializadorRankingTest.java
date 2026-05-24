package json;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import modelo.juego.EstadisticasPartida;

import static org.junit.jupiter.api.Assertions.*;

class SerializadorRankingTest {

    @TempDir
    Path tempDir;

    @Test
    void creaRankingSiNoExiste() throws Exception {
        String ruta = tempDir.resolve("ranking.json").toString();

        SerializadorRanking.guardarResultado(resultado("A", 100), ruta);

        ResultadoPartidaDTO[] ranking = SerializadorRanking.leerRanking(ruta);
        assertEquals(1, ranking.length);
        assertEquals("A", ranking[0].getNombreJugador());
    }

    @Test
    void conservaEntradasYAnadeNueva() throws Exception {
        String ruta = tempDir.resolve("ranking.json").toString();

        SerializadorRanking.guardarResultado(resultado("A", 100), ruta);
        SerializadorRanking.guardarResultado(resultado("B", 200), ruta);

        ResultadoPartidaDTO[] ranking = SerializadorRanking.leerRanking(ruta);
        assertEquals(2, ranking.length);
        assertEquals("A", ranking[0].getNombreJugador());
        assertEquals("B", ranking[1].getNombreJugador());
    }

    @Test
    void devuelveTop10OrdenadoPorPuntuacion() throws Exception {
        String ruta = tempDir.resolve("ranking.json").toString();
        for (int i = 0; i < 12; i++) {
            SerializadorRanking.guardarResultado(resultado("Mago" + i, i * 100), ruta);
        }

        ResultadoPartidaDTO[] top = SerializadorRanking.obtenerTop10(ruta);

        assertEquals(10, top.length);
        assertEquals("Mago11", top[0].getNombreJugador());
        assertEquals("Mago2", top[9].getNombreJugador());
    }

    @Test
    void escribeJsonFormateado() throws Exception {
        String ruta = tempDir.resolve("ranking.json").toString();

        SerializadorRanking.guardarResultado(resultado("A", 100), ruta);

        String contenido = java.nio.file.Files.readString(tempDir.resolve("ranking.json"));
        assertTrue(contenido.contains("\n"));
        assertTrue(contenido.contains("  \"nombreJugador\""));
    }

    private ResultadoPartidaDTO resultado(String nombre, int danoEjercido) {
        EstadisticasPartida stats = new EstadisticasPartida();
        stats.registrarDanoEjercido(danoEjercido);
        return new ResultadoPartidaDTO(nombre, false, stats);
    }
}
