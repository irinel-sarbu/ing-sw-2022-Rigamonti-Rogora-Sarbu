package network.client;

import eventSystem.EventManager;
import eventSystem.events.Event;
import eventSystem.events.network.Messages;
import eventSystem.events.network.NetworkEvent;
import eventSystem.events.network.server.ServerMessage;
import util.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

public class Client implements Runnable {
    private final LinkedBlockingQueue<Event> eventQueue;

    private UUID cliendIdentifier;
    private String lobbyId;

    private ServerConnection server;
    private Socket socket;

    private final String IPAddress;
    private final int port;

    public Client(String ip, int port) {
        this.IPAddress = ip;
        this.port = port;
        eventQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        try {
            socket = new Socket(IPAddress, port);
            server = new ServerConnection(this, socket);
        } catch (IOException e) {
            EventManager.notify(new ServerMessage(Messages.CONNECTION_REFUSED));
            return;
        }

        server.start();

        // Event digestion
        new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    Event event = eventQueue.take();
                    EventManager.notify(event);
                } catch (InterruptedException e) {
                    Logger.error(e.getMessage());
                }
            }
        }).start();
    }

    public synchronized void pushEvent(Event event) {
        eventQueue.add(event);
    }

    public void sendToServer(NetworkEvent event) {
        event.setClientId(cliendIdentifier);
        if (lobbyId != null) {
            event.setScope(lobbyId);
        }

        server.send(event);
    }

    public void setClientIdentifier(UUID identifier) {
        this.cliendIdentifier = identifier;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }
}