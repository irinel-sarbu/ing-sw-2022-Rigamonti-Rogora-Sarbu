package events.types.serverToClient;

import events.Event;
import events.EventType;
import model.board.Assistant;

public class EPlayerChoseAssistant extends Event {
    private final String player;
    private final Assistant assistant;

    public EPlayerChoseAssistant(String player, Assistant assistant) {
        super(EventType.PLAYER_CHOSE_ASSISTANT);
        this.player = player;
        this.assistant = assistant;
    }

    public String getPlayer() {
        return player;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    @Override
    public String toString() {
        return "EPlayerChoseAssistant { Player: " + getPlayer() + ", Assistant: " + getAssistant() + "}";
    }
}
