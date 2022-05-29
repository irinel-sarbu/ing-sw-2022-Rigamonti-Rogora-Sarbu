package controller.client;

import eventSystem.EventListener;
import eventSystem.EventManager;
import eventSystem.annotations.EventHandler;
import eventSystem.events.local.EUpdateNickname;
import eventSystem.events.local.EUpdateServerInfo;
import eventSystem.events.network.ERegister;
import eventSystem.events.network.Messages;
import eventSystem.events.network.client.*;
import eventSystem.events.network.client.actionPhaseRelated.EMoveMotherNature;
import eventSystem.events.network.client.actionPhaseRelated.ESelectRefillCloud;
import eventSystem.events.network.client.actionPhaseRelated.EStudentMovementToDining;
import eventSystem.events.network.client.actionPhaseRelated.EStudentMovementToIsland;
import eventSystem.events.network.server.*;
import eventSystem.events.network.server.gameStateEvents.*;
import network.LightModel;
import network.client.Client;
import util.CliHelper;
import view.View;

public class ClientController implements EventListener {
    private final View view;

    private Client client;
    private LightModel model;
    private String lobbyCode;

    private int numOfConsecutiveErrors = 0;

    public ClientController(View view) {
        this.view = view;
        EventManager.register(this, null);
    }

    @EventHandler
    public void onMessage(ServerMessage message) {
        switch (message.getMsg()) {
            case Messages.CONNECTION_OK -> {
                numOfConsecutiveErrors = 0;
                view.displayMessage(CliHelper.ANSI_GREEN, "Connection established!");
                view.askNickname();
            }

            case Messages.CONNECTION_REFUSED -> {
                if (numOfConsecutiveErrors > 0)
                    view.clearLines(4);
                else
                    view.clearLines(3);

                view.displayError(Messages.CONNECTION_REFUSED);
                view.setupConnection(true);
                numOfConsecutiveErrors++;
            }

            case Messages.CONNECTION_CLOSED -> {
                view.setupConnection(true);
                view.displayError(Messages.CONNECTION_CLOSED);
            }

            case Messages.REGISTRATION_OK -> {
                numOfConsecutiveErrors = 0;
                view.chooseCreateOrJoin(false);
            }

            case Messages.NAME_NOT_AVAILABLE -> {
                if (numOfConsecutiveErrors > 0)
                    view.clearLines(2);
                else
                    view.clearLines(1);

                view.displayError("Player name '" + client.getNickname() + "' already taken. Try again.");
                view.askNickname();
                numOfConsecutiveErrors++;
            }

            case Messages.LOBBY_NOT_FOUND -> {
                view.displayError("Lobby not found!");
                view.chooseCreateOrJoin(false);
            }
            case Messages.LOBBY_FULL -> {
                view.displayError("Lobby is full!");
                view.chooseCreateOrJoin(false);
            }

            case Messages.ALL_CLIENTS_CONNECTED -> {
                view.displayMessage("All clients connected. Starting game.");
                view.allPlayersConnected();
            }

            case Messages.GAME_STARTED -> view.displayMessage("All players are ready. First turn starting.");

            case Messages.CHOOSE_ASSISTANT -> view.chooseAssistant(model.getDeck());

            case Messages.UPDATE_VIEW -> view.update(model);

            case Messages.INVALID_ASSISTANT -> view.displayError("Invalid Assistant card. Please select a valid one:");

            case Messages.START_TURN, Messages.CONTINUE_TURN -> {
                model.setCurrentPlayerName(client.getNickname());
                view.showMenu(model, client.getNickname());
            }

            case Messages.WRONG_PHASE -> view.displayMessage("You can't do that now.");

            case Messages.ILLEGAL_STEPS -> view.displayError("Too many steps, look at your max steps from the assistant card");

            case Messages.INSUFFICIENT_COINS -> {
                view.displayMessage("Not Enough Coins.");
                view.showMenu(model, client.getNickname());
            }
            case Messages.EFFECT_USED -> {
                view.displayMessage("An effect has already been used. You can't use another.");
                view.showMenu(model, client.getNickname());
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
    public void onPlayerNameInserted(EUpdateNickname event) {
        client.setClientNickname(event.getNickname());
        client.register(new ERegister(event.getNickname()));
    }

    /**
     * Client tries to create a Lobby
     */
    @EventHandler
    public void onCreateLobbyRequest(ECreateLobbyRequest event) {
        view.displayMessage("Creating Lobby...");
        client.sendToServer(event);

        createClientModel(client.getNickname());
    }

    /**
     * Client tries to connect to a Lobby.
     */
    @EventHandler
    public void onJoinLobbyRequest(EJoinLobbyRequest event) {
        client.sendToServer(event);
        createClientModel(client.getNickname());
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
        //changed to work with both gui and cli
        view.joinedLobbyDisplay(lobbyCode);
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
        view.chooseCreateOrJoin(true);
    }

    @EventHandler
    public void onPlayerChoosing(EPlayerChoosing event) {
        switch (event.getChoiceType()) {
            case WIZARD -> view.displayMessage(event.getPlayerName() + " is choosing wizard.");
            case ASSISTANT -> {
                view.displayMessage(event.getPlayerName() + " is choosing assistant.");
                view.otherPlayerIsChoosingAssistant();
            }
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
        model.setChosenAssistant(event.getAssistant());
        client.sendToServer(new EAssistantChosen(event.getAssistant()));
    }

    @EventHandler
    public void onPlayerChoseAssistant(EPlayerChoseAssistant event) {
        view.displayMessage("Player " + event.getPlayer() + " chose " + event.getAssistant());
        view.playerChoseAssistant(event.getAssistant());
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
    public void onUpdateGameState(EUpdateGameState event) {
        model.setGameState(event.getGameState());
    }

    @EventHandler
    public void onPlayerTurnStarted(EPlayerTurnStarted event) {
        model.setCurrentPlayerName(event.getPlayer());
        view.displayIdleMenu(model, event.getPlayer());
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

    @EventHandler
    public void onEMoveMotherNature(EMoveMotherNature event) {
        client.sendToServer(new EMoveMotherNature(event.getSteps()));
    }

    @EventHandler
    public void onSelectRefillCloud(ESelectRefillCloud event) {
        client.sendToServer(new ESelectRefillCloud(event.getCloudID()));
    }

    @EventHandler
    public void onDeclareWinner(EDeclareWinner event) {
        view.displayMessage("\n\nPlayer " + event.getPlayer() + " Won!!\n\n");
    }

    @EventHandler
    public void onCheckLastRound(ECheckLastRound event) {
        model.setLastRound(event.lastRound);
    }
}

