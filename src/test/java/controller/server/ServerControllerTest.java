package controller.server;

import network.client.Client;
import network.server.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerControllerTest {
    private static Server server;
    private static ServerController controller;
    private static Client client1;
    private static Client client2;

    @BeforeAll
    public static void setup() {
        server = new Server();
        controller = new ServerController(server);
    }

    @Test
    public static void clientConnectionTest() {
        client1 = new Client("localhost", 5000);
        client2 = new Client("localhost", 5000);

        assertTrue(client1 != null);
    }


}
