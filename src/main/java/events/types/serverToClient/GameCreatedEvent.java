package events.types.serverToClient;

import events.Event;
import events.EventType;
import util.GameMode;

public class GameCreatedEvent extends Event {
    private final String code;

    public GameCreatedEvent(String gameCode) {
        super(EventType.GAME_CREATED);
        this.code = gameCode;
    }

    public String getCode() {
        return code;
    }
}
