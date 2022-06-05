package controller.server.lobbyStates;

import controller.server.GameLobby;

public abstract class LobbyState {
    private final GameLobby lobby;
    protected boolean finished;

    /**
     * Class constructor
     * @param gameLobby
     */
    public LobbyState(GameLobby gameLobby) {
        this.lobby = gameLobby;
        this.finished = false;
    }

    /**
     * Get lobby status
     * @return {@link true} if the game inside the lobby finished, {@link false} otherwise
     */
    public boolean isFinished() {
        return finished;
    }
}
