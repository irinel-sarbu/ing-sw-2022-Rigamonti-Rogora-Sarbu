package events;

import network.ClientConnection;
import util.Tuple;

public class EventDispatcher {
    private final Event event;
    private final Tuple<Event, ClientConnection> networkEvent;

    public EventDispatcher(Event event) {
        this.event = event;
        this.networkEvent = null;
    }

    public EventDispatcher(Tuple<Event, ClientConnection> networkEvent) {
        this.networkEvent = networkEvent;
        this.event = networkEvent.getKey();
    }

    public void dispatch(EventType eventType, EventHandler handler) {
        if(event.handled || event.getType() != eventType)
            return;

        event.handled = handler.handle(event);
    }

    public void dispatch(EventType eventType, NetworkEventHandler handler) {
        if (event.handled || event.getType() != eventType)
            return;

        event.handled = handler.handle(networkEvent);
    }
}