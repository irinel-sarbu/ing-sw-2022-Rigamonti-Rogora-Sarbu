package view.cli;

import events.Event;
import events.types.clientToClient.ConnectEvent;
import events.types.clientToClient.PlayerNameInsertedEvent;
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
    public void onEvent(Event event) {
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
        } while(insertedName.isEmpty());

        notifyListeners(new PlayerNameInsertedEvent(insertedName));
    }
}
