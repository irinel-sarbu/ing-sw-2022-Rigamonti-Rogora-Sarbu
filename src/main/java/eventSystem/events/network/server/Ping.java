package eventSystem.events.network.server;

import eventSystem.events.Event;

public final class Ping extends Event {
    public Ping() {
    }

    @Override
    public String toString() {
        return "Ping";
    }
}
