package eventSystem.events.network.server;

import eventSystem.events.Event;

/**
 * Class that represents a ping by the server directed to client.
 * Used to check connection.
 */
public final class Ping extends Event {

    /**
     * Default constructor
     */
    public Ping() {
    }

    @Override
    public String toString() {
        return "Ping";
    }
}
