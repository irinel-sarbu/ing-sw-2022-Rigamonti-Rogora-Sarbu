package controller.client;

import eventSystem.EventListener;
import eventSystem.EventManager;
import eventSystem.annotations.EventHandler;
import eventSystem.events.local.EUpdateServerInfo;
import eventSystem.events.network.EConnectionAccepted;
import eventSystem.events.network.Messages;
import eventSystem.events.network.client.*;
import eventSystem.events.network.client.actionPhaseRelated.EStudentMovementToDining;
import eventSystem.events.network.client.actionPhaseRelated.EStudentMovementToIsland;
import eventSystem.events.network.server.*;
import eventSystem.events.network.server.gameStateEvents.*;
import network.LightModel;
import network.client.Client;
import util.Logger;
import view.View;

public class ClientController implements EventListener {
    private final View view;

    private Client client;
    private LightModel model;
    private String nickname;
    private String lobbyCode;

    public ClientController(View view) {
        this.view = view;
        EventManager.register(this, null);
    }

    @EventHandler
    public void onMessage(ServerMessage message) {
        switch (message.getMsg()) {
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

            case Messages.INVALID_ASSISTANT -> view.displayMessage("Invalid Assistant card. Please select a valid one:");

            case Messages.START_TURN, Messages.CONTINUE_TURN -> view.showFirstMenu(model, nickname);

            case Messages.WRONG_PHASE -> view.displayMessage("You can't do that now.");

            case Messages.INSUFFICIENT_COINS -> {
                view.displayMessage("Not Enough Coins.");
                view.showFirstMenu(model, nickname);
            }
            case Messages.EFFECT_USED -> {
                view.displayMessage("An effect has already been used. You can't use another.");
                view.showFirstMenu(model, nickname);
            }
        }
    }

    /**
     * Client inserted server info
     */
    @EventHandler
    public void onUpdateServerInfo(EUpdateServerInfo event) {
        client = new Client(event.getIP(), event.getPort());
        Thread clientThread = new Thread(client);
        clientThread.start();
    }

    @EventHandler
    public void onConnectionAccepted(EConnectionAccepted event) {
        Logger.debug("Client uuid: " + event.getId());
        client.setClientIdentifier(event.getId());
        view.chooseCreateOrJoin();
    }

    /**
     * Client tries to create a Lobby
     */
    @EventHandler
    public void onCreateLobbyRequest(ECreateLobbyRequest event) {
        view.displayMessage("Creating Lobby...");
        this.nickname = event.getPlayerName();
        client.sendToServer(new ECreateLobbyRequest(event.getGameMode(), event.getNumOfPlayers(), nickname));

        createClientModel(nickname);
    }

    /**
     * Client tries to connect to a Lobby.
     */
    @EventHandler
    public void onJoinLobbyRequest(EJoinLobbyRequest event) {
        this.nickname = event.getPlayerName();
        this.lobbyCode = event.getLobbyCode();
        client.sendToServer(new EJoinLobbyRequest(lobbyCode, nickname));

        createClientModel(nickname);
    }

    private void createClientModel(String nickname) {
        this.model = new LightModel(nickname);
    }

    /**
     * If Lobby creation was successful, Client connects to Lobby.
     * This event can also be triggered by the Client when Event <code>JoinLobby</code> is successful.
     */
    @EventHandler
    public void onLobbyJoined(ELobbyJoined event) {
        this.lobbyCode = event.getCode();
        client.setLobbyId(lobbyCode);

        view.displayMessage("Joined lobby " + event.getCode());
        view.displayMessage("Waiting for other players to connect...");
    }

    /**
     * A new Client connected to the same Lobby
     */
    @EventHandler
    public void onPlayerConnected(EPlayerJoined event) {
        view.displayMessage(event.getPlayerName() + " connected to Lobby!");
    }

    /**
     * Client disconnected from lobby
     */
    @EventHandler
    public void onPlayerDisconnected(EPlayerDisconnected event) {
        view.displayMessage(event.getPlayerName() + " left the Lobby!");
    }

    @EventHandler
    public void onPlayerChoosing(EPlayerChoosing event) {
        switch (event.getChoiceType()) {
            case WIZARD -> view.displayMessage(event.getPlayerName() + " is choosing wizard.");
            case ASSISTANT -> view.displayMessage(event.getPlayerName() + " is choosing assistant.");
        }
    }

    @EventHandler
    public void onChooseWizard(EChooseWizard event) {
        view.chooseWizard(event.getAvailableWizards());
    }

    @EventHandler
    public void onWizardChosen(EWizardChosen event) {
        client.sendToServer(new EWizardChosen(event.getWizard()));
    }

    @EventHandler
    public void onAssistantChosen(EAssistantChosen event) {
        client.sendToServer(new EAssistantChosen(event.getAssistant()));
    }

    @EventHandler
    public void onUpdateSchoolboard(EUpdateSchoolBoard event) {
        model.setPlayerSchoolBoard(event.getPlayerName(), event.getSchoolBoard());
    }

    @EventHandler
    public void onUpdateCloudTiles(EUpdateCloudTiles event) {
        model.setCloudTiles(event.getCloudTiles());
    }

    @EventHandler
    public void onUpdateIslands(EUpdateIslands event) {
        model.setIslandGroups(event.getIslandGroups());
        model.setMotherNaturePosition(event.getMotherNaturePos());
    }

    @EventHandler
    public void onUpdateAssistantDeck(EUpdateAssistantDeck event) {
        model.setDeck(event.getAssistants());
    }

    @EventHandler
    public void onUpdateCharacterEffect(EUpdateCharacterEffect event) {
        model.setActiveCharacterEffect(event.getCharacterType());
    }

    @EventHandler
    public void onLightModelSetup(ELightModelSetup event) {
        model.setCharacters(event.getCharacterCards());
    }

    @EventHandler
    public void onPlayerChoseAssistant(EPlayerChoseAssistant event) {
        view.displayMessage("Player " + event.getPlayer() + " chose " + event.getAssistant());
    }

    @EventHandler
    public void onPlayerTurnStarted(EPlayerTurnStarted event) {
        view.displayMessage("Player " + event.getPlayer() + " has started his turn");
    }
    // Planning phase

    @EventHandler
    public void onStudentMovementToDining(EStudentMovementToDining event) {
        client.sendToServer(new EStudentMovementToDining(event.getStudentID()));
    }

    @EventHandler
    public void onStudentMovementToIsland(EStudentMovementToIsland event) {
        client.sendToServer(new EStudentMovementToIsland(event.getStudentID(), event.getIslandID()));
    }

    //CharacterEffect Events
    @EventHandler
    public void onEUseMonkEffect(EUseMonkEffect event) {
        client.sendToServer(new EUseMonkEffect(event.getStudentID(), event.getIslandPos()));
    }

    @EventHandler
    public void onEUseCharacterEffect(EUseCharacterEffect event) {
        client.sendToServer(new EUseCharacterEffect(event.getCharacterType()));
    }

    @EventHandler
    public void onEUseHeraldEffect(EUseHeraldEffect event) {
        client.sendToServer(new EUseHeraldEffect(event.getIslandGroupID()));
    }

    @EventHandler
    public void onEUseGrannyEffect(EUseGrannyEffect event) {
        client.sendToServer(new EUseGrannyEffect(event.getIslandID()));
    }

    @EventHandler
    public void onEUseJesterEffect(EUseJesterEffect event) {
        client.sendToServer(new EUseJesterEffect(event.getEntranceStudents(), event.getJesterStudents()));
    }

    @EventHandler
    public void onEUseMinstrelEffect(EUseMinstrelEffect event) {
        client.sendToServer(new EUseMinstrelEffect(event.getEntranceStudents(), event.getDiningStudents()));
    }

    @EventHandler
    public void onEUsePrincessEffect(EUsePrincessEffect event) {
        client.sendToServer(new EUsePrincessEffect(event.getStudentID()));
    }

    @EventHandler
    public void onEUseFanaticEffect(EUseFanaticEffect event) {
        client.sendToServer(new EUseFanaticEffect(event.getColor()));
    }

    @EventHandler
    public void onEUseThiefEffect(EUseThiefEffect event) {
        client.sendToServer(new EUseThiefEffect(event.getColor()));
    }
}