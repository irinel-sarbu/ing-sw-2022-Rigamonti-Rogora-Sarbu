package controller.server;

import events.Event;
import events.EventDispatcher;
import events.EventType;
import events.types.Messages;
import events.types.clientToServer.ECreateLobbyRequest;
import events.types.clientToServer.EJoinLobbyRequest;
import events.types.serverToClient.EPlayerDisconnected;
import events.types.serverToClient.Message;
import exceptions.LobbyNotFoundException;
import network.server.ClientSocketConnection;
import observer.NetworkObserver;
import util.GameMode;
import util.Logger;
import util.Tuple;
import util.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerController implements NetworkObserver {
    private final Map<String, GameLobby> games;

    /**
     * Main controller class constructor
     */
    public ServerController() {
        this.games = new ConcurrentHashMap<>();
    }

    /**
     * Randomly generate unique lobby code
     *
     * @return an unique {@link String} of 5 alphanumeric characters associated to a lobby
     */
    private String generateLobbyCode() {
        String lobbyCode;
        do {
            lobbyCode = Utils.randomString(5);
        } while (games.containsKey(lobbyCode));

        return lobbyCode;
    }

    /**
     * Get a lobby instance by its unique code
     *
     * @param code the code of the lobby to search for
     * @return a {@link GameLobby} associated to the specified code
     * @throws LobbyNotFoundException if lobby code is not found
     */
    public GameLobby getLobbyByCode(String code) throws LobbyNotFoundException {
        GameLobby ret = games.get(code);
        if (ret != null)
            return ret;
        throw new LobbyNotFoundException("Lobby with id " + code + " not found.");
    }

    /**
     * React when an {@link Event} is raised:
     * if the event is a network event dispatch it, otherwise pass the event to the respective {@link GameLobby}
     * Network events comprehends:
     * - Creating a new lobby
     * - Joining an existing lobby
     * - React to a ping message to detect client disconnections
     *
     * @param networkEvent the event to react to
     */
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
                String playerName = lobby.getPlayerNameBySocket(client);

                lobby.broadcastExceptOne(new EPlayerDisconnected(playerName), playerName);
                lobby.removeClientFromLobbyByName(playerName);
            }
        }

        return true;
    }

    // Lobby creation

    /**
     * {@link ECreateLobbyRequest} handler
     */
    private boolean onCreateLobbyRequest(ECreateLobbyRequest event, ClientSocketConnection client) {
        GameLobby createdLobby = createLobby(event.getNumOfPlayers(), event.getGameMode());
        createdLobby.addClientToLobby(event.getPlayerName(), client);
        return true;
    }

    /**
     * Creates a new Lobby
     *
     * @param numOfPlayers Max players of the lobby
     * @param gameMode     NORMAL or EXPERT
     * @return Lobby code
     */
    private GameLobby createLobby(int numOfPlayers, GameMode gameMode) {
        String code = generateLobbyCode();
        games.put(code, new GameLobby(numOfPlayers, gameMode, code));
        return games.get(code);
    }

    /**
     * {@link EJoinLobbyRequest} handler
     */
    private boolean onJoinLobbyRequest(EJoinLobbyRequest event, ClientSocketConnection client) {
        GameLobby lobby = null;
        try {
            lobby = getLobbyByCode(event.getLobbyCode());
        } catch (LobbyNotFoundException e) {
            Logger.warning(e.getMessage());
            client.send(new Message(Messages.LOBBY_NOT_FOUND));
            return true;
        }

        switch (lobby.getLobbyState()) {
            case INIT -> {
                if (lobby.getClientByName(event.getPlayerName()) != null) {
                    Logger.warning("Player " + event.getPlayerName() + " trying to connect to lobby '" + event.getLobbyCode() + "' but there is already a player with that name connected.");
                    client.send(new Message(Messages.NAME_NOT_AVAILABLE));
                    return true;
                }

                lobby.addClientToLobby(event.getPlayerName(), client);
            }

            case PRE_GAME, IN_GAME, END -> {
                client.send(new Message(Messages.LOBBY_FULL));
                Logger.warning("Player " + event.getPlayerName() + " trying to connect to lobby '" + event.getLobbyCode() + "'but is full.");
            }
        }

        return true;
    }
}
