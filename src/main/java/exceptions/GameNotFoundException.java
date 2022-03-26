package exceptions;

public class GameNotFoundException extends Exception{
    public GameNotFoundException() {
        super();
    }

    public GameNotFoundException(String message) {
        super(message);
    }
}
