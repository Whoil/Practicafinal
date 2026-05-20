package ParteB.Grafo;

import java.io.IOException;

public class MainPractica {
    public static void main(String[] args) throws IOException {
        CargadorJSON cargador = new CargadorJSON();

        // Cargamos un grafo conectado
        GrafoConocimiento conectado = cargador.cargarGrafo("datos/conectado.json");
        System.out.println("Conectado es disjunto:");
        System.out.println(conectado.isDisjunto());

        // Cargamos un grafo disjunto
        GrafoConocimiento disjunto = cargador.cargarGrafo("datos/disjunto.json");
        System.out.println("Disjunto es disjunto:");
        System.out.println(disjunto.isDisjunto());

        // Cargamos el grafo de premios nobel
        GrafoConocimiento nobel = cargador.cargarGrafo("datos/nobel.json");

        System.out.println("Camino minimo Einstein Ulm:");
        System.out.println(nobel.caminoMinimo("persona:Einstein", "lugar:Ulm"));
        System.out.println("Fisicos nacidos como Einstein:");
        System.out.println(nobel.getFisicosNacidosComoEinstein());

        // Anadimos la tripleta de Antonio
        nobel.addTriplete(new Triplete("persona:Antonio", "nace_en", "lugar:Villarrubia de los Caballeros"));

        System.out.println("Lugares nacimiento premios Nobel:");
        System.out.println(nobel.getLugaresNacimientoPremiosNobel());

        // Tipos de nodos del grafo
        System.out.println("Tipos de nodos:");
        System.out.println(nobel.getTiposNodos());
    }
}
