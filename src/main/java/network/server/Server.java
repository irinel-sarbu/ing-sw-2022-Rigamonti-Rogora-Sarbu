package network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import events.*;
import observer.NetworkObservable;
import util.Logger;
import util.Tuple;

public class Server extends NetworkObservable implements Runnable {

    private final int PORT = 5000;
    private ServerSocket serverSocket;
    private final LinkedBlockingQueue<Tuple<Event, ClientSocketConnection>> eventQueue;

    public Server() {
        this.eventQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            Logger.info("Server started on port " + PORT + ".\n\tWaiting for clients...");
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }

        // Event digestion
        new Thread(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    Tuple<Event, ClientSocketConnection> networkEvent = eventQueue.take();
                    notifyListeners(networkEvent);
                } catch (InterruptedException e) {
                    Logger.error(e.getMessage());
                }
            }
        }).start();

        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientSocketConnection clientConnection = new ClientSocketConnection(this, clientSocket);
                clientConnection.start();

            } catch (IOException e) {
                Logger.error(e.getMessage());
            }
        }
    }

    public synchronized void pushEvent(Tuple<Event, ClientSocketConnection> networkEvent) {
        eventQueue.add(networkEvent);
    }
}
