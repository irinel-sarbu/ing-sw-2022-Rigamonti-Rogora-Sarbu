package events;

public enum EventType {
    // Handled on server, Sended by client
    REGISTER,
    DISCONNECT,
    CREATE_GAME,
    JOIN_GAME,

    // Handled on client, Sended by server
    SERVER_ACK,
    
    NETWORK_MESSAGE
}
