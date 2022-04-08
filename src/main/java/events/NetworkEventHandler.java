package events;

import network.server.ClientSocketConnection;
import util.Tuple;

public interface NetworkEventHandler {
    boolean handle(Tuple<Event, ClientSocketConnection> networkEvent);
}
