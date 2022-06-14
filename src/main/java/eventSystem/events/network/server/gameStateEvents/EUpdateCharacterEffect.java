package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;
import util.CharacterType;

/**
 * Class that represents a server to client message.
 */
public class EUpdateCharacterEffect extends Event {
    private final CharacterType characterType;

    /**
     * Default constructor
     *
     * @param characterType character effect
     */
    public EUpdateCharacterEffect(CharacterType characterType) {
        this.characterType = characterType;
    }

    /**
     * Getter
     *
     * @return character effet
     */
    public CharacterType getCharacterType() {
        return characterType;
    }

    @Override
    public String toString() {

        if (characterType != null) {
            return "EUpdateCharacterEffect { ActiveCharcater: " + characterType.toString() + " }";
        } else {
            return "EUpdateCharacterEffect { ActiveCharcater: none }";
        }
    }
}
