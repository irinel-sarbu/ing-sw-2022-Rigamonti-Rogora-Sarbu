package events.types.clientToServer;

import util.CharacterType;
import util.Color;

public class EUseThiefEffect extends EUseCharacterEffect {
    private final Color color;

    public EUseThiefEffect(Color color) {
        super(CharacterType.THIEF);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
