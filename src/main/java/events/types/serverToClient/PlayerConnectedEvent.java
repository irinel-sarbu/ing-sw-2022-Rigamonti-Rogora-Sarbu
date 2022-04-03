package events.types.serverToClient;

import events.Event;
import events.EventType;

public class PlayerConnectedEvent extends Event {
    private final String playerName;
    private final String message;

    public PlayerConnectedEvent(String playerName) {
        super(EventType.PLAYER_CONNECTED);
        this.playerName = playerName;
        this.message = "";
    }

    public PlayerConnectedEvent(String playerName, String message) {
        super(EventType.PLAYER_CONNECTED);
        this.playerName = playerName;
        this.message = message;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getMessage() {
        return message;
    }
}
