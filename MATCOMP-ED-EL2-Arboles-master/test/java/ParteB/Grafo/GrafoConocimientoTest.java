package ParteB.Grafo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrafoConocimientoTest {

    private GrafoConocimiento crearGrafoConocimiento() {
        GrafoConocimiento grafo = new GrafoConocimiento();

        grafo.addTriplete(new Triplete("persona:Einstein", "nace_en", "lugar:Ulm"));
        grafo.addTriplete(new Triplete("persona:Einstein", "profesion", "profesion:Fisico"));
        grafo.addTriplete(new Triplete("persona:Einstein", "premio", "premio:NobelFisica"));

        grafo.addTriplete(new Triplete("persona:MaxBorn", "nace_en", "lugar:Ulm"));
        grafo.addTriplete(new Triplete("persona:MaxBorn", "profesion", "profesion:Fisico"));
        grafo.addTriplete(new Triplete("persona:MaxBorn", "premio", "premio:NobelFisica"));

        grafo.addTriplete(new Triplete("persona:MarieCurie", "nace_en", "lugar:Varsovia"));
        grafo.addTriplete(new Triplete("persona:MarieCurie", "profesion", "profesion:Fisico"));
        grafo.addTriplete(new Triplete("persona:MarieCurie", "premio", "premio:NobelFisica"));

        grafo.addTriplete(new Triplete("persona:Antonio", "nace_en", "lugar:Villarrubia de los Caballeros"));

        return grafo;
    }

    @Test
    void addTriplete() {
        GrafoConocimiento grafo = new GrafoConocimiento();

        grafo.addTriplete(new Triplete("persona:Einstein", "nace_en", "lugar:Ulm"));

        assertEquals(2, grafo.getNodos().getSize());
        assertEquals(1, grafo.getArcos().getSize());
        assertTrue(grafo.existeTriplete("persona:Einstein", "nace_en", "lugar:Ulm"));
    }

    @Test
    void buscarObjeto() {
        GrafoConocimiento grafo = crearGrafoConocimiento();

        assertEquals("lugar:Ulm", grafo.buscarObjeto("persona:Einstein", "nace_en"));
        assertNull(grafo.buscarObjeto("persona:Einstein", "vive_en"));
    }

    @Test
    void tienePredicado() {
        GrafoConocimiento grafo = crearGrafoConocimiento();

        assertTrue(grafo.tienePredicado("persona:Einstein", "premio"));
        assertFalse(grafo.tienePredicado("persona:Antonio", "premio"));
    }

    @Test
    void existeTriplete() {
        GrafoConocimiento grafo = crearGrafoConocimiento();

        assertTrue(grafo.existeTriplete("persona:Einstein", "nace_en", "lugar:Ulm"));
        assertFalse(grafo.existeTriplete("persona:Einstein", "nace_en", "lugar:Varsovia"));
    }

    @Test
    void obtenerTipo() {
        GrafoConocimiento grafo = new GrafoConocimiento();

        assertEquals("persona", grafo.obtenerTipo("persona:Einstein"));
        assertEquals("sin_tipo", grafo.obtenerTipo("1921"));
    }

    @Test
    void getTiposNodos() {
        GrafoConocimiento grafo = crearGrafoConocimiento();
        grafo.addTriplete(new Triplete("persona:Einstein", "anio", "1921"));

        assertEquals("[persona, lugar, profesion, premio, sin_tipo]", grafo.getTiposNodos().toString());
    }

    @Test
    void getFisicosNacidosComoEinstein() {
        GrafoConocimiento grafo = crearGrafoConocimiento();

        assertEquals("[persona:MaxBorn]", grafo.getFisicosNacidosComoEinstein().toString());
    }

    @Test
    void getLugaresNacimientoPremiosNobel() {
        GrafoConocimiento grafo = crearGrafoConocimiento();

        assertEquals("[lugar:Ulm, lugar:Varsovia]", grafo.getLugaresNacimientoPremiosNobel().toString());
    }
}
