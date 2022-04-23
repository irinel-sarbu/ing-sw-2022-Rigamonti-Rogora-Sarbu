package events.types.serverToClient.gameStateEvents;

import events.Event;
import events.EventType;
import model.expert.CharacterCard;

import java.util.List;

public class ELightModelSetup extends Event {
    private final List<CharacterCard> characterCards;

    public ELightModelSetup(List<CharacterCard> characterCards) {
        super(EventType.LIGHT_MODEL_SETUP);
        this.characterCards = characterCards;
    }

    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    @Override
    public String toString() {
        return "ELightModelSetup { ExtractedCharacters: " + characterCards.toString() + "}";
    }
}
