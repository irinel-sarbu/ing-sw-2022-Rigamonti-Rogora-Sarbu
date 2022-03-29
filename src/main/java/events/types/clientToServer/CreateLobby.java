package events.types.clientToServer;

import events.Event;
import events.EventType;
import util.GameMode;

public class CreateLobby extends Event {
    private final int numOfPlayers;
    private final GameMode gameMode;
    private final String creatorName;

    public CreateLobby(GameMode gameMode, int numOfPlayers, String creatorName) {
        super(EventType.CREATE_LOBBY);
        this.gameMode = gameMode;
        this.numOfPlayers = numOfPlayers;
        this.creatorName = creatorName;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public String getCreatorName() {
        return creatorName;
    }
}
