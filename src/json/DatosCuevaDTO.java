package json;

/**
 * Estado actual de una cueva para guardado/recuperacion.
 *
 * La matriz de tipos de celda se almacena como String[][] igual que en
 * el JSON de configuracion, pero aqui puede reflejar cambios ocurridos
 * durante la partida (puertas abiertas, trampas activadas, etc.).
 * Los enemigos y objetos del mapa guardan su estado actual (vida,
 * posicion, recogido, etc.).
 */
public class DatosCuevaDTO {
    private String id;
    private String nombre;
    private int filas;
    private int columnas;
    private String[][] matriz;
    private DatosEnemigoDTO[] enemigos;
    private DatosObjetoDTO[] objetos;

    public DatosCuevaDTO() {
    }

    public DatosCuevaDTO(String id, String nombre, int filas, int columnas,
                         String[][] matriz, DatosEnemigoDTO[] enemigos,
                         DatosObjetoDTO[] objetos) {
        this.id = id;
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.matriz = matriz;
        this.enemigos = enemigos;
        this.objetos = objetos;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
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

    public DatosEnemigoDTO[] getEnemigos() {
        return enemigos;
    }

    public DatosObjetoDTO[] getObjetos() {
        return objetos;
    }
}
