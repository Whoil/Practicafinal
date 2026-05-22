package modelo.objetos;

/**
 * Objeto equipable que aumenta el ataque del jugador.
 *
 * En B-02 no se implementa la accion de atacar. Esta clase solo aporta el
 * modificador que B-03 usara cuando calcule el daño real del combate.
 */
public abstract class Arma extends Objeto {
    private final int bonificacionAtaque;

    protected Arma(String id, String nombre, String descripcion, int bonificacionAtaque) {
        super(id, nombre, descripcion);
        validarNoNegativo(bonificacionAtaque, "bonificacionAtaque");
        this.bonificacionAtaque = bonificacionAtaque;
    }

    public int getBonificacionAtaque() {
        return bonificacionAtaque;
    }

    /**
     * Toda arma se puede equipar en la ranura de arma del jugador.
     */
    @Override
    public boolean esEquipable() {
        return true;
    }

    private static void validarNoNegativo(int valor, String campo) {
        if (valor < 0) {
            throw new IllegalArgumentException("El campo " + campo + " no puede ser negativo");
        }
    }
}
