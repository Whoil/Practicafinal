package modelo.juego;

import modelo.mapa.Cueva;

/**
 * Puerta jugable entre dos cuevas.
 *
 * Mazmorra sabe que existe una conexion dirigida entre cuevas. Puerta anade la
 * regla de juego: que llave hace falta para atravesar esa conexion.
 *
 * Esta clase no crea conexiones. Si la conexion no existe en Mazmorra, Partida
 * debe rechazar la puerta durante la configuracion inicial.
 */
public class Puerta {
    private final String id;
    private final Cueva origen;
    private final Cueva destino;
    private final String codigoLlave;
    private boolean abierta;

    public Puerta(String id, Cueva origen, Cueva destino, String codigoLlave) {
        validarTexto(id, "id");
        validarTexto(codigoLlave, "codigoLlave");
        if (origen == null) {
            throw new IllegalArgumentException("La cueva origen es obligatoria");
        }
        if (destino == null) {
            throw new IllegalArgumentException("La cueva destino es obligatoria");
        }
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.codigoLlave = codigoLlave;
        this.abierta = false;
    }

    public String getId() {
        return id;
    }

    public Cueva getOrigen() {
        return origen;
    }

    public Cueva getDestino() {
        return destino;
    }

    public String getCodigoLlave() {
        return codigoLlave;
    }

    public boolean isAbierta() {
        return abierta;
    }

    public void abrir() {
        abierta = true;
    }

    public boolean conecta(Cueva posibleOrigen, Cueva posibleDestino) {
        if (posibleOrigen == null || posibleDestino == null) {
            return false;
        }
        return origen.equals(posibleOrigen) && destino.equals(posibleDestino);
    }

    @Override
    public boolean equals(Object otro) {
        if (!(otro instanceof Puerta)) {
            return false;
        }
        Puerta otraPuerta = (Puerta) otro;
        return id.equals(otraPuerta.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    private static void validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
    }
}
