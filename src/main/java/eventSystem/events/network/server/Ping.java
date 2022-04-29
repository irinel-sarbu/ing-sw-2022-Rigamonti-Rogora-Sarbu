package eventSystem.events.network.server;

import eventSystem.events.Event;

public final class Ping extends Event {
    public Ping() {
        super();
    }

    @Override
    public String toString() {
        return "Ping";
    }
}
