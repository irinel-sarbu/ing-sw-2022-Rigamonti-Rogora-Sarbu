package events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class EventSender {
    private final List<EventListener> listenerList = new ArrayList<>();

    public void registerListener(EventListener listener) {
        listenerList.add(listener);
    }

    public void registerListener(EventListener... listeners) {
        listenerList.addAll(Arrays.asList(listeners));
    }

    protected void notifyListeners(Event event) {
        for (EventListener listener : listenerList)
            listener.onEvent(event);
    }
}
