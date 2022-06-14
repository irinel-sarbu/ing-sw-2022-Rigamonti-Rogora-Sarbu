package eventSystem.events.network.client;

import util.CharacterType;
import util.Color;

import java.util.List;

/**
 * Class that represents a client to server message.
 */
public final class EUseMinstrelEffect extends EUseCharacterEffect {
    private final List<Integer> entranceStudents;
    private final List<Color> diningStudents;

    /**
     * Default constructor
     *
     * @param entranceStudents students to move from entrance to dining
     * @param diningStudents   students to move from dining to students
     */
    public EUseMinstrelEffect(List<Integer> entranceStudents, List<Color> diningStudents) {
        super(CharacterType.MINSTREL);
        this.entranceStudents = entranceStudents;
        this.diningStudents = diningStudents;
    }

    /**
     * Getter
     *
     * @return List of entrance students to be moved
     */
    public List<Integer> getEntranceStudents() {
        return entranceStudents;
    }

    /**
     * Getter
     *
     * @return List of dining students to be moved
     */
    public List<Color> getDiningStudents() {
        return diningStudents;
    }
}
