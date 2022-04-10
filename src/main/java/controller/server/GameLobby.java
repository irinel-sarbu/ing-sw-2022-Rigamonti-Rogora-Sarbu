package controller.server;

import controller.server.states.*;
import events.*;
import events.types.Messages;
import events.types.clientToServer.*;
import events.types.serverToClient.*;
import events.types.serverToClient.Message;
import exceptions.PlayerNotFoundException;
import exceptions.supplyEmptyException;
import model.GameModel;
import model.Player;
import model.expert.CharacterCard;
import model.expert.CoinSupply;
import network.server.ClientSocketConnection;
import observer.NetworkObserver;
import util.*;

import java.util.*;

public class GameLobby implements NetworkObserver {

    private final String lobbyCode;
    private final int maxPlayers;

    private LobbyState lobbyState;

    private final GameMode gameMode;
    private final Map<String, ClientSocketConnection> clientList;

    private final List<Wizard> availableWizards;

    private final GameModel model;

    private int turnCounter = 0;
    private Player currentPlayer;
    private int turnProgress;
    private int studentsMoved;
    private GameState currentGameState;
    private List<Player> planningPhaseOrder;
    private List<Player> actionPhaseOrder;
    private List<Player> nextPlanningPhaseOrder;

    //States handlers
    private ResolveIsland resolveIsland;
    private StudentMovement studentMovement;
    private MotherNatureMovement motherNatureMovement;
    private final TurnEpilogue epilogue;
    private final PlanningPhase planningPhase;
    private final GameOver gameOver;
    private final CharacterEffectHandler characterEffectHandler;

    /**
     * Controller of the GameModel
     *
     * @param numOfPlayers How many players will play
     * @param gameMode     Game mode
     * @param code         Unique identifier of each lobby
     */
    public GameLobby(int numOfPlayers, GameMode gameMode, String code) {

        this.lobbyCode = code;
        this.maxPlayers = numOfPlayers;
        this.gameMode = gameMode;
        this.clientList = new HashMap<>();

        setLobbyState(LobbyState.INIT);

        this.model = new GameModel(maxPlayers, this.gameMode);
        this.availableWizards = new ArrayList<>(Arrays.asList(Wizard.values()));

        // TODO: ERROR At this point there are 0 players in lobby -> move to create lobby function
//        try {
//            this.currentPlayer = model.getPlayerByID(0);
//        } catch (PlayerNotFoundException e) {
//            Logger.warning("Game lobby <" + code + "> is empty");
//        }
//        this.studentsMoved = 0;
//        this.currentGameState = GameState.SETUP;
//        this.planningPhaseOrder = gameModel.getPlayers();
//        this.actionPhaseOrder = null;
//        this.nextPlanningPhaseOrder = null;
//        this.turnProgress = 1;

        //states
        this.epilogue = new TurnEpilogue();
        this.studentMovement = new DefaultStudentMovement();
        this.resolveIsland = new DefaultResolveIsland();
        this.planningPhase = new PlanningPhase();
        this.motherNatureMovement = new DefaultMotherNatureMovement();
        this.gameOver = new GameOver();
        this.characterEffectHandler = new CharacterEffectHandler();
    }

    /**
     * Set net Lobby state
     *
     * @param newState New {@link LobbyState}
     */
    public void setLobbyState(LobbyState newState) {
        this.lobbyState = newState;
    }

    /**
     * Get current Lobby state
     *
     * @return {@link LobbyState}
     */
    public LobbyState getLobbyState() {
        return this.lobbyState;
    }

    @Override
    public void onNetworkEvent(Tuple<Event, ClientSocketConnection> networkEvent) {
        EventDispatcher dp = new EventDispatcher(networkEvent);

        switch (lobbyState) {
            case INIT -> initState(networkEvent);
            case PRE_GAME -> preGameState(networkEvent);
            case IN_GAME -> inGameState(networkEvent);
            case END -> endGameState(networkEvent);
        }

        dp.dispatch(EventType.WIZARD_CHOSEN, (Tuple<Event, ClientSocketConnection> t) -> playerHasChosenWizard((EWizardChosen) t.getKey(), t.getValue()));
        dp.dispatch(EventType.USE_CHARACTER_EFFECT, (Tuple<Event, ClientSocketConnection> t) -> playerHasActivatedEffect((EUseCharacterEffect) t.getKey(), t.getValue()));
    }

    // LOBBY STATES

    /**
     * Handling of lobby init state
     *
     * @param networkEvent Event from active player
     */
    private void initState(Tuple<Event, ClientSocketConnection> networkEvent) {
        // In this state a player can disconnect at any time (if players are 3)
        // If the only player in lobby disconnects, lobby is destroyed
        EventDispatcher initDispatcher = new EventDispatcher(networkEvent);


    }

    private void preGameState(Tuple<Event, ClientSocketConnection> networkEvent) {

    }

    private void inGameState(Tuple<Event, ClientSocketConnection> networkEvent) {

    }

    private void endGameState(Tuple<Event, ClientSocketConnection> networkEvent) {

    }

    /**
     * Broadcast Event to all clients connected to lobby
     *
     * @param event Event to broadcast
     */
    public void broadcast(Event event) {
        for (Map.Entry<String, ClientSocketConnection> entry : clientList.entrySet()) {
            ClientSocketConnection client = entry.getValue();
            client.asyncSend(event);
        }
    }

    /**
     * Broadcast Event to all clients connected to lobby, except sender
     *
     * @param event          Event to broadcast
     * @param excludedClient Client to exclude
     */
    public void broadcastExceptOne(Event event, String excludedClient) {
        for (Map.Entry<String, ClientSocketConnection> entry : clientList.entrySet()) {
            if (entry.getKey() != null && !entry.getKey().equals(excludedClient)) {
                ClientSocketConnection client = entry.getValue();
                client.asyncSend(event);
            }
        }
    }

    /**
     * Get current lobby code
     */
    public String getLobbyCode() {
        return lobbyCode;
    }

    public void addClientToLobby(String name, ClientSocketConnection client) {
        clientList.put(name, client);
        client.joinLobby(getLobbyCode());

        client.asyncSend(new ELobbyJoined(lobbyCode));
        broadcastExceptOne(new EPlayerJoined(name), name);

        if (clientList.size() == maxPlayers) {
            broadcast(new Message(Messages.ALL_CLIENTS_CONNECTED));
            setLobbyState(LobbyState.PRE_GAME);
            Logger.debug("Lobby " + getLobbyCode() + " - " + "All clients connected. Switching state to " + getLobbyState());
            setupPreGame();
        }

        // TODO: Resilience to disconnections

//        if (model.getPlayerSize() >= maxPlayers) {
//            client.asyncSend(new Message(Messages.LOBBY_FULL));
//            Logger.warning("Player " + name + " trying to connect but lobby is full.");
//        }
//
//        try {
//            Player player = model.getPlayerByName(name);
//            if (player.isDisconnected()) {
//                clientList.put(name, client);
//                client.joinLobby(lobbyCode);
//                model.getPlayerByName(name).setDisconnected(false);
//                broadcastExceptOne(new EPlayerJoined(name), name);
//                Logger.info("Lobby " + getLobbyCode() + " - "  + "Player " + name + " reconnected");
//            } else {
//                client.asyncSend(new Message(Messages.NAME_NOT_AVAILABLE));
//                Logger.warning("Lobby " + getLobbyCode() + " - "  + "Player " + name + " trying to connect but there is already a player with that name connected.");
//            }
//        } catch (PlayerNotFoundException e) {
//            Logger.info("Lobby " + getLobbyCode() + " - "  + name + " joined lobby");
//            clientList.put(name, client);
//            client.joinLobby(lobbyCode);
//            client.asyncSend(new ELobbyJoined(lobbyCode));
//            broadcastExceptOne(new EPlayerJoined(name), name);
//            client.asyncSend(new EChooseWizard(new ArrayList<>(availableWizards)));
//        }
    }

    public void removeClientFromLobbyByName(String name) {
        clientList.remove(name);

        try {
            Player player = model.getPlayerByName(name);
            player.setDisconnected(true);
        } catch (PlayerNotFoundException e) {
            Logger.severe(e.getMessage());
        }
    }

    public ClientSocketConnection getClientByName(String name) {
        return clientList.get(name);
    }

    public String getClientBySocket(ClientSocketConnection clientSocket) {
        for (Map.Entry<String, ClientSocketConnection> client : clientList.entrySet()) {
            if (client.getValue().equals(clientSocket)) {
                return client.getKey();
            }
        }
        return null;
    }

    // TODO: rename this section

    private void setupPreGame() {

    }

    // Handlers

    public boolean playerHasChosenWizard(EWizardChosen event, ClientSocketConnection client) {
        Wizard choice = event.getWizard();

        if (!availableWizards.contains(choice)) {
            client.asyncSend(new EWizardNotAvailable(availableWizards));
            return true;
        }

        String playerName = getClientBySocket(client);
        Logger.info("Lobby " + getLobbyCode() + " - Adding " + playerName + " [" + choice + "] to board.");
        model.addPlayer(new Player(playerName, choice, TowerColor.BLACK));
        availableWizards.remove(choice);

        checkReadyPlayers();
        return true;
    }

    public boolean playerHasActivatedEffect(EUseCharacterEffect event, ClientSocketConnection client) {
        if (currentGameState == GameState.GAME_OVER || currentGameState == GameState.PLANNING) {
            client.asyncSend(new Message(Messages.WRONG_PHASE));
            return true;
        }

        if (model.getActiveCharacterEffect() != null) {
            client.asyncSend(new Message(Messages.ANOTHER_EFFECT_IS_ACTIVE));
            return true;
        }

        switch (event.getCharacterType()) {
            case POSTMAN -> {
                CharacterCard card = model.getCharacterByType(CharacterType.POSTMAN);
                CoinSupply playerCoinSupply = currentPlayer.getSchoolBoard().getCoinSupply();

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.asyncSend(new Message(Messages.INSUFFICIENT_COINS));
                    return true;
                }

                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.postmanEffect(this);
                model.setActiveCharacterEffect(CharacterType.POSTMAN);
                motherNatureMovement = new PostmanMotherNatureMovement();
            }
        }

        return true;
    }

    // TODO: rename block of functions with something more accurate
    // Other functions

    private void checkReadyPlayers() {
        if (model.getPlayerSize() == maxPlayers) {
            broadcast(new Message(Messages.GAME_STARTED));
        }
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public void setGameState(GameState nextGameState) {
        this.currentGameState = nextGameState;
        currentPlayer = (currentGameState == GameState.PLANNING ? planningPhaseOrder : actionPhaseOrder).get(0);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public boolean setNextPlayer() {
        currentPlayer = getNextPlayer();
        turnProgress++;

        model.setActiveCharacterEffect(null);
        return currentPlayer != null;
    }

    public int getStudentsMoved() {
        return studentsMoved;
    }

    public void addStudentsMoved() {
        this.studentsMoved += 1;
    }

    public void resetStudentsMoved() {
        this.studentsMoved = 0;
    }

    public int getMaxStudentsMoved() {
        return model.getMaxNumOfPlayers() + 1;
    }

    public Player getNextPlayer() {
        List<Player> turns = currentGameState == GameState.PLANNING ?
                planningPhaseOrder :
                actionPhaseOrder;
        if (isLastPlayer(turns)) return null;
        else return turns.get(turns.indexOf(currentPlayer) + 1);
    }

    public List<Player> getOrder() {
        return currentGameState == GameState.PLANNING ? planningPhaseOrder : actionPhaseOrder;
    }

    public boolean isLastPlayer(List<Player> players) {
        int index = players.indexOf(currentPlayer);
        return index == players.size() - 1;
    }

    public void nextTurn() {
        // Reset defaults before turn start
        model.setActiveCharacterEffect(null);
        studentMovement = new DefaultStudentMovement();
        motherNatureMovement = new DefaultMotherNatureMovement();
        resolveIsland = new DefaultResolveIsland();

        // CharacterCards.resetEffect() happens in setNextPlayer, always called before nextTurn
        setGameState(GameState.PLANNING);
        planningPhaseOrder = nextPlanningPhaseOrder;
        setCurrentPlayer(planningPhaseOrder.get(0));
        nextPlanningPhaseOrder = null;
        actionPhaseOrder = null;
        turnCounter++;
        turnProgress = 1;
    }

    public GameModel getModel() {
        return model;
    }

    public void setOrder(List<Player> actionOrder) {
        actionPhaseOrder = new ArrayList<>(actionOrder);
        nextPlanningPhaseOrder = new ArrayList<>(actionOrder);
        Collections.reverse(nextPlanningPhaseOrder);
    }

    public boolean wrongState(GameState gameState) {
        return !this.getCurrentGameState().equals(gameState);
    }

    public boolean wrongPlayer(Player player) {
        return !this.getCurrentPlayer().equals(player);
    }

    public ResolveIsland getResolveIsland() {
        return resolveIsland;
    }

    public GameOver getGameOver() {
        return gameOver;
    }

    // TODO : in the event dispatcher that calls knightEffect, centaurEffect, mushroomFanaticEffect create a
    //  new ResolveIsland of the specific Type.
    // TODO : Do the same exact thing for StudentMovement and MotherNatureMovement.
}
