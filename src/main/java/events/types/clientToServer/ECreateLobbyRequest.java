package events.types.clientToServer;

import events.Event;
import events.EventType;
import util.GameMode;

public class ECreateLobbyRequest extends Event {
    private final int numOfPlayers;
    private final GameMode gameMode;
    private final String playerName;

    public ECreateLobbyRequest(GameMode gameMode, int numOfPlayers, String playerName) {
        super(EventType.CREATE_LOBBY_REQUEST);
        this.gameMode = gameMode;
        this.numOfPlayers = numOfPlayers;
        this.playerName = playerName;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public String getPlayerName() {
        return playerName;
    }
}
