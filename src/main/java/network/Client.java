package network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import events.Event;
import events.EventDispatcher;
import events.EventListener;
import events.EventType;
import events.types.network.ServerACKEvent;

public class Client extends Thread implements EventListener {
    private final Logger LOGGER = Logger.getLogger(Client.class.getName());

    private ServerConnection server;
    private List<Event> eventQueue;
    private Socket socket;

    private String IPAddress;
    private int port;

    public Client(String IPAddress, int port) {
        this.IPAddress = IPAddress;
        this.port = port;
        eventQueue = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            socket = new Socket(IPAddress, port);
            server = new ServerConnection(this, socket);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }

        server.start();
        Thread eventDigest = new Thread() {
            public void run() {
                while (!socket.isClosed()) {
                    onEvent(digestEvent());
                }
            }
        };
        eventDigest.start();
    }

    public synchronized void pushEvent(Event event) {
        eventQueue.add(event);
    }

    public synchronized Event digestEvent() {
        if (eventQueue.size() > 0)
            return eventQueue.get(0);
        return null;
    }

    @Override
    public synchronized void onEvent(Event event) {
        if (event == null)
            return;
        EventDispatcher dispatcher = new EventDispatcher(event);

        dispatcher.dispatch(EventType.SERVER_ACK, (Event e) -> onServerAck((ServerACKEvent) e));
    }

    public void send(Event obj) {
        server.write(obj);
    }

    // Handlers
    private boolean onServerAck(ServerACKEvent event) {
        LOGGER.info("New SERVER_ACK event: " + event.getMessage());
        return true;
    }

}