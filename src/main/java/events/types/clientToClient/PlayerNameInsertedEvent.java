package events.types.clientToClient;

import events.Event;
import events.EventType;

public class PlayerNameInsertedEvent extends Event {
    private final String name;

    public PlayerNameInsertedEvent(String name) {
        super(EventType.PLAYER_NAME_INSERTED);
        this.name = name.strip();
    }

    public String getName() {
        return name;
    }
}
