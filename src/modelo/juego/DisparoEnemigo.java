package modelo.juego;

/**
 * Evento de disparo enemigo consumido por la UI para animar el proyectil.
 */
public class DisparoEnemigo {
    private final int filaOrigen;
    private final int columnaOrigen;
    private final int filaDestino;
    private final int columnaDestino;
    private final int dano;
    private final String nombreEnemigo;

    public DisparoEnemigo(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino,
                          int dano, String nombreEnemigo) {
        this.filaOrigen = filaOrigen;
        this.columnaOrigen = columnaOrigen;
        this.filaDestino = filaDestino;
        this.columnaDestino = columnaDestino;
        this.dano = dano;
        this.nombreEnemigo = nombreEnemigo;
    }

    public int getFilaOrigen() {
        return filaOrigen;
    }

    public int getColumnaOrigen() {
        return columnaOrigen;
    }

    public int getFilaDestino() {
        return filaDestino;
    }

    public int getColumnaDestino() {
        return columnaDestino;
    }

    public int getDano() {
        return dano;
    }

    public String getNombreEnemigo() {
        return nombreEnemigo;
    }
}
