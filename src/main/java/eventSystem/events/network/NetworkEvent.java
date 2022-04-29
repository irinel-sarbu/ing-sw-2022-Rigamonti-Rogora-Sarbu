package eventSystem.events.network;

import eventSystem.events.Event;

import java.util.UUID;

public abstract class NetworkEvent extends Event {
    private UUID clientIdentifier;

    public void setClientId(UUID clientId) {
        this.clientIdentifier = clientId;
    }

    public UUID getClientId() {
        return clientIdentifier;
    }
}
