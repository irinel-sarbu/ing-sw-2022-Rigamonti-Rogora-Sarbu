package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import events.*;
import util.Logger;
import util.Tuple;

public class Server extends NetworkEventSender implements Runnable {

    private final Map<String, ClientConnection> clientList;
    private final LinkedBlockingQueue<Tuple<Event, ClientConnection>> eventQueue;
    private ServerSocket serverSocket;

    public Server() {
        clientList = new HashMap<>();
        eventQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(5000);
            Logger.info("Server started on port 5000.", "Waiting for clients...");
        } catch (IOException e) {
            Logger.severe(e.getMessage());
        }

        Thread incomingEventsDigestion = new Thread(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    notifyListeners(eventQueue.take());
                } catch (InterruptedException e) {
                    Logger.severe(e.getMessage());
                }
            }
        });
        incomingEventsDigestion.start();

        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientConnection clientConnection = new ClientConnection(this, clientSocket);
                clientConnection.start();

            } catch (IOException e) {
                Logger.severe(e.toString());
            }
        }
    }

    public synchronized void pushEvent(Tuple<Event, ClientConnection> networkEvent) {
        eventQueue.add(networkEvent);
    }

    public Map<String, ClientConnection> getClientList() {
        return clientList;
    }

    public void onDisconnect(ClientConnection clientConnection) {
        String name = getNameByClientConnection(clientConnection);

        Logger.info(name + " disconnected.");
    }

    public ClientConnection getClientConnectionByName(String client) {
        for (Entry<String, ClientConnection> entry : clientList.entrySet()) {
            if (entry.getKey().equals(client)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public String getNameByClientConnection(ClientConnection client) {
        for (Entry<String, ClientConnection> entry : clientList.entrySet()) {
            if (entry.getValue().equals(client)) {
                return entry.getKey();
            }
        }
        return null;
    }
}