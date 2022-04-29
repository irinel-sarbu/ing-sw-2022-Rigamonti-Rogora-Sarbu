package controller.server;

import eventSystem.EventManager;
import network.server.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ServerControllerTest {
    private static Server server;
    private static ServerController controller;

    @BeforeAll
    public static void setup() {
        server = new Server();
        controller = new ServerController(server);
        EventManager.register(controller, null);
    }

    @Test
    public static void clientConnectionTest() {

    }
}
