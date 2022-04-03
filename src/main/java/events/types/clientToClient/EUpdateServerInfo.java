package events.types.clientToClient;

import events.Event;
import events.EventType;

public class EUpdateServerInfo extends Event {
    private final String ip;
    private final int port;

    public EUpdateServerInfo(String ip, int port) {
        super(EventType.UPDATE_SERVER_INFO);
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
