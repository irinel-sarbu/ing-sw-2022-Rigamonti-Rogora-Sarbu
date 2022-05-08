package controller.server.states;

import controller.server.GameLobby;
import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.board.IslandGroup;
import model.board.Professor;
import util.CharacterType;

public class CentaurResolveIsland extends ResolveIsland {

    /**
     * Get each player's influence on the island group when the centaur effect is active (each tower gives the player a +1 of influence to the corresponding player)
     *
     * @param tempLobby       current gameLobby
     * @param tempGame        current gameModel
     * @param tempIslandGroup island group to resolve
     * @param computeTowers   // TODO: (should be omitted) specify if towers need to be computed
     * @return a vector of all players influence on the island group
     * @throws PlayerNotFoundException should never happen
     */
    @Override
    protected int[] checkMostInfluence(GameLobby tempLobby, GameModel tempGame, IslandGroup tempIslandGroup, boolean computeTowers) throws PlayerNotFoundException {
        int[] islandSum = new int[tempGame.getPlayers().size()];
        for (int i = 0; i < tempGame.getPlayers().size(); i++) {
            if (computeTowers && tempIslandGroup.getIslands().get(0).getTowerColor() == tempGame.getPlayerByID(i).getColor()) {
                //passive effect of CENTAUR
                islandSum[i]++;
            }
            for (Professor professor : tempGame.getPlayerByID(i).getSchoolBoard().getProfessors()) {
                islandSum[i] += tempIslandGroup.getStudentsNumber(professor.getColor());
            }
        }
        return islandSum;
    }
}
