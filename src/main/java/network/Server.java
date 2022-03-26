package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import events.Event;
import events.EventDispatcher;
import events.EventListener;
import events.EventType;
import events.types.network.ClientToServerInfoEvent;
import events.types.network.ServerACKEvent;

public class Server implements EventListener {
	private final Logger LOGGER = Logger.getLogger(Server.class.getName());

	private ArrayList<ClientConnection> clientList;
	private LinkedBlockingQueue<Event> messages;
	private ServerSocket serverSocket;

	public Server(int port) throws IOException {
		clientList = new ArrayList<ClientConnection>();
		messages = new LinkedBlockingQueue<>();
		serverSocket = new ServerSocket(port);

		Thread accept = new Thread() {
			public void run() {
				LOGGER.info("Waiting for clients to connect...");
				while (true) {
					try {
						Socket s = serverSocket.accept();
						clientList.add(new ClientConnection(s));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		accept.start();

		Thread messageHandling = new Thread() {
			public void run() {
				while (true) {
					try {
						onEvent(messages.take());
					} catch (InterruptedException e) {
						LOGGER.info("Connection interrupted...");
					}
				}
			}
		};
		messageHandling.start();

	}

	@Override
	public void onEvent(Event event) {
		EventDispatcher dispatcher = new EventDispatcher(event);
		dispatcher.dispatch(EventType.NETWORK_MESSAGE, (Event e) -> onClientInfo((ClientToServerInfoEvent) e));
	}

	private class ClientConnection {
		ObjectInputStream in;
		ObjectOutputStream out;
		Socket socket;

		ClientConnection(Socket socket) throws IOException {
			LOGGER.info("New client connected!");
			this.socket = socket;
			in = new ObjectInputStream(socket.getInputStream());

			Thread read = new Thread() {
				public void run() {
					while (true) {
						try {
							messages.put((Event) in.readObject());
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};

			read.setDaemon(true);
			read.start();

			out = new ObjectOutputStream(socket.getOutputStream());
		}

		public void write(Event obj) {
			try {
				out.writeObject(obj);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendToOne(int index, Event message) throws IndexOutOfBoundsException {
		clientList.get(index).write(message);
	}

	public void sendToAll(Event message) {
		for (ClientConnection client : clientList)
			client.write(message);
	}

	// Handlers
	private boolean onClientInfo(ClientToServerInfoEvent event) {
		LOGGER.info("New CLIENT_TO_SERVER_INFO event: " + event.getMessage());
		LOGGER.info(">\tSending ACK to new Client...");
		sendToAll(new ServerACKEvent("Sup bro! Welcome to the the server!"));
		return true;
	}
}
