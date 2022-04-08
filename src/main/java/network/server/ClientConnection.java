package network.server;

import events.Event;

public interface ClientConnection {
    void closeConnection();
    void asyncSend(Event event);
}
