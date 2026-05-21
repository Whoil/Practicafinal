package json;

/**
 * Datos de una cueva leidos del JSON de configuracion.
 * Cada cueva contiene su matriz de tipos de celda representada como
 * String[filas][columnas], ademas de listas de enemigos y objetos
 * que la poblaran al iniciar la partida (gestionado por Parte B).
 */
public class ConfiguracionCuevaDTO {
    private String id;
    private String nombre;
    private String dificultad;
    private int filas;
    private int columnas;
    private String[][] matriz;
    private ConfiguracionEnemigoDTO[] enemigos;
    private ConfiguracionObjetoDTO[] objetos;

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDificultad() {
        return dificultad;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public String[][] getMatriz() {
        return matriz;
    }

    public ConfiguracionEnemigoDTO[] getEnemigos() {
        return enemigos;
    }

    public ConfiguracionObjetoDTO[] getObjetos() {
        return objetos;
    }
}
