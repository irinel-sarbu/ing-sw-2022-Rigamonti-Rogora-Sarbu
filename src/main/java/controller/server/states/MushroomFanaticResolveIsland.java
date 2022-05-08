package controller.server.states;

import controller.server.GameLobby;
import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.board.IslandGroup;
import model.board.Professor;
import util.CharacterType;

public class MushroomFanaticResolveIsland extends ResolveIsland {
    /**
     * Check each player's influence of the specified island group when the mushroom fanatic's effect is active (ignore selected color)
     *
     * @param tempLobby       current gameLobby
     * @param tempGame        current gameModel
     * @param tempIslandGroup island group to resolve
     * @return a vector containing all players' influence on the specified island group
     * @throws PlayerNotFoundException should never happen
     */
    @Override
    protected int[] checkMostInfluence(GameLobby tempLobby, GameModel tempGame, IslandGroup tempIslandGroup) throws PlayerNotFoundException {
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
