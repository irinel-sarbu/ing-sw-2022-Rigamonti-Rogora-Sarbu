package events.types.serverToClient;

import events.Event;
import events.EventType;

public class PlayerNameTaken extends Event {
    private final String lobbyToJoin;

    public PlayerNameTaken(String lobbyToJoin) {
        super(EventType.PLAYER_NAME_TAKEN);
        this.lobbyToJoin = lobbyToJoin;
    }

    public String getLobbyToJoin() {
        return lobbyToJoin;
    }
}
