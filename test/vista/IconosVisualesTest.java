package vista;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class IconosVisualesTest {

    private static final String[] ICONOS = {
        "pocion.png",
        "salida.png",
        "escudo.png",
        "tesoro.png"
    };

    private static final String[] ASSETS_PACK = {
        "door_closed.png"
    };

    @Test
    void iconosVisualesExistenYUsanFormatoPng() throws IOException {
        for (String archivo : ICONOS) {
            Path ruta = Path.of("datos", "iconos", archivo);
            assertTrue(Files.size(ruta) > 100, "El icono debe existir y tener contenido: " + archivo);
            byte[] contenido = Files.readAllBytes(ruta);
            assertArrayEquals(
                    new byte[] {(byte) 0x89, 'P', 'N', 'G'},
                    primerosBytes(contenido, 0, 4));
        }
        for (String archivo : ASSETS_PACK) {
            Path ruta = Path.of("Dungeon Asset Pack", archivo);
            assertTrue(Files.size(ruta) > 100, "El asset debe existir y tener contenido: " + archivo);

            byte[] contenido = Files.readAllBytes(ruta);
            assertArrayEquals(
                    new byte[] {(byte) 0x89, 'P', 'N', 'G'},
                    primerosBytes(contenido, 0, 4));
        }
    }

    private byte[] primerosBytes(byte[] origen, int inicio, int longitud) {
        byte[] resultado = new byte[longitud];
        for (int i = 0; i < longitud; i++) {
            resultado[i] = origen[inicio + i];
        }
        return resultado;
    }
}
