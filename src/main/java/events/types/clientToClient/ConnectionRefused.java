package events.types.clientToClient;

import events.Event;
import events.EventType;

public class ConnectionRefused extends Event {
    private final String msg;

    public ConnectionRefused(String msg) {
        super(EventType.CONNECTION_REFUSED);
        this.msg = msg.strip();
    }

    public String getMessage() {
        return msg;
    }
}
