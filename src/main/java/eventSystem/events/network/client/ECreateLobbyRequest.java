package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;
import util.GameMode;

public final class ECreateLobbyRequest extends NetworkEvent {
    private final int numOfPlayers;
    private final GameMode gameMode;
    private final String playerName;

    public ECreateLobbyRequest(GameMode gameMode, int numOfPlayers, String playerName) {
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

    @Override
    public String toString() {
        return "ECreateLobbyRequest { numOfPlayers: " + numOfPlayers + "; gameMode: " + gameMode + "; playerName: '" + playerName + "' }";
    }
}
