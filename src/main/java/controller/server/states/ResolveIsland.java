package controller.server.states;

import controller.server.GameLobby;
import exceptions.PlayerNotFoundException;
import exceptions.TowersFullException;
import exceptions.TowersIsEmptyException;
import model.GameModel;
import model.Player;
import model.board.IslandGroup;
import model.board.Tower;
import util.TowerColor;

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

            // determine who had the group previously (using tower color)
            TowerColor oldColor = tempIslandGroup.getTowersColor();

            // calculates the influence of each player and stores it in islandSum in the relative position

            islandSum = checkMostInfluence(tempLobby, tempGame, tempIslandGroup);

            // check which player has the most influence and, if there is one, changes island's tower to his color
            playerPosition = playerID(islandSum);

            // determine who has the group now (using tower color) (avoid IndexOutOfBoundsException)
            TowerColor newColor = (playerPosition != -1) ? tempGame.getPlayerByID(playerPosition).getColor() : null;

            // playerPosition == -1 when no player has maximum influence
            if (playerPosition != -1 && (!newColor.equals(oldColor))) {// do this only if the owner has changed

                // get number of islands in the group
                int numOfIslands = tempIslandGroup.getIslands().size();

                // get old and new owner of the group
                Player oldOwner = tempGame.getPlayers().stream().filter(p -> p.getColor().equals(oldColor)).findFirst().orElse(null);
                Player newOwner = tempGame.getPlayerByID(playerPosition);

                // give back towers to the old owner and remove them from the new owner
                for (int i = 0; i < numOfIslands; i++) {
                    // if someone had the group previously give the towers back
                    if (oldOwner != null) {
                        oldOwner.getSchoolBoard().addTower(new Tower(oldOwner.getColor()));
                    }

                    // remove tower from the player who now owns the group
                    newOwner.getSchoolBoard().removeTower();
                }

                // renew group's tower color
                tempIslandGroup.setTowersColor(newColor);

            }
            // joins adjacent islandGroups
            tempGame.joinAdjacent(islandGroupID);
        } catch (PlayerNotFoundException | TowersIsEmptyException e) {
            // TODO : write a line of text that notify the issue
        } catch (TowersFullException e) {
            // Should never happen (if happens an error in the logic occurred)
            System.err.println("Error giving back towers to owner: too much towers");
            throw new RuntimeException("Error giving back towers to owner: too much towers");
        }
    }

    /**
     * Abstract method: check the influence of each player on the specified island group
     *
     * @param tempLobby       current gameLobby
     * @param tempGame        current gameModel
     * @param tempIslandGroup island group to resolve
     * @return a vector containing influence of each player
     * @throws PlayerNotFoundException should never happen
     */
    protected abstract int[] checkMostInfluence(GameLobby tempLobby, GameModel tempGame, IslandGroup tempIslandGroup) throws PlayerNotFoundException;

    // LEGACY: do not edit

    /**
     * Determine the ID of the player with most influence
     *
     * @param islandSum is an array of integers: each values correspond to the influence of each player, in the order of {@link GameModel#getPlayers()}
     * @return the ID of the {@link Player} with the most influence, -1 if no player has the most influence
     */
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
