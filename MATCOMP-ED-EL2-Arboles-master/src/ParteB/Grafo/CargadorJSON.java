package ParteB.Grafo;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;

public class CargadorJSON {

    public DatosGrafo leerDatos(String ruta) throws IOException {
        Gson gson = new Gson();
        FileReader lector = new FileReader(ruta);
        DatosGrafo datos = gson.fromJson(lector, DatosGrafo.class);
        lector.close();
        return datos;
    }

    public GrafoConocimiento cargarGrafo(String ruta) throws IOException {
        DatosGrafo datos = leerDatos(ruta);
        GrafoConocimiento grafo = new GrafoConocimiento();

        if (datos.getTripletas() != null) {
            for (int i = 0; i < datos.getTripletas().length; i++) {
                grafo.addTriplete(datos.getTripletas()[i]);
            }
        }

        return grafo;
    }
}
