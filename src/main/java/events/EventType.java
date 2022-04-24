package events;

public enum EventType {
    // Handled on server, Sent by client
    CLIENT_DISCONNECT,
    CREATE_LOBBY_REQUEST,
    JOIN_LOBBY_REQUEST,
    WIZARD_CHOSEN,
    ASSISTANT_CHOSEN,
    PLAYER_CHOOSING,

    // Handled on client, Sent by server
    LOBBY_JOINED,
    PLAYER_JOINED,
    PLAYER_DISCONNECTED,

    CHOOSE_WIZARD,
    WIZARD_NOT_AVAILABLE,
    USE_CHARACTER_EFFECT,

    LIGHT_MODEL_SETUP,
    UPDATE_SCHOOLBOARD,
    UPDATE_CLOUD_TILES,
    UPDATE_ISLANDS,
    UPDATE_CHARACTER_EFFECT,

    UPDATE_ASSISTANT_DECK,
    PLAYER_CHOSE_ASSISTANT,

    // handled on client, Sent by client
    UPDATE_SERVER_INFO,

    // other
    PING,
    MESSAGE
}
