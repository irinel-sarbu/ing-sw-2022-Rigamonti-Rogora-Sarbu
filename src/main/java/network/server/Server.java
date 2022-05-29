package network.server;

import eventSystem.EventManager;
import eventSystem.events.Event;
import util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Server implements Runnable {
    private final Map<String, ClientHandler> clientMap;

    private ServerSocket serverSocket;
    private final LinkedBlockingQueue<Event> eventQueue;

    private final int port;

    public Server(int port) {
        this.port = port;
        this.clientMap = new ConcurrentHashMap<>();
        this.eventQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            Logger.info("Server started on port " + port, "Waiting for clients...");
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
                ClientHandler clientConnection = new ClientHandler(this, clientSocket);
                clientConnection.start();
            } catch (IOException e) {
                Logger.error(e.getMessage());
            }
        }
    }

    public boolean checkPlayerIsRegistered(String nickname) {
        return clientMap.get(nickname.toLowerCase()) != null;
    }

    public void register(String nickname, ClientHandler client) {
        clientMap.put(nickname, client);
        client.setRegistered();
    }

    public void unregister(String nickname) {
        Logger.debug("Unregistering " + nickname);
        clientMap.remove(nickname);
    }

    public ClientHandler getClientByNickname(String nickname) {
        return clientMap.get(nickname.toLowerCase());
    }

    public String getClientNickname(ClientHandler clientHandler) {
        for (Map.Entry<String, ClientHandler> entry : clientMap.entrySet()) {
            if (entry.getValue().equals(clientHandler)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public synchronized void pushEvent(Event event) {
        eventQueue.add(event);
    }
}