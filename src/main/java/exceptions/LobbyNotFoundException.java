package exceptions;

public class LobbyNotFoundException extends Exception{
    public LobbyNotFoundException() {
        super();
    }

    public LobbyNotFoundException(String message) {
        super(message);
    }
}
