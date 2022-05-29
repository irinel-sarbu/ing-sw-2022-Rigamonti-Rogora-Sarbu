package view;

import model.board.Assistant;
import network.LightModel;
import util.CliHelper;
import util.Wizard;

import java.util.List;

public abstract class View {

    public abstract void run();

    public void displayMessage(String color, String message) {
        System.out.println(color + message + CliHelper.ANSI_RESET);
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void joinedLobbyDisplay(String code) {
    }

    public void allPlayersConnected() {
    }

    public void displayError(String errorMessage) {
        System.out.println(CliHelper.ANSI_RED + "ERROR " + errorMessage + CliHelper.ANSI_RESET);
    }

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

    public abstract void setupConnection(boolean connectionReset);

    public abstract void askNickname();

    public abstract void createLobby();

    public abstract void joinLobby();

    public abstract void chooseCreateOrJoin(boolean wasInLobby);

    public abstract void chooseWizard(List<Wizard> availableWizards);

    public abstract void chooseAssistant(List<Assistant> deck);

    public void playerChoseAssistant(Assistant assistant) {
    }

    public void otherPlayerIsChoosingAssistant() {
    }

    public abstract void showMenu(LightModel model, String client);

    public void displayIdleMenu(LightModel model, String playerName) {
    }

    public abstract void update(LightModel model);
}