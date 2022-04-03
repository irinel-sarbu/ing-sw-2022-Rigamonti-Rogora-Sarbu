package observer;

import events.Event;

import java.util.ArrayList;
import java.util.List;

public abstract class ViewObservable {
    private final List<ViewObserver> listenerList = new ArrayList<>();
    private final Object LOCK = new Object();

    public void registerListener(ViewObserver listener) {
        synchronized (LOCK) {
            listenerList.add(listener);
        }
    }

    public void removeListener(ViewObserver listener) {
        synchronized (LOCK) {
            listenerList.remove(listener);
        }
    }

    protected void notifyListeners(Event event) {
        List<ViewObserver> listenerListCopy;
        synchronized (LOCK) {
            listenerListCopy = new ArrayList<>(listenerList);
        }

        for (ViewObserver listener : listenerListCopy)
            listener.onViewEvent(event);
    }
}
