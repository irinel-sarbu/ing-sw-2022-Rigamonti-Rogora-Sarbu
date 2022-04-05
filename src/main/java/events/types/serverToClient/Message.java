package events.types.serverToClient;

import events.Event;
import events.EventType;

public class Message extends Event {
    private final String message;

    public Message(String message) {
        super(EventType.MESSAGE);
        this.message = message;
    }

    public String getMsg() {
        return message;
    }

    @Override
    public String toString() {
        return "Message {  msg: '" + message + "' }";
    }
}
