package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;

/**
 * Class that represents a server to client message.
 */
public class ECheckLastRound extends Event {
    private final Boolean lastRound;

    /**
     * Default constructor
     *
     * @param lastRound True if is last round
     */
    public ECheckLastRound(Boolean lastRound) {
        this.lastRound = lastRound;
    }

    /**
     * Getter
     *
     * @return True if is last round
     */
    public Boolean getLastRound() {
        return lastRound;
    }

    @Override
    public String toString() {
        return "ECheckLastRound { lastRound=" + lastRound + " }";
    }
}
