package eventSystem.events.network;

import eventSystem.events.Event;

import java.util.UUID;

public final class EConnectionAccepted extends Event {
    private final UUID clientUUID;

    public EConnectionAccepted(final UUID uuid) {
        this.clientUUID = uuid;
    }

    public UUID getId() {
        return clientUUID;
    }
}
