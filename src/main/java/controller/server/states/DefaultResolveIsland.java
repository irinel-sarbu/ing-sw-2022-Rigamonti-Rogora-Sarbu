package controller.server.states;

import controller.server.GameLobby;
import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.board.IslandGroup;
import model.board.Professor;
import util.CharacterType;

public class DefaultResolveIsland extends ResolveIsland {
    /**
     * Get each player's influence on the island group in default condition
     *
     * @param tempLobby       current gameLobby
     * @param tempGame        current gameModel
     * @param tempIslandGroup island group to resolve
     * @param computeTowers   // TODO: (should be omitted) specify if towers need to be computed
     * @return a vector of all players influence on the island group
     * @throws PlayerNotFoundException should never happens
     */
    @Override
    protected int[] checkMostInfluence(GameLobby tempLobby, GameModel tempGame, IslandGroup tempIslandGroup, boolean computeTowers) throws PlayerNotFoundException {
        int[] islandSum = new int[tempGame.getPlayers().size()];
        for (int i = 0; i < tempGame.getPlayers().size(); i++) {
            for (Professor professor : tempGame.getPlayerByID(i).getSchoolBoard().getProfessors()) {
                islandSum[i] += tempIslandGroup.getStudentsNumber(professor.getColor());
            }
        }
        return islandSum;
    }
}
