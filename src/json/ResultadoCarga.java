package json;

import Estructuras.ListaSE;
import modelo.juego.Mazmorra;

/**
 * Resultado de cargar la configuracion JSON de la mazmorra.
 *
 * Contiene la Mazmorra completamente construida (con cuevas, matriz de
 * celdas y conexiones del grafo) ademas de las configuraciones de
 * enemigos y objetos extraidas del JSON.
 *
 * Parte B usa estas listas para instanciar los personajes, objetos y
 * puertas de la partida sin mezclar reglas de juego dentro del cargador.
 */
public class ResultadoCarga {
    private final Mazmorra mazmorra;
    private final ListaSE<ConfiguracionEnemigoDTO> enemigos;
    private final ListaSE<ConfiguracionObjetoDTO> objetos;
    private final ListaSE<ConexionDTO> conexiones;

    public ResultadoCarga(Mazmorra mazmorra,
                          ListaSE<ConfiguracionEnemigoDTO> enemigos,
                          ListaSE<ConfiguracionObjetoDTO> objetos,
                          ListaSE<ConexionDTO> conexiones) {
        this.mazmorra = mazmorra;
        this.enemigos = enemigos;
        this.objetos = objetos;
        this.conexiones = conexiones;
    }

    public Mazmorra getMazmorra() {
        return mazmorra;
    }

    public ListaSE<ConfiguracionEnemigoDTO> getEnemigos() {
        return enemigos;
    }

    public ListaSE<ConfiguracionObjetoDTO> getObjetos() {
        return objetos;
    }

    public ListaSE<ConexionDTO> getConexiones() {
        return conexiones;
    }
}
