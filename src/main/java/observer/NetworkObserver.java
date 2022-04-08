package observer;

import events.Event;
import network.server.ClientSocketConnection;
import util.Tuple;

public interface NetworkObserver {
    void onNetworkEvent(Tuple<Event, ClientSocketConnection> event);
}
