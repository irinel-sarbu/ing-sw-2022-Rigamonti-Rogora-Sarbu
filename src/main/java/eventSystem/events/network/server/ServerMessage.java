package eventSystem.events.network.server;

import eventSystem.events.Event;

/**
 * Class that represents event sent by server
 */
public class ServerMessage extends Event {
    private final String message;

    /**
     * Default constructor
     *
     * @param message to be sent
     */
    public ServerMessage(String message) {
        this.message = message;
    }

    /**
     * Getter
     *
     * @return message
     */
    public String getMsg() {
        return message;
    }

    @Override
    public String toString() {
        return "ServerMessage {  msg: '" + message + "' }";
    }
}
