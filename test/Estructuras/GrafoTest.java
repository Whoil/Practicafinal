package Estructuras;

import modelo.mapa.Cueva;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrafoTest {

    private static class DatoSinOrden {
        private final String id;

        private DatoSinOrden(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object otro) {
            if (!(otro instanceof DatoSinOrden)) {
                return false;
            }
            return id.equals(((DatoSinOrden) otro).id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public String toString() {
            return id;
        }
    }

    @Test
    void addNodoEvitaDuplicadosUsandoEquals() {
        Grafo<DatoSinOrden> grafo = new Grafo<>();

        assertTrue(grafo.addNodo(new DatoSinOrden("nivel-1")));
        assertFalse(grafo.addNodo(new DatoSinOrden("nivel-1")));

        assertEquals(1, grafo.getNumeroNodos());
    }

    @Test
    void rechazaNodosYArcosConDatosNulos() {
        Grafo<String> grafo = new Grafo<>();

        assertThrows(IllegalArgumentException.class, () -> grafo.addNodo(null));
        assertThrows(IllegalArgumentException.class, () -> grafo.addArco(null, "cueva-2", "puerta"));
        assertThrows(IllegalArgumentException.class, () -> grafo.addArco("cueva-1", null, "puerta"));
        assertEquals(0, grafo.getNumeroNodos());
        assertEquals(0, grafo.getNumeroArcos());
    }

    @Test
    void addArcoCreaConexionDirigida() {
        Grafo<String> grafo = new Grafo<>();

        assertTrue(grafo.addArco("cueva-1", "cueva-2", "puerta-1-2"));

        assertTrue(grafo.existeConexion("cueva-1", "cueva-2"));
        assertFalse(grafo.existeConexion("cueva-2", "cueva-1"));
        assertEquals("[cueva-2]", grafo.getAdyacentes("cueva-1").toString());
        assertEquals("[]", grafo.getAdyacentes("cueva-2").toString());
    }

    @Test
    void addArcoEvitaDuplicadosConMismaEtiqueta() {
        Grafo<String> grafo = new Grafo<>();

        assertTrue(grafo.addArco("cueva-1", "cueva-2", "puerta"));
        assertFalse(grafo.addArco("cueva-1", "cueva-2", "puerta"));
        assertTrue(grafo.addArco("cueva-1", "cueva-2", "portal"));

        assertEquals(2, grafo.getNumeroArcos());
        assertTrue(grafo.existeArco("cueva-1", "cueva-2", "puerta"));
        assertTrue(grafo.existeArco("cueva-1", "cueva-2", "portal"));
    }

    @Test
    void recorridoBFSRespetaDireccion() {
        Grafo<String> grafo = new Grafo<>();
        grafo.addArco("cueva-1", "cueva-2", "puerta-1-2");
        grafo.addArco("cueva-2", "cueva-3", "puerta-2-3");

        assertEquals("[cueva-1, cueva-2, cueva-3]", grafo.recorridoBFS("cueva-1").toString());
        assertEquals("[cueva-2, cueva-3]", grafo.recorridoBFS("cueva-2").toString());
        assertEquals("[cueva-3]", grafo.recorridoBFS("cueva-3").toString());
    }

    @Test
    void caminoMinimoYDistanciaEntreCuevas() {
        Grafo<String> grafo = new Grafo<>();
        grafo.addArco("cueva-1", "cueva-2", "puerta-1-2");
        grafo.addArco("cueva-2", "cueva-3", "puerta-2-3");
        grafo.addArco("cueva-1", "cueva-3", "atajo");

        assertEquals("[cueva-1, cueva-3]", grafo.caminoMinimo("cueva-1", "cueva-3").toString());
        assertEquals(1, grafo.getDistanciaMinima("cueva-1", "cueva-3"));
        assertTrue(grafo.existeCamino("cueva-1", "cueva-3"));
        assertFalse(grafo.existeCamino("cueva-3", "cueva-1"));
        assertEquals(-1, grafo.getDistanciaMinima("cueva-3", "cueva-1"));
    }

    @Test
    void funcionaConCuevaComoDatoDelGrafo() {
        Grafo<Cueva> grafo = new Grafo<>();
        Cueva nivel1 = new Cueva("nivel-1", 2, 2);
        Cueva nivel2 = new Cueva("nivel-2", 2, 2);
        Cueva nivel3 = new Cueva("nivel-3", 2, 2);

        grafo.addArco(nivel1, nivel2, "puerta-1-2");
        grafo.addArco(nivel2, nivel3, "puerta-2-3");

        assertEquals(3, grafo.getNumeroNodos());
        assertEquals(2, grafo.getNumeroArcos());
        assertTrue(grafo.existeCamino(nivel1, nivel3));
        assertFalse(grafo.existeCamino(nivel3, nivel1));
        assertEquals(2, grafo.getDistanciaMinima(nivel1, nivel3));
    }

    @Test
    void getNodosYGetArcosDevuelvenCopiasDeListas() {
        Grafo<String> grafo = new Grafo<>();
        grafo.addArco("cueva-1", "cueva-2", "puerta");

        ListaSE<String> nodos = grafo.getNodos();
        ListaSE<ArcoGrafo<String>> arcos = grafo.getArcos();
        nodos.clear();
        arcos.clear();

        assertEquals(2, grafo.getNumeroNodos());
        assertEquals(1, grafo.getNumeroArcos());
    }
}
