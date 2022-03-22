package events.types;

import events.Event;
import events.EventType;

public class SimpleMessageEvent extends Event {
    private final String message;

    public SimpleMessageEvent(String message) {
        super(EventType.SIMPLE_MESSAGE);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
