package events.types.serverToClient.gameStateEvents;

import events.Event;
import events.EventType;
import model.board.Assistant;

import java.util.List;

public class EUpdateAssistantDeck extends Event {
    private final List<Assistant> assistants;

    public EUpdateAssistantDeck(List<Assistant> assistants) {
        super(EventType.UPDATE_ASSISTANT_DECK);
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
