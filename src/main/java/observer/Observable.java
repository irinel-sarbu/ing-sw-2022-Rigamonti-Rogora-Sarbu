package observer;

import events.Event;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    private final List<Observer> listenerList = new ArrayList<>();
    private final Object LOCK = new Object();

    public synchronized void registerListener(Observer listener) {
        synchronized (LOCK) {
            listenerList.add(listener);
        }
    }

    public synchronized void removeListener(Observer listener) {
        synchronized (LOCK) {
            listenerList.remove(listener);
        }
    }

    protected synchronized void notifyListeners(Event event) {
        List<Observer> listenerListCopy;
        synchronized (LOCK){
            listenerListCopy = new ArrayList<>(listenerList);
        }

        for (Observer listener : listenerListCopy)
            listener.onEvent(event);
    }
}
