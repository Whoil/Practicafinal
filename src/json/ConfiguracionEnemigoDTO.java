package json;

/**
 * Datos de un enemigo definidos en el JSON de configuracion.
 * Parte B decidira como convertir estos datos en objetos Enemigo o Boss.
 */
public class ConfiguracionEnemigoDTO {
    private String idCueva;
    private String tipo;
    private int fila;
    private int columna;
    private int vida;
    private int ataque;
    private int defensa;
    private int movimiento;

    public String getTipo() {
        return tipo;
    }

    public String getIdCueva() {
        return idCueva;
    }

    void setIdCueva(String idCueva) {
        this.idCueva = idCueva;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getVida() {
        return vida;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getMovimiento() {
        return movimiento;
    }
}
