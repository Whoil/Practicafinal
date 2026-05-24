package json;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import Estructuras.ListaDE;
import Estructuras.ListaSE;
import Estructuras.MiIterador;
import modelo.juego.Mazmorra;
import modelo.mapa.Celda;
import modelo.mapa.Cueva;
import modelo.mapa.TipoCelda;
import modelo.objetos.Arco;
import modelo.objetos.Arma;
import modelo.objetos.Escudo;
import modelo.objetos.Espada;
import modelo.objetos.Llave;
import modelo.objetos.Objeto;
import modelo.objetos.Pocion;
import modelo.objetos.TipoLlave;
import modelo.personajes.Enemigo;
import modelo.personajes.Jugador;
import modelo.personajes.TipoEnemigo;

/**
 * Serializa y deserializa el estado completo de una partida a/desde JSON.
 *
 * Sigue el mismo patron que CargadorConfiguracion: usa Gson para leer
 * y escribir DTOs (DatosPartidaDTO, DatosMazmorraDTO, etc.) desde/hacia
 * ficheros JSON. La escritura usa pretty printing para que el fichero
 * sea legible y depurable.
 *
 * Ademas de la E/S de ficheros, proporciona metodos de conversion entre
 * los DTOs de guardado y los objetos del modelo (Mazmorra, Jugador,
 * Enemigo, Objeto, etc.) para que Parte B pueda integrar el guardado
 * y la carga en Partida cuando implemente B-03.
 */
public class SerializadorPartida {

    private static final String VERSION_ACTUAL = "1.0";

    // ---------------------------------------------------------------
    // E/S de ficheros
    // ---------------------------------------------------------------

    public static void guardar(DatosPartidaDTO partida, String ruta) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter escritor = new FileWriter(ruta)) {
            gson.toJson(partida, escritor);
        }
    }

    /**
     * Carga el estado de una partida desde un fichero JSON.
     *
     * @param ruta ruta del fichero a leer
     * @return estado de la partida deserializado
     * @throws IOException          si hay error de lectura
     * @throws JsonSyntaxException  si el JSON no es valido
     */
    public static DatosPartidaDTO cargar(String ruta) throws IOException {
        Gson gson = new Gson();
        try (FileReader lector = new FileReader(ruta)) {
            DatosPartidaDTO partida = gson.fromJson(lector, DatosPartidaDTO.class);
            if (partida == null) {
                throw new IllegalArgumentException(
                        "El fichero no contiene datos de partida validos: " + ruta);
            }
            return partida;
        }
    }

    public static String getVersionActual() {
        return VERSION_ACTUAL;
    }

    // ---------------------------------------------------------------
    // Conversion modelo -> DTO (para guardar)
    // ---------------------------------------------------------------

    /**
     * Construye un DatosPartidaDTO a partir de los objetos del modelo.
     * Este es el metodo principal que Partida llamara para guardar.
     */
    public static DatosPartidaDTO desdeMazmorraJugador(
            Mazmorra mazmorra, Jugador jugador, String estado, int turnosRestantes) {
        DatosMazmorraDTO mazmorraDTO = mazmorraADTO(mazmorra);
        DatosJugadorDTO jugadorDTO = jugadorADTO(jugador);
        return new DatosPartidaDTO(VERSION_ACTUAL, mazmorraDTO, jugadorDTO,
                estado, turnosRestantes);
    }

    public static DatosMazmorraDTO mazmorraADTO(Mazmorra mazmorra) {
        String cuevaActualId = mazmorra.getCuevaActual() != null
                ? mazmorra.getCuevaActual().getId() : null;

        ListaSE<Cueva> cuevas = mazmorra.getCuevas();
        DatosCuevaDTO[] cuevasDTO = new DatosCuevaDTO[cuevas.getSize()];
        MiIterador<Cueva> itCuevas = cuevas.getIterador();
        int i = 0;
        while (itCuevas.hasNext()) {
            Cueva cueva = itCuevas.next();
            cuevasDTO[i] = cuevaADTO(cueva);
            i++;
        }

        ConexionDTO[] conexionesDTO = conexionesDesdeMazmorra(mazmorra);

        return new DatosMazmorraDTO(cuevaActualId, cuevasDTO, conexionesDTO);
    }

    public static DatosCuevaDTO cuevaADTO(Cueva cueva) {
        int filas = cueva.getFilas();
        int columnas = cueva.getColumnas();
        String[][] matriz = new String[filas][columnas];

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                Celda celda = cueva.getCelda(f, c);
                matriz[f][c] = celda.getTipo().name();
            }
        }

        return new DatosCuevaDTO(
                cueva.getId(), cueva.getId(), filas, columnas,
                matriz, new DatosEnemigoDTO[0], new DatosObjetoDTO[0]);
    }

    public static DatosJugadorDTO jugadorADTO(Jugador jugador) {
        String armaId = jugador.getArmaEquipada() != null
                ? jugador.getArmaEquipada().getId() : null;
        String escudoId = jugador.getEscudoEquipado() != null
                ? jugador.getEscudoEquipado().getId() : null;

        ListaDE<Objeto> inventario = jugador.getInventario();
        DatosObjetoDTO[] invDTO = new DatosObjetoDTO[inventario.getSize()];
        MiIterador<Objeto> itInv = inventario.getIterador();
        int i = 0;
        while (itInv.hasNext()) {
            Objeto obj = itInv.next();
            invDTO[i] = objetoADTO(obj, -1, -1);
            i++;
        }

        return new DatosJugadorDTO(
                jugador.getNombre(),
                jugador.getVidaActual(),
                jugador.getVidaMaxima(),
                jugador.getAtaqueBase(),
                jugador.getDefensaBase(),
                jugador.getMovimiento(),
                jugador.getFila(),
                jugador.getColumna(),
                armaId, escudoId, invDTO);
    }

    public static DatosEnemigoDTO enemigoADTO(Enemigo enemigo) {
        return new DatosEnemigoDTO(
                enemigo.getTipoEnemigo().name(),
                enemigo.getNombre(),
                enemigo.getVidaActual(),
                enemigo.getVidaMaxima(),
                enemigo.getAtaqueBase(),
                enemigo.getDefensaBase(),
                enemigo.getMovimiento(),
                enemigo.getFila(),
                enemigo.getColumna(),
                enemigo.estaVivo(),
                enemigo.getTurnosCongelado());
    }

    public static DatosObjetoDTO objetoADTO(Objeto objeto, int fila, int columna) {
        int cura = 0;
        int bonifAtaque = 0;
        int bonifDefensa = 0;
        String tipoLlave = null;
        String tipo = objeto.getClass().getSimpleName().toUpperCase();

        if (objeto instanceof Pocion) {
            cura = ((Pocion) objeto).getPuntosCuracion();
        } else if (objeto instanceof Arma) {
            bonifAtaque = ((Arma) objeto).getBonificacionAtaque();
        } else if (objeto instanceof Escudo) {
            bonifDefensa = ((Escudo) objeto).getBonificacionDefensa();
        } else if (objeto instanceof Llave) {
            tipoLlave = ((Llave) objeto).getTipoLlave().name();
        }

        return new DatosObjetoDTO(
                objeto.getId(), tipo, objeto.getNombre(),
                objeto.getDescripcion(), fila, columna,
                cura, bonifAtaque, bonifDefensa, tipoLlave);
    }

    // ---------------------------------------------------------------
    // Conversion DTO -> modelo (para cargar)
    // ---------------------------------------------------------------

    /**
     * Reconstruye una Mazmorra a partir de su DTO de guardado.
     * Crea las cuevas con su matriz de celdas, las conecta segun
     * las conexiones registradas y establece la cueva actual.
     */
    public static Mazmorra dtoAMazmorra(DatosMazmorraDTO dto) {
        Mazmorra mazmorra = new Mazmorra();

        if (dto.getCuevas() != null) {
            for (DatosCuevaDTO cuevaDTO : dto.getCuevas()) {
                Cueva cueva = dtoACueva(cuevaDTO);
                mazmorra.addCueva(cueva);
            }
        }

        if (dto.getConexiones() != null) {
            for (ConexionDTO conn : dto.getConexiones()) {
                Cueva origen = mazmorra.getCuevaPorId(conn.getOrigen());
                Cueva destino = mazmorra.getCuevaPorId(conn.getDestino());
                if (origen != null && destino != null) {
                    mazmorra.conectarCuevas(origen, destino, conn.getEtiqueta());
                }
            }
        }

        if (dto.getCuevaActual() != null) {
            Cueva actual = mazmorra.getCuevaPorId(dto.getCuevaActual());
            if (actual != null) {
                mazmorra.setCuevaActual(actual);
            }
        }

        return mazmorra;
    }

    /**
     * Reconstruye una Cueva a partir de su DTO.
     * Crea la cueva con el constructor estandar y despues asigna
     * el TipoCelda de cada celda segun la matriz de Strings.
     */
    public static Cueva dtoACueva(DatosCuevaDTO dto) {
        Cueva cueva = new Cueva(dto.getId(), dto.getFilas(), dto.getColumnas());
        String[][] matriz = dto.getMatriz();

        for (int f = 0; f < dto.getFilas(); f++) {
            for (int c = 0; c < dto.getColumnas(); c++) {
                String tipoStr = matriz[f][c];
                TipoCelda tipo;
                try {
                    tipo = TipoCelda.valueOf(tipoStr);
                } catch (IllegalArgumentException e) {
                    tipo = TipoCelda.SUELO;
                }
                cueva.cambiarTipoCelda(f, c, tipo);
            }
        }

        return cueva;
    }

    /**
     * Reconstruye un Jugador a partir de su DTO.
     * Crea el jugador, ajusta la vida actual, anade los objetos
     * del inventario y equipa arma/escudo si estaban asignados.
     */
    public static Jugador dtoAJugador(DatosJugadorDTO dto) {
        Jugador jugador = new Jugador(
                dto.getNombre(),
                dto.getVidaMaxima(),
                dto.getAtaqueBase(),
                dto.getDefensaBase(),
                dto.getMovimiento(),
                dto.getFila(),
                dto.getColumna());

        int diferenciaVida = dto.getVidaActual() - dto.getVidaMaxima();
        if (diferenciaVida < 0) {
            jugador.recibirDano(-diferenciaVida);
        } else if (diferenciaVida > 0) {
            jugador.curar(diferenciaVida);
        }

        if (dto.getInventario() != null) {
            for (DatosObjetoDTO objDTO : dto.getInventario()) {
                Objeto obj = dtoAObjeto(objDTO);
                if (obj != null) {
                    jugador.agregarObjeto(obj);
                }
            }
        }

        if (dto.getArmaEquipadaId() != null) {
            for (DatosObjetoDTO objDTO : dto.getInventario()) {
                if (objDTO.getId().equals(dto.getArmaEquipadaId())) {
                    Objeto obj = dtoAObjeto(objDTO);
                    if (obj instanceof Arma) {
                        jugador.equiparArma((Arma) obj);
                    }
                    break;
                }
            }
        }

        if (dto.getEscudoEquipadoId() != null) {
            for (DatosObjetoDTO objDTO : dto.getInventario()) {
                if (objDTO.getId().equals(dto.getEscudoEquipadoId())) {
                    Objeto obj = dtoAObjeto(objDTO);
                    if (obj instanceof Escudo) {
                        jugador.equiparEscudo((Escudo) obj);
                    }
                    break;
                }
            }
        }

        return jugador;
    }

    /**
     * Reconstruye un Enemigo a partir de su DTO.
     */
    public static Enemigo dtoAEnemigo(DatosEnemigoDTO dto) {
        TipoEnemigo tipo;
        try {
            tipo = TipoEnemigo.valueOf(dto.getTipo());
        } catch (IllegalArgumentException e) {
            tipo = TipoEnemigo.ESQUELETO;
        }

        Enemigo enemigo = new Enemigo(
                dto.getNombre(), tipo,
                dto.getVidaMaxima(),
                dto.getAtaqueBase(),
                dto.getDefensaBase(),
                dto.getMovimiento(),
                dto.getFila(),
                dto.getColumna());

        int diferenciaVida = dto.getVidaActual() - dto.getVidaMaxima();
        if (diferenciaVida < 0) {
            enemigo.recibirDano(-diferenciaVida);
        } else if (diferenciaVida > 0) {
            enemigo.curar(diferenciaVida);
        }
        enemigo.congelar(dto.getTurnosCongelado());

        return enemigo;
    }

    /**
     * Fabrica un Objeto concreto a partir del DTO de guardado.
     * Usa el campo tipo (nombre de la subclase en mayusculas)
     * para decidir que subclase instanciar.
     */
    public static Objeto dtoAObjeto(DatosObjetoDTO dto) {
        if (dto == null || dto.getId() == null) {
            return null;
        }

        String tipo = dto.getTipo() != null ? dto.getTipo() : "";

        switch (tipo) {
            case "POCION":
                return new Pocion(dto.getId(), dto.getCura());
            case "ESPADA":
                return new Espada(dto.getId());
            case "ARCO":
                return new Arco(dto.getId());
            case "ESCUDO":
                return new Escudo(dto.getId());
            case "LLAVE": {
                TipoLlave tipoLlave = TipoLlave.PUERTA;
                if (dto.getTipoLlave() != null) {
                    try {
                        tipoLlave = TipoLlave.valueOf(dto.getTipoLlave());
                    } catch (IllegalArgumentException ignored) {
                    }
                }
                return new Llave(dto.getId(), tipoLlave, dto.getId());
            }
            default:
                return null;
        }
    }

    // ---------------------------------------------------------------
    // Metodos auxiliares privados
    // ---------------------------------------------------------------

    /**
     * Extrae las conexiones del grafo de la mazmorra como array de
     * ConexionDTO para serializacion.
     */
    private static ConexionDTO[] conexionesDesdeMazmorra(Mazmorra mazmorra) {
        ListaSE<Cueva> cuevas = mazmorra.getCuevas();
        ListaSE<ConexionDTO> lista = new ListaSE<>();

        MiIterador<Cueva> itCuevas = cuevas.getIterador();
        while (itCuevas.hasNext()) {
            Cueva origen = itCuevas.next();
            ListaSE<Cueva> siguientes = mazmorra.getCuevasSiguientes(origen);
            if (siguientes != null) {
                MiIterador<Cueva> itSiguientes = siguientes.getIterador();
                while (itSiguientes.hasNext()) {
                    Cueva destino = itSiguientes.next();
                    lista.addLast(new ConexionDTO(
                            origen.getId(), destino.getId(), ""));
                }
            }
        }

        ConexionDTO[] arr = new ConexionDTO[lista.getSize()];
        MiIterador<ConexionDTO> itLista = lista.getIterador();
        int i = 0;
        while (itLista.hasNext()) {
            arr[i] = itLista.next();
            i++;
        }
        return arr;
    }
}
