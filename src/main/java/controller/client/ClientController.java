package controller.client;

import eventSystem.EventManager;
import eventSystem.annotations.EventHandler;
import events.types.Messages;
import events.types.clientToClient.EUpdateServerInfo;
import events.types.clientToServer.EAssistantChosen;
import events.types.clientToServer.ECreateLobbyRequest;
import events.types.clientToServer.EJoinLobbyRequest;
import events.types.clientToServer.EWizardChosen;
import events.types.clientToServer.actionPhaseRelated.EStudentMovementToDining;
import events.types.clientToServer.actionPhaseRelated.EStudentMovementToIsland;
import events.types.serverToClient.*;
import events.types.serverToClient.gameStateEvents.*;
import network.LightModel;
import network.client.Client;
import view.View;

public class ClientController {
    private final View view;

    private Client client;
    private LightModel model;
    private String nickname;
    private String lobbyCode;

    public ClientController(View view) {
        this.view = view;
        EventManager.get().register(this);
    }

    @EventHandler
    public boolean onMessage(Message message) {
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

            case Messages.START_TURN -> view.startTurn(model, nickname);

            default -> {
                return false;
            }

        }

        return true;
    }

    /**
     * Client inserted server info
     */
    @EventHandler
    public boolean onUpdateServerInfo(EUpdateServerInfo event) {
        client = new Client(event.getIP(), event.getPort());
        Thread clientThread = new Thread(client);
        clientThread.start();
        return true;
    }

    /**
     * Client tries to create a Lobby
     */
    @EventHandler
    public boolean onCreateLobbyRequest(ECreateLobbyRequest event) {
        view.displayMessage("Creating Lobby...");
        this.nickname = event.getPlayerName();
        client.sendToServer(new ECreateLobbyRequest(event.getGameMode(), event.getNumOfPlayers(), nickname));

        createClientModel(nickname);
        return true;
    }

    /**
     * Client tries to connect to a Lobby.
     */
    @EventHandler
    public boolean onJoinLobbyRequest(EJoinLobbyRequest event) {
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
    @EventHandler
    public boolean onLobbyJoined(ELobbyJoined event) {
        this.lobbyCode = event.getCode();
        view.displayMessage("Joined lobby " + event.getCode());
        view.displayMessage("Waiting for other players to connect...");
        return true;
    }

    /**
     * A new Client connected to the same Lobby
     */
    @EventHandler
    public boolean onPlayerConnected(EPlayerJoined event) {
        view.displayMessage(event.getPlayerName() + " connected to Lobby!");
        return true;
    }

    /**
     * Client disconnected from lobby
     */
    @EventHandler
    public boolean onPlayerDisconnected(EPlayerDisconnected event) {
        view.displayMessage(event.getPlayerName() + " left the Lobby!");
        return true;
    }

    @EventHandler
    public boolean onPlayerChoosing(EPlayerChoosing event) {
        switch (event.getChoiceType()) {
            case WIZARD -> view.displayMessage(event.getPlayerName() + " is choosing wizard.");
            case ASSISTANT -> view.displayMessage(event.getPlayerName() + " is choosing assistant.");
            default -> {
                return false;
            }
        }

        return true;
    }

    @EventHandler
    public boolean onChooseWizard(EChooseWizard event) {
        view.chooseWizard(event.getAvailableWizards());
        return true;
    }

    @EventHandler
    public boolean onWizardNoMoreAvailable(EWizardNotAvailable event) {
        view.displayError("Wizard is no longer available. Choose another one.");
        view.chooseWizard(event.getAvailableWizards());
        return true;
    }

    @EventHandler
    public boolean onWizardChosen(EWizardChosen event) {
        client.sendToServer(new EWizardChosen(event.getWizard()));
        return true;
    }

    @EventHandler
    public boolean onAssistantChosen(EAssistantChosen event) {
        client.sendToServer(new EAssistantChosen(event.getAssistant()));
        return true;
    }

    @EventHandler
    public boolean onUpdateSchoolboard(EUpdateSchoolBoard event) {
        model.setPlayerSchoolBoard(event.getPlayerName(), event.getSchoolBoard());
        return true;
    }

    @EventHandler
    public boolean onUpdateCloudTiles(EUpdateCloudTiles event) {
        model.setCloudTiles(event.getCloudTiles());
        return true;
    }

    @EventHandler
    public boolean onUpdateIslands(EUpdateIslands event) {
        model.setIslandGroups(event.getIslandGroups());
        model.setMotherNaturePosition(event.getMotherNaturePos());
        return true;
    }

    @EventHandler
    public boolean onUpdateAssistantDeck(EUpdateAssistantDeck event) {
        model.setDeck(event.getAssistants());
        return true;
    }

    @EventHandler
    public boolean onUpdateCharacterEffect(EUpdateCharacterEffect event) {
        model.setActiveCharacterEffect(event.getCharacterType());
        return true;
    }

    @EventHandler
    public boolean onLightModelSetup(ELightModelSetup event) {
        model.setCharacters(event.getCharacterCards());
        return true;
    }

    @EventHandler
    public boolean onPlayerChoseAssistant(EPlayerChoseAssistant event) {
        view.displayMessage("Player " + event.getPlayer() + " chose " + event.getAssistant());
        return true;
    }

    @EventHandler
    public boolean onPlayerTurnStarted(EPlayerTurnStarted event) {
        view.displayMessage("Player " + event.getPlayer() + " has started the action phase");
        return true;
    }
    // Planning phase

    @EventHandler
    public boolean onStudentMovementToDining(EStudentMovementToDining event) {
        client.sendToServer(new EStudentMovementToDining(event.getStudentID()));
        return true;
    }

    @EventHandler
    public boolean onStudentMovementToIsland(EStudentMovementToIsland event) {
        client.sendToServer(new EStudentMovementToIsland(event.getStudentID(), event.getIslandID()));
        return true;
    }
}