package view;

import model.board.Assistant;
import network.LightModel;
import util.CliHelper;
import util.Wizard;

import java.util.List;

public abstract class View {

    /**
     * used to start the view component
     */
    public abstract void run();

    /**
     * displays a message with colors, used in cli
     */
    public void displayMessage(String color, String message) {
        System.out.println(color + message + CliHelper.ANSI_RESET);
    }

    /**
     * display a basic message, used in cli
     */
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * displays the lobby joined
     *
     * @param code is the code of the lobby joined
     */
    public void joinedLobbyDisplay(String code) {
    }

    /**
     * calls a view update when all players connected, used in gui
     */
    public void allPlayersConnected() {
    }

    /**
     * displays an error message, in cli red, in gui an error popup
     *
     * @param errorMessage is the error message
     */
    public void displayError(String errorMessage) {
        System.out.println(CliHelper.ANSI_RED + "ERROR " + errorMessage + CliHelper.ANSI_RESET);
    }

    /**
     * used in cli to clear the console
     *
     * @param numOfLines is the number of lines that is wished to be cleared
     */
    public void clearLines(int numOfLines) {
        if (numOfLines <= 0) return;
        for (int i = 0; i < numOfLines; i++) {
            // Set cursor at start line
            System.out.print("\r");
            // Clear line
            System.out.print("\033[2K");
            // Move up by one
            System.out.printf("\033[%dA", 1);
            // Clear line
            System.out.print("\033[2K");

            System.out.print("Cleared " + numOfLines);
        }
    }

    /**
     * displays the connection set up scene/part of the cli
     *
     * @param connectionReset
     */
    public abstract void setupConnection(boolean connectionReset);

    /**
     * displays the nickname query
     */
    public abstract void askNickname();

    /**
     * displays the lobby creation scene/part of the cli
     */
    public abstract void createLobby();

    /**
     * displays the lobby join scene/part of the cli
     */
    public abstract void joinLobby();

    /**
     * displays the lobby choice between create and join scene/part of the cli
     */
    public abstract void chooseCreateOrJoin(boolean wasInLobby);

    /**
     * displays the lobby choice of the wizard scene/part of the cli
     */
    public abstract void chooseWizard(List<Wizard> availableWizards);

    /**
     * displays the lobby choice of the assistant scene/part of the cli
     */
    public abstract void chooseAssistant(List<Assistant> deck);

    /**
     * displays the chosen assistant scene/part of the cli
     */
    public void playerChoseAssistant(Assistant assistant) {
    }

    /**
     * displays the idle scene/part of the cli waiting for other players to choose their assistants
     */
    public void otherPlayerIsChoosingAssistant() {
    }

    /**
     * displays the MAIN MENU scene/part of the cli
     */
    public abstract void showMenu(LightModel model, String client);

    /**
     * displays the IDLE MENU scene/part of the cli, which is played when it's not the client's turn
     */
    public void displayIdleMenu(LightModel model, String playerName) {
    }

    /**
     * displays the Game Over scene/part of the cli
     */
    public void gameOver(LightModel model, String winningPlayer) {
    }

    /**
     * Updates the view with lightModel changes
     */
    public abstract void update(LightModel model);
}