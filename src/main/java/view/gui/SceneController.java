package view.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import network.LightModel;
import util.Logger;
import view.gui.controllers.ErrorPopUpSceneController;
import view.gui.controllers.GameEndingController;
import view.gui.controllers.GenericSceneController;
import view.gui.controllers.LobbyJoinedSceneController;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class SceneController {
    private static Stage stage;
    private static Scene currentScene;
    private static Parent parent;
    private static GenericSceneController currentSceneController;

    private static File currentSong;
    private static Media currentMedia;
    private static MediaPlayer mediaPlayer = null;

    /**
     * Set the stage for the current scene
     * @param stage
     */
    public static void setStage(Stage stage) {
        SceneController.stage = stage;
    }

    /**
     * Starts the music Media Player reproducing the ServerConnectionMusic
     * NOTE: The class {@link MediaPlayer} depends on external libraries to reproduce music, and some systems does
     * not provide them out of the box, so it's possible that the game will start without the music
     */
    public static void startMediaPlayer() {
        try {
            currentSong = new File(Objects.requireNonNull(SceneController.class.getResource("/bgMusic/ServerConnectionMusic.mp3")).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        currentMedia = new Media(currentSong.toURI().toString());
        try {
            mediaPlayer = new MediaPlayer(currentMedia);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        } catch (Exception e) {
            Logger.warning("Could not create MediaPlayer, the game will start without music");
            currentSong = null;
            currentMedia = null;
            mediaPlayer = null;
        }
    }

    /**
     * Stops the current song
     */
    public static void stopMediaPlayer() {
        if (mediaPlayer == null) return; 
        mediaPlayer.stop();
        mediaPlayer.dispose();
    }

    /**
     * Get the current active scene
     * @return a reference to the scene
     */
    public static Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * Get the instance of the current scene controller
     * @return
     */
    public static GenericSceneController getCurrentSceneController() {
        return currentSceneController;
    }

    /**
     * Switch scene keeping the current music
     * @param sceneFile .fxml scene file path
     */
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

    /**
     * Switch scene and change music
     * @param sceneFile .fxml scene file path
     * @param songFile music file path
     */
    public static void switchSceneAndSong(String sceneFile, String songFile) {
        switchScene(sceneFile);
        if (mediaPlayer == null) return;
        mediaPlayer.stop();
        mediaPlayer.dispose();
        try {
            currentSong = new File(Objects.requireNonNull(SceneController.class.getResource(songFile)).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        currentMedia = new Media(currentSong.toURI().toString());
        mediaPlayer = new MediaPlayer(currentMedia);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    /**
     * Switch scene and change music, creating a new stage to display the scene
     * @param sceneFile .fxml scene file path
     * @param songFile music file path
     */
    public static void switchSceneSongAndStage(String sceneFile, String songFile) {
        stage.hide();
        stage = new Stage();
        stage.setResizable(true);
        stage.setTitle("Eriantys");
        stage.getIcons().add(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/ui/Icon.png"))));
        switchSceneAndSong(sceneFile, songFile);
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * Display message as a pop-up window
     * @param message String of the message to display on the pop-up window
     */
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

    /**
     * Switch current scene to the waiting room of the lobby
     * @param sceneFile .fxml scene file path
     * @param code joined game lobby unique code
     */
    public static void switchSceneToLobbyIdle(String sceneFile, String code) {
        switchScene(sceneFile);
        LobbyJoinedSceneController controller = (LobbyJoinedSceneController) currentSceneController;
        controller.editCode(code);
    }

    /**
     * Return to login scene
     */
    public static void switchBackToLogin() {
        stopMediaPlayer();
        stage.hide();
        stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Eriantys");
        stage.getIcons().add(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/ui/Icon.png"))));
        SceneController.switchScene("loginScene.fxml");
        SceneController.startMediaPlayer();
        stage.show();
    }

    /**
     * Return to scene of lobby creation or joining
     */
    public static void switchBackToCreateOrJoin() {
        stopMediaPlayer();
        stage.hide();
        stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Eriantys");
        stage.getIcons().add(new Image(Objects.requireNonNull(SceneController.class.getResourceAsStream("/ui/Icon.png"))));
        SceneController.switchScene("createOrJoin.fxml");
        SceneController.startMediaPlayer();
        stage.show();
    }

    /**
     * Switch to Game Overe scene
     * @param model Reference to the client's light model
     * @param winningPlayer Nickname of the player who won the game
     */
    public static void switchToGameEnd(LightModel model, String winningPlayer) {
        switchSceneAndSong("gameEnding.fxml", Objects.requireNonNull(SceneController.class.getClassLoader().getResource("bgMusic/InGameMusic3.mp3")).getFile());
        GameEndingController controller = (GameEndingController) getCurrentSceneController();
        controller.setUp(model, winningPlayer);
    }
}
