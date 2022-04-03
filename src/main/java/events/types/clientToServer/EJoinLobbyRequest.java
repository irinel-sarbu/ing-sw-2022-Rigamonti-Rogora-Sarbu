package events.types.clientToServer;

import events.Event;
import events.EventType;

public class EJoinLobbyRequest extends Event {
    private final String name;
    private final String lobbyCode;

    public EJoinLobbyRequest(String lobbyCode, String playerName) {
        super(EventType.JOIN_LOBBY_REQUEST);
        this.lobbyCode = lobbyCode;
        this.name = playerName;
    }

    public String getPlayerName() {
        return name;
    }

    public String getLobbyCode() {
        return  lobbyCode;
    }
}