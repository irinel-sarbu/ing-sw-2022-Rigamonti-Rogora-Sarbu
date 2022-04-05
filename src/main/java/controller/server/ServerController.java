package controller.server;

import events.*;
import events.types.Messages;
import events.types.clientToServer.*;
import events.types.serverToClient.Message;
import events.types.serverToClient.EPlayerDisconnected;
import network.server.ClientSocketConnection;
import network.server.Server;
import observer.NetworkObserver;
import util.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerController implements NetworkObserver {
    private final Map<String, Lobby> lobbyList;
    private final Server server;

    public ServerController(Server server) {
        this.lobbyList = new ConcurrentHashMap<>();
        this.server = server;
    }

    private String generateLobbyCode() {
        String lobbyCode;
        do {
            lobbyCode = Utils.randomString(5);
        } while (lobbyList.containsKey(lobbyCode));

        return lobbyCode;
    }

    /**
     * Searches Lobby by code.
     * Returns Lobby if found.
     * Returns null if Lobby not found.
     *
     * @param code Lobby code
     * @return Lobby | null
     */
    public Lobby getLobbyByCode(String code) {
        return lobbyList.get(code);
    }

    /**
     * Creates a new Lobby
     *
     * @param numOfPlayers Max players of the lobby
     * @param gameMode     NORMAL or EXPERT
     * @return Lobby code
     */
    public Lobby createLobby(int numOfPlayers, GameMode gameMode) {
        String code = generateLobbyCode();
        lobbyList.put(code, new Lobby(code, numOfPlayers, gameMode));
        return lobbyList.get(code);
    }

    /**
     * Dispatches events.
     * If Event can't be handled it is redirected to the correct Lobby.
     *
     * @param networkEvent An Event linked to a ClientConnection
     */
    @Override
    public synchronized void onNetworkEvent(Tuple<Event, ClientSocketConnection> networkEvent) {
        EventDispatcher dp = new EventDispatcher(networkEvent);

        dp.dispatch(EventType.MESSAGE, (Tuple<Event, ClientSocketConnection> t) -> onMessage((Message) t.getKey(), t.getValue()));

        dp.dispatch(EventType.CREATE_LOBBY_REQUEST, (Tuple<Event, ClientSocketConnection> t) -> onCreateLobbyRequest((ECreateLobbyRequest) t.getKey(), t.getValue()));
        dp.dispatch(EventType.JOIN_LOBBY_REQUEST, (Tuple<Event, ClientSocketConnection> t) -> onJoinLobbyRequest((EJoinLobbyRequest) t.getKey(), t.getValue()));
    }

    // Handlers
    private boolean onMessage(Message message, ClientSocketConnection client) {
        switch (message.getMsg()) {
            case Messages.CLIENT_DISCONNECTED -> {
                if(!client.isInLobby())
                    return true;

                Lobby lobby = lobbyList.get(client.getLobbyCode());
                String playerName = lobby.getClientBySocket(client);

                lobby.broadcastExceptOne(new EPlayerDisconnected(playerName), playerName);
                lobby.removeClientFromLobbyByName(playerName);
            }
        }

        return true;
    }

    private boolean onCreateLobbyRequest(ECreateLobbyRequest event, ClientSocketConnection client) {
        Lobby createdLobby = createLobby(event.getNumOfPlayers(), event.getGameMode());
        createdLobby.addClientToLobby(event.getPlayerName(), client);
        server.registerListener(createdLobby);
        return true;
    }

    private boolean onJoinLobbyRequest(EJoinLobbyRequest event, ClientSocketConnection client) {
        Lobby lobby = getLobbyByCode(event.getLobbyCode());
        if (lobby == null) {
            client.asyncSend(new Message(Messages.LOBBY_NOT_FOUND));
            return true;
        }

        lobby.addClientToLobby(event.getPlayerName(), client);
        return true;
    }
}
