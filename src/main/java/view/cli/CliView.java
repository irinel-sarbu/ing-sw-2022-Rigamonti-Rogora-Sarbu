package view.cli;

import events.types.clientToClient.EUpdateServerInfo;
import events.types.clientToServer.ECreateLobbyRequest;
import events.types.clientToServer.EJoinLobbyRequest;
import events.types.clientToServer.EWizardChosen;
import model.board.CloudTile;
import model.board.IslandGroup;
import model.board.IslandTile;
import model.board.SchoolBoard;
import network.LightModel;
import util.GameMode;
import util.Wizard;
import view.View;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CliView extends View {
    Scanner scanner;

    @Override
    public void run() {
        this.scanner = new Scanner(System.in);

        System.out.println("Eriantys #LOGO TODO");
        askServerInfo();
    }

    public void closeScanner() {
        scanner.close();
        scanner = new Scanner(System.in);
    }

    private String readString(String defaultValue) {
        String string;
        string = scanner.nextLine();
        return string.isBlank() ? defaultValue : string;
    }

    private int readInt(int defaultValue) {
        int num;

        try {
            String numString = readString("");
            if (numString.isBlank())
                return defaultValue;
            num = Integer.parseInt(numString);
        } catch (NumberFormatException e) {
            System.err.print("Please insert a number and not a String >>> ");
            num = readInt(defaultValue);
        }

        return num;
    }

    @Override
    public void askServerInfo() {
        final int defaultPort = 5000;
        final String defaultAddress = "localhost";

        String address;
        int port;

        System.out.println("> Please specify the following settings. The default value is shown between brackets.");

        System.out.print("\rInsert server ADDRESS [localhost] >>> ");
        address = readString(defaultAddress);

        System.out.print("\rInsert server PORT [" + defaultPort + "] >>> ");
        port = readInt(defaultPort);

        notifyListeners(new EUpdateServerInfo(address, port));
    }

    private String askPlayerName() {
        String insertedName;

        do {
            System.out.print("\rInsert your name >>> ");
            insertedName = readString("");

            if (insertedName.equals("wwssadadba")) {
                displayMessage("I know you know...");
            }
        } while (insertedName.isBlank());

        return insertedName;
    }

    @Override
    public void chooseCreateOrJoin() {
        int selection;

        System.out.println("Choose between:");
        System.out.println(" [1] Create lobby");
        System.out.println(" [2] Join lobby");

        do {
            System.out.print("\rInsert your choice [1 or 2] >>> ");
            selection = readInt(0);
        } while ((selection != 1) && (selection != 2));

        switch (selection) {
            case 1 -> createLobby();
            case 2 -> joinLobby();
        }
    }

    @Override
    public void createLobby() {
        System.out.println("Choose between:");
        System.out.println(" [1] NORMAL");
        System.out.println(" [2] EXPERT");

        int gameMode;
        do {
            System.out.print("\rInsert your choice [1 or 2] >>> ");
            gameMode = readInt(0);
        } while ((gameMode != 1) && (gameMode != 2));

        int numOfPlayers;
        do {
            System.out.print("\rInsert how many players are going to play [2 or 3] >>> ");
            numOfPlayers = readInt(0);
        } while ((numOfPlayers != 2) && (numOfPlayers != 3));

        String nickname = askPlayerName();
        notifyListeners(new ECreateLobbyRequest(gameMode == 1 ? GameMode.NORMAL : GameMode.EXPERT, numOfPlayers, nickname));
    }

    @Override
    public void joinLobby() {
        System.out.print("\rInsert lobby code >>> ");
        String lobbyCode = readString("");

        String nickname = askPlayerName();
        notifyListeners(new EJoinLobbyRequest(lobbyCode, nickname));
    }

    @Override
    public void chooseWizard(List<Wizard> availableWizards) {
        System.out.println("Choose your wizard:");
        for (int i = 0; i < availableWizards.size(); i++) {
            System.out.println(" [" + i + "] " + availableWizards.get(i));
        }

        int choice;
        do {
            System.out.print("\rInsert your choice >>> ");
            choice = readInt(-1);
        } while (choice < 0 || choice >= availableWizards.size());

        notifyListeners(new EWizardChosen(availableWizards.get(choice)));
    }

    @Override
    public void update(LightModel model) {
        // Clear screen - doesn't work in intellij terminal
        System.out.print("\033[H\033[2J");

        System.out.println("BOARD");

        System.out.println("- islands:");
        printIslands(model.getIslandGroups());

        System.out.println("- mother nature position: " + model.getMotherNaturePosition());

        System.out.println("- cloud tiles: ");
        printCloudTiles(model.getCloudTiles());

        for (Map.Entry<String, SchoolBoard> entry : model.getSchoolBoardMap().entrySet()) {
            System.out.println("- schoolBoard of " + entry.getKey() + ":\n" + entry.getValue());
        }

        if (model.getCharacters() != null) {
            System.out.println("- extracted characters: " + model.getCharacters());
            if (model.getActiveCharacterEffect() != null) {
                System.out.println("- active character effect: " + model.getActiveCharacterEffect());
            } else {
                System.out.println("- active character effect: none");
            }
        }

        System.out.println("- assistants: " + model.getDeck());
    }

    private void printIslands(List<IslandGroup> islandGroups) {
        for (IslandGroup group : islandGroups) {
            String islandGroupName = "IslandGroup_" + String.format("%2s", group.getIslandGroupID()).replace(' ', '0');
            System.out.println("\t" + islandGroupName + ":");
            for (IslandTile island : group.getIslands()) {
                System.out.println("\t\t" + island);
            }
        }
        System.out.println();
    }

    private void printCloudTiles(List<CloudTile> cloudTiles) {
        for (CloudTile cloudTile : cloudTiles) {
            System.out.println("\t" + cloudTile);
        }
        System.out.println();
    }
}
