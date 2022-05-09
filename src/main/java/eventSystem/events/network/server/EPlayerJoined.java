package eventSystem.events.network.server;

import eventSystem.events.Event;

public class EPlayerJoined extends Event {
    private final String playerName;
    private final String message;

    public EPlayerJoined(String playerName) {
        this.playerName = playerName;
        this.message = "";
    }

    public EPlayerJoined(String playerName, String message) {
        this.playerName = playerName;
        this.message = message;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "PlayerJoined { playerName: '" + playerName + (message != null ? ("', message: '" + message + "' }") : "' }");
    }
}
