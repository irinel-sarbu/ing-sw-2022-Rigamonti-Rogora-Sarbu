package events.types.serverToClient;

import events.Event;
import events.EventType;

public class PlayerDisconnected extends Event {
    private final String playerName;

    public PlayerDisconnected(String playerName) {
        super(EventType.PLAYER_DISCONNECTED);
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
