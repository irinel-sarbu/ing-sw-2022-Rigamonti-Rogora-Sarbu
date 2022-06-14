import controller.server.ServerController;
import eventSystem.EventManager;
import network.server.Server;
import util.Logger;

public class ServerApp {
    static public void main(String[] args) {
        boolean run = true;
        int port = 5000;
        for (String arg : args) {
            if (arg.matches("--port=\\d{1,5}")) {
                String[] split = arg.split("=");
                port = Integer.parseInt(split[1]);

                if (port > 49151 || port < 1024) {
                    System.out.println("Invalid TCP port. Out of range (1024 < x < 49151)");
                    run = false;
                }
            } else {
                System.out.println("Invalid argument sintax.");
                System.out.println("Syntax: executable [--port=yourPort]");

                run = false;
                break;
            }
        }

        if (run) {
            Server server = new Server(port);
            ServerController controller = new ServerController(server);
            EventManager.register(controller, null);

            Logger.setLevel(Logger.LoggerLevel.ALL);

            new Thread(server).start();
        }
    }
}
