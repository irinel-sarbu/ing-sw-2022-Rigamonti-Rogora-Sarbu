package eventSystem.events.network.server;

import eventSystem.events.Event;

public class EPlayerTurnStarted extends Event {
    private final String player;

    public EPlayerTurnStarted(String player) {
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
