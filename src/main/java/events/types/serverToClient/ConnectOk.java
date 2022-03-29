package events.types.serverToClient;

import events.Event;
import events.EventType;

public class ConnectOk extends Event {
    public ConnectOk() {
        super(EventType.CONNECTION_OK);
    }
}
