package controller.server;

import events.Event;
import events.types.serverToClient.*;
import network.ClientConnection;
import util.GameMode;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Lobby {
    private final Logger LOGGER = Logger.getLogger(Lobby.class.getName());

    private final ServerController controller;
    private final String lobbyCode;
    private final int maxPlayers;
    private final GameMode gameMode;
    private final Map<String, ClientConnection> clientList;

    public Lobby(ServerController controller, String code, int maxPlayers, GameMode gameMode) {
        this.controller = controller;
        this.lobbyCode = code;
        this.maxPlayers = maxPlayers;
        this.gameMode = gameMode;

        this.clientList = new HashMap<>();
    }

    /**
     * Broadcast Event to all clients connected to lobby
     * @param event Event to broadcast
     */
    public void broadcast(Event event) {
        for (Map.Entry<String, ClientConnection> entry : clientList.entrySet()) {
            ClientConnection client = entry.getValue();
            client.send(event);
        }
    }

    /**
     * Broadcast Event to all clients connected to lobby, except sender
     * @param event Event to broadcast
     * @param excludedClient Client to exclude
     */
    public void broadcastExceptOne(Event event, String excludedClient) {
        for (Map.Entry<String, ClientConnection> entry : clientList.entrySet()) {
            if(!entry.getKey().equals(excludedClient)) {
                ClientConnection client = entry.getValue();
                client.send(event);
            }
        }
    }

    public String getLobbyCode() {
        return lobbyCode;
    }

    public void addClientToLobby(String name, ClientConnection client) {
        if(clientList.size() > maxPlayers) {
            client.send(new LobbyFull(lobbyCode));
            LOGGER.info("Player " + name + " trying to connect but lobby is full.");
            return;
        }

        if(getClientByName(name) != null) {
            client.send(new PlayerNameTaken(lobbyCode));
            LOGGER.info("Player " + name + " trying to connect but lobby there is already a player with that name connected.");
            return;
        }

        clientList.put(name, client);
        client.joinLobby(lobbyCode);
        client.send(new LobbyJoined(lobbyCode));
        broadcastExceptOne(new PlayerJoined(name), name);

        // TODO implement start game if lobby is full
    }

    public ClientConnection getClientByName(String name) {
        return clientList.get(name);
    }

    public String getClientBySocket(ClientConnection clientSocket) {
        for (Map.Entry<String, ClientConnection> client : clientList.entrySet()) {
            if (client.getValue().equals(clientSocket)) {
                return client.getKey();
            }
        }
        return null;
    }
}