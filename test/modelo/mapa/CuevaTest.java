package modelo.mapa;

import Estructuras.ListaSE;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CuevaTest {

    @Test
    void creaMatrizConListasPropias() {
        Cueva cueva = new Cueva("nivel-1", 3, 4);

        assertEquals("nivel-1", cueva.getId());
        assertEquals(3, cueva.getFilas());
        assertEquals(4, cueva.getColumnas());
        assertEquals(3, cueva.getMatriz().getSize());

        ListaSE<Celda> primeraFila = cueva.getMatriz().get(0);
        assertNotNull(primeraFila);
        assertEquals(4, primeraFila.getSize());
    }

    @Test
    void getMatrizNoPermiteRomperLaEstructuraInterna() {
        Cueva cueva = new Cueva("nivel-1", 2, 2);
        ListaSE<ListaSE<Celda>> matrizExterna = cueva.getMatriz();

        matrizExterna.clear();
        matrizExterna.get(0);

        assertEquals(2, cueva.getMatriz().getSize());
        assertEquals(cueva.getCelda(1, 1), cueva.getMatriz().get(1).get(1));
    }

    @Test
    void accedeACeldasPorFilaYColumna() {
        Cueva cueva = new Cueva("nivel-1", 2, 2);

        Celda celda = cueva.getCelda(1, 0);

        assertEquals(1, celda.getFila());
        assertEquals(0, celda.getColumna());
        assertEquals(TipoCelda.SUELO, celda.getTipo());
    }

    @Test
    void cambiaTipoDeCelda() {
        Cueva cueva = new Cueva("nivel-1", 2, 2);

        cueva.cambiarTipoCelda(0, 1, TipoCelda.MURO);

        assertEquals(TipoCelda.MURO, cueva.getCelda(0, 1).getTipo());
        assertFalse(cueva.esTransitable(0, 1));
    }

    @Test
    void validaLimitesDeLaCueva() {
        Cueva cueva = new Cueva("nivel-1", 2, 3);

        assertTrue(cueva.estaDentro(0, 0));
        assertTrue(cueva.estaDentro(1, 2));
        assertFalse(cueva.estaDentro(-1, 0));
        assertFalse(cueva.estaDentro(2, 0));
        assertFalse(cueva.estaDentro(0, 3));
        assertThrows(IndexOutOfBoundsException.class, () -> cueva.getCelda(2, 0));
    }

    @Test
    void rechazaDimensionesInvalidas() {
        assertThrows(IllegalArgumentException.class, () -> new Cueva("nivel-1", 0, 3));
        assertThrows(IllegalArgumentException.class, () -> new Cueva("nivel-1", 3, 0));
    }

    @Test
    void rechazaIdNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () -> new Cueva(null, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> new Cueva("", 2, 2));
        assertThrows(IllegalArgumentException.class, () -> new Cueva("   ", 2, 2));
    }

    @Test
    void cuevasConMismoIdSonIgualesParaElGrafo() {
        Cueva original = new Cueva("nivel-1", 2, 2);
        Cueva misma = new Cueva("nivel-1", 4, 4);
        Cueva distinta = new Cueva("nivel-2", 2, 2);

        assertEquals(original, misma);
        assertNotEquals(original, distinta);
    }

    @Test
    void bfsDevuelveCeldasAlcanzablesSinDiagonales() {
        Cueva cueva = new Cueva("nivel-1", 3, 3);

        ListaSE<Celda> alcanzables = cueva.getCeldasAlcanzables(1, 1, 1);

        assertEquals(5, alcanzables.getSize());
        assertTrue(alcanzables.existeDato(cueva.getCelda(1, 1)));
        assertTrue(alcanzables.existeDato(cueva.getCelda(0, 1)));
        assertTrue(alcanzables.existeDato(cueva.getCelda(2, 1)));
        assertTrue(alcanzables.existeDato(cueva.getCelda(1, 0)));
        assertTrue(alcanzables.existeDato(cueva.getCelda(1, 2)));
        assertFalse(alcanzables.existeDato(cueva.getCelda(0, 0)));
    }

    @Test
    void bfsRespetaMurosYLimiteDePasos() {
        Cueva cueva = new Cueva("nivel-1", 3, 3);
        cueva.cambiarTipoCelda(1, 2, TipoCelda.MURO);
        cueva.cambiarTipoCelda(2, 1, TipoCelda.MURO);

        ListaSE<Celda> alcanzables = cueva.getCeldasAlcanzables(1, 1, 2);

        assertTrue(alcanzables.existeDato(cueva.getCelda(1, 1)));
        assertTrue(alcanzables.existeDato(cueva.getCelda(0, 1)));
        assertTrue(alcanzables.existeDato(cueva.getCelda(1, 0)));
        assertTrue(alcanzables.existeDato(cueva.getCelda(0, 0)));
        assertTrue(alcanzables.existeDato(cueva.getCelda(0, 2)));
        assertTrue(alcanzables.existeDato(cueva.getCelda(2, 0)));
        assertFalse(alcanzables.existeDato(cueva.getCelda(1, 2)));
        assertFalse(alcanzables.existeDato(cueva.getCelda(2, 1)));
        assertFalse(alcanzables.existeDato(cueva.getCelda(2, 2)));
    }

    @Test
    void bfsDevuelveListaVaciaSiOrigenNoSirve() {
        Cueva cueva = new Cueva("nivel-1", 2, 2);
        cueva.cambiarTipoCelda(0, 0, TipoCelda.MURO);

        assertTrue(cueva.getCeldasAlcanzables(0, 0, 2).isEmpty());
        assertTrue(cueva.getCeldasAlcanzables(-1, 0, 2).isEmpty());
        assertTrue(cueva.getCeldasAlcanzables(0, 0, -1).isEmpty());
    }

    @Test
    void bfsConRangoCeroSoloDevuelveLaCeldaInicial() {
        Cueva cueva = new Cueva("nivel-1", 3, 3);

        ListaSE<Celda> alcanzables = cueva.getCeldasAlcanzables(1, 1, 0);

        assertEquals(1, alcanzables.getSize());
        assertEquals(cueva.getCelda(1, 1), alcanzables.get(0));
    }

    @Test
    void bfsNoRepiteCeldasAunqueHayaVariosCaminos() {
        Cueva cueva = new Cueva("nivel-1", 2, 2);

        ListaSE<Celda> alcanzables = cueva.getCeldasAlcanzables(0, 0, 2);

        assertEquals(4, alcanzables.getSize());
        assertTrue(alcanzables.existeDato(cueva.getCelda(0, 0)));
        assertTrue(alcanzables.existeDato(cueva.getCelda(0, 1)));
        assertTrue(alcanzables.existeDato(cueva.getCelda(1, 0)));
        assertTrue(alcanzables.existeDato(cueva.getCelda(1, 1)));
    }

    @Test
    void caminoMinimoIncluyeOrigenYDestino() {
        Cueva cueva = new Cueva("nivel-1", 3, 3);

        ListaSE<Celda> camino = cueva.getCaminoMinimo(0, 0, 0, 2);

        assertEquals(3, camino.getSize());
        assertEquals(cueva.getCelda(0, 0), camino.get(0));
        assertEquals(cueva.getCelda(0, 1), camino.get(1));
        assertEquals(cueva.getCelda(0, 2), camino.get(2));
        assertEquals(2, cueva.getDistanciaMinima(0, 0, 0, 2));
    }

    @Test
    void caminoMinimoRodeaMuros() {
        Cueva cueva = new Cueva("nivel-1", 3, 3);
        cueva.cambiarTipoCelda(0, 1, TipoCelda.MURO);

        ListaSE<Celda> camino = cueva.getCaminoMinimo(0, 0, 0, 2);

        assertEquals(5, camino.getSize());
        assertEquals(cueva.getCelda(0, 0), camino.get(0));
        assertEquals(cueva.getCelda(0, 2), camino.get(4));
        assertEquals(4, cueva.getDistanciaMinima(0, 0, 0, 2));
        assertFalse(camino.existeDato(cueva.getCelda(0, 1)));
    }

    @Test
    void caminoMinimoDevuelveVacioSiNoHayCamino() {
        Cueva cueva = new Cueva("nivel-1", 2, 2);
        cueva.cambiarTipoCelda(0, 1, TipoCelda.MURO);
        cueva.cambiarTipoCelda(1, 0, TipoCelda.MURO);

        assertTrue(cueva.getCaminoMinimo(0, 0, 1, 1).isEmpty());
        assertEquals(-1, cueva.getDistanciaMinima(0, 0, 1, 1));
    }

    @Test
    void caminoMinimoDevuelveVacioSiOrigenODestinoNoSonValidos() {
        Cueva cueva = new Cueva("nivel-1", 2, 2);
        cueva.cambiarTipoCelda(1, 1, TipoCelda.MURO);

        assertTrue(cueva.getCaminoMinimo(-1, 0, 1, 1).isEmpty());
        assertTrue(cueva.getCaminoMinimo(0, 0, 1, 1).isEmpty());
        assertEquals(-1, cueva.getDistanciaMinima(0, 0, 1, 1));
        assertEquals(-1, cueva.getDistanciaMinima(0, 0, 5, 5));
    }

    @Test
    void caminoMinimoPermiteQuedarseEnLaMismaCelda() {
        Cueva cueva = new Cueva("nivel-1", 2, 2);

        ListaSE<Celda> camino = cueva.getCaminoMinimo(1, 1, 1, 1);

        assertEquals(1, camino.getSize());
        assertEquals(cueva.getCelda(1, 1), camino.get(0));
        assertEquals(0, cueva.getDistanciaMinima(1, 1, 1, 1));
    }
}
