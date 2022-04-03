package events;

import network.server.ClientSocketConnection;
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
        if(event.handled || event.getType() != eventType)
            return;

        event.handled = handler.handle(event);
    }

    public synchronized void dispatch(EventType eventType, NetworkEventHandler handler) {
//        System.err.println("Dispatching event " + event + " with event type " + eventType);
//        System.err.println("\tEvent.handled " + event.handled);
//        System.err.println("\tEvent.type " + event.getType());
        if (event.handled || event.getType() != eventType)
            return;
//        System.err.println("Event " + eventType + " passed test, going to handler");
        event.handled = handler.handle(networkEvent);
    }
}