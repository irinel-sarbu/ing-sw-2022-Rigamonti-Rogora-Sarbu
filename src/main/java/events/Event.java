package events;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public abstract class Event {
    protected Logger LOGGER = Logger.getLogger(Event.class.getName());

    protected boolean handled;
    private final EventType type;
    private final UUID uuid;

    protected Event(EventType type) {
        this.type = type;
        this.uuid = UUID.randomUUID();
    }

    public EventType getType() {
        return type;
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
