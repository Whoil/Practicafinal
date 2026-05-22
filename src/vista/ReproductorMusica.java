package vista;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * ReproductorMusica - Singleton que gestiona la musica de fondo.
 *
 * Reproduce un unico archivo de audio en bucle infinito.
 * Proporciona metodos para iniciar, detener y ajustar el volumen.
 *
 * @implNote Usa javafx.scene.media.MediaPlayer con ciclo
 *           indefinido (INDEFINITE). Acepta MP3, WAV, AAC.
 */
public final class ReproductorMusica {

    private static ReproductorMusica instancia;

    private MediaPlayer mediaPlayer;

    /** Ruta por defecto relativa al directorio del proyecto. */
    private static final String RUTA_DEFECTO = "datos/audio/Cueva1.mp3";

    private ReproductorMusica() {
    }

    /**
     * Devuelve la instancia unica del reproductor.
     * La primera llamada crea el Singleton.
     */
    public static synchronized ReproductorMusica getInstancia() {
        if (instancia == null) {
            instancia = new ReproductorMusica();
        }
        return instancia;
    }

    /**
     * Inicia la reproduccion en bucle del archivo por defecto
     * (datos/audio/Cueva1.mp3).
     * Si ya hay musica sonando, no hace nada (evita duplicados).
     */
    public void reproducir() {
        reproducir(RUTA_DEFECTO);
    }

    /**
     * Inicia la reproduccion en bucle de un archivo de audio.
     *
     * @param ruta  Ruta al archivo de audio (relativa o absoluta).
     */
    public void reproducir(String ruta) {
        if (mediaPlayer != null) {
            return; // ya hay musica
        }
        try {
            File archivo = new File(ruta);
            Media media = new Media(archivo.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("[ReproductorMusica] No se pudo reproducir: " + ruta);
            e.printStackTrace();
        }
    }

    /**
     * Detiene la musica y libera recursos.
     */
    public void detener() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    /**
     * Ajusta el volumen (0.0 silencio, 1.0 maximo).
     */
    public void setVolumen(double volumen) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(Math.max(0.0, Math.min(1.0, volumen)));
        }
    }

    /**
     * Devuelve el volumen actual (0.0 - 1.0).
     * Si no hay reproductor activo, devuelve 0.0.
     */
    public double getVolumen() {
        return (mediaPlayer != null) ? mediaPlayer.getVolume() : 0.0;
    }

    /**
     * Indica si hay musica reproduciendose actualmente.
     */
    public boolean estaReproduciendo() {
        return mediaPlayer != null
                && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
}
