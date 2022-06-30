import controller.client.ClientController;
import controller.server.ServerController;
import eventSystem.EventManager;
import javafx.application.Application;
import network.server.Server;
import util.Logger;
import view.View;
import view.cli.CliView;
import view.gui.GuiApplication;

public class App {
    static public void main(String[] args) {
        Logger.setLevel(Logger.LoggerLevel.DISABLED);

        boolean run = true;

        boolean client = true;
        boolean cliEnabled = false;

        int serverPort = 5000;

        if (args.length > 0) {
            // Parse arguments
            switch (args.length) {
                case 1 -> {
                    switch (args[0]) {
                        case "--cli", "-c" -> cliEnabled = true;
                        case "--server", "-s" -> client = false;
                    }
                }
                case 2 -> {
                    if ((args[0].equals("--server") || args[0].equals("-s")) && args[1].matches("--port=\\d{1,5}")) {
                        client = false;
                        String[] split = args[1].split("=");
                        serverPort = Integer.parseInt(split[1]);

                        if (serverPort > 49151 || serverPort < 1024) {
                            System.out.println("Invalid TCP port. Out of range (1024 < x < 49151)");
                            run = false;
                        }
                    } else {
                        System.out.println("Invalid argument syntax.");
                        run = false;
                    }
                }
                default -> {
                    System.out.println("Number of arguments must be 1 or 2.");
                    run = false;
                }
            }
        }

        if (run) {
            if (client) {
                if (cliEnabled) {
                    View view = new CliView();
                    ClientController controller = new ClientController(view);
                    view.run();
                } else {
                    Logger.setLevel(Logger.LoggerLevel.INFO);
                    Application.launch(GuiApplication.class);
                }
            } else {
                Server server = new Server(serverPort);
                ServerController controller = new ServerController(server);
                EventManager.register(controller, null);

                Logger.setLevel(Logger.LoggerLevel.ALL);

                new Thread(server).start();
            }
        }
    }
}
