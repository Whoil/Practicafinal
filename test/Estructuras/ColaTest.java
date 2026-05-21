package Estructuras;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColaTest {

    @Test
    void respetaOrdenFifo() {
        Cola<Integer> cola = new Cola<>();

        cola.offer(1);
        cola.offer(2);
        cola.offer(3);

        assertEquals(1, cola.poll());
        assertEquals(2, cola.poll());
        assertEquals(3, cola.poll());
        assertTrue(cola.isEmpty());
    }

    @Test
    void peekConsultaSinEliminar() {
        Cola<String> cola = new Cola<>();
        cola.offer("a");
        cola.offer("b");

        assertEquals("a", cola.peek());
        assertEquals(2, cola.getSize());
        assertEquals("a", cola.poll());
    }

    @Test
    void colaVaciaDevuelveNull() {
        Cola<Integer> cola = new Cola<>();

        assertNull(cola.peek());
        assertNull(cola.poll());
        assertEquals(0, cola.getSize());
    }

    @Test
    void clearVaciaLaCola() {
        Cola<Integer> cola = new Cola<>();
        cola.offer(1);
        cola.offer(2);

        cola.clear();

        assertTrue(cola.isEmpty());
        assertEquals(0, cola.getSize());
        assertNull(cola.poll());
    }
}
