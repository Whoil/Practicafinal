package json;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

import Estructuras.ListaSE;
import modelo.juego.Mazmorra;
import modelo.mapa.Celda;
import modelo.mapa.Cueva;
import modelo.mapa.TipoCelda;

/**
 * Lee el fichero datos/cuevas.json y construye los objetos del modelo.
 *
 * Gson deserializa el JSON en objetos DTO (ConfiguracionMazmorra,
 * ConfiguracionCuevaDTO, etc.) y despues este cargador traduce esos DTOs
 * a las clases reales del juego: Cueva, Mazmorra y conexiones del grafo.
 *
 * Las configuraciones de enemigos y objetos se devuelven en el
 * ResultadoCarga para que Parte B monte la Partida real sin meter reglas
 * de juego dentro de la capa JSON.
 */
public class CargadorConfiguracion {

    /**
     * Punto de entrada principal: recibe la ruta al JSON y devuelve
     * un ResultadoCarga con la Mazmorra construida y los datos de
     * enemigos y objetos de cada cueva.
     */
    public ResultadoCarga cargar(String ruta) throws IOException {
        Gson gson = new Gson();

        ConfiguracionMazmorra config;
        try (FileReader lector = new FileReader(ruta)) {
            config = gson.fromJson(lector, ConfiguracionMazmorra.class);
        }

        if (config == null) {
            throw new IllegalArgumentException("El fichero JSON no contiene datos validos: " + ruta);
        }

        Mazmorra mazmorra = new Mazmorra();
        ListaSE<ConfiguracionCuevaDTO> cuevasDTO = new ListaSE<>();
        ListaSE<ConfiguracionEnemigoDTO> todosEnemigos = new ListaSE<>();
        ListaSE<ConfiguracionObjetoDTO> todosObjetos = new ListaSE<>();
        ListaSE<ConexionDTO> todasConexiones = new ListaSE<>();

        ConfiguracionCuevaDTO[] cuevasArray = config.getCuevas();
        if (cuevasArray == null || cuevasArray.length == 0) {
            throw new IllegalArgumentException("El JSON debe contener al menos una cueva");
        }

        /*
         * Primera pasada: crear todas las cuevas con su matriz de celdas.
         * Se recorre la matriz de Strings del JSON y se asigna el
         * TipoCelda correspondiente a cada posicion de la cueva.
         */
        for (ConfiguracionCuevaDTO cuevaDTO : cuevasArray) {
            Cueva cueva = new Cueva(cuevaDTO.getId(), cuevaDTO.getFilas(), cuevaDTO.getColumnas());
            String[][] matrizStr = cuevaDTO.getMatriz();

            for (int fila = 0; fila < cuevaDTO.getFilas(); fila++) {
                for (int columna = 0; columna < cuevaDTO.getColumnas(); columna++) {
                    String tipoStr = matrizStr[fila][columna];
                    TipoCelda tipo = convertirATipoCelda(tipoStr, fila, columna);
                    cueva.cambiarTipoCelda(fila, columna, tipo);
                }
            }

            mazmorra.addCueva(cueva);
            cuevasDTO.addLast(cuevaDTO);

            /*
             * Acumular enemigos y objetos de esta cueva para devolverlos
             * en el resultado. Se asocian al id de la cueva para que
             * Parte B pueda colocarlos correctamente.
             */
            if (cuevaDTO.getEnemigos() != null) {
                for (ConfiguracionEnemigoDTO enemigo : cuevaDTO.getEnemigos()) {
                    enemigo.setIdCueva(cuevaDTO.getId());
                    todosEnemigos.addLast(enemigo);
                }
            }
            if (cuevaDTO.getObjetos() != null) {
                for (ConfiguracionObjetoDTO objeto : cuevaDTO.getObjetos()) {
                    objeto.setIdCueva(cuevaDTO.getId());
                    todosObjetos.addLast(objeto);
                }
            }
        }

        /*
         * Segunda pasada: crear las conexiones entre cuevas.
         * Las conexiones usan los ids definidos en el JSON.
         */
        ConexionDTO[] conexiones = config.getConexiones();
        if (conexiones != null) {
            for (ConexionDTO conexion : conexiones) {
                Cueva origen = mazmorra.getCuevaPorId(conexion.getOrigen());
                Cueva destino = mazmorra.getCuevaPorId(conexion.getDestino());
                if (origen == null || destino == null) {
                    throw new IllegalArgumentException("Conexion invalida en JSON: "
                            + conexion.getOrigen() + " -> " + conexion.getDestino());
                }
                mazmorra.conectarCuevas(origen, destino, conexion.getEtiqueta());
                todasConexiones.addLast(conexion);
            }
        }

        return new ResultadoCarga(mazmorra, todosEnemigos, todosObjetos, todasConexiones);
    }

    /**
     * Convierte un String del JSON al enum TipoCelda.
     * Si el String no corresponde a ningun tipo conocido, se usa SUELO
     * como valor seguro por defecto en lugar de lanzar una excepcion
     * que detendria la carga completa.
     */
    private TipoCelda convertirATipoCelda(String tipo, int fila, int columna) {
        if (tipo == null || tipo.isBlank()) {
            return TipoCelda.SUELO;
        }
        try {
            return TipoCelda.valueOf(tipo);
        } catch (IllegalArgumentException e) {
            System.err.println("Advertencia: tipo de celda desconocido '" + tipo
                    + "' en (" + fila + ", " + columna + "). Se usara SUELO.");
            return TipoCelda.SUELO;
        }
    }
}
