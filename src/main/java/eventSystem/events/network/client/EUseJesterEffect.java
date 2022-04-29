package eventSystem.events.network.client;

import util.CharacterType;

import java.util.ArrayList;
import java.util.List;


public final class EUseJesterEffect extends EUseCharacterEffect {
    private final List<Integer> entranceStudents, jesterStudents;

    public EUseJesterEffect(List<Integer> entranceStudents, List<Integer> jesterStudents) {
        super(CharacterType.JESTER);
        this.entranceStudents = entranceStudents;
        this.jesterStudents = jesterStudents;
    }

    public List<Integer> getEntranceStudents() {
        return entranceStudents;
    }

    public List<Integer> getJesterStudents() {
        return jesterStudents;
    }
}
