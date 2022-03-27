package events.types.clientToClient;

import events.Event;
import events.EventType;

public class ConnectEvent extends Event {
    private final String ip;
    private final int port;

    public ConnectEvent(String ip, int port) {
        super(EventType.CONNECT);
        this.ip = ip.strip();
        this.port = port;
    }

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
