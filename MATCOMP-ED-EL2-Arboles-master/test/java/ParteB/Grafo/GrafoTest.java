package ParteB.Grafo;

import Estructuras.ListaSE;
import Estructuras.MiIterador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrafoTest {

    private <T extends Comparable<T>> int contarElementos(ListaSE<T> lista) {
        int contador = 0;
        MiIterador<T> iterador = lista.getIterador();

        while (iterador.hasNext()) {
            iterador.next();
            contador++;
        }

        return contador;
    }

    @Test
    void constructorGrafo() {
        Grafo<Integer> grafo = new Grafo<>();

        assertNotNull(grafo.getNodos());
        assertNotNull(grafo.getArcos());
        assertEquals(0, contarElementos(grafo.getNodos()));
        assertEquals(0, contarElementos(grafo.getArcos()));
        assertEquals(1, grafo.getIdNodo());
        assertEquals(1, grafo.getIdArco());
    }

    @Test
    void buscarNodoYExisteNodoCuandoNoHayNodos() {
        Grafo<Integer> grafo = new Grafo<>();

        assertNull(grafo.buscarNodo(10));
        assertFalse(grafo.existeNodo(10));
    }

    @Test
    void addNodoCuandoNoExiste() {
        Grafo<Integer> grafo = new Grafo<>();

        Nodo<Integer> nodo = grafo.addNodo(10);

        assertNotNull(nodo);
        assertEquals(1, nodo.getId());
        assertEquals(10, nodo.getDato());
        assertEquals(1, contarElementos(grafo.getNodos()));
        assertEquals(2, grafo.getIdNodo());
        assertTrue(grafo.existeNodo(10));
        assertEquals(nodo, grafo.buscarNodo(10));
    }

    @Test
    void addNodoCuandoYaExisteNoDuplica() {
        Grafo<Integer> grafo = new Grafo<>();

        Nodo<Integer> primero = grafo.addNodo(10);
        Nodo<Integer> segundo = grafo.addNodo(10);

        assertSame(primero, segundo);
        assertEquals(1, contarElementos(grafo.getNodos()));
        assertEquals(2, grafo.getIdNodo());
    }

    @Test
    void buscarArcoYExisteArcoCuandoNoHayArcos() {
        Grafo<Integer> grafo = new Grafo<>();

        Nodo<Integer> origen = new Nodo<>(1, 10);
        Nodo<Integer> destino = new Nodo<>(2, 20);

        assertNull(grafo.buscarArco(origen, destino, "une"));
        assertFalse(grafo.existeArco(origen, destino, "une"));
    }

    @Test
    void addArcoCreaNodosYArco() {
        Grafo<Integer> grafo = new Grafo<>();

        Arco<Integer> arco = grafo.addArco(10, 20, "une");

        assertNotNull(arco);
        assertEquals(1, arco.getId());
        assertEquals("une", arco.getDato());

        assertEquals(2, contarElementos(grafo.getNodos()));
        assertEquals(1, contarElementos(grafo.getArcos()));

        assertEquals(3, grafo.getIdNodo());
        assertEquals(2, grafo.getIdArco());

        Nodo<Integer> origen = grafo.buscarNodo(10);
        Nodo<Integer> destino = grafo.buscarNodo(20);

        assertNotNull(origen);
        assertNotNull(destino);

        assertEquals(origen, arco.getOrigen());
        assertEquals(destino, arco.getDestino());

        assertEquals(1, contarElementos(origen.getArcosSalida()));
        assertEquals(1, contarElementos(destino.getArcosEntrada()));
    }

    @Test
    void addArcoCuandoYaExisteNoDuplica() {
        Grafo<Integer> grafo = new Grafo<>();

        Arco<Integer> primero = grafo.addArco(10, 20, "une");
        Arco<Integer> segundo = grafo.addArco(10, 20, "une");

        assertSame(primero, segundo);
        assertEquals(2, contarElementos(grafo.getNodos()));
        assertEquals(1, contarElementos(grafo.getArcos()));
        assertEquals(3, grafo.getIdNodo());
        assertEquals(2, grafo.getIdArco());
    }

    @Test
    void buscarArcoYExisteArcoCuandoExiste() {
        Grafo<Integer> grafo = new Grafo<>();

        Arco<Integer> arco = grafo.addArco(10, 20, "une");

        Nodo<Integer> origen = grafo.buscarNodo(10);
        Nodo<Integer> destino = grafo.buscarNodo(20);

        assertEquals(arco, grafo.buscarArco(origen, destino, "une"));
        assertTrue(grafo.existeArco(origen, destino, "une"));
    }

    @Test
    void buscarArcoDevuelveNullSiDatoOrigenODestinoNoCoinciden() {
        Grafo<Integer> grafo = new Grafo<>();

        grafo.addArco(10, 20, "une");

        Nodo<Integer> origenCorrecto = grafo.buscarNodo(10);
        Nodo<Integer> destinoCorrecto = grafo.buscarNodo(20);
        Nodo<Integer> otroNodo = new Nodo<>(99, 99);

        assertNull(grafo.buscarArco(origenCorrecto, destinoCorrecto, "otro"));
        assertNull(grafo.buscarArco(otroNodo, destinoCorrecto, "une"));
        assertNull(grafo.buscarArco(origenCorrecto, otroNodo, "une"));

        assertFalse(grafo.existeArco(origenCorrecto, destinoCorrecto, "otro"));
    }

    @Test
    void getAdyacentesDeNodoExistente() {
        Grafo<Integer> grafo = new Grafo<>();

        grafo.addArco(10, 20, "a");
        grafo.addArco(10, 30, "b");
        grafo.addArco(20, 40, "c");

        ListaSE<Integer> adyacentes = grafo.getAdyacentes(10);

        assertEquals(2, contarElementos(adyacentes));

        MiIterador<Integer> iterador = adyacentes.getIterador();

        assertTrue(iterador.hasNext());
        assertEquals(20, iterador.next());

        assertTrue(iterador.hasNext());
        assertEquals(30, iterador.next());

        assertFalse(iterador.hasNext());
    }

    @Test
    void getAdyacentesDeNodoInexistenteDevuelveListaVacia() {
        Grafo<Integer> grafo = new Grafo<>();

        ListaSE<Integer> adyacentes = grafo.getAdyacentes(999);

        assertNotNull(adyacentes);
        assertEquals(0, contarElementos(adyacentes));
    }

    @Test
    void toStringGrafoVacioDevuelveTextoVacio() {
        Grafo<Integer> grafo = new Grafo<>();

        assertEquals("", grafo.toString());
    }

    @Test
    void toStringGrafoConArcos() {
        Grafo<Integer> grafo = new Grafo<>();

        grafo.addArco(10, 20, "une");
        grafo.addArco(20, 30, "conecta");

        String texto = grafo.toString();

        assertTrue(texto.contains("(1, 10)--une--> (2, 20)"));
        assertTrue(texto.contains("(2, 20)--conecta--> (3, 30)"));
    }
}