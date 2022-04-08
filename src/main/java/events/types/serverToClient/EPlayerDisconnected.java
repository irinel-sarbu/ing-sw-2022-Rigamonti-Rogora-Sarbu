package events.types.serverToClient;

import events.Event;
import events.EventType;

public class EPlayerDisconnected extends Event {
    private final String playerName;

    public EPlayerDisconnected(String playerName) {
        super(EventType.PLAYER_DISCONNECTED);
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String toString() {
        return "PlayerDisconnected { playerName: '" + playerName + " }";
    }
}
