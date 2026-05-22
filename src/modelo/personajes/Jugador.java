package modelo.personajes;

import Estructuras.ListaDE;
import Estructuras.MiIterador;
import modelo.objetos.Arma;
import modelo.objetos.Arco;
import modelo.objetos.Escudo;
import modelo.objetos.Objeto;
import modelo.objetos.Pocion;

/**
 * Personaje controlado por el usuario.
 *
 * En B-02 incorpora inventario con ListaDE y equipo separado para arma y
 * escudo. La accion de usar un objeto dentro de un turno se resolvera en B-03;
 * aqui solo se mantiene el estado del jugador y los efectos directos de objetos
 * simples como la pocion de cura.
 */
public class Jugador extends Personaje {
    private final ListaDE<Objeto> inventario;
    private Arma armaEquipada;
    private Escudo escudoEquipado;

    public Jugador(String nombre, int vidaMaxima, int ataqueBase, int defensaBase,
                   int movimiento, int fila, int columna) {
        super(nombre, vidaMaxima, ataqueBase, defensaBase, movimiento, fila, columna);
        this.inventario = new ListaDE<>();
        this.armaEquipada = null;
        this.escudoEquipado = null;
    }

    public void agregarObjeto(Objeto objeto) {
        validarObjeto(objeto);
        if (tieneObjetoConId(objeto.getId())) {
            throw new IllegalArgumentException("Ya existe un objeto con id " + objeto.getId());
        }
        inventario.addLast(objeto);
    }

    public boolean tieneObjeto(Objeto objeto) {
        validarObjeto(objeto);
        return inventario.existeDato(objeto);
    }

    public Objeto quitarObjeto(Objeto objeto) {
        validarObjeto(objeto);
        Objeto eliminado = inventario.del(objeto);
        if (eliminado != null) {
            if (eliminado.equals(armaEquipada)) {
                armaEquipada = null;
            }
            if (eliminado.equals(escudoEquipado)) {
                escudoEquipado = null;
            }
        }
        return eliminado;
    }

    /**
     * Equipa un arma que ya esta en el inventario.
     *
     * El arco ocupa las dos manos. Por eso, si se equipa un arco, el escudo se
     * desequipa automaticamente. Esta regla queda aqui porque forma parte del
     * estado de equipo del jugador, no de la formula de combate.
     */
    public boolean equiparArma(Arma arma) {
        validarObjeto(arma);
        Objeto objetoEnInventario = inventario.get(arma);
        if (!(objetoEnInventario instanceof Arma)) {
            return false;
        }
        Arma armaEnInventario = (Arma) objetoEnInventario;
        armaEquipada = armaEnInventario;
        if (armaEquipada instanceof Arco) {
            quitarEscudoEquipado();
        }
        return true;
    }

    public boolean equiparEscudo(Escudo escudo) {
        validarObjeto(escudo);
        if (armaEquipada instanceof Arco) {
            return false;
        }
        Objeto objetoEnInventario = inventario.get(escudo);
        if (!(objetoEnInventario instanceof Escudo)) {
            return false;
        }
        Escudo escudoEnInventario = (Escudo) objetoEnInventario;
        escudoEquipado = escudoEnInventario;
        return true;
    }

    public boolean desequiparArma() {
        if (armaEquipada == null) {
            return false;
        }
        armaEquipada = null;
        return true;
    }

    public boolean desequiparEscudo() {
        if (escudoEquipado == null) {
            return false;
        }
        quitarEscudoEquipado();
        return true;
    }

    public boolean usarPocion(Pocion pocion) {
        validarObjeto(pocion);
        Objeto objetoEnInventario = inventario.get(pocion);
        if (!(objetoEnInventario instanceof Pocion)) {
            return false;
        }
        Pocion pocionEnInventario = (Pocion) objetoEnInventario;
        boolean seConsume = pocionEnInventario.usarSobre(this);
        if (seConsume) {
            inventario.del(pocionEnInventario);
        }
        return true;
    }

    public int getCantidadObjetosInventario() {
        return inventario.getSize();
    }

    public ListaDE<Objeto> getInventario() {
        return inventario.copy();
    }

    public Arma getArmaEquipada() {
        return armaEquipada;
    }

    public Escudo getEscudoEquipado() {
        return escudoEquipado;
    }

    public int getAtaqueTotal() {
        int ataqueTotal = getAtaqueBase();
        if (armaEquipada != null) {
            ataqueTotal += armaEquipada.getBonificacionAtaque();
        }
        return ataqueTotal;
    }

    public int getDefensaTotal() {
        int defensaTotal = getDefensaBase();
        if (escudoEquipado != null) {
            defensaTotal += escudoEquipado.getBonificacionDefensa();
        }
        return defensaTotal;
    }

    public boolean tieneObjetoConId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El id es obligatorio");
        }
        MiIterador<Objeto> iterador = inventario.getIterador();
        while (iterador.hasNext()) {
            Objeto objeto = iterador.next();
            if (objeto.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private void validarObjeto(Objeto objeto) {
        if (objeto == null) {
            throw new IllegalArgumentException("El objeto es obligatorio");
        }
    }

    private void quitarEscudoEquipado() {
        escudoEquipado = null;
    }
}
