package events.types.serverToClient;

import events.Event;
import events.EventType;

public class ELobbyJoined extends Event {
    private final String code;

    public ELobbyJoined(String gameCode) {
        super(EventType.LOBBY_JOINED);
        this.code = gameCode;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "LobbyJoined { code: '" + code + "' }";
    }
}
