package vista;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

class ReproductorSfxTest {

    @Test
    void generaWavValidoParaEfectosDeSonido() {
        byte[] contenido = ReproductorSfx.crearWavParaTest(new double[] {440, 660}, 120, 0.5);
        assertArrayEquals(new byte[] {'R', 'I', 'F', 'F'}, primerosBytes(contenido, 0, 4));
        assertArrayEquals(new byte[] {'W', 'A', 'V', 'E'}, primerosBytes(contenido, 8, 4));
        assertArrayEquals(new byte[] {'d', 'a', 't', 'a'}, primerosBytes(contenido, 36, 4));
    }

    private byte[] primerosBytes(byte[] origen, int inicio, int longitud) {
        byte[] resultado = new byte[longitud];
        for (int i = 0; i < longitud; i++) {
            resultado[i] = origen[inicio + i];
        }
        return resultado;
    }
}
