package eventSystem.events.network.client;

import util.CharacterType;
import util.Color;

public final class EUseFanaticEffect extends EUseCharacterEffect {
    private final Color color;

    public EUseFanaticEffect(Color color) {
        super(CharacterType.MUSHROOM_FANATIC);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
