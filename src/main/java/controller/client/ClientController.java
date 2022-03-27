package controller.client;

import controller.server.GameController;
import events.*;
import events.types.clientToClient.ConnectEvent;
import events.types.clientToClient.ConnectionRefusedEvent;
import events.types.clientToClient.PlayerNameInsertedEvent;
import events.types.clientToServer.RegisterEvent;
import events.types.serverToClient.ConnectOkEvent;
import events.types.serverToClient.PlayerNameTakenEvent;
import model.GameModel;
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

        // From other parts of the clientApp
        dispatcher.dispatch(EventType.CONNECT, (Event e) -> onConnectRequested((ConnectEvent) e));
        dispatcher.dispatch(EventType.CONNECTION_OK, (Event e) -> onConnectionOk((ConnectOkEvent) e));
        dispatcher.dispatch(EventType.CONNECTION_REFUSED, (Event e) -> onConnectionRefused((ConnectionRefusedEvent) e));

        dispatcher.dispatch(EventType.PLAYER_NAME_INSERTED, (Event e) -> onPlayerNameInserted((PlayerNameInsertedEvent) e));
    }

    private boolean onConnectRequested(ConnectEvent event) {
        view.displayMessage("Connecting to server...");
        client.setAddressAndPort(event.getIP(), event.getPort());
        Thread clientThread = new Thread(client);
        clientThread.start();
        return true;
    }

    private boolean onConnectionRefused(ConnectionRefusedEvent event) {
        view.displayError(event.getMessage());
        view.getServerInfo();
        return true;
    }

    private boolean onConnectionOk(ConnectOkEvent event) {
        view.displayMessage("Connection OK");
        view.getPlayerName();
        return true;
    }

    private boolean onPlayerNameInserted(PlayerNameInsertedEvent event) {
        client.send(new RegisterEvent(event.getName()));
        return true;
    }

    private boolean onPlayerNameTaken(PlayerNameTakenEvent event) {
        view.displayError("Player name already taken. Try again.");
        view.getPlayerName();
        return true;
    }
}
