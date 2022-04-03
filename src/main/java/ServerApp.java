import controller.server.ServerController;
import network.server.Server;

public class ServerApp {
	static public void main(String[] args) {
		Server server = new Server();
		ServerController controller = new ServerController(server);
		server.registerListener(controller);

		new Thread(server).start();
	}
}
