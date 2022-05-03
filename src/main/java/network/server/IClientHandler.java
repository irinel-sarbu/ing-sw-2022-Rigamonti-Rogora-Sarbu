package network.server;

import eventSystem.events.Event;

public interface IClientHandler {
    boolean isReady();

    void setReady();

    boolean isInLobby();

    void joinLobby(String lobbyCode);

    String getLobbyCode();

    void send(Event event);

    void closeConnection();
}
