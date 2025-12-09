package com.comp2042.gameUI;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * Simple audio helper for background music playback using JavaFX MediaPlayer.
 *
 * <p>Provides methods to play a looping background track and to stop playback.
 */

public class SoundOrganiser {
    private MediaPlayer mediaPlayer;
    /**
     * Play a background music file (resource on classpath). If the resource cannot be
     * located the call is ignored and a message is printed to stdout.
     *
     * @param fileName name of the resource file (e.g. "music.mp3")
     */

    public void playBackgroundMusic(String fileName) {
        try {
            URL resource = getClass().getClassLoader().getResource(fileName);
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever
                mediaPlayer.setVolume(0.3); // Set volume (0.0 to 1.0)
                mediaPlayer.play();
            } else {
                System.out.println("Music file not found: " + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
