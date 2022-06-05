package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;
import util.GameState;

/**
 * Class that represents a server to client message.
 */
public class EUpdateGameState extends Event {
    private final GameState gameState;

    /**
     * Default constructor
     *
     * @param gameState new game state
     */
    public EUpdateGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Getter
     *
     * @return game state
     */
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public String toString() {
        return "EUpdateGameState { Game State: " + getGameState() + " }";
    }
}
