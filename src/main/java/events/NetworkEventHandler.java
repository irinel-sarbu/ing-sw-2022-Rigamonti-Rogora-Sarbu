package events;

import network.ClientConnection;
import util.Tuple;

public interface NetworkEventHandler {
    boolean handle(Tuple<Event, ClientConnection> networkEvent);
}
