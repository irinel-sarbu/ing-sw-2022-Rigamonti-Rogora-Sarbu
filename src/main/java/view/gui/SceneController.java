package view.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import network.LightModel;
import util.Logger;
import util.Wizard;
import view.gui.controllers.GenericMenuSceneController;
import view.gui.controllers.GenericSceneController;
import view.gui.controllers.ErrorPopUpSceneController;
import view.gui.controllers.LobbyJoinedSceneController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SceneController {
    private static Stage stage;
    private static Scene currentScene;
    private static Parent parent;
    private static GenericSceneController currentSceneController;

    private static File currentSong;
    private static Media currentMedia;
    private static MediaPlayer mediaPlayer;

    public static void setStage(Stage stage) {
        SceneController.stage = stage;
    }

    public static void startMediaPlayer() {
        currentSong = new File("src/main/resources/bgMusic/ServerConnectionMusic.mp3");
        currentMedia = new Media(currentSong.toURI().toString());
        mediaPlayer = new MediaPlayer(currentMedia);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public static void stopMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.dispose();
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static GenericSceneController getCurrentSceneController() {
        return currentSceneController;
    }

    public static void switchScene(String sceneFile) {
        try {
            Logger.info("Switching Scene...");
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + sceneFile));
            Parent root = loader.load();
            currentSceneController = loader.getController();

            currentScene = new Scene(root);
            currentScene.setRoot(root);
            stage.setScene(currentScene);
            Logger.info("Scene Switched");
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

    public static void switchSceneSongAndStage(String sceneFile, String songFile) {
        stage.hide();
        stage = new Stage();
        stage.setResizable(true);
        stage.setTitle("Eriantys");
        stage.getIcons().add(new Image("/ui/Icon.png"));
        switchSceneAndSong(sceneFile, songFile);
        stage.setMaximized(true);
        stage.show();
    }

    public static void displayMessagePopUp(String message) {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/errorPopUpScene.fxml"));
        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            Logger.severe("Couldn't Load PopUp Scene");
            e.printStackTrace();
            return;
        }
        ErrorPopUpSceneController errorPopUpSceneController = loader.getController();
        Scene popUpScene = new Scene(parent);
        errorPopUpSceneController.setScene(popUpScene);
        errorPopUpSceneController.display(message);
        errorPopUpSceneController.showWindow();
    }

    public static void switchSceneToLobbyIdle(String sceneFile, String code) {
        switchScene(sceneFile);
        LobbyJoinedSceneController controller = (LobbyJoinedSceneController) currentSceneController;
        controller.editCode(code);
    }

    public static void switchBackToLogin() {
        stopMediaPlayer();
        stage.hide();
        stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Eriantys");
        stage.getIcons().add(new Image("/ui/Icon.png"));
        SceneController.switchScene("loginScene.fxml");
        SceneController.startMediaPlayer();
        stage.show();
    }

    public static void switchBackToCreateOrJoin() {
        stopMediaPlayer();
        stage.hide();
        stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Eriantys");
        stage.getIcons().add(new Image("/ui/Icon.png"));
        SceneController.switchScene("createOrJoin.fxml");
        SceneController.startMediaPlayer();
        stage.show();
    }
}
