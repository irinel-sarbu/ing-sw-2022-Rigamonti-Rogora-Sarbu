package controller.server.states;

import controller.server.GameLobby;
import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.board.IslandGroup;

public abstract class ResolveIsland {

    public abstract void solveIsland(GameLobby tempLobby, int islandGroupID);

    protected abstract int[] checkMostInfluence(GameLobby tempLobby, GameModel tempGame, IslandGroup tempIslandGroup, boolean computeTowers) throws PlayerNotFoundException;

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
