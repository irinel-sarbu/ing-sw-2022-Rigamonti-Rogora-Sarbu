import java.io.IOException;

import controller.ServerController;
import model.GameModel;
import network.Server;

public class ServerApp {

	static public void main(String[] args) throws IOException {
		Server server = new Server(5000);
		// GameModel model = new GameModel();
		// ServerController controller = new ServerController(model, server);
		// server.registerListener(controller);
		// model.registerListener(server);

	}
}
