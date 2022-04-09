package controller.server.states;

import controller.server.GameLobby;
import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.board.IslandGroup;
import model.board.Professor;
import util.CharacterType;

public class TmpResolveIsland {

    public void solveIsland(GameLobby tempLobby, int islandGroupID) {
        try {
            GameModel tempGame = tempLobby.getModel();
            int[] islandSum;
            IslandGroup tempIslandGroup = tempGame.getIslandGroupByID(islandGroupID);
            int playerPosition;

            // calculates the influence of each player and stores it in islandSum in the relative position

            islandSum = checkMostInfluence(tempLobby, tempGame, tempIslandGroup, tempIslandGroup.getIslandTileByID(0).getHasTower());

            // check which player has the most influence and, if there is one, changes island's tower to his color
            playerPosition = playerId(islandSum);
            if (playerPosition != -1) {
                tempIslandGroup.setTowersColor(tempGame.getPlayerByID(playerPosition).getColor());
            }
            // joins adjacent islandGroups
            tempGame.joinAdjacent(islandGroupID);
        } catch (PlayerNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    private int[] checkMostInfluence(GameLobby tempLobby, GameModel tempGame, IslandGroup tempIslandGroup, boolean computeTowers) throws PlayerNotFoundException {
        int[] islandSum = new int[tempGame.getPlayers().size()];
        for (int i = 0; i < tempGame.getPlayers().size(); i++) {
            if (computeTowers && tempIslandGroup.getIslandTileByID(0).getTowerColor() == tempGame.getPlayerByID(i).getColor()) {
                //checks for passive effect of CENTAUR
                if (tempLobby.getModel().getCharacterByType(CharacterType.CENTAUR) == null || !tempLobby.getModel().getCharacterByType(CharacterType.CENTAUR).getEffect()) {
                    islandSum[i]++;
                }
            }
            for (Professor professor : tempGame.getPlayerByID(i).getSchoolBoard().getProfessors()) {
                //checks for passive effect of MUSHROOM_FANATIC
                if (tempLobby.getModel().getCharacterByType(CharacterType.MUSHROOM_FANATIC) != null && tempLobby.getModel().getCharacterByType(CharacterType.MUSHROOM_FANATIC).getEffect()) {
                    if (professor.getColor() != tempGame.getCharacterByType(CharacterType.MUSHROOM_FANATIC).getColor()) {
                        islandSum[i] += tempIslandGroup.getStudentsNumber(professor.getColor());
                    }
                } else {
                    islandSum[i] += tempIslandGroup.getStudentsNumber(professor.getColor());
                }

            }
        }
        //checks for passive effect of KNIGHT
        if (tempLobby.getModel().getCharacterByType(CharacterType.KNIGHT) != null && tempLobby.getModel().getCharacterByType(CharacterType.KNIGHT).getEffect()) {
            islandSum[tempGame.getPlayerId(tempLobby.getCurrentPlayer())] += 2;
        }

        return islandSum;
    }

    private int playerId(int[] islandSum) {
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
