import java.io.IOException;

import controller.server.GameController;
import network.Server;

public class ServerApp {

	static public void main(String[] args) throws IOException {
		GameController controller = new GameController();
		Server server = new Server(controller);
		server.start();
	}
}
