package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;
import model.expert.CharacterCard;

import java.util.List;

/**
 * Class that represents a server to client message.
 */
public class ELightModelSetup extends Event {
    private final List<CharacterCard> characterCards;

    /**
     * Default constructor
     *
     * @param characterCards updated character cards
     */
    public ELightModelSetup(List<CharacterCard> characterCards) {
        this.characterCards = characterCards;
    }

    /**
     * Getter
     *
     * @return list of character cards
     */
    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    @Override
    public String toString() {
        return "ELightModelSetup { ExtractedCharacters: " + characterCards.toString() + " }";
    }
}
