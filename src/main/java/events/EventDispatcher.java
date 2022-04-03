package events;

import network.server.ClientSocketConnection;
import util.Logger;
import util.Tuple;

public class EventDispatcher {
    private final Event event;
    private final Tuple<Event, ClientSocketConnection> networkEvent;

    public EventDispatcher(Event event) {
        this.event = event;
        this.networkEvent = null;
    }

    public EventDispatcher(Tuple<Event, ClientSocketConnection> networkEvent) {
        this.networkEvent = networkEvent;
        this.event = networkEvent.getKey();
    }

    public synchronized void dispatch(EventType eventType, EventHandler handler) {
        if (event.handled || event.getType() != eventType)
            return;

        event.handled = handler.handle(event);
    }

    public synchronized void dispatch(EventType eventType, NetworkEventHandler handler) {
        Logger.debug("Dispatching event " + event + " with event type " + eventType + "\n" +
                "\tEvent.handled " + event.handled + "\n" +
                "\tEvent.type " + event.getType());
        if (event.handled || event.getType() != eventType)
            return;

        event.handled = handler.handle(networkEvent);
    }
}