package json;

import java.time.LocalDateTime;

import modelo.juego.EstadisticasPartida;

/**
 * Entrada persistida en ranking.json.
 *
 * Es un DTO plano para Gson: contiene el resumen final de una partida y las
 * estadisticas usadas para calcular puntuacion y titulo.
 */
public class ResultadoPartidaDTO {
    private String nombreJugador;
    private boolean victoria;
    private int turnosJugados;
    private int danoRecibido;
    private int danoEjercido;
    private int enemigosMuertos;
    private int bossesMuertos;
    private boolean malakorDerrotado;
    private int puntuacion;
    private String titulo;
    private String fecha;

    public ResultadoPartidaDTO() {
    }

    public ResultadoPartidaDTO(String nombreJugador, boolean victoria,
                               EstadisticasPartida estadisticas) {
        EstadisticasPartida copia = new EstadisticasPartida(estadisticas);
        this.nombreJugador = textoOValor(nombreJugador, "Mago Errante");
        this.victoria = victoria;
        this.turnosJugados = copia.getTurnosJugados();
        this.danoRecibido = copia.getDanoRecibido();
        this.danoEjercido = copia.getDanoEjercido();
        this.enemigosMuertos = copia.getEnemigosMuertos();
        this.bossesMuertos = copia.getBossesMuertos();
        this.malakorDerrotado = copia.isMalakorDerrotado();
        this.puntuacion = copia.calcularPuntuacion(victoria);
        this.titulo = copia.calcularTitulo(victoria);
        this.fecha = LocalDateTime.now().toString();
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public boolean isVictoria() {
        return victoria;
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

    public int getPuntuacion() {
        return puntuacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getFecha() {
        return fecha;
    }

    private static String textoOValor(String texto, String valorPorDefecto) {
        if (texto == null || texto.trim().isEmpty()) {
            return valorPorDefecto;
        }
        return texto.trim();
    }
}
