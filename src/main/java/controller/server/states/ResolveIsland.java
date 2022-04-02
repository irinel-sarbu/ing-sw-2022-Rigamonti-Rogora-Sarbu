package controller.server.states;

import controller.server.GameController;
import exceptions.LobbyNotFoundException;
import exceptions.PlayerNotFoundException;
import model.GameModel;
import model.board.IslandGroup;
import model.board.Professor;

public class ResolveIsland {

    public void solveIsland(String code, int islandGroupID){
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            int[] islandSum;
            IslandGroup tempIslandGroup = tempGame.getIslandGroupByID(islandGroupID);
            int playerPosition;

            // calculates the influence of each player and stores it in islandSum in the relative position

            islandSum = checkMostInfluence(tempGame, tempIslandGroup, tempIslandGroup.getIslandTileByID(0).getHasTower());

            // check which player has the most influence and, if there is one, changes island's tower to his color
            playerPosition = playerId(islandSum);
            if (playerPosition != -1) {
                tempIslandGroup.setTowersColor(tempGame.getPlayerByID(playerPosition).getColor());
            }
            // joins adjacent islandGroups
            tempGame.joinAdiacent();
        } catch (LobbyNotFoundException | PlayerNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    private int[] checkMostInfluence(GameModel tempGame, IslandGroup tempIslandGroup, boolean computeTowers) throws PlayerNotFoundException {
        int[] islandSum = new int[tempGame.getPlayers().size()];
        for (int i = 0; i < tempGame.getPlayers().size(); i++) {
            if (computeTowers && tempIslandGroup.getIslandTileByID(0).getTowerColor() == tempGame.getPlayerByID(i).getColor()) {
                islandSum[i]++;
            }
            for (Professor professor : tempGame.getPlayerByID(i).getSchoolBoard().getProfessors()) {
                islandSum[i] += tempIslandGroup.getStudentsNumber(professor.getColor());
            }
        }
        return islandSum;
    }

    private int playerId(int[] islandSum){
        int max1 = 0, max2 = 0;
        int pos = 0;
        for(int i=0; i<islandSum.length; i++){
            if(islandSum[i]>max1){
                max1=islandSum[i];
                pos=i;
            }
        }
        islandSum[pos]=0;
        for(int i=0; i<islandSum.length; i++){
            if(islandSum[i]>max2){
                max2=islandSum[i];
            }
        }
        if(max1==max2){
            return -1;
        }
        else{
            return pos;
        }
    }
}
