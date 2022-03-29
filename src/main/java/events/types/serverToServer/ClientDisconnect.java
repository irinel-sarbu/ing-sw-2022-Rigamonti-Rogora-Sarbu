package events.types.serverToServer;

import events.Event;
import events.EventType;

public class ClientDisconnect extends Event {
    public ClientDisconnect() {
        super(EventType.CLIENT_DISCONNECT);
    }
}
