package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;
import util.CharacterType;

public class EUseCharacterEffect extends NetworkEvent {
    private final CharacterType characterType;

    public EUseCharacterEffect(CharacterType characterType) {
        this.characterType = characterType;
    }

    public CharacterType getCharacterType() {
        return characterType;
    }
}
