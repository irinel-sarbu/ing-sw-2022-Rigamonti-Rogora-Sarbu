package view.gui;

import javafx.application.Platform;
import model.board.Assistant;
import network.LightModel;
import util.Logger;
import util.Wizard;
import view.View;
import view.gui.controllers.CreateOrJoinSceneController;
import view.gui.controllers.LoginSceneController;
import view.gui.controllers.NameSelectionSceneController;

import java.util.List;

public class GuiView extends View {
    @Override
    public void run() {

    }

    @Override
    public void setupConnection() {

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
    public void chooseCreateOrJoin() {
        if (SceneController.getCurrentSceneController() instanceof NameSelectionSceneController) {
            Platform.runLater(() -> SceneController.switchScene("createOrJoin.fxml"));
        }
    }

    @Override
    public void chooseWizard(List<Wizard> availableWizards) {

    }

    @Override
    public void chooseAssistant(List<Assistant> deck) {

    }

    @Override
    public void showMenu(LightModel model, String client) {

    }

    @Override
    public void update(LightModel model) {

    }

    @Override
    public void displayMessage(String message) {
    }

    @Override
    public void displayError(String message) {
        Platform.runLater(() -> SceneController.displayMessagePopUp(message));
    }
}
