package network.server;

import eventSystem.EventManager;
import eventSystem.events.Event;
import util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Server implements Runnable {
    private final Map<UUID, ClientSocketConnection> clientMap;

    private final int PORT = 5000;
    private ServerSocket serverSocket;
    private final LinkedBlockingQueue<Event> eventQueue;

    public Server() {
        this.clientMap = new ConcurrentHashMap<>();
        this.eventQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            Logger.info("Server started on port " + PORT, "Waiting for clients...");
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }

        // Event digestion
        new Thread(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    Event networkEvent = eventQueue.take();
                    EventManager.notify(networkEvent);
                } catch (InterruptedException e) {
                    Logger.error(e.getMessage());
                }
            }
        }).start();

        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();

                UUID newClientUUID = UUID.randomUUID();
                ClientSocketConnection clientConnection = new ClientSocketConnection(this, clientSocket, newClientUUID);
                clientConnection.start();

                clientMap.put(newClientUUID, clientConnection);
            } catch (IOException e) {
                Logger.error(e.getMessage());
            }
        }
    }

    public ClientSocketConnection getClientById(UUID clientId) {
        return clientMap.get(clientId);
    }

    public synchronized void pushEvent(Event event) {
        eventQueue.add(event);
    }
}