package events.types.clientToServer;

import events.Event;
import events.EventType;

public class JoinGameEvent extends Event {
    private final String code;

    public JoinGameEvent(String gameCode) {
        super(EventType.JOIN_GAME);
        this.code = gameCode.strip();
    }

    public String getCode() {
        return code;
    }
}