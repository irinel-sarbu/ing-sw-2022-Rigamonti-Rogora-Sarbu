package controller.server.states;

import controller.server.GameLobby;
import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.board.IslandGroup;
import model.board.Professor;
import util.CharacterType;

public class MushroomFanaticResolveIsland extends ResolveIsland {

    protected int[] checkMostInfluence(GameLobby tempLobby, GameModel tempGame, IslandGroup tempIslandGroup, boolean computeTowers) throws PlayerNotFoundException {
        int[] islandSum = new int[tempGame.getPlayers().size()];
        for (int i = 0; i < tempGame.getPlayers().size(); i++) {
            for (Professor professor : tempGame.getPlayerByID(i).getSchoolBoard().getProfessors()) {
                //passive effect of MUSHROOM_FANATIC
                if (professor.getColor() != tempGame.getCharacterByType(CharacterType.MUSHROOM_FANATIC).getColor()) {
                    islandSum[i] += tempIslandGroup.getStudentsNumber(professor.getColor());
                }
            }
        }
        return islandSum;
    }
}
