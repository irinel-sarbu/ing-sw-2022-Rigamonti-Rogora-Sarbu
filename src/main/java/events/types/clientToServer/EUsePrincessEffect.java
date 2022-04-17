package events.types.clientToServer;

import util.CharacterType;

public class EUsePrincessEffect extends EUseCharacterEffect {
    private final int studentID;

    public EUsePrincessEffect(int studentID) {
        super(CharacterType.PRINCESS);
        this.studentID = studentID;
    }

    public int getStudentID() {
        return studentID;
    }
}
