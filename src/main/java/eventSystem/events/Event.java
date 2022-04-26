package eventSystem.events;

import network.server.ClientSocketConnection;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class Event implements Serializable {
    private final UUID uuid;
    private ClientSocketConnection client;

    protected Event() {
        this.uuid = UUID.randomUUID();
        this.client = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(uuid, event.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    public abstract String getScope();

    public void setClient(ClientSocketConnection client) {
        this.client = client;
    }

    public ClientSocketConnection getClient() {
        return client;
    }
}