package eventSystem.events.network.server;

import eventSystem.events.Event;

/**
 * Class that represents a server to client message.
 */
public class EPlayerDisconnected extends Event {
    private final String playerName;

    /**
     * Default constructor
     *
     * @param playerName that disconnected
     */
    public EPlayerDisconnected(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Getter
     *
     * @return name of player that disconnected
     */
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String toString() {
        return "PlayerDisconnected { playerName: '" + playerName + "' }";
    }
}
