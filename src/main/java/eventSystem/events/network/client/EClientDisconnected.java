package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;

/**
 * Class that represents a client to server message.
 * Unregistered client
 */
public class EClientDisconnected extends NetworkEvent {
    /**
     * Default constructor
     */
    public EClientDisconnected() {
    }

    @Override
    public String toString() {
        return "EClientDisconnected";
    }
}
