package controller.server;

import eventSystem.EventListener;
import eventSystem.EventManager;
import eventSystem.Filter;
import eventSystem.annotations.EventHandler;
import eventSystem.events.network.Messages;
import eventSystem.events.network.client.ClientMessage;
import eventSystem.events.network.client.ECreateLobbyRequest;
import eventSystem.events.network.client.EJoinLobbyRequest;
import eventSystem.events.network.server.EPlayerDisconnected;
import eventSystem.events.network.server.ServerMessage;
import exceptions.LobbyNotFoundException;
import network.server.ClientSocketConnection;
import network.server.Server;
import util.GameMode;
import util.Logger;
import util.Utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerController implements EventListener {
    Server server;
    private final Map<String, GameLobby> games;

    /**
     * Main controller class constructor
     */
    public ServerController(Server server) {
        this.server = server;
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

    @EventHandler
    public void onMessage(ClientMessage message) {
        UUID clientId = message.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        switch (message.getMsg()) {
            case Messages.CLIENT_DISCONNECTED -> {
                if (!client.isInLobby())
                    return;

                GameLobby lobby = games.get(client.getLobbyCode());
                String playerName = lobby.getPlayerNameBySocket(client);

                lobby.broadcastExceptOne(new EPlayerDisconnected(playerName), playerName);
                lobby.removeClientFromLobbyByName(playerName);
            }
        }
    }

    // Lobby creation

    /**
     * {@link ECreateLobbyRequest} handler
     */
    @EventHandler
    public void onCreateLobbyRequest(ECreateLobbyRequest event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        GameLobby createdLobby = createLobby(event.getNumOfPlayers(), event.getGameMode());
        createdLobby.addClientToLobby(event.getPlayerName(), client);
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
        GameLobby newGameLobby = new GameLobby(numOfPlayers, gameMode, code, server);
        EventManager.register(newGameLobby, new Filter(code));
        games.put(code, newGameLobby);

        return games.get(code);
    }

    /**
     * {@link EJoinLobbyRequest} handler
     */
    @EventHandler
    public boolean onJoinLobbyRequest(EJoinLobbyRequest event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        GameLobby lobby = null;
        try {
            lobby = getLobbyByCode(event.getLobbyCode());
        } catch (LobbyNotFoundException e) {
            Logger.warning(e.getMessage());
            client.send(new ServerMessage(Messages.LOBBY_NOT_FOUND));
            return true;
        }

        switch (lobby.getLobbyState()) {
            case INIT -> {
                if (lobby.getClientByName(event.getPlayerName()) != null) {
                    Logger.warning("Player " + event.getPlayerName() + " trying to connect to lobby '" + event.getLobbyCode() + "' but there is already a player with that name connected.");
                    client.send(new ServerMessage(Messages.NAME_NOT_AVAILABLE));
                    return true;
                }

                lobby.addClientToLobby(event.getPlayerName(), client);
            }

            case PRE_GAME, IN_GAME, END -> {
                client.send(new ServerMessage(Messages.LOBBY_FULL));
                Logger.warning("Player " + event.getPlayerName() + " trying to connect to lobby '" + event.getLobbyCode() + "'but is full.");
            }
        }

        return true;
    }
}
