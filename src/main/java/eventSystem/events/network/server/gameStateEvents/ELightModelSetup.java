package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;
import model.expert.CharacterCard;

import java.util.List;

public class ELightModelSetup extends Event {
    private final List<CharacterCard> characterCards;

    public ELightModelSetup(List<CharacterCard> characterCards) {
        this.characterCards = characterCards;
    }

    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    @Override
    public String toString() {
        return "ELightModelSetup { ExtractedCharacters: " + characterCards.toString() + " }";
    }
}
