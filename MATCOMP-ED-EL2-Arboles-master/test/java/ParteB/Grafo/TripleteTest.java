package ParteB.Grafo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TripleteTest {

    @Test
    void constructorVacio() {
        Triplete triplete = new Triplete();

        assertNull(triplete.getS());
        assertNull(triplete.getP());
        assertNull(triplete.getO());
    }

    @Test
    void constructorYGetters() {
        Triplete triplete = new Triplete("persona:Einstein", "nace_en", "lugar:Ulm");

        assertEquals("persona:Einstein", triplete.getS());
        assertEquals("nace_en", triplete.getP());
        assertEquals("lugar:Ulm", triplete.getO());
    }

    @Test
    void setters() {
        Triplete triplete = new Triplete();

        triplete.setS("persona:Einstein");
        triplete.setP("nace_en");
        triplete.setO("lugar:Ulm");

        assertEquals("persona:Einstein", triplete.getS());
        assertEquals("nace_en", triplete.getP());
        assertEquals("lugar:Ulm", triplete.getO());
    }

    @Test
    void compareTo() {
        Triplete t1 = new Triplete("A", "dato", "B");
        Triplete t2 = new Triplete("C", "dato", "D");
        Triplete t3 = new Triplete("A", "dato", "B");

        assertTrue(t1.compareTo(t2) < 0);
        assertTrue(t2.compareTo(t1) > 0);
        assertEquals(0, t1.compareTo(t3));
    }

    @Test
    void toStringTriplete() {
        Triplete triplete = new Triplete("persona:Einstein", "nace_en", "lugar:Ulm");

        assertEquals("(persona:Einstein, nace_en, lugar:Ulm)", triplete.toString());
    }
}
