package Estructuras;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListaDETest {

    @Test
    void addYAddLastMantienenElOrdenEsperado() {
        ListaDE<Integer> lista = new ListaDE<>();

        lista.add(2);
        lista.add(1);
        lista.addLast(3);

        assertEquals(3, lista.getSize());
        assertEquals(1, lista.getPrimero());
        assertEquals(3, lista.getUltimo());
        assertEquals("[1, 2, 3]", lista.toString());
    }

    @Test
    void getYExisteDatoUsanEqualsSinComparable() {
        ListaDE<ValorSinComparable> lista = new ListaDE<>();
        ValorSinComparable valor = new ValorSinComparable("objeto-1");

        lista.addLast(valor);

        assertSame(valor, lista.get(new ValorSinComparable("objeto-1")));
        assertTrue(lista.existeDato(new ValorSinComparable("objeto-1")));
        assertFalse(lista.existeDato(new ValorSinComparable("objeto-2")));
    }

    @Test
    void getPorPosicionDevuelveNullSiNoExiste() {
        ListaDE<Integer> lista = new ListaDE<>();
        lista.addLast(10);

        assertNull(lista.get(-1));
        assertNull(lista.get(1));
        assertEquals(10, lista.get(0));
    }

    @Test
    void borraPrimeroUltimoYPorValor() {
        ListaDE<Integer> lista = new ListaDE<>();
        lista.addLast(1);
        lista.addLast(2);
        lista.addLast(3);
        lista.addLast(4);

        assertEquals(1, lista.delFirst());
        assertEquals(4, lista.delLast());
        assertEquals(2, lista.del(2));

        assertEquals(1, lista.getSize());
        assertEquals(3, lista.getPrimero());
        assertEquals(3, lista.getUltimo());
        assertNull(lista.del(99));
    }

    @Test
    void borrarEnListaVaciaDevuelveNull() {
        ListaDE<Integer> lista = new ListaDE<>();

        assertNull(lista.delFirst());
        assertNull(lista.delLast());
        assertNull(lista.del(1));
        assertTrue(lista.isEmpty());
    }

    @Test
    void copyCreaOtraListaYClearSoloVaciaOriginal() {
        ListaDE<Integer> lista = new ListaDE<>();
        lista.addLast(1);
        lista.addLast(2);

        ListaDE<Integer> copia = lista.copy();
        lista.clear();

        assertTrue(lista.isEmpty());
        assertEquals(0, lista.getSize());
        assertEquals("[1, 2]", copia.toString());
    }

    @Test
    void iteradorRecorreEnOrden() {
        ListaDE<Integer> lista = new ListaDE<>();
        lista.addLast(1);
        lista.addLast(2);

        MiIterador<Integer> iterador = lista.getIterador();

        assertTrue(iterador.hasNext());
        assertEquals(1, iterador.next());
        assertTrue(iterador.hasNext());
        assertEquals(2, iterador.next());
        assertFalse(iterador.hasNext());
        assertNull(iterador.next());
    }

    private static class ValorSinComparable {
        private final String id;

        private ValorSinComparable(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object otro) {
            if (this == otro) {
                return true;
            }
            if (!(otro instanceof ValorSinComparable)) {
                return false;
            }
            ValorSinComparable valor = (ValorSinComparable) otro;
            return id.equals(valor.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }
}
