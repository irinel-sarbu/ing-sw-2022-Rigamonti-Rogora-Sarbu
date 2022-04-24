package controller.client;

import events.Event;
import events.EventDispatcher;
import events.EventType;
import events.types.Messages;
import events.types.clientToClient.EUpdateServerInfo;
import events.types.clientToServer.EAssistantChosen;
import events.types.clientToServer.ECreateLobbyRequest;
import events.types.clientToServer.EJoinLobbyRequest;
import events.types.clientToServer.EWizardChosen;
import events.types.serverToClient.*;
import events.types.serverToClient.gameStateEvents.*;
import network.LightModel;
import network.client.Client;
import observer.Observer;
import view.View;

public class ClientController implements Observer {
    private final View view;

    private Client client;
    private LightModel model;
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

        dp.dispatch(EventType.PLAYER_CHOOSING, (Event e) -> onPlayerChoosing((EPlayerChoosing) e));

        dp.dispatch(EventType.CHOOSE_WIZARD, (Event e) -> onChooseWizard((EChooseWizard) e));
        // TODO: remove this function
        dp.dispatch(EventType.WIZARD_NOT_AVAILABLE, (Event e) -> onWizardNoMoreAvailable((EWizardNotAvailable) e));

        dp.dispatch(EventType.UPDATE_SCHOOLBOARD, (Event e) -> onUpdateSchoolboard((EUpdateSchoolBoard) e));
        dp.dispatch(EventType.UPDATE_CLOUD_TILES, (Event e) -> onUpdateCloudTiles((EUpdateCloudTiles) e));
        dp.dispatch(EventType.UPDATE_ISLANDS, (Event e) -> onUpdateIslands((EUpdateIslands) e));
        dp.dispatch(EventType.UPDATE_ASSISTANT_DECK, (Event e) -> onUpdateAssistantDeck((EUpdateAssistantDeck) e));
        dp.dispatch(EventType.UPDATE_CHARACTER_EFFECT, (Event e) -> onUpdateCharacterEffect((EUpdateCharacterEffect) e));
        dp.dispatch(EventType.LIGHT_MODEL_SETUP, (Event e) -> onLightModelSetup((ELightModelSetup) e));

        // View Events
        dp.dispatch(EventType.UPDATE_SERVER_INFO, (Event e) -> onUpdateServerInfo((EUpdateServerInfo) e));
        dp.dispatch(EventType.CREATE_LOBBY_REQUEST, (Event e) -> onCreateLobbyRequest((ECreateLobbyRequest) e));
        dp.dispatch(EventType.JOIN_LOBBY_REQUEST, (Event e) -> onJoinLobbyRequest((EJoinLobbyRequest) e));
        dp.dispatch(EventType.WIZARD_CHOSEN, (Event e) -> onWizardChosen((EWizardChosen) e));

        // Game Events
        dp.dispatch(EventType.ASSISTANT_CHOSEN, (Event e) -> onAssistantChosen((EAssistantChosen) e));
        dp.dispatch(EventType.PLAYER_CHOSE_ASSISTANT, (Event e) -> onPlayerChoseAssistant((EPlayerChoseAssistant) e));
        dp.dispatch(EventType.PLAYER_TURN_STARTED, (Event e) -> onPlayerTurnStarted((EPlayerTurnStarted) e));


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

            case Messages.GAME_STARTED -> view.displayMessage("All players are ready. First turn starting.");

            case Messages.CHOOSE_ASSISTANT -> view.chooseAssistant(model.getDeck());

            case Messages.UPDATE_VIEW -> view.update(model);

            case Messages.INVALID_ASSISTANT ->
                    view.displayMessage("Invalid Assistant card. Please select a valid one:");

            case Messages.START_TURN -> view.startTurn(model);

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

        createClientModel(nickname);
        return true;
    }

    /**
     * Client tries to connect to a Lobby.
     */
    private boolean onJoinLobbyRequest(EJoinLobbyRequest event) {
        this.nickname = event.getPlayerName();
        this.lobbyCode = event.getLobbyCode();
        client.sendToServer(new EJoinLobbyRequest(lobbyCode, nickname));

        createClientModel(nickname);
        return true;
    }

    private void createClientModel(String nickname) {
        this.model = new LightModel(nickname);
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

    private boolean onPlayerChoosing(EPlayerChoosing event) {
        switch (event.getChoiceType()) {
            case WIZARD -> view.displayMessage(event.getPlayerName() + " is choosing wizard.");
            case ASSISTANT -> view.displayMessage(event.getPlayerName() + " is choosing assistant.");
            default -> {
                return false;
            }
        }

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

    private boolean onAssistantChosen(EAssistantChosen event) {
        client.sendToServer(new EAssistantChosen(event.getAssistant()));
        return true;
    }

    private boolean onUpdateSchoolboard(EUpdateSchoolBoard event) {
        model.setPlayerSchoolBoard(event.getPlayerName(), event.getSchoolBoard());
        return true;
    }

    private boolean onUpdateCloudTiles(EUpdateCloudTiles event) {
        model.setCloudTiles(event.getCloudTiles());
        return true;
    }

    private boolean onUpdateIslands(EUpdateIslands event) {
        model.setIslandGroups(event.getIslandGroups());
        model.setMotherNaturePosition(event.getMotherNaturePos());
        return true;
    }

    private boolean onUpdateAssistantDeck(EUpdateAssistantDeck event) {
        model.setDeck(event.getAssistants());
        return true;
    }

    private boolean onUpdateCharacterEffect(EUpdateCharacterEffect event) {
        model.setActiveCharacterEffect(event.getCharacterType());
        return true;
    }

    private boolean onLightModelSetup(ELightModelSetup event) {
        model.setCharacters(event.getCharacterCards());
        return true;
    }

    private boolean onPlayerChoseAssistant(EPlayerChoseAssistant event) {
        view.displayMessage("Player " + event.getPlayer() + " chose " + event.getAssistant());
        return true;
    }

    private boolean onPlayerTurnStarted(EPlayerTurnStarted event) {
        view.displayMessage("Player " + event.getPlayer() + " has started the action phase");
        return true;
    }
    // Planning phase
}