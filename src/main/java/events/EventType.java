package events;

public enum EventType {
    // Handled on server, Sended by client
    CLIENT_DISCONNECT,
    CREATE_LOBBY,
    JOIN_LOBBY,

    // Handled on client, Sended by server
    PLAYER_NAME_TAKEN,
    LOBBY_FULL,
    LOBBY_CREATED,
    LOBBY_JOINED,
    LOBBY_NOT_FOUND,
    PLAYER_JOINED,
    PLAYER_DISCONNECTED,

    // Sended by server, not to be handled
    PING,
    
    // handled on client, Sended by client
    CONNECT,
    CONNECTION_REFUSED,
    CONNECTION_OK
}
