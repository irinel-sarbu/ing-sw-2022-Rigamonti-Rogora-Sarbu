package events.types.serverToClient;

import events.Event;
import events.EventType;

public class LobbyCreated extends Event {
    private final String code;

    public LobbyCreated(String gameCode) {
        super(EventType.LOBBY_CREATED);
        this.code = gameCode;
    }

    public String getCode() {
        return code;
    }
}
