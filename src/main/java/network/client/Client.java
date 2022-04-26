package network.client;

import eventSystem.EventManager;
import events.Event;
import events.types.Messages;
import events.types.serverToClient.Message;
import observer.Observable;
import util.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Client extends Observable implements Runnable {

    private final LinkedBlockingQueue<Event> eventQueue;

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
            notifyListeners(new Message(Messages.CONNECTION_REFUSED));
            EventManager.notify(new Message(Messages.CONNECTION_REFUSED));
            return;
        }

        server.start();

        notifyListeners(new Message(Messages.CONNECTION_OK));
        EventManager.notify(new Message(Messages.CONNECTION_OK));

        // Event digestion
        new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    Event event = eventQueue.take();
                    notifyListeners(event);
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

    public void sendToServer(Event obj) {
        server.send(obj);
    }
}