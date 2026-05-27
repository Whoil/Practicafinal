package modelo.juego;

/**
 * Acumula las estadisticas jugables de una partida.
 *
 * La clase vive dentro del modelo para que los datos no dependan de que la UI
 * invoque una accion por teclado, raton o boton. Partida registra aqui los
 * eventos importantes y esta clase calcula la puntuacion final con una formula
 * unica y facil de probar.
 */
public class EstadisticasPartida {
    private int turnosJugados;
    private int danoRecibido;
    private int danoEjercido;
    private int enemigosMuertos;
    private int bossesMuertos;
    private boolean malakorDerrotado;

    public EstadisticasPartida() {
        this.turnosJugados = 0;
        this.danoRecibido = 0;
        this.danoEjercido = 0;
        this.enemigosMuertos = 0;
        this.bossesMuertos = 0;
        this.malakorDerrotado = false;
    }

    public EstadisticasPartida(EstadisticasPartida otra) {
        this();
        if (otra != null) {
            this.turnosJugados = otra.turnosJugados;
            this.danoRecibido = otra.danoRecibido;
            this.danoEjercido = otra.danoEjercido;
            this.enemigosMuertos = otra.enemigosMuertos;
            this.bossesMuertos = otra.bossesMuertos;
            this.malakorDerrotado = otra.malakorDerrotado;
        }
    }

    public void registrarTurnoJugado() {
        turnosJugados++;
    }

    public void registrarDanoRecibido(int cantidad) {
        if (cantidad > 0) {
            danoRecibido += cantidad;
        }
    }

    public void registrarDanoEjercido(int cantidad) {
        if (cantidad > 0) {
            danoEjercido += cantidad;
        }
    }

    public void registrarEnemigoComunMuerto() {
        enemigosMuertos++;
    }

    public void registrarBossMuerto() {
        bossesMuertos++;
        malakorDerrotado = true;
    }

    public int calcularPuntuacion(boolean victoria) {
        int puntuacion = 0;
        puntuacion += enemigosMuertos * 100;
        puntuacion += bossesMuertos * 500;
        puntuacion += danoEjercido;
        puntuacion -= danoRecibido * 2;
        puntuacion -= turnosJugados * 5;
        if (victoria) {
            puntuacion += 1000;
        }
        return Math.max(0, puntuacion);
    }

    public String calcularTitulo(boolean victoria) {
        int puntuacion = calcularPuntuacion(victoria);
        String titulo;
        if (puntuacion < 0) {
            titulo = "Escudero Novato";
        } else if (puntuacion < 800) {
            titulo = "Hidalgo Aventurero";
        } else if (puntuacion < 1500) {
            titulo = "Caballero de la Orden";
        } else if (puntuacion < 2500) {
            titulo = "Paladín del Reino";
        } else {
            titulo = "Maestro Hechicero";
        }
        if (malakorDerrotado) {
            titulo += " (Salvador del Reino)";
        }
        return titulo;
    }

    public int getTurnosJugados() {
        return turnosJugados;
    }

    public int getDanoRecibido() {
        return danoRecibido;
    }

    public int getDanoEjercido() {
        return danoEjercido;
    }

    public int getEnemigosMuertos() {
        return enemigosMuertos;
    }

    public int getBossesMuertos() {
        return bossesMuertos;
    }

    public boolean isMalakorDerrotado() {
        return malakorDerrotado;
    }
}
