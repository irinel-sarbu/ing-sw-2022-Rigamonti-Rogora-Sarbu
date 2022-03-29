package controller.server.states;


import controller.server.GameController;
import exceptions.LobbyNotFoundException;
import exceptions.StudentNotFoundException;
import model.GameModel;
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
            // TODO : fix islandPos and handling of islandTileReference. Fix static int count in all classes
            tempGame.getIslandTileByID(islandPos).addStudent(tempCharacter.removeStudent(studentID));
            if (tempGame.getBag().getRemainingStudents() != 0) tempCharacter.addStudent(tempGame.getBag().pull());
        } catch (LobbyNotFoundException | StudentNotFoundException e) {
            // TODO : write a line of text that notify the issue
        }
    }
}
