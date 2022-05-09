package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;
import util.GameState;

public class EUpdateGameState extends Event {
    private final GameState gameState;

    public EUpdateGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    @Override
    public String toString() {
        return "EUpdateGameState { Game State: " + getGameState() + " }";
    }
}
