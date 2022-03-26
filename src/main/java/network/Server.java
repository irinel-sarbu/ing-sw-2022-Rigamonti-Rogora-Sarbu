package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import controller.server.GameController;
import events.Event;
import events.EventDispatcher;
import events.EventListener;
import events.EventType;
import events.NetworkEventListener;
import events.types.clientToServer.RegisterEvent;
import util.Tuple;

public class Server extends Thread implements NetworkEventListener {
	private final Logger LOGGER = Logger.getLogger(Server.class.getName());

	private final GameController controller;
	private final Map<String, ClientConnection> clientList;
	private final List<Tuple<Event, ClientConnection>> eventQueue;
	private ServerSocket serverSocket;

	public Server(GameController controller) {
		this.controller = controller;
		clientList = new HashMap<>();
		eventQueue = new ArrayList<>();
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(5000);
			LOGGER.info("Server started on port 5000.\nWaiting for clients...");
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
		}

		Thread eventDigest = new Thread() {
			public void run() {
				while (!serverSocket.isClosed()) {
					onEvent(digestEvent());
				}
			}
		};
		eventDigest.start();

		while (!serverSocket.isClosed()) {
			try {
				Socket clientSocket = serverSocket.accept();
				ClientConnection clientConnection = new ClientConnection(this, clientSocket);
				clientConnection.start();

			} catch (IOException e) {
				LOGGER.severe(e.toString());
			}
		}
	}

	public synchronized void pushEvent(Tuple<Event, ClientConnection> networkEvent) {
		eventQueue.add(networkEvent);
	}

	public synchronized Tuple<Event, ClientConnection> digestEvent() {
		if (eventQueue.size() > 0)
			return eventQueue.get(0);
		return null;
	}

	@Override
	public synchronized void onEvent(Tuple<Event, ClientConnection> networkEvent) {
		if (networkEvent == null)
			return;
		EventDispatcher dispatcher = new EventDispatcher(networkEvent);

		dispatcher.dispatch(EventType.REGISTER, (Tuple<Event, ClientConnection> t) -> onRegister((RegisterEvent) t.getKey(), t.getValue()));
	}

	public void onDisconnect(ClientConnection clientConnection) {
		String name = getNameByClientCOnnection(clientConnection);

		LOGGER.info(name + " disconnected.");
	}

	public String getNameByClientCOnnection(ClientConnection client) {
		for (Entry<String, ClientConnection> entry : clientList.entrySet()) {
			if (entry.getValue().equals(client)) {
				return entry.getKey();
			}
		}
		return null;
	}

	// Handlers
	private boolean onRegister(RegisterEvent event, ClientConnection client) {
		LOGGER.info("Registering: " + event.getName());
		clientList.put(event.getName(), client);
		return true;
	}
}