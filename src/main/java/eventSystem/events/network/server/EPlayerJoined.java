package eventSystem.events.network.server;

import eventSystem.events.Event;

/**
 * Class that represents a server to client message.
 */
public class EPlayerJoined extends Event {
    private final String playerName;

    /**
     * Default constructor
     *
     * @param playerName that joined lobby
     */
    public EPlayerJoined(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Getter
     *
     * @return name of player that joined lobby
     */
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String toString() {
        return "PlayerJoined { playerName: '" + playerName + "' }";
    }
}
