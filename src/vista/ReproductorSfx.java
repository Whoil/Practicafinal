package vista;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.scene.media.AudioClip;

/**
 * ReproductorSfx - efectos cortos generados por codigo.
 *
 * Evita depender de descargas externas para la demo. Los clips se crean como
 * WAV temporales y JavaFX los reproduce con AudioClip.
 */
public final class ReproductorSfx {

    private static ReproductorSfx instancia;

    private final AudioClip recoger;
    private final AudioClip ataque;
    private final AudioClip dano;
    private final AudioClip puerta;
    private final AudioClip guardar;
    private final AudioClip pausa;

    private double volumen = 0.9;

    private ReproductorSfx() {
        recoger = crearClip("recoger", new double[] {660, 990}, 130, 0.55);
        ataque = crearClip("ataque", new double[] {120, 220, 440}, 210, 0.95);
        dano = crearClip("dano", new double[] {130, 170}, 180, 0.75);
        puerta = crearClip("puerta", new double[] {90, 180, 360}, 420, 0.85);
        guardar = crearClip("guardar", new double[] {523, 659, 784}, 180, 0.55);
        pausa = crearClip("pausa", new double[] {440, 330}, 100, 0.45);
    }

    public static synchronized ReproductorSfx getInstancia() {
        if (instancia == null) {
            instancia = new ReproductorSfx();
        }
        return instancia;
    }

    public void reproducirRecoger() { reproducir(recoger); }
    public void reproducirAtaque() { reproducir(ataque); }
    public void reproducirDano() { reproducir(dano); }
    public void reproducirPuerta() { reproducir(puerta); }
    public void reproducirGuardar() { reproducir(guardar); }
    public void reproducirPausa() { reproducir(pausa); }

    public void setVolumen(double volumen) {
        this.volumen = Math.max(0.0, Math.min(1.0, volumen));
    }

    static byte[] crearWavParaTest(double[] frecuencias, int duracionMs, double volumen) {
        return crearWav(frecuencias, duracionMs, volumen);
    }

    private AudioClip crearClip(String nombre, double[] frecuencias, int duracionMs, double volumenClip) {
        try {
            File archivo = File.createTempFile("escape-mazmorra-" + nombre + "-", ".wav");
            archivo.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(archivo)) {
                out.write(crearWav(frecuencias, duracionMs, volumenClip));
            }
            AudioClip clip = new AudioClip(archivo.toURI().toString());
            clip.setVolume(volumen);
            return clip;
        } catch (IOException | RuntimeException e) {
            System.err.println("[ReproductorSfx] No se pudo crear SFX: " + nombre);
            return null;
        }
    }

    private static byte[] crearWav(double[] frecuencias, int duracionMs, double volumen) {
        final int sampleRate = 44100;
        int samples = sampleRate * duracionMs / 1000;
        byte[] data = new byte[samples * 2];
        for (int i = 0; i < samples; i++) {
            double t = i / (double) sampleRate;
            double env = Math.max(0.0, 1.0 - Math.pow(i / (double) samples, 1.7));
            double valor = 0.0;
            for (double frecuencia : frecuencias) {
                valor += Math.sin(2 * Math.PI * frecuencia * t);
            }
            valor = (valor / frecuencias.length) * volumen * env;
            short muestra = (short) Math.max(Short.MIN_VALUE,
                    Math.min(Short.MAX_VALUE, valor * Short.MAX_VALUE));
            data[i * 2] = (byte) (muestra & 0xff);
            data[i * 2 + 1] = (byte) ((muestra >> 8) & 0xff);
        }

        byte[] wav = new byte[44 + data.length];
        escribirAscii(wav, 0, "RIFF");
        escribirInt(wav, 4, 36 + data.length);
        escribirAscii(wav, 8, "WAVE");
        escribirAscii(wav, 12, "fmt ");
        escribirInt(wav, 16, 16);
        escribirShort(wav, 20, 1);
        escribirShort(wav, 22, 1);
        escribirInt(wav, 24, sampleRate);
        escribirInt(wav, 28, sampleRate * 2);
        escribirShort(wav, 32, 2);
        escribirShort(wav, 34, 16);
        escribirAscii(wav, 36, "data");
        escribirInt(wav, 40, data.length);
        System.arraycopy(data, 0, wav, 44, data.length);
        return wav;
    }

    private static void escribirAscii(byte[] destino, int offset, String texto) {
        for (int i = 0; i < texto.length(); i++) {
            destino[offset + i] = (byte) texto.charAt(i);
        }
    }

    private static void escribirInt(byte[] destino, int offset, int valor) {
        destino[offset] = (byte) (valor & 0xff);
        destino[offset + 1] = (byte) ((valor >> 8) & 0xff);
        destino[offset + 2] = (byte) ((valor >> 16) & 0xff);
        destino[offset + 3] = (byte) ((valor >> 24) & 0xff);
    }

    private static void escribirShort(byte[] destino, int offset, int valor) {
        destino[offset] = (byte) (valor & 0xff);
        destino[offset + 1] = (byte) ((valor >> 8) & 0xff);
    }

    private void reproducir(AudioClip clip) {
        if (clip != null) {
            clip.play(volumen);
        }
    }
}
