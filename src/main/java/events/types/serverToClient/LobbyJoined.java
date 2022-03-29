package events.types.serverToClient;

import events.Event;
import events.EventType;

public class LobbyJoined extends Event {
    private final String code;

    public LobbyJoined(String gameCode) {
        super(EventType.LOBBY_JOINED);
        this.code = gameCode;
    }

    public String getCode() {
        return code;
    }
}
