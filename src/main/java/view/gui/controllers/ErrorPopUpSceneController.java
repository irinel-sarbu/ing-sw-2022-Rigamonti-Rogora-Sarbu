package view.gui.controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import util.Logger;
import view.gui.SceneController;

import java.awt.event.MouseEvent;

public class ErrorPopUpSceneController implements GenericSceneController {
    private final Stage stage;
    private final int pauseTime = 5;

    @FXML
    private Label textLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private AnchorPane anchor;

    public ErrorPopUpSceneController() {
        stage = new Stage();
        stage.initOwner(SceneController.getCurrentScene().getWindow());
        stage.setTitle("PopUp Message");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    public void display(String message) {
        textLabel.setText(message);
    }

    public void onXButtonClicked() {
        stage.close();
    }

    public void showWindow() {
        Logger.info("Showing PopUp");
        stage.show();
        PauseTransition wait = new PauseTransition(Duration.seconds(pauseTime));
        wait.setOnFinished((e) -> {
            stage.close();
        });
        wait.play();
    }
}
