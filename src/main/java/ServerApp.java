import controller.server.ServerController;
import network.server.Server;
import util.Logger;

public class ServerApp {
	static public void main(String[] args) {
		Server server = new Server();
		ServerController controller = new ServerController(server);
		server.registerListener(controller);

		Logger.setLevel(Logger.LoggerLevel.INFO);

		new Thread(server).start();
	}
}
