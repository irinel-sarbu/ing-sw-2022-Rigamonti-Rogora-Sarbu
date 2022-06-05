package eventSystem.events.network.server;

import eventSystem.events.Event;

public class EPlayerJoined extends Event {
    private final String playerName;

    public EPlayerJoined(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String toString() {
        return "PlayerJoined { playerName: '" + playerName + "' }";
    }
}
