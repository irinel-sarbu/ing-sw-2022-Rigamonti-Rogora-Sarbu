package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;
import model.board.Assistant;

/**
 * Class that represents a client to server message.
 */
public final class EAssistantChosen extends NetworkEvent {
    private final Assistant assistant;

    /**
     * Default constructor
     *
     * @param assistant chosen assistant
     */
    public EAssistantChosen(Assistant assistant) {
        this.assistant = assistant;
    }

    /**
     * Getter
     *
     * @return assistant chosen
     */
    public Assistant getAssistant() {
        return assistant;
    }

    @Override
    public String toString() {
        return "EAssistantChosen { assistant: " + assistant + " }";
    }
}