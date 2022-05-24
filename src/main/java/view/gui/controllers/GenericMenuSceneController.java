package view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import network.LightModel;
import util.GameMode;


public class GenericMenuSceneController implements GenericSceneController {
    @FXML
    private Pane showButtonsPane, studentMovementPane, studentMovementsPane, motherNaturePane, cloudSelectionPane;
    @FXML
    private Button backButton, characterEffectButton, characterEffectButton1, characterEffectButton2;
    @FXML
    private Label notYourTurnText;

    private LightModel model;

    @FXML
    public void onMoveStudent(MouseEvent mouseEvent) {
        studentMovementPane.setVisible(false);
        showButtonsPane.setVisible(false);
        studentMovementsPane.setVisible(true);
        backButton.setVisible(true);
    }

    @FXML
    public void onBack(MouseEvent mouseEvent) {
        studentMovementsPane.setVisible(false);
        showButtonsPane.setVisible(true);
        studentMovementPane.setVisible(true);
        backButton.setVisible(false);
    }

    public void setController(LightModel model) {
        this.model = model;
        if (model.getGameMode() == GameMode.EXPERT) {
            characterEffectButton.setVisible(true);
            characterEffectButton1.setVisible(true);
            characterEffectButton2.setVisible(true);
        }
        showButtonsPane.setVisible(true);
        switch (model.getGameState()) {
            case STUDENT_MOVEMENT -> {
                studentMovementPane.setVisible(true);
            }
            case MOTHERNATURE_MOVEMENT -> {
                motherNaturePane.setVisible(true);
            }
            case TURN_EPILOGUE -> {
                cloudSelectionPane.setVisible(true);
            }
        }
    }

    public void setIdle(LightModel model, String playerName) {
        this.model = model;
        notYourTurnText.setText(playerName + " turn started...");
        notYourTurnText.setVisible(true);
        showButtonsPane.setVisible(true);
    }
}
