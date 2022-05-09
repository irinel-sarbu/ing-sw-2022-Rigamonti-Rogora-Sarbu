package eventSystem.events.network.server.gameStateEvents;

import eventSystem.events.Event;

public class EDeclareWinner extends Event {
    public final String player;

    public EDeclareWinner(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public String toString() {
        return "EDeclareWinner { player " + getPlayer() + " is the winner }";
    }
}
