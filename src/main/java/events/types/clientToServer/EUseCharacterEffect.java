package events.types.clientToServer;

import events.Event;
import events.EventType;
import util.CharacterType;

public class EUseCharacterEffect extends Event {
    private final CharacterType characterType;

    protected EUseCharacterEffect(CharacterType characterType) {
        super(EventType.USE_CHARACTER_EFFECT);
        this.characterType = characterType;
    }

    public CharacterType getCharacterType() {
        return characterType;
    }
}
