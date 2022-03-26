package events;

import network.ClientConnection;
import util.Tuple;

public interface NetworkEventListener {
    void onEvent(Tuple<Event, ClientConnection> event);
}
