package controller.server;

import events.*;
import events.types.Messages;
import events.types.clientToServer.*;
import events.types.serverToClient.Message;
import events.types.serverToClient.EPlayerDisconnected;
import exceptions.LobbyNotFoundException;
import network.server.ClientSocketConnection;
import network.server.Server;
import observer.NetworkObservable;
import observer.NetworkObserver;
import util.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerController implements NetworkObserver {
    private final Map<String, GameLobby> games;
    private final Server server;

    public ServerController(Server server) {
        this.games = new ConcurrentHashMap<>();
        this.server = server;
    }

    private String generateLobbyCode() {
        String lobbyCode;
        do {
            lobbyCode = Utils.randomString(5);
        } while (games.containsKey(lobbyCode));

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
    public GameLobby getLobbyByCode(String code) throws LobbyNotFoundException {
        GameLobby ret = games.get(code);
        if (ret != null)
            return ret;
        throw new LobbyNotFoundException("Lobby with id " + code + " not found.");
    }

    /**
     * Creates a new Lobby
     *
     * @param numOfPlayers Max players of the lobby
     * @param gameMode     NORMAL or EXPERT
     * @return Lobby code
     */
    public GameLobby createLobby(int numOfPlayers, GameMode gameMode) {
        String code = generateLobbyCode();
        games.put(code, new GameLobby(numOfPlayers, gameMode, code));
        return games.get(code);
    }

    @Override
    public synchronized void onNetworkEvent(Tuple<Event, ClientSocketConnection> networkEvent) {
        EventDispatcher dp = new EventDispatcher(networkEvent);

        dp.dispatch(EventType.MESSAGE, (Tuple<Event, ClientSocketConnection> t) -> onMessage((Message) t.getKey(), t.getValue()));

        dp.dispatch(EventType.CREATE_LOBBY_REQUEST, (Tuple<Event, ClientSocketConnection> t) -> onCreateLobbyRequest((ECreateLobbyRequest) t.getKey(), t.getValue()));
        dp.dispatch(EventType.JOIN_LOBBY_REQUEST, (Tuple<Event, ClientSocketConnection> t) -> onJoinLobbyRequest((EJoinLobbyRequest) t.getKey(), t.getValue()));

        // Check if event was dispatched in ServerController. If not, notify correct GameLobby
        if (networkEvent.getKey().isHandled())
            return;
        Logger.debug("ServerController was unable to dispatch networkEvent " + networkEvent, "Sending event to correct GameLobby");

        try {
            GameLobby clientLobby = getLobbyByCode(networkEvent.getValue().getLobbyCode());
            clientLobby.onNetworkEvent(networkEvent);
            return;
        } catch (LobbyNotFoundException e) {
            // This should never happen
            Logger.severe(e.getMessage());
        }

        if (networkEvent.getKey().isHandled())
            return;

        // Code should never arrive here, event was not dispatched to any lobby
        Logger.severe("Unhandled event " + networkEvent);
    }

    // Handlers
    private boolean onMessage(Message message, ClientSocketConnection client) {
        switch (message.getMsg()) {
            case Messages.CLIENT_DISCONNECTED -> {
                if (!client.isInLobby())
                    return true;

                GameLobby lobby = games.get(client.getLobbyCode());
                String playerName = lobby.getClientBySocket(client);

                lobby.broadcastExceptOne(new EPlayerDisconnected(playerName), playerName);
                lobby.removeClientFromLobbyByName(playerName);
            }
        }

        return true;
    }

    private boolean onCreateLobbyRequest(ECreateLobbyRequest event, ClientSocketConnection client) {
        GameLobby createdLobby = createLobby(event.getNumOfPlayers(), event.getGameMode());
        createdLobby.addClientToLobby(event.getPlayerName(), client);
        return true;
    }

    private boolean onJoinLobbyRequest(EJoinLobbyRequest event, ClientSocketConnection client) {
        GameLobby lobby = null;
        try {
            lobby = getLobbyByCode(event.getLobbyCode());
        } catch (LobbyNotFoundException e) {
            Logger.warning(e.getMessage());
            client.asyncSend(new Message(Messages.LOBBY_NOT_FOUND));
        }

        lobby.addClientToLobby(event.getPlayerName(), client);
        return true;
    }
}
