package events.types.clientToServer;

import events.Event;
import events.EventType;
import util.GameMode;

public class CreateGameEvent extends Event {
    private final int numOfPlayers;
    private final GameMode gameMode;

    public CreateGameEvent(GameMode gameMode, int numOfPlayers) {
        super(EventType.CREATE_GAME);
        this.gameMode = gameMode;
        this.numOfPlayers = numOfPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
