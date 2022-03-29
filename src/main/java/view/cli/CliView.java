package view.cli;

import events.types.clientToClient.ConnectEvent;
import events.types.clientToServer.CreateLobby;
import events.types.clientToServer.JoinLobby;
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
        final int defaultPort = 5000;
        final String defaultAddress = "localhost";

        String insertedAddress;
        String insertedPort;

        System.out.print("\rInsert server ADDRESS [localhost] >>> ");
        insertedAddress = scanner.nextLine();

        System.out.print("\rInsert server PORT [" + defaultPort + "] >>> ");
        insertedPort = scanner.nextLine();

        String eventIP = insertedAddress.isEmpty() ? defaultAddress : insertedAddress;
        int eventPort = insertedPort.isEmpty() ? defaultPort : Integer.parseInt(insertedPort);

        notifyListeners(new ConnectEvent(eventIP, eventPort));
    }

    private String getPlayerName() {
        String insertedName;

        do {
            System.out.print("\rInsert your name >>> ");
            insertedName = scanner.nextLine();
        } while (insertedName.isEmpty());

        return insertedName;
    }

    @Override
    public void getPlayerName(String lobbyToJoin) {
        notifyListeners(new JoinLobby(getPlayerName(), lobbyToJoin));
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

        String lobbyCode;

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

                notifyListeners(new CreateLobby(gameMode, numOfPlayers, getPlayerName()));
            }

            case 2 -> {
                System.out.print("\rInsert lobby code >>> ");
                lobbyCode = scanner.next();

                notifyListeners(new JoinLobby(getPlayerName(), lobbyCode));
            }
        }
    }
}
