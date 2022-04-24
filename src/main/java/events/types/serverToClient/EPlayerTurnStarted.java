package events.types.serverToClient;

import events.Event;
import events.EventType;

public class EPlayerTurnStarted extends Event {
    private final String player;

    public EPlayerTurnStarted(String player) {
        super(EventType.PLAYER_TURN_STARTED);
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "EPlayerTurnStarted { " + getPlayer() + "'s turn started }";
    }
}
