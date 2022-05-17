package view.gui;

import controller.client.ClientController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import util.Logger;
import view.View;

import java.io.File;
import java.io.IOException;

public class GuiApplication extends Application {

    private ClientController clientController;
    private View guiView;


    @Override
    public void start(Stage stage) throws Exception {
        this.guiView = new GuiView();
        this.clientController = new ClientController(guiView);
        stage.setResizable(false);
        stage.setTitle("Eriantys");
        SceneController.setStage(stage);
        SceneController.switchScene("loginScene.fxml");
        SceneController.startMediaPlayer();
        stage.show();
    }

    @Override
    public void stop() {
        SceneController.stopMediaPlayer();
        Platform.exit();
        System.exit(0);
    }
}
