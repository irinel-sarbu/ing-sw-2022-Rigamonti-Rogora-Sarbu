package network;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import events.*;
import events.types.clientToClient.ConnectionRefusedEvent;
import events.types.serverToClient.ConnectOkEvent;
import util.Logger;

public class Client extends EventSender implements Runnable {

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
            notifyListeners(new ConnectionRefusedEvent(e.getMessage()));
            return;
        }

        server.start();
        Thread incomingEventsDigestion = new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    notifyListeners(eventQueue.take());
                } catch (InterruptedException e) {
                    Logger.severe(e.getMessage());
                }
            }
        });
        incomingEventsDigestion.start();

        notifyListeners(new ConnectOkEvent());
    }

    public synchronized void pushEvent(Event event) {
        eventQueue.add(event);
    }

    public void send(Event obj) {
        server.write(obj);
    }
}