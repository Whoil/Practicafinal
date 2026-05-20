package ParteA;

import Estructuras.ListaSE;

public class Main {
    public static void main(String[] args) {

        ArbolBinarioDeBusqueda<Integer> arbol = new ArbolBinarioDeBusqueda<>();

        arbol.add(8);
        arbol.add(3);
        arbol.add(10);
        arbol.add(1);
        arbol.add(6);

        System.out.println("ÁRBOL BINARIO DE BÚSQUEDA");
        System.out.println("------------------------");

        System.out.println("Altura del árbol: " + arbol.getAlturaRaiz());
        System.out.println("Grado del árbol: " + arbol.getGrado());

        System.out.println("Preorden: " + arbol.getListaPreOrden());
        System.out.println("Orden central: " + arbol.getListaOrdenCentral());
        System.out.println("Postorden: " + arbol.getListaPostOrden());

        System.out.println("Camino hasta 6: " + arbol.getCamino(6));
        System.out.println("Datos del nivel 1: " + arbol.getListaDatosNivel(1));
        System.out.println("Datos del nivel 2: " + arbol.getListaDatosNivel(2));
        System.out.println("Datos del nivel 3: " + arbol.getListaDatosNivel(3));

        System.out.println("¿Árbol homogéneo?: " + arbol.isArbolHomogeneo());
        System.out.println("¿Árbol completo?: " + arbol.isArbolCompleto());
        System.out.println("¿Árbol casi completo?: " + arbol.isArbolCasiCompleto());
        System.out.println("¿Árbol equilibrado?: " + arbol.isEquilibrado());

        System.out.println("Subárbol izquierdo en orden central: " + arbol.getSubArbolIzquierda().getListaOrdenCentral());
        System.out.println("Subárbol derecho en orden central: " + arbol.getSubArbolDerecha().getListaOrdenCentral());


        ArbolBinarioDeBusqueda<Integer> arbolEquilibrado = new ArbolBinarioDeBusqueda<>();

        arbolEquilibrado.add(1);
        arbolEquilibrado.add(2);
        arbolEquilibrado.add(3);
        arbolEquilibrado.add(4);
        arbolEquilibrado.add(5);

        System.out.println();
        System.out.println("PRUEBA DE EQUILIBRADO");
        System.out.println("---------------------");
        System.out.println("Orden central: " + arbolEquilibrado.getListaOrdenCentral());
        System.out.println("Preorden: " + arbolEquilibrado.getListaPreOrden());
        System.out.println("Altura del árbol: " + arbolEquilibrado.getAlturaRaiz());
        System.out.println("¿Árbol equilibrado?: " + arbolEquilibrado.isEquilibrado());

        System.out.println();
        System.out.println("PRUEBA ÁRBOL ENTEROS DE EL 0 AL 128");
        System.out.println("-------------------------------------");
        ArbolBinarioDeBusquedaEnteros arbolE = new ArbolBinarioDeBusquedaEnteros();

        for (int i = 0; i <= 128; i++) {
            arbolE.add(i);
        }

        System.out.println("Suma: " + arbolE.getSuma());
        System.out.println("Preorden: " + arbolE.getSumaLista(arbolE.getListaPreOrden()));
        System.out.println("Orden central: " + arbolE.getSumaLista(arbolE.getListaOrdenCentral()));
        System.out.println("Postorden: " + arbolE.getSumaLista(arbolE.getListaPostOrden()));
        System.out.println("Altura: " + arbolE.getAlturaRaiz());
        System.out.println("Camino hasta 110: " + arbolE.getCamino(110));
        System.out.println("Longitud camino: " + (arbolE.getCamino(110).getSize() - 1));

        System.out.println();
        System.out.println("PRUEBA ÁRBOL ENTEROS ALEATORIOS");
        System.out.println("-------------------------------");

        ArbolBinarioDeBusquedaEnteros arbolAleatorio = new ArbolBinarioDeBusquedaEnteros();
        ListaSE<Integer> numeros = new ListaSE<>();

        while (numeros.getSize() < 129) {
            int numero = (int) (Math.random() * 129);

            if (!numeros.existeDato(numero)) {
                numeros.addLast(numero);
            }
        }

        for (int i = 0; i < numeros.getSize(); i++) {
            arbolAleatorio.add(numeros.get(i));
        }

        System.out.println("Números insertados: " + numeros);
        System.out.println("Suma: " + arbolAleatorio.getSuma());
        System.out.println("Preorden: " + arbolAleatorio.getSumaLista(arbolAleatorio.getListaPreOrden()));
        System.out.println("Orden central: " + arbolAleatorio.getSumaLista(arbolAleatorio.getListaOrdenCentral()));
        System.out.println("Postorden: " + arbolAleatorio.getSumaLista(arbolAleatorio.getListaPostOrden()));
        System.out.println("Altura: " + arbolAleatorio.getAlturaRaiz());
        System.out.println("Camino hasta 110: " + arbolAleatorio.getCamino(110));
        System.out.println("Longitud camino: " + (arbolAleatorio.getCamino(110).getSize() - 1));




    }
}
