package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;
import util.GameMode;

public final class ECreateLobbyRequest extends NetworkEvent {
    private final int numOfPlayers;
    private final GameMode gameMode;

    public ECreateLobbyRequest(GameMode gameMode, int numOfPlayers) {
        this.gameMode = gameMode;
        this.numOfPlayers = numOfPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public String toString() {
        return "ECreateLobbyRequest { numOfPlayers: " + numOfPlayers + "; gameMode: " + gameMode + "' }";
    }
}
