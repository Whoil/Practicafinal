package ParteB.Grafo;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CargadorJSONTest {

    @Test
    void leerDatos() throws IOException {
        CargadorJSON cargador = new CargadorJSON();

        DatosGrafo datos = cargador.leerDatos("datos/nobel.json");

        assertNotNull(datos);
        assertNotNull(datos.getTipos());
        assertNotNull(datos.getTripletas());
        assertEquals(4, datos.getTipos().length);
        assertEquals(9, datos.getTripletas().length);
    }

    @Test
    void cargarGrafoConectado() throws IOException {
        CargadorJSON cargador = new CargadorJSON();

        GrafoConocimiento grafo = cargador.cargarGrafo("datos/conectado.json");

        assertNotNull(grafo);
        assertFalse(grafo.isDisjunto());
    }

    @Test
    void cargarGrafoDisjunto() throws IOException {
        CargadorJSON cargador = new CargadorJSON();

        GrafoConocimiento grafo = cargador.cargarGrafo("datos/disjunto.json");

        assertNotNull(grafo);
        assertTrue(grafo.isDisjunto());
    }

    @Test
    void cargarGrafoNobel() throws IOException {
        CargadorJSON cargador = new CargadorJSON();

        GrafoConocimiento grafo = cargador.cargarGrafo("datos/nobel.json");

        assertNotNull(grafo);
        assertEquals("[persona:MaxBorn]", grafo.getFisicosNacidosComoEinstein().toString());
        assertEquals("[lugar:Ulm, lugar:Varsovia]", grafo.getLugaresNacimientoPremiosNobel().toString());
    }
}
