package eventSystem.events.network.client;

import util.CharacterType;
import util.Color;

public final class EUseThiefEffect extends EUseCharacterEffect {
    private final Color color;

    public EUseThiefEffect(Color color) {
        super(CharacterType.THIEF);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
