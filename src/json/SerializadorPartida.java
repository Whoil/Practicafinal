package json;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Serializa y deserializa el estado completo de una partida a/desde JSON.
 *
 * Sigue el mismo patron que CargadorConfiguracion: usa Gson para leer
 * y escribir DTOs (DatosPartidaDTO, DatosMazmorraDTO, etc.) desde/hacia
 * ficheros JSON. La escritura usa pretty printing para que el fichero
 * sea legible y depurable.
 *
 * El formato de guardado contiene:
 * - Mazmorra con todas las cuevas, su matriz actual, enemigos y objetos
 * - Jugador con atributos, inventario y equipo
 * - Estado global (en curso, victoria o derrota)
 * - Turnos restantes
 */
public class SerializadorPartida {

    private static final String VERSION_ACTUAL = "1.0";

    /**
     * Guarda el estado de una partida en un fichero JSON.
     *
     * @param partida estado de la partida a guardar
     * @param ruta    ruta del fichero donde escribir
     * @throws IOException si hay error de escritura
     */
    public static void guardar(DatosPartidaDTO partida, String ruta) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter escritor = new FileWriter(ruta)) {
            gson.toJson(partida, escritor);
        }
    }

    /**
     * Carga el estado de una partida desde un fichero JSON.
     *
     * @param ruta ruta del fichero a leer
     * @return estado de la partida deserializado
     * @throws IOException          si hay error de lectura
     * @throws JsonSyntaxException  si el JSON no es valido
     */
    public static DatosPartidaDTO cargar(String ruta) throws IOException {
        Gson gson = new Gson();
        try (FileReader lector = new FileReader(ruta)) {
            DatosPartidaDTO partida = gson.fromJson(lector, DatosPartidaDTO.class);
            if (partida == null) {
                throw new IllegalArgumentException(
                        "El fichero no contiene datos de partida validos: " + ruta);
            }
            return partida;
        }
    }

    public static String getVersionActual() {
        return VERSION_ACTUAL;
    }
}
