package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;
import util.CharacterType;

/**
 * Class that represents a client to server message.
 */
public class EUseCharacterEffect extends NetworkEvent {
    private final CharacterType characterType;

    /**
     * Default constructor
     *
     * @param characterType CharacterType of used effect
     */
    public EUseCharacterEffect(CharacterType characterType) {
        this.characterType = characterType;
    }

    /**
     * Getter
     *
     * @return CharacterType of used effect
     */
    public CharacterType getCharacterType() {
        return characterType;
    }
}
