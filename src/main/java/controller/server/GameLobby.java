package controller.server;

import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.Player;
import util.GameMode;
import util.GameState;

import java.util.List;
import java.util.logging.Logger;

public class GameLobby {
    private final Logger logger = Logger.getLogger(GameLobby.class.getName());

    private final String code;
    private final GameModel gameModel;
    private Player currentPlayer;
    private int studentsMoved;
    private GameState currentGameState;
    private List<Player> planningPhaseOrder;
    private List<Player> actionPhaseOrder;

    public GameLobby(int numOfPlayers, GameMode gameMode, String code) {
        this.code = code;
        this.gameModel = new GameModel(numOfPlayers, gameMode);
        try {
            this.currentPlayer = gameModel.getPlayerByID(0);
        } catch (PlayerNotFoundException e) {
            logger.warning("Game lobby <" + code + "> is empty");
        }
        this.studentsMoved = 0;
        this.currentGameState = GameState.SETUP;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
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
}
