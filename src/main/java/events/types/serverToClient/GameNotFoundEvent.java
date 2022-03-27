package events.types.serverToClient;

import events.Event;
import events.EventType;

public class GameNotFoundEvent extends Event {
    private final String code;

    public GameNotFoundEvent(String gameCode) {
        super(EventType.GAME_NOT_FOUND);
        this.code = gameCode;
    }

    public String getCode() {
        return code;
    }
}
