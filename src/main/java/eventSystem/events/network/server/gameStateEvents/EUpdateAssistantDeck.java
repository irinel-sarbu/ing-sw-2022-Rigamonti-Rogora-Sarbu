package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;
import model.board.Assistant;

import java.util.List;

/**
 * Class that represents a server to client message.
 */
public class EUpdateAssistantDeck extends Event {
    private final List<Assistant> assistants;

    /**
     * Default constructor
     *
     * @param assistants updated assistant list
     */
    public EUpdateAssistantDeck(List<Assistant> assistants) {
        this.assistants = assistants;
    }

    /**
     * Getter
     *
     * @return list of assistants
     */
    public List<Assistant> getAssistants() {
        return assistants;
    }

    @Override
    public String toString() {
        return "EUpdateAssistantDeck { Assistants: " + assistants.toString() + " }";
    }
}
