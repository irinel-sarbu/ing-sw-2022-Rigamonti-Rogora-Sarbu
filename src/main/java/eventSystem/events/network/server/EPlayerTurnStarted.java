package eventSystem.events.network.server;

import eventSystem.events.Event;

/**
 * Class that represents a server to client message.
 */
public class EPlayerTurnStarted extends Event {
    private final String player;

    /**
     * Default constructor
     *
     * @param player whose turn started
     */
    public EPlayerTurnStarted(String player) {
        this.player = player;
    }

    /**
     * Getter
     *
     * @return player whose turn started
     */
    public String getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "EPlayerTurnStarted { " + getPlayer() + "'s turn started }";
    }
}
