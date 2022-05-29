package eventSystem.events.network.server;

import eventSystem.events.Event;

public class EPlayerDisconnected extends Event {
    private final String playerName;

    public EPlayerDisconnected(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String toString() {
        return "PlayerDisconnected { playerName: '" + playerName + "' }";
    }
}
