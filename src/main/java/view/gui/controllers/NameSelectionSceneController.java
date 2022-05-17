package view.gui.controllers;


import eventSystem.EventManager;
import eventSystem.events.local.EUpdateNickname;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import util.Logger;



public class NameSelectionSceneController{
    public TextField name;

    @FXML
    public void onNameConfirmed(MouseEvent mouseEvent) {
        Logger.info("Name Selected: " + name.getText());
        EventManager.notify(new EUpdateNickname(name.getText()));
    }
}
