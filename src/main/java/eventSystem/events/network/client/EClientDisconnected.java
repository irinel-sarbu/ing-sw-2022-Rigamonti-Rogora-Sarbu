package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;

public class EClientDisconnected extends NetworkEvent {
    public EClientDisconnected() {
    }

    @Override
    public String toString() {
        return "EClientDisconnected";
    }
}
