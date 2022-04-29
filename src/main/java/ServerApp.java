import controller.server.ServerController;
import eventSystem.EventManager;
import network.server.Server;
import util.Logger;

public class ServerApp {
	static public void main(String[] args) {
		Server server = new Server();
		ServerController controller = new ServerController(server);
		EventManager.get().register(controller, null);

		Logger.setLevel(Logger.LoggerLevel.ALL);

		new Thread(server).start();
	}
}
