package events;

public enum EventType {
    // Handled on server, Sended by client
    CLIENT_TO_SERVER_INFO,

    // Handled on client, Sended by server
    SERVER_ACK,
    
    NETWORK_MESSAGE
}
