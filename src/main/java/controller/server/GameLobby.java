package controller.server;

import controller.server.states.*;
import eventSystem.EventListener;
import eventSystem.annotations.EventHandler;
import eventSystem.events.Event;
import eventSystem.events.network.Messages;
import eventSystem.events.network.client.*;
import eventSystem.events.network.client.actionPhaseRelated.EMoveMotherNature;
import eventSystem.events.network.client.actionPhaseRelated.EStudentMovementToDining;
import eventSystem.events.network.client.actionPhaseRelated.EStudentMovementToIsland;
import eventSystem.events.network.server.*;
import eventSystem.events.network.server.gameStateEvents.*;
import exceptions.IllegalMovementException;
import exceptions.PlayerNotFoundException;
import exceptions.supplyEmptyException;
import model.GameModel;
import model.Player;
import model.expert.CharacterCard;
import model.expert.CoinSupply;
import network.server.ClientSocketConnection;
import network.server.Server;
import util.*;

import java.util.*;

public class GameLobby implements EventListener {
    private final Server server;

    private final String lobbyCode;
    private final int maxPlayers;

    private LobbyState lobbyState;

    private final GameMode gameMode;
    private final Map<String, ClientSocketConnection> clientList;

    private final List<Wizard> availableWizards;
    private final Stack<TowerColor> availableTowerColors;

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
    public GameLobby(int numOfPlayers, GameMode gameMode, String code, Server server) {
        this.server = server;

        this.lobbyCode = code;
        this.maxPlayers = numOfPlayers;
        this.gameMode = gameMode;
        this.clientList = new HashMap<>();

        this.currentGameState = GameState.SETUP;
        setLobbyState(LobbyState.INIT);

        this.model = new GameModel(maxPlayers, this.gameMode);
        this.availableWizards = new ArrayList<>(Arrays.asList(Wizard.values()));
        this.availableTowerColors = new Stack<>();
        for (TowerColor color : TowerColor.values()) availableTowerColors.push(color);

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

    /**
     * Broadcast Event to all clients connected to lobby
     *
     * @param event Event to broadcast
     */
    public void broadcast(Event event) {
        for (Map.Entry<String, ClientSocketConnection> entry : clientList.entrySet()) {
            ClientSocketConnection client = entry.getValue();
            client.send(event);
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
                client.send(event);
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

        client.send(new ELobbyJoined(lobbyCode));
        broadcastExceptOne(new EPlayerJoined(name), name);

        if (clientList.size() == maxPlayers) {
            broadcast(new ServerMessage(Messages.ALL_CLIENTS_CONNECTED));
            setLobbyState(LobbyState.PRE_GAME);
            Logger.debug(getLobbyCode() + " - " + "All clients connected. Switching state to " + getLobbyState());
            setupPreGame();
        }

        // TODO: Resilience to disconnections

//        if (model.getPlayerSize() >= maxPlayers) {
//            client.send(new Message(Messages.LOBBY_FULL));
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
//                client.send(new Message(Messages.NAME_NOT_AVAILABLE));
//                Logger.warning("Lobby " + getLobbyCode() + " - "  + "Player " + name + " trying to connect but there is already a player with that name connected.");
//            }
//        } catch (PlayerNotFoundException e) {
//            Logger.info("Lobby " + getLobbyCode() + " - "  + name + " joined lobby");
//            clientList.put(name, client);
//            client.joinLobby(lobbyCode);
//            client.send(new ELobbyJoined(lobbyCode));
//            broadcastExceptOne(new EPlayerJoined(name), name);
//            client.send(new EChooseWizard(new ArrayList<>(availableWizards)));
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
     * Inverse of {@link GameLobby#getPlayerNameBySocket(ClientSocketConnection)}
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
    public String getPlayerNameBySocket(ClientSocketConnection clientSocket) {
        for (Map.Entry<String, ClientSocketConnection> entry : clientList.entrySet()) {
            if (entry.getValue().equals(clientSocket)) {
                return entry.getKey();
            }
        }

        return null;
    }

    // Event Senders:

    private void setupPreGame() {
        ClientSocketConnection currentClient = null;
        for (ClientSocketConnection client : clientList.values()) {
            if (!client.isReady()) {
                currentClient = client;
                break;
            }
        }

        // All players have chosen wizard. STATE -> IN_GAME
        if (currentClient == null) {
            setLobbyState(LobbyState.IN_GAME);
            Logger.debug(getLobbyCode() + " - " + "All players are ready. Switching state to " + getLobbyState());
            broadcast(new ServerMessage(Messages.GAME_STARTED));

            setGameState(GameState.PLANNING);
            broadcast(new EUpdateGameState(getCurrentGameState()));

            planningPhase.refillEmptyClouds(this);

            if (gameMode == GameMode.EXPERT) {
                broadcast(new ELightModelSetup(model.getCharacters()));
                broadcast(new EUpdateCharacterEffect(model.getActiveCharacterEffect()));
            }

            for (Player player : model.getPlayers()) {
                broadcast(new EUpdateSchoolBoard(player.getSchoolBoard(), player.getName()));
            }

            broadcast(new EUpdateCloudTiles(model.getCloudTiles()));

            broadcast(new EUpdateIslands(model.getIslandGroups(), model.getMotherNature().getPosition()));

            for (Map.Entry<String, ClientSocketConnection> entry : clientList.entrySet()) {
                ClientSocketConnection client = entry.getValue();
                try {
                    client.send(new EUpdateAssistantDeck(model.getPlayerByName(entry.getKey()).getAssistants()));
                } catch (PlayerNotFoundException e) {
                    Logger.error(e.getMessage());
                }
            }

            // TODO: Each update is called after respective element is modified

            broadcast(new ServerMessage(Messages.UPDATE_VIEW));
            setGameState(GameState.PLANNING);

            // First turn starts now
            sendChooseAssistantEvent();

            return;
        }

        currentClient.send(new EChooseWizard(availableWizards));
        String currentPlayerName = getPlayerNameBySocket(currentClient);
        broadcastExceptOne(new EPlayerChoosing(currentPlayerName, ChoiceType.WIZARD), currentPlayerName);
    }

    private void sendChooseAssistantEvent() {
        ClientSocketConnection currentPlayerClient = clientList.get(currentPlayer.getName());
        currentPlayerClient.send(new ServerMessage(Messages.CHOOSE_ASSISTANT));
        broadcastExceptOne(new EPlayerChoosing(currentPlayer.getName(), ChoiceType.ASSISTANT), currentPlayer.getName());
    }

    private void sendStartTurn() {
        ClientSocketConnection currentPlayerClient = clientList.get(currentPlayer.getName());
        currentPlayerClient.send(new ServerMessage(Messages.START_TURN));
        broadcastExceptOne(new EPlayerTurnStarted(currentPlayer.getName()), currentPlayer.getName());
    }

    private void sendContinueTurn() {
        ClientSocketConnection currentPlayerClient = clientList.get(currentPlayer.getName());
        currentPlayerClient.send(new ServerMessage(Messages.CONTINUE_TURN));
    }

    /**
     * Add player to the current game with the selected wizard back if available
     *
     * @param event event to react to
     * @return true
     */
    @EventHandler
    public boolean playerHasChosenWizard(EWizardChosen event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        Wizard choice = event.getWizard();

        String playerName = getPlayerNameBySocket(client);
        Logger.info(getLobbyCode() + " - Adding " + playerName + " [" + choice + "] to board.");

        TowerColor color = availableTowerColors.pop();
        Player player = new Player(playerName, choice, color, model.getGameMode());
        model.addPlayer(player);
        planningPhaseOrder.add(player);
        availableWizards.remove(choice);

        client.setReady();
        setupPreGame();
        return true;
    }

    @EventHandler
    public boolean playerHasChosenAssistant(EAssistantChosen event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        try {
            planningPhase.playCard(this, model.getPlayerByName(getPlayerNameBySocket(client)), event.getAssistant(), client);
        } catch (Exception others) {
            others.printStackTrace();
        }
        if (currentGameState == GameState.PLANNING) {
            sendChooseAssistantEvent();
        } else {
            broadcast(new EUpdateGameState(getCurrentGameState()));
            sendStartTurn();
        }
        return true;
    }

    /**
     * When a player activate a character effect check its status as active for this turn if it's a passive character,
     * performs respective actions when an active character is activated
     *
     * @param event event to react to
     * @return true
     */
    @EventHandler
    public boolean playerHasActivatedEffect(EUseCharacterEffect event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        if (effectActivationCheck(client)) return true;

        CoinSupply playerCoinSupply = currentPlayer.getSchoolBoard().getCoinSupply();

        switch (event.getCharacterType()) {
            case FARMER -> {
                CharacterCard card = model.getCharacterByType(CharacterType.FARMER);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.send(new ServerMessage(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.farmerEffect(this);
                model.setActiveCharacterEffect(CharacterType.FARMER);
                studentMovement = new FarmerStudentMovement();
            }
            case POSTMAN -> {
                CharacterCard card = model.getCharacterByType(CharacterType.POSTMAN);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.send(new ServerMessage(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.postmanEffect(this);
                model.setActiveCharacterEffect(CharacterType.POSTMAN);
                motherNatureMovement = new PostmanMotherNatureMovement();
            }
            case CENTAUR -> {
                CharacterCard card = model.getCharacterByType(CharacterType.CENTAUR);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.send(new ServerMessage(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.centaurEffect(this);
                model.setActiveCharacterEffect(CharacterType.CENTAUR);
                resolveIsland = new CentaurResolveIsland();
            }
            case KNIGHT -> {
                CharacterCard card = model.getCharacterByType(CharacterType.KNIGHT);

                try {
                    playerCoinSupply.removeCoins(card.getCost());
                } catch (supplyEmptyException e) {
                    client.send(new ServerMessage(Messages.INSUFFICIENT_COINS));
                    return true;
                }
                model.getCoinSupply().addCoins(card.getCost());

                characterEffectHandler.knightEffect(this);
                model.setActiveCharacterEffect(CharacterType.KNIGHT);
                resolveIsland = new KnightResolveIsland();
            }
            default -> {
                Logger.warning("wrong Effect type");
            }
        }
        client.send(new ServerMessage(Messages.EFFECT_USED));
        return true;
    }

    @EventHandler
    public boolean playerHasActivatedEffect(EUseFanaticEffect event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        if (effectActivationCheck(client)) return true;

        CoinSupply playerCoinSupply = currentPlayer.getSchoolBoard().getCoinSupply();

        CharacterCard card = model.getCharacterByType(CharacterType.MUSHROOM_FANATIC);

        try {
            playerCoinSupply.removeCoins(card.getCost());
        } catch (supplyEmptyException e) {
            client.send(new ServerMessage(Messages.INSUFFICIENT_COINS));
            return true;
        }
        model.getCoinSupply().addCoins(card.getCost());

        characterEffectHandler.mushroomFanaticEffect(this, event.getColor());
        model.setActiveCharacterEffect(CharacterType.MUSHROOM_FANATIC);
        resolveIsland = new MushroomFanaticResolveIsland();

        client.send(new ServerMessage(Messages.EFFECT_USED));
        return true;
    }

    @EventHandler
    public boolean playerHasActivatedEffect(EUseGrannyEffect event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        if (effectActivationCheck(client)) return true;

        CoinSupply playerCoinSupply = currentPlayer.getSchoolBoard().getCoinSupply();

        CharacterCard card = model.getCharacterByType(CharacterType.GRANNY_HERBS);

        try {
            playerCoinSupply.removeCoins(card.getCost());
        } catch (supplyEmptyException e) {
            client.send(new ServerMessage(Messages.INSUFFICIENT_COINS));
            return true;
        }
        model.getCoinSupply().addCoins(card.getCost());

        characterEffectHandler.grannyHerbsEffect(this, event.getIslandID());
        model.setActiveCharacterEffect(CharacterType.GRANNY_HERBS);

        client.send(new ServerMessage(Messages.EFFECT_USED));
        return true;
    }

    @EventHandler
    public boolean playerHasActivatedEffect(EUseHeraldEffect event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        if (effectActivationCheck(client)) return true;

        CoinSupply playerCoinSupply = currentPlayer.getSchoolBoard().getCoinSupply();

        CharacterCard card = model.getCharacterByType(CharacterType.HERALD);

        try {
            playerCoinSupply.removeCoins(card.getCost());
        } catch (supplyEmptyException e) {
            client.send(new ServerMessage(Messages.INSUFFICIENT_COINS));
            return true;
        }
        model.getCoinSupply().addCoins(card.getCost());

        characterEffectHandler.heraldEffect(this, event.getIslandGroupID());
        model.setActiveCharacterEffect(CharacterType.HERALD);

        client.send(new ServerMessage(Messages.EFFECT_USED));
        return true;
    }

    @EventHandler
    public boolean playerHasActivatedEffect(EUseJesterEffect event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        if (effectActivationCheck(client)) return true;

        CoinSupply playerCoinSupply = currentPlayer.getSchoolBoard().getCoinSupply();

        CharacterCard card = model.getCharacterByType(CharacterType.JESTER);

        try {
            playerCoinSupply.removeCoins(card.getCost());
        } catch (supplyEmptyException e) {
            client.send(new ServerMessage(Messages.INSUFFICIENT_COINS));
            return true;
        }
        model.getCoinSupply().addCoins(card.getCost());

        characterEffectHandler.jesterEffect(this, event.getEntranceStudents(), event.getJesterStudents());
        model.setActiveCharacterEffect(CharacterType.JESTER);

        client.send(new ServerMessage(Messages.EFFECT_USED));
        return true;
    }

    @EventHandler
    public boolean playerHasActivatedEffect(EUseMinstrelEffect event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        if (effectActivationCheck(client)) return true;

        CoinSupply playerCoinSupply = currentPlayer.getSchoolBoard().getCoinSupply();

        CharacterCard card = model.getCharacterByType(CharacterType.MINSTREL);

        try {
            playerCoinSupply.removeCoins(card.getCost());
        } catch (supplyEmptyException e) {
            client.send(new ServerMessage(Messages.INSUFFICIENT_COINS));
            return true;
        }
        model.getCoinSupply().addCoins(card.getCost());

        characterEffectHandler.minstrelEffect(this, event.getEntranceStudents(), event.getDiningStudents());
        model.setActiveCharacterEffect(CharacterType.MINSTREL);

        client.send(new ServerMessage(Messages.EFFECT_USED));
        return true;
    }

    @EventHandler
    public boolean playerHasActivatedEffect(EUseMonkEffect event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        if (effectActivationCheck(client)) return true;

        CoinSupply playerCoinSupply = currentPlayer.getSchoolBoard().getCoinSupply();

        CharacterCard card = model.getCharacterByType(CharacterType.MONK);

        try {
            playerCoinSupply.removeCoins(card.getCost());
        } catch (supplyEmptyException e) {
            client.send(new ServerMessage(Messages.INSUFFICIENT_COINS));
            return true;
        }
        model.getCoinSupply().addCoins(card.getCost());

        characterEffectHandler.monkEffect(this, event.getStudentID(), event.getIslandPos());
        model.setActiveCharacterEffect(CharacterType.MONK);

        client.send(new ServerMessage(Messages.EFFECT_USED));
        return true;
    }

    @EventHandler
    public boolean playerHasActivatedEffect(EUsePrincessEffect event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        if (effectActivationCheck(client)) return true;

        CoinSupply playerCoinSupply = currentPlayer.getSchoolBoard().getCoinSupply();

        CharacterCard card = model.getCharacterByType(CharacterType.PRINCESS);

        try {
            playerCoinSupply.removeCoins(card.getCost());
        } catch (supplyEmptyException e) {
            client.send(new ServerMessage(Messages.INSUFFICIENT_COINS));
            return true;
        }
        model.getCoinSupply().addCoins(card.getCost());

        characterEffectHandler.princessEffect(this, event.getStudentID());
        model.setActiveCharacterEffect(CharacterType.PRINCESS);

        client.send(new ServerMessage(Messages.EFFECT_USED));
        return true;
    }

    @EventHandler
    public boolean playerHasActivatedEffect(EUseThiefEffect event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        if (effectActivationCheck(client)) return true;

        CoinSupply playerCoinSupply = currentPlayer.getSchoolBoard().getCoinSupply();

        CharacterCard card = model.getCharacterByType(CharacterType.THIEF);

        try {
            playerCoinSupply.removeCoins(card.getCost());
        } catch (supplyEmptyException e) {
            client.send(new ServerMessage(Messages.INSUFFICIENT_COINS));
            return true;
        }
        model.getCoinSupply().addCoins(card.getCost());

        characterEffectHandler.thiefEffect(this, event.getColor());
        model.setActiveCharacterEffect(CharacterType.THIEF);

        client.send(new ServerMessage(Messages.EFFECT_USED));
        return true;
    }

    private boolean effectActivationCheck(ClientSocketConnection client) {
        if (currentGameState == GameState.GAME_OVER || currentGameState == GameState.PLANNING) {
            client.send(new ServerMessage(Messages.WRONG_PHASE));
            return true;
        }
        if (model.getActiveCharacterEffect() != null) {
            client.send(new ServerMessage(Messages.ANOTHER_EFFECT_IS_ACTIVE));
            return true;
        }
        return false;
    }

    @EventHandler
    public boolean playerHasMovedToDining(EStudentMovementToDining event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        try {
            studentMovement.moveStudentToDining(this, model.getPlayerByName(getPlayerNameBySocket(client)), event.getStudentID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (currentGameState != GameState.STUDENT_MOVEMENT) {
            broadcast(new EUpdateGameState(getCurrentGameState()));
        }
        sendContinueTurn();
        return true;
    }

    @EventHandler
    public boolean playerHasMovedToIsland(EStudentMovementToIsland event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        try {
            studentMovement.moveStudentToIsland(this, model.getPlayerByName(getPlayerNameBySocket(client)), event.getStudentID(), event.getIslandID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (currentGameState != GameState.STUDENT_MOVEMENT) {
            broadcast(new EUpdateGameState(getCurrentGameState()));
        }
        sendContinueTurn();
        return true;
    }

    @EventHandler
    public boolean playerHasMovedMotherNature(EMoveMotherNature event) {
        UUID clientId = event.getClientId();
        ClientSocketConnection client = server.getClientById(clientId);

        try {
            motherNatureMovement.moveMotherNature(this, event.getSteps());
        } catch (IllegalMovementException e) {
            client.send(new ServerMessage(Messages.ILLEGAL_STEPS));
            sendContinueTurn();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        broadcast(new EUpdateIslands(model.getIslandGroups(), model.getMotherNature().getPosition()));
        broadcast(new EUpdateSchoolBoard(getCurrentPlayer().getSchoolBoard(), getCurrentPlayer().getName()));

        if (currentGameState != GameState.MOTHERNATURE_MOVEMENT) {
            broadcast(new EUpdateGameState(getCurrentGameState()));
            broadcast(new ServerMessage(Messages.UPDATE_VIEW));
            //TODO: add function that switches to turn epilogue
        }
        return true;
    }

    // State functions

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
        List<Player> turns = currentGameState == GameState.PLANNING ? planningPhaseOrder : actionPhaseOrder;
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
        planningPhaseOrder = new ArrayList<>(actionOrder);          // small fix to avoid errors
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
