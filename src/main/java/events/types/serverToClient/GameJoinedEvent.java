package events.types.serverToClient;

import events.Event;
import events.EventType;

public class GameJoinedEvent extends Event {
    private final String code;

    public GameJoinedEvent(String gameCode) {
        super(EventType.GAME_JOINED);
        this.code = gameCode;
    }

    public String getCode() {
        return code;
    }
}
