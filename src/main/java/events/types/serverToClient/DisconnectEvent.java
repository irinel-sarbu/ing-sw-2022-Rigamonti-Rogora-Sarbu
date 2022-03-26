package events.types.serverToClient;

import events.Event;
import events.EventType;

public class DisconnectEvent extends Event {
    private final String name;

    public DisconnectEvent(String name) {
        super(EventType.DISCONNECT);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
