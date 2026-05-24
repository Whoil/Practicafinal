package modelo.juego;

/**
 * Resultado inmutable de un impacto de Bola de Fuego.
 *
 * Permite que JavaFX anime y reproduzca sonidos sin tocar directamente
 * enemigos internos de Partida.
 */
public class ResultadoImpactoBolaFuego {

    private final boolean impacto;
    private final String nombreEnemigo;
    private final int danoReal;
    private final boolean enemigoMuerto;
    private final boolean boss;

    private ResultadoImpactoBolaFuego(boolean impacto, String nombreEnemigo,
                                      int danoReal, boolean enemigoMuerto, boolean boss) {
        this.impacto = impacto;
        this.nombreEnemigo = nombreEnemigo;
        this.danoReal = danoReal;
        this.enemigoMuerto = enemigoMuerto;
        this.boss = boss;
    }

    public static ResultadoImpactoBolaFuego sinImpacto() {
        return new ResultadoImpactoBolaFuego(false, null, 0, false, false);
    }

    public static ResultadoImpactoBolaFuego impacto(String nombreEnemigo, int danoReal,
                                                     boolean enemigoMuerto, boolean boss) {
        return new ResultadoImpactoBolaFuego(true, nombreEnemigo, danoReal, enemigoMuerto, boss);
    }

    public boolean hayImpacto() {
        return impacto;
    }

    public String getNombreEnemigo() {
        return nombreEnemigo;
    }

    public int getDanoReal() {
        return danoReal;
    }

    public boolean isEnemigoMuerto() {
        return enemigoMuerto;
    }

    public boolean isBoss() {
        return boss;
    }
}
