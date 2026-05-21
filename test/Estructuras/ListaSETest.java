package Estructuras;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListaSETest {

    @Test
    void addYAddLastMantienenElOrdenEsperado() {
        ListaSE<Integer> lista = new ListaSE<>();

        lista.add(2);
        lista.add(1);
        lista.addLast(3);

        assertEquals(3, lista.getSize());
        assertEquals(1, lista.get(0));
        assertEquals(2, lista.get(1));
        assertEquals(3, lista.get(2));
    }

    @Test
    void getYExisteDatoUsanEqualsSinComparable() {
        ListaSE<String> lista = new ListaSE<>();

        lista.addLast(new String("celda"));

        assertEquals("celda", lista.get(new String("celda")));
        assertTrue(lista.existeDato(new String("celda")));
        assertFalse(lista.existeDato("otra"));
    }

    @Test
    void borraPrimeroUltimoYPorValor() {
        ListaSE<Integer> lista = new ListaSE<>();
        lista.addLast(1);
        lista.addLast(2);
        lista.addLast(3);

        assertEquals(1, lista.delFirst());
        assertEquals(3, lista.delLast());
        assertEquals(2, lista.del(2));
        assertTrue(lista.isEmpty());
    }

    @Test
    void copyCreaOtraListaEInvertirCambiaOrden() {
        ListaSE<Integer> lista = new ListaSE<>();
        lista.addLast(1);
        lista.addLast(2);
        lista.addLast(3);

        ListaSE<Integer> copia = lista.copy();
        lista.invertir();

        assertEquals("[1, 2, 3]", copia.toString());
        assertEquals("[3, 2, 1]", lista.toString());
    }

    @Test
    void clearVaciaLaLista() {
        ListaSE<Integer> lista = new ListaSE<>();
        lista.addLast(1);
        lista.addLast(2);

        lista.clear();

        assertTrue(lista.isEmpty());
        assertEquals(0, lista.getSize());
        assertNull(lista.get(0));
    }
}
