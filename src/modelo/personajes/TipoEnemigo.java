package modelo.personajes;

/**
 * Tipos de enemigo acordados para la Parte B.
 *
 * Se usa un enum para evitar textos sueltos como "orco" o "boss" repartidos
 * por el codigo. Asi, las partes de combate, JSON o interfaz podran comparar
 * tipos de enemigo de forma clara cuando les toque usar esta informacion.
 */
public enum TipoEnemigo {
    ESQUELETO,
    ORCO,
    MAGO,
    BOSS
}
