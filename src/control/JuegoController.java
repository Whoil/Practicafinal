package control;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import Estructuras.ListaSE;
import modelo.juego.ConstantesJuego;
import modelo.juego.DisparoEnemigo;
import modelo.juego.Partida;
import modelo.personajes.Enemigo;
import modelo.personajes.Jugador;
import vista.PantallaJuego;
import vista.ReproductorSfx;

public class JuegoController {

    private final PantallaJuego vista;
    private final Partida partida;

    private ConstantesJuego.HechizoPendiente hechizoPendiente = ConstantesJuego.HechizoPendiente.NINGUNO;
    private Timeline hechizoPendienteTimer;

    public JuegoController(PantallaJuego vista, Partida partida) {
        this.vista = vista;
        this.partida = partida;
    }

    public void handleKeyPressed(KeyEvent e) {
        try {
            KeyCode k = e.getCode();
            if (k == KeyCode.P || k == KeyCode.ESCAPE) {
                vista.togglePausa();
                e.consume();
                return;
            }
            if (vista.isPausaVisible()) {
                e.consume();
                return;
            }
            boolean ok = true;
            boolean movio = false;
            String msg = null;
            ListaSE<DisparoEnemigo> disparosEnemigosTurno = null;
            Jugador jug = partida.getJugador();
            int pf = jug.getFila(), pc = jug.getColumna();
            int filas = partida.getCuevaActual().getFilas();
            int cols = partida.getCuevaActual().getColumnas();
            if (k == KeyCode.F) {
                activarModoHechizo(ConstantesJuego.HechizoPendiente.FUEGO);
                e.consume();
                return;
            }
            if (k == KeyCode.C) {
                activarModoHechizo(ConstantesJuego.HechizoPendiente.HIELO);
                e.consume();
                return;
            }
            if (esFlechaDireccion(k) && hechizoPendiente != ConstantesJuego.HechizoPendiente.NINGUNO) {
                ConstantesJuego.HechizoPendiente hechizo = hechizoPendiente;
                if (dispararHechizo(hechizo, deltaFila(k), deltaColumna(k))) {
                    msg = null;
                } else {
                    ok = false;
                    msg = "No puedes lanzar el hechizo ahora";
                }
                desactivarModoHechizo();
            } else if (e.isShiftDown() && esTeclaDireccion(k)) {
                int df = deltaFila(k);
                int dc = deltaColumna(k);
                if (partida.isAccionRealizada()) {
                    ok = false;
                    msg = "Ya has usado la accion";
                } else if (!partida.hayEnemigoEnDireccion(df, dc)) {
                    ok = false;
                    msg = "No hay enemigo en esa direccion";
                } else {
                    ok = vista.atacarCelda(pf + df, pc + dc);
                    if (!ok) {
                        msg = "No puedes atacar ahora";
                    }
                }
            } else if ((k == KeyCode.W || k == KeyCode.UP) && pf > 0) {
                if (partida.hayEnemigoEn(pf - 1, pc)) { ok = false; msg = "Hay un enemigo ahi"; }
                else if (vista.esTesoro(partida.getCuevaActual().getCelda(pf - 1, pc))) { ok = false; msg = null; }
                else if (vista.esObstaculo(partida.getCuevaActual().getCelda(pf - 1, pc))) { ok = false; msg = "Hay una pared"; }
                else { ok = partida.moverJugadorArriba(); movio = ok; if (!ok) msg = "No puedes moverte mas este turno"; }
            } else if ((k == KeyCode.S || k == KeyCode.DOWN) && pf < filas - 1) {
                if (partida.hayEnemigoEn(pf + 1, pc)) { ok = false; msg = "Hay un enemigo ahi"; }
                else if (vista.esTesoro(partida.getCuevaActual().getCelda(pf + 1, pc))) { ok = false; msg = null; }
                else if (vista.esObstaculo(partida.getCuevaActual().getCelda(pf + 1, pc))) { ok = false; msg = "Hay una pared"; }
                else { ok = partida.moverJugadorAbajo(); movio = ok; if (!ok) msg = "No puedes moverte mas este turno"; }
            } else if ((k == KeyCode.A || k == KeyCode.LEFT) && pc > 0) {
                if (partida.hayEnemigoEn(pf, pc - 1)) { ok = false; msg = "Hay un enemigo ahi"; }
                else if (vista.esTesoro(partida.getCuevaActual().getCelda(pf, pc - 1))) { ok = false; msg = null; }
                else if (vista.esObstaculo(partida.getCuevaActual().getCelda(pf, pc - 1))) { ok = false; msg = "Hay una pared"; }
                else { ok = partida.moverJugadorIzquierda(); movio = ok; if (!ok) msg = "No puedes moverte mas este turno"; }
            } else if ((k == KeyCode.D || k == KeyCode.RIGHT) && pc < cols - 1) {
                if (partida.hayEnemigoEn(pf, pc + 1)) { ok = false; msg = "Hay un enemigo ahi"; }
                else if (vista.esTesoro(partida.getCuevaActual().getCelda(pf, pc + 1))) { ok = false; msg = null; }
                else if (vista.esObstaculo(partida.getCuevaActual().getCelda(pf, pc + 1))) { ok = false; msg = "Hay una pared"; }
                else { ok = partida.moverJugadorDerecha(); movio = ok; if (!ok) msg = "No puedes moverte mas este turno"; }
            } else if (k == KeyCode.SPACE) {
                if (partida.isAccionRealizada()) {
                    ok = false;
                    msg = null;
                } else {
                    Enemigo target = partida.getEnemigoAdyacente();
                    if (target != null) {
                        vista.setAtaqueFila(target.getFila());
                        vista.setAtaqueCol(target.getColumna());
                        vista.setTargetAntesAtaque(target);
                        ok = partida.atacar();
                        if (ok) {
                            ReproductorSfx.getInstancia().reproducirAtaque();
                            vista.iniciarEfectoAtaque();
                        } else {
                            vista.setAtaqueFila(-1);
                            vista.setAtaqueCol(-1);
                            msg = "No puedes atacar ahora";
                        }
                    } else {
                        ok = false;
                        msg = "No hay enemigo para atacar";
                    }
                }
            }
            else if (k == KeyCode.R) {
                if (partida.isAccionRealizada()) {
                    ok = false;
                    msg = null;
                } else {
                    ok = partida.recogerObjeto();
                    if (!ok && partida.hayTesoroCercano()) {
                        ok = partida.abrirTesoro();
                    }
                    if (ok) ReproductorSfx.getInstancia().reproducirRecoger();
                    if (!ok) msg = "No hay objeto que recoger aqui";
                }
            }
            else if (k == KeyCode.H) { vista.toggleAyuda(); return; }
            else if (k == KeyCode.T) {
                int hpAntes = partida.getJugador().getVidaActual();
                ok = partida.terminarTurno();
                if (ok) {
                    disparosEnemigosTurno = partida.consumirDisparosEnemigosPendientes();
                    if (partida.getJugador().getVidaActual() < hpAntes) {
                        Jugador j2 = partida.getJugador();
                        ReproductorSfx.getInstancia().reproducirDano();
                        vista.iniciarEfectoRecibirAtaque(j2.getFila(), j2.getColumna());
                    }
                    msg = null;
                } else {
                    msg = "No puedes terminar el turno ahora";
                }
            }
            vista.actualizar();
            if (disparosEnemigosTurno != null) {
                vista.animarDisparosEnemigos(disparosEnemigosTurno);
            }

            if (movio && partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO
                    && partida.puedeCambiarCueva()) {
                if (partida.cambiarCueva()) {
                    ReproductorSfx.getInstancia().reproducirPuerta();
                    if (vista.getAlCambiarCueva() != null) {
                        vista.getAlCambiarCueva().run();
                        return;
                    }
                    vista.actualizar();
                }
            }

            if (movio && partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO) {
                vista.animarMovimiento(pf, pc);
            }

            if (vista.getAtaqueFila() >= 0 && partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO) {
                vista.animarProyectil(pf, pc, vista.getAtaqueFila(), vista.getAtaqueCol());
                vista.animarAtaque(vista.getAtaqueFila(), vista.getAtaqueCol());
                if (vista.getTargetAntesAtaque() != null && !vista.getTargetAntesAtaque().estaVivo()) {
                    vista.animarMuerteEnemigo(vista.getAtaqueFila(), vista.getAtaqueCol(),
                            vista.getEnemyAssetPath(vista.getTargetAntesAtaque()));
                }
                vista.setTargetAntesAtaque(null);
            }

            if (msg != null) {
                vista.mostrarFeedback(msg, ok ? Color.LIGHTGREEN : Color.rgb(255, 120, 100));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            vista.mostrarFeedback("Error: " + ex.getMessage(), Color.RED);
        }
    }

    public void handleKeyReleased(KeyEvent e) {
        if (e.getCode() == KeyCode.F || e.getCode() == KeyCode.C) {
            desactivarModoHechizo();
        }
    }

    public void handleCellClick(int fila, int columna, boolean pausaVisible) {
        if (pausaVisible) {
            return;
        }
        boolean ok;
        boolean movimientoPorClick = false;
        String clickMsg = null;
        int clickOldF = partida.getJugador().getFila();
        int clickOldC = partida.getJugador().getColumna();
        if (partida.hayEnemigoEn(fila, columna)) {
            if (partida.isAccionRealizada()) {
                ok = false;
                clickMsg = "Ya has usado la accion";
            } else {
                ok = vista.atacarCelda(fila, columna);
                if (!ok) {
                    clickMsg = "No puedes atacar ahora";
                }
            }
        } else if (vista.esTesoro(partida.getCuevaActual().getCelda(fila, columna))) {
            ok = false;
        } else if (vista.esObstaculo(partida.getCuevaActual().getCelda(fila, columna))) {
            ok = false;
            clickMsg = "Hay una pared";
        } else {
            ok = partida.moverJugador(fila, columna);
            movimientoPorClick = ok;
            if (!ok) clickMsg = "No puedes moverte alli";
        }
        vista.actualizar();
        if (movimientoPorClick && partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO
                && partida.puedeCambiarCueva()) {
            if (partida.cambiarCueva()) {
                ReproductorSfx.getInstancia().reproducirPuerta();
                if (vista.getAlCambiarCueva() != null) {
                    vista.getAlCambiarCueva().run();
                    return;
                }
                vista.actualizar();
            }
        }
        if (movimientoPorClick && partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO) {
            vista.animarMovimiento(clickOldF, clickOldC);
        }
        if (vista.getAtaqueFila() >= 0 && partida.getEstado() == modelo.juego.EstadoPartida.EN_CURSO) {
            vista.animarProyectil(clickOldF, clickOldC, vista.getAtaqueFila(), vista.getAtaqueCol());
            vista.animarAtaque(vista.getAtaqueFila(), vista.getAtaqueCol());
            if (vista.getTargetAntesAtaque() != null && !vista.getTargetAntesAtaque().estaVivo()) {
                vista.animarMuerteEnemigo(vista.getAtaqueFila(), vista.getAtaqueCol(),
                        vista.getEnemyAssetPath(vista.getTargetAntesAtaque()));
            }
            vista.setTargetAntesAtaque(null);
        }
        if (clickMsg != null) {
            vista.mostrarFeedback(clickMsg, Color.rgb(255, 120, 100));
        }
    }

    // --- Hechizo ---

    private void activarModoHechizo(ConstantesJuego.HechizoPendiente hechizo) {
        hechizoPendiente = hechizo;
        if (hechizoPendienteTimer != null) {
            hechizoPendienteTimer.stop();
        }
        hechizoPendienteTimer = new Timeline(new KeyFrame(Duration.millis(450), e -> {
            hechizoPendiente = ConstantesJuego.HechizoPendiente.NINGUNO;
        }));
        hechizoPendienteTimer.setCycleCount(1);
        hechizoPendienteTimer.play();
    }

    private void desactivarModoHechizo() {
        hechizoPendiente = ConstantesJuego.HechizoPendiente.NINGUNO;
        if (hechizoPendienteTimer != null) {
            hechizoPendienteTimer.stop();
            hechizoPendienteTimer = null;
        }
    }

    private boolean dispararHechizo(ConstantesJuego.HechizoPendiente hechizo, int df, int dc) {
        if (hechizo == ConstantesJuego.HechizoPendiente.FUEGO) {
            return vista.dispararBolaFuego(df, dc);
        }
        if (hechizo == ConstantesJuego.HechizoPendiente.HIELO) {
            return vista.dispararBolaHielo(df, dc);
        }
        return false;
    }

    // --- Helpers de teclado ---

    private static boolean esTeclaDireccion(KeyCode k) {
        return k == KeyCode.W || k == KeyCode.UP
                || k == KeyCode.S || k == KeyCode.DOWN
                || k == KeyCode.A || k == KeyCode.LEFT
                || k == KeyCode.D || k == KeyCode.RIGHT;
    }

    private static int deltaFila(KeyCode k) {
        if (k == KeyCode.W || k == KeyCode.UP) return -1;
        if (k == KeyCode.S || k == KeyCode.DOWN) return 1;
        return 0;
    }

    private static int deltaColumna(KeyCode k) {
        if (k == KeyCode.A || k == KeyCode.LEFT) return -1;
        if (k == KeyCode.D || k == KeyCode.RIGHT) return 1;
        return 0;
    }

    private static boolean esFlechaDireccion(KeyCode k) {
        return k == KeyCode.UP || k == KeyCode.DOWN || k == KeyCode.LEFT || k == KeyCode.RIGHT;
    }
}
