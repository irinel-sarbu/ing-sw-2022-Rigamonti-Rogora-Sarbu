package controller.client;

import events.*;
import events.types.Messages;
import events.types.clientToClient.*;
import events.types.clientToServer.*;
import events.types.serverToClient.*;
import network.client.Client;
import observer.Observer;
import view.View;

public class ClientController implements Observer {
    private final View view;

    private Client client;
    private String nickname;
    private String lobbyCode;

    public ClientController(View view) {
        this.view = view;
    }

    @Override
    public void onEvent(Event event) {
        EventDispatcher dp = new EventDispatcher(event);

        // Server events
        dp.dispatch(EventType.PING, (Event e) -> onPing((Ping) e));
        dp.dispatch(EventType.MESSAGE, (Event e) -> onMessage((Message) e));

        dp.dispatch(EventType.LOBBY_JOINED, (Event e) -> onLobbyJoined((ELobbyJoined) e));
        dp.dispatch(EventType.PLAYER_JOINED, (Event e) -> onPlayerConnected((EPlayerJoined) e));
        dp.dispatch(EventType.PLAYER_DISCONNECTED, (Event e) -> onPlayerDisconnected((EPlayerDisconnected) e));

        dp.dispatch(EventType.CHOOSE_WIZARD, (Event e) -> onChooseWizard((EChooseWizard) e));
        dp.dispatch(EventType.WIZARD_NOT_AVAILABLE, (Event e) -> onWizardNoMoreAvailable((EWizardNotAvailable) e));

        // View Events
        dp.dispatch(EventType.UPDATE_SERVER_INFO, (Event e) -> onUpdateServerInfo((EUpdateServerInfo) e));
        dp.dispatch(EventType.CREATE_LOBBY_REQUEST, (Event e) -> onCreateLobbyRequest((ECreateLobbyRequest) e));
        dp.dispatch(EventType.JOIN_LOBBY_REQUEST, (Event e) -> onJoinLobbyRequest((EJoinLobbyRequest) e));
        dp.dispatch(EventType.WIZARD_CHOSEN, (Event e) -> onWizardChosen((EWizardChosen) e));

        if (!event.isHandled()) {
            view.displayError("Unhandled event " + event);
        }
    }

    private boolean onPing(Ping ping) {
        // Do nothing, just a check by the server
        return true;
    }

    private boolean onMessage(Message message) {
        switch (message.getMsg()) {
            case Messages.CONNECTION_OK -> view.chooseCreateOrJoin();
            case Messages.CONNECTION_REFUSED -> {
                view.displayError(Messages.CONNECTION_REFUSED);
                view.askServerInfo();
            }

            case Messages.LOBBY_NOT_FOUND -> {
                view.displayError("Lobby with ID " + lobbyCode + " not found!");
                view.chooseCreateOrJoin();
            }
            case Messages.LOBBY_FULL -> {
                view.displayError("Lobby with ID " + lobbyCode + " is full!");
                view.chooseCreateOrJoin();
            }
            case Messages.NAME_NOT_AVAILABLE -> {
                view.displayError("Player name already taken. Try again.");
                view.chooseCreateOrJoin();
            }
            case Messages.ALL_CLIENTS_CONNECTED -> view.displayMessage("All clients connected. Starting game.");

            default -> {
                return false;
            }
        }

        return true;
    }

    /**
     * Client inserted server info
     */
    private boolean onUpdateServerInfo(EUpdateServerInfo event) {
        client = new Client(event.getIP(), event.getPort());
        client.registerListener(this);
        Thread clientThread = new Thread(client);
        clientThread.start();
        return true;
    }

    /**
     * Client tries to create a Lobby
     */
    private boolean onCreateLobbyRequest(ECreateLobbyRequest event) {
        view.displayMessage("Creating Lobby...");
        this.nickname = event.getPlayerName();
        client.sendToServer(new ECreateLobbyRequest(event.getGameMode(), event.getNumOfPlayers(), nickname));
        return true;
    }

    /**
     * Client tries to connect to a Lobby.
     */
    private boolean onJoinLobbyRequest(EJoinLobbyRequest event) {
        this.nickname = event.getPlayerName();
        this.lobbyCode = event.getLobbyCode();
        client.sendToServer(new EJoinLobbyRequest(lobbyCode, nickname));
        return true;
    }

    /**
     * If Lobby creation was successful, Client connects to Lobby.
     * This event can also be triggered by the Client when Event <code>JoinLobby</code> is successful.
     */
    private boolean onLobbyJoined(ELobbyJoined event) {
        this.lobbyCode = event.getCode();
        view.displayMessage("Joined lobby " + event.getCode());
        view.displayMessage("Waiting for other players to connect...");
        return true;
    }

    /**
     * A new Client connected to the same Lobby
     */
    private boolean onPlayerConnected(EPlayerJoined event) {
        view.displayMessage(event.getPlayerName() + " connected to Lobby!");
        return true;
    }

    /**
     * Client disconnected from lobby
     */
    private boolean onPlayerDisconnected(EPlayerDisconnected event) {
        view.displayMessage(event.getPlayerName() + " left the Lobby!");
        return true;
    }

    private boolean onChooseWizard(EChooseWizard event) {
        view.chooseWizard(event.getAvailableWizards());
        return true;
    }

    private boolean onWizardNoMoreAvailable(EWizardNotAvailable event) {
        view.displayError("Wizard is no longer available. Choose another one.");
        view.chooseWizard(event.getAvailableWizards());
        return true;
    }

    private boolean onWizardChosen(EWizardChosen event) {
        client.sendToServer(new EWizardChosen(event.getWizard()));
        return true;
    }
}