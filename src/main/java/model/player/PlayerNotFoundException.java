package model.player;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String message) {
        super(message);
    }

    public PlayerNotFoundException() {
        super();
    }
}
