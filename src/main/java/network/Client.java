package network;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import events.*;
import events.types.clientToClient.ConnectionRefused;
import events.types.serverToClient.ConnectOk;

public class Client extends EventSender implements Runnable {
    private final Logger LOGGER = Logger.getLogger(Client.class.getName());

    private final LinkedBlockingQueue<Event> eventQueue;

    private ServerConnection server;
    private Socket socket;

    private String IPAddress;
    private int port;

    public Client() {
        eventQueue = new LinkedBlockingQueue<>();
    }

    public void setAddressAndPort(String ip, int port) {
        this.IPAddress = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(IPAddress, port);
            server = new ServerConnection(this, socket);
        } catch (IOException e) {
            notifyListeners(new ConnectionRefused(e.getMessage()));
            return;
        }

        server.start();
        Thread incomingEventsDigestion = new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    notifyListeners(eventQueue.take());
                } catch (InterruptedException e) {
                    LOGGER.severe(e.getMessage());
                }
            }
        });
        incomingEventsDigestion.start();

        notifyListeners(new ConnectOk());
    }

    public synchronized void pushEvent(Event event) {
        eventQueue.add(event);
    }

    public void sendToServer(Event obj) {
        server.write(obj);
    }
}