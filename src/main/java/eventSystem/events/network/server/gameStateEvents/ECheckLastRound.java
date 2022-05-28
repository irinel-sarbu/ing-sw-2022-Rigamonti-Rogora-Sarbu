package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;

public class ECheckLastRound extends Event {
    public final Boolean lastRound;

    public ECheckLastRound(Boolean lastRound) {
        this.lastRound = lastRound;
    }

    public Boolean getLastRound() {
        return lastRound;
    }

    @Override
    public String toString() {
        return "ECheckLastRound { lastRound=" + lastRound + " }";
    }
}
