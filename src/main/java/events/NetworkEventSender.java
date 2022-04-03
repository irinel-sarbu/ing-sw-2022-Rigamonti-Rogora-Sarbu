package events;

import network.ClientConnection;
import util.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class NetworkEventSender {
    private final List<NetworkEventListener> listenerList = new ArrayList<>();

    public void registerListener(NetworkEventListener listener) {
        listenerList.add(listener);
    }

    public void registerListener(NetworkEventListener... listeners) {
        listenerList.addAll(Arrays.asList(listeners));
    }

    protected void notifyListeners(Tuple<Event, ClientConnection> networkEvent) {
        for (NetworkEventListener listener : listenerList)
            listener.onEvent(networkEvent);
    }
}
