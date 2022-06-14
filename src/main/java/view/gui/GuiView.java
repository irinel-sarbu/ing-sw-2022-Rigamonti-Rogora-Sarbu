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

    /**
     * Return to log in scene if the parameter is true
     * @param connectionReset
     */
    @Override
    public void setupConnection(boolean connectionReset) {
        Platform.runLater(() -> {
            if (connectionReset) {
                displayError("Connection with server closed");
                SceneController.switchBackToLogin();
            }
        });
    }

    /**
     * Swith to the wizard selection scene
     */
    @Override
    public void allPlayersConnected() {
        Platform.runLater(() -> SceneController.switchSceneSongAndStage("wizardChoiceScene.fxml", "/bgMusic/InGameMusic2.mp3"));
    }

    /**
     * Switch to the name selection scene if the {@link SceneController} is currently a {@link LoginSceneController}
     */
    @Override
    public void askNickname() {
        if (SceneController.getCurrentSceneController() instanceof LoginSceneController) {
            Platform.runLater(() -> SceneController.switchSceneAndSong("nameSelection.fxml", "/bgMusic/MainMenuMusic.mp3"));
        }
    }

    /**
     * Switch to lobby's waiting room scene
     * @param code unique lobby code
     */
    @Override
    public void joinedLobbyDisplay(String code) {
        if (SceneController.getCurrentSceneController() instanceof CreateOrJoinSceneController) {
            Platform.runLater(() -> SceneController.switchSceneToLobbyIdle("lobbyJoined.fxml", code));
        }
    }

    /**
     * empty
     */
    @Override
    public void createLobby() {
    }

    /**
     * empty
     */
    @Override
    public void joinLobby() {

    }

    /**
     * Switch to create/join lobby scene, if the parameter is {@link true} notify disconnection from lobby
     * @param wasInLobby The client was previously in a different lobby (a player has disconnected)
     */
    @Override
    public void chooseCreateOrJoin(boolean wasInLobby) {
        if (wasInLobby) {
            Platform.runLater(() -> {
                displayError("A player disconnected, Game Aborted...");
                SceneController.switchBackToCreateOrJoin();
            });
        } else {
            if (SceneController.getCurrentSceneController() instanceof NameSelectionSceneController) {
                Platform.runLater(() -> SceneController.switchScene("createOrJoin.fxml"));
            }
        }
    }

    /**
     * Ask the player to choose a wizard from the provided list
     * @param availableWizards list of available wizards
     */
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

    /**
     * Ask the player to choose an assistant from the provided list
     * @param deck list of available assistants
     */
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

    /**
     * Add the selected assistant to the list of selected ones
     * @param assistant the assistant to add
     */
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

    /**
     * Wait for other players to choose assistant
     */
    @Override
    public void otherPlayerIsChoosingAssistant() {
        Platform.runLater(() -> {
            if (!(SceneController.getCurrentSceneController() instanceof AssistantChoiceSceneController)) {
                SceneController.switchScene("assistantChoiceScene.fxml");
            }
        });
    }

    /**
     * Display generic menu
     * @param model current light model
     * @param client client name
     */
    @Override
    public void showMenu(LightModel model, String client) {
        Platform.runLater(() -> {
            SceneController.switchScene("genericMenuScene.fxml");
            GenericMenuSceneController controller = (GenericMenuSceneController) SceneController.getCurrentSceneController();
            controller.setController(model, client);
        });
    }

    /**
     * Display idle menu (only views, no actions allowed)
     * @param model current light model
     * @param playerName client name
     */
    @Override
    public void displayIdleMenu(LightModel model, String playerName) {
        Platform.runLater(() -> {
            SceneController.switchScene("genericMenuScene.fxml");
            GenericMenuSceneController controller = (GenericMenuSceneController) SceneController.getCurrentSceneController();
            controller.setIdle(model, playerName);
        });
    }

    /**
     * Update current view scene
     * @param model provide infos about the game with a reference to the light model
     */
    @Override
    public void update(LightModel model) {
        Platform.runLater(() -> {
            if (SceneController.getCurrentSceneController() instanceof IslandViewSceneController) {
                IslandViewSceneController controller = (IslandViewSceneController) SceneController.getCurrentSceneController();
                controller.updateView(model);
            }
            if (SceneController.getCurrentSceneController() instanceof SchoolboardViewSceneController) {
                SchoolboardViewSceneController controller = (SchoolboardViewSceneController) SceneController.getCurrentSceneController();
                controller.updateView(model);
            }
        });
    }

    /**
     * Switch to GameOver scene
     * @param model Reference to the light model
     * @param winningPlayer Name of the winning player
     */
    @Override
    public void gameOver(LightModel model, String winningPlayer) {
        Platform.runLater(() -> {
            SceneController.switchToGameEnd(model, winningPlayer);
        });
    }

    /**
     * empty
     */
    @Override
    public void displayMessage(String message) {
    }

    /**
     * Display a pop-up error
     * @param message to display
     */
    @Override
    public void displayError(String message) {
        Platform.runLater(() -> SceneController.displayMessagePopUp(message));
    }
}
