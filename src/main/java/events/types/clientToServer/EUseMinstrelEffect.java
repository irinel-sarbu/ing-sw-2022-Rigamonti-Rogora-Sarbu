package events.types.clientToServer;

import util.CharacterType;
import util.Color;

import java.util.ArrayList;
import java.util.List;

public class EUseMinstrelEffect extends EUseCharacterEffect {
    private final List<Integer> entranceStudents;
    private final List<Color> diningStudents;

    public EUseMinstrelEffect(ArrayList<Integer> entranceStudents, ArrayList<Color> diningStudents) {
        super(CharacterType.MINSTREL);
        this.entranceStudents = entranceStudents;
        this.diningStudents = diningStudents;
    }

    public List<Integer> getEntranceStudents() {
        return entranceStudents;
    }

    public List<Color> getDiningStudents() {
        return diningStudents;
    }
}
