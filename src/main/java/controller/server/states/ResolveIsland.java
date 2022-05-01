package controller.server.states;

import controller.server.GameLobby;
import exceptions.PlayerNotFoundException;
import exceptions.TowersIsEmptyException;
import model.GameModel;
import model.board.IslandGroup;

public abstract class ResolveIsland {

    /**
     * Calculate influence on an island and substitute towers if needed
     *
     * @param tempLobby     lobby the command is referring to
     * @param islandGroupID ID of the island group to resolve
     */
    public void solveIsland(GameLobby tempLobby, int islandGroupID) {
        // TODO: check if in correct game phase
        try {
            GameModel tempGame = tempLobby.getModel();
            int[] islandSum;
            IslandGroup tempIslandGroup = tempGame.getIslandGroupByID(islandGroupID);
            int playerPosition;

            // calculates the influence of each player and stores it in islandSum in the relative position

            islandSum = checkMostInfluence(tempLobby, tempGame, tempIslandGroup, tempIslandGroup.getTowersColor() != null);

            // check which player has the most influence and, if there is one, changes island's tower to his color
            playerPosition = playerID(islandSum);
            if (playerPosition != -1) {
                tempIslandGroup.setTowersColor(tempGame.getPlayerByID(playerPosition).getColor());
                tempGame.getPlayerByID(playerPosition).getSchoolBoard().removeTower();
            }
            // joins adjacent islandGroups
            tempGame.joinAdjacent(islandGroupID);
        } catch (PlayerNotFoundException | TowersIsEmptyException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    /**
     * Abstract method: check the influence of each player on the specified island group
     *
     * @param tempLobby       current gameLobby
     * @param tempGame        current gameModel
     * @param tempIslandGroup island group to resolve
     * @param computeTowers   // TODO: (should be omitted) specify if towers need to be computed
     * @return a vector containing influence of each player
     * @throws PlayerNotFoundException should never happen
     */
    protected abstract int[] checkMostInfluence(GameLobby tempLobby, GameModel tempGame, IslandGroup tempIslandGroup, boolean computeTowers) throws PlayerNotFoundException;

    // LEGACY: Have no idea what this function does
    // get ID of the player with most influence on the group, using computed island sum
    protected int playerID(int[] islandSum) {
        int max1 = 0, max2 = 0;
        int pos = 0;
        for (int i = 0; i < islandSum.length; i++) {
            if (islandSum[i] > max1) {
                max1 = islandSum[i];
                pos = i;
            }
        }
        islandSum[pos] = 0;
        for (int j : islandSum) {
            if (j > max2) {
                max2 = j;
            }
        }
        if (max1 == max2) {
            return -1;
        } else {
            return pos;
        }
    }

}
