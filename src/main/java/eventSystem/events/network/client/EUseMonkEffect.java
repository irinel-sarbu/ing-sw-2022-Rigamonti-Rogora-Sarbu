package eventSystem.events.network.client;

import util.CharacterType;

public final class EUseMonkEffect extends EUseCharacterEffect {
    private final int studentID, islandPos;

    public EUseMonkEffect(int studentID, int islandPos) {
        super(CharacterType.MONK);
        this.studentID = studentID;
        this.islandPos = islandPos;
    }

    public int getStudentID() {
        return studentID;
    }

    public int getIslandPos() {
        return islandPos;
    }
}
