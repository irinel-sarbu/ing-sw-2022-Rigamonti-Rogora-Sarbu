package controller.server;

import controller.server.states.*;
import events.ChoiceType;
import events.Event;
import events.EventDispatcher;
import events.EventType;
import events.types.Messages;
import events.types.clientToServer.*;
import events.types.serverToClient.*;
import events.types.serverToClient.gameStateEvents.EUpdateCloudTiles;
import events.types.serverToClient.gameStateEvents.EUpdateIslands;
import events.types.serverToClient.gameStateEvents.EUpdateSchoolBoard;
import exceptions.PlayerNotFoundException;
import exceptions.supplyEmptyException;
import model.GameModel;
import model.Player;
import model.board.CloudTile;
import model.board.IslandGroup;
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

        this.currentGameState = GameState.SETUP;
        setLobbyState(LobbyState.INIT);

        this.model = new GameModel(maxPlayers, this.gameMode);
        this.availableWizards = new ArrayList<>(Arrays.asList(Wizard.values()));

        // TODO: ERROR At this point there are 0 players in lobby -> move to create lobby function
        this.studentsMoved = 0;
        this.currentGameState = GameState.SETUP;
        this.planningPhaseOrder = new ArrayList<>();
        this.actionPhaseOrder = null;
        this.nextPlanningPhaseOrder = null;
        this.turnProgress = 1;

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
        switch (lobbyState) {
            case INIT -> initState(networkEvent);
            case PRE_GAME -> preGameState(networkEvent);
            case IN_GAME -> inGameState(networkEvent);
            case END -> endGameState(networkEvent);
        }
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

        // TODO: i think this can be deleted - irinel
    }

    private void preGameState(Tuple<Event, ClientSocketConnection> networkEvent) {
        EventDispatcher preGameDispatcher = new EventDispatcher(networkEvent);
        preGameDispatcher.dispatch(EventType.WIZARD_CHOSEN, (Tuple<Event, ClientSocketConnection> t) -> playerHasChosenWizard((EWizardChosen) t.getKey(), t.getValue()));

    }

    private void inGameState(Tuple<Event, ClientSocketConnection> networkEvent) {
        EventDispatcher dp = new EventDispatcher(networkEvent);
        dp.dispatch(EventType.USE_CHARACTER_EFFECT, (Tuple<Event, ClientSocketConnection> t) -> playerHasActivatedEffect((EUseCharacterEffect) t.getKey(), t.getValue()));
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
     *
     * @return a 5 characters alphanumeric string representing the game lobby
     */
    public String getLobbyCode() {
        return lobbyCode;
    }

    /**
     * Add a client to this game lobby with the specified name
     *
     * @param name   nickname of the player to add to the game lobby
     * @param client reference to the client connection to attach to this lobby
     */
    public void addClientToLobby(String name, ClientSocketConnection client) {
        clientList.put(name, client);
        client.joinLobby(getLobbyCode());

        client.asyncSend(new ELobbyJoined(lobbyCode));
        broadcastExceptOne(new EPlayerJoined(name), name);

        if (clientList.size() == maxPlayers) {
            broadcast(new Message(Messages.ALL_CLIENTS_CONNECTED));
            setLobbyState(LobbyState.PRE_GAME);
            Logger.debug(getLobbyCode() + " - " + "All clients connected. Switching state to " + getLobbyState());
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

    /**
     * Remove a client from the current lobby, using it's nickname
     *
     * @param name nickname of the player to remove
     */
    public void removeClientFromLobbyByName(String name) {
        clientList.remove(name);

        try {
            Player player = model.getPlayerByName(name);
            player.setDisconnected(true);
        } catch (PlayerNotFoundException e) {
            Logger.severe(e.getMessage());
        }
    }

    /**
     * Get client by name
     * Inverse of {@link GameLobby#getClientBySocket(ClientSocketConnection)}
     *
     * @param name player nickname to search for
     * @return instance of the client associated to the specified name
     */
    public ClientSocketConnection getClientByName(String name) {
        return clientList.get(name);
    }

    /**
     * Get nickname by connection
     * Inverse of {@link GameLobby#getClientByName(String)}
     *
     * @param clientSocket socket to search for associated name
     * @return nickname of the player associated to the socket
     */
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
        ClientSocketConnection currentClient = null;
        for(ClientSocketConnection client : clientList.values()) {
            if (!client.isReady()) {
                currentClient = client;
                break;
            }
        }

        if(currentClient == null) {
            setLobbyState(LobbyState.IN_GAME);
            Logger.debug(getLobbyCode() + " - " + "All players are ready. Switching state to " + getLobbyState());
            broadcast(new Message(Messages.GAME_STARTED));

            setGameState(GameState.PLANNING);
            planningPhase.refillEmptyClouds(this);

            for (Player player : model.getPlayers()) {
                broadcast(new EUpdateSchoolBoard(player.getSchoolBoard(), player.getName()));
            }
            List<CloudTile> cloudTiles = new ArrayList<>();
            for (int id = 0; id < model.getNumOfCloudTiles(); id++) {
                cloudTiles.add(model.getCloudTile(id));
            }
            broadcast(new EUpdateCloudTiles(cloudTiles));
            List<IslandGroup> islandGroups = new ArrayList<>();
            for (int id = 0; id < model.getRemainingIslandGroups(); id++) {
                islandGroups.add(model.getIslandGroupByID(id));
            }
            broadcast(new EUpdateIslands(islandGroups, model.getMotherNature().getPosition()));
            /* TODO:
             * Send to each client
             * - general
             *  1 - schoolBoards of all players
             *  2 - cloud tiles
             *  3 - islands
             *  4 - if EXPERT drawn characters
             *  5 - if EXPERT active character effect
             *
             * - different for each
             *  1 - assistant cards
             *
             * Implement update method for each element of the list
             * Each update is called after respective element is modified
             */


            return;
        }

        currentClient.asyncSend(new EChooseWizard(availableWizards));
        String currentPlayerName = getClientBySocket(currentClient);
        broadcastExceptOne(new EPlayerChoosing(currentPlayerName, ChoiceType.WIZARD), currentPlayerName);
    }

    // Handlers

    /**
     * Add player to the current game with the selected wizard back if available, otherwise respond with an {@link EWizardNotAvailable} event
     *
     * @param event  event to react to
     * @param client client sending the message
     * @return true
     */
    public boolean playerHasChosenWizard(EWizardChosen event, ClientSocketConnection client) {
        Wizard choice = event.getWizard();

        if (!availableWizards.contains(choice)) {
            client.asyncSend(new EWizardNotAvailable(availableWizards));
            return true;
        }

        String playerName = getClientBySocket(client);
        Logger.info(getLobbyCode() + " - Adding " + playerName + " [" + choice + "] to board.");
        // TODO: change tower color
        Player player = new Player(playerName, choice, TowerColor.BLACK);
        model.addPlayer(player);
        planningPhaseOrder.add(player);
        availableWizards.remove(choice);

        client.setReady();
        setupPreGame();
        return true;
    }

    /**
     * When a player activate a character effect check its status as active for this turn if it's a passive character,
     * performs respective actions when an active character is activated
     *
     * @param event  event to react to
     * @param client client sending the event
     * @return true
     */
    public boolean playerHasActivatedEffect(EUseCharacterEffect event, ClientSocketConnection client) {
        if (currentGameState == GameState.GAME_OVER || currentGameState == GameState.PLANNING) {
            client.asyncSend(new Message(Messages.WRONG_PHASE));
            return true;
        }

        if (model.getActiveCharacterEffect() != null) {
            client.asyncSend(new Message(Messages.ANOTHER_EFFECT_IS_ACTIVE));
            return true;
        }

        CoinSupply playerCoinSupply = currentPlayer.getSchoolBoard().getCoinSupply();

        switch (event.getCharacterType()) {
            case MONK -> {
                //Casting of EUseCharacterEffect -> EUseMonkEffect, Objects which type is EUseMonkEffect have studentID and islandPos Attributes.
                EUseMonkEffect monkEvent = (EUseMonkEffect) event;

                CharacterCard card = model.getCharacterByType(CharacterType.MONK);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.asyncSend(new Message(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.monkEffect(this, monkEvent.getStudentID(), monkEvent.getIslandPos());
                model.setActiveCharacterEffect(CharacterType.MONK);
            }
            case FARMER -> {
                CharacterCard card = model.getCharacterByType(CharacterType.FARMER);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.asyncSend(new Message(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.farmerEffect(this);
                model.setActiveCharacterEffect(CharacterType.FARMER);
                studentMovement = new FarmerStudentMovement();
            }
            case HERALD -> {
                //Casting of EUseCharacterEffect -> EUseHeraldEffect
                EUseHeraldEffect heraldEvent = (EUseHeraldEffect) event;

                CharacterCard card = model.getCharacterByType(CharacterType.HERALD);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.asyncSend(new Message(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.heraldEffect(this, heraldEvent.getIslandGroupID());
                model.setActiveCharacterEffect(CharacterType.HERALD);
            }
            case POSTMAN -> {
                CharacterCard card = model.getCharacterByType(CharacterType.POSTMAN);

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
            case GRANNY_HERBS -> {
                //Casting of EUseCharacterEffect -> EUseGrannyEffect
                EUseGrannyEffect grannyEvent = (EUseGrannyEffect) event;

                CharacterCard card = model.getCharacterByType(CharacterType.GRANNY_HERBS);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.asyncSend(new Message(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.grannyHerbsEffect(this, grannyEvent.getIslandID());
                model.setActiveCharacterEffect(CharacterType.GRANNY_HERBS);
            }
            case CENTAUR -> {
                CharacterCard card = model.getCharacterByType(CharacterType.CENTAUR);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.asyncSend(new Message(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.centaurEffect(this);
                model.setActiveCharacterEffect(CharacterType.CENTAUR);
                resolveIsland = new CentaurResolveIsland();
            }
            case JESTER -> {
                //Casting of EUseCharacterEffect -> EUseJesterEffect
                EUseJesterEffect jesterEvent = (EUseJesterEffect) event;

                CharacterCard card = model.getCharacterByType(CharacterType.JESTER);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.asyncSend(new Message(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.jesterEffect(this, jesterEvent.getEntranceStudents(), jesterEvent.getJesterStudents());
                model.setActiveCharacterEffect(CharacterType.JESTER);
            }
            case MINSTREL -> {
                //Casting of EUseCharacterEffect -> EUseMinstrelEffect
                EUseMinstrelEffect minstrelEvent = (EUseMinstrelEffect) event;

                CharacterCard card = model.getCharacterByType(CharacterType.MINSTREL);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.asyncSend(new Message(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.minstrelEffect(this, minstrelEvent.getEntranceStudents(), minstrelEvent.getDiningStudents());
                model.setActiveCharacterEffect(CharacterType.MINSTREL);
            }
            case KNIGHT -> {
                CharacterCard card = model.getCharacterByType(CharacterType.KNIGHT);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.asyncSend(new Message(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.knightEffect(this);
                model.setActiveCharacterEffect(CharacterType.KNIGHT);
                resolveIsland = new KnightResolveIsland();
            }
            case PRINCESS -> {
                //Casting of EUseCharacterEffect -> EUsePrincessEffect
                EUsePrincessEffect princessEvent = (EUsePrincessEffect) event;

                CharacterCard card = model.getCharacterByType(CharacterType.PRINCESS);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.asyncSend(new Message(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.princessEffect(this, princessEvent.getStudentID());
                model.setActiveCharacterEffect(CharacterType.PRINCESS);
            }
            case MUSHROOM_FANATIC -> {
                //Casting of EUseCharacterEffect -> EUseFanaticEffect
                EUseFanaticEffect fanaticEvent = (EUseFanaticEffect) event;

                CharacterCard card = model.getCharacterByType(CharacterType.MUSHROOM_FANATIC);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.asyncSend(new Message(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.mushroomFanaticEffect(this, fanaticEvent.getColor());
                model.setActiveCharacterEffect(CharacterType.MUSHROOM_FANATIC);
                resolveIsland = new MushroomFanaticResolveIsland();
            }
            case THIEF -> {
                //Casting of EUseCharacterEffect -> EUseThiefEffect
                EUseThiefEffect thiefEvent = (EUseThiefEffect) event;

                CharacterCard card = model.getCharacterByType(CharacterType.THIEF);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.asyncSend(new Message(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.thiefEffect(this, thiefEvent.getColor());
                model.setActiveCharacterEffect(CharacterType.THIEF);
            }
        }
        client.asyncSend(new Message(Messages.EFFECT_USED));
        return true;
    }

    // TODO: rename block of functions with something more accurate
    // Other functions

    /**
     * Notify game start when the lobby is full
     */
    private void checkReadyPlayers() {
        if (model.getPlayerSize() == maxPlayers) {
            broadcast(new Message(Messages.GAME_STARTED));
        }
    }

    /**
     * Get the current game state
     *
     * @return a {@link GameState} reference to {@link GameLobby#currentGameState}
     */
    public GameState getCurrentGameState() {
        return currentGameState;
    }

    /**
     * Set the next game state, also reset current active player
     *
     * @param nextGameState the game state to enter
     */
    public void setGameState(GameState nextGameState) {
        this.currentGameState = nextGameState;
        currentPlayer = (currentGameState == GameState.PLANNING ? planningPhaseOrder : actionPhaseOrder).get(0);
    }

    /**
     * Get the only player who can act at this moment
     *
     * @return a {@link Player} reference to {@link GameLobby#currentPlayer}
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Set the player who can act
     *
     * @param player reference to the player who can act at this moment
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    /**
     * Move the control to the next player and deactivate all characters
     *
     * @return true if a next player exists in this game phase, false otherwise
     */
    public boolean setNextPlayer() {
        currentPlayer = getNextPlayer();
        turnProgress++;

        model.setActiveCharacterEffect(null);
        return currentPlayer != null;
    }

    /**
     * Get number of students moved in this phase
     *
     * @return {@link GameLobby#studentsMoved}
     */
    public int getStudentsMoved() {
        return studentsMoved;
    }

    /**
     * Increment the number of the moved students in this phase by 1
     */
    public void addStudentsMoved() {
        this.studentsMoved += 1;
    }

    /**
     * Reset to 0 the number of students moved in this phase
     */
    public void resetStudentsMoved() {
        this.studentsMoved = 0;
    }

    /**
     * Get the maximum number of students moved
     *
     * @return an integer representing the maximum amount of movable student by the current player during this phase
     */
    public int getMaxStudentsMoved() {
        return model.getMaxNumOfPlayers() + 1;
    }

    /**
     * Get the next acting player following current phase order
     *
     * @return a reference of type {@link Player} referring to the next acting player, null if there is no next player
     */
    public Player getNextPlayer() {
        List<Player> turns = currentGameState == GameState.PLANNING ?
                planningPhaseOrder :
                actionPhaseOrder;
        if (isLastPlayer(turns)) return null;
        else return turns.get(turns.indexOf(currentPlayer) + 1);
    }

    /**
     * Get order of the players in the current game phase
     *
     * @return a {@link List<Player>} containing all acting players in this phase in order
     */
    public List<Player> getOrder() {
        return currentGameState == GameState.PLANNING ? planningPhaseOrder : actionPhaseOrder;
    }

    /**
     * Set the playing order for the planning phase and the next action phase
     *
     * @param actionOrder A {@link List<Player>} containing the reference to the players in the action phase order
     */
    public void setOrder(List<Player> actionOrder) {
        actionPhaseOrder = new ArrayList<>(actionOrder);
        nextPlanningPhaseOrder = new ArrayList<>(actionOrder);
        Collections.reverse(nextPlanningPhaseOrder);
    }

    /**
     * Check if the current player is the last one in this phase
     *
     * @param players the player to check
     * @return true if the specified player is the last one, false otherwise
     */
    public boolean isLastPlayer(List<Player> players) {
        int index = players.indexOf(currentPlayer);
        return index == players.size() - 1;
    }

    /**
     * Performs all actions needed to switch to the next turn:
     * - deactivate all characters and reset default strategies
     * - reset game phase to planning and set the next planning order
     * - get the new firs player
     */
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

    /**
     * Get the {@link GameModel} associated to this lobby
     *
     * @return {@link GameLobby#model}
     */
    public GameModel getModel() {
        return model;
    }

    /**
     * Check if the command sent by the player is referring to the wrong phase
     *
     * @param gameState the {@link GameState} the game should be in to accept the command
     * @return true if the game is NOT in the specified state, false otherwise
     */
    public boolean wrongState(GameState gameState) {
        return !this.getCurrentGameState().equals(gameState);
    }

    /**
     * Check if the player who sent a command is the one who should act at the moment
     *
     * @param player a reference to the player who is sending the command
     * @return true if the command does NOT come from the expected player, false otherwise
     */
    public boolean wrongPlayer(Player player) {
        return !this.getCurrentPlayer().equals(player);
    }

    /**
     * The current strategy to resolve islands
     *
     * @return a reference to {@link ResolveIsland} pointing to the current strategy to resolve islands
     */
    public ResolveIsland getResolveIsland() {
        return resolveIsland;
    }

    /**
     * Get the game over instance
     *
     * @return a reference to this lobby game over instance
     */
    public GameOver getGameOver() {
        return gameOver;
    }
}
