package events.types.serverToClient;

import events.Event;
import events.EventType;

public class LobbyFull extends Event {
    private final String code;

    public LobbyFull(String gameCode) {
        super(EventType.LOBBY_FULL);
        this.code = gameCode;
    }

    public String getCode() {
        return code;
    }
}
