package eventSystem.events.network.client;

import eventSystem.events.network.NetworkEvent;

public final class ClientMessage extends NetworkEvent {
    private final String message;

    public ClientMessage(String message) {
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
