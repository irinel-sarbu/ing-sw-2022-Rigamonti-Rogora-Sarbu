package view.gui.controllers;

import eventSystem.EventManager;
import eventSystem.events.local.EUpdateServerInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import util.Logger;
import view.gui.SceneController;


public class LoginSceneController implements GenericSceneController {

    public TextField ip;
    public TextField port;
    public Label warning;

    /**
     * notify client entering lobby
     * @param mouseEvent
     */
    @FXML
    public void onLogin(MouseEvent mouseEvent) {
        Logger.info("Login button Pressed");
        String portValue = port.getText();
        if (portValue.matches("\\d{1,8}")) {
            EventManager.notify(new EUpdateServerInfo(ip.getText(), Integer.parseInt(portValue)));
            warning.opacityProperty().setValue(0);
        } else {
            warning.opacityProperty().setValue(100);
        }
    }
}
