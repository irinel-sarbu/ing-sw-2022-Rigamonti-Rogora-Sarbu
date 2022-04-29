package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;

public final class EJoinLobbyRequest extends NetworkEvent {
    private final String name;
    private final String lobbyCode;

    public EJoinLobbyRequest(String lobbyCode, String playerName) {
        this.lobbyCode = lobbyCode;
        this.name = playerName;
    }

    public String getPlayerName() {
        return name;
    }

    public String getLobbyCode() {
        return  lobbyCode;
    }

    @Override
    public String toString() {
        return "EJoinLobbyRequest { name: '" + name + "'; lobbyCode: '" + lobbyCode + "' }";
    }
}