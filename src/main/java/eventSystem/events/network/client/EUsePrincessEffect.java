package eventSystem.events.network.client;

import util.CharacterType;

/**
 * Class that represents a client to server message.
 */
public final class EUsePrincessEffect extends EUseCharacterEffect {
    private final int studentID;

    /**
     * Default constructor
     *
     * @param studentID chosen student
     */
    public EUsePrincessEffect(int studentID) {
        super(CharacterType.PRINCESS);
        this.studentID = studentID;
    }

    /**
     * Getter
     *
     * @return student chosen
     */
    public int getStudentID() {
        return studentID;
    }
}
