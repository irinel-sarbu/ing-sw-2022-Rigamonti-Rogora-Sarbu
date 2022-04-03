package events.types.serverToClient;

import events.Event;
import events.EventType;

public class ConnectOkEvent extends Event {
    public ConnectOkEvent() {
        super(EventType.CONNECTION_OK);
    }
}
