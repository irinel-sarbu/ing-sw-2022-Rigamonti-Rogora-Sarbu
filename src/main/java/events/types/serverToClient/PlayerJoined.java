package events.types.serverToClient;

import events.Event;
import events.EventType;

public class PlayerJoined extends Event {
    private final String playerName;
    private final String message;

    public PlayerJoined(String playerName) {
        super(EventType.PLAYER_JOINED);
        this.playerName = playerName;
        this.message = "";
    }

    public PlayerJoined(String playerName, String message) {
        super(EventType.PLAYER_JOINED);
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
