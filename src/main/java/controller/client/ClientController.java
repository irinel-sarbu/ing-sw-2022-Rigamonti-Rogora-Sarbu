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

/**
 * Client controller class
 */
public class ClientController implements EventListener {
    private final View view;

    private Client client;
    private LightModel model;
    private String lobbyCode;

    private int numOfConsecutiveErrors = 0;

    /**
     * Constructor of client controller
     *
     * @param view Is the instance of the chosen view (between CLI and GUI)
     */
    public ClientController(View view) {
        this.view = view;
        EventManager.register(this, null);
    }

    /**
     * Handles event of type "Message" which do not contain any parameter except for the message string,
     * calls different methods based on the Type of message
     *
     * @param message Is the message event
     */
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
                view.displayError("Not Enough Coins.");
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

    /**
     * User inserted nickname, calls method to register it
     *
     * @param event
     */
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

    /**
     * Creates a new instance of the light model
     *
     * @param nickname
     */
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

    /**
     * Handles player choice event (of Wizard and Assistant choices)
     *
     * @param event Contains which player is choosing which type of piece
     */
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

    /**
     * Response from the server for the wizard choice, client now can choose his wizard between the remaining ones contained in the event.
     *
     * @param event contains a List of remaining wizards
     */
    @EventHandler
    public void onChooseWizard(EChooseWizard event) {
        view.chooseWizard(event.getAvailableWizards());
    }

    /**
     * Sends the chosen wizard to the server
     *
     * @param event contains the chosen wizard (sent by VIEW)
     */
    @EventHandler
    public void onWizardChosen(EWizardChosen event) {
        client.sendToServer(new EWizardChosen(event.getWizard()));
    }

    /**
     * Sends the chosen assistant to the server
     *
     * @param event contains the chosen assistant (sent by VIEW)
     */
    @EventHandler
    public void onAssistantChosen(EAssistantChosen event) {
        model.setChosenAssistant(event.getAssistant());
        client.sendToServer(new EAssistantChosen(event.getAssistant()));
    }

    /**
     * Response from server that notify user that another player chose an assistant, calls view methods to show it
     *
     * @param event contains the player name and the assistant he chose
     */
    @EventHandler
    public void onPlayerChoseAssistant(EPlayerChoseAssistant event) {
        view.displayMessage("Player " + event.getPlayer() + " chose " + event.getAssistant());
        view.playerChoseAssistant(event.getAssistant());
    }

    /**
     * Updates the LightModel schoolBoard
     *
     * @param event contains the updated schoolBoard
     */
    @EventHandler
    public void onUpdateSchoolboard(EUpdateSchoolBoard event) {
        model.setPlayerSchoolBoard(event.getPlayerName(), event.getSchoolBoard());
    }

    /**
     * Updates the LightModel cloud tiles
     *
     * @param event contains the updated cloud tiles
     */
    @EventHandler
    public void onUpdateCloudTiles(EUpdateCloudTiles event) {
        model.setCloudTiles(event.getCloudTiles());
    }

    /**
     * Updates the LightModel Island Groups
     *
     * @param event contains the updated Island Groups
     */
    @EventHandler
    public void onUpdateIslands(EUpdateIslands event) {
        model.setIslandGroups(event.getIslandGroups());
        model.setMotherNaturePosition(event.getMotherNaturePos());
    }

    /**
     * Updates the LightModel Assistant List
     *
     * @param event contains the updated Assistant List
     */
    @EventHandler
    public void onUpdateAssistantDeck(EUpdateAssistantDeck event) {
        model.setDeck(event.getAssistants());
    }

    /**
     * Updates the LightModel active character effect
     *
     * @param event contains the updated active character effect
     */
    @EventHandler
    public void onUpdateCharacterEffect(EUpdateCharacterEffect event) {
        model.setActiveCharacterEffect(event.getCharacterType());
    }

    /**
     * Set the extracted characters into the LightModel
     *
     * @param event contains the extracted characters
     */
    @EventHandler
    public void onLightModelSetup(ELightModelSetup event) {
        model.setCharacters(event.getCharacterCards());
    }

    /**
     * Updates the LightModel Game State
     *
     * @param event contains the updated Game State
     */
    @EventHandler
    public void onUpdateGameState(EUpdateGameState event) {
        model.setGameState(event.getGameState());
    }

    /**
     * Notifies to the view which player started his turn
     *
     * @param event contains the name of the player
     */
    @EventHandler
    public void onPlayerTurnStarted(EPlayerTurnStarted event) {
        model.setCurrentPlayerName(event.getPlayer());
        view.displayIdleMenu(model, event.getPlayer());
        view.displayMessage("Player " + event.getPlayer() + " has started his turn");
    }
    // Planning phase

    /**
     * Send to server the event regarding student movement to the dining room
     *
     * @param event contains the chosen student ID
     */
    @EventHandler
    public void onStudentMovementToDining(EStudentMovementToDining event) {
        client.sendToServer(new EStudentMovementToDining(event.getStudentID()));
    }

    /**
     * Send to server the event regarding student movement to an island
     *
     * @param event contains the chosen student id and the chosen island id
     */
    @EventHandler
    public void onStudentMovementToIsland(EStudentMovementToIsland event) {
        client.sendToServer(new EStudentMovementToIsland(event.getStudentID(), event.getIslandID()));
    }

    //CharacterEffect Events

    /**
     * Sends Monk effect event to server
     *
     * @param event contains monk effect parameters
     */
    @EventHandler
    public void onEUseMonkEffect(EUseMonkEffect event) {
        client.sendToServer(new EUseMonkEffect(event.getStudentID(), event.getIslandPos()));
    }

    /**
     * Sends passive effect event to server (Knight, Postman, Farmer, Centaur)
     *
     * @param event contains passive effect parameters
     */
    @EventHandler
    public void onEUseCharacterEffect(EUseCharacterEffect event) {
        client.sendToServer(new EUseCharacterEffect(event.getCharacterType()));
    }

    /**
     * Sends Herald effect event to server
     *
     * @param event contains Herald effect parameters
     */
    @EventHandler
    public void onEUseHeraldEffect(EUseHeraldEffect event) {
        client.sendToServer(new EUseHeraldEffect(event.getIslandGroupID()));
    }

    /**
     * Sends Granny effect event to server
     *
     * @param event contains Granny effect parameters
     */
    @EventHandler
    public void onEUseGrannyEffect(EUseGrannyEffect event) {
        client.sendToServer(new EUseGrannyEffect(event.getIslandID()));
    }

    /**
     * Sends Jester effect event to server
     *
     * @param event contains Jester effect parameters
     */
    @EventHandler
    public void onEUseJesterEffect(EUseJesterEffect event) {
        client.sendToServer(new EUseJesterEffect(event.getEntranceStudents(), event.getJesterStudents()));
    }

    /**
     * Sends Minstrel effect event to server
     *
     * @param event contains Minstrel effect parameters
     */
    @EventHandler
    public void onEUseMinstrelEffect(EUseMinstrelEffect event) {
        client.sendToServer(new EUseMinstrelEffect(event.getEntranceStudents(), event.getDiningStudents()));
    }

    /**
     * Sends Princess effect event to server
     *
     * @param event contains Princess effect parameters
     */
    @EventHandler
    public void onEUsePrincessEffect(EUsePrincessEffect event) {
        client.sendToServer(new EUsePrincessEffect(event.getStudentID()));
    }

    /**
     * Sends Fanatic effect event to server
     *
     * @param event contains Fanatic effect parameters
     */
    @EventHandler
    public void onEUseFanaticEffect(EUseFanaticEffect event) {
        client.sendToServer(new EUseFanaticEffect(event.getColor()));
    }

    /**
     * Sends Thief effect event to server
     *
     * @param event contains Thief effect parameters
     */
    @EventHandler
    public void onEUseThiefEffect(EUseThiefEffect event) {
        client.sendToServer(new EUseThiefEffect(event.getColor()));
    }

    /**
     * Sends move mother nature event
     *
     * @param event contains how many steps should mother nature do
     */
    @EventHandler
    public void onEMoveMotherNature(EMoveMotherNature event) {
        client.sendToServer(new EMoveMotherNature(event.getSteps()));
    }

    /**
     * Sends Refill Cloud event to server
     *
     * @param event contains which cloud was chosen to refill from
     */
    @EventHandler
    public void onSelectRefillCloud(ESelectRefillCloud event) {
        client.sendToServer(new ESelectRefillCloud(event.getCloudID()));
    }

    /**
     * Receives the response of the server containing who won and notifies the view to show it
     *
     * @param event contains the name of the player that won
     */
    @EventHandler
    public void onDeclareWinner(EDeclareWinner event) {
        view.gameOver(model, event.getPlayer());
        view.displayMessage("\n\nPlayer " + event.getPlayer() + " Won!!\n\n");
    }

    /**
     * Updates the lastRound boolean to adapt the cloud selection in case of empty cloud
     *
     * @param event contains true;
     */
    @EventHandler
    public void onCheckLastRound(ECheckLastRound event) {
        model.setLastRound(event.lastRound);
    }
}

