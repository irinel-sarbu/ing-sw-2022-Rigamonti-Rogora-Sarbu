package controller.server.states;

import controller.server.GameController;
import controller.server.GameLobby;
import exceptions.LobbyNotFoundException;
import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.board.SchoolBoard;
import util.GameState;

public class GameOver {

    public void selectWinner(String code) {
        try {
            GameLobby tempLobby = GameController.getLobby(code);
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

            tempGame.setState(GameState.GAME_OVER);
            // TODO : create a function that broadcast the player indicised by playerId

        } catch (LobbyNotFoundException | PlayerNotFoundException e) {
            e.printStackTrace();
        }
    }
}
