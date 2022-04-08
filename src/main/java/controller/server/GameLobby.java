package controller.server;

import controller.server.states.*;
import events.Event;
import events.EventDispatcher;
import events.EventType;
import events.types.Messages;
import events.types.clientToServer.*;
import events.types.serverToClient.*;
import events.types.serverToClient.Message;
import exceptions.CharacterCardNotFound;
import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.Player;
import network.server.ClientSocketConnection;
import observer.NetworkObserver;
import util.*;

import java.util.*;

public class GameLobby implements NetworkObserver {

    private final String lobbyCode;
    private final int maxPlayers;
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
    private final TurnEpilogue epilogue;
    private final StudentMovement studentMovement;
    private final ResolveIsland resolveIsland;
    private final PlanningPhase planningPhase;
    private final MotherNatureMovement motherNatureMovement;
    private final GameOver gameOver;
    private final CharacterEffectHandler characterEffectHandler;


    public GameLobby(int numOfPlayers, GameMode gameMode, String code) {
        this.lobbyCode = code;
        this.maxPlayers = numOfPlayers;
        this.gameMode = gameMode;

        this.clientList = new HashMap<>();
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
        this.studentMovement = new StudentMovement();
        this.resolveIsland = new ResolveIsland();
        this.planningPhase = new PlanningPhase();
        this.motherNatureMovement = new MotherNatureMovement();
        this.gameOver = new GameOver();
        this.characterEffectHandler = new CharacterEffectHandler();
    }

    @Override
    public void onNetworkEvent(Tuple<Event, ClientSocketConnection> event) {
        EventDispatcher dp = new EventDispatcher(event);

        dp.dispatch(EventType.WIZARD_CHOSEN, (Tuple<Event, ClientSocketConnection> t) -> playerHasChosenWizard((EWizardChosen) t.getKey(), t.getValue()));

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

    public String getLobbyCode() {
        return lobbyCode;
    }

    public void addClientToLobby(String name, ClientSocketConnection client) {
        if (model.getPlayerSize() >= maxPlayers) {
            client.asyncSend(new Message(Messages.LOBBY_FULL));
            Logger.info("Player " + name + " trying to connect but lobby is full.");
        }

        try {
            Player player = model.getPlayerByName(name);
            if (player.isDisconnected()) {
                clientList.put(name, client);
                client.joinLobby(lobbyCode);
                model.getPlayerByName(name).setDisconnected(false);
                broadcastExceptOne(new EPlayerJoined(name), name);
                Logger.info("Player " + name + " reconnected to Lobby " + getLobbyCode());
            } else {
                client.asyncSend(new Message(Messages.NAME_NOT_AVAILABLE));
                Logger.info("Player " + name + " trying to connect but lobby there is already a player with that name connected.");
            }
        } catch (PlayerNotFoundException e) {
            clientList.put(name, client);
            client.joinLobby(lobbyCode);
            client.asyncSend(new ELobbyJoined(lobbyCode));
            broadcastExceptOne(new EPlayerJoined(name), name);
            client.asyncSend(new EChooseWizard(new ArrayList<>(availableWizards)));
        }


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

    public boolean playerHasChosenWizard(EWizardChosen event, ClientSocketConnection client) {
        Wizard choice = event.getWizard();

        if (!availableWizards.contains(choice)) {
            client.asyncSend(new EWizardNotAvailable(availableWizards));
            return true;
        }

        String playerName = getClientBySocket(client);
        Logger.debug("Lobby " + getLobbyCode() + " - Adding " + playerName + " [" + choice + "] to board.");
        model.addPlayer(new Player(playerName, choice, TowerColor.BLACK));
        availableWizards.remove(choice);

        checkReadyPlayers();
        return true;
    }

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
        try {
            for (int i = 0; i < getModel().getCharacters().size(); i++)
                this.getModel().getCharacterById(i).resetEffect();
        } catch (CharacterCardNotFound characterCardNotFound) {
            assert false;   // should never happen
        }
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
}
