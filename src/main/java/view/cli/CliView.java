package view.cli;

import events.Event;
import events.types.clientToClient.ConnectEvent;
import events.types.clientToClient.PlayerNameInsertedEvent;
import events.types.clientToServer.CreateGameEvent;
import events.types.clientToServer.JoinGameEvent;
import model.GameModel;
import util.GameMode;
import view.View;

import java.util.Scanner;

public class CliView extends View {
    Scanner scanner;

    @Override
    public void run() {
        this.scanner = new Scanner(System.in);

        System.out.println("Eriantys #LOGO TODO");
        getServerInfo();
    }

    @Override
    public void getServerInfo() {
        final int defaultPort = 12345;
        final String defaultAddress = "localhost";

        String insertedAddress = "";
        String insertedPort = "";

        System.out.print("\rInsert server ADDRESS [localhost] >>> ");
        insertedAddress = scanner.nextLine();

        System.out.print("\rInsert server PORT [" + defaultPort + "] >>> ");
        insertedPort = scanner.nextLine();

        String eventIP = insertedAddress.isEmpty() ? defaultAddress : insertedAddress;
        int eventPort = insertedPort.isEmpty() ? defaultPort : Integer.parseInt(insertedPort);

        notifyListeners(new ConnectEvent(eventIP, eventPort));
    }

    @Override
    public void getPlayerName() {
        String insertedName = "";

        do {
            System.out.print("\rInsert your name >>> ");
            insertedName = scanner.nextLine();
        } while (insertedName.isEmpty());

        notifyListeners(new PlayerNameInsertedEvent(insertedName));
    }

    @Override
    public void chooseCreateOrJoin() {
        int createOrJoin;

        System.out.println("Choose between:");
        System.out.println(" [1] Create lobby");
        System.out.println(" [2] Join lobby");

        do {
            System.out.print("\rInsert your choice [1 or 2] >>> ");
            createOrJoin = scanner.nextInt();
        } while ((createOrJoin != 1) && (createOrJoin != 2));

        int normalOrExpert;
        int numOfPlayers;

        String lobbyJoinCode = "";

        switch (createOrJoin) {
            case 1 -> {
                System.out.println("Choose between:");
                System.out.println(" [1] NORMAL");
                System.out.println(" [2] EXPERT");

                do {
                    System.out.print("\rInsert your choice [1 or 2] >>> ");
                    normalOrExpert = scanner.nextInt();
                } while ((normalOrExpert != 1) && (normalOrExpert != 2));

                do {
                    System.out.print("\rInsert number of players [2 or 3] >>> ");
                    numOfPlayers = scanner.nextInt();
                } while ((numOfPlayers != 2) && (numOfPlayers != 3));

                GameMode gameMode = normalOrExpert == 1 ? GameMode.NORMAL : GameMode.EXPERT;
                notifyListeners(new CreateGameEvent(gameMode, numOfPlayers));
            }

            case 2 -> {
                System.out.print("\rInsert lobby code >>> ");
                lobbyJoinCode = scanner.next();
                notifyListeners(new JoinGameEvent(lobbyJoinCode));
            }
        }
    }
}
