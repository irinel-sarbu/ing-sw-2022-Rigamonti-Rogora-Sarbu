package eventSystem.events.network.client;

import util.CharacterType;
import util.Color;

/**
 * Class that represents a client to server message.
 */
public final class EUseThiefEffect extends EUseCharacterEffect {
    private final Color color;

    /**
     * Default constructor
     *
     * @param color chosen color
     */
    public EUseThiefEffect(Color color) {
        super(CharacterType.THIEF);
        this.color = color;
    }

    /**
     * Getter
     *
     * @return chosen color
     */
    public Color getColor() {
        return color;
    }
}
