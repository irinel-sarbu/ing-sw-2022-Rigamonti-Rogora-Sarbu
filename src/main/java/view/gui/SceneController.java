package view.gui;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.Logger;

import java.io.File;
import java.io.IOException;

public class SceneController {
    private static Stage stage;
    private static Scene currentScene;
    private static Parent parent;

    private static File currentSong;
    private static Media currentMedia;
    private static MediaPlayer mediaPlayer;

    public static void setStage(Stage stage) {
        SceneController.stage = stage;
    }

    public static void startMediaPlayer(){
        currentSong = new File("src/main/resources/bgMusic/ServerConnectionMusic.mp3");
        currentMedia = new Media(currentSong.toURI().toString());
        mediaPlayer = new MediaPlayer(currentMedia);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static void switchScene(String sceneFile) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + sceneFile));
            Parent root = loader.load();
            currentScene = new Scene(root);
            currentScene.setRoot(root);
            stage.setScene(currentScene);
        } catch (IOException e) {
            Logger.severe(e.getMessage());
        }
    }

    public static void switchSceneAndSong(String sceneFile, String songFile) {
        switchScene(sceneFile);
        mediaPlayer.stop();
        mediaPlayer.dispose();
        currentSong = new File(songFile);
        currentMedia = new Media(currentSong.toURI().toString());
        mediaPlayer = new MediaPlayer(currentMedia);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }
}
