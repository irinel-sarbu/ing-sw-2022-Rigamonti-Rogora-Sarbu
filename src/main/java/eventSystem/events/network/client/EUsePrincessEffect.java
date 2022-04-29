package eventSystem.events.network.client;

import util.CharacterType;

public final class EUsePrincessEffect extends EUseCharacterEffect {
    private final int studentID;

    public EUsePrincessEffect(int studentID) {
        super(CharacterType.PRINCESS);
        this.studentID = studentID;
    }

    public int getStudentID() {
        return studentID;
    }
}
