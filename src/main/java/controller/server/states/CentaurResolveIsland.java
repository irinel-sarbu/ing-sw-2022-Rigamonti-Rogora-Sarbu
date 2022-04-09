package controller.server.states;

import controller.server.GameLobby;
import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.board.IslandGroup;
import model.board.Professor;
import util.CharacterType;

public class CentaurResolveIsland extends ResolveIsland {

    protected int[] checkMostInfluence(GameLobby tempLobby, GameModel tempGame, IslandGroup tempIslandGroup, boolean computeTowers) throws PlayerNotFoundException {
        int[] islandSum = new int[tempGame.getPlayers().size()];
        for (int i = 0; i < tempGame.getPlayers().size(); i++) {
            if (computeTowers && tempIslandGroup.getIslandTileByID(0).getTowerColor() == tempGame.getPlayerByID(i).getColor()) {
                //checks for passive effect of CENTAUR
                if (tempLobby.getModel().getCharacterByType(CharacterType.CENTAUR) == null || !tempLobby.getModel().getCharacterByType(CharacterType.CENTAUR).getEffect()) {
                    islandSum[i]++;
                }
            }
            for (Professor professor : tempGame.getPlayerByID(i).getSchoolBoard().getProfessors()) {
                islandSum[i] += tempIslandGroup.getStudentsNumber(professor.getColor());
            }
        }
        return islandSum;
    }
}