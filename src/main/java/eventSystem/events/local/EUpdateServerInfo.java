package eventSystem.events.local;

public class EUpdateServerInfo extends LocalEvent {
    private final String ip;
    private final int port;

    public EUpdateServerInfo(String ip, int port) {
        this.ip = ip.strip();
        this.port = port;
    }

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "EUpdateServerInfo { ip: '" + ip + "'; port: " + port + " }";
    }
}
