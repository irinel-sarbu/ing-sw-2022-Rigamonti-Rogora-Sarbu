package controller.server.states;


import controller.server.GameController;
import exceptions.LobbyNotFoundException;
import exceptions.PlayerNotFoundException;
import exceptions.StudentNotFoundException;
import model.GameModel;
import model.Player;
import model.board.IslandGroup;
import model.board.Professor;
import model.expert.CharacterCard;
import util.CharacterType;

/**
 * When the client activates a character effect, the event handler will call one of the methods Below.
 * All the information needed will be sent using the parameters.
 * After the effect is activated, effectIsUsed will become TRUE.
 *  TODO : switch effectIsUsed to FALSE at the end of the turn.
 */
public class CharacterEffectHandler {
    public CharacterEffectHandler() {
    }

    public void monkEffect(String code, int studentID, int islandPos) {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.MONK);
            tempCharacter.useEffect();
            tempCharacter.setCost(tempCharacter.getCost() + 1);
            tempGame.getIslandTileByID(islandPos).addStudent(tempCharacter.removeStudent(studentID));
            if (tempGame.getBag().getRemainingStudents() != 0) tempCharacter.addStudent(tempGame.getBag().pull());
        } catch (LobbyNotFoundException | StudentNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    public void farmerEffect(String code) {
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.FARMER);
            tempCharacter.useEffect();
            tempCharacter.setCost(tempCharacter.getCost() + 1);
            // TODO : when calculating to whom the professor goes, add an if that checks if this effect is active
        } catch (LobbyNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }

    public void heraldEffect(String code, int islandGroupID) {
        //herald resolves and island with his own method because the resolve of the actual game has a different
        //implementation, which is too much complicated for this purpose
        try {
            GameModel tempGame = GameController.getLobby(code).getModel();
            int[] islandSum = new int[tempGame.getPlayers().size()];
            CharacterCard tempCharacter = tempGame.getCharacterByType(CharacterType.HERALD);
            IslandGroup tempIslandGroup = tempGame.getIslandGroupByID(islandGroupID);
            tempCharacter.useEffect();
            tempCharacter.setCost(tempCharacter.getCost() + 1);
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

    /*private int[] checkMostInfluenceWithTower(GameModel tempGame, IslandGroup tempIslandGroup) throws PlayerNotFoundException {
        int[] islandSum = new int[tempGame.getPlayers().size()];
        for (int i = 0; i < tempGame.getPlayers().size(); i++) {
            if (tempIslandGroup.getIslandTileByID(0).getTowerColor() == tempGame.getPlayerByID(i).getColor()) {
                islandSum[i]++;
            }
            for (Professor professor : tempGame.getPlayerByID(i).getSchoolBoard().getProfessors()) {
                islandSum[i] += tempIslandGroup.getStudentsNumber(professor.getColor());
            }
        }
        return islandSum;
    }

    private int[] checkMostInfluenceWithoutTower(GameModel tempGame, IslandGroup tempIslandGroup) throws PlayerNotFoundException {
        int[] islandSum = new int[tempGame.getPlayers().size()];
        for (int i = 0; i < tempGame.getPlayers().size(); i++) {
            for (Professor professor : tempGame.getPlayerByID(i).getSchoolBoard().getProfessors()) {
                islandSum[i] += tempIslandGroup.getStudentsNumber(professor.getColor());
            }
        }
        return islandSum;
    }*/

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
