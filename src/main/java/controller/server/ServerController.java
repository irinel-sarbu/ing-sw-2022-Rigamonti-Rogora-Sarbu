package controller.server;

import events.*;
import events.types.clientToServer.*;
import events.types.serverToClient.LobbyNotFound;
import events.types.serverToClient.PingEvent;
import events.types.serverToClient.PlayerDisconnected;
import events.types.serverToServer.ClientDisconnect;
import network.*;
import util.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ServerController implements NetworkEventListener {
    private final Logger LOGGER = Logger.getLogger(ServerController.class.getName());

    protected final Map<String, Lobby> lobbyList;

    public ServerController() {
        this.lobbyList = new ConcurrentHashMap<>();
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
        lobbyList.put(code, new Lobby(this, code, numOfPlayers, gameMode));
        return lobbyList.get(code);
    }

    /**
     * Search if player is in a Lobby.
     * If True, returns the Lobby.
     * If False, returns null.
     *
     * @param client ClientConnection
     * @return LobbyController | null
     */
    private Lobby searchClientInLobbies(ClientConnection client) {
        return client.isInLobby() ? lobbyList.get(client.getLobbyCode()) : null;
    }

    /**
     * Dispatches events.
     * If Event can't be handled it is redirected to the correct Lobby.
     *
     * @param networkEvent An Event linked to a ClientConnection
     */
    @Override
    public synchronized void onEvent(Tuple<Event, ClientConnection> networkEvent) {
        EventDispatcher dispatcher = new EventDispatcher(networkEvent);

        LOGGER.info("New event " + networkEvent.getKey() + " from " + networkEvent.getValue());

        dispatcher.dispatch(EventType.CREATE_LOBBY, (Tuple<Event, ClientConnection> t) -> onCreateLobby((CreateLobby) t.getKey(), t.getValue()));
        dispatcher.dispatch(EventType.JOIN_LOBBY, (Tuple<Event, ClientConnection> t) -> onJoinLobby((JoinLobby) t.getKey(), t.getValue()));

        dispatcher.dispatch(EventType.CLIENT_DISCONNECT, (Tuple<Event, ClientConnection> t) -> onClientDisconnect((ClientDisconnect) t.getKey(), t.getValue()));
    }

    // Handlers
    private boolean onCreateLobby(CreateLobby event, ClientConnection client) {
        Lobby createdLobby = createLobby(event.getNumOfPlayers(), event.getGameMode());
        createdLobby.addClientToLobby(event.getCreatorName(), client);
        return true;
    }

    private boolean onJoinLobby(JoinLobby event, ClientConnection client) {
        Lobby lobby = getLobbyByCode(event.getLobbyCode());
        if (lobby == null) {
            client.send(new LobbyNotFound(event.getLobbyCode()));
            return true;
        }

        lobby.addClientToLobby(event.getPlayerName(), client);

        return true;
    }

    private boolean onClientDisconnect(ClientDisconnect event, ClientConnection client) {
        if(!client.isInLobby())
            return true;

        Lobby lobby = lobbyList.get(client.getLobbyCode());
        String playerName = lobby.getClientBySocket(client);

        lobby.broadcastExceptOne(new PlayerDisconnected(playerName), playerName);
        return true;
    }

}
