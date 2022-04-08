package observer;

import events.Event;

public interface Observer {
    void onEvent(Event event);
}
