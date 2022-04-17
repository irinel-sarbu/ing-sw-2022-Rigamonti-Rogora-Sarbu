package events.types.clientToServer;

import util.CharacterType;
import util.Color;

public class EUseFanaticEffect extends EUseCharacterEffect {
    private final Color color;

    public EUseFanaticEffect(Color color) {
        super(CharacterType.MUSHROOM_FANATIC);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
