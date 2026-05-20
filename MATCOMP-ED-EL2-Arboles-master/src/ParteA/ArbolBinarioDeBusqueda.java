package ParteA;
import Estructuras.ListaSE;
import Estructuras.Cola;

public class ArbolBinarioDeBusqueda<T extends Comparable<T>> implements ArbolBinario<T> {


    //ParteA.Nodo raiz del árbol
    private Nodo<T> raiz;

    //Constructor que inicializa el árbol con una raíz dada
    public ArbolBinarioDeBusqueda(Nodo<T> raiz){
        this.raiz=raiz;
    }

    //Constructor vacío
    public ArbolBinarioDeBusqueda() {
        this.raiz = null;
    }

    //Devuelve la raíz del árbol
    public Nodo<T> getRaiz() {
        return raiz;
    }

    //Cambia la raíz del árbol
    public void setRaiz(Nodo<T> raiz) {
        this.raiz = raiz;
    }

    // Comprueba si el árbol está vacío
    public boolean isEmpty(){
        return raiz==null; // devuelve true si no hay nodo raiz ( el árbol es vacío)
    }



    //Inserta un dato comenzando desde la raíz
    public void add(T dato){
        raiz = addNodo(raiz, dato);
    }

    // Inserta un dato en el árbol de forma recursiva
    protected Nodo<T> addNodo(Nodo<T> actual, T dato){
        // Si no hay nodo en esta posición, se crea uno nuevo
        if(actual==null){

            return new Nodo<>(dato);
        }
        //Comparamos de forma recursiva para decidir en qué dirección irá el dato

        // Si el dato es menor, irá a la izquierda
        if(actual.getDato().compareTo(dato)>0){
            actual.setIzquierda(addNodo(actual.getIzquierda(), dato));

        }
        // Si el dato es mayor, irá a la derecha
        else if (actual.getDato().compareTo(dato)<0){
            actual.setDerecha(addNodo(actual.getDerecha(), dato));
        }

        // Este árbol binario de búsqueda se equilibra automáticamente tras cada inserción.
        // Por eso, aunque se inserten los números en orden, el árbol no queda degenerado.
        // Esto afecta especialmente a la altura y al camino hasta un dato concreto.
        return equilibrarArbol(actual);

    }


    //Obtiene la altura de uno de los nodos de manera recursiva
    protected int getAltura(Nodo<T> actual){
        //Si no hay nodos, la altura es 0
        if (actual==null){
            return 0;
        }
        //Calcula la altura de los subárboles de la izquierda y la derecha de forma recursiva
        int alturaIzquierda=getAltura(actual.getIzquierda());
        int alturaDerecha=getAltura(actual.getDerecha());


        //La altura actual es 1 + la mayor de las alturas
        return 1 + Math.max(alturaDerecha,alturaIzquierda);
    }


    //Devuelve la altura del árbol completo
    public int getAlturaRaiz(){
        return getAltura(raiz);
    }

    //Crea una lista y la rellena del recorrido en preorden
    public ListaSE<T> getListaPreOrden(){
        ListaSE<T> DatosPre=new ListaSE<>();
        return getListaPreOrden(raiz, DatosPre);
    }

    //Recorre el árbol en preorden mientras: raíz, izquierda, derecha
   protected ListaSE<T> getListaPreOrden(Nodo<T> actual, ListaSE<T> DatosPre){

        //Caso base, si el nodo es nulo se devuelve la lista acumulada.
        if(actual==null){
            return DatosPre;
        }

        //Se inserta primero la raíz en la lista
       DatosPre.addLast(actual.getDato());
        //Luego recorre el subárbol izquiedo
        getListaPreOrden(actual.getIzquierda(), DatosPre);
        //Finalmente recorre el subárbol derecho.
        getListaPreOrden(actual.getDerecha(), DatosPre);
        return DatosPre;

   }


    // Crea una lista y la rellena con el recorrido en orden central
    public ListaSE<T> getListaOrdenCentral(){
        ListaSE<T> DatosCentral= new ListaSE<>();
        return getListaOrdenCentral(raiz,DatosCentral);
    }

    //Recorre el árbol en orden central: izquierda, ráiz, derecha
   protected ListaSE<T> getListaOrdenCentral(Nodo<T> actual, ListaSE<T> DatosCentral){
       //Caso base, si el nodo es nulo devuelve la lista acumulada.
        if(actual==null){
           return DatosCentral;
       }

        //Recorre el subárbol izquierdo
       getListaOrdenCentral(actual.getIzquierda(), DatosCentral);
        //Añade el dato del nodo actual
       DatosCentral.addLast(actual.getDato());

       // Recorre el subárbol derecho
       getListaOrdenCentral(actual.getDerecha(), DatosCentral);
       return DatosCentral;
   }


    // Crea una lista y la rellena con el recorrido en postorden
   public ListaSE<T> getListaPostOrden(){
        ListaSE<T> DatosPost=new ListaSE<>();
        return getListaPostorden(raiz,DatosPost);
   }

    // Recorre el árbol en postorden: izquierda, derecha, raíz
   protected ListaSE<T> getListaPostorden(Nodo<T> actual, ListaSE<T> DatosPost){
        if(actual==null){
            return DatosPost;
        }
       // Recorre el subárbol izquierdo
        getListaPostorden(actual.getIzquierda(), DatosPost);
       // Recorre el subárbol derecho
        getListaPostorden(actual.getDerecha(), DatosPost);
       // Añade el dato del nodo actual
        DatosPost.addLast(actual.getDato());
        return DatosPost;
   }

    // Devuelve el subárbol derecho del árbol actual
   public ArbolBinarioDeBusqueda<T> getSubArbolDerecha(){
       // Devuelve el subárbol derecho del árbol actual
         ArbolBinarioDeBusqueda<T> SubArbol=new ArbolBinarioDeBusqueda<>(raiz);
       // Si el árbol actual está vacío, se devuelve vacío
        if(isEmpty()){
            return SubArbol ;

        }
       // La raíz del nuevo árbol será el hijo derecho de la raíz actual
        SubArbol.setRaiz(raiz.getDerecha());
        return SubArbol;

   }

    // Devuelve el subárbol izquierdo del árbol actual
   public ArbolBinarioDeBusqueda<T> getSubArbolIzquierda(){
       // Se crea un nuevo árbol inicialmente con raíz nula
        ArbolBinarioDeBusqueda<T> SubArbol= new ArbolBinarioDeBusqueda<>(raiz);

       // Si el árbol actual está vacío, se devuelve vacío
        if(isEmpty()){
            return SubArbol;

        }
       // La raíz del nuevo árbol será el hijo izquierdo de la raíz actual
        SubArbol.setRaiz(raiz.getIzquierda());
        return SubArbol;

   }

    // Calcula el grado del árbol desde un nodo dado
    // El grado de un nodo es el número de hijos que tiene
   protected int getGrado(Nodo<T> actual){
       int gradoActual=0;
       // Si el nodo es nulo, su grado es 0
        if( actual==null){
            return 0;
       }
       // Si tiene hijo izquierdo, suma 1
        if (actual.getIzquierda()!=null) gradoActual++;

       // Si tiene hijo derecho, suma 1
        if (actual.getDerecha()!=null) gradoActual++;

       // Calcula el mayor grado en el subárbol izquierdo
        int gradoIzquierda = getGrado(actual.getIzquierda());

       // Calcula el mayor grado en el subárbol derecho
        int gradoDerecha= getGrado(actual.getDerecha());

       // Devuelve el mayor entre el nodo actual y sus subárboles
        return Math.max(gradoActual,Math.max(gradoDerecha,gradoIzquierda));
   }

    // Devuelve el grado del árbol completo
   public int getGrado(){
        return getGrado(raiz);
   }


    // Devuelve una lista con el camino desde la raíz hasta el dato buscado.
   public ListaSE<T> getCamino(T dato){


       ListaSE<T> camino=new ListaSE<>();

        boolean encontrado = getCamino(raiz, dato,camino);

       // Si el dato no se encuentra, devuelve una lista vacía.
       if(encontrado==false){
           return new ListaSE<>();
       }
        return camino;
    }

    // Método auxiliar recursivo que busca el dato en el árbol binario de búsqueda.
    protected boolean getCamino(Nodo<T> actual, T dato, ListaSE<T> camino) {
        // Recorre recursivamente el árbol desde el nodo actual.
        // Si el nodo es null, el dato no se ha encontrado en esta rama.

        if (actual == null) {
            return false;
        }
        // Va añadiendo a la lista los datos de los nodos por los que pasa.
        camino.addLast(actual.getDato());

        if (actual.getDato().compareTo(dato) == 0) {
            // Si encuentra el dato, devuelve true.
            return true;

        } else if (actual.getDato().compareTo(dato) > 0) {
            // Si el dato buscado es menor, continúa por la izquierda.
            return getCamino(actual.getIzquierda(), dato, camino);

        } else {
            // Si es mayor, continúa por la derecha.
            return getCamino(actual.getDerecha(), dato, camino);

        }
    }

    //Comprueba que el árbol está equilibrado
    public boolean isEquilibrado(){
        return(isEquilibrado(raiz));
    }

    protected boolean isEquilibrado(Nodo<T> actual){
        //Caso base, si no existen más nodos devuelve true
        if (actual==null){
            return true;
        }
        //Se calcula la altura del subárbol derecho
        int alturaDerecha = getAltura(actual.getDerecha());

        //Se calcula la altura del subárbol izquierdo
        int alturaIzquierda= getAltura(actual.getIzquierda());

        //Se comprueba si existe desequilibrio
        if (Math.abs(alturaIzquierda-alturaDerecha)>1){
            return false;
        }

        //Repetimos el proceso de manera recursiva
        return isEquilibrado(actual.getIzquierda()) && isEquilibrado(actual.getDerecha());


    }


    protected int getFactorEquilibrio(Nodo<T> nodo) {
        if (nodo == null) {
            return 0;
        }

        return getAltura(nodo.getIzquierda()) - getAltura(nodo.getDerecha());
    }
    // Hace una rotación a la derecha y devuelve la nueva raíz del subárbol.
    protected Nodo<T> girarDerecha(Nodo<T> nodo) {
        Nodo<T> nuevaRaiz = nodo.getIzquierda();
        Nodo<T> subArbolMovido = nuevaRaiz.getDerecha();

        nuevaRaiz.setDerecha(nodo);
        nodo.setIzquierda(subArbolMovido);

        return nuevaRaiz;
    }
    // Hace una rotación a la izquierda y devuelve la nueva raíz del subárbol.
    protected Nodo<T> girarIzquierda(Nodo<T> nodo) {
        Nodo<T> nuevaRaiz = nodo.getDerecha();
        Nodo<T> subArbolMovido = nuevaRaiz.getIzquierda();

        nuevaRaiz.setIzquierda(nodo);
        nodo.setDerecha(subArbolMovido);

        return nuevaRaiz;
    }
    // Equilibra el subárbol cuya raíz es el nodo indicado.
    // Si el subárbol ya está equilibrado, lo devuelve tal cual.
    // Si no, aplica la rotación que corresponda.
    protected Nodo<T> equilibrarArbol(Nodo<T> nodo) {
        if (nodo == null) {
            return nodo;
        }

        int equilibrio = getFactorEquilibrio(nodo);

        // El árbol pesa demasiado hacia la izquierda
        if (equilibrio > 1) {

            // Caso izquierda-derecha
            if (getFactorEquilibrio(nodo.getIzquierda()) < 0) {
                nodo.setIzquierda(girarIzquierda(nodo.getIzquierda()));
            }

            // Caso izquierda-izquierda
            return girarDerecha(nodo);
        }

        // El árbol pesa demasiado hacia la derecha
        if (equilibrio < -1) {

            // Caso derecha-izquierda
            if (getFactorEquilibrio(nodo.getDerecha()) > 0) {
                nodo.setDerecha(girarDerecha(nodo.getDerecha()));
            }

            // Caso derecha-derecha
            return girarIzquierda(nodo);
        }

        return nodo;
    }


    // Devuelve una lista con los datos que se encuentran en el nivel indicado del árbol.
    public ListaSE<T> getListaDatosNivel(int nivel){
        ListaSE<T> ListaDatosNivel= new ListaSE<>();
        return getListaDatosNivel(raiz, nivel, 1,ListaDatosNivel);
    }

    // Método auxiliar recursivo que recorre el árbol buscando los nodos del nivel indicado.




    private ListaSE<T> getListaDatosNivel(Nodo<T> actual, int nivelBuscado, int nivelActual, ListaSE<T> ListaNivel){
        // Si el nodo actual es null, devuelve la lista acumulada.
        if (actual==null){
            return ListaNivel;
        }
        // Si el nivel actual coincide con el nivel buscado, añade el dato del nodo a la lista.
        if(nivelActual==nivelBuscado){
            ListaNivel.addLast(actual.getDato());
            return ListaNivel;
        }
        // Si no coincide, sigue recorriendo los subárboles izquierdo y derecho.
        getListaDatosNivel(actual.getIzquierda(),nivelBuscado,nivelActual+1,ListaNivel);
        getListaDatosNivel(actual.getDerecha(),nivelBuscado,nivelActual+1,ListaNivel);

        return ListaNivel;


    }



    // Comprueba si el árbol es homogéneo.
    // Un árbol binario homogéneo solo puede tener nodos con 0 o 2 hijos.
    public boolean  isArbolHomogeneo(){
        return isArbolHomogeneo(raiz);
    }


    // Método auxiliar recursivo que comprueba si el subárbol actual es homogéneo.


    private boolean isArbolHomogeneo(Nodo<T> actual){

        // Si el nodo es null, devuelve true.
        if ((actual==null)){
            return true;
        }
        // Si tiene dos hijos, comprueba recursivamente ambos subárboles.

        else if (actual.getDerecha()!=null &&actual.getIzquierda()!=null){
            return isArbolHomogeneo(actual.getIzquierda()) && isArbolHomogeneo(actual.getDerecha());
        }
        // Si no tiene hijos, devuelve true.
        else if(actual.getDerecha()==null &&actual.getIzquierda()==null)return true;

            // Si tiene solo un hijo, devuelve false.
        else return false;



    }



    // Comprueba si el árbol es completo.
    // Un árbol es completo si todas sus hojas están a la misma profundidad.
    public boolean isArbolCompleto(){
        return isArbolCompleto(raiz, 1 ,getAlturaRaiz() );
    }



    // Método auxiliar recursivo que comprueba si todas las hojas están en el mismo nivel.

    private boolean isArbolCompleto(Nodo<T> actual, int NivelActual, int NivelArbol ){

        // Si el nodo es hoja, compara su nivel con la altura del árbol.
        if (actual==null) return true;
        if (actual.getIzquierda() == null && actual.getDerecha() == null) {
            if (NivelArbol == NivelActual) return true;
            else return false;
        }
        // Si el nodo tiene solo un hijo, el árbol no es completo
        else if ((actual.getIzquierda() == null && actual.getDerecha() != null) ||
                (actual.getIzquierda() != null && actual.getDerecha() == null)) {
            return false;
        }
        // Si tiene dos hijos, sigue comprobando recursivamente ambos subárboles.
        else {
            return isArbolCompleto(actual.getDerecha(), NivelActual + 1, NivelArbol)
                    && isArbolCompleto(actual.getIzquierda(), NivelActual + 1, NivelArbol);
        }


    }


    // Comprueba si el árbol es casi completo.
    // Un árbol casi completo puede tener el último nivel incompleto,
    // pero los nodos deben estar colocados de izquierda a derecha sin huecos intermedios.
    public boolean isArbolCasiCompleto() {
        if (raiz == null) {
            return true;
        }

        Cola<Nodo<T>> cola = new Cola<>();
        cola.offer(raiz);

        boolean huecoEncontrado = false;


        // Recorre el árbol por niveles usando una cola.
        // Si aparece un hueco, ya no puede aparecer después ningún hijo.
        while (!cola.isEmpty()) {
            Nodo<T> actual = cola.poll();
            // Comprueba el hijo izquierdo
            if (actual.getIzquierda() != null) {
                if (huecoEncontrado) {
                    return false;
                }
                cola.offer(actual.getIzquierda());
            } else {
                huecoEncontrado = true;
            }
            // Comprueba el hijo derecho
            if (actual.getDerecha() != null) {
                if (huecoEncontrado) {
                    return false;
                }
                cola.offer(actual.getDerecha());
            } else {
                huecoEncontrado = true;
            }
        }

        return true;
    }

}
