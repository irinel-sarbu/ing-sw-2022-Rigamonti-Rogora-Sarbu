package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;

/**
 * Class that represents a client to server message.
 */
public final class EJoinLobbyRequest extends NetworkEvent {
    private final String lobbyCode;

    /**
     * Default constructor
     *
     * @param lobbyCode code of lobby to join
     */
    public EJoinLobbyRequest(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }

    /**
     * Getter
     *
     * @return lobby code
     */
    public String getLobbyCode() {
        return lobbyCode;
    }

    @Override
    public String toString() {
        return "EJoinLobbyRequest { lobbyCode: '" + lobbyCode + "' }";
    }
}