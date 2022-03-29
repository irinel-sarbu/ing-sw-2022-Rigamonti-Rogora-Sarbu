package controller.client;

import events.*;
import events.types.clientToClient.*;
import events.types.clientToServer.*;
import events.types.serverToClient.*;
import network.Client;
import view.View;

public class ClientController implements EventListener {

    private final Client client;
    private final View view;

    public ClientController(Client client, View view) {
        this.client = client;
        this.view = view;
    }

    @Override
    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);

        // Connection stage
        dispatcher.dispatch(EventType.CONNECT, (Event e) -> onConnectRequested((ConnectEvent) e));
        dispatcher.dispatch(EventType.CONNECTION_OK, (Event e) -> onConnectionOk((ConnectOk) e));
        dispatcher.dispatch(EventType.CONNECTION_REFUSED, (Event e) -> onConnectionRefused((ConnectionRefused) e));

        // Create/Join lobby
        dispatcher.dispatch(EventType.CREATE_LOBBY, (Event e) -> onCreateLobby((CreateLobby) e));
        dispatcher.dispatch(EventType.JOIN_LOBBY, (Event e) -> onJoinLobby((JoinLobby) e));
        dispatcher.dispatch(EventType.LOBBY_JOINED, (Event e) -> onLobbyJoined((LobbyJoined) e));
        dispatcher.dispatch(EventType.PLAYER_JOINED, (Event e) -> onPlayerConnected((PlayerJoined) e));
        dispatcher.dispatch(EventType.PLAYER_DISCONNECTED, (Event e) -> onPlayerDisconnected((PlayerDisconnected) e));

        // Create/Join lobby exception handling
        dispatcher.dispatch(EventType.PLAYER_NAME_TAKEN, (Event e) -> onPlayerNameTaken((PlayerNameTaken) e));
        dispatcher.dispatch(EventType.LOBBY_NOT_FOUND, (Event e) -> onLobbyNotFound((LobbyNotFound) e));
        dispatcher.dispatch(EventType.LOBBY_FULL, (Event e) -> onLobbyFull((LobbyFull) e));
    }

    /**
     * Client request a connection to the server
     */
    private boolean onConnectRequested(ConnectEvent event) {
        view.displayMessage("Connecting to server...");
        client.setAddressAndPort(event.getIP(), event.getPort());
        Thread clientThread = new Thread(client);
        clientThread.start();
        return true;
    }

    /**
     * Client established a connection with the Server
     */
    private boolean onConnectionOk(ConnectOk event) {
        view.chooseCreateOrJoin();
        return true;
    }

    /**
     * Client was unable to connect to Server
     */
    private boolean onConnectionRefused(ConnectionRefused event) {
        view.displayError(event.getMessage());
        view.getServerInfo();
        return true;
    }

    /**
     * Client is trying to create a Lobby
     */
    private boolean onCreateLobby(CreateLobby event) {
        view.displayMessage("Creating Lobby...");
        client.sendToServer(event);
        return true;
    }

    /**
     * If Lobby creation was successful, Client connects to Lobby.
     * This event can also be triggered by the Client when Event <code>JoinLobby</code> is successful.
     */
    private boolean onLobbyJoined(LobbyJoined event) {
        view.displayMessage("Joined lobby " + event.getCode());
        return true;
    }

    /**
     * Client is trying to connect to a Lobby.
     */
    private boolean onJoinLobby(JoinLobby event) {
        client.sendToServer(event);
        return true;
    }

    /**
     * This is an Exception thrown by the server when client try to connect to a non-existent Lobby
     */
    private boolean onLobbyNotFound(LobbyNotFound event) {
        view.displayError("Lobby with ID " + event.getCode() + " not found!");
        view.chooseCreateOrJoin();
        return true;
    }

    /**
     * This is an Exception thrown by the server when client try to connect to a full Lobby
     */
    private boolean onLobbyFull(LobbyFull event) {
        view.displayError("Lobby with ID " + event.getCode() + " is full!");
        view.chooseCreateOrJoin();
        return true;
    }

    /**
     * This is an Exception thrown by the server when client try to connect to a Lobby but there is already a Player with the same name.
     */
    private boolean onPlayerNameTaken(PlayerNameTaken event) {
        view.displayError("Player name already taken. Try again.");
        view.getPlayerName(event.getLobbyToJoin());
        return true;
    }

    /**
     * A new Client connected to the same Lobby
     */
    private boolean onPlayerConnected(PlayerJoined event) {
        view.displayMessage(event.getPlayerName() + " connected to Lobby!");
        return true;
    }

    /**
     * A new Client connected to the same Lobby
     */
    private boolean onPlayerDisconnected(PlayerDisconnected event) {
        view.displayMessage(event.getPlayerName() + " left the Lobby!");
        return true;
    }
}