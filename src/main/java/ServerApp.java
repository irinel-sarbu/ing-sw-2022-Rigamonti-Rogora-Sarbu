import controller.server.ServerController;
import network.Server;

public class ServerApp {
	static public void main(String[] args) {
		ServerController controller = new ServerController();
		Server server = new Server(controller);

		new Thread(server).start();
	}
}
