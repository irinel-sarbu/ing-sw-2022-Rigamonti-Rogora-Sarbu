package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;
import model.board.Assistant;

import java.util.List;

public class EUpdateAssistantDeck extends Event {
    private final List<Assistant> assistants;

    public EUpdateAssistantDeck(List<Assistant> assistants) {
        super();
        this.assistants = assistants;
    }

    public List<Assistant> getAssistants() {
        return assistants;
    }

    @Override
    public String toString() {
        return "EUpdateAssistantDeck { Assistants: " + assistants.toString() + " }";
    }
}
