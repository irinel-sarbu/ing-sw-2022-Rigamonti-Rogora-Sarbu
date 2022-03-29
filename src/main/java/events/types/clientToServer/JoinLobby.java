package events.types.clientToServer;

import events.Event;
import events.EventType;

public class JoinLobby extends Event {
    private final String playerName;
    private final String lobbyCode;

    public JoinLobby(String playerName, String lobbyCode) {
        super(EventType.JOIN_LOBBY);
        this.playerName = playerName.strip();
        this.lobbyCode = lobbyCode;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getLobbyCode() {
        return  lobbyCode;
    }
}