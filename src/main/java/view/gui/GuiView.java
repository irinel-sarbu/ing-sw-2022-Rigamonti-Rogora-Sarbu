package view.gui;

import javafx.application.Platform;
import model.board.Assistant;
import network.LightModel;
import util.Logger;
import util.Wizard;
import view.View;
import view.gui.controllers.*;

import java.util.List;

public class GuiView extends View {
    @Override
    public void run() {

    }

    @Override
    public void setupConnection(boolean connectionReset) {

    }

    @Override
    public void allPlayersConnected() {
        Platform.runLater(() -> SceneController.switchSceneSongAndStage("wizardChoiceScene.fxml", "src/main/resources/bgMusic/InGameMusic2.mp3"));
    }

    @Override
    public void askNickname() {
        if (SceneController.getCurrentSceneController() instanceof LoginSceneController) {
            Platform.runLater(() -> SceneController.switchSceneAndSong("nameSelection.fxml", "src/main/resources/bgMusic/MainMenuMusic.mp3"));
        }
    }

    @Override
    public void joinedLobbyDisplay(String code) {
        if (SceneController.getCurrentSceneController() instanceof CreateOrJoinSceneController) {
            Platform.runLater(() -> SceneController.switchSceneToLobbyIdle("lobbyJoined.fxml", code));
        }
    }

    @Override
    public void createLobby() {
    }

    @Override
    public void joinLobby() {

    }

    @Override
    public void chooseCreateOrJoin(boolean wasInLobby) {
        if (SceneController.getCurrentSceneController() instanceof NameSelectionSceneController) {
            Platform.runLater(() -> SceneController.switchScene("createOrJoin.fxml"));
        }
    }

    @Override
    public void chooseWizard(List<Wizard> availableWizards) {
        Platform.runLater(() -> {
            if (SceneController.getCurrentSceneController() instanceof WizardChoiceController) {
                Logger.info("choosing wizard from " + availableWizards.toString());
                WizardChoiceController controller = (WizardChoiceController) SceneController.getCurrentSceneController();
                controller.chooseWizards(availableWizards);
            }
        });
    }

    @Override
    public void chooseAssistant(List<Assistant> deck) {
        Platform.runLater(() -> {
            if (SceneController.getCurrentSceneController() instanceof AssistantChoiceSceneController) {
                Logger.info("Choose assistant from" + deck.toString());
                AssistantChoiceSceneController controller = (AssistantChoiceSceneController) SceneController.getCurrentSceneController();
                controller.showAssistants(deck);
            } else {
                SceneController.switchScene("assistantChoiceScene.fxml");
                AssistantChoiceSceneController controller = (AssistantChoiceSceneController) SceneController.getCurrentSceneController();
                controller.showAssistants(deck);
            }
        });
    }

    @Override
    public void playerChoseAssistant(Assistant assistant) {
        Platform.runLater(() -> {
            if (SceneController.getCurrentSceneController() instanceof AssistantChoiceSceneController) {
                Logger.info("Player chose assistant " + assistant.toString());
                AssistantChoiceSceneController controller = (AssistantChoiceSceneController) SceneController.getCurrentSceneController();
                controller.updateOtherChoices(assistant.getValue());
            }
        });
    }

    @Override
    public void otherPlayerIsChoosingAssistant() {
        Platform.runLater(() -> {
            if (!(SceneController.getCurrentSceneController() instanceof AssistantChoiceSceneController)) {
                SceneController.switchScene("assistantChoiceScene.fxml");
            }
        });
    }

    @Override
    public void showMenu(LightModel model, String client) {
        Platform.runLater(() -> {
            SceneController.switchScene("genericMenuScene.fxml");
            GenericMenuSceneController controller = (GenericMenuSceneController) SceneController.getCurrentSceneController();
            controller.setController(model, client);
        });
    }

    @Override
    public void displayIdleMenu(LightModel model, String playerName) {
        Platform.runLater(() -> {
            SceneController.switchScene("genericMenuScene.fxml");
            GenericMenuSceneController controller = (GenericMenuSceneController) SceneController.getCurrentSceneController();
            controller.setIdle(model, playerName);
        });
    }

    @Override
    public void update(LightModel model) {
        Platform.runLater(() -> {
            if (SceneController.getCurrentSceneController() instanceof IslandViewSceneController) {
                IslandViewSceneController controller = (IslandViewSceneController) SceneController.getCurrentSceneController();
                controller.updateView(model);
            }
        });
    }

    @Override
    public void displayMessage(String message) {
    }

    @Override
    public void displayError(String message) {
        Platform.runLater(() -> SceneController.displayMessagePopUp(message));
    }
}
