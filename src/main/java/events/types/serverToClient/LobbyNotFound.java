package events.types.serverToClient;

import events.Event;
import events.EventType;

public class LobbyNotFound extends Event {
    private final String code;

    public LobbyNotFound(String gameCode) {
        super(EventType.LOBBY_NOT_FOUND);
        this.code = gameCode;
    }

    public String getCode() {
        return code;
    }
}
