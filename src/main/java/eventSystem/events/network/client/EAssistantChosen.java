package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;
import model.board.Assistant;

public final class EAssistantChosen extends NetworkEvent {
    private final Assistant assistant;

    public EAssistantChosen(Assistant assistant) {
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