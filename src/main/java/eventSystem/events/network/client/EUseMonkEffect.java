package eventSystem.events.network.client;

import util.CharacterType;

/**
 * Class that represents a client to server message.
 */
public final class EUseMonkEffect extends EUseCharacterEffect {
    private final int studentID, islandPos;

    /**
     * Default constructor
     *
     * @param studentID student chosen
     * @param islandPos island position
     */
    public EUseMonkEffect(int studentID, int islandPos) {
        super(CharacterType.MONK);
        this.studentID = studentID;
        this.islandPos = islandPos;
    }

    /**
     * Getter
     *
     * @return chosen student id
     */
    public int getStudentID() {
        return studentID;
    }

    /**
     * Getter
     *
     * @return island position
     */
    public int getIslandPos() {
        return islandPos;
    }
}
