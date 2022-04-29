package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;
import util.CharacterType;

public class EUpdateCharacterEffect extends Event {
    private final CharacterType characterType;

    public EUpdateCharacterEffect(CharacterType characterType) {
        super();
        this.characterType = characterType;
    }

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
