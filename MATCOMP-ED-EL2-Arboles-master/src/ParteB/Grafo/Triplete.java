package ParteB.Grafo;

public class Triplete implements Comparable<Triplete> {
    private String s;
    private String p;
    private String o;

    public Triplete() {
        this.s = null;
        this.p = null;
        this.o = null;
    }

    public Triplete(String s, String p, String o) {
        this.s = s;
        this.p = p;
        this.o = o;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    @Override
    public int compareTo(Triplete otro) {
        return this.toString().compareTo(otro.toString());
    }

    @Override
    public String toString() {
        return "(" + s + ", " + p + ", " + o + ")";
    }
}
