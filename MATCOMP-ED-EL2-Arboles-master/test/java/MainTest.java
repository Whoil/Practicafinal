import ParteA.ArbolBinarioDeBusqueda;
import ParteA.ArbolBinarioDeBusquedaEnteros;
import ParteA.Nodo;
import Estructuras.ListaSE;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class MainTest {
    private ArbolBinarioDeBusqueda<Integer> crearArbol() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(8);
        arbol.add(3);
        arbol.add(10);
        arbol.add(1);
        arbol.add(6);
        return arbol;
    }

    @Test
    void arbolVacio() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();

        assertTrue(arbol.isEmpty());
        assertEquals(0, arbol.getAlturaRaiz());
        assertEquals(0, arbol.getGrado());
        assertTrue(arbol.isArbolHomogeneo());
        assertTrue(arbol.isArbolCompleto());
        assertTrue(arbol.isArbolCasiCompleto());
        assertTrue(arbol.isEquilibrado());
    }

    @Test
    void alturaYGrado() {
        ArbolBinarioDeBusqueda<Integer> arbol = crearArbol();

        assertEquals(3, arbol.getAlturaRaiz());
        assertEquals(2, arbol.getGrado());
    }

    @Test
    void recorridos() {
        ArbolBinarioDeBusqueda<Integer> arbol = crearArbol();

        assertEquals("[8, 3, 1, 6, 10]", arbol.getListaPreOrden().toString());
        assertEquals("[1, 3, 6, 8, 10]", arbol.getListaOrdenCentral().toString());
        assertEquals("[1, 6, 3, 10, 8]", arbol.getListaPostOrden().toString());
    }

    @Test
    void camino() {
        ArbolBinarioDeBusqueda<Integer> arbol = crearArbol();

        assertEquals("[8, 3, 6]", arbol.getCamino(6).toString());
        assertEquals("[8, 10]", arbol.getCamino(10).toString());
        assertEquals("[]", arbol.getCamino(99).toString());
    }

    @Test
    void niveles() {
        ArbolBinarioDeBusqueda<Integer> arbol = crearArbol();

        assertEquals("[8]", arbol.getListaDatosNivel(1).toString());
        assertEquals("[3, 10]", arbol.getListaDatosNivel(2).toString());
        assertEquals("[1, 6]", arbol.getListaDatosNivel(3).toString());
    }

    @Test
    void tiposDeArbol() {
        ArbolBinarioDeBusqueda<Integer> arbol = crearArbol();

        assertTrue(arbol.isArbolHomogeneo());
        assertFalse(arbol.isArbolCompleto());
        assertTrue(arbol.isArbolCasiCompleto());
        assertTrue(arbol.isEquilibrado());
    }

    @Test
    void subArboles() {
        ArbolBinarioDeBusqueda<Integer> arbol = crearArbol();

        assertEquals("[1, 3, 6]", arbol.getSubArbolIzquierda().getListaOrdenCentral().toString());
        assertEquals("[10]", arbol.getSubArbolDerecha().getListaOrdenCentral().toString());
    }

    @Test
    void nodo() {
        Nodo<Integer> nodo = new Nodo<>(5);

        assertEquals(5, nodo.getDato());
        assertNull(nodo.getIzquierda());
        assertNull(nodo.getDerecha());

        Nodo<Integer> izq = new Nodo<>(3);
        Nodo<Integer> der = new Nodo<>(8);

        nodo.setIzquierda(izq);
        nodo.setDerecha(der);
        nodo.setDato(6);

        assertEquals(6, nodo.getDato());
        assertEquals(izq, nodo.getIzquierda());
        assertEquals(der, nodo.getDerecha());
        assertTrue(nodo.compareTo(new Nodo<>(7)) < 0);
        assertTrue(nodo.compareTo(new Nodo<>(4)) > 0);
        assertEquals(0, nodo.compareTo(new Nodo<>(6)));
    }



    @Test
    void getRaiz() {
        ArbolBinarioDeBusqueda<Integer> arbol = crearArbol();
        assertEquals(8, arbol.getRaiz().getDato());
    }
    @Test
    void subArbolesVacios() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        assertTrue(arbol.getSubArbolIzquierda().isEmpty());
        assertTrue(arbol.getSubArbolDerecha().isEmpty());
    }
    @Test
    void arbolNoHomogeneo() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(1);
        arbol.add(2);
        arbol.add(3);
        arbol.add(4);

        assertFalse(arbol.isArbolHomogeneo());
    }

    @Test
    void giroDerecha() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(3);
        arbol.add(2);
        arbol.add(1);

        assertTrue(arbol.isEquilibrado());
        assertEquals(2, arbol.getRaiz().getDato());
    }

    @Test
    void giroIzquierda() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(1);
        arbol.add(2);
        arbol.add(3);

        assertTrue(arbol.isEquilibrado());
        assertEquals(2, arbol.getRaiz().getDato());
    }


    @Test
    void giroIzquierdaDerecha() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(3);
        arbol.add(1);
        arbol.add(2);

        assertTrue(arbol.isEquilibrado());
        assertEquals(2, arbol.getRaiz().getDato());
    }


    @Test
    void giroDerechaIzquierda() {
        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.add(1);
        arbol.add(3);
        arbol.add(2);

        assertTrue(arbol.isEquilibrado());
        assertEquals(2, arbol.getRaiz().getDato());
    }

    @Test
    void arbolEnterosVacio() {
        ArbolBinarioDeBusquedaEnteros arbol = new ArbolBinarioDeBusquedaEnteros();
        ListaSE<Integer> lista = new ListaSE<>();

        assertEquals(0, arbol.getSuma());
        assertEquals(0, arbol.getSumaLista(lista));
    }

    @Test
    void arbolEnterosSuma() {
        ArbolBinarioDeBusquedaEnteros arbol = new ArbolBinarioDeBusquedaEnteros();

        for (int i = 0; i <= 128; i++) {
            arbol.add(i);
        }

        assertEquals(8256, arbol.getSuma());
        assertEquals(8256, arbol.getSumaLista(arbol.getListaPreOrden()));
        assertEquals(8256, arbol.getSumaLista(arbol.getListaOrdenCentral()));
        assertEquals(8256, arbol.getSumaLista(arbol.getListaPostOrden()));
    }

    @Test
    void arbolEnterosSumaSubArboles() {
        ArbolBinarioDeBusquedaEnteros arbol = new ArbolBinarioDeBusquedaEnteros();

        for (int i = 0; i <= 128; i++) {
            arbol.add(i);
        }

        int sumaIzquierda = arbol.getSumaLista(arbol.getSubArbolIzquierda().getListaOrdenCentral());
        int sumaDerecha = arbol.getSumaLista(arbol.getSubArbolDerecha().getListaOrdenCentral());
        int sumaTotal = sumaIzquierda + sumaDerecha + arbol.getRaiz().getDato();

        assertEquals(arbol.getSuma(), sumaTotal);
    }

}
