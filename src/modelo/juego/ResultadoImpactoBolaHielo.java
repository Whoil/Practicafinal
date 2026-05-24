package modelo.juego;

/**
 * Resultado inmutable de un impacto de Bola de Hielo.
 */
public class ResultadoImpactoBolaHielo {
    private final boolean impacto;
    private final String nombreEnemigo;
    private final int turnosCongelado;

    private ResultadoImpactoBolaHielo(boolean impacto, String nombreEnemigo, int turnosCongelado) {
        this.impacto = impacto;
        this.nombreEnemigo = nombreEnemigo;
        this.turnosCongelado = turnosCongelado;
    }

    public static ResultadoImpactoBolaHielo sinImpacto() {
        return new ResultadoImpactoBolaHielo(false, null, 0);
    }

    public static ResultadoImpactoBolaHielo impacto(String nombreEnemigo, int turnosCongelado) {
        return new ResultadoImpactoBolaHielo(true, nombreEnemigo, turnosCongelado);
    }

    public boolean hayImpacto() {
        return impacto;
    }

    public String getNombreEnemigo() {
        return nombreEnemigo;
    }

    public int getTurnosCongelado() {
        return turnosCongelado;
    }
}
