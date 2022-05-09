package controller.server.lobbyStates;

import controller.server.GameLobby;

public abstract class LobbyState {
    private final GameLobby lobby;
    protected boolean finished;

    public LobbyState(GameLobby gameLobby) {
        this.lobby = gameLobby;
        this.finished = false;
    }

    public boolean isFinished() {
        return finished;
    }
}
