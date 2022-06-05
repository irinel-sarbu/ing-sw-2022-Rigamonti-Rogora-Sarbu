package eventSystem.events.network.client;

import util.CharacterType;

import java.util.List;

/**
 * Class that represents a client to server message.
 */
public final class EUseJesterEffect extends EUseCharacterEffect {
    private final List<Integer> entranceStudents, jesterStudents;

    /**
     * Default constructor
     *
     * @param entranceStudents students selected from entrance
     * @param jesterStudents   students selected from card
     */
    public EUseJesterEffect(List<Integer> entranceStudents, List<Integer> jesterStudents) {
        super(CharacterType.JESTER);
        this.entranceStudents = entranceStudents;
        this.jesterStudents = jesterStudents;
    }

    /**
     * Getter
     *
     * @return list of selected entrance students
     */
    public List<Integer> getEntranceStudents() {
        return entranceStudents;
    }

    /**
     * Getter
     *
     * @return list of selected jester students
     */
    public List<Integer> getJesterStudents() {
        return jesterStudents;
    }
}
