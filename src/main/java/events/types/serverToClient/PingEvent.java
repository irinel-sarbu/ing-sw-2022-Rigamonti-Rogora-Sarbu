package events.types.serverToClient;

import events.Event;
import events.EventType;

public class PingEvent extends Event {
    public PingEvent() {
        super(EventType.PING);
    }
}
