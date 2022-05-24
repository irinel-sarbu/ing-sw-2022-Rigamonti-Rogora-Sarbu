package view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import network.LightModel;

public class GenericMenuSceneController implements GenericSceneController {
    @FXML
    private Pane navigationMenu, notYourTurnPane, showButtonsPane, studentMovementPane, studentMovementsPane, motherNaturePane, cloudSelectionPane;
    @FXML
    private Button backButton;
    @FXML
    private Label notYourTurnText; // TODO: change dynamically whose turn is in each instant

    private LightModel model;


    public void updateController(LightModel model) {
        this.model = model;
    }
}
