package events.types.clientToServer;

import util.CharacterType;

import java.util.ArrayList;
import java.util.List;


public class EUseJesterEffect extends EUseCharacterEffect {
    private final List<Integer> entranceStudents, jesterStudents;

    public EUseJesterEffect(ArrayList<Integer> entranceStudents, ArrayList<Integer> jesterStudents) {
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
