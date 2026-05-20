package ParteB.Grafo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArcoTest {

    @Test
    void constructorYGetters() {
        Nodo<Integer> origen = new Nodo<>(1, 10);
        Nodo<Integer> destino = new Nodo<>(2, 20);

        Arco<Integer> arco = new Arco<>(5, "nace_en", origen, destino);

        assertEquals(5, arco.getId());
        assertEquals("nace_en", arco.getDato());
        assertEquals(origen, arco.getOrigen());
        assertEquals(destino, arco.getDestino());
    }

    @Test
    void setters() {
        Nodo<Integer> origen = new Nodo<>(1, 10);
        Nodo<Integer> destino = new Nodo<>(2, 20);
        Nodo<Integer> nuevoOrigen = new Nodo<>(3, 30);
        Nodo<Integer> nuevoDestino = new Nodo<>(4, 40);

        Arco<Integer> arco = new Arco<>(5, "nace_en", origen, destino);

        arco.setId(8);
        arco.setDato("vive_en");
        arco.setOrigen(nuevoOrigen);
        arco.setDestino(nuevoDestino);

        assertEquals(8, arco.getId());
        assertEquals("vive_en", arco.getDato());
        assertEquals(nuevoOrigen, arco.getOrigen());
        assertEquals(nuevoDestino, arco.getDestino());
    }

    @Test
    void compareToDevuelveMenorMayorEIgual() {
        Nodo<Integer> origen = new Nodo<>(1, 10);
        Nodo<Integer> destino = new Nodo<>(2, 20);

        Arco<Integer> a1 = new Arco<>(1, "a", origen, destino);
        Arco<Integer> a2 = new Arco<>(2, "b", origen, destino);
        Arco<Integer> a3 = new Arco<>(1, "c", origen, destino);

        assertEquals(-1, a1.compareTo(a2));
        assertEquals(1, a2.compareTo(a1));
        assertEquals(0, a1.compareTo(a3));
    }

    @Test
    void toStringArco() {
        Nodo<Integer> origen = new Nodo<>(1, 10);
        Nodo<Integer> destino = new Nodo<>(2, 20);

        Arco<Integer> arco = new Arco<>(5, "nace_en", origen, destino);

        assertEquals("(1, 10)--nace_en--> (2, 20)", arco.toString());
    }
}