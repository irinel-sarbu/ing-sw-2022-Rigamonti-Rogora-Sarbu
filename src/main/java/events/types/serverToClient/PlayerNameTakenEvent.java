package events.types.serverToClient;

import events.Event;
import events.EventType;

public class PlayerNameTakenEvent extends Event {
    public PlayerNameTakenEvent() {
        super(EventType.PLAYER_NAME_TAKEN);
    }
}
