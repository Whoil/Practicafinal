package control;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import json.ResultadoPartidaDTO;
import json.SerializadorRanking;
import vista.PantallaIntroduccion;
import vista.PantallaTransicion;
import vista.PantallaJuego;
import vista.PantallaFinal;
import vista.DatosTemaCueva;

public class ControladorFlujo {

    private final Stage stage;
    private modelo.juego.Partida partida;
    private String nombreJugadorActual = "Mago Errante";
    private final Runnable volverAlMenu;

    public ControladorFlujo(Stage stage, Runnable volverAlMenu) {
        this.stage = stage;
        this.volverAlMenu = volverAlMenu;
    }

    public void setPartida(modelo.juego.Partida partida) {
        this.partida = partida;
    }

    public void setNombreJugador(String nombre) {
        this.nombreJugadorActual = nombre;
    }

    /**
     * Muestra la pantalla de introduccion con la historia inicial.
     */
    public void mostrarIntroduccion() {
        PantallaIntroduccion intro = new PantallaIntroduccion(() -> {
            mostrarTransicion("cueva_facil");
        });
        Scene scene = intro.crearScene();
        scene.setOnMouseClicked(e -> scene.getRoot().requestFocus());
        stage.setScene(scene);
        fijarVentanaEstable(false);
    }

    /**
     * Muestra la pantalla de transicion correspondiente a una cueva.
     */
    public void mostrarTransicion(String cuevaId) {
        DatosTemaCueva tema = DatosTemaCueva.paraCuevaId(cuevaId);
        PantallaTransicion transicion = new PantallaTransicion(tema, () -> {
            mostrarJuego();
        });
        Scene scene = transicion.crearScene();
        scene.setOnMouseClicked(e -> scene.getRoot().requestFocus());
        stage.setScene(scene);
        fijarVentanaEstable(false);
    }

    /**
     * Muestra la pantalla de juego (PantallaJuego) vinculada a la
     * partida actual. Configura los callbacks para cambio de cueva
     * y fin de partida.
     */
    private void logDirecto(String msg) {
        try {
            java.io.FileWriter fw = new java.io.FileWriter("direct_debug.log", true);
            fw.write(java.time.LocalDateTime.now() + " " + msg + "\n");
            fw.close();
        } catch (Exception ign) {}
    }

    public void mostrarJuego() {
        logDirecto("mostrarJuego() INICIO");
        try {
            PantallaJuego pj = new PantallaJuego(partida, stage, volverAlMenu);
            logDirecto("PantallaJuego creado");
            pj.setAlCambiarCueva(() -> {
                logDirecto("Callback cambiar cueva: " + partida.getCuevaActual().getId());
                mostrarTransicion(partida.getCuevaActual().getId());
            });
            pj.setAlTerminarPartida(() -> {
                boolean victoria = partida.getEstado() == modelo.juego.EstadoPartida.VICTORIA;
                logDirecto("Callback fin partida: " + (victoria ? "VICTORIA" : "DERROTA"));
                mostrarFinal(victoria);
            });
            Scene gameScene = pj.crearScene();
            logDirecto("crearScene() completado");
            stage.setScene(gameScene);
            // La vista de juego usa una escena fija 1280x720. Ajustamos el
            // tamano de la ventana a la escena, no al borde exterior.
            fijarVentanaEstable(true);
            logDirecto("stage.setScene() completado");
        } catch (Throwable t) {
            logDirecto("EXCEPCION EN mostrarJuego: " + t.getClass().getName() + " - " + t.getMessage());
            for (StackTraceElement ste : t.getStackTrace()) {
                logDirecto("  at " + ste.toString());
            }
        }
    }

    /**
     * Muestra la pantalla de victoria o derrota segun el resultado.
     */
    public void mostrarFinal(boolean victoria) {
        ResultadoPartidaDTO resultado = new ResultadoPartidaDTO(
                nombreJugadorActual,
                victoria,
                partida != null ? partida.getEstadisticas() : null);
        try {
            SerializadorRanking.guardarResultado(resultado);
        } catch (IOException ex) {
            logDirecto("No se pudo guardar ranking: " + ex.getMessage());
        }
        PantallaFinal pantallaFinal = new PantallaFinal(victoria, resultado, volverAlMenu);
        Scene scene = pantallaFinal.crearScene();
        scene.setOnMouseClicked(e -> scene.getRoot().requestFocus());
        stage.setScene(scene);
        fijarVentanaEstable(false);
    }

    /**
     * Devuelve la ventana a un modo fijo y conocido tras cambiar de escena.
     * Evita que un estado previo maximizado o de pantalla completa deforme
     * pantallas disenadas para 1280x720.
     */
    private void fijarVentanaEstable(boolean centrar) {
        stage.setFullScreen(false);
        stage.setMaximized(false);
        stage.setResizable(false);
        stage.sizeToScene();
        if (centrar) {
            stage.centerOnScreen();
        }
    }
}
