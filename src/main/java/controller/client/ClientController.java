package controller.client;

import controller.server.GameController;
import events.*;
import events.types.clientToClient.*;
import events.types.clientToServer.*;
import events.types.serverToClient.*;
import model.Player;
import network.Client;
import view.View;

import java.util.logging.Logger;

public class ClientController implements EventListener {
    private final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    private final Client client;
    private final View view;

    public ClientController(Client client, View view) {
        this.client = client;
        this.view = view;
    }

    @Override
    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);

        // From server
        dispatcher.dispatch(EventType.PLAYER_NAME_TAKEN, (Event e) -> onPlayerNameTaken((PlayerNameTakenEvent) e));
        dispatcher.dispatch(EventType.REGISTER_OK, (Event e) -> onRegistrationOk((RegistrationOkEvent) e));
        dispatcher.dispatch(EventType.GAME_CREATED, (Event e) -> onGameCreated((GameCreatedEvent) e));
        dispatcher.dispatch(EventType.GAME_JOINED, (Event e) -> onGameJoined((GameJoinedEvent) e));
        dispatcher.dispatch(EventType.GAME_NOT_FOUND, (Event e) -> onGameNotFound((GameNotFoundEvent) e));
        dispatcher.dispatch(EventType.PLAYER_CONNECTED, (Event e) -> onPlayerConnected((PlayerConnectedEvent) e));

        // From other parts of the clientApp
        dispatcher.dispatch(EventType.CONNECT, (Event e) -> onConnectRequested((ConnectEvent) e));
        dispatcher.dispatch(EventType.CONNECTION_OK, (Event e) -> onConnectionOk((ConnectOkEvent) e));
        dispatcher.dispatch(EventType.CONNECTION_REFUSED, (Event e) -> onConnectionRefused((ConnectionRefusedEvent) e));
        dispatcher.dispatch(EventType.PLAYER_NAME_INSERTED, (Event e) -> onPlayerNameInserted((PlayerNameInsertedEvent) e));
        dispatcher.dispatch(EventType.CREATE_GAME, (Event e) -> onCreateGame((CreateGameEvent) e));
        dispatcher.dispatch(EventType.JOIN_GAME, (Event e) -> onJoinGame((JoinGameEvent) e));
    }

    // Events from server
    private boolean onPlayerNameTaken(PlayerNameTakenEvent event) {
        view.displayError("Player name already taken. Try again.");
        view.getPlayerName();
        return true;
    }

    private boolean onRegistrationOk(RegistrationOkEvent event) {
        view.chooseCreateOrJoin();
        return true;
    }

    private boolean onGameCreated(GameCreatedEvent event) {
        view.displayMessage("Lobby with id " + event.getCode() + " created.");
        view.displayMessage("Waiting for other players to connect...");
        return true;
    }

    private boolean onGameJoined(GameJoinedEvent event) {
        view.displayMessage("Joined lobby " + event.getCode());
        return true;
    }

    private boolean onGameNotFound(GameNotFoundEvent event) {
        view.displayError("Lobby with ID " + event.getCode() + " not found!");
        view.chooseCreateOrJoin();
        return true;
    }

    private boolean onPlayerConnected(PlayerConnectedEvent event) {
        view.displayMessage(event.getPlayerName() + " connected!");
        return true;
    }

    // Events other parts of the client
    private boolean onConnectRequested(ConnectEvent event) {
        view.displayMessage("Connecting to server...");
        client.setAddressAndPort(event.getIP(), event.getPort());
        Thread clientThread = new Thread(client);
        clientThread.start();
        return true;
    }

    private boolean onConnectionOk(ConnectOkEvent event) {
        view.displayMessage("Connection OK");
        view.getPlayerName();
        return true;
    }

    private boolean onConnectionRefused(ConnectionRefusedEvent event) {
        view.displayError(event.getMessage());
        view.getServerInfo();
        return true;
    }

    private boolean onPlayerNameInserted(PlayerNameInsertedEvent event) {
        client.send(new RegisterEvent(event.getName()));
        return true;
    }

    private boolean onCreateGame(CreateGameEvent event) {
        view.displayMessage("Creating game...");
        client.send(event);
        return true;
    }

    private boolean onJoinGame(JoinGameEvent event) {
        view.displayMessage("Joining game...");
        client.send(event);
        return true;
    }
}