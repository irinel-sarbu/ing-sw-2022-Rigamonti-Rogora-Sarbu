package eventSystem.events.network.client;

import util.CharacterType;
import util.Color;

/**
 * Class that represents a client to server message.
 */
public final class EUseFanaticEffect extends EUseCharacterEffect {
    private final Color color;

    /**
     * Default constructor
     *
     * @param color chosen color
     */
    public EUseFanaticEffect(Color color) {
        super(CharacterType.MUSHROOM_FANATIC);
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
