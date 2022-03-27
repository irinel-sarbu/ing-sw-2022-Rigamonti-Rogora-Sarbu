package events.types.serverToClient;

import events.Event;
import events.EventType;

public class RegistrationOkEvent extends Event {
    public RegistrationOkEvent() {
        super(EventType.REGISTER_OK);
    }
}
