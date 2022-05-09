package eventSystem.events.network.server;

import eventSystem.events.Event;

public class ELobbyJoined extends Event {
    private final String code;

    public ELobbyJoined(String gameCode) {
        this.code = gameCode;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "LobbyJoined { code: '" + code + "' }";
    }
}
