package controller.server.states;

import controller.server.GameLobby;
import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.board.IslandGroup;
import model.board.Professor;
import util.CharacterType;

public class KnightResolveIsland extends ResolveIsland {

    protected int[] checkMostInfluence(GameLobby tempLobby, GameModel tempGame, IslandGroup tempIslandGroup, boolean computeTowers) throws PlayerNotFoundException {
        int[] islandSum = new int[tempGame.getPlayers().size()];
        for (int i = 0; i < tempGame.getPlayers().size(); i++) {
            for (Professor professor : tempGame.getPlayerByID(i).getSchoolBoard().getProfessors()) {
                islandSum[i] += tempIslandGroup.getStudentsNumber(professor.getColor());
            }
        }
        //checks for passive effect of KNIGHT
        if (tempLobby.getModel().getCharacterByType(CharacterType.KNIGHT) != null && tempLobby.getModel().getCharacterByType(CharacterType.KNIGHT).getEffect()) {
            islandSum[tempGame.getPlayerId(tempLobby.getCurrentPlayer())] += 2;
        }

        return islandSum;
    }
}
