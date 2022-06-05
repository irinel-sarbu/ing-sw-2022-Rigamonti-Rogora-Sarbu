package controller.server.lobbyStates;

import controller.server.GameLobby;

public class PreGameState extends LobbyState {

    /**
     * Keeps old GameLobby state
     * @param gameLobby
     */
    public PreGameState(GameLobby gameLobby) {
        super(gameLobby);
    }
}
