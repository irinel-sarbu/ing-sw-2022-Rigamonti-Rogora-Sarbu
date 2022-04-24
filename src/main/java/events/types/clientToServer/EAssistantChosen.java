package events.types.clientToServer;

import events.Event;
import events.EventType;
import model.board.Assistant;

public class EAssistantChosen extends Event {
    private final Assistant assistant;

    public EAssistantChosen(Assistant assistant) {
        super(EventType.ASSISTANT_CHOSEN);
        this.assistant = assistant;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    @Override
    public String toString() {
        return "EAssistantChosen { assistant: " + assistant + " }";
    }
}