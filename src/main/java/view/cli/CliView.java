package view.cli;

import events.Event;
import view.View;

import java.util.Scanner;

public class CliView extends View {
    Scanner scanner;

    @Override
    public void run() {
        this.scanner = new Scanner(System.in);
        init();
    }

    @Override
    public void onEvent(Event event) {

    }

    private void init() {
        System.out.println("Eriantys #LOGO TODO");
        getServerInfo();
    }

    private void getServerInfo() {
        final String defaultPort = "12345";
        final String defaultAddress = "127.0.0.1";

        String insertedAddress = "";
        String insertedPort = "";

        boolean serverAddrIsValid = false;
        boolean serverPortIsValid = false;

        while (!serverAddrIsValid) {
            System.out.print("\rInsert server ADDRESS [localhost] >>> ");
            insertedAddress = scanner.nextLine();

            if (insertedAddress.isEmpty())
                insertedAddress = defaultAddress;
            //serverAddrIsValid = ClientController.validateIPV4(insertedAddress);

            if (!serverAddrIsValid) System.out.println("\rAddress is invalid try again!\r");
        }

        while(!serverPortIsValid) {
            System.out.print("\rInsert server PORT [" + defaultPort + "] >>> ");
            insertedPort = scanner.nextLine();

            if (insertedPort.isEmpty())
                insertedPort = defaultPort;
            //serverPortIsValid = ClientController.validateServerPort(insertedPort);

            if (!serverPortIsValid) System.out.println("\rPort is invalid try again!\r");
        }

        //notifyListeners(new ServerInformationInsertedEvent(insertedAddress, Integer.parseInt(insertedPort)));
    }
}
