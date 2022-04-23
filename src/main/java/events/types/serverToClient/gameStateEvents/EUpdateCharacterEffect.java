package events.types.serverToClient.gameStateEvents;

import events.Event;
import events.EventType;
import util.CharacterType;

public class EUpdateCharacterEffect extends Event {
    private final CharacterType characterType;

    public EUpdateCharacterEffect(CharacterType characterType) {
        super(EventType.UPDATE_CHARACTER_EFFECT);
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
