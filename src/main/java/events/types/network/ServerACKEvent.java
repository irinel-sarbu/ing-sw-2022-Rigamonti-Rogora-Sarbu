package events.types.network;

import events.Event;
import events.EventType;

public class ServerACKEvent extends Event {
    private final String msg;

    public ServerACKEvent(String msg) {
        super(EventType.SERVER_ACK);
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
