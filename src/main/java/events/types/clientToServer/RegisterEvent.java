package events.types.clientToServer;

import events.Event;
import events.EventType;

public class RegisterEvent extends Event {
    private final String name;

    public RegisterEvent(String name) {
        super(EventType.REGISTER);
        this.name = name.toLowerCase().strip();
    }

    public String getName() {
        return name;
    }
}
