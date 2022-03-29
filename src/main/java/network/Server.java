package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import controller.server.ServerController;
import events.*;
import util.Tuple;

public class Server implements Runnable {
    private final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private final ServerController controller;

    private final LinkedBlockingQueue<Tuple<Event, ClientConnection>> eventQueue;
    private ServerSocket serverSocket;

    public Server(ServerController serverController) {
        this.controller = serverController;
        this.eventQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(5000);
            LOGGER.info("Server started on port 5000.\nWaiting for clients...");
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }

        Thread incomingEventsDigestion = new Thread(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    controller.onEvent(eventQueue.take());
                } catch (InterruptedException e) {
                    LOGGER.severe(e.getMessage());
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
                LOGGER.severe(e.toString());
            }
        }
    }

    public synchronized void pushEvent(Tuple<Event, ClientConnection> networkEvent) {
        eventQueue.add(networkEvent);
    }
}