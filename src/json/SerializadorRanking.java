package json;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import Estructuras.ListaSE;

/**
 * Lee y escribe el ranking local de partidas terminadas.
 *
 * El fichero se guarda como array JSON simple para que sea facil de revisar a
 * mano. Internamente se usa ListaSE propia para anadir y ordenar entradas sin
 * recurrir a colecciones prohibidas.
 */
public class SerializadorRanking {
    public static final String RUTA_RANKING = "ranking.json";

    public static void guardarResultado(ResultadoPartidaDTO resultado) throws IOException {
        guardarResultado(resultado, RUTA_RANKING);
    }

    public static void guardarResultado(ResultadoPartidaDTO resultado, String ruta) throws IOException {
        if (resultado == null) {
            return;
        }
        ListaSE<ResultadoPartidaDTO> entradas = leerComoLista(ruta);
        entradas.addLast(resultado);
        escribir(convertirArray(entradas), ruta);
    }

    public static ResultadoPartidaDTO[] leerRanking() throws IOException {
        return leerRanking(RUTA_RANKING);
    }

    public static ResultadoPartidaDTO[] leerRanking(String ruta) throws IOException {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return new ResultadoPartidaDTO[0];
        }
        Gson gson = new Gson();
        try (FileReader lector = new FileReader(archivo)) {
            ResultadoPartidaDTO[] resultados = gson.fromJson(lector, ResultadoPartidaDTO[].class);
            return resultados != null ? resultados : new ResultadoPartidaDTO[0];
        } catch (JsonSyntaxException ex) {
            return new ResultadoPartidaDTO[0];
        }
    }

    public static ResultadoPartidaDTO[] obtenerTop10() throws IOException {
        return obtenerTop10(RUTA_RANKING);
    }

    public static ResultadoPartidaDTO[] obtenerTop10(String ruta) throws IOException {
        ListaSE<ResultadoPartidaDTO> ordenados = ordenarPorPuntuacion(leerComoLista(ruta));
        int limite = ordenados.getSize() < 10 ? ordenados.getSize() : 10;
        ResultadoPartidaDTO[] top = new ResultadoPartidaDTO[limite];
        for (int i = 0; i < limite; i++) {
            top[i] = ordenados.get(i);
        }
        return top;
    }

    private static ListaSE<ResultadoPartidaDTO> leerComoLista(String ruta) throws IOException {
        ResultadoPartidaDTO[] array = leerRanking(ruta);
        ListaSE<ResultadoPartidaDTO> lista = new ListaSE<>();
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                lista.addLast(array[i]);
            }
        }
        return lista;
    }

    private static void escribir(ResultadoPartidaDTO[] resultados, String ruta) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter escritor = new FileWriter(ruta)) {
            gson.toJson(resultados, escritor);
        }
    }

    private static ResultadoPartidaDTO[] convertirArray(ListaSE<ResultadoPartidaDTO> lista) {
        ResultadoPartidaDTO[] array = new ResultadoPartidaDTO[lista.getSize()];
        for (int i = 0; i < lista.getSize(); i++) {
            array[i] = lista.get(i);
        }
        return array;
    }

    private static ListaSE<ResultadoPartidaDTO> ordenarPorPuntuacion(ListaSE<ResultadoPartidaDTO> origen) {
        ListaSE<ResultadoPartidaDTO> restantes = origen.copy();
        ListaSE<ResultadoPartidaDTO> ordenados = new ListaSE<>();
        while (!restantes.isEmpty()) {
            ResultadoPartidaDTO mejor = buscarMejor(restantes);
            ordenados.addLast(mejor);
            restantes.del(mejor);
        }
        return ordenados;
    }

    private static ResultadoPartidaDTO buscarMejor(ListaSE<ResultadoPartidaDTO> lista) {
        ResultadoPartidaDTO mejor = lista.get(0);
        for (int i = 1; i < lista.getSize(); i++) {
            ResultadoPartidaDTO actual = lista.get(i);
            if (actual != null && (mejor == null || actual.getPuntuacion() > mejor.getPuntuacion())) {
                mejor = actual;
            }
        }
        return mejor;
    }
}
