package ParteB.Grafo;

import Estructuras.ListaSE;
import Estructuras.MiIterador;



public class GrafoConocimiento extends Grafo<String> { // En este grafo vamos a usar la estructura del Grafo TAD que hemos creado anteriormente

    public void addTriplete(Triplete triplete) {
        // El sujeto es el origen, el objeto es el destino y el predicado es el dato del arco
        addArco(triplete.getS(), triplete.getO(), triplete.getP());
    }

    public String buscarObjeto(String sujeto, String predicado) {
        MiIterador<Arco<String>> iterador = getArcos().getIterador();
        while (iterador.hasNext()) {
            Arco<String> actual = iterador.next();
            if (actual.getOrigen().getDato().compareTo(sujeto) == 0  && actual.getDato().compareTo(predicado) == 0) {
                return actual.getDestino().getDato();
            }
        }
        return null;
    }

    public boolean tienePredicado(String sujeto, String predicado) {
        MiIterador<Arco<String>> iterador = getArcos().getIterador();
        while (iterador.hasNext()) {
            Arco<String> actual = iterador.next();
            if (actual.getOrigen().getDato().compareTo(sujeto) == 0 && actual.getDato().compareTo(predicado) == 0) { // al recorrer los arcos, si encontramos un origen que tenga al sujeto como dato
                // y el dato del arco coincide con el predicado, devuelve true
                return true;
            }
        }

        return false;
    }

    public boolean existeTriplete(String sujeto, String predicado, String objeto) {
        MiIterador<Arco<String>> iterador = getArcos().getIterador();

        while (iterador.hasNext()) {
            Arco<String> actual = iterador.next(); // recorremos los arcos con un iterador

            if (actual.getOrigen().getDato().compareTo(sujeto) == 0 && actual.getDato().compareTo(predicado) == 0
                    && actual.getDestino().getDato().compareTo(objeto) == 0) { // Si el sujeto, predicado y objeto coinciden con los del triplete, devuelve true
                return true;
            }
        }

        return false;
    }

    public String obtenerTipo(String dato) {
        String[] partes = dato.split(":"); // divide el texto en tipo y dato

        if (partes.length < 2) { // si no se ha podido dividir es que no hay tipo
            return "sin_tipo";
        }
        return partes[0]; // si se ha podido dividir devuelve el tipo, en primer lugar
    }

    public ListaSE<String> getTiposNodos() {
        ListaSE<String> tipos = new ListaSE<>();
        MiIterador<Nodo<String>> iterador = getNodos().getIterador();

        while (iterador.hasNext()) { // recorremos los nodos del grafo
            Nodo<String> actual = iterador.next();
            String tipo = obtenerTipo(actual.getDato()); // obtenemos el tipo de dato del nodo

            if (!tipos.existeDato(tipo)) { // si el tipo no esta ya en la lista, lo añadimos
                tipos.addLast(tipo);
            }
        }
        return tipos;
    }

    public ListaSE<String> getFisicosNacidosComoEinstein() {
        ListaSE<String> respuesta = new ListaSE<>();
        String ciudadEinstein = buscarObjeto("persona:Einstein", "nace_en"); // Buscamos la ciudad en la que nace Einstein
        MiIterador<Nodo<String>> iterador = getNodos().getIterador();

        while (iterador.hasNext()) {
            String persona = iterador.next().getDato();
            if (obtenerTipo(persona).compareTo("persona") == 0 && persona.compareTo("persona:Einstein") != 0) { //Buscamos a una persona distinta de Einstein

                boolean esFisico = existeTriplete(persona, "profesion", "profesion:Fisico");
                boolean esNobel = existeTriplete(persona, "premio", "premio:NobelFisica");
                boolean mismaCiudad = existeTriplete(persona, "nace_en", ciudadEinstein);

                if (esFisico && esNobel && mismaCiudad) { // Si la persona es fisico, nobel y nacio en la misma ciudad, lo añadimos a la lista
                    respuesta.addLast(persona);
                }
            }
        }
        return respuesta;
    }

    public ListaSE<String> getLugaresNacimientoPremiosNobel() {
        ListaSE<String> lugares = new ListaSE<>();
        MiIterador<Nodo<String>> iterador = getNodos().getIterador();
        while (iterador.hasNext()) {

            String persona = iterador.next().getDato();
            if (obtenerTipo(persona).compareTo("persona") == 0) { // comprobar que es persona
                boolean tienePremioNobel = tienePredicado(persona, "premio"); // comprobar que tiene un premio
                String lugar = buscarObjeto(persona, "nace_en");
                if (tienePremioNobel && lugar != null && !lugares.existeDato(lugar)) { // si el lugar de nacimiento no esta ya en lugares, se añade
                    lugares.addLast(lugar);
                }
            }
        }
        return lugares;
    }
}
