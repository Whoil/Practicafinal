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
 * Parte B usara las listas de enemigos y objetos para instanciar los
 * personajes y objetos del juego cuando llegue el momento.
 */
public class ResultadoCarga {
    private final Mazmorra mazmorra;
    private final ListaSE<ConfiguracionEnemigoDTO> enemigos;
    private final ListaSE<ConfiguracionObjetoDTO> objetos;

    public ResultadoCarga(Mazmorra mazmorra,
                          ListaSE<ConfiguracionEnemigoDTO> enemigos,
                          ListaSE<ConfiguracionObjetoDTO> objetos) {
        this.mazmorra = mazmorra;
        this.enemigos = enemigos;
        this.objetos = objetos;
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
}
