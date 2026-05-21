package modelo.personajes;

/**
 * Contrato base de todos los personajes del juego.
 *
 * El PDF pide definir contratos antes de implementar. Para B-01 este contrato
 * se expresa como clase abstracta, porque Jugador, Enemigo y Boss comparten
 * vida, ataque, defensa, movimiento y posicion. De esta forma se evita duplicar
 * reglas basicas en cada subtipo.
 *
 * No se ofrecen setters generales para nombre, vida maxima, ataque, defensa o
 * movimiento. Esos valores deben cambiar solo mediante reglas concretas del
 * juego, objetos o combate. En B-01 solo se permite modificar la vida mediante
 * recibirDano/curar y la posicion mediante cambiarPosicion.
 */
public abstract class Personaje {
    private final String nombre;
    private final int vidaMaxima;
    private int vidaActual;
    private final int ataqueBase;
    private final int defensaBase;
    private final int movimiento;
    private int fila;
    private int columna;

    protected Personaje(String nombre, int vidaMaxima, int ataqueBase, int defensaBase,
                        int movimiento, int fila, int columna) {
        validarTexto(nombre, "nombre");
        validarPositivo(vidaMaxima, "vidaMaxima");
        validarNoNegativo(ataqueBase, "ataqueBase");
        validarNoNegativo(defensaBase, "defensaBase");
        validarNoNegativo(movimiento, "movimiento");
        validarNoNegativo(fila, "fila");
        validarNoNegativo(columna, "columna");

        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
        this.ataqueBase = ataqueBase;
        this.defensaBase = defensaBase;
        this.movimiento = movimiento;
        this.fila = fila;
        this.columna = columna;
    }

    /**
     * Aplica dano directo al personaje sin permitir que la vida sea negativa.
     *
     * En B-01 no se calcula ataque contra defensa: esa formula pertenece a
     * B-03. Este metodo solo recibe una cantidad de dano ya calculada y protege
     * el invariante del enunciado: la vida nunca puede quedar por debajo de 0.
     */
    public void recibirDano(int cantidad) {
        validarNoNegativo(cantidad, "cantidad");

        vidaActual = vidaActual - cantidad;
        if (vidaActual < 0) {
            vidaActual = 0;
        }
    }

    /**
     * Recupera vida sin superar la vida maxima.
     *
     * Las pociones se implementaran en B-02, pero el contrato de personaje ya
     * necesita una forma controlada de recuperar vida para mantener los limites
     * correctos.
     */
    public void curar(int cantidad) {
        validarNoNegativo(cantidad, "cantidad");

        vidaActual = vidaActual + cantidad;
        if (vidaActual > vidaMaxima) {
            vidaActual = vidaMaxima;
        }
    }

    public boolean estaVivo() {
        return vidaActual > 0;
    }

    /**
     * Cambia la posicion interna del personaje.
     *
     * No se comprueba si la celda existe o si es transitable porque eso
     * pertenece a mapa/Partida. Aqui solo se guarda la posicion comun de
     * personajes con coordenadas no negativas.
     */
    public void cambiarPosicion(int fila, int columna) {
        validarNoNegativo(fila, "fila");
        validarNoNegativo(columna, "columna");

        this.fila = fila;
        this.columna = columna;
    }

    public String getNombre() {
        return nombre;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public int getAtaqueBase() {
        return ataqueBase;
    }

    public int getDefensaBase() {
        return defensaBase;
    }

    public int getMovimiento() {
        return movimiento;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    private static void validarTexto(String texto, String campo) {
        // Evita crear personajes con datos obligatorios vacios o compuestos solo por espacios.
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
    }

    private static void validarPositivo(int valor, String campo) {
        // Se usa para valores que deben existir de verdad, como la vida maxima.
        if (valor <= 0) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser positivo");
        }
    }

    private static void validarNoNegativo(int valor, String campo) {
        // Permite cero cuando tiene sentido, pero bloquea valores imposibles como posicion o dano negativos.
        if (valor < 0) {
            throw new IllegalArgumentException("El campo " + campo + " no puede ser negativo");
        }
    }
}
