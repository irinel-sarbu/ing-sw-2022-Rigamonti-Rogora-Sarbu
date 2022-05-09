package eventSystem.events.network.server;

import eventSystem.events.Event;

public class ServerMessage extends Event {
    private final String message;

    public ServerMessage(String message) {
        this.message = message;
    }

    public String getMsg() {
        return message;
    }

    @Override
    public String toString() {
        return "ServerMessage {  msg: '" + message + "' }";
    }
}
