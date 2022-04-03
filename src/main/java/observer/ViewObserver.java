package observer;

import events.Event;

public interface ViewObserver {
    void onViewEvent(Event event);
}
