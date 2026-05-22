package modelo.juego;

/**
 * Estado general de una partida.
 *
 * Parte B lo usara para controlar si la logica debe seguir aceptando acciones.
 * Parte C lo usara para mostrar al jugador si la partida sigue en curso, se ha
 * ganado o se ha perdido.
 */
public enum EstadoPartida {
    EN_CURSO,
    VICTORIA,
    DERROTA
}
