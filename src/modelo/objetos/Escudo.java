package modelo.objetos;

/**
 * Objeto equipable que aumenta la defensa del jugador.
 */
public class Escudo extends Objeto {
    public static final int DEFENSA_EXTRA = 5;

    private final int bonificacionDefensa;

    public Escudo(String id) {
        super(id, "Escudo", "Aumenta la defensa del jugador");
        this.bonificacionDefensa = DEFENSA_EXTRA;
    }

    public int getBonificacionDefensa() {
        return bonificacionDefensa;
    }

    /**
     * El escudo se equipa en su propia ranura salvo que el jugador use arco.
     */
    @Override
    public boolean esEquipable() {
        return true;
    }
}
