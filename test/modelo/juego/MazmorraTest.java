package modelo.juego;

import Estructuras.ListaSE;
import modelo.mapa.Cueva;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MazmorraTest {

    private Cueva cueva(String id) {
        return new Cueva(id, 2, 2);
    }

    @Test
    void addCuevaRegistraCuevasYFijaPrimeraComoActual() {
        Mazmorra mazmorra = new Mazmorra();
        Cueva nivel1 = cueva("nivel-1");
        Cueva nivel2 = cueva("nivel-2");

        assertTrue(mazmorra.addCueva(nivel1));
        assertTrue(mazmorra.addCueva(nivel2));
        assertFalse(mazmorra.addCueva(new Cueva("nivel-1", 3, 3)));

        assertEquals(2, mazmorra.getNumeroCuevas());
        assertEquals(nivel1, mazmorra.getCuevaActual());
        assertTrue(mazmorra.existeCueva(nivel2));
    }

    @Test
    void rechazaOperacionesConCuevasNulas() {
        Mazmorra mazmorra = new Mazmorra();
        Cueva nivel1 = cueva("nivel-1");

        assertFalse(mazmorra.addCueva(null));
        assertFalse(mazmorra.conectarCuevas(null, nivel1, "puerta"));
        assertFalse(mazmorra.conectarCuevas(nivel1, null, "puerta"));
        assertFalse(mazmorra.existeCueva(null));
        assertFalse(mazmorra.existeConexion(null, nivel1));
        assertTrue(mazmorra.getCuevasSiguientes(null).isEmpty());
        assertFalse(mazmorra.setCuevaActual(null));
        assertFalse(mazmorra.avanzarACueva(null));
        assertFalse(mazmorra.existeCamino(null, nivel1));
        assertTrue(mazmorra.getCaminoMinimo(null, nivel1).isEmpty());
        assertEquals(-1, mazmorra.getDistanciaMinima(null, nivel1));
        assertEquals(0, mazmorra.getNumeroCuevas());
    }

    @Test
    void getCuevaPorIdRechazaIdsInvalidos() {
        Mazmorra mazmorra = new Mazmorra();

        assertNull(mazmorra.getCuevaPorId(null));
        assertNull(mazmorra.getCuevaPorId(""));
        assertNull(mazmorra.getCuevaPorId("   "));
    }

    @Test
    void conectarCuevasCreaPuertasDirigidas() {
        Mazmorra mazmorra = new Mazmorra();
        Cueva nivel1 = cueva("nivel-1");
        Cueva nivel2 = cueva("nivel-2");

        assertTrue(mazmorra.conectarCuevas(nivel1, nivel2, "puerta-1-2"));

        assertEquals(2, mazmorra.getNumeroCuevas());
        assertEquals(1, mazmorra.getNumeroConexiones());
        assertTrue(mazmorra.existeConexion(nivel1, nivel2));
        assertFalse(mazmorra.existeConexion(nivel2, nivel1));
        assertEquals("[nivel-2]", ids(mazmorra.getCuevasSiguientes(nivel1)));
    }

    @Test
    void getCuevaPorIdDevuelveLaRegistrada() {
        Mazmorra mazmorra = new Mazmorra();
        Cueva nivel1 = cueva("nivel-1");
        mazmorra.addCueva(nivel1);

        assertSame(nivel1, mazmorra.getCuevaPorId("nivel-1"));
        assertNull(mazmorra.getCuevaPorId("no-existe"));
    }

    @Test
    void setCuevaActualSoloAceptaCuevasRegistradas() {
        Mazmorra mazmorra = new Mazmorra();
        Cueva nivel1 = cueva("nivel-1");
        Cueva nivel2 = cueva("nivel-2");
        mazmorra.addCueva(nivel1);

        assertFalse(mazmorra.setCuevaActual(nivel2));
        assertEquals(nivel1, mazmorra.getCuevaActual());

        mazmorra.addCueva(nivel2);
        assertTrue(mazmorra.setCuevaActual(nivel2));
        assertTrue(mazmorra.esCuevaActual(new Cueva("nivel-2", 4, 4)));
    }

    @Test
    void avanzarACuevaRespetaConexionDirectaYDireccion() {
        Mazmorra mazmorra = new Mazmorra();
        Cueva nivel1 = cueva("nivel-1");
        Cueva nivel2 = cueva("nivel-2");
        Cueva nivel3 = cueva("nivel-3");
        mazmorra.conectarCuevas(nivel1, nivel2, "puerta-1-2");
        mazmorra.conectarCuevas(nivel2, nivel3, "puerta-2-3");

        assertEquals(nivel1, mazmorra.getCuevaActual());
        assertFalse(mazmorra.avanzarACueva(nivel3));
        assertEquals(nivel1, mazmorra.getCuevaActual());

        assertTrue(mazmorra.avanzarACueva(nivel2));
        assertEquals(nivel2, mazmorra.getCuevaActual());
        assertFalse(mazmorra.avanzarACueva(nivel1));
    }

    @Test
    void caminoYDistanciaEntreCuevasUsanGrafoDirigido() {
        Mazmorra mazmorra = new Mazmorra();
        Cueva nivel1 = cueva("nivel-1");
        Cueva nivel2 = cueva("nivel-2");
        Cueva nivel3 = cueva("nivel-3");
        mazmorra.conectarCuevas(nivel1, nivel2, "puerta-1-2");
        mazmorra.conectarCuevas(nivel2, nivel3, "puerta-2-3");

        assertTrue(mazmorra.existeCamino(nivel1, nivel3));
        assertFalse(mazmorra.existeCamino(nivel3, nivel1));
        assertEquals(2, mazmorra.getDistanciaMinima(nivel1, nivel3));
        assertEquals("[nivel-1, nivel-2, nivel-3]", ids(mazmorra.getCaminoMinimo(nivel1, nivel3)));
    }

    @Test
    void caminoMasLargoEnGrafoConBifurcacion() {
        Mazmorra mazmorra = new Mazmorra();
        Cueva a = cueva("A");
        Cueva b = cueva("B");
        Cueva c = cueva("C");
        Cueva d = cueva("D");
        mazmorra.addCueva(a);
        mazmorra.addCueva(b);
        mazmorra.addCueva(c);
        mazmorra.addCueva(d);
        mazmorra.conectarCuevas(a, b, "ab");
        mazmorra.conectarCuevas(a, c, "ac");
        mazmorra.conectarCuevas(b, d, "bd");
        mazmorra.conectarCuevas(c, d, "cd");

        assertTrue(mazmorra.existeCamino(a, d));
        assertEquals(2, mazmorra.getDistanciaMinima(a, d));
        assertEquals(4, mazmorra.getNumeroCuevas());
        assertEquals(4, mazmorra.getNumeroConexiones());
    }

    @Test
    void grafoDisconexoNoTieneCamino() {
        Mazmorra mazmorra = new Mazmorra();
        Cueva a = cueva("A");
        Cueva b = cueva("B");
        mazmorra.addCueva(a);
        mazmorra.addCueva(b);

        assertFalse(mazmorra.existeCamino(a, b));
        assertTrue(mazmorra.getCaminoMinimo(a, b).isEmpty());
        assertEquals(-1, mazmorra.getDistanciaMinima(a, b));
    }

    @Test
    void caminoMinimoACuevaMismaEsVacio() {
        Mazmorra mazmorra = new Mazmorra();
        Cueva a = cueva("A");
        mazmorra.addCueva(a);

        assertTrue(mazmorra.existeCamino(a, a));
        assertEquals(0, mazmorra.getDistanciaMinima(a, a));
        assertTrue(mazmorra.getCaminoMinimo(a, a).isEmpty());
    }

    @Test
    void cicloEnGrafoNoBloqueaCamino() {
        Mazmorra mazmorra = new Mazmorra();
        Cueva a = cueva("A");
        Cueva b = cueva("B");
        Cueva c = cueva("C");
        mazmorra.addCueva(a);
        mazmorra.addCueva(b);
        mazmorra.addCueva(c);
        mazmorra.conectarCuevas(a, b, "ab");
        mazmorra.conectarCuevas(b, c, "bc");
        mazmorra.conectarCuevas(c, a, "ca");

        assertTrue(mazmorra.existeCamino(a, c));
        assertEquals(2, mazmorra.getDistanciaMinima(a, c));
    }

    @Test
    void getCuevasDevuelveCopiaDeLaLista() {
        Mazmorra mazmorra = new Mazmorra();
        mazmorra.addCueva(cueva("nivel-1"));

        ListaSE<Cueva> cuevas = mazmorra.getCuevas();
        cuevas.clear();

        assertEquals(1, mazmorra.getNumeroCuevas());
    }

    private String ids(ListaSE<Cueva> cuevas) {
        String texto = "[";
        for (int indice = 0; indice < cuevas.getSize(); indice++) {
            texto += cuevas.get(indice).getId();
            if (indice < cuevas.getSize() - 1) {
                texto += ", ";
            }
        }
        return texto + "]";
    }
}
