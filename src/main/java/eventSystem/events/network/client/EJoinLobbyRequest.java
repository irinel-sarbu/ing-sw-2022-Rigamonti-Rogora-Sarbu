package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;

public final class EJoinLobbyRequest extends NetworkEvent {
    private final String lobbyCode;

    public EJoinLobbyRequest(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }

    public String getLobbyCode() {
        return  lobbyCode;
    }

    @Override
    public String toString() {
        return "EJoinLobbyRequest { lobbyCode: '" + lobbyCode + "' }";
    }
}