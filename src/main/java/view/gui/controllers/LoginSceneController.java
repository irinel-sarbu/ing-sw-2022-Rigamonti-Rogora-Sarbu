package view.gui.controllers;

import com.sun.javafx.scene.control.IntegerField;
import eventSystem.EventManager;
import eventSystem.events.local.EUpdateServerInfo;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginSceneController {

    public TextField ip;
    public IntegerField port;

    @FXML
    public void onLogin(MouseEvent mouseEvent) {
        System.out.println("Il fra ha premuto login");
        EventManager.notify(new EUpdateServerInfo(ip.getText(), port.getValue()));
    }
}
