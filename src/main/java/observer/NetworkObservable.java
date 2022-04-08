package observer;

import events.Event;
import network.server.ClientSocketConnection;
import util.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class NetworkObservable {
    private final List<NetworkObserver> listenerList = new ArrayList<>();
    private final Object LOCK = new Object();

    public synchronized void registerListener(NetworkObserver listener) {
        synchronized (LOCK) {
            listenerList.add(listener);
        }
    }

    public void removeListener(NetworkObserver listener) {
        synchronized (LOCK) {
            listenerList.remove(listener);
        }
    }

    protected synchronized void notifyListeners(Tuple<Event, ClientSocketConnection> event) {
        List<NetworkObserver> listenerListCopy;
        synchronized (LOCK) {
            listenerListCopy = new ArrayList<>(listenerList);
        }

        for (NetworkObserver listener : listenerListCopy)
            listener.onNetworkEvent(event);
    }
}
