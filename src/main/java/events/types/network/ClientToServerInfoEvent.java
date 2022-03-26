package events.types.network;

import events.Event;
import events.EventType;

public class ClientToServerInfoEvent extends Event {
    private final String msg;

    public ClientToServerInfoEvent(String msg) {
        super(EventType.NETWORK_MESSAGE);
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
