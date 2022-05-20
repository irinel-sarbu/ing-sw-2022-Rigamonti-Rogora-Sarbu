package view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

//used as a loading screen
public class LobbyJoinedSceneController implements GenericSceneController {
    @FXML
    private TextField textField;

    public void editCode(String code) {
        textField.setText(code);
        textField.setVisible(true);
    }
}
