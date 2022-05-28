package view.gui.controllers;


import eventSystem.EventManager;
import eventSystem.events.local.EUpdateNickname;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import util.Logger;

import java.awt.*;


public class NameSelectionSceneController implements GenericSceneController {
    public TextField name;
    public Pane balloonText;
    public Label errorLabel;

    @FXML
    public void onNameConfirmed(MouseEvent mouseEvent) {
        if (name.getText().length() <= 0) {
            errorLabel.setText("Name too Short");
            balloonText.setVisible(true);
        } else if (name.getText().length() > 10) {
            errorLabel.setText("Name too Long");
            balloonText.setVisible(true);
        } else {
            balloonText.setVisible(false);
            Logger.info("Name Selected: " + name.getText());
            EventManager.notify(new EUpdateNickname(name.getText()));
        }
    }
}
