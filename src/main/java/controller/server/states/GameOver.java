package controller.server.states;

import controller.server.GameLobby;
import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.board.SchoolBoard;
import util.GameState;

public class GameOver {

    /**
     * Determine the player with less towers or more professors on school board to declare winner
     *
     * @param tempLobby current game
     */
    // TODO: still does not performs any action to declare the winner
    public void selectWinner(GameLobby tempLobby) {
        try {
            GameModel tempGame = tempLobby.getModel();

            int playerId, numOfTowers = 100, numOfProfessor = 0;

            for (int i = 0; i < tempGame.getPlayers().size(); i++) {
                SchoolBoard tempSchoolBoard = tempGame.getPlayerByID(i).getSchoolBoard();
                if (tempSchoolBoard.getTowers().size() < numOfTowers || (tempSchoolBoard.getTowers().size() == numOfTowers && tempSchoolBoard.getProfessors().size() > numOfProfessor)) {
                    numOfTowers = tempSchoolBoard.getTowers().size();
                    numOfProfessor = tempSchoolBoard.getProfessors().size();
                    playerId = i;
                }
            }

            tempLobby.setGameState(GameState.GAME_OVER);
            // TODO : create a function that broadcast the player indicised by playerId

        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }
    }
}
