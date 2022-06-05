package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;
import util.GameMode;

/**
 * Class that represents a client to server message.
 */
public final class ECreateLobbyRequest extends NetworkEvent {
    private final int numOfPlayers;
    private final GameMode gameMode;

    /**
     * Default constructor
     *
     * @param gameMode     new lobby GameMode
     * @param numOfPlayers max num of players
     */
    public ECreateLobbyRequest(GameMode gameMode, int numOfPlayers) {
        this.gameMode = gameMode;
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * Getter
     *
     * @return max num of players
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * Getter
     *
     * @return new lobby game mode
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public String toString() {
        return "ECreateLobbyRequest { numOfPlayers: " + numOfPlayers + "; gameMode: " + gameMode + "' }";
    }
}
