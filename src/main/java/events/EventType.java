package events;

public enum EventType {
    // Handled on server, Sended by client
    REGISTER,
    DISCONNECT,
    CREATE_GAME,
    JOIN_GAME,

    // Handled on client, Sended by server
    PLAYER_NAME_TAKEN,
    REGISTER_OK,
    SERVER_ACK,
    
    // handled on client, Sended by client
    CONNECT,
    CONNECTION_REFUSED,
    CONNECTION_OK,

    PLAYER_NAME_INSERTED
}
