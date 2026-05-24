package modelo.juego;

import Estructuras.ListaSE;
import json.ConexionDTO;
import json.ConfiguracionEnemigoDTO;
import json.ConfiguracionObjetoDTO;
import json.ResultadoCarga;
import modelo.mapa.Celda;
import modelo.mapa.Cueva;
import modelo.mapa.TipoCelda;
import modelo.objetos.Arco;
import modelo.objetos.Escudo;
import modelo.objetos.Espada;
import modelo.objetos.Llave;
import modelo.objetos.Objeto;
import modelo.objetos.Pocion;
import modelo.objetos.TipoLlave;
import modelo.personajes.Boss;
import modelo.personajes.Enemigo;
import modelo.personajes.Jugador;
import modelo.personajes.TipoEnemigo;

/**
 * Construye una Partida jugable a partir del resultado del cargador JSON.
 *
 * Esta clase es el puente minimo que necesita Parte C: JSON sigue leyendo datos
 * y construyendo la Mazmorra, mientras Parte B decide como transformar esos
 * datos en Jugador, enemigos, objetos y puertas reales del modelo.
 */
public class FabricaPartida {
    public static final int VIDA_JUGADOR = 100;
    public static final int ATAQUE_JUGADOR = 15;
    public static final int DEFENSA_JUGADOR = 5;
    public static final int MOVIMIENTO_JUGADOR = 3;
    public static final int TURNOS_INICIALES = 60;

    public Partida crearPartida(ResultadoCarga resultadoCarga) {
        return crearPartida(resultadoCarga, "Jugador");
    }

    public Partida crearPartida(ResultadoCarga resultadoCarga, String nombreJugador) {
        validarResultado(resultadoCarga);

        Mazmorra mazmorra = resultadoCarga.getMazmorra();
        Cueva cuevaInicial = mazmorra.getCuevaActual();
        Celda inicio = buscarCeldaInicio(cuevaInicial);
        Jugador jugador = new Jugador(textoOValor(nombreJugador, "Jugador"), VIDA_JUGADOR, ATAQUE_JUGADOR,
                DEFENSA_JUGADOR, MOVIMIENTO_JUGADOR, inicio.getFila(), inicio.getColumna());
        ListaSE<Puerta> puertas = crearPuertas(mazmorra, resultadoCarga.getConexiones());

        Partida partida = new Partida(mazmorra, jugador, TURNOS_INICIALES, puertas);
        cargarEnemigos(partida, mazmorra, resultadoCarga.getEnemigos());
        cargarObjetos(partida, mazmorra, resultadoCarga.getObjetos());
        return partida;
    }

    private void validarResultado(ResultadoCarga resultadoCarga) {
        if (resultadoCarga == null) {
            throw new IllegalArgumentException("El resultado de carga es obligatorio");
        }
        if (resultadoCarga.getMazmorra() == null) {
            throw new IllegalArgumentException("La mazmorra cargada es obligatoria");
        }
        if (resultadoCarga.getMazmorra().getCuevaActual() == null) {
            throw new IllegalArgumentException("La mazmorra debe tener una cueva inicial");
        }
    }

    private Celda buscarCeldaInicio(Cueva cueva) {
        for (int fila = 0; fila < cueva.getFilas(); fila++) {
            for (int columna = 0; columna < cueva.getColumnas(); columna++) {
                Celda celda = cueva.getCelda(fila, columna);
                if (celda.getTipo() == TipoCelda.INICIO) {
                    return celda;
                }
            }
        }
        throw new IllegalArgumentException("La cueva inicial debe tener una celda INICIO");
    }

    private ListaSE<Puerta> crearPuertas(Mazmorra mazmorra, ListaSE<ConexionDTO> conexiones) {
        ListaSE<Puerta> puertas = new ListaSE<>();
        if (conexiones == null) {
            return puertas;
        }
        for (int indice = 0; indice < conexiones.getSize(); indice++) {
            ConexionDTO conexion = conexiones.get(indice);
            validarConexion(conexion);
            Cueva origen = mazmorra.getCuevaPorId(conexion.getOrigen());
            Cueva destino = mazmorra.getCuevaPorId(conexion.getDestino());
            if (origen == null || destino == null || !mazmorra.existeConexion(origen, destino)) {
                throw new IllegalArgumentException("Conexion de puerta no valida: " + conexion.getOrigen()
                        + " -> " + conexion.getDestino());
            }
            String idPuerta = textoOValor(conexion.getEtiqueta(), "puerta-" + origen.getId() + "-" + destino.getId());
            puertas.addLast(new Puerta(idPuerta, origen, destino, codigoLlaveParaDestino(destino.getId())));
        }
        return puertas;
    }

    private void cargarEnemigos(Partida partida, Mazmorra mazmorra, ListaSE<ConfiguracionEnemigoDTO> enemigos) {
        if (enemigos == null) {
            return;
        }
        for (int indice = 0; indice < enemigos.getSize(); indice++) {
            ConfiguracionEnemigoDTO dto = enemigos.get(indice);
            Cueva cueva = obtenerCueva(mazmorra, dto.getIdCueva());
            Enemigo enemigo = crearEnemigo(dto);
            if (!partida.anadirEnemigo(cueva, enemigo)) {
                throw new IllegalArgumentException("No se pudo colocar enemigo en "
                        + dto.getIdCueva() + " (" + dto.getFila() + ", " + dto.getColumna() + ")");
            }
        }
    }

    private Enemigo crearEnemigo(ConfiguracionEnemigoDTO dto) {
        validarTexto(dto.getTipo(), "tipoEnemigo");
        TipoEnemigo tipo;
        try {
            tipo = TipoEnemigo.valueOf(dto.getTipo());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de enemigo desconocido: " + dto.getTipo());
        }
        if (tipo == TipoEnemigo.BOSS) {
            return new Boss("Boss", dto.getVida(), dto.getAtaque(), dto.getDefensa(),
                    dto.getMovimiento(), dto.getFila(), dto.getColumna());
        }
        return new Enemigo(tipo.name(), tipo, dto.getVida(), dto.getAtaque(), dto.getDefensa(),
                dto.getMovimiento(), dto.getFila(), dto.getColumna());
    }

    private void cargarObjetos(Partida partida, Mazmorra mazmorra, ListaSE<ConfiguracionObjetoDTO> objetos) {
        if (objetos == null) {
            return;
        }
        for (int indice = 0; indice < objetos.getSize(); indice++) {
            ConfiguracionObjetoDTO dto = objetos.get(indice);
            Cueva cueva = obtenerCueva(mazmorra, dto.getIdCueva());
            Objeto objeto = crearObjeto(dto, indice);
            if (!partida.anadirObjetoEnSuelo(cueva, objeto, dto.getFila(), dto.getColumna())) {
                throw new IllegalArgumentException("No se pudo colocar objeto en "
                        + dto.getIdCueva() + " (" + dto.getFila() + ", " + dto.getColumna() + ")");
            }
        }
    }

    private Objeto crearObjeto(ConfiguracionObjetoDTO dto, int indice) {
        validarTexto(dto.getTipo(), "tipoObjeto");
        String id = textoOValor(dto.getId(), "objeto-" + dto.getIdCueva() + "-" + indice);
        String tipo = dto.getTipo();
        if ("POCION".equals(tipo)) {
            int cura = dto.getCura() > 0 ? dto.getCura() : Pocion.CURACION_BASE;
            return new Pocion(id, cura);
        }
        if ("ESPADA".equals(tipo)) {
            return new Espada(id);
        }
        if ("ARCO".equals(tipo)) {
            return new Arco(id);
        }
        if ("ESCUDO".equals(tipo)) {
            return new Escudo(id);
        }
        if ("LLAVE".equals(tipo)) {
            validarTexto(dto.getCodigoCerradura(), "codigoCerradura");
            TipoLlave tipoLlave = convertirTipoLlave(dto.getTipoLlave());
            return new Llave(id, tipoLlave, dto.getCodigoCerradura());
        }
        throw new IllegalArgumentException("Tipo de objeto desconocido: " + tipo);
    }

    private TipoLlave convertirTipoLlave(String tipoLlave) {
        validarTexto(tipoLlave, "tipoLlave");
        try {
            return TipoLlave.valueOf(tipoLlave);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de llave desconocido: " + tipoLlave);
        }
    }

    private Cueva obtenerCueva(Mazmorra mazmorra, String idCueva) {
        validarTexto(idCueva, "idCueva");
        Cueva cueva = mazmorra.getCuevaPorId(idCueva);
        if (cueva == null) {
            throw new IllegalArgumentException("No existe la cueva " + idCueva);
        }
        return cueva;
    }

    private void validarConexion(ConexionDTO conexion) {
        if (conexion == null) {
            throw new IllegalArgumentException("La conexion es obligatoria");
        }
        validarTexto(conexion.getOrigen(), "origen");
        validarTexto(conexion.getDestino(), "destino");
    }

    private String codigoLlaveParaDestino(String idDestino) {
        return "llave-" + idDestino;
    }

    private String textoOValor(String texto, String valorPorDefecto) {
        if (texto == null || texto.trim().isEmpty()) {
            return valorPorDefecto;
        }
        return texto.trim();
    }

    private void validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
    }
}
