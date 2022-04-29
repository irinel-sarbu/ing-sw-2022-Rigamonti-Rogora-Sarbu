package eventSystem.events;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class Event implements Serializable {
    private final UUID uuid;
    private String scope;

    protected Event() {
        this.uuid = UUID.randomUUID();
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
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
}