package eventSystem.events;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Abstract event class used to notify events
 */
public abstract class Event implements Serializable {
    private final UUID uuid;
    private String scope;

    /**
     * Default constructor
     */
    protected Event() {
        this.uuid = UUID.randomUUID();
    }

    /**
     * Set event scope
     *
     * @param scope filter
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Get event scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * Override for equals.
     *
     * @return <code>True</code> if this == o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(uuid, event.uuid);
    }

    /**
     * Override hashCode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}