package eventSystem.events.network.server;

import eventSystem.events.Event;
import model.board.Assistant;

/**
 * Class that represents a server to client message.
 */
public class EPlayerChoseAssistant extends Event {
    private final String player;
    private final Assistant assistant;

    /**
     * Default constructor
     *
     * @param player    that chose the assistant
     * @param assistant chosen
     */
    public EPlayerChoseAssistant(String player, Assistant assistant) {
        this.player = player;
        this.assistant = assistant;
    }

    /**
     * Getter
     *
     * @return player that chose assistant
     */
    public String getPlayer() {
        return player;
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
        return "EPlayerChoseAssistant { Player: " + getPlayer() + ", Assistant: " + getAssistant() + "}";
    }
}
