package events;

public class EventDispatcher {
    private final Event event;

    public EventDispatcher(Event event) {
        this.event = event;
    }

    public void dispatch(EventType eventType, EventHandler handler) {
        if(event.handled && event.getType() != eventType)
            return;

        event.handled = handler.handle(event);
    }
}