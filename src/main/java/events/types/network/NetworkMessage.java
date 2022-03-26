package events.types.network;

import events.Event;
import events.EventType;

public class NetworkMessage extends Event {
    private final String msg;

    public NetworkMessage(String msg) {
        super(EventType.NETWORK_MESSAGE);
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
