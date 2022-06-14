package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;

/**
 * Class that represents a server to client message.
 */
public class EDeclareWinner extends Event {
    private final String player;

    /**
     * Default constructor
     *
     * @param player winner
     */
    public EDeclareWinner(String player) {
        this.player = player;
    }

    /**
     * Getter
     *
     * @return winner's name
     */
    public String getPlayer() {
        return player;
    }

    public String toString() {
        return "EDeclareWinner { player " + getPlayer() + " is the winner }";
    }
}
