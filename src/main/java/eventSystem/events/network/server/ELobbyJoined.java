package eventSystem.events.network.server;

import eventSystem.events.Event;

/**
 * Class that represents a server to client message.
 */
public class ELobbyJoined extends Event {
    private final String code;

    /**
     * Default constructor
     *
     * @param gameCode code of lobby joined - ACK
     */
    public ELobbyJoined(String gameCode) {
        this.code = gameCode;
    }

    /**
     * Getter
     *
     * @return lobby code
     */
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "LobbyJoined { code: '" + code + "' }";
    }
}
