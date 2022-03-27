import controller.server.GameController;
import network.Server;

public class ServerApp {
	static public void main(String[] args) {
		Server server = new Server();
		GameController controller = new GameController(server);

		server.registerListener(controller);
		new Thread(server).start();
	}
}
