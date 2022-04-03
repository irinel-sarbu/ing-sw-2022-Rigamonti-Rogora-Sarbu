package controller.server;

import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.Player;
import util.GameMode;
import util.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GameLobby {
    private final Logger logger = Logger.getLogger(GameLobby.class.getName());

    private int turnCounter = 0;
    private final String code;
    private final GameModel gameModel;
    private Player currentPlayer;
    private int turnProgress;
    private int studentsMoved;
    private GameState currentGameState;
    private List<Player> planningPhaseOrder;
    private List<Player> actionPhaseOrder;

    private List<Player> nextPlanningPhaseOrder;

    public GameLobby(int numOfPlayers, GameMode gameMode, String code) {
        this.code = code;
        this.gameModel = new GameModel(numOfPlayers, gameMode);
        try {
            this.currentPlayer = gameModel.getPlayerByID(0);
        } catch (PlayerNotFoundException e) {
            System.out.println("[INFO] Game lobby <" + code + "> is empty");
        }
        this.studentsMoved = 0;
        this.currentGameState = GameState.SETUP;
        this.planningPhaseOrder = gameModel.getPlayers();
        this.actionPhaseOrder = null;
        this.nextPlanningPhaseOrder = null;
        this.turnProgress = 1;
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }

    public void setCurrentGameState(GameState currentGameState) {
        this.currentGameState = currentGameState;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public void setNextPlayer() {
        currentPlayer = getNextPlayer();
        turnProgress++;
    }

    public int getStudentsMoved() {
        return studentsMoved;
    }

    public void setStudentsMoved(int studentsMoved) {
        this.studentsMoved = studentsMoved;
    }

    public void resetStudentsMoved() {
        this.studentsMoved = 0;
    }

    public int getMaxStudentMovements() {
        return gameModel.getNumOfPlayers() + 1;
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
        planningPhaseOrder = nextPlanningPhaseOrder;
        currentPlayer = planningPhaseOrder.get(0);
        nextPlanningPhaseOrder = null;
        turnCounter++;
        turnProgress = 1;
    }

    public GameModel getModel() {
        return gameModel;
    }
}
