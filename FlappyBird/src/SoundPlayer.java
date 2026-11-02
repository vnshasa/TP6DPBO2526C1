import javax.sound.sampled.*;
import java.net.URL;

public class SoundPlayer {
    private static Clip bgmClip;

    // Memutar musik yang berulang di Main Menu
    public static void playBGM(String path) {
        try {
            stopBGM();
            URL url = SoundPlayer.class.getResource(path);
            if (url != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
                bgmClip = AudioSystem.getClip();
                bgmClip.open(audioStream);
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                bgmClip.start();
            } else {
                System.out.println("File BGM tidak ditemukan di folder assets: " + path);
            }
        } catch (Exception e) {
            System.out.println("Gagal memutar BGM (" + path + "): " + e.getMessage());
        }
    }

    public static void stopBGM() {
        if (bgmClip != null) {
            if (bgmClip.isRunning()) {
                bgmClip.stop();
            }
            bgmClip.close();
        }
    }

    // Memutar efek suara sekali saja (bird, score, game_over)
    public static void playSFX(String path) {
        try {
            URL url = SoundPlayer.class.getResource(path);
            if (url != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
                Clip sfxClip = AudioSystem.getClip();
                sfxClip.open(audioStream);
                sfxClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        sfxClip.close();
                    }
                });
                sfxClip.start();
            } else {
                System.out.println("File SFX tidak ditemukan di folder assets: " + path);
            }
        } catch (Exception e) {
            System.out.println("Gagal memutar SFX (" + path + "): " + e.getMessage());
        }
    }
}