package events.types.serverToClient;

import events.Event;
import events.EventType;

public class Ping extends Event {
    public Ping() {
        super(EventType.PING);
    }

    @Override
    public String toString() {
        return "Ping";
    }
}
