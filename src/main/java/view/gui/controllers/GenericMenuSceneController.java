package view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import network.LightModel;
import util.GameMode;
import view.gui.SceneController;


public class GenericMenuSceneController implements GenericSceneController {
    @FXML
    private Pane showButtonsPane, studentMovementPane, studentMovementsPane, motherNaturePane, cloudSelectionPane;
    @FXML
    private Button backButton, characterEffectButton, characterEffectButton1, characterEffectButton2;
    @FXML
    private Label notYourTurnText;

    private LightModel model;
    private String playerName;

    /**
     * enable student movement
     * @param mouseEvent
     */
    @FXML
    public void onMoveStudent(MouseEvent mouseEvent) {
        studentMovementPane.setVisible(false);
        showButtonsPane.setVisible(false);
        studentMovementsPane.setVisible(true);
        backButton.setVisible(true);
    }

    /**
     * return to previous scene
     * @param mouseEvent
     */
    @FXML
    public void onBack(MouseEvent mouseEvent) {
        studentMovementsPane.setVisible(false);
        showButtonsPane.setVisible(true);
        studentMovementPane.setVisible(true);
        backButton.setVisible(false);
    }

    /**
     * switch to island view
     * @param mouseEvent
     */
    @FXML
    public void onShowIslands(MouseEvent mouseEvent) {
        SceneController.switchScene("IslandView.fxml");
        IslandViewSceneController controller = (IslandViewSceneController) SceneController.getCurrentSceneController();
        controller.updateView(model);
    }

    /**
     * switch to entrance student selection (movement to dining)
     * @param mouseEvent
     */
    @FXML
    public void onToDining(MouseEvent mouseEvent) {
        SceneController.switchScene("entranceScene.fxml");
        EntranceSceneController controller = (EntranceSceneController) SceneController.getCurrentSceneController();
        controller.setUp(model, playerName, false);
    }

    /**
     * switch to entrance student selection (movement to island)
     * @param mouseEvent
     */
    @FXML
    public void onToIsland(MouseEvent mouseEvent) {
        SceneController.switchScene("entranceScene.fxml");
        EntranceSceneController controller = (EntranceSceneController) SceneController.getCurrentSceneController();
        controller.setUp(model, playerName, true);
    }

    /**
     * switch to mother nature movement
     * @param mouseEvent
     */
    @FXML
    public void onMoveMN(MouseEvent mouseEvent) {
        SceneController.switchScene("motherNatureMovement.fxml");
        MotherNatureMovementSceneController controller = (MotherNatureMovementSceneController) SceneController.getCurrentSceneController();
        controller.setupIslands(model);
    }

    /**
     * switch to cloud selection
     * @param mouseEvent
     */
    @FXML
    public void onSelectCloud(MouseEvent mouseEvent) {
        SceneController.switchScene("CloudChoice.fxml");
        CloudChoiceSceneController controller = (CloudChoiceSceneController) SceneController.getCurrentSceneController();
        controller.setupClouds(model);
    }

    /**
     * switch to character selection
     * @param mouseEvent
     */
    @FXML
    public void onCharacter(MouseEvent mouseEvent) {
        SceneController.switchScene("CharacterSelection.fxml");
        CharacterSelectionSceneController controller = (CharacterSelectionSceneController) SceneController.getCurrentSceneController();
        controller.setUpCharacterChoice(model);
    }

    /**
     * switch to school board view
     * @param mouseEvent
     */
    @FXML
    public void onSchoolBoard(MouseEvent mouseEvent) {
        SceneController.switchScene("SchoolboardView.fxml");
        SchoolboardViewSceneController controller = (SchoolboardViewSceneController) SceneController.getCurrentSceneController();
        controller.updateView(model);
    }

    /**
     * Set correct menu depending on current context
     * @param model reference to light model
     * @param playerName name of the acting player
     */
    public void setController(LightModel model, String playerName) {
        this.model = model;
        this.playerName = playerName;
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

    /**
     * switch to idle menu hiding buttons
     * @param model reference to light model
     * @param playerName current acting player
     */
    public void setIdle(LightModel model, String playerName) {
        this.model = model;
        this.playerName = playerName;
        notYourTurnText.setText(playerName + " turn started...");
        notYourTurnText.setVisible(true);
        showButtonsPane.setVisible(true);
    }
}
